
package MWC.GUI.S57.features;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

import MWC.GUI.CanvasType;
import MWC.GenericData.WorldLocation;

public class AreaFeature extends LineFeature
{

	public AreaFeature(final String name, final Double minScale, final Color defaultColor)
	{
		super(name, minScale, defaultColor);
	}

	public void doPaint(final CanvasType dest)
	{
		dest.setColor(getColor());
		for (final Iterator<Vector<WorldLocation>> iterator = _lines.iterator(); iterator.hasNext();)
		{
			final Vector<WorldLocation> thisLine = (Vector<WorldLocation>) iterator.next();

			final int npts = thisLine.size();
			final int[] xpts = new int[npts];
			final int[] ypts = new int[npts];

			int ctr = 0;

			for (final Iterator<WorldLocation> iter = thisLine.iterator(); iter.hasNext();)
			{
				final WorldLocation loc = (WorldLocation) iter.next();

				final Point screen = dest.toScreen(loc);
				
		    // handle unable to gen screen coords (if off visible area)
		    if(screen == null)
		      return;


        final Point pt = new Point(screen);
				xpts[ctr] = pt.x;
				ypts[ctr] = pt.y;
				ctr++;
			}
			// and plot it.
			dest.fillPolygon(xpts, ypts, npts);
		}
	}
}
