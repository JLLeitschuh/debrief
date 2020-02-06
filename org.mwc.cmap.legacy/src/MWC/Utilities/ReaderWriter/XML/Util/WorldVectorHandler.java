
package MWC.Utilities.ReaderWriter.XML.Util;


/*******************************************************************************
 * Debrief - the Open Source Maritime Analysis Application
 * http://debrief.info
 *  
 * (C) 2000-2020, Deep Blue C Technology Ltd
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html)
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *******************************************************************************/

import MWC.GenericData.WorldDistance;
import MWC.GenericData.WorldVector;
import MWC.Utilities.ReaderWriter.XML.MWCXMLReader;


abstract public class WorldVectorHandler extends MWCXMLReader
{

  WorldDistance _range;
  double _bearingDegs;
  private static final String BEARING = "BearingDegs";
  private static final String RANGE = "Range";

  public WorldVectorHandler(final String myType)
  {
    super(myType);

    addHandler(new WorldDistanceHandler(RANGE){
		
			@Override
			public void setWorldDistance(final WorldDistance res)
			{
				_range = res;
			}
		});

    addAttributeHandler(new HandleDoubleAttribute(BEARING)
    {
      public void setValue(final String name, final double val)
      {
      	_bearingDegs = val;
      }
    });
  }


  public WorldVectorHandler()
  {
    // inform our parent what type of class we are
    this("WorldDistance");
  }


  public void elementClosed()
  {
  	final WorldVector res = new WorldVector(MWC.Algorithms.Conversions.Degs2Rads(_bearingDegs),
  			_range, null);
    setWorldVector(res);

    // reset the units.  If it doesn't get overwritten we continue to use NM
    _range = null;
    _bearingDegs = 0;
  }

  abstract public void setWorldVector(WorldVector res);



  public static void exportVector(final String myType, final WorldVector vector, final org.w3c.dom.Element parent,
                                    final org.w3c.dom.Document doc)
  {
  	// create the object
    final org.w3c.dom.Element eLoc = doc.createElement(myType);
    
    // store the fields
    eLoc.setAttribute(BEARING, writeThis(MWC.Algorithms.Conversions.Rads2Degs(vector.getBearing())));
    final WorldDistance dist = new WorldDistance(vector.getRange(), WorldDistance.DEGS);
    WorldDistanceHandler.exportDistance(RANGE, dist, eLoc, doc);

    // remember it
    parent.appendChild(eLoc);
  }

}