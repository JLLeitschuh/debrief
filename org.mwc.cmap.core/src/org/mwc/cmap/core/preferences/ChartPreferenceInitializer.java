
package org.mwc.cmap.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.mwc.cmap.core.CorePlugin;
import org.mwc.cmap.core.preferences.ChartPrefsPage.PreferenceConstants;

public class ChartPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {

		// and store the default location
		final IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.CHART_TRANSPARENCY, "200");
		store.setDefault(PreferenceConstants.CHART_FOLDER, "");
	}

}