
package org.mwc.debrief.core.actions.drag;

import org.eclipse.swt.graphics.Cursor;
import org.mwc.cmap.core.CursorRegistry;

import Debrief.Wrappers.FixWrapper;
import Debrief.Wrappers.TrackWrapper;
import Debrief.Wrappers.Track.CoreTMASegment;
import Debrief.Wrappers.Track.TrackSegment;
import MWC.GUI.Layers;
import MWC.GUI.Shapes.DraggableItem;
import MWC.GenericData.WorldLocation;
import MWC.GenericData.WorldVector;

public class StretchDragMode extends RotateDragMode
{

	
	public static class StretchOperation extends RotateOperation
	{
//		private Double lastRange;

		public StretchOperation(final WorldLocation cursorLoc, final WorldLocation origin,
				final CoreTMASegment segment, final TrackWrapper parent, final Layers theLayers)
		{
			super(cursorLoc, origin, segment, parent, theLayers);
		}

		public void shift(final WorldVector vector)
		{
			final CoreTMASegment seg = (CoreTMASegment) _segment;
			
			// find out where the cursor currently is
			workingLoc.addToMe(vector);
	
			// what's the bearing from the origin
			final WorldVector thisVector = workingLoc.subtract(_origin);
	
			// work out the distance from the start
			final double rng =  thisVector.getRange(); //- _originalDistDegs;
	
			// undo the previous turn
      // NO: we don't need to undo the previous operation. we aren't doing an XOR 
      // paint any more
//			if (lastRange != null)
//			{
//				seg.stretch(-lastRange, _origin);
//			}
	
			// now do the current one
			seg.stretch(rng, _origin);
			
			// and remember it
	//		lastRange = new Double(rng);
			
			// and tell the props view to update itself
			updatePropsView(seg, _parent, _layers);

			
		}
	
		public Cursor getHotspotCursor()
		{			
			return CursorRegistry.getCursor(CursorRegistry.SELECT_FEATURE_HIT_STRETCH);
		}
	}
	
	/**
	 * generate an operation for when the centre of the line segment is dragged
	 * 
	 * @param seg
	 *          the segment being dragged
	 * @return an operation we can use to do this
	 */
	protected DraggableItem getCentreOperation(final TrackSegment seg, final TrackWrapper parent, final Layers theLayers)
	{
		return new StretchFanOperation(seg, parent, theLayers);
	}


	public StretchDragMode()
	{
		super("Stretch", "Apply stretch operation to TMA solution");
	}

	@Override
	protected DraggableItem getEndOperation(final WorldLocation cursorLoc,
			final TrackSegment seg, final FixWrapper subject, final TrackWrapper parent, final Layers theLayers)
	{
		return new StretchOperation(cursorLoc, subject.getFixLocation(), (CoreTMASegment) seg, parent, theLayers);
	}

	@Override
	protected boolean isAcceptable(final TrackSegment seg)
	{
		return (seg instanceof CoreTMASegment);
	}		
	
	
}
