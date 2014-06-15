package sk.hackcraft.bwu.resource;

import java.util.Set;

public interface EntityPool<E>
{
	void add(E entity);
	void remove(E entity);
	
	Contract<E> createContract();

	public interface ContractListener<E>
	{
		void entityRemoved(E entity);
	}
	
	public interface Contract<E>
	{
		Set<E> getAvailableEntities();
		
		boolean requestEntity(E entity, ContractListener<E> listener);
		void returnEntity(E entity);
	}
}
