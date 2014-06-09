package net.moergil.cortex.evosim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.hackcraft.bwu.Vector2D;

public class Environment
{
	private List<Organism> allOrganism;
	
	private Map<Organism, Vector2D> positions;
	
	public List<Organism> getVisibleOrganisms(Organism source, float radius)
	{
		List<Organism> visible = new ArrayList<Organism>();
		
		for (Organism organism : allOrganism)
		{
			if (positions.get(organism).sub(positions.get(source)).getLength() < radius)
			{
				visible.add(organism);
			}
		}
		
		return visible;
	}
	
	public void moveOrganism(Organism organism, Vector2D newPosition)
	{
		
	}
	
}
