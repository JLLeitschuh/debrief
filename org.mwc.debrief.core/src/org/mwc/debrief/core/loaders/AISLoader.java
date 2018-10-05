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
package org.mwc.debrief.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.mwc.debrief.core.DebriefPlugin;
import org.mwc.debrief.core.interfaces.IPlotLoader;

import Debrief.ReaderWriter.ais.ImportAIS;
import MWC.GUI.Layers;

/**
 */
public class AISLoader extends IPlotLoader.BaseLoader
{
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mwc.debrief.core.interfaces.IPlotLoader#loadFile(org.mwc.cmap.plotViewer
	 * .editors.CorePlotEditor, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void loadFile(final IAdaptable target, final InputStream inputStream,
			final String fileName, final CompleteListener listener)
	{
    final IPlotLoader finalLoader = this;
    
		// ok, we'll need somewhere to put the data
		final Layers theLayers = (Layers) target.getAdapter(Layers.class);

		try
		{
			// hmm, is there anything in the file?
			final int numAvailable = inputStream.available();
			if (numAvailable > 0)
			{

				final IWorkbench wb = PlatformUI.getWorkbench();
				final IProgressService ps = wb.getProgressService();
				ps.busyCursorWhile(new IRunnableWithProgress()
				{
					@Override
					public void run(final IProgressMonitor pm)
					{
						// right, better suspend the LayerManager extended updates from
						// firing
						theLayers.suspendFiringExtended(true);

						try
						{
							DebriefPlugin.logError(Status.INFO, "about to start loading:"
									+ fileName, null);

							// ok - get loading going
							ImportAIS importer = new ImportAIS(theLayers);
							importer.importThis(fileName, inputStream);
							
							DebriefPlugin.logError(Status.INFO, "completed loading:"
									+ fileName, null);

						}
						catch (final RuntimeException e)
						{
							DebriefPlugin.logError(Status.ERROR, "Problem loading AIS datafile:"
									+ fileName, e);
						}
						catch (final Exception e)
						{
							DebriefPlugin.logError(Status.ERROR, "Problem loading AIS datafile:"
									+ fileName, e);
						}
						finally
						{
							// and inform the plot editor
							listener.complete(finalLoader);

							DebriefPlugin.logError(Status.INFO, "parent plot informed", null);

							// ok, allow the layers object to inform anybody what's
							// happening
							// again
							theLayers.suspendFiringExtended(false);

							// and trigger an update ourselves
							// theLayers.fireExtended();
						}
					}
				});

			}

		}
		catch (final InvocationTargetException e)
		{
			DebriefPlugin.logError(Status.ERROR, "Problem loading datafile:"
					+ fileName, e);
		}
		catch (final InterruptedException e)
		{
			DebriefPlugin.logError(Status.ERROR, "Problem loading datafile:"
					+ fileName, e);
		}
		catch (final IOException e)
		{
			DebriefPlugin
					.logError(Status.ERROR, "Problem loading AIS:" + fileName, e);
		}

		// ok, load the data...
		DebriefPlugin.logError(Status.INFO, "Successfully loaded AIS file", null);
	}
}
