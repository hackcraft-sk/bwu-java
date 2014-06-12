package sk.hackcraft.bwu.mining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jnibwapi.BaseLocation;
import jnibwapi.Position;
import jnibwapi.Unit;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Updateable;

public class MapResourcesAgent implements Updateable
{
	private final Set<MiningAgent> miningAgents;
	
	private final Map<Position, ExpandInfo> expandsInformations;
	
	public MapResourcesAgent(Game game)
	{
		miningAgents = new HashSet<>();
		expandsInformations = new HashMap<>();
		
		List<BaseLocation> baseLocations = game.getMap().getBaseLocations();
		
		/*for (BaseLocation baseLocation : baseLocations)
		{
			Position position = baseLocation.getCenter();
			Set<Unit> mineralFields = baseLocation.get
		}*/
	}
	
	public Set<MiningAgent> getMiningAgents()
	{
		return miningAgents;
	}
	
	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		
	}
	
	public static class ExpandInfo
	{
		private boolean baseOperational;
		private boolean baseConstructed;
		private final boolean gasAvailable;
		private boolean mimneralsExhausted;
		private boolean gasDepleted;

		public ExpandInfo(boolean baseOperational, boolean baseConstructed, boolean gasAvailable, boolean mimneralsExhausted, boolean gasDepleted)
		{
			this.baseOperational = baseOperational;
			this.baseConstructed = baseConstructed;
			this.gasAvailable = gasAvailable;
			this.mimneralsExhausted = mimneralsExhausted;
			this.gasDepleted = gasDepleted;
		}
		
		public void setBaseConstructed(boolean baseConstructed)
		{
			this.baseConstructed = baseConstructed;
		}
		
		public void setBaseOperational(boolean baseOperational)
		{
			this.baseOperational = baseOperational;
		}
		
		public void setGasDepleted(boolean gasDepleted)
		{
			this.gasDepleted = gasDepleted;
		}
		
		public void setMimneralsExhausted(boolean mimneralsExhausted)
		{
			this.mimneralsExhausted = mimneralsExhausted;
		}
		
		public boolean isBaseOperational()
		{
			return baseOperational;
		}
		
		public boolean isBaseConstructed()
		{
			return baseConstructed;
		}
		
		public boolean isGasAvailable()
		{
			return gasAvailable;
		}
		
		public boolean isGasDepleted()
		{
			return gasDepleted;
		}
		
		public boolean isMimneralsExhausted()
		{
			return mimneralsExhausted;
		}
	}
}
