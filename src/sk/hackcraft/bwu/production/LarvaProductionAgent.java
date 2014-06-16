package sk.hackcraft.bwu.production;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.resource.EntityPool.Contract;
import sk.hackcraft.bwu.resource.EntityPool.ContractListener;
import sk.hackcraft.bwu.selection.TypeSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

public class LarvaProductionAgent implements Updateable
{
	private final Set<Unit> hatcheries;
	private final Set<Unit> larvae;

	private final Contract<Unit> unitsContract;
	
	public LarvaProductionAgent(Contract<Unit> unitsContract)
	{
		this.hatcheries = new HashSet<>();
		this.larvae = new HashSet<>();
		
		this.unitsContract = unitsContract;
	}
	
	@Override
	public void update()
	{
		UnitSet notOwnedHacheries = new UnitSet(unitsContract.getAcquirableEntities(false)).where(UnitSelector.IS_SPAWNING_LARVAE);
		
		for (final Unit hatchery : notOwnedHacheries)
		{
			ContractListener<Unit> listener = new ContractListener<Unit>()
			{
				@Override
				public void entityRemoved(Unit entity)
				{
					hatcheries.remove(entity);
					
					List<Unit> hatcheryLarvae = entity.getLarva();
					for (Unit larva : hatcheryLarvae)
					{
						larvae.remove(larva);
						unitsContract.returnEntity(larva);
					}
				}
			};
			
			unitsContract.requestEntity(hatchery, listener, false);
			
			hatcheries.add(hatchery);
		}
		
		UnitSet notOwnedLarvae = new UnitSet(unitsContract.getAcquirableEntities(false)).where(new TypeSelector(UnitTypes.Zerg_Larva));
		
		for (final Unit larva : notOwnedLarvae)
		{
			Unit homeHatchery = larva.getHatchery();
			
			if (!hatcheries.contains(homeHatchery))
			{
				continue;
			}
			
			ContractListener<Unit> listener = new ContractListener<Unit>()
			{
				@Override
				public void entityRemoved(Unit entity)
				{
					larvae.remove(entity);
				}
			};
			
			unitsContract.requestEntity(larva, listener, false);
			larvae.add(larva);
		}
	}
	
	public boolean produce(UnitType type)
	{
		if (larvae.isEmpty())
		{
			return false;
		}
		
		Unit larva = larvae.iterator().next();

		return produce(type, larva);
	}
	
	public boolean produce(UnitType type, Unit larva)
	{
		if (!larvae.contains(larva))
		{
			throw new IllegalStateException("Production doesn't own this larva.");
		}
		
		boolean result = larva.morph(type);
		
		if (result)
		{
			larvae.remove(larva);
			unitsContract.returnEntity(larva);
		}
		
		return result;
	}
}
