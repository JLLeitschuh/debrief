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

package org.mwc.asset.comms.restlet.host;

import java.util.List;

import org.mwc.asset.comms.restlet.data.Participant;
import org.mwc.asset.comms.restlet.data.ParticipantsResource;
import org.mwc.asset.comms.restlet.host.ASSETHost.HostProvider;
import org.restlet.resource.Get;

public class ParticipantsHandler extends ASSETResource implements ParticipantsResource {

	@Override
	@Get
	public List<Participant> retrieve() {
		final ASSETHost.HostProvider hostP = (HostProvider) getApplication();
		final ASSETHost host = hostP.getHost();

		final List<Participant> list = host.getParticipantsFor(getScenarioId());
		return list;
	}

}