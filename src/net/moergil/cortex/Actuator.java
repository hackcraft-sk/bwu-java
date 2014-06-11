package net.moergil.cortex;

import sk.hackcraft.bwu.Updateable;

public class Actuator implements Updateable
{
	private final Neuron triggeringNeuron;
	
	private boolean active;
	
	public Actuator(Neuron triggerinnNeuron)
	{
		this.triggeringNeuron = triggerinnNeuron;
	}
	
	@Override
	public void update()
	{
		if (triggeringNeuron.getOutput() >= 1 && !active)
		{
			active = true;
			activate();
		}
		else if (triggeringNeuron.getOutput() < 1 && active)
		{
			active = false;
			deactivate();
		}
	}
	
	protected void activate()
	{
	}
	
	protected void deactivate()
	{
	}
}
