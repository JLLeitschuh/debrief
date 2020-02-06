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
package org.mwc.debrief.core.ui;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.mwc.cmap.core.CorePlugin;
import org.mwc.debrief.core.preferences.PrefsPage.PreferenceConstants;

import Debrief.ReaderWriter.Word.ImportNarrativeDocument.ImportNarrativeEnum;
import Debrief.ReaderWriter.Word.ImportNarrativeDocument.TrimNarrativeHelper;

/**
 * Helper class to pop up dialog to offer choice to analyst
 * to import all data or loaded tracks 
 * @author Ayesha <ayesha.ma@gmail.com>
 *
 */
public class ImportNarrativeHelper implements TrimNarrativeHelper
{

  public static final String PREF_DEF_NARRATIVE_CHOICE = "defaultNarrativeEntryChoice";
  @Override
  public ImportNarrativeEnum findWhatToImport()
  {
    final Display targetDisplay;
    boolean reuseChoice = CorePlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.REUSE_TRIM_NARRATIVES_DIALOG_CHOICE);
    String defaultChoice = CorePlugin.getDefault().getPreference(PREF_DEF_NARRATIVE_CHOICE);
    if(reuseChoice && defaultChoice!=null && !defaultChoice.isEmpty()) {
      return ImportNarrativeEnum.getByName(defaultChoice);
    }
    else {
      final StringBuilder retVal = new StringBuilder();
      if(Display.getCurrent() == null)
      {
        targetDisplay = Display.getDefault();
      }
      else
      {
        targetDisplay = Display.getCurrent();
      }
      
      // ok, get the answer
      targetDisplay.syncExec(new Runnable(){
        @Override
        public void run()
        {
          ImportNarrativeDialog dialog =
              new ImportNarrativeDialog(targetDisplay.getActiveShell());
          if(dialog.open()==Window.OK) {
            ImportNarrativeEnum userChoice = dialog.getUserChoice();
            retVal.append(userChoice.getName());
            if(dialog.getPreference()) {
              CorePlugin.getDefault().getPreferenceStore().setValue(PREF_DEF_NARRATIVE_CHOICE,userChoice.getName());
            }  
          }else {
            ImportNarrativeEnum userChoice = dialog.getUserChoice();
            retVal.append(userChoice.getName());
          }
          
          
        }});
      return ImportNarrativeEnum.getByName(retVal.toString());
    }
  }
  

}
