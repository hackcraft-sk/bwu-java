package sk.hackcraft.bwu.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import sk.hackcraft.bwu.Updateable;

public class StateMachine implements Updateable
{
	private final Map<State, Map<State, StateTransition>> transitionsMap;
	
	private State currentState;
	private StateTransition currentTransition;
	private State targetState;

	public StateMachine(State initialState)
	{
		transitionsMap = new HashMap<>();
		
		addState(initialState);
		
		this.currentState = initialState;
	}
	
	public State getCurrentState()
	{
		return currentState;
	}
	
	public StateTransition getCurrentTransition()
	{
		return currentTransition;
	}
	
	public StateMachine addState(State state)
	{
		transitionsMap.put(state, new HashMap<State, StateTransition>());
		return this;
	}
	
	public StateMachine addTransition(State startState, State endState, StateTransition transition)
	{
		transitionsMap.get(startState).put(endState, transition);
		return this;
	}
	
	public boolean isChangingState()
	{
		return currentTransition != null;
	}
	
	public void changeState(State targetState)
	{
		Map<State, StateTransition> availableTransitions = transitionsMap.get(currentState);
		
		if (!availableTransitions.containsKey(targetState))
		{
			throw new IllegalArgumentException("No transiton from " + currentState + " to " + targetState + " exits.");
		}

		currentTransition = transitionsMap.get(currentState).get(targetState);	
		this.targetState = targetState;
	}
	
	@Override
	public void update()
	{
		if (currentTransition != null)
		{
			if (currentTransition.isPossible())
			{
				currentTransition = null;

				currentState = targetState;
				targetState = null;
			}
		}
		
		currentState.update();
	}
	
	public interface State extends Updateable
	{
	}
	
	public interface StateTransition
	{
		boolean isPossible();
		
		public static StateTransition ALWAYS = new StateTransition()
		{
			@Override
			public boolean isPossible()
			{
				return true;
			}
		};
	}
}
