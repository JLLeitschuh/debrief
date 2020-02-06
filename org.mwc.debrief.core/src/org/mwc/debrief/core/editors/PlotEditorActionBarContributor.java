
package org.mwc.debrief.core.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;
/**
 * Plot editor action bar contributor.
 */
public class PlotEditorActionBarContributor extends EditorActionBarContributor {
	private static final String EDITOR_VIEW_MENU_ID = "/org.mwc.debrief.core.EditorView";

	// current editor
	protected PlotEditor _myEditor;

	public PlotEditorActionBarContributor() {
		super();
	}

	/**
	 * Sets the active editor for the contributor.
	 * <p>
	 * The <code>EditorActionBarContributor</code> implementation of this method does
	 * nothing. Subclasses may reimplement. This generally entails disconnecting
	 * from the old editor, connecting to the new editor, and updating the actions
	 * to reflect the new editor.
	 * </p>
	 * 
	 * @param targetEditor the new target editor
	 */
	public void setActiveEditor(IEditorPart targetEditor) {
		if (targetEditor instanceof PlotEditor) {
			_myEditor = (PlotEditor) targetEditor;
		} else {
			_myEditor = null;
		}
		IActionBars bars = getActionBars();
		if (bars == null)
		{
			return;
		}
		bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), _myEditor.getUndoAction());
		bars.setGlobalActionHandler(ActionFactory.REDO.getId(), _myEditor.getRedoAction());
		bars.updateActionBars();	
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}

	@Override
	public void init(IActionBars bars)
	{
		super.init(bars);
	}

	@SuppressWarnings("unused")
	@Override
	public void contributeToMenu(IMenuManager menuManager)
	{
		super.contributeToMenu(menuManager);
		IMenuManager editorViewMenu = menuManager.findMenuUsingPath(EDITOR_VIEW_MENU_ID);
		// TODO: verify what is happening with the above item. Do we need it?
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager)
	{
	}
}