package sk.hackcraft.bwu.mining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import sk.hackcraft.bwu.Updateable;

public class MiningAgent implements Updateable
{
	private final Set<Resource> resources;
	
	private SurplusMinerListener surplusMinerListener;
	
	private final Set<Miner> miners;
	
	private final Map<Miner, Resource> assignments;
	private final Map<Resource, Integer> actualSaturations;
	private final Map<Resource, Integer> fullSaturations;
	
	public MiningAgent(Set<Resource> resources)
	{
		this.resources = resources;

		this.miners = new HashSet<>();
		
		this.assignments = new HashMap<>();
		this.actualSaturations = new HashMap<>();
		this.fullSaturations = new HashMap<>();
		
		for (Resource resource : resources)
		{
			actualSaturations.put(resource, 0);
			
			// TODO add some heuristic based on distance
			fullSaturations.put(resource, 3);
		}
	}
	
	public void setSurplusMinerListener(SurplusMinerListener surplusMinerListener)
	{
		this.surplusMinerListener = surplusMinerListener;
	}
	
	public void addMiner(Miner miner)
	{
		miners.add(miner);
	}
	
	public void removeMiner(Miner miner)
	{
		miners.remove(miner);
	}
	
	@Override
	public void update()
	{
		checkWorkersAssignments();
		checkResourcesState();
	}
	
	private void checkWorkersAssignments()
	{
		for (Miner miner : miners)
		{
			if (!assignments.containsKey(miner))
			{
				Resource resource = selectResourceForAssignment();
				
				if (resource == null)
				{
					freeMiner(miner);
				}
				else
				{
					miner.setResource(resource);

					assignments.put(miner, resource);
					
					int actualSaturation = actualSaturations.get(resource);
					actualSaturations.put(resource, actualSaturation + 1);
				}
			}
		}
	}
	
	private void checkResourcesState()
	{
		Set<Resource> resourcesCopy = new HashSet<>(resources);
		for (Resource resource : resourcesCopy)
		{
			if (!resource.areDataAvailable())
			{
				continue;
			}
			
			if (resource.isMinedOut())
			{
				removeResource(resource);
			}
		}
	}
	
	private void removeResource(Resource resource)
	{
		Map<Miner, Resource> assignmentsCopy = new HashMap<>(assignments);
		for (Entry<Miner, Resource> entry : assignmentsCopy.entrySet())
		{
			if (entry.getValue() == resource)
			{
				assignments.remove(entry.getKey());
			}
		}
		
		resources.remove(resource);
		actualSaturations.remove(resource);
		fullSaturations.remove(resource);
	}
	
	private void freeMiner(Miner miner)
	{
		Resource assignedResource = miner.getAssignedResource();
		
		int saturation = actualSaturations.get(assignedResource);
		actualSaturations.put(assignedResource, saturation - 1);
		
		assignments.remove(miner);
		
		if (surplusMinerListener != null)
		{
			surplusMinerListener.onMinerNotNeeded(miner);
		}
	}

	private Resource selectResourceForAssignment()
	{
		Resource leastSaturatedResource = null;
		int lowestSaturation = Integer.MAX_VALUE;
		
		for (Map.Entry<Resource, Integer> entry : actualSaturations.entrySet())
		{
			Resource resource = entry.getKey();
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
	
	public interface Resource
	{
		boolean areDataAvailable();
		boolean isMiningPossible();
		
		int getValue();
		boolean isMinedOut();
	}
	
	public interface GasResource
	{
		boolean hasExtractorBuilding();
		boolean isDepleted();
	}
	
	public interface Miner extends Updateable
	{
		public enum State
		{
			UNDEFINED,
			MOVING_TO_RESOURCE,
			WAITING_FOR_RESOURCE,
			MINING_RESOURCE,
			RETURNING_RESOURCES;
		}
		
		Resource getAssignedResource();
		
		void setResource(Resource resource);
		
		State getState();
		
		boolean isWorking();
		boolean canWork();
	}
	
	public interface SurplusMinerListener
	{
		void onMinerNotNeeded(Miner miner);
	}
}
