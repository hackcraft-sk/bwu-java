package sk.hackcraft.bwu.resource;

import java.util.Map;
import java.util.Set;

import jnibwapi.Unit;

public class UnitPool implements EntityPool<Unit>
{
	private Set<Unit> units;
	private Set<Unit> freeUnits;
	private Map<Unit, Contract<Unit>> contracts;
	private Map<Unit, ContractListener<Unit>> contractListeners;
	
	@Override
	public void add(Unit unit)
	{
		units.add(unit);
		freeUnits.add(unit);
	}

	@Override
	public void remove(Unit unit)
	{
		units.remove(unit);
		freeUnits.remove(unit);
		
		contracts.remove(unit);
		ContractListener<Unit> listener = contractListeners.remove(unit);
		
		if (listener != null)
		{
			listener.entityRemoved(unit);
		}
	}
	
	@Override
	public Contract<Unit> createContract()
	{
		return new Contract<Unit>()
		{
			
			@Override
			public void returnEntity(Unit unit)
			{
				if (units.contains(unit) && contracts.get(unit) == this)
				{
					contracts.remove(unit);
					contractListeners.remove(unit);
					freeUnits.add(unit);
				}
				else
				{
					throw new IllegalStateException("Unit is not owned by this owner.");
				}
			}
			
			@Override
			public boolean requestEntity(Unit unit, ContractListener<Unit> listener)
			{
				if (freeUnits.contains(unit))
				{
					contracts.put(unit, this);
					contractListeners.put(unit, listener);
					return true;
				}
				else
				{
					return false;
				}
			}
			
			@Override
			public Set<Unit> getAvailableEntities()
			{
				return freeUnits;
			}
		};
	}
}
