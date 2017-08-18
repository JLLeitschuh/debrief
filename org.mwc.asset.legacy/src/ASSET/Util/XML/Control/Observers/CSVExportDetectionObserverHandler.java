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
package ASSET.Util.XML.Control.Observers;

import ASSET.Models.Decision.TargetType;
import ASSET.Scenario.Observers.ScenarioObserver;
import ASSET.Scenario.Observers.Recording.CSVExportDetectionsObserver;
import ASSET.Util.XML.Decisions.Util.TargetTypeHandler;


/**
 * read in a debrief replay observer from file
 */
abstract class CSVExportDetectionObserverHandler extends CoreFileObserverHandler
{

  private final static String type = "CSVDetectionObserver";

  private TargetType _targetType = null;
  private String _subjectName = null;
  private String _sensorName = null;
  private static final String SENSOR_NAME = "SensorName";
  private static final String SUBJECT_NAME = "SubjectName";
  private static final String TARGET_TYPE = "SubjectToTrack";


  public CSVExportDetectionObserverHandler(String type)
  {
    super(type);

    addHandler(new TargetTypeHandler(TARGET_TYPE)
    {
      public void setTargetType(TargetType type1)
      {
        _targetType = type1;
      }
    });
    addAttributeHandler(new HandleAttribute(SUBJECT_NAME)
    {
      public void setValue(String name, final String val)
      {
        _subjectName = val;
      }
    });
    addAttributeHandler(new HandleAttribute(SENSOR_NAME)
    {
      public void setValue(String name, final String val)
      {
        _sensorName = val;
      }
    });
  }

  public CSVExportDetectionObserverHandler()
  {
    this(type);
  }

  public void elementClosed()
  {
    // create ourselves
    final ScenarioObserver debriefObserver = getObserver(_name, _isActive, _targetType, _subjectName, _sensorName);

    setObserver(debriefObserver);

    // close the parenet
    super.elementClosed();

    // and clear the data
    _targetType = null;
    _sensorName = null;
    _subjectName = null;
  }

  protected ScenarioObserver getObserver(String name, boolean isActive, TargetType subject, String subjectName, String sensorName)
  {
    return new CSVExportDetectionsObserver(_directory, _fileName, subject, name, isActive, subjectName, sensorName);
  }


  abstract public void setObserver(ScenarioObserver obs);

  static public void exportThis(final Object toExport, final org.w3c.dom.Element parent,
                                final org.w3c.dom.Document doc)
  {
    // create ourselves
    final org.w3c.dom.Element thisPart = doc.createElement(type);

    // get data item
    final CSVExportDetectionsObserver bb = (CSVExportDetectionsObserver) toExport;

    // output the parent ttributes
    CoreFileObserverHandler.exportThis(bb, thisPart);

    // output it's attributes
    if (bb.getSubjectToTrack() != null)
    {
      TargetTypeHandler.exportThis(TARGET_TYPE, bb.getSubjectToTrack(), thisPart, doc);
    }
    if(bb.getSensorName() != null)
    {
      TargetTypeHandler.exportThis(SENSOR_NAME, bb.getSensorName(), thisPart, doc);      
    }
    if(bb.getSubjectName() != null)
    {
      TargetTypeHandler.exportThis(SUBJECT_NAME, bb.getSubjectName(), thisPart, doc);      
    }

    
    // output it's attributes
    parent.appendChild(thisPart);

  }


}