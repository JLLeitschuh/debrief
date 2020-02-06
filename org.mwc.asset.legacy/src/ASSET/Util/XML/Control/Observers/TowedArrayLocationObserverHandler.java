
package ASSET.Util.XML.Control.Observers;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import ASSET.Models.Decision.TargetType;
import ASSET.Scenario.Observers.CoreObserver;
import ASSET.Scenario.Observers.ScenarioObserver;
import ASSET.Scenario.Observers.Recording.TowedArrayLocationObserver;
import ASSET.Scenario.Observers.Recording.TowedArrayLocationObserver.RecorderType;
import ASSET.Util.XML.Decisions.Util.TargetTypeHandler;

abstract class TowedArrayLocationObserverHandler extends
    CoreFileObserverHandler
{

  private final static String type = "TowedArrayLocationObserver";

  TargetType _subjectName;
  List<Double> offsets;
  String messageName;
  Double defaultDepth;
  String sensorName;

  public TowedArrayLocationObserverHandler()
  {
    super(type);

    // add the other handlers
    addHandler(new TargetTypeHandler("SubjectToTrack")
    {
      public void setTargetType(TargetType type1)
      {
        _subjectName = type1;
      }
    });
    addAttributeHandler(new HandleAttribute("OFFSETS")
    {
      public void setValue(String name, final String val)
      {
        offsets = new ArrayList<Double>();

        String[] items = val.split(",");
        for (int i = 0; i < items.length; i++)
        {
          String string = items[i];
          offsets.add(Double.parseDouble(string));
        }
      }
    });
    addAttributeHandler(new HandleAttribute("MESSAGE_NAME")
    {
      public void setValue(String name, final String val)
      {
        messageName = val;
      }
    });
    addAttributeHandler(new HandleDoubleAttribute("DEFAULT_DEPTH")
    {
      @Override
      public void setValue(String name, double value)
      {
        defaultDepth = value;
      }
    });
    addAttributeHandler(new HandleAttribute("SENSOR_NAME")
    {
      public void setValue(String name, final String val)
      {
        sensorName = val;
      }
    });
  }

  public void elementClosed()
  {
    defaultDepth = 12d;

    // handle the message type
    final RecorderType recorderType;

    // sort out the recorder type, and do some checking
    switch (messageName)
    {
    case "TA_FORE_AFT":
      // just two lengths
      if (offsets.size() == 2)
      {
        // ok, safe
        recorderType = RecorderType.HDG_DEPTH;
      }
      else
      {
        System.err
            .println("Wrong number of array offsets supplied. Should be two (fore/after), but got "
                + offsets.size());
        recorderType = null;
      }
      break;
    case "TA_MODULES":
      recorderType = RecorderType.HDG_DEPTH;
      break;
    case "TA_COG_REL":
      // just two lengths
      if (offsets.size() == 1)
      {
        // ok, safe
        recorderType = RecorderType.LOC_RELATIVE;
      }
      else
      {
        System.err
            .println("Wrong number of array offsets supplied. Should be one (Centre of Graviry), but got "
                + offsets.size());
        recorderType = null;
      }
      break;
    case "TA_COG_ABS":
      // just two lengths
      if (offsets.size() == 1)
      {
        // ok, safe
        recorderType = RecorderType.LOC_ABS;
      }
      else
      {
        System.err
            .println("Wrong number of array offsets supplied. Should be one (Centre of Graviry), but got "
                + offsets.size());
        recorderType = null;
      }
      break;
    default:
    {
      System.err
          .println("Towed Array Location Observer Handler : Message format not found for:"
              + messageName);
      recorderType = null;
    }
    }

    // have we got suitable data?
    if (recorderType != null)
    {
      // create ourselves
      final CoreObserver timeO =
          new TowedArrayLocationObserver(_directory, _fileName, _subjectName,
              _name, _isActive, offsets, recorderType, messageName,
              defaultDepth, sensorName);

      setObserver(timeO);

    }

    // and reset
    super.elementClosed();

    // clear the params
    _subjectName = null;
    messageName = null;
    defaultDepth = null;
    sensorName = null;
  }

  abstract public void setObserver(ScenarioObserver obs);

  static public void exportThis(final Object toExport,
      final org.w3c.dom.Element parent, final org.w3c.dom.Document doc)
  {
    throw new UnsupportedOperationException("Method not implemented");
  }

}