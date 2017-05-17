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
package org.mwc.debrief.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.mwc.cmap.core.CorePlugin;

import Debrief.Wrappers.Track.DynamicInfillSegment;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences
   * ()
   */
  public void initializeDefaultPreferences()
  {
    final IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
    store.setDefault(PrefsPage.PreferenceConstants.AUTO_SELECT, true);
    store.setDefault(PrefsPage.PreferenceConstants.CALC_SLANT_RANGE, false);
    store.setDefault(PrefsPage.PreferenceConstants.DONT_SHOW_DRAG_IN_PROPS,
        true);
    store.setDefault(PrefsPage.PreferenceConstants.ASK_ABOUT_PROJECT, true);
    store.setDefault(PrefsPage.PreferenceConstants.INFILL_COLOR_STRATEGY,
        DynamicInfillSegment.RANDOM_INFILL);
    store.setDefault(PrefsPage.PreferenceConstants.USE_CUT_COLOR, true);
    store.setDefault(PrefsPage.PreferenceConstants.CUT_OFF_VALUE_DEGS, 3);
    
  }

}