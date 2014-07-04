package sk.hackcraft.bwu;

public interface EntitiesContract<E>
{
	void setListener(ContractListener<E> listener);

	void returnEntity(E entity);
	
	public interface ContractListener<E>
	{
		void entityAdded(E entity);
		void entityRemoved(E entity);
	}
}
