package sk.hackcraft.bwu.util;

import java.util.HashSet;
import java.util.Iterator;

import jnibwapi.Map;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Minimap;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSet;

public class Clustering {
	static public class Cluster extends UnitSet {
		private Vector2D position;
		private Object representative = new Object();
		
		public Cluster(Vector2D position) {
			this.position = position;
		}
		
		public void setPosition(Vector2D position) {
			this.position = position;
		}
		
		public void setPositionToSquareDistanceCenter() {
			if(size() > 0) {
				Vector2D position = Vector2D.ZERO;
				double cumulation = 0;
				
				for(Unit unit : this) {
					double distance = unit.getPosition().sub(this.position).length / 10;
					
					double scale = 1/(distance*distance);
					
					if(Double.isInfinite(scale) || Double.isNaN(scale)) {
						continue;
					}
					
					position = position.add(unit.getPosition().scale(scale));
					
					cumulation += scale;
				}
				
				if(cumulation > 0.0000000001) {
					this.position = position.scale(1/cumulation);
				}
			}
		}
		
		public Vector2D getPosition() {
			return position;
		}
		
		public double getSquareDistanceError() {
			if(this.isEmpty()) {
				return 0;
			}
			
			double cumulative = 0;
			
			for(Unit unit : this) {
				double distance = unit.getPosition().sub(position).length / 10;
				cumulative += distance*distance;
			}
			
			return cumulative / this.size();
		}
		
		@Override
		public int hashCode() {
			return representative.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Clustering)) {
				return false;
			}
			
			return ((Cluster)o).representative == representative;
		}
	}

	private Map map;
	private HashSet<Cluster> clusters = new HashSet<>();
	private double clusterPrice = 40;
	private double clusterCountChangePrice = 5000;
	
	private int clusterImprovementCount = 10;
	
	public Clustering(Map map, int initialClusterCount) {
		this.map = map;
		
		for(int i=0; i<initialClusterCount; i++) {
			addCluster();
		}
	}
	
	private void moveClustersRandomly() {
		for(Cluster cluster : clusters) {
			cluster.setPosition(Vector2D.random().scale(map));
		}
	}
	
	public void updateFor(UnitSet units) {
		removeNonExistingUnitsFromClusters(units);

		doOneIteration(units);
		double costWithoutAnything = getCombinedCostOfClusters();
		
		addCluster();
		doOneIteration(units);
		
		double costWithNewCluster = getCombinedCostOfClusters()+clusterCountChangePrice;
		
		if(costWithNewCluster < costWithoutAnything) {
			return;
		}
		
		removeCluster();
		
		if(clusters.size() <= 1) {
			return;
		}
		removeCluster();
		
		doOneIteration(units);
		double costWithClusterRemoval = getCombinedCostOfClusters()+clusterCountChangePrice;
		
		if(costWithClusterRemoval > costWithoutAnything) {
			addCluster();
		}
		
		doOneIteration(units);
	}
	
	private void addCluster() {
		clusters.add(new Cluster(Vector2D.random().scale(map)));
	}
	
	private void removeCluster() {
		if(!clusters.isEmpty()) {
			Iterator<Cluster> it = clusters.iterator();
			it.next();
			it.remove();
		}
	}
	
	private double getCombinedCostOfClusters() {
		double combined = 0;
		for(Cluster cluster : clusters) {
			combined += cluster.getSquareDistanceError();
		}
		return combined+clusterPrice*Math.pow(2, clusters.size());
	}
	
	private void doOneIteration(UnitSet units) {
		moveClustersRandomly();
		doImprovements(units);
	}
	
	private void doImprovements(UnitSet units) {
		for(int i=0; i<clusterImprovementCount; i++) {
			recomputeClustersCenters();
			clearClusters();
			assignUnitsToBestClusters(units);
		}
	}
	
	private void assignUnitsToBestClusters(UnitSet units) {
		for(Unit unit : units) {
			findBestClusterFor(unit).add(unit);
		}
	}
	
	private void recomputeClustersCenters() {
		for(Cluster cluster : clusters) {
			cluster.setPositionToSquareDistanceCenter();
		}
	}
	
	private void clearClusters() {
		for(Cluster cluster : clusters) {
			cluster.clear();
		}
	}
	
	private void removeNonExistingUnitsFromClusters(UnitSet units) {
		for(Cluster cluster : clusters) {
			Iterator<Unit> it = cluster.iterator();
			
			while(it.hasNext()) {
				if(!units.contains(it.next())) {
					it.remove();
				}
			}
		}
	}
	
	private Cluster findBestClusterFor(Unit unit) {
		Cluster bestCluster = null;
		double bestClusterDistance = 0;
		
		for(Cluster cluster : clusters) {
			double clusterDistance = cluster.getPosition().sub(unit.getPosition()).length;
			
			if(bestCluster == null || bestClusterDistance > clusterDistance) {
				bestCluster = cluster;
				bestClusterDistance = clusterDistance;
			}
		}
		
		return bestCluster;
	}
	
	public void drawOn(Minimap minimap) {
		for(Cluster cluster : clusters) {
			if(cluster.isEmpty()) {
				continue;
			}
			minimap.setColor(Graphics.Color.BLUE);
			for(Unit unit : cluster) {
				minimap.drawLine(unit.getPosition(), cluster.getPosition());
			}
			
			minimap.setColor(Graphics.Color.RED);
			minimap.fillCircle(cluster.getPosition(), 5);
		}
	}
}
