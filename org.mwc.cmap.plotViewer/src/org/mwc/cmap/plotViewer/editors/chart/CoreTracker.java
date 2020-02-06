
package org.mwc.cmap.plotViewer.editors.chart;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.part.EditorPart;
import org.mwc.cmap.core.ui_support.LineItem;

public class CoreTracker
{

	/**
	 * single instance of cursor tracker.
	 * 
	 */
	protected static CoreTracker _singleton;

	/**
	 * the currently assigned editor
	 * 
	 */
	protected EditorPart _myEditor;

	/**
	 * the line instance we write to
	 * 
	 */
	final LineItem _myLine;

	public String _lastText = null;

	// ///////////////////////////////////////////////////
	// constructor
	// ///////////////////////////////////////////////////

	protected CoreTracker(final String string, final String duffString, final String tooltip,
			final String prefsId)
	{
		// first the status bar contribution
		_myLine = new LineItem(string, duffString, tooltip, prefsId);
	}

	public void close()
	{
		// belt & braces, ditch stuff.
		forgetSettings(this);
	}

	public static void write(final String txt)
	{
		if (_singleton != null)
		{
			_singleton._myLine.setText(txt);
			_singleton._lastText = txt;
		}
	}

	/**
	 * teardown for this chart
	 * 
	 */
	protected static void forgetSettings(final CoreTracker tracker)
	{
		if (tracker._myEditor != null)
		{
			// get the status manager for this editor
			final IStatusLineManager oldMgr = tracker._myEditor.getEditorSite()
					.getActionBars().getStatusLineManager();

			// try to remove our line item
			oldMgr.remove(tracker._myLine);
		}

		tracker._myEditor = null;
	}

	/**
	 * setup for this chart
	 * 
	 * @param editor
	 */
	protected static void storeSettings(final CoreTracker tracker, final EditorPart editor)
	{
		_singleton._myLine.reset();

		tracker._myEditor = editor;

		// get the status manager for this editor
		final IStatusLineManager oldMgr = tracker._myEditor.getEditorSite()
				.getActionBars().getStatusLineManager();

		// try to add our line item
		oldMgr.add(tracker._myLine);

		// and tell everybody about the change
		tracker._myEditor.getEditorSite().getActionBars().updateActionBars();

	}
}
