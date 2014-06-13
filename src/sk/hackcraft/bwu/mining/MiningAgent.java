package sk.hackcraft.bwu.mining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.OrderType.OrderTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSelector;

public class MiningAgent implements Updateable, Drawable
{
	private final Unit resourceDepot;
	
	private final Set<Unit> resources;

	private final Set<Unit> miners;
	private final Set<Unit> freeMiners;
	private final Map<Unit, Unit> minersToResourcesAssignments;
	
	private final Map<Unit, Integer> actualSaturations;
	private final Map<Unit, Integer> fullSaturations;
	
	public MiningAgent(Unit resourceDepot, Set<Unit> resources)
	{
		this.resourceDepot = resourceDepot;
		this.resources = resources;

		this.miners = new HashSet<>();
		this.freeMiners = new HashSet<>();
		
		this.minersToResourcesAssignments = new HashMap<>();
		this.actualSaturations = new HashMap<>();
		this.fullSaturations = new HashMap<>();
		
		for (Unit resource : resources)
		{
			actualSaturations.put(resource, 0);
			fullSaturations.put(resource, 2);
		}
	}
	
	public Unit getResourceDepot()
	{
		return resourceDepot;
	}

	public void addMiner(Unit miner)
	{
		miners.add(miner);
		freeMiners.add(miner);
	}

	public void removeMiner(Unit miner)
	{
		if (miners.contains(miner))
		{
			if (!freeMiners.contains(miner))
			{
				freeMiner(miner);
			}

			freeMiners.remove(miner);
			miners.remove(miner);
		}
	}
	
	public Set<Unit> getFreeMiners()
	{
		return freeMiners;
	}

	public int getSaturationDifference()
	{
		return getFullSaturation() - getActualSaturation();
	}
	
	public int getFullSaturation()
	{
		int fullSaturation = 0;
		
		for (int resourceFullSaturaton : fullSaturations.values())
		{
			fullSaturation += resourceFullSaturaton;
		}
		
		return fullSaturation;
	}
	
	public int getActualSaturation()
	{
		return miners.size();
	}
	
	@Override
	public void update()
	{
		checkResources();
		checkWorkersAssignments();
		checkWorkers();
	}
	
	@Override
	public void draw(Graphics graphics)
	{
		graphics.setGameCoordinates();
		
		for (Unit miner : miners)
		{
			graphics.setColor(BWColor.Green);
			graphics.drawCircle(miner, 3);
			
			if (miner.isSelected())
			{
				Vector2D resourceDepotVector = Convert.toPositionVector(resourceDepot.getPosition());
				Vector2D resourceVector = Convert.toPositionVector(miner.getPosition());
				
				graphics.setColor(BWColor.Blue);
				graphics.drawLine(resourceDepotVector, resourceVector);
			}
			
			if (freeMiners.contains(miner))
			{
				graphics.setColor(BWColor.Grey);
				graphics.fillCircle(miner, 3);
			}
			
			if (miner.getOrder() == OrderTypes.MiningMinerals)
			{
				graphics.setColor(BWColor.Green);
				graphics.fillCircle(miner, 3);
			}
			
			if (miner.getOrder() == OrderTypes.WaitForMinerals)
			{
				graphics.setColor(BWColor.Orange);
				graphics.fillCircle(miner, 3);
			}
			
			Unit target = miner.getTarget();
			if (target != null)
			{
				Position targetPosition = target.getPosition();
				Position minerPosition = miner.getPosition();
				
				if (targetPosition.getPDistance(minerPosition) < 500)
				{
					graphics.setColor(BWColor.Green);
					
					Vector2D from = Convert.toPositionVector(targetPosition);
					Vector2D to = Convert.toPositionVector(minerPosition);
					
					graphics.drawLine(from, to);
				}
			}
		}
		
		graphics.drawText(resourceDepot, "A: " + miners.size() + " F: " + freeMiners.size());
		
		if (resourceDepot.isSelected())
		{
			for (Unit resource : resources)
			{
				Vector2D resourceDepotVector = Convert.toPositionVector(resourceDepot.getPosition());
				Vector2D resourceVector = Convert.toPositionVector(resource.getPosition());
				
				graphics.setColor(BWColor.Grey);
				graphics.drawLine(resourceDepotVector, resourceVector);
			}
			
			for (Unit miner : miners)
			{
				Vector2D resourceDepotVector = Convert.toPositionVector(resourceDepot.getPosition());
				Vector2D resourceVector = Convert.toPositionVector(miner.getPosition());
				
				graphics.setColor(BWColor.Blue);
				graphics.drawLine(resourceDepotVector, resourceVector);
			}
		}
	}
	
	private void checkWorkersAssignments()
	{
		Set<Unit> freeMinersCopy = new HashSet<>(freeMiners);
		for (Unit miner : freeMinersCopy)
		{
			if (!minersToResourcesAssignments.containsKey(miner))
			{
				Unit resource = selectResourceForAssignment();
				
				if (resource != null)
				{
					freeMiners.remove(miner);

					minersToResourcesAssignments.put(miner, resource);
					
					int actualSaturation = actualSaturations.get(resource);
					actualSaturations.put(resource, actualSaturation + 1);
				}
			}
		}
	}
	
	private void checkWorkers()
	{
		for (Unit miner : miners)
		{
			if (miner.isIdle())
			{
				Unit resource = minersToResourcesAssignments.get(miner);
				miner.gather(resource, false);
				continue;
			}
			
			Unit target = miner.getTarget();
			
			if (target != null && target.getType().isResourceContainer())
			{
				Unit assignedResource = minersToResourcesAssignments.get(miner);
				if (!assignedResource.equals(target))
				{
					miner.gather(assignedResource, false);
				}
			}
		}
	}
	
	private void checkResources()
	{
		Set<Unit> resourcesCopy = new HashSet<>(resources);
		for (Unit resource : resourcesCopy)
		{
			if (!resource.isVisible())
			{
				continue;
			}
			
			if (resource.getResources() <= 0)
			{
				removeResource(resource);
			}
		}
	}
	
	private void removeResource(Unit resource)
	{
		Unit miner = null;
		
		Map<Unit, Unit> assignmentsCopy = new HashMap<>(minersToResourcesAssignments);
		for (Map.Entry<Unit, Unit> entry : assignmentsCopy.entrySet())
		{
			if (entry.getValue() == resource)
			{
				miner = entry.getKey();
				break;
			}
		}
		
		freeMiner(miner);
		
		resources.remove(resource);
		actualSaturations.remove(resource);
		fullSaturations.remove(resource);
	}
	
	private void freeMiner(Unit miner)
	{
		Unit assignedResource = minersToResourcesAssignments.get(miner);
		
		int saturation = actualSaturations.get(assignedResource);
		actualSaturations.put(assignedResource, saturation - 1);
		
		minersToResourcesAssignments.remove(miner);

		freeMiners.add(miner);
	}

	private Unit selectResourceForAssignment()
	{
		Unit leastSaturatedResource = null;
		int lowestSaturation = Integer.MAX_VALUE;
		
		for (Map.Entry<Unit, Integer> entry : actualSaturations.entrySet())
		{
			Unit resource = entry.getKey();
			
			if (!UnitSelector.IS_MINERAL.isTrueFor(resource))
			{
				continue;
			}
			
			int actualSaturation = entry.getValue();
			
			int fullSaturation = fullSaturations.get(resource);
			
			if (actualSaturation < fullSaturation && actualSaturation < lowestSaturation)
			{
				leastSaturatedResource = resource;
				lowestSaturation = actualSaturation;
			}
		}
		
		return leastSaturatedResource;
	}
}
