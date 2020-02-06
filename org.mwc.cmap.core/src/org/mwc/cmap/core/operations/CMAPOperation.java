
package org.mwc.cmap.core.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.*;
import org.eclipse.core.runtime.*;
import org.mwc.cmap.core.CorePlugin;

/** convenience method for setting up a Debrief operation
 * 
 * @author ian.mayo
 *
 */
abstract public class CMAPOperation extends AbstractOperation
{

	/** constructor - that also sorts out the context
	 * 
	 * @param title
	 */
	public CMAPOperation(final String title)
	{
		super(title);
		
		if (CorePlugin.getUndoContext() != null) {
			super.addContext(CorePlugin.getUndoContext());
		}	
	}

	/** instead of having to implement REDO, just call execute
	 * @param monitor
	 * @param info
	 * @return
	 * @throws ExecutionException
	 */
	public IStatus redo(final IProgressMonitor monitor, final IAdaptable info) throws ExecutionException
	{
		return execute(monitor, info);
	}

	
	
}
