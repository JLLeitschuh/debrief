
package Debrief.ReaderWriter.XML.Tactical;

/**
 * Title:        Debrief 2000
 * Description:  Debrief 2000 Track Analysis Software
 * Copyright:    Copyright (c) 2000
 * Company:      MWC
 * @author Ian Mayo
 * @version 1.0
 */

import org.w3c.dom.Element;

import Debrief.Wrappers.Track.CoreTMASegment;
import MWC.GenericData.WorldSpeed;
import MWC.Utilities.ReaderWriter.XML.Util.WorldSpeedHandler;

abstract public class CoreTMASegmentHandler extends CoreTrackSegmentHandler
{
	public static final String COURSE_DEGS = "CourseDegs";
	public static final String SPEED= "Speed";

	protected double _courseDegs = 0d;
	protected  WorldSpeed _speed;
	
	public CoreTMASegmentHandler(final String myName)
	{
		// inform our parent what type of class we are
		super(myName);

		
		addAttributeHandler(new HandleDoubleAttribute(COURSE_DEGS)
		{
			@Override
			public void setValue(final String name, final double val)
			{
				_courseDegs = val;
			}
		});		
		addHandler(new WorldSpeedHandler(SPEED){
			@Override
			public void setSpeed(final WorldSpeed res)
			{
				_speed = res;
			}
		});
	}
	
	public static void exportThisTMASegment(final org.w3c.dom.Document doc, final CoreTMASegment theSegment, final Element theElement)
	{
		// sort out the remaining attributes
		theElement.setAttribute(COURSE_DEGS, writeThis(theSegment.getCourse()));
		WorldSpeedHandler.exportSpeed(SPEED, theSegment.getSpeed(), theElement, doc);
	}

}