/*******************************************************************************
 * Debrief - the Open Source Maritime Analysis Application
 * http://debrief.info
 *
 * (C) 2000-2020, Deep Blue C Technology Ltd
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html)
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *******************************************************************************/
/**
 *
 */
package org.mwc.debrief.core.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.mwc.cmap.core.CorePlugin;
import org.mwc.debrief.core.preferences.PrefsPage.PreferenceConstants;

import Debrief.ReaderWriter.Word.ImportNarrativeDocument.ImportNarrativeEnum;

/**
 * Dialog popped up from {@link ImportNarrativeHelper}
 *
 * @author Ayesha
 *
 */
public class ImportNarrativeDialog extends Dialog {
	private Button _btnLoadedTracks;
	private ImportNarrativeEnum userChoice = ImportNarrativeEnum.ALL_DATA;
	private boolean preference;

	private final SelectionListener selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (_btnLoadedTracks.getSelection()) {
				userChoice = ImportNarrativeEnum.TRIMMED_DATA;
			} else {
				userChoice = ImportNarrativeEnum.ALL_DATA;
			}
		}
	};

	public ImportNarrativeDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void cancelPressed() {
		userChoice = ImportNarrativeEnum.CANCEL;
		super.cancelPressed();
	}

	@Override
	protected void configureShell(final Shell newShell) {
		newShell.setText("Import Narrative Entries");

		super.configureShell(newShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite control = (Composite) super.createDialogArea(parent);
		final Composite composite = new Composite(control, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Label title = new Label(composite, SWT.BOLD);
		FontDescriptor descriptor = FontDescriptor.createFrom(title.getFont());
		// setStyle method returns a new font descriptor for the given style
		descriptor = descriptor.setStyle(SWT.BOLD);
		title.setFont(descriptor.createFont(title.getDisplay()));
		title.setText("Loading narrative data.");

		final Label intro = new Label(composite, SWT.NONE);
		intro.setText("Please select whether you wish to trim\n" + "the narrative entries to the period of the \n"
				+ "currently loaded tracks, or whether\n" + "all entries should be loaded");

		_btnLoadedTracks = new Button(composite, SWT.RADIO);
		_btnLoadedTracks.setText("Trim to loaded tracks");
		_btnLoadedTracks.addSelectionListener(selectionListener);
		_btnLoadedTracks.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		_btnLoadedTracks.setSelection(true);
		final Button _btnAllData = new Button(composite, SWT.RADIO);
		_btnAllData.setText("Load all data");
		_btnAllData.addSelectionListener(selectionListener);
		_btnAllData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NONE).setLayoutData(new GridData(GridData.FILL));
		final Button dontAskAgain = new Button(composite, SWT.CHECK);
		dontAskAgain.setText("Use this mode next time");
		dontAskAgain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				preference = dontAskAgain.getSelection();
				CorePlugin.getDefault().getPreferenceStore()
						.setValue(PreferenceConstants.REUSE_TRIM_NARRATIVES_DIALOG_CHOICE, preference);
			}
		});
		return control;
	}

	public boolean getPreference() {
		return preference;
	}

	public ImportNarrativeEnum getUserChoice() {
		return userChoice;
	}

}
