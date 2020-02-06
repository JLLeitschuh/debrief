
package ASSET.Util.XML.Sensors;

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

import org.w3c.dom.Element;

import ASSET.Models.SensorType;
import ASSET.Models.Sensor.Lookup.*;
import ASSET.Util.XML.Sensors.Lookup.*;


abstract  public class SensorFitHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
{
  private final static String type = "SensorFit";
  private final static String NAME = "Name";
	private final static String WORKING = "Working";

  private ASSET.Models.Sensor.SensorList _myList;
	
	Boolean _isWorking = null;
	String _myName = null;

  public SensorFitHandler()
  {
    // inform our parent what type of class we are
    super(type);


    super.addAttributeHandler(new HandleBooleanAttribute(WORKING )
    {
			public void setValue(String name, boolean value)
			{
				_isWorking = new Boolean(value);
			}
    });    
    super.addAttributeHandler(new HandleAttribute(NAME)
    {
			public void setValue(String name, String value)
			{
				_myName = value;
			}
    });    
    addHandler(new TypedCookieSensorHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new TypedCookieInterceptHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    
    addHandler(new PlainCookieSensorHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.OpticSensorHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.BroadbandHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.ActiveBroadbandHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.DippingActiveBroadbandHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.NarrowbandHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.ActiveInterceptHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new ASSET.Util.XML.Sensors.BistaticReceiverHandler()
    {
      public void addSensor(final SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new OpticLookupSensorHandler()
    {
      public void addSensor(SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new RadarLookupSensorHandler()
    {
      public void addSensor(SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
    addHandler(new MADLookupSensorHandler()
    {
      public void addSensor(SensorType sensor)
      {
        addThisSensor(sensor);
      }
    });
  }


  public void elementClosed()
  {
  	if(_isWorking != null)
  		_myList.setWorking(_isWorking.booleanValue());
  	if(_myName != null)
  		_myList.setName(_myName);
  	
    setSensorFit(_myList);
    
    _myList = null;
    _myName = null;
    _isWorking = null;
  }

  void addThisSensor(final SensorType sensor)
  {
    if (_myList == null)
      _myList = new ASSET.Models.Sensor.SensorList();

    _myList.add(sensor);
  }

  abstract public void setSensorFit(ASSET.Models.Sensor.SensorList list);


  public static void exportThis(final ASSET.ParticipantType participant, final org.w3c.dom.Element parent,
                                final org.w3c.dom.Document doc)
  {
    // create ourselves
    final Element sens = doc.createElement(type);
    
    // store it's name
    sens.setAttribute(WORKING, writeThis(participant.getSensorFit().isWorking()));
    sens.setAttribute(NAME, participant.getSensorFit().getName());

    // step through data
    final int len = participant.getNumSensors();
    for (int i = 0; i < len; i++)
    {
      final ASSET.Models.SensorType sensor = participant.getSensorAt(i);

      // see what type it is
      if (sensor instanceof ASSET.Models.Sensor.Initial.DippingActiveBroadbandSensor)
      {
        DippingActiveBroadbandHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Initial.ActiveBroadbandSensor)
      {
        ActiveBroadbandHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Initial.BroadbandSensor)
      {
        BroadbandHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Initial.OpticSensor)
      {
        OpticSensorHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Cookie.TypedCookieSensor)
      {
        PlainCookieSensorHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Cookie.PlainCookieSensor)
      {
        PlainCookieSensorHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Initial.NarrowbandSensor)
      {
        NarrowbandHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Initial.ActiveInterceptSensor)
      {
        ActiveInterceptHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof ASSET.Models.Sensor.Initial.BistaticReceiver)
      {
        BistaticReceiverHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof OpticLookupSensor)
      {
        OpticLookupSensorHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof MADLookupSensor)
      {
        MADLookupSensorHandler.exportThis(sensor, sens, doc);
      }
      if (sensor instanceof RadarLookupSensor)
      {
        RadarLookupSensorHandler.exportThis(sensor, sens, doc);
      }
    }

    parent.appendChild(sens);

  }


}