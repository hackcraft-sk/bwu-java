package sk.hackcraft.bwu.production;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.resource.EntityPool;
import sk.hackcraft.bwu.resource.EntityPool.Contract;
import sk.hackcraft.bwu.resource.EntityPool.ContractListener;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.NearestPicker;
import sk.hackcraft.bwu.selection.Pickers;
import sk.hackcraft.bwu.selection.TypeSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;

public class DroneBuildingConstructionAgent implements Updateable
{
	private final JNIBWAPI jnibwapi;
	
	private final Position center;
	
	private final EntityPool.Contract<Unit> unitsContract;
	
	private final Set<ConstructionTask> tasks;
	private final Map<Unit, ConstructionTask> workersTasks;
	
	public DroneBuildingConstructionAgent(JNIBWAPI jnibwapi, Position center, EntityPool.Contract<Unit> unitsContract)
	{
		this.jnibwapi = jnibwapi;
		this.center = center;
		this.unitsContract = unitsContract;
		
		tasks = new HashSet<>();
		workersTasks = new HashMap<Unit, ConstructionTask>();
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
				workersTasks.remove(task.getWorker());
				continue;
			}
			
			task.update();
		}
	}
	
	public boolean construct(UnitType buildingType, ConstructionListener listener, boolean urgent)
	{
		UnitSet drones = new UnitSet(unitsContract.getAcquirableEntities(urgent)).where(new TypeSelector(UnitTypes.Zerg_Drone));

		if (drones.isEmpty())
		{
			return false;
		}
		
		Position buildPosition = pickBuildPositon(buildingType);
		if (buildPosition == null)
		{
			return false;
		}
		
		Unit drone = drones.pick(new NearestPicker(buildPosition));
		
		construct(buildPosition, drone, buildingType, listener, urgent);
		return true;
	}
	
	public void construct(Unit drone, UnitType buildingType, ConstructionListener listener, boolean urgent)
	{
		Position position = pickBuildPositon(buildingType);
		
		construct(position, drone, buildingType, listener, urgent);
	}
	
	public void construct(Position position, Unit drone, UnitType buildingType, ConstructionListener listener, boolean urgent)
	{
		ContractListener<Unit> contractListener = new ContractListener<Unit>()
		{
			@Override
			public void entityRemoved(Unit entity)
			{
				ConstructionTask task = workersTasks.get(entity);
				task.cancel();
			}
		};
		
		unitsContract.requestEntity(drone, contractListener, urgent);
		
		ConstructionTask task = new ConstructionTask(position, drone, buildingType, listener);
		tasks.add(task);
		workersTasks.put(drone, task);
	}
	
	private Position pickBuildPositon(UnitType buildingType)
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
			
			return position;
		}
		
		return null;
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
		
		public Unit getWorker()
		{
			return worker;
		}
		
		public boolean getStatus()
		{
			return status;
		}
		
		public void cancel()
		{
			// TODO
		}
	}
}
