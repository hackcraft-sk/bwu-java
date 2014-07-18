package sk.hackcraft.bwu.buildorder;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.production.BuildingConstructionAgent;
import sk.hackcraft.bwu.production.ProductionAgent;

public class BuildOrderContext
{
	final private Game game;
	final private BuildingConstructionAgent buildingConstructionAgent;
	final private ProductionAgent productionAgent;
	
	public BuildOrderContext(Game game, BuildingConstructionAgent buildingConstructionAgent, ProductionAgent productionAgent)
	{
		this.game = game;
		this.buildingConstructionAgent = buildingConstructionAgent;
		this.productionAgent = productionAgent;
	}
	
	public BuildingConstructionAgent getBuildingConstructionAgent()
	{
		return buildingConstructionAgent;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public ProductionAgent getProductionAgent()
	{
		return productionAgent;
	}
}
