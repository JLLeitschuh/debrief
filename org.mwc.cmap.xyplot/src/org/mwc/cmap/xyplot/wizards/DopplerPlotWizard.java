package org.mwc.cmap.xyplot.wizards;

import java.text.DecimalFormat;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.mwc.cmap.core.ui_support.wizards.SimplePageListWizard;
import org.mwc.cmap.core.wizards.EnterStringPage;

import Debrief.Tools.FilterOperations.ShowTimeVariablePlot3.CalculationWizard;
import Debrief.Tools.Tote.toteCalculation;
import Debrief.Tools.Tote.Calculations.dopplerCalc;
import MWC.Algorithms.Conversions;
import MWC.GUI.Editable;
import MWC.GenericData.WatchableList;

public class DopplerPlotWizard implements CalculationWizard
{

	public int open(toteCalculation pCalc,WatchableList primary, Editable[] subjects)
	{
		dopplerCalc calc = (dopplerCalc) pCalc;
		
		// create the wizard to color/name this
		final SimplePageListWizard wizard = new SimplePageListWizard();

		final String imagePath = "images/UnderwaterPropagation.png";
		final String taskTitle = "Generate Doppler Plot";
		
		DecimalFormat fFormat= new DecimalFormat("0.0000");
		DecimalFormat cFormat= new DecimalFormat("0.0");
		

		final EnterStringPage getf0 = new EnterStringPage(null, " " + fFormat.format(calc.getFNought()) + " ",
				taskTitle, "Please enter a f-Nought (Hz) for [" + primary + "]",
				"(e.g. 150)",
				imagePath, null, false, null);
		final EnterStringPage getSoundSpeed = new EnterStringPage(null, " " +  cFormat.format(Conversions.Kts2Mps(calc.getSpeedOfSound())) + " ",
				taskTitle, "Please enter the Speed of Sound in Water (m/sec)",
				"(e.g. 2000)",
				imagePath, null, false, null);
		wizard.addWizard(getf0);
		wizard.addWizard(getSoundSpeed);
		final WizardDialog dialog = new WizardDialog(Display.getCurrent()
				.getActiveShell(), wizard);
		dialog.create();
		dialog.setBlockOnOpen(true);
		dialog.open();
		// did it work?
		if (dialog.getReturnCode() == WizardDialog.OK)
		{
			calc.setFNought(Double.parseDouble(getf0.getString()));
			calc.setSpeedOfSound(Conversions.Mps2Kts(Double.parseDouble(getSoundSpeed.getString())));
		}
		
		// ok, return the dialog ok/cancel value
		return dialog.getReturnCode();
	}
}
