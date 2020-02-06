
package org.mwc.cmap.NarrativeViewer.actions;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;
import org.mwc.cmap.NarrativeViewer.NarrativeViewer;
import org.mwc.cmap.NarrativeViewer.NarrativeViewerModel;


public class NarrativeViewerActions extends ActionGroup {
	private final AbstractDynamicAction myShowSourceColumn;
	private final AbstractDynamicAction myShowTypeColumn;
	
	private final AbstractDynamicAction myFilterSource;
	private final AbstractDynamicAction myFilterType;
	
	public NarrativeViewerActions(final NarrativeViewer viewer){
		final NarrativeViewerModel model = viewer.getModel();
		myShowSourceColumn = new SwitchColumnVisibilityAction(model.getColumnSource(), "Show source");
		myShowTypeColumn = new SwitchColumnVisibilityAction(model.getColumnType(), "Show type");
		
		myFilterSource = new OpenFilterAction(viewer, model.getColumnSource(), "Set source filter...");
		myFilterType = new OpenFilterAction(viewer, model.getColumnType(), "Set type filter...");
	}
	
	@Override
	public void updateActionBars() {
		super.updateActionBars();
		updateActions();
	}
	
	@Override
	public void fillActionBars(final IActionBars actionBars) {
		super.fillActionBars(actionBars);
		final IMenuManager menu = actionBars.getMenuManager();
		menu.add(myShowSourceColumn);
		menu.add(myShowTypeColumn);
		menu.add(new Separator());
		menu.add(myFilterSource);
		menu.add(myFilterType);
		
		menu.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(final IMenuManager manager) {
				updateActions();
			}
		});
	}
	
	void updateActions(){
		myFilterSource.refresh();
		myFilterType.refresh();
		myShowSourceColumn.refresh();
		myShowTypeColumn.refresh();
	}
	
}
