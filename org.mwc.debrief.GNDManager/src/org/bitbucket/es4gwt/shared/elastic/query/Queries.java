/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.bitbucket.es4gwt.shared.elastic.query;

import static org.bitbucket.es4gwt.shared.elastic.filter.Filters.dateRange;

import org.bitbucket.es4gwt.shared.elastic.filter.ElasticFilter;

/**
 * @author Mikael Couzic
 */
public class Queries {

	private Queries() {
	}

	public static ElasticQuery matchAll() {
		return new MatchAll();
	}

	public static ElasticQuery field(String fieldName, String fieldValue) {
		return new Field(fieldName, fieldValue);
	}

	public static ElasticQuery filtered(ElasticQuery query, ElasticFilter filter) {
		return new Filtered(query, filter);
	}

	public static ElasticQuery dateRangeFiltered(String early, String late) {
		return filtered(matchAll(), dateRange(early, late));
	}

}
