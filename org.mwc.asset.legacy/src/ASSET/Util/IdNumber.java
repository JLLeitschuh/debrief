/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package ASSET.Util;

/**
 * Title: IdNumber
 * Description: Generates unique id numbers
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Ian Mayo
 * @version 1.0
 */

public class IdNumber {

  /** the randomizer we use for generation
   *
   */
  private static java.util.Random _randomizer = new java.util.Random();

  /** largest integer we create
   *
   */
  private static int _limit = 1000000;

  public static int generateInt()
  {
    return _randomizer.nextInt(_limit);
  }
}