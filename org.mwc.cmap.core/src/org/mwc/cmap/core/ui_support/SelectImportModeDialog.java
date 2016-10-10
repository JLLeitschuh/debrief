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
package org.mwc.cmap.core.ui_support;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.mwc.cmap.core.CorePlugin;

import Debrief.ReaderWriter.Replay.ImportReplay;

/**
 * This class demonstrates how to create your own dialog classes. It allows users
 * to input a String
 */
public class SelectImportModeDialog extends Dialog implements SelectionListener {
  private final String message;
  private String input;
  
  private boolean _rememberIt;
  private String _mode = ImportReplay.IMPORT_AS_OTG;
  
  private long _resampleFrequency;

  /**
   * InputDialog constructor
   * 
   * @param parent the parent
   */
  public SelectImportModeDialog(final Shell parent, final String trackName) {
    // Pass the default styles here
    this(parent, trackName, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
  }

  /**
   * InputDialog constructor
   * 
   * @param parent the parent
   * @param style the style
   */
  public SelectImportModeDialog(final Shell parent, final String trackName, final int style) {
    // Let users override the default styles
    super(parent, style);
    setText("Select track mode");
    message = "Debrief can plot tracks using one of two modes." +
    		"\nUse this dialog to select how to import the track titled " + trackName + "." +
    		"\nYou can override your choice using the CMAP tab of Windows/Preferences";
  }


  /**
   * Gets the input
   * 
   * @return String
   */
  public String getInput() {
    return input;
  }

  /**
   * Sets the input
   * 
   * @param input the new input
   */
  public void setInput(final String input) {
    this.input = input;
  }

  /**
   * Opens the dialog and returns the input
   * 
   * @return String
   */
  public String open() {
    // Create the dialog window
    final Shell shell = new Shell(getParent(), getStyle());
    shell.setText(getText());
    createContents(shell);
    shell.pack();
    shell.open();
    final Display display = getParent().getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    // Return the entered value (will be null if user cancelled)
    return _mode;
  }
  
  public long getResampleFrequency()
  {
    return _resampleFrequency;
  }

 

	/**
   * Creates the dialog's contents
   * 
   * @param shell the dialog window
   */
  /**
   * @param shell
   */
  private void createContents(final Shell shell) {
    shell.setLayout(new GridLayout(1, true));

    // Show the message
    final Label label = new Label(shell, SWT.NONE);
    label.setText(message);
    GridData data = new GridData();
    data.horizontalSpan = 2;
    label.setLayoutData(data);

    // Display the radio button list

    final Button[] radios = new Button[2];

    radios[0] = new Button(shell, SWT.RADIO);
    radios[0].setText("Dead Reckoning (DR) - positions are calculated using recorded course and speed");
    radios[0].setData(ImportReplay.IMPORT_AS_DR);
    radios[0].addSelectionListener(this);

    radios[1] = new Button(shell, SWT.RADIO);
    radios[1].setText("Over The Ground (OTG) - where positions are plotted according to the recorded location");
    radios[1].setData(ImportReplay.IMPORT_AS_OTG);
    radios[1].addSelectionListener(this);
    radios[1].setSelection(true);
    

    {
      
      new Label(shell, SWT.NONE).setText("Resample frequency:");
      final ComboViewer comboViewer = new ComboViewer(shell);
      comboViewer.setContentProvider(new ArrayContentProvider());
      comboViewer.setLabelProvider( new ColumnLabelProvider()
      {

        @Override
        public String getText(final Object element)
        {
          if (element instanceof Long)
          {
            final long longValue = ((Long) element).longValue();
            if (longValue == 0)
              return "All";

            if (longValue == Long.MAX_VALUE)
              return "None";
            if (longValue == 5000)
              return "5 Second";
            if (longValue == 15000)
              return "15 Second";
            if (longValue == 60000)
              return "1 Minute";
            if (longValue == 300000)
              return "5 Minute";
            if (longValue == 600000)
              return "10 Minute";
            if (longValue == 3600000)
              return "1 Hour";
          }
          return super.getText(element);
        }
      });
      comboViewer.setInput( new Long[]
          {0l, 5000l, 15000l, 60000l, 300000l, 600000l, 3600000l, Long.MAX_VALUE});

      comboViewer.getCombo().setLayoutData(
          new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
      comboViewer.setSelection(new StructuredSelection(Long
          .valueOf(_resampleFrequency)));
      comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {

        @Override
        public void selectionChanged(final SelectionChangedEvent event)
        {
          final IStructuredSelection selection =
              (IStructuredSelection) event.getSelection();
          if (selection.getFirstElement() instanceof Long)
          {
            _resampleFrequency = (Long) selection.getFirstElement();
          }
        }
      });
      
    }
    
    final Button rememberBtn = new Button(shell, SWT.CHECK);
    rememberBtn.setText("Automatically use this mode next time");
    rememberBtn.addSelectionListener(new SelectionListener(){
		
			public void widgetSelected(final SelectionEvent e)
			{
				_rememberIt = rememberBtn.getSelection();
			}
		
			public void widgetDefaultSelected(final SelectionEvent e)
			{
			}
		});
    
    final Composite holder = new Composite(shell, SWT.NONE);
    holder.setLayout(new GridLayout(2, true));
    

    // Create the OK button and add a handler
    // so that pressing it will set input
    // to the entered value
    final Button ok = new Button(holder, SWT.PUSH);
    ok.setText("OK");
    data = new GridData(GridData.FILL_HORIZONTAL);
    ok.setLayoutData(data);
    ok.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent event) {
        // so, do they want us to remember the value?
        if(_rememberIt)
        {
        	// put it into the prefs.
          CorePlugin.getToolParent().setProperty(ImportReplay.TRACK_IMPORT_MODE, _mode);
          CorePlugin.getToolParent().setProperty(ImportReplay.RESAMPLE_FREQUENCY, Long.toString(_resampleFrequency));
        }
        
        shell.close();
      }
    });

    // Create the cancel button and add a handler
    // so that pressing it will set input to null
    final Button cancel = new Button(holder, SWT.PUSH);
    cancel.setText("Cancel");
    data = new GridData(GridData.FILL_HORIZONTAL);
    cancel.setLayoutData(data);
    cancel.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent event) {
        // clear the selection
      	_mode = null;
        shell.close();
      }
    });

    // Set the OK button as the default, so
    // user can type input and press Enter
    // to dismiss
    shell.setDefaultButton(ok);
  }

	public void widgetDefaultSelected(final SelectionEvent e)
	{

	}

	public void widgetSelected(final SelectionEvent e)
	{
		final Button btn = (Button) e.widget;
		_mode = (String) btn.getData();
	}
}
           
       