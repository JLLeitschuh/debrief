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
package org.bitbucket.es4gwt.shared.spec;

import org.bitbucket.es4gwt.shared.elastic.ElasticFacet;
import org.bitbucket.es4gwt.shared.elastic.filter.FilterBuilder;

/**
 * @author Mikael Couzic
 */
public class FilterConverter {

	public static FilterBuilder toElasticFilterBuilder(SearchRequest spec) {
		FilterBuilder builder = new FilterBuilder(spec.getFacetFilters().asMultimap());

		for (ElasticFacet facet : spec.getFilterModeFacets()) {
			builder.filterMode(facet, spec.getFilterMode(facet));
		}
		return builder;
	}
}
