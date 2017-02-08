package org.mwc.debrief.dis.listeners.impl;

import java.awt.Color;
import java.util.Iterator;

import Debrief.Wrappers.TrackWrapper;
import MWC.GUI.Layer;
import MWC.GUI.Layers;
import MWC.GUI.Layers.INewItemListener;
import MWC.GUI.Plottable;
import MWC.GUI.Properties.DebriefColors;

public class DebriefCoreListener
{
  protected final java.awt.Color[] defaultColors = new java.awt.Color[]
  {DebriefColors.RED, DebriefColors.GREEN, DebriefColors.BLUE,
      DebriefColors.CYAN, DebriefColors.MAGENTA, DebriefColors.ORANGE,
      DebriefColors.PINK};
  
  protected final IDISContext _context;

  public DebriefCoreListener(IDISContext context)
  {
    _context = context;
  }

  /**
   * get the default color for this name
   * 
   * @param name
   * @return
   */
  protected Color colorFor(short exerciseId, String name)
  {
    Color res;
    
    Layer layer = _context.findLayer(exerciseId, name);
    
    if(layer != null && layer instanceof TrackWrapper)
    {
      TrackWrapper track = (TrackWrapper) layer;
      res = track.getColor();
    }
    else
    {
      // ok, get the hashmap
      int index = Math.abs(name.hashCode()) % defaultColors.length;
      res = defaultColors[index];
    }
    
    return res;
    
  }

  /**
   * helper interface, that provides the data for adding new items
   * 
   * @author ian
   * 
   */
  public static interface ListenerHelper
  {
    /**
     * create the parent layer
     * 
     * @return
     */
    Layer createLayer();

    /**
     * create the parent item
     * 
     * @return
     */
    Plottable createItem();
  }

  protected Layer
      getLayer(short exerciseId, String name, ListenerHelper helper)
  {
    // find the narratives layer
    Layer nLayer = _context.findLayer(exerciseId, name);
    if (nLayer == null)
    {
      nLayer = helper.createLayer();
      nLayer.setName(name);

      // and store it
      _context.addThisLayer(nLayer);

      // share the news
      Iterator<INewItemListener> iter = _context.getNewItemListeners();
      if(iter != null)
      {
        while (iter.hasNext())
        {
          Layers.INewItemListener newI = (Layers.INewItemListener) iter.next();
          newI.newItem(nLayer, null, null);
        }
      }
    }
    return nLayer;
  }

  /**
   * add this item to the layer with the specified name
   * 
   * @param eid
   * @param layerName
   * @param item
   */
  protected void addNewItem(short eid, String layerName, ListenerHelper helper)
  {
    final Layer destination = getLayer(eid, layerName, helper);

    Plottable item = helper.createItem();

    destination.add(item);

    final Layer finalLayer = destination;

    // should we try any formatting?
    Iterator<INewItemListener> iter = _context.getNewItemListeners();
    if (iter != null)
    {
      while (iter.hasNext())
      {
        Layers.INewItemListener newI = (Layers.INewItemListener) iter.next();
        newI.newItem(finalLayer, item, null);
      }
    }

  }

}