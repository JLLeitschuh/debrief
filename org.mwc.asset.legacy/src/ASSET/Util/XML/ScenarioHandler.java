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

package ASSET.Util.XML;

import java.text.DateFormat;
import java.util.Date;

import ASSET.ScenarioType;
import ASSET.GUI.Workbench.Plotters.ScenarioLayer;
import ASSET.Models.Environment.EnvironmentType;
import ASSET.Models.Environment.SimpleEnvironment;
import ASSET.Scenario.CoreScenario;
import ASSET.Util.XML.Utils.EnvironmentHandler;
import ASSET.Util.XML.Utils.MockLayerHandler;
import MWC.GUI.BaseLayer;
import MWC.GUI.Layer;
import MWC.GUI.Layers;
import MWC.GenericData.Duration;
import MWC.Utilities.ReaderWriter.XML.LayerHandler;
import MWC.Utilities.ReaderWriter.XML.Util.DurationHandler;
import MWC.Utilities.TextFormatting.GMTDateFormat;

public class ScenarioHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
{

  private static final String DEBRIEF_LAYER_NAME = "DebriefLayer";

	CoreScenario _theScenario;
	Layers _myLayers = null;

  static final public String type = "Scenario";
  static final private String TIME = "StartTime";
  private static final String NAME_ATTRIBUTE = "Name";
  private static final String CASE_ATTRIBUTE = "Case";
  private static final String SCENARIO_STEP_TIME = "StepTime";
  private static final String SCENARIO_STEP_PAUSE = "StepPause";
  

  public ScenarioHandler(final ASSET.Scenario.CoreScenario theScenario)
  {
  	this(theScenario, null);
  }

  public ScenarioHandler(final ASSET.Scenario.CoreScenario theScenario, Layers theLayers)
  {
    // inform our parent what type of class we are
    super(type);

    _theScenario = theScenario;
    _myLayers = theLayers;

    // sort out the handlers
    addAttributeHandler(new HandleDateTimeAttribute(TIME)
    {
      public void setValue(String name, final long val)
      {
        _theScenario.setTime(val);
      }
    });
    super.addAttributeHandler(new HandleAttribute(NAME_ATTRIBUTE)
    {
      public void setValue(String name, final String val)
      {
        // store the name
        _theScenario.setName(val);
      }
    });
    super.addAttributeHandler(new HandleAttribute(CASE_ATTRIBUTE)
    {
      public void setValue(String name, final String val)
      {
        // store the name
        _theScenario.setCaseId(val);
      }
    });
    
    super.addAttributeHandler(new HandleAttribute(ScenarioLayer.SHOW_ACTIVITY)
    {
      public void setValue(String name, final String val)
      {
        // store the name
        _theScenario.addDisplaySetting(name, val);
      }
    });
    super.addAttributeHandler(new HandleAttribute(ScenarioLayer.SHOW_NAME)
    {
      public void setValue(String name, final String val)
      {
        // store the name
        _theScenario.addDisplaySetting(name, val);
      }
    });
    super.addAttributeHandler(new HandleAttribute(ScenarioLayer.SHOW_STATUS)
    {
      public void setValue(String name, final String val)
      {
        // store the name
        _theScenario.addDisplaySetting(name, val);
      }
    });
    super.addAttributeHandler(new HandleAttribute(ScenarioLayer.SHOW_SYMBOL)
    {
      public void setValue(String name, final String val)
      {
        // store the name
        _theScenario.addDisplaySetting(name, val);
      }
    });
    
    
    

    // does the scenario have it's scenario object?
    addHandler(new ParticipantsHandler(theScenario));


    addHandler(new EnvironmentHandler()
    {
      public void setEnvironment(EnvironmentType theEnv)
      {
        _theScenario.setEnvironment(theEnv);
      }
    });

    addHandler(new DurationHandler(SCENARIO_STEP_TIME)
    {
      public void setDuration(Duration res)
      {
        _theScenario.setScenarioStepTime((int) res.getMillis());
      }
    });
    addHandler(new DurationHandler(SCENARIO_STEP_PAUSE)
    {
      public void setDuration(Duration res)
      {
        _theScenario.setStepTime((int) res.getMillis());
      }
    });
    addHandler(new MockLayerHandler(DEBRIEF_LAYER_NAME)
    {
      public void setLayer(BaseLayer theLayer)
      {
      	if(_myLayers != null)
      		_myLayers.addThisLayer(theLayer);
      }
    });
    
  }

  public static org.w3c.dom.Element exportScenario(final ScenarioType scenario, Layer theDecorations, final org.w3c.dom.Document doc)
  {
    final org.w3c.dom.Element scen = doc.createElement(type);
    DateFormat xmlFormatter = new GMTDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    scen.setAttribute("Created",xmlFormatter.format(new java.util.Date()));
    scen.setAttribute(NAME_ATTRIBUTE, "ASSET Scenario");
    scen.setAttribute(TIME, writeThisInXML(new Date(scenario.getTime())));   
    
    DurationHandler.exportDuration(SCENARIO_STEP_TIME, new Duration(scenario.getScenarioStepTime(), Duration.MILLISECONDS), scen, doc);
    DurationHandler.exportDuration(SCENARIO_STEP_PAUSE, new Duration(scenario.getStepTime(), Duration.MILLISECONDS), scen, doc);

    if (scenario.getCaseId() != null)
      scen.setAttribute(CASE_ATTRIBUTE, scenario.getCaseId());

    EnvironmentType env = scenario.getEnvironment();
    if (env instanceof SimpleEnvironment)
      EnvironmentHandler.exportEnvironment((SimpleEnvironment) env, scen, doc);

    ParticipantsHandler.exportThis(scenario, scen, doc);
    
    // and now the graphic layers item
    final org.w3c.dom.Element layerHolder = doc.createElement(DEBRIEF_LAYER_NAME);
    scen.appendChild(layerHolder);
    Layer backdropLayer = scenario.getBackdrop();
    if(backdropLayer != null)
    	LayerHandler.exportLayer((BaseLayer) scenario.getBackdrop(), layerHolder, doc);

    return scen;
  }


}