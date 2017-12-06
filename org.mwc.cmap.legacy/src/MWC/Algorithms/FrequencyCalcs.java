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
package MWC.Algorithms;

import MWC.GenericData.WorldSpeed;

public class FrequencyCalcs
{

	/**
	 * calculate the doppler component of the observed frequency
	 * 
	 * @param theBearingRads
	 *          bearing to the target
	 * @param myCourseRads
	 *          ownship course
	 * @param mySpeedKts
	 *          ownship speed
	 * @param observedFreq
	 *          observed frequency (hz)
	 * @return ownship component of doppler
	 */
	public static double calcDopplerComponent(final double theBearingRads,
			final double myCourseRads, final double mySpeedKts,
			final double observedFreq)
	{

		final double speedOfSoundKts = 2951;
		final double relBearingRads = theBearingRads - myCourseRads;
		
		final double ownSpeedAlongKts = Math.cos(relBearingRads) * mySpeedKts;

		double dopplerOffset = (ownSpeedAlongKts * observedFreq) / speedOfSoundKts;

		return dopplerOffset;
	}

	/** calculate what the received frequency should in these circumstances
	 * 
	 * @param f0
	 * @param speedOfSoundKts
	 * @param rxSpeedKts
	 * @param rxCourseDegs
	 * @param txSpeedKts
	 * @param txCourseDegs
	 * @param bearingDegs
	 * @return
	 */
	public static double getPredictedFreq(double f0, double speedOfSoundKts,
			double rxSpeedKts, double rxCourseDegs, double txSpeedKts,
			double txCourseDegs, double bearingDegs)
	{
		// collate the data
		double C = new WorldSpeed(speedOfSoundKts, WorldSpeed.Kts)
				.getValueIn(WorldSpeed.M_sec);
		double rxSpeed = new WorldSpeed(rxSpeedKts, WorldSpeed.Kts)
				.getValueIn(WorldSpeed.M_sec);
		double txSpeed = new WorldSpeed(txSpeedKts, WorldSpeed.Kts)
				.getValueIn(WorldSpeed.M_sec);

		double bearingRads = Math.toRadians(bearingDegs);
		double rxCrse = Math.toRadians(rxCourseDegs);
		double txCrse = Math.toRadians(txCourseDegs);

		return calcPredictedFreqSI(C,
				rxCrse, txCrse,
				rxSpeed, txSpeed, bearingRads,
				f0);
	}

