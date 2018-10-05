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

import Debrief.ReaderWriter.XML.KML.ImportKML;
import MWC.GUI.Layers;

/**
 * @author ian.mayo
 */
public class KMLLoader extends IPlotLoader.BaseLoader
{


	public KMLLoader()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mwc.debrief.core.interfaces.IPlotLoader#loadFile(org.mwc.cmap.plotViewer.editors.CorePlotEditor,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void loadFile(final IAdaptable target, final InputStream inputStream, final String fileName, final CompleteListener listener)
	{
			final Layers theLayers = (Layers) target.getAdapter(Layers.class);
	    final IPlotLoader finalLoader = this;

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
						public void run(final IProgressMonitor pm)
						{
							// right, better suspend the LayerManager extended updates from
							// firing
							theLayers.suspendFiringExtended(true);

							try
							{
								DebriefPlugin.logError(Status.INFO, "about to start loading:"
										+ fileName, null);

								// quick check, is this a KMZ
								if(fileName.toLowerCase().endsWith(".kmz"))
								{
									// ok - get loading going
									ImportKML.doZipImport(theLayers, inputStream, fileName);
									
								}
								else if(fileName.toLowerCase().endsWith(".kml"))
								{
									// ok - get loading going
									ImportKML.doImport(theLayers, inputStream, fileName);									
								}
								
								DebriefPlugin.logError(Status.INFO,
										"completed loading:" + fileName, null);

								// and inform the plot editor
	              listener.complete(finalLoader);

								DebriefPlugin.logError(Status.INFO, "parent plot informed", null);

							}
							catch (final RuntimeException e)
							{
								DebriefPlugin.logError(Status.ERROR, "Problem loading datafile:"
										+ fileName, e);
							}
							finally
							{
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
				DebriefPlugin.logError(Status.ERROR, "Problem loading kml:"
						+ fileName, e);
			}
			catch (final InterruptedException e)
			{
				DebriefPlugin.logError(Status.ERROR, "Problem loading kml:"
						+ fileName, e);
			}
			catch (final IOException e)
			{
				DebriefPlugin.logError(Status.ERROR, "Problem loading kml:"
						+ fileName, e);
			}
			finally
			{
			}
	//	}
		// ok, load the data...
		DebriefPlugin.logError(Status.INFO, "Successfully loaded XML file", null);
	}
}
