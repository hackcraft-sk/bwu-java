package sk.hackcraft.bwu.production;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.RaceType;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Constants;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.EnvironmentTime;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.resource.EntityPool;
import sk.hackcraft.bwu.resource.EntityPool.ContractListener;
import sk.hackcraft.bwu.selection.NearestPicker;
import sk.hackcraft.bwu.selection.UnitSet;

public class BuildingConstructionAgent implements Updateable, Drawable
{
	private final JNIBWAPI jnibwapi;
	private final EnvironmentTime time;
	
	private final Position center;
	
	private final EntityPool.Contract<Unit> unitsContract;
	
	private final Set<ConstructionTask> tasks;
	private final Map<Unit, ConstructionTask> workersTasks;
	
	public BuildingConstructionAgent(JNIBWAPI jnibwapi, EnvironmentTime time, Position center, EntityPool.Contract<Unit> unitsContract)
	{
		this.jnibwapi = jnibwapi;
		this.time = time;
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
			if (!task.getStatus())
			{
				tasks.remove(task);
				workersTasks.remove(task.getWorker());
				
				continue;
			}
			
			if (task.getWorker() != null && task.isWorkerStillNeeded())
			{
				returnWorker(task);
			}
			
			task.update();
		}
	}
	
	@Override
	public void draw(Graphics graphics)
	{
		for (ConstructionTask task : tasks)
		{
			task.draw(graphics);
		}
	}
	
	private void returnWorker(ConstructionTask task)
	{
		Unit worker = task.getWorker();
		task.setWorker(null);
		unitsContract.returnEntity(worker);
	}
	
	public boolean construct(UnitType buildingType, ConstructionListener listener, boolean urgent)
	{
		UnitType workerType = buildingType.getRaceType().getWorkerType();
		UnitSet worker = new UnitSet(unitsContract.getAcquirableEntities(urgent)).whereType(workerType);

		if (worker.isEmpty())
		{
			return false;
		}
		
		Position buildPosition = pickBuildPositon(buildingType);
		if (buildPosition == null)
		{
			return false;
		}
		
		Unit drone = worker.pick(new NearestPicker(buildPosition));
		
		construct(buildPosition, drone, buildingType, listener, urgent);
		return true;
	}
	
	public void construct(Unit worker, UnitType buildingType, ConstructionListener listener, boolean urgent)
	{
		Position position = pickBuildPositon(buildingType);
		
		construct(position, worker, buildingType, listener, urgent);
	}
	
	public void construct(Position position, Unit worker, UnitType buildingType, ConstructionListener listener, boolean urgent)
	{
		ContractListener<Unit> contractListener = new ContractListener<Unit>()
		{
			@Override
			public void entityRemoved(Unit entity)
			{
				if (!entity.isConstructing() && !entity.isMorphing())
				{
					// TODO if entity is drone, check if its morphed
					ConstructionTask task = workersTasks.get(entity);
					task.cancel();
					
					workersTasks.remove(entity);
				}
			}
		};
		
		unitsContract.requestEntity(worker, contractListener, urgent);
		
		ConstructionTask task = new ConstructionTask(position, worker, buildingType, listener);
		tasks.add(task);
		workersTasks.put(worker, task);
	}
	
	private Position pickBuildPositon(UnitType buildingType)
	{
		// TODO implement properly
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
	
	public enum ConstructionTaskState
	{
		MOVING_TO_POSITION(1),
		STARTING_CONSTRUCTION(2),
		CONSTRUCTING(3),
		COMPLETED(4),
		TERMINATED(5);
		
		private int order;
		
		private ConstructionTaskState(int order)
		{
			this.order = order;
		}
		
		public int getOrder()
		{
			return order;
		}
	};
	
	private class ConstructionTask implements Updateable, Drawable
	{
		private final Position buildPosition;
		private final Position buildCenterPosition;
		private final UnitType buildingType;
		private final ConstructionListener constructionListener;
		
		private Unit worker;
		private Unit constructedBuilding;
		private ConstructionTaskState actualState;
		
		public ConstructionTask(Position buildPosition, Unit worker, UnitType buildingType, ConstructionListener listener)
		{
			this.buildPosition = buildPosition;
		
			int width = (buildingType.getTileWidth() * Game.TILE_SIZE) / 2;
			int height = (buildingType.getTileHeight() * Game.TILE_SIZE) / 2;
			
			buildCenterPosition = new Position(buildPosition.getPX() + width, buildPosition.getPY() + height);
			
			this.worker = worker;
			this.buildingType = buildingType;
			this.constructionListener = listener;
			
			actualState = ConstructionTaskState.MOVING_TO_POSITION;
			
			worker.stop(false);
		}
		
		public Unit getWorker()
		{
			return worker;
		}
		
		public void setWorker(Unit worker)
		{
			this.worker = worker;
		}
		
		public boolean isConsumingWorker()
		{
			return buildingType.getRaceType() == RaceTypes.Zerg;
		}
		
		public boolean isWorkerStillNeeded()
		{
			RaceType raceType = buildingType.getRaceType();
			if (raceType == RaceTypes.Protoss)
			{
				return actualState.getOrder() >= ConstructionTaskState.CONSTRUCTING.getOrder();
			}
			else if (raceType == RaceTypes.Terran)
			{
				return actualState.getOrder() >= ConstructionTaskState.COMPLETED.getOrder();
			}
			else
			{
				return worker.getType() == buildingType;
			}
		}
		
		@Override
		public void update()
		{
			if (constructionTerminated())
			{
				actualState = ConstructionTaskState.TERMINATED;
				return;
			}

			switch (actualState)
			{
				case MOVING_TO_POSITION:
				{
					if (isInPosition())
					{
						actualState = ConstructionTaskState.STARTING_CONSTRUCTION;
					}
					else
					{
						moveWorkerToPosition();
					}
					
					break;
				}
				case STARTING_CONSTRUCTION:
				{
					if (startedConstruction())
					{
						actualState = ConstructionTaskState.CONSTRUCTING;
					}
					else
					{
						commenceConstruction(time);
					}
					
					break;
				}
				case CONSTRUCTING:
				{
					if (finishedConstruction())
					{
						actualState = ConstructionTaskState.COMPLETED;
					}
					break;
				}
				case COMPLETED:
				{
					break;
				}
				case TERMINATED:
				{
					break;
				}
			}
		}
		
		@Override
		public void draw(Graphics graphics)
		{
			if (worker != null)
			{
				Vector2D workerPositionVector = Convert.toPositionVector(worker.getPosition());
				
				graphics.setColor(BWColor.Blue);
				graphics.fillCircle(worker, 4);
				
				Vector2D buildCenterPositionVector = Convert.toPositionVector(buildCenterPosition);
				Vector2D vectorToBuildPositon = buildCenterPositionVector.sub(workerPositionVector);
				
				if (vectorToBuildPositon.getLength() > Constants.LINE_DRAW_VISUAL_LIMIT)
				{
					vectorToBuildPositon.normalize().scale((float)Constants.LINE_DRAW_VISUAL_LIMIT);
				}
				
				graphics.drawLine(workerPositionVector, buildCenterPositionVector);
				
				graphics.drawText(worker, worker.getOrder().getName());
			}
			
			graphics.setColor(BWColor.Yellow);
			
			float width = buildingType.getTileWidth() * Game.TILE_SIZE;
			float height = buildingType.getTileHeight() * Game.TILE_SIZE;
			
			Vector2D buildPositionVector = Convert.toPositionVector(buildPosition);
			Vector2D topLeftCorner = new Vector2D(buildPositionVector.getX(), buildPositionVector.getY());
			Vector2D bottomRightCorner = new Vector2D(buildPositionVector.getX() + width, buildPositionVector.getY() + height);
			
			graphics.drawBox(topLeftCorner, bottomRightCorner);
		}
		
		private boolean constructionTerminated()
		{
			// TODO
			return false;
		}
		
		private boolean workerDestroyed()
		{
			return !worker.isExists();
		}

		private boolean buildingDestroyed()
		{
			return !constructedBuilding.isExists();
		}

		private void moveWorkerToPosition()
		{
			if (!worker.isMoving() && time.getFrame() - worker.getLastCommandFrame() > 30)
			{
				worker.move(buildCenterPosition, false);
			}
		}

		private void commenceConstruction(EnvironmentTime time)
		{
			if (time.getFrame() % 30 == 0)
			{
				worker.build(buildPosition, buildingType);
			}
		}

		private boolean finishedConstruction()
		{
			// TODO Auto-generated method stub
			return false;
		}

		private boolean startedConstruction()
		{
			// TOOD
			return worker == null;
		}

		private boolean isInPosition()
		{
			double distance = worker.getPosition().getPDistance(buildCenterPosition);
			return distance < Constants.IN_POSITION_DELTA;
		}
		
		public boolean isEnded()
		{
			return actualState == ConstructionTaskState.COMPLETED || actualState == ConstructionTaskState.TERMINATED;
		}
		
		public boolean getStatus()
		{
			return actualState != ConstructionTaskState.TERMINATED;
		}
		
		public void cancel()
		{
			worker.stop(false);
		}
	}
}
