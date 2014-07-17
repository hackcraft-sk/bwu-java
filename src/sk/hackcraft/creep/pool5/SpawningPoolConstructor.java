package sk.hackcraft.creep.pool5;

import java.util.Random;

import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.EnvironmentTime;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.grid.Bounds;
import sk.hackcraft.bwu.grid.GameLayerFactory;
import sk.hackcraft.bwu.grid.Grid;
import sk.hackcraft.bwu.grid.GridDimension;
import sk.hackcraft.bwu.grid.GridPoint;
import sk.hackcraft.bwu.grid.grids.SparseLayer;
import sk.hackcraft.bwu.grid.visualization.LayerColorDrawable;
import sk.hackcraft.bwu.grid.visualization.LayerDrawable;
import sk.hackcraft.bwu.selection.Pickers;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.Position.PosType;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;

public class SpawningPoolConstructor implements Updateable, Drawable
{
	private final Game game;
	private final Position referencePosition;
	private final EnvironmentTime time;

	private final Grid buildableGrid;

	private Unit drone;
	private Position buildPosition;

	private final LayerDrawable buildableGridDrawable;

	public SpawningPoolConstructor(Game game, Position referencePosition, EnvironmentTime time)
	{
		this.game = game;
		this.referencePosition = referencePosition;
		this.time = time;

		buildableGrid = GameLayerFactory.createBuildableLayer(game.getMap());

		buildableGridDrawable = LayerColorDrawable.createBoolean(buildableGrid, 32);

		int bx = referencePosition.getBX() - 10;
		int by = referencePosition.getBY() - 10;
		int bw = 20;
		int bh = 20;
		GridPoint boundsPosition = new GridPoint(bx, by);
		GridDimension boundsDimension = new GridDimension(bw, bh);
		Bounds buildableGridBounds = new Bounds(boundsPosition, boundsDimension);

		buildableGridDrawable.setBounds(buildableGridBounds);
	}

	public void construct(Unit drone)
	{
		this.drone = drone;
		drone.stop(false);
	}
	
	public boolean isConstructing()
	{
		return drone != null && drone.isExists();
	}

	@Override
	public void update()
	{
		updateBuildableGrid();

		if (drone != null)
		{
			if (buildPosition == null)
			{
				findBuildPosition();
			}
			else
			{
				if (!drone.isConstructing())
				{
					boolean result = drone.build(buildPosition, UnitTypes.Zerg_Spawning_Pool);
					
					if (!result)
					{
						drone.move(buildPosition, false);
					}
				}
			}
		}
	}

	private void updateBuildableGrid()
	{
		UnitSet units = game.getAllUnits();
		for (Unit unit : units)
		{
			UnitType type = unit.getType();

			if (type.isBuilding())
			{
				for (int i = 0; i < type.getTileWidth(); i++)
				{
					for (int j = 0; j < type.getTileHeight(); j++)
					{
						Position p = unit.getTilePosition();
						int x = p.getBX() + i;
						int y = p.getBY() + j;

						buildableGrid.set(x, y, 0);
					}
				}
			}
			else if (!type.isFlyer())
			{
				Position p = unit.getPosition();
				int x = p.getBX();
				int y = p.getBY();

				buildableGrid.set(x, y, 0);
			}
		}
	}

	private void findBuildPosition()
	{
		int x = referencePosition.getBX() - 10;
		int y = referencePosition.getBY() - 10;

		double nearestDistance = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < 20; i++)
		{
			for (int j = 0; j < 20; j++)
			{
				int bx = x + i;
				int by = y + j;

				if (isBuildable(bx, by))
				{
					Position droneTilePosition = drone.getTilePosition();
					double distance = droneTilePosition.getApproxPDistance(new Position(bx, by, PosType.BUILD));
					
					if (distance < nearestDistance)
					{
						buildPosition = new Position(bx, by, PosType.BUILD);
						nearestDistance = distance;
					}
				}
			}
		}
	}

	private boolean isBuildable(int x, int y)
	{
		boolean buildable = true;
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				int bx = x + i;
				int by = y + j;

				GridPoint p = new GridPoint(bx, by);
				if (!buildableGrid.isValid(p))
				{
					buildable = false;
					break;
				}
				
				if (buildableGrid.get(bx, by) != 1)
				{
					buildable = false;
					break;
				}
			}
		}
		
		return buildable;
	}

	@Override
	public void draw(Graphics graphics)
	{
		graphics.setGameCoordinates();
		buildableGridDrawable.draw(graphics);
		
		// draw build position outline
		
		if (buildPosition != null)
		{
			graphics.setColor(BWColor.Yellow);
	
			UnitType buildingType = UnitTypes.Zerg_Spawning_Pool;
			float width = buildingType.getTileWidth() * Game.TILE_SIZE;
			float height = buildingType.getTileHeight() * Game.TILE_SIZE;
	
			Vector2D buildPositionVector = Convert.toPositionVector(buildPosition);
			Vector2D topLeftCorner = new Vector2D(buildPositionVector.getX(), buildPositionVector.getY());
			Vector2D bottomRightCorner = new Vector2D(buildPositionVector.getX() + width, buildPositionVector.getY() + height);
	
			graphics.drawBox(topLeftCorner, bottomRightCorner);
		}
		
		if (drone != null)
		{
			graphics.setColor(BWColor.Yellow);
			graphics.drawCircle(drone, 15);
		}
	}
}
