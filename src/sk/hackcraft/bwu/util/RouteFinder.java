package sk.hackcraft.bwu.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sk.hackcraft.bwu.Vector2D;

public class RouteFinder {
	private List<Vector2D> vertices;
	private Map<Integer, Map<Integer, Double>> distances;
	private Map<Integer, Map<Integer, Integer>> successors;
	private boolean refreshed = false;
	
	public RouteFinder(List<Vector2D> vertices) {
		this.vertices = vertices;
		distances = new HashMap<>();
		successors = new HashMap<>();
		
		for(int i=0; i<vertices.size(); i++) {
			distances.put(i, new HashMap<Integer, Double>());
			successors.put(i, new HashMap<Integer, Integer>());
			for(int j=0; j<vertices.size(); j++) {
				distances.get(i).put(j, (i == j ? 0 : Double.POSITIVE_INFINITY));
				successors.get(i).put(j, -1);
			}
		}
	}
	
	public void connect(int from, int to) {
		double length = vertices.get(from).sub(vertices.get(to)).length;
		distances.get(from).put(to, length);
	}
	
	public void connectBoth(int from, int to) {
		connect(from, to);
		connect(to, from);
	}
	
	private void runFloyd() {
		for(int k=0; k<vertices.size(); k++) {
			for(int i=0; i<vertices.size(); i++) {
				for(int j=0; j<vertices.size(); j++) {
					double newDistance = distances.get(i).get(k) + distances.get(k).get(j);
					if(newDistance < distances.get(i).get(j)) {
						distances.get(i).put(j, newDistance);
						successors.get(i).put(j, k);
					}
				}
			}
		}
	}
	
	public List<Vector2D> getShortestPath(Vector2D from, Vector2D to) {
		if(!refreshed) {
			runFloyd();
			refreshed = true;
		}
		
		int startingIndex = getClosestIndexFor(from);
		int endingIndex = getClosestIndexFor(to);
		
		if(startingIndex == endingIndex) {
			LinkedList<Vector2D> result = new LinkedList<>();
			result.add(to);
			
			return result;
		}
		
		List<Vector2D> subResult = getShortestPath(startingIndex, endingIndex);
		subResult.add(to);
		return subResult;
	}
	
	private List<Vector2D> getShortestPath(int indexFrom, int indexTo) {
		if(distances.get(indexFrom).get(indexTo) == Double.POSITIVE_INFINITY) {
			return null;
		}
		
		int intermediate = successors.get(indexFrom).get(indexTo);
		if(intermediate == -1) {
			return new LinkedList<>();
		} else {
			LinkedList<Vector2D> result = new LinkedList<>();
			
			result.addAll(getShortestPath(indexFrom, intermediate));
			result.add(vertices.get(intermediate));
			result.addAll(getShortestPath(intermediate, indexTo));
			
			return result;
		}
	}
	
	private int getClosestIndexFor(Vector2D position) {
		int index = -1;
		double shortest = Double.MAX_VALUE;
		
		for(int i=0; i<vertices.size(); i++) {
			double distance = position.sub(vertices.get(i)).length;
			
			if(distance < shortest) {
				index = i;
				shortest = distance;
			}
		}
		
		return index;
	}
}
