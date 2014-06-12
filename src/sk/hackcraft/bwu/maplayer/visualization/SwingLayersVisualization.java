package sk.hackcraft.bwu.maplayer.visualization;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.sun.prism.paint.Color;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerDrawable;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.MatrixLayer;

public class SwingLayersVisualization
{
	private final JFrame window;
	
	private final List<Layer> layers;
	private final List<Layer> activeLayers;
	
	private final Map<Layer, LayerDrawable> layersDrawables;
	private final Map<Layer, String> layersNames;
	
	private final LayersCanvas canvas;
	private final JList<String> layersList;
	private final JList<String> activeLayersList;
	
	public SwingLayersVisualization(LayersPainter layersPainter)
	{
		window = new JFrame("Map layers");
		
		window.setSize(300, 300);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		layers = new ArrayList<>();
		activeLayers = new ArrayList<>();
		
		layersDrawables = new HashMap<>();
		layersNames = new HashMap<>();
		
		Container contentPane = window.getContentPane();
		contentPane.setLayout(new GridBagLayout());
		
		canvas = new LayersCanvas(layersPainter);
		layersList = new JList<>();
		activeLayersList = new JList<>();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		
		contentPane.add(canvas, c);
		//contentPane.add(comp)
		
		new Timer(20, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				canvas.repaint();
			}
		}).start();
	}
	
	public void addLayer(String name, Layer layer, LayerDrawable drawable)
	{
		layers.add(layer);
		
		layersDrawables.put(layer, drawable);
		layersNames.put(layer, name);
	}
	
	public void start()
	{
		window.setVisible(true);
	}
	
	public void close()
	{
		window.dispose();
	}
	
	/*private class LayersListModel implements ListModel<String>
	{
		private final List<Layer> backingList;
		
		private final List<ListDataListener> listDataListeners;

		public LayersListModel(List<Layer> layers)
		{
			this.backingList = layers;
			this.listDataListeners = new ArrayList<>();
		}
		
		@Override
		public int getSize()
		{
			return backingList.size();
			
			listDataListeners.get(0).
		}

		@Override
		public String getElementAt(int index)
		{
			Layer layer = backingList.get(index);
			return layersNames.get(layer);
		}

		@Override
		public void addListDataListener(ListDataListener l)
		{
			listDataListeners.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l)
		{
			listDataListeners.remove(l);
		}
	}*/
}