	/**
	 * perform doppler shift calculation Note: we receive dLat, dLong to support
	 * different range calculations. The Excel spreadsheet (DopplerEffect.xls)
	 * that is used to verify the algorithm uses flat-earth calcs. In normal use
	 * we wish to use round-earth calcs
	 * 
	 * @param SpeedOfSound
	 *          m/s
	 * @param osHeadingRads
	 *          rads
	 * @param tgtHeadingRads
	 *          rads
	 * @param osSpeed
	 *          m/s
	 * @param tgtSpeed
	 *          m/s
	 * @param bearing
	 *          radians
	 * @return
	 */
	public static double calcPredictedFreqSI(final double SpeedOfSound,
			final double osHeadingRads, final double tgtHeadingRads,
			final double osSpeed, final double tgtSpeed, double bearing,
			final double fNought)
	{
		final double relB = bearing - osHeadingRads;
		
		// note - contrary to some publications TSL uses the
		// angle along the bearing, not the angle back down the bearing (ATB).
		final double hisBearing = Math.PI + bearing;
		final double AngleOffTheOtherB = hisBearing - tgtHeadingRads;

		final double OSL = Math.cos(relB) * osSpeed;
		final double TSL = Math.cos(AngleOffTheOtherB) * tgtSpeed;

		final double freq = fNought * (SpeedOfSound + OSL) / (SpeedOfSound + TSL);

		return freq;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// testing for this class
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	static public final class testCalc extends junit.framework.TestCase
	{

		public void testDemoFigures()
		{
			// (figures as provided by Iain)
			double osKts = 2.5;
			double osDegs = 208.2;
			double tgtKts = 9;
			double tgtDegs = 350.17;
			double bearing = 260.47;
			double Ckts = 2951;
			double f0 = 150;

			double osMS = new WorldSpeed(osKts, WorldSpeed.Kts)
					.getValueIn(WorldSpeed.M_sec);
			double tgtMS = new WorldSpeed(tgtKts, WorldSpeed.Kts)
					.getValueIn(WorldSpeed.M_sec);
			double osRads = Math.toRadians(osDegs);
			double tgtRads = Math.toRadians(tgtDegs);
			double brgRads = Math.toRadians(bearing);
			double Cms = new WorldSpeed(Ckts, WorldSpeed.Kts)
					.getValueIn(WorldSpeed.M_sec);

			double relBrg = bearing - osDegs;
			double OSL = osKts * Math.cos(Math.toRadians(-relBrg));
			double ATB = tgtDegs - bearing;
			double TSL = tgtKts * Math.cos(Math.toRadians(ATB));

			assertEquals("correct OSL", 1.5298, OSL, 0.0001);
			assertEquals("correct TSL", 0.04712, TSL, 0.0001);

			double res = FrequencyCalcs.calcPredictedFreqSI(Cms, osRads, tgtRads, osMS, tgtMS, brgRads, f0);
//					calcObservedFreqCollate(f0, Cms, brgRads,
//					osRads, osMS, tgtRads, tgtMS);

			assertEquals("correct obs", 150.0750, res, 0.001);

		}

		// public void testDopplerShiftLowLevel()
		// {
		// final double SPEED_OF_SOUND = 1500;
		// final double F_NOUGHT = 150;
		//
		// final WorldLocation hostLoc = new WorldLocation(4, 4, 0);
		// final WorldLocation tgtLoc = new WorldLocation(4, 7, 0);
		// double hostCourse = MWC.Algorithms.Conversions.Degs2Rads(60);
		// double hostSpeed = 8d; // MWC.Algorithms.Conversions.Kts2Yps(8);
		// double tgtCourse = MWC.Algorithms.Conversions.Degs2Rads(300);
		// double tgtSpeed = 4; // MWC.Algorithms.Conversions.Kts2Yps(4);
		//
		// double dLat = hostLoc.getLat() - tgtLoc.getLat();
		// double dLong = hostLoc.getLong() - tgtLoc.getLong();
		//
		// double dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 1.00133, dShift, 0.00001d);
		//
		// // and another permutation
		// hostCourse = MWC.Algorithms.Conversions.Degs2Rads(12d);
		// tgtCourse = MWC.Algorithms.Conversions.Degs2Rads(333d);
		// hostSpeed = 3;
		// tgtSpeed = 4;
		//
		// dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 0.9995840, dShift, 0.00001d);
		//
		// // move to southern hemi
		// hostLoc.setLat(-4);
		// tgtLoc.setLat(-5);
		// hostCourse = 0;
		// tgtCourse = MWC.Algorithms.Conversions.Degs2Rads(180);
		//
		// dLat = hostLoc.getLat() - tgtLoc.getLat();
		// dLong = hostLoc.getLong() - tgtLoc.getLong();
		//
		// dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 1.004427, dShift, 0.00001d);
		//
		// hostCourse = MWC.Algorithms.Conversions.Degs2Rads(12d);
		// tgtCourse = MWC.Algorithms.Conversions.Degs2Rads(333d);
		// hostSpeed = 3;
		//
		// dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 0.99908, dShift, 0.00001d);
		//
		// hostLoc.setLong(-2);
		// tgtLoc.setLong(-3);
		// dLat = hostLoc.getLat() - tgtLoc.getLat();
		// dLong = hostLoc.getLong() - tgtLoc.getLong();
		//
		// dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 0.99914, dShift, 0.00001d);
		//
		// // slow down target, so they're divering more slowly
		// hostSpeed = 0.1;
		//
		// dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 1.000768, dShift, 0.000001d);
		//
		// // stop them both
		// hostSpeed = 0d;
		// tgtSpeed = 0d;
		//
		// dShift = calcDopplerShift(SPEED_OF_SOUND, hostCourse, tgtCourse,
		// hostSpeed, tgtSpeed, dLat, dLong, F_NOUGHT);
		//
		// assertEquals("correct doppler shift", 1.000, dShift, 0.000001d);
		// }
	}
}
