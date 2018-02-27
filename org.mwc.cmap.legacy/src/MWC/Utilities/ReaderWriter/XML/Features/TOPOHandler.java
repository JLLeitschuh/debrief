/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package MWC.Utilities.ReaderWriter.XML.Features;

/**
 * Title:        Debrief 2000
 * Description:  Debrief 2000 Track Analysis Software
 * Copyright:    Copyright (c) 2000
 * Company:      MWC
 * @author Ian Mayo
 * @version 1.0
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import MWC.GUI.Layers;
import MWC.GUI.Chart.Painters.ETOPOPainter;
import MWC.GUI.ETOPO.ETOPO_2_Minute;
import MWC.GUI.Tools.Palette.CreateTOPO;
import MWC.Utilities.ReaderWriter.XML.MWCXMLReader;
import MWC.Utilities.ReaderWriter.XML.Util.ColourHandler;

public class TOPOHandler extends MWCXMLReader
{

  java.awt.Color _theColor;
  boolean _isVisible;
  private final Layers _theLayers;
  Integer _scaleLocation;
  Boolean _showLand = null;
  String _contourDepths = null;
  Boolean _neShade = null;

  Boolean _showContours = null;
  Boolean _showBathy = null;
  
  private static final String NE_SHADES = "NE_SHADES";

  /**
   * class which contains list of textual representations of scale locations
   */
  static ETOPOPainter.KeyLocationPropertyEditor lp =
      new ETOPOPainter.KeyLocationPropertyEditor();

  public TOPOHandler(final Layers theLayers)
  {
    // inform our parent what type of class we are
    super("topo");

    _theLayers = theLayers;

    addAttributeHandler(new HandleBooleanAttribute("Visible")
    {
      public void setValue(final String name, final boolean value)
      {
        _isVisible = value;
      }
    });
    addAttributeHandler(new HandleBooleanAttribute(NE_SHADES)
    {
      public void setValue(final String name, final boolean value)
      {
        _neShade = value;
      }
    });
    addAttributeHandler(new HandleBooleanAttribute("ShowLand")
    {
      public void setValue(final String name, final boolean value)
      {
        _showLand = new Boolean(value);
      }
    });
    addAttributeHandler(new HandleBooleanAttribute("ShowBathy")
    {
      public void setValue(final String name, final boolean value)
      {
        _showBathy = new Boolean(value);
      }
    });
    addAttributeHandler(new HandleBooleanAttribute("ShowContours")
    {
      public void setValue(final String name, final boolean value)
      {
        _showContours = new Boolean(value);
      }
    });
    addHandler(new ColourHandler()
    {
      public void setColour(final java.awt.Color color)
      {
        _theColor = color;
      }
    });
    addAttributeHandler(new HandleAttribute("ScaleLocation")
    {
      public void setValue(final String name, final String val)
      {
        lp.setAsText(val);
        _scaleLocation = (Integer) lp.getValue();
      }
    });

    addAttributeHandler(new HandleAttribute("ContourDepths")
    {
      public void setValue(final String name, final String val)
      {
        _contourDepths = val;
      }
    });

  }

  public void elementClosed()
  {

    final String topoPath = CreateTOPO.getETOPOPath();

    if (topoPath == null)
    {
      System.err.println("TOPO PATH MISSING");
      return;
    }

    // create a Grid from this data
    final ETOPO_2_Minute painter = new ETOPO_2_Minute(topoPath);
    painter.setColor(_theColor);

    painter.setVisible(_isVisible);

    if (_showLand != null)
      painter.setShowLand(_showLand.booleanValue());
    if (_showBathy != null)
      painter.setBathyVisible(_showBathy.booleanValue());
    if (_showContours != null)
      painter.setContoursVisible(_showContours.booleanValue());
    if(_neShade != null)
    {
      painter.setNEShading(_neShade);
    }

    if (_scaleLocation != null)
      painter.setKeyLocation(_scaleLocation);

    if (_contourDepths != null)
      painter.setContourDepths(_contourDepths);

    _theLayers.addThisLayer(painter);

    // reset our variables
    _neShade = null;
    _theColor = null;
    _isVisible = false;
    _showLand = null;
    _showContours = null;
    _showBathy = null;
  }

  public static void exportThisPlottable(final MWC.GUI.Plottable plottable,
      final Element parent, final Document doc)
  {

    final ETOPO_2_Minute csp = (ETOPO_2_Minute) plottable;
    final Element etopo = doc.createElement("topo");

    // do the visibility
    etopo.setAttribute("Visible", writeThis(csp.getVisible()));
    etopo.setAttribute("ShowLand", writeThis(csp.getShowLand()));
    etopo.setAttribute("ShowBathy", writeThis(csp.isBathyVisible()));
    etopo.setAttribute("ShowContours", writeThis(csp.isContoursVisible()));
    etopo.setAttribute(NE_SHADES, writeThis(csp.isNEShading()));

    etopo.setAttribute("ContourDepths", csp.getContourDepths());

    lp.setValue(csp.getKeyLocation());
    etopo.setAttribute("ScaleLocation", lp.getAsText());
    // etopo.setAttribute("LineThickness", writeThis(csp.getLineThickness()));

    // do the colour
    ColourHandler.exportColour(csp.getColor(), etopo, doc);

    parent.appendChild(etopo);
  }

}