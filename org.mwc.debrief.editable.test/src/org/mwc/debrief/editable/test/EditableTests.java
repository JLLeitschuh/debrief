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
package org.mwc.debrief.editable.test;

import java.awt.Color;
import java.beans.BeanDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.SearchablePluginsManager;
import org.eclipse.swt.widgets.Display;
import org.mwc.cmap.naturalearth.Activator;
import org.mwc.cmap.naturalearth.wrapper.NELayer;
import org.mwc.debrief.satc_interface.data.SATC_Solution;
import org.osgi.framework.Bundle;

import Debrief.Wrappers.SensorContactWrapper;
import Debrief.Wrappers.SensorWrapper;
import Debrief.Wrappers.Track.SplittableLayer;
import MWC.GUI.Editable;
import MWC.GUI.Editable.EditorType;
import MWC.GUI.ExternallyManagedDataLayer;
import MWC.GUI.Chart.Painters.ETOPOPainter;
import MWC.GUI.ETOPO.ETOPO_2_Minute;
import MWC.GUI.Properties.Swing.SwingPropertyEditor2;
import MWC.GUI.Shapes.ChartFolio;
import MWC.GUI.Shapes.CircleShape;
import MWC.GUI.Shapes.PolygonShape;
import MWC.GUI.VPF.CoverageLayer;
import MWC.GUI.VPF.DebriefFeatureWarehouse;
import MWC.GUI.VPF.FeaturePainter;
import MWC.GenericData.HiResDate;
import MWC.GenericData.WorldLocation;

import com.bbn.openmap.layer.vpf.LibrarySelectionTable;
import com.planetmayo.debrief.satc.model.generator.ISolver;
import com.planetmayo.debrief.satc.model.manager.ISolversManager;
import com.planetmayo.debrief.satc_rcp.SATC_Activator;

public class EditableTests extends TestCase
{

	private static final String ORG_MWC_CMAP_LEGACY = "org.mwc.cmap.legacy";

	public EditableTests(final String testName)
	{
		super(testName);
	}

