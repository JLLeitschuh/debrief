
package org.mwc.debrief.core.actions.drag;

import org.eclipse.swt.graphics.Cursor;
import org.mwc.cmap.core.CursorRegistry;
import org.mwc.debrief.core.actions.DragSegment.IconProvider;

import Debrief.Wrappers.Track.TrackSegment;
import MWC.GUI.Shapes.DraggableItem;
import MWC.GenericData.WorldVector;

public class TranslateOperation extends CoreDragOperation implements
		DraggableItem, IconProvider
{
	public TranslateOperation(final TrackSegment segment)
	{
		super(segment, "centre point");
	}

	public void shift(final WorldVector vector)
	{
		//
		_segment.shift(vector);
	}

	public Cursor getHotspotCursor()
	{
		return CursorRegistry.getCursor(CursorRegistry.SELECT_FEATURE_HIT_DRAG);
	}
}