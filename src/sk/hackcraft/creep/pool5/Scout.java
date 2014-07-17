package sk.hackcraft.creep.pool5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.Pickers;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public abstract class Scout implements Updateable, Drawable
{
	private final JNIBWAPI bwapi;

	private final Position initialScoutPosition;

	private final List<Position> positions = new ArrayList<>();

	private final Map<Unit, Position> scoutUnits = new HashMap<>();

	public Position enemyPosition;

	public Scout(JNIBWAPI bwapi, Position initialScoutPosition, Set<Position> scoutPositions)
	{
		this.bwapi = bwapi;
		this.initialScoutPosition = initialScoutPosition;

		positions.addAll(scoutPositions);

		Comparator<Position> comparator = (Position p1, Position p2) -> {
			return p1.getApproxPDistance(initialScoutPosition) - p2.getApproxPDistance(initialScoutPosition);
		};
		Collections.sort(positions, comparator);
	}

	public int getScoutPositionsCount()
	{
		return positions.size();
	}

	public void addDrone(Unit drone)
	{
		drone.stop(false);

		Position scoutPosition;
		if (positions.size() > 1)
		{
			scoutPosition = positions.get(1);
		}
		else
		{
			scoutPosition = positions.get(0);
		}

		scoutUnits.put(drone, scoutPosition);
	}

	public void addOverlord(Unit overlord)
	{
		overlord.stop(false);
		scoutUnits.put(overlord, positions.get(0));
	}

	protected abstract void enemyPositionFound(Position position);
	protected abstract void droneReturned(Unit unit);

	@Override
	public void update()
	{
		Iterator<Map.Entry<Unit, Position>> it = scoutUnits.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Unit, Position> entry = it.next();
			
			Unit u = entry.getKey();
			Position p = entry.getValue();
			
			if (!u.isExists())
			{
				it.remove();
				continue;
			}

			if (enemyPosition == null)
			{
				UnitSet enemyBuildings = new UnitSet(bwapi.getEnemyUnits()).whereLessOrEqual(new DistanceSelector(Convert.toPositionVector(p)), 500).where(UnitSelector.IS_BUILDING);
				
				if (bwapi.isVisible(p))
				{
					if (!enemyBuildings.isEmpty())
					{
						setEnemyPosition(p);
					}
					else
					{
						positions.remove(p);

						if (positions.size() == 1 && enemyPosition == null)
						{
							setEnemyPosition(positions.iterator().next());
						}
						else
						{
							for (Position p2 : positions)
							{
								if (!scoutUnits.containsValue(p2))
								{
									u.stop(false);
									scoutUnits.put(u, p2);
								}
							}
						}
					}

					continue;
				}
				
				if (u.isIdle())
				{
					u.move(p, false);
				}
			}
			else
			{
				if (u.getType() == UnitTypes.Zerg_Drone)
				{
					droneReturned(u);
					it.remove();
				}
				
				// TODO add some dancing for overlord
				if (u.isIdle())
				{
					u.move(p, false);
				}
			}
		}
	}
	
	private void setEnemyPosition(Position position)
	{
		enemyPosition = position;
		
		Iterator<Map.Entry<Unit, Position>> it = scoutUnits.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Unit, Position> entry = it.next();
			
			Unit u = entry.getKey();

			u.stop(false);

			entry.setValue(position);
		}
		
		enemyPositionFound(position);
	}

	@Override
	public void draw(Graphics graphics)
	{
		graphics.setGameCoordinates();

		for (Map.Entry<Unit, Position> entry : scoutUnits.entrySet())
		{
			Unit u = entry.getKey();
			Position p = entry.getValue();

			if (p == null)
			{
				graphics.setColor(BWColor.Red);
				graphics.drawCircle(u, 10);
			}
			else
			{
				Vector2D from = Convert.toPositionVector(u.getPosition());
				Vector2D to = Convert.toPositionVector(p);

				graphics.setColor(BWColor.Yellow);
				graphics.drawLine(from, to);
			}
		}

		graphics.setScreenCoordinates();

		graphics.drawText(new Vector2D(10, 20), (enemyPosition == null) ? "unknown" : enemyPosition);
	}
}
