/*
 * Copyright 2010-2012 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.schildbach.pte.live;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.SbbProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.NearbyStationsResult;
import de.schildbach.pte.dto.QueryConnectionsResult;
import de.schildbach.pte.dto.QueryDeparturesResult;

/**
 * @author Andreas Schildbach
 */
public class SbbProviderLiveTest extends AbstractProviderLiveTest
{
	public SbbProviderLiveTest()
	{
        // TODO fix handling of secrets
		super(null); // new SbbProvider(Secrets.SBB_ACCESS_ID));
	}

	@Test
	public void nearbyStations() throws Exception
	{
		final NearbyStationsResult result = provider.queryNearbyStations(new Location(LocationType.STATION, 8500010), 0, 0);

		System.out.println(result.status + "  " + result.stations.size() + "  " + result.stations);
	}

	@Test
	public void nearbyStationsByCoordinate() throws Exception
	{
		final NearbyStationsResult result = provider.queryNearbyStations(new Location(LocationType.ADDRESS, 52525589, 13369548), 0, 0);

		print(result);
	}

	@Test
	public void queryDepartures() throws Exception
	{
		final QueryDeparturesResult result = provider.queryDepartures(8500010, 0, false);

		print(result);
	}

	@Test
	public void autoComplete() throws Exception
	{
		final List<Location> autocompletes = provider.autocompleteStations("haupt");

		print(autocompletes);
	}

	@Test
	public void autoCompleteAddress() throws Exception
	{
		final List<Location> autocompletes = provider.autocompleteStations("Dorfstrasse 10, Dällikon, Schweiz");

		print(autocompletes);
	}

	@Test
	public void shortConnection() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.STATION, 8503000, null, "Zürich HB"), null, new Location(
				LocationType.STATION, 8507785, null, "Bern, Hauptbahnhof"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL, Accessibility.NEUTRAL);
		System.out.println(result);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}

	@Test
	public void slowConnection() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.ANY, 0, null, "Schocherswil, Alte Post!"), null,
				new Location(LocationType.ANY, 0, null, "Laconnex, Mollach"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL, Accessibility.NEUTRAL);
		System.out.println(result);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}

	@Test
	public void connectionWithFootway() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.ADDRESS, 0, null, "Spiez, Seestraße 62"), null,
				new Location(LocationType.ADDRESS, 0, null, "Einsiedeln, Erlenmoosweg 24"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL,
				Accessibility.NEUTRAL);
		System.out.println(result);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}

	@Test
	public void connectionFromAddress() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.ADDRESS, 0, null, "Dorfstrasse 10, Dällikon, Schweiz"),
				null, new Location(LocationType.STATION, 8500010, null, "Basel"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL,
				Accessibility.NEUTRAL);
		System.out.println(result);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}
}