	/**
	 * Perform pre-test initialization
	 *
	 * @throws Exception
	 *
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();

//		IPreferenceStore store = PDEPlugin.getDefault().getPreferenceStore();
//
//		store.setValue(IPreferenceConstants.ADD_TO_JAVA_SEARCH, true);
//		try
//		{
//
//			ITargetHandle target = TargetPlatformService.getDefault()
//					.getWorkspaceTargetHandle();
//			if (target != null)
//			{
//				AddToJavaSearchJob.synchWithTarget(target.getTargetDefinition());
//			}
//
//			else
//			{
//				AddToJavaSearchJob.clearAll();
//			}
//		}
//		catch (CoreException e)
//		{
//			org.mwc.debrief.editable.test.Activator.log(e);
//		}
//		PDEPlugin.getDefault().getPreferenceManager().savePluginPreferences();
//		waitForJobs();

		Bundle bundle = Platform.getBundle(ORG_MWC_CMAP_LEGACY);
		URL cmapLegacyURL = FileLocator.resolve(bundle.getEntry("/"));
		File cmapLegacy = new File(cmapLegacyURL.getPath());
		File rootFile;
		if (cmapLegacy.isDirectory())
		{
			rootFile = cmapLegacy.getParentFile();
		}
		else
		{
			URL homeURL = FileLocator.resolve(Platform.getInstallLocation().getURL());
			File home = new File(homeURL.getPath());
			// org.mwc.debrief.editable.test/target/work/../../..
			rootFile = home.getParentFile().getParentFile().getParentFile();
		}
		if (!rootFile.exists())
		{
			return;
		}
		openProjects(rootFile);
		waitForJobs();
	}

	private void openProjects(File rootFile) throws CoreException
	{
		File[] files = rootFile.listFiles(new FileFilter()
		{

			@Override
			public boolean accept(File pathname)
			{
				if (!pathname.isDirectory())
				{
					return false;
				}
				if (!pathname.getName().startsWith("org.mwc")
						|| pathname.getName().startsWith("org.mwc.asset"))
				{
					return false;
				}
				if (pathname.getName().endsWith(".test") ||
						pathname.getName().endsWith(".tests") ||
						pathname.getName().endsWith(".test2") ||
						pathname.getName().endsWith(".feature") ||
						pathname.getName().endsWith(".site") ||
						pathname.getName().endsWith(".media") ||
						pathname.getName().endsWith(".GNDManager") ) {
					return false;
				}
				return true;
			}
		});
		if (files == null)
		{
			return;
		}
		for (File file : files)
		{
			File projectFile = new File(file, ".project");
			if (!projectFile.isFile())
			{
				continue;
			}
			IProject project;
			IPath path = new Path(projectFile.getAbsolutePath());
			IProjectDescription description = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(path);
			project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(description.getName());
			project.create(description, null);
			project.open(null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}
	}

	/**
	 * Perform post-test clean up
	 *
	 * @throws Exception
	 *
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();

		// Dispose of the test fixture
		waitForJobs();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (IProject project : projects)
		{
			try
			{
				project.delete(false, true, null);
			}
			catch (Exception e)
			{
				// ignore
			}
		}
	}

	/**
	 * Run the editable properties
	 * 
	 * @throws CoreException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void testEditable() throws CoreException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{
		IProgressMonitor monitor = new NullProgressMonitor();
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(ORG_MWC_CMAP_LEGACY);
		IJavaProject javaProject;
		if (project.exists())
		{
			javaProject = JavaCore.create(project);
			javaProject.open(monitor);
		}
		else
		{
			SearchablePluginsManager manager = PDECore.getDefault().getSearchablePluginsManager();
			javaProject = manager.getProxyProject();
		}
		IType editableType = javaProject.findType("MWC.GUI.Editable");
		ITypeHierarchy hierarchy = editableType.newTypeHierarchy(null);
		hierarchy.refresh(monitor);
		IType[] subTypes = hierarchy.getAllSubtypes(editableType);
		for (IType type : subTypes)
		{
			if (type.isClass() && !Flags.isAbstract(type.getFlags()) && Flags.isPublic(type.getFlags()))
			{
				//System.out.println(type.getFullyQualifiedName());
				if ("MWC.TacticalData.GND.GTrack".equals(type.getFullyQualifiedName()))
				{
					continue;
				}
				Editable editable = getEditable(type);
				if (editable == null)
				{
					continue;
				}
				EditorType info = null;
				try
				{
					info = editable.getInfo();
				}
				catch (Exception e)
				{
					System.out.println("Info issue " + type.getFullyQualifiedName());
					continue;
				}
				if (info == null)
				{
					continue;
				}
				testTheseParameters(editable);
			}
		}
	}

	private Editable getEditable(IType type)
	{
		Editable editable = null;
		if ("Debrief.Wrappers.SensorWrapper".equals(type.getFullyQualifiedName()))
		{
			SensorWrapper sensor = new SensorWrapper("tester");
			final java.util.Calendar cal = new java.util.GregorianCalendar(2001, 10,
					4, 4, 4, 0);

			// and create the list of sensor contact data items
			cal.set(2001, 10, 4, 4, 4, 0);
			final long start_time = cal.getTime().getTime();
			sensor.add(new SensorContactWrapper("tester", new HiResDate(cal.getTime()
					.getTime()), null, null, null, null, null, 1, sensor.getName()));

			cal.set(2001, 10, 4, 4, 4, 23);
			sensor.add(new SensorContactWrapper("tester", new HiResDate(cal.getTime()
					.getTime()), null, null, null, null, null, 1, sensor.getName()));
			return sensor;

		}
		Class<?> clazz;
		try
		{
			clazz = Class.forName(type.getFullyQualifiedName());
		}
		catch (ClassNotFoundException e1)
		{
			//e1.printStackTrace();
			System.out.println(e1.getMessage() + " " + type.getFullyQualifiedName());
			return null;
		}
		try
		{
			@SuppressWarnings("unused")
			Method infoMethod = clazz.getDeclaredMethod("getInfo", new Class[0]);
		}
		catch (Exception e)
		{
			return null;
		}
		try
		{
			editable = (Editable) clazz.newInstance();
		}
		catch (Exception e)
		{
			Constructor<?>[] constructors = clazz.getConstructors();
			for (Constructor<?> constructor:constructors)
			{
				try
				{
					if (constructor.getParameterTypes().length == 1)
					{
						Class<?> paramType = constructor.getParameterTypes()[0];
						if ("java.lang.String".equals(paramType.getName()))
						{
							editable = (Editable) constructor.newInstance("test");
						}
						else
						{
							editable = (Editable) constructor.newInstance(null);
						}
						break;
					}
					if (constructor.getParameterTypes().length == 2)
					{
						editable = (Editable) constructor.newInstance(null, null);
						break;
					}
					if (constructor.getParameterTypes().length == 3)
					{
						editable = (Editable) constructor.newInstance(null, null, null);
						break;
					}
				}
				catch (Exception e1)
				{
					// ignore
					//e1.printStackTrace();
				}
			}
		}
		if (editable == null)
		{
			switch (type.getFullyQualifiedName())
			{
			case "MWC.GUI.Shapes.ChartFolio":
				editable = new ChartFolio(false, Color.white);
				break;
			case "org.mwc.cmap.naturalearth.wrapper.NELayer":
				editable = new NELayer(Activator.getDefault().getDefaultStyleSet());
				break;
			case "MWC.GUI.VPF.CoverageLayer$ReferenceCoverageLayer":
				final LibrarySelectionTable LST = null;
				final DebriefFeatureWarehouse myWarehouse = new DebriefFeatureWarehouse();
				final FeaturePainter fp = new FeaturePainter("libref", "Coastline");
				fp.setVisible(true);
				editable = new CoverageLayer.ReferenceCoverageLayer(LST, myWarehouse,
						"libref", "libref", "Coastline", fp);
				break;
			case "MWC.GUI.Chart.Painters.ETOPOPainter":
				editable = new ETOPOPainter("etopo", null);
				break;
			case "MWC.GUI.ETOPO.ETOPO_2_Minute":
				editable = new ETOPO_2_Minute("etopo");
				break;
			case "MWC.GUI.ExternallyManagedDataLayer":
				editable = new ExternallyManagedDataLayer("test", "test", "test");
				break;
			case "MWC.GUI.Shapes.CircleShape":
				editable = new CircleShape(new WorldLocation(2d, 2d, 2d), 2d);
				break;
			case "Debrief.Wrappers.Track.SplittableLayer":
				editable = new SplittableLayer(true);
				break;
			case "org.mwc.debrief.satc_interface.data.SATC_Solution":
				final ISolversManager solvMgr = SATC_Activator.getDefault().getService(
						ISolversManager.class, true);
				final ISolver newSolution = solvMgr.createSolver("test");
				editable = new SATC_Solution(newSolution);
				break;
			case "MWC.GUI.Shapes.PolygonShape":
				editable = new PolygonShape(null);
				break;
			default:
				System.out.println("Can't instantiate type " + type.getFullyQualifiedName());
				break;
			}

		}
		return editable;
	}

	/**
	 * Process UI input but do not return for the specified time interval.
	 * 
	 * @param waitTimeMillis
	 *          the number of milliseconds
	 */
	protected static void delay(final long waitTimeMillis)
	{
		final Display display = Display.getCurrent();

		// If this is the user interface thread, then process input
		if (display != null)
		{
			final long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
			while (System.currentTimeMillis() < endTimeMillis)
			{
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.update();
		}

		// Otherwise perform a simple sleep
		else
		{
			try
			{
				Thread.sleep(waitTimeMillis);
			}
			catch (final InterruptedException e)
			{
				// ignored
			}
		}
	}

	/**
	 * Wait until all background tasks are complete
	 */
	public void waitForJobs()
	{
		waitForJobs(60 * 60 * 1000);
	}

	public static void waitForJobs(long maxIdle)
	{
		long start = System.currentTimeMillis();
		while (!Job.getJobManager().isIdle())
		{
			delay(1000);
			if ((System.currentTimeMillis() - start) > maxIdle)
			{
				Job[] jobs = Job.getJobManager().find(null);
				StringBuffer buffer = new StringBuffer();
				for (Job job : jobs)
				{
					if (job.getThread() != null)
					{
						buffer.append(job.getName()).append(" (").append(job.getClass())
								.append(")\n");
					}
				}
				if (buffer.length() > 0)
					throw new RuntimeException("Invalid jobs found:" + buffer.toString()); //$NON-NLS-1$
			}
		}
	}
	
	/**
   * test helper, to check that all of the object property getters/setters are
   * there
   * 
   * @param toBeTested
   */
  public static void testTheseParameters(final Editable toBeTested)
  {
    // check if we received an object
    if (toBeTested == null)
      return;

    Assert.assertNotNull("Found editable object", toBeTested);

    final Editable.EditorType et = toBeTested.getInfo();

    if (et == null)
    {
      Assert.fail("no editor type returned for");
      return;
    }

    // first see if we return a custom bean descriptor
    final BeanDescriptor desc = et.getBeanDescriptor();

    // did we get one?
    if (desc != null)
    {
      final Class<?> editorClass = desc.getCustomizerClass();
      if (editorClass != null)
      {
        Object newInstance = null;
        try
        {
          newInstance = editorClass.newInstance();
        }
        catch (final InstantiationException e)
        {
          e.printStackTrace(); // To change body of catch statement use File
                                // | Settings | File Templates.
        }
        catch (final IllegalAccessException e)
        {
          e.printStackTrace(); // To change body of catch statement use File
                                // | Settings | File Templates.
        }
        // check it worked
        Assert.assertNotNull("we didn't create the custom editor for:",
            newInstance);
      }

      else
      {

        // there isn't a dedicated editor, try the custom ones.

        // do the edits
        final PropertyDescriptor[] pd = et.getPropertyDescriptors();

        if (pd == null)
        {
          Assert.fail("problem fetching property editors for " + toBeTested);
          return;
        }

        final int len = pd.length;
        if (len == 0)
        {
          System.out.println("zero property editors found for " + toBeTested
              + ", " + toBeTested.getClass());
          return;
        }

        // get the data
        final Object data = et.getData();

        for (int i = 0; i < len; i++)
        {
          // get the methods
          final PropertyDescriptor p = pd[i];

          // find out the type of the editor
          final Method getter = p.getReadMethod();
          final Method setter = p.getWriteMethod();

          // is there a custom editor for this type?

          Object res = null;

          try
          {
            // sort out the getter
            final Object[] dummyArgument = null;
            res = getter.invoke(data, dummyArgument);
          }
          catch (final InvocationTargetException ie)
          {
            Assert.fail("missing getter for " + toBeTested + " called:" + getter.getName() + " property:"
                + p.getDisplayName() + " (" + et.getClass() + ") because:" + ie.getCause());
          }
          catch (final IllegalAccessException al)
          {
            Assert.fail("getter not visible for " + toBeTested);
          }

          try
          {
            final Object[] params = { res };
            // sort out the setter
            setter.invoke(data, params);
          }
          catch (final InvocationTargetException ie)
          {
            // just check that we were using a valid value for the res
            if (res != null)
            {
              Assert.fail("missing setter for "
                  + p.getWriteMethod().getName() + ", "
                  + toBeTested.getClass());
            }
            else
              System.out
                  .println("######## null value returned form getter for "
                      + p.getReadMethod().getName());
          }
          catch (final IllegalAccessException al)
          {
            Assert.fail("setter not visible for " + toBeTested);
          }

          // check if we can get a property editor GUI component for this
          SwingPropertyEditor2.checkPropertyEditors();
          final PropertyEditor editor = SwingPropertyEditor2.findEditor(p);
          if (editor == null)
          {
          	System.out.println(toBeTested.getClass() + ":" + " getter:" + p.getReadMethod().getName());
          } 
          else
          {
          	Assert.assertNotNull("could not find GUI editor component for:" + toBeTested.getName() + "" + data
          			+ " getter:" + p.getReadMethod().getName(), editor);
          }
        }
      } // whether there was a customizer class
    } // whether there was a custom bean descriptor

    // now try out the methods
    final MethodDescriptor[] methods = et.getMethodDescriptors();
    if (methods != null)
    {
      for (int thisM = 0; thisM < methods.length; thisM++)
      {
        final MethodDescriptor method = methods[thisM];
        final Method thisOne = method.getMethod();
        final String theName = thisOne.getName();
        Assert.assertNotNull(theName);
      }
    }
  }
}
