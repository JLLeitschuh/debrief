
package org.mwc.debrief.core.creators.shapes;

import MWC.GUI.Shapes.FurthestOnCircleShape;
import MWC.GUI.Shapes.PlainShape;
import MWC.GenericData.WorldLocation;
import MWC.GenericData.WorldSpeed;

/**
 * @author ian.mayo
 * 
 */
public class InsertFurthestOnCircles extends CoreInsertShape
{

	/**
	 * produce the shape for the user
	 * 
	 * @param centre
	 *          the current centre of the screen
	 * @return a shape, based on the centre
	 */
	protected PlainShape getShape(final WorldLocation centre)
	{
		// generate the shape
		final PlainShape res = new FurthestOnCircleShape(centre, 5, new WorldSpeed(10,
				WorldSpeed.Kts), FurthestOnCircleShape.DEFAULT_INTERVAL, 180, 360);
		return res;
	}

	/**
	 * return the name of this shape, used give the shape an initial name
	 * 
	 * @return the name of this type of shape, eg: rectangle
	 */
	protected String getShapeName()
	{
		return "Furthest On Circle";
	}

}