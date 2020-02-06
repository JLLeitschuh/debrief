
package Debrief.ReaderWriter.XML.Tactical;

/**
 * Title:        Debrief 2000
 * Description:  Debrief 2000 Track Analysis Software
 * Copyright:    Copyright (c) 2000
 * Company:      MWC
 * @author Ian Mayo
 * @version 1.0
 */

import java.awt.Color;

import org.w3c.dom.Element;

import Debrief.GUI.Frames.Application;
import Debrief.Wrappers.FixWrapper;
import Debrief.Wrappers.Track.DynamicInfillSegment;
import Debrief.Wrappers.Track.TrackSegment;

abstract public class DynamicInfillSegmentHandler extends
    CoreTrackSegmentHandler
{
  private static final String DYNAMIC_SEGMENT = "DynamicInfillSegment";
  private static final String BEFORE = "BeforeLeg";
  private static final String AFTER = "AfterLeg";

  private String _beforeName;
  private String _afterName;

  public DynamicInfillSegmentHandler()
  {
    // inform our parent what type of class we are
    super(DYNAMIC_SEGMENT);

    addAttributeHandler(new HandleAttribute(BEFORE)
    {
      @Override
      public void setValue(String name, String value)
      {
        _beforeName = value;
      }
    });
    addAttributeHandler(new HandleAttribute(AFTER)
    {
      @Override
      public void setValue(String name, String value)
      {
        _afterName = value;
      }
    });
  }

  @Override
  protected TrackSegment createTrack()
  {
    // get the color form the first item
    final Color fixColor;
    if (super._fixes.isEmpty())
    {
      // no fixes in solution.
      // updated logic means this shouldn't happen.
      // But, robustly handle it, if it does.
      fixColor = Color.red;
    }
    else
    {
      FixWrapper first = super._fixes.firstElement();
      fixColor = first.getColor();
    }

    final TrackSegment res;
    if (_fixes.size() <= 1)
    {
      // ok. We used to allow single point infills,
      // now we don't. Skip this infill
      res = null;

      // log this, we may forget we're consciously skipping such short ones
      Application.logError2(Application.WARNING,
          "We're deliberately skipping this one-point infill:" + _name, null);
    }
    else
    {
      res = new DynamicInfillSegment(_beforeName, _afterName, fixColor);
    }
    return res;
  }

  public static void exportThisSegment(final org.w3c.dom.Document doc,
      final Element trk, final DynamicInfillSegment seg)
  {
    final Element segE =
        CoreTrackSegmentHandler.exportThisSegment(doc, seg, DYNAMIC_SEGMENT);

    // sort out the remaining attributes
    segE.setAttribute(BEFORE, seg.getBeforeName());
    segE.setAttribute(AFTER, seg.getAfterName());

    trk.appendChild(segE);
  }
}