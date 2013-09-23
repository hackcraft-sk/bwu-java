package sk.hackcraft.bwu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.hackcraft.bwu.Vector2D;

public class VectorGraph {
	public interface InformationSystem {
		public double getValueFor(Vector2D position);
		public double combineSelfAndSiblings(VertexValue me, VertexValue siblings);
	}
	
	public class VertexValue {
		final public Vector2D point;
		final public double value;
		
		private VertexValue(Vector2D point, double value) {
			this.point = point;
			this.value = value;
		}
	}
	
	private class Vertex {
		private Map<InformationSystem, Double> systemValues = new HashMap<>();
		private Vector2D point;
		private Set<Vertex> edges = new HashSet<>();
		private Map<Vertex, Double> distances = new HashMap<>();
		private Map<Vertex, Integer> successors = new HashMap<>();
		
		public Vertex(Vector2D point) {
			this.point = point;
		}
	}
	
	private List<Vertex> vertices = new ArrayList<>();
	private boolean refreshed = false;
	
	public VectorGraph(Vector2D... points) {
		for(Vector2D point : points) {
			vertices.add(new Vertex(point));
		}
	}
	
	public void connect(int indexA, int indexB) {
		vertices.get(indexA).edges.add(vertices.get(indexB));
	}
	
	public void connectBothWays(int indexA, int indexB) {
		connect(indexA, indexB);
		connect(indexB, indexA);
	}
	
	private void runFloyd() {
		for(int k=0; k<vertices.size(); k++) {
			for(int i=0; i<vertices.size(); i++) {
				for(int j=0; j<vertices.size(); j++) {
					double newDistance = vertices.get(i).distances.get(k) + vertices.get(k).distances.get(j);
					if(newDistance < vertices.get(i).distances.get(j)) {
						vertices.get(i).distances.put(vertices.get(j), newDistance);
						vertices.get(i).successors.put(vertices.get(j), k);
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
		if(vertices.get(indexFrom).distances.get(indexTo) == Double.POSITIVE_INFINITY) {
			return null;
		}
		
		int intermediate = vertices.get(indexFrom).successors.get(indexTo);
		if(intermediate == -1) {
			return new LinkedList<>();
		} else {
			LinkedList<Vector2D> result = new LinkedList<>();
			
			result.addAll(getShortestPath(indexFrom, intermediate));
			result.add(vertices.get(intermediate).point);
			result.addAll(getShortestPath(intermediate, indexTo));
			
			return result;
		}
	}
	
	private int getClosestIndexFor(Vector2D position) {
		int index = -1;
		double shortest = Double.MAX_VALUE;
		
		for(int i=0; i<vertices.size(); i++) {
			double distance = position.sub(vertices.get(i).point).length;
			
			if(distance < shortest) {
				index = i;
				shortest = distance;
			}
		}
		
		return index;
	}
	
	public void update(InformationSystem system, int repeatings) {
		// ensure that every vertex has a value for this system
		for(Vertex vertex : vertices) {
			if(!vertex.systemValues.containsKey(system)) {
				vertex.systemValues.put(system, (double)system.getValueFor(vertex.point));
			}
		}
		
		// repeat many times
		for(int repeating=0; repeating < repeatings; repeating++) {
			for(Vertex vertex : vertices) {
				VertexValue [] siblingValues = new VertexValue[vertex.edges.size()];
				
				Iterator<Vertex> it = vertex.edges.iterator();
				for(int edgeIndex=0; edgeIndex < vertex.edges.size(); edgeIndex++) {
					Vertex edgeVertex = it.next();
					
				}
			}
		}
	}
}
