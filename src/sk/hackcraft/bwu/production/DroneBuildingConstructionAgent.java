package sk.hackcraft.bwu.production;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import sk.hackcraft.bwu.Updateable;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

public class DroneBuildingConstructionAgent implements Updateable
{
	private final JNIBWAPI jnibwapi;
	
	private final Position center;
	
	private final Set<ConstructionTask> tasks;
	
	public DroneBuildingConstructionAgent(JNIBWAPI jnibwapi, Position center)
	{
		this.jnibwapi = jnibwapi;
		this.center = center;
		
		tasks = new HashSet<>();
	}
	
	@Override
	public void update()
	{
		Set<ConstructionTask> tasksCopy = new HashSet<>(tasks);
		for (ConstructionTask task : tasksCopy)
		{
			if (task.getStatus() == false)
			{
				tasks.remove(task);
				continue;
			}
			
			task.update();
		}
	}
	
	public void construct(Unit drone, UnitType buildingType, ConstructionListener listener)
	{
		Random random = new Random();
		
		for (int i = 0; i < 1000; i++)
		{
			int x = random.nextInt(20) - 10 + center.getBX();
			int y = random.nextInt(20) - 10 + center.getBY(); 
			
			Position position = new Position(x, y, PosType.BUILD);
			
			if (!jnibwapi.canBuildHere(position, buildingType, true))
			{
				continue;
			}
			
			ConstructionTask task = new ConstructionTask(position, drone, buildingType, listener);
			tasks.add(task);
			break;
		}
	}
	
	public interface ConstructionListener
	{
		void onFailed();
	}
	
	private class ConstructionTask implements Updateable
	{
		private final Position position;
		private final Unit worker;
		private final UnitType buildingType;
		private final ConstructionListener constructionListener;
		
		private boolean status;
		
		public ConstructionTask(Position position, Unit worker, UnitType buildingType, ConstructionListener listener)
		{
			this.position = position;
			this.worker = worker;
			this.buildingType = buildingType;
			this.constructionListener = listener;
			
			this.status = true;
		}
		
		@Override
		public void update()
		{
			if (!worker.isExists())
			{
				constructionListener.onFailed();
				status = false;
			}
			
			if (worker.isIdle())
			{
				worker.build(position, buildingType);
			}
		}
		
		public boolean getStatus()
		{
			return status;
		}
	}
}
