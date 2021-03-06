
package ASSET.Util.XML.Decisions;

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

import ASSET.Models.Decision.Movement.TransitWaypoint;
import ASSET.Models.Movement.WaypointVisitor;
import ASSET.Util.XML.Decisions.Tactical.CoreDecisionHandler;
import ASSET.Util.XML.Utils.ASSETWorldPathHandler;
import MWC.GenericData.WorldSpeed;
import MWC.Utilities.ReaderWriter.XML.Util.WorldSpeedHandler;

abstract public class TransitWaypointHandler extends CoreDecisionHandler {

	private final static String type = "TransitWaypoint";
	private final static String LOOPING = "Looping";
	private final static String REVERSE = "Reverse";
	private final static String VISITOR = "Visitor";
	private final static String SPEED = "Speed";

	static public void exportThis(final Object toExport, final org.w3c.dom.Element parent,
			final org.w3c.dom.Document doc) {
		// create ourselves
		final org.w3c.dom.Element thisPart = doc.createElement(type);

		// get data item
		final TransitWaypoint bb = (TransitWaypoint) toExport;

		// output the parent bits first
		CoreDecisionHandler.exportThis(bb, thisPart, doc);

		// output it's attributes

		final WorldSpeed theSpeed = bb.getSpeed();
		if (theSpeed != null) {
			WorldSpeedHandler.exportSpeed(SPEED, bb.getSpeed(), thisPart, doc);
		}
		thisPart.setAttribute(VISITOR, bb.getVisitor().getType());
		thisPart.setAttribute(LOOPING, writeThis(bb.getLoop()));
		thisPart.setAttribute(REVERSE, writeThis(bb.getReverse()));

		ASSETWorldPathHandler.exportThis(bb.getDestinations(), thisPart, doc);

		parent.appendChild(thisPart);

	}

	boolean _looping;
	boolean _reverse;
	MWC.GenericData.WorldPath _myPath;
	WorldSpeed _speed;

	String _visitor = null;

	public TransitWaypointHandler() {
		this(type);
	}

	private TransitWaypointHandler(final String type) {
		super(type);

		final MWC.Utilities.ReaderWriter.XML.MWCXMLReader hand = new ASSETWorldPathHandler() {
			@Override
			public void setPath(final MWC.GenericData.WorldPath path) {
				_myPath = path;
			}
		};

		addHandler(hand);
		addHandler(new WorldSpeedHandler(SPEED) {
			@Override
			public void setSpeed(final WorldSpeed res) {
				_speed = res;
			}
		});
		addAttributeHandler(new HandleAttribute(VISITOR) {
			@Override
			public void setValue(final String name, final String val) {
				_visitor = val;
			}
		});

		addAttributeHandler(new HandleBooleanAttribute(LOOPING) {
			@Override
			public void setValue(final String name, final boolean val) {
				_looping = val;
			}
		});
		addAttributeHandler(new HandleBooleanAttribute(REVERSE) {
			@Override
			public void setValue(final String name, final boolean val) {
				_reverse = val;
			}
		});

	}

	@Override
	public final void elementClosed() {
		final WaypointVisitor wv = WaypointVisitor.createVisitor(_visitor);

		final TransitWaypoint route = new TransitWaypoint(_myPath, _speed, _looping, wv);

		super.setAttributes(route);

		route.setReverse(_reverse);
		setModel(route);

		_myPath = null;
		_visitor = null;
		_speed = null;
		_reverse = false;
		_looping = false;
	}

	abstract public void setModel(ASSET.Models.DecisionType dec);
}