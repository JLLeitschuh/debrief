
package ASSET.GUI.Editors;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;

import ASSET.NetworkParticipant;
import ASSET.ScenarioType;
import ASSET.GUI.Workbench.Plotters.ScenarioParticipantWrapper;
import ASSET.Participants.Status;
import MWC.GUI.Properties.Swing.SwingPropertiesPanel;
import MWC.GUI.Tools.Swing.MyMetalToolBarUI;

/*******************************************************************************
 * Debrief - the Open Source Maritime Analysis Application http://debrief.info
 *
 * (C) 2000-2020, Deep Blue C Technology Ltd
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html)
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 *******************************************************************************/

public class VesselPane extends MWC.GUI.Properties.Swing.SwingCustomEditor
		implements MWC.GUI.Properties.NoEditorButtons, ASSET.Participants.ParticipantMovedListener,
		java.awt.event.ActionListener, MyMetalToolBarUI.ToolbarOwner {

	//////////////////////////////////////////////////////////////////////
	// custom editor which shows current list of detections
	//////////////////////////////////////////////////////////////////////
	public static class DetectionViewerHolder extends MWC.GUI.Editable.EditorType {

		/**
		 * constructor for editable details of a set of Layers
		 *
		 * @param data the Layers themselves
		 */
		public DetectionViewerHolder(final NetworkParticipant data) {
			super(data, data.getName(), "View ");
		}

		/**
		 * return a description of this bean, also specifies the custom editor we use
		 *
		 * @return the BeanDescriptor
		 */
		@Override
		public java.beans.BeanDescriptor getBeanDescriptor() {
			// final java.beans.BeanDescriptor bp = new
			// java.beans.BeanDescriptor(ASSET.ParticipantType.class,
			// ASSET.GUI.Editors.GraphicDetectionViewer.class);
			// bp.setDisplayName(super.getData().toString());
			return null;
		}

	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static final public String GUI = "GUI";
	static final public String STATUS = "STATUS";
	static final public String DEM_STATUS = "DEM_STATUS";
	static final public String DETECTIONS = "DETECTIONS";
	static final public String MOVEMENT = "MOVEMENT";
	static final public String SENSORS = "SENSORS";
	static final public String DECISION = "DECISION";

	// private ScenarioLayer.ParticipantListener _myWrapper = null;

	static final public String RADIATED_NOISE = "RADIATED_NOISE";

	private ASSET.ParticipantType _myParticipant = null;

	private final ASSET.GUI.Editors.VesselPaneGUI _myGUI = new ASSET.GUI.Editors.VesselPaneGUI();

	//////////////////////////////////////////////////////////////////////
	// GUI components
	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	// member functions
	//////////////////////////////////////////////////////////////////////

	/**
	 * we hold our own tabbed panel
	 *
	 */
	private SwingPropertiesPanel _myPanel = null;

	@Override
	public void actionPerformed(final java.awt.event.ActionEvent ae) {
		final String type = ae.getActionCommand();
		if (type == VesselPane.STATUS) {
			// wrap the participant
			final ASSET.GUI.Editors.VesselStatusEditor vg = new ASSET.GUI.Editors.VesselStatusEditor(_myParticipant);
			_myPanel.addEditor(vg.getInfo(), null);
		}
		if (type == VesselPane.DECISION) {
			// wrap the participant
			final ASSET.Models.DecisionType decider = _myParticipant.getDecisionModel();
			if (decider.hasEditor())
				_myPanel.addEditor(decider.getInfo(), null);
		}
		if (type == VesselPane.DETECTIONS) {
			// wrap the participant
			_myPanel.addEditor(new DetectionViewerHolder(_myParticipant), null);
		}
		if (type == VesselPane.MOVEMENT) {
			// wrap the participant
			_myPanel.addEditor(_myParticipant.getMovementChars().getInfo(), null);
		}
		if (type == VesselPane.RADIATED_NOISE) {
			// wrap the participant
			_myPanel.addEditor(_myParticipant.getRadiatedChars().getInfo(), null);
		}
		if (type == VesselPane.SENSORS) {
			// wrap the participant
			_myPanel.addEditor(_myParticipant.getSensorFit().getInfo(), null);
		}
	}

	/**
	 * the form has closed, stop listening
	 *
	 */
	@Override
	public void doClose() {
		super.doClose();

		// stop listening
		if (_myParticipant != null) {
			_myParticipant.removeParticipantMovedListener(this);
		}

	}

	/**
	 * provide a getName() method which will be called to provide an id name for any
	 * properties panel which get dragged out
	 *
	 * @return the name of this vessel
	 */
	@Override
	public String getName() {
		return _myGUI.getName();
	}

	private void initForm() {
		this.setLayout(new BorderLayout());

		// get the panel, as a Swing component
		final SwingPropertiesPanel pp = (SwingPropertiesPanel) this.getPanel();

		// produce the panel
		_myPanel = new SwingPropertiesPanel(_theLayers, pp.getBuffer(), pp.getToolParent(), this);

		_myPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.darkGray));

		// ok, add the vessel status viewer
		_myPanel.add(_myGUI);

		// and disply the new properties panel
		this.add(_myPanel, BorderLayout.CENTER);
	}

	/**
	 * this participant has moved
	 *
	 */
	@Override
	public void moved(final Status newStatus) {
		updateForm();
	}

	/**
	 * support the restart event
	 *
	 */
	@Override
	public void restart(final ScenarioType scenario) {
		updateForm();
	}

	@Override
	public void setObject(final Object value) {
		setValue(value);
	}

	private void setValue(final Object value) {
		// initialise our participant
		_myParticipant = null;

		//
		if (value instanceof ScenarioParticipantWrapper) {

			// remember this participant
			final ScenarioParticipantWrapper wrapper = (ScenarioParticipantWrapper) value;

			// listen to movements of the participant
			_myParticipant = wrapper.getParticipant();

		} else if (value instanceof ASSET.GUI.SuperSearch.Plotters.SSGuiSupport.ParticipantListener) {
			final ASSET.GUI.SuperSearch.Plotters.SSGuiSupport.ParticipantListener list = (ASSET.GUI.SuperSearch.Plotters.SSGuiSupport.ParticipantListener) value;

			_myParticipant = list.getParticipant();

		}

		if (_myParticipant != null) {

			// build the form
			initForm();

			_myParticipant.addParticipantMovedListener(this);

			// perform the update
			updateForm();

			// does this vessel use fuel?
			if (_myParticipant.getMovementChars().getFuelUsageRate() > 0) {
				_myGUI.showFuel(true);
			} else
				_myGUI.showFuel(false);

			// listen to the pane's events
			_myGUI.addActionListener(this);

			// and tell the GUI it's name
			_myGUI.setVesselName(_myParticipant.getName());
		}

	}

	public boolean supportsCustomEditor() {
		return true;
	}

	private void updateForm() {
		_myGUI.setStatus(_myParticipant.getStatus());
		_myGUI.setDemandedStatus(_myParticipant.getDemandedStatus());
		_myGUI.setActivity(_myParticipant.getActivity());
	}

}