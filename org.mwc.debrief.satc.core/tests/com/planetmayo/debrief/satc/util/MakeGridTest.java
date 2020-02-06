
package com.planetmayo.debrief.satc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class MakeGridTest
{
	
	@Test
	public void testGridding() throws ParseException
	{
		WKTReader wkt = new WKTReader();
		Geometry geom = wkt
				.read("POLYGON ((0.0 3.0, 2.0 4.0, 4.0 4.0, 2.0 3.0, 0.0 3.0))");

		// how many points?
		final int num = 100;

		// ok, try the tesselate function
		long start = System.currentTimeMillis();
		ArrayList<Point> pts = MakeGrid.ST_Tile(geom, num, 6);
		System.out.println("elapsed:" + (System.currentTimeMillis() - start));
		assertNotNull("something returned", pts);
		assertEquals("correct num", 98, pts.size());
		for (Point po : pts)
		{
			assertEquals("point is in area", true, geom.contains(po));
		}
	}	
}
