package sk.hackcraft.bwu.maplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.hackcraft.bwu.Comparison;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.maplayer.processors.GradientFloodFillProcessor;

public class PotentialFieldLayer extends MatrixLayer implements Updateable
{
	private final List<FieldEmitter> fieldEmitters;
	private final Map<FieldEmitter, CacheFieldEmitter> cacheEmitters;
	
	public PotentialFieldLayer(LayerDimension dimension)
	{
		super(dimension);
		
		this.fieldEmitters = new ArrayList<>();
		this.cacheEmitters = new HashMap<>();
	}
	
	public void addFieldEmitter(FieldEmitter emitter)
	{
		fieldEmitters.add(emitter);
		
		CacheFieldEmitter cacheFieldEmitter = new CacheFieldEmitter();
		
		cacheFieldEmitter.update(emitter);
		
		cacheEmitters.put(emitter, cacheFieldEmitter);
	}
	
	public void removeFieldEmitter(FieldEmitter emitter)
	{
		fieldEmitters.remove(emitter);
		CacheFieldEmitter cacheEmitter = cacheEmitters.remove(emitter);
		
		Set<LayerPoint> defillPoints = new HashSet<>();
		defillPoints.add(cacheEmitter.getPosition());
		GradientFloodFillProcessor.floodDefill(this, 0, defillPoints);
	}
	
	@Override
	public void update()
	{
		Set<LayerPoint> defillPoints = new HashSet<>();
		Set<LayerPoint> fillPoints = new HashSet<>();
		
		for (FieldEmitter emitter : fieldEmitters)
		{
			CacheFieldEmitter cacheFieldEmitter = cacheEmitters.get(emitter);
			
			if (!cacheFieldEmitter.equals(emitter))
			{
				LayerPoint fillPoint = emitter.getPosition();
				set(fillPoint, emitter.getValue());
				fillPoints.add(fillPoint);
				
				defillPoints.add(cacheFieldEmitter.getPosition());
				
				cacheFieldEmitter.update(emitter);
			}
		}
		
		Set<LayerPoint> borderPoints = GradientFloodFillProcessor.floodDefill(this, 0, defillPoints);
		
		fillPoints.addAll(borderPoints);
		
		GradientFloodFillProcessor.floodFill(this, fillPoints);
	}
	
	public interface FieldEmitter
	{
		int getValue();

		LayerPoint getPosition();
	}
	
	private class CacheFieldEmitter implements FieldEmitter
	{
		private int value = 0;
		private LayerPoint position = LayerPoint.ORIGIN;
		
		public void update(FieldEmitter sourceEmitter)
		{
			this.value = sourceEmitter.getValue();
			this.position = sourceEmitter.getPosition();
		}
		
		@Override
		public int getValue()
		{
			return value;
		}
		
		@Override
		public LayerPoint getPosition()
		{
			return position;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof FieldEmitter))
			{
				return false;
			}
			
			FieldEmitter emitter = (FieldEmitter)obj;
			
			return emitter.getValue() == value && emitter.getPosition().equals(position);
		}
		
		@Override
		public int hashCode()
		{
			return value * 31 + position.hashCode();
		}
	}
}
