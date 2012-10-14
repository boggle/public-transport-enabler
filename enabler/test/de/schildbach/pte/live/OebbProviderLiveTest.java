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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.OebbProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.NearbyStationsResult;
import de.schildbach.pte.dto.QueryConnectionsResult;
import de.schildbach.pte.dto.QueryDeparturesResult;

/**
 * @author Andreas Schildbach
 */
public class OebbProviderLiveTest extends AbstractProviderLiveTest
{
	public OebbProviderLiveTest()
	{
		super(new OebbProvider());
	}

	@Test
	public void nearbyStations() throws Exception
	{
		final NearbyStationsResult result = provider.queryNearbyStations(new Location(LocationType.STATION, 902006), 0, 0);

		print(result);
	}

	@Test
	public void nearbyStationsByCoordinate() throws Exception
	{
		final NearbyStationsResult result = provider.queryNearbyStations(new Location(LocationType.ADDRESS, 48200239, 16370773), 0, 0);

		print(result);
		assertEquals(NearbyStationsResult.Status.OK, result.status);
		assertTrue(result.stations.size() > 0);
	}

	@Test
	public void queryDepartures() throws Exception
	{
		final QueryDeparturesResult result = provider.queryDepartures(902006, 0, false);

		print(result);
		assertEquals(QueryDeparturesResult.Status.OK, result.status);
		assertTrue(result.stationDepartures.size() > 0);
	}

	@Test
	public void autocomplete() throws Exception
	{
		final List<Location> autocompletes = provider.autocompleteStations("Wien");

		print(autocompletes);
		assertTrue(autocompletes.size() > 0);
	}

	@Test
	public void autocompleteUmlaut() throws Exception
	{
		final List<Location> autocompletes = provider.autocompleteStations("Obirhöhle");

		print(autocompletes);
	}

	@Test
	public void shortConnection() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.STATION, 1140101, null, "Linz"), null, new Location(
				LocationType.STATION, 1190100, null, "Wien"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL, Accessibility.NEUTRAL);
		System.out.println(result);
		assertEquals(QueryConnectionsResult.Status.OK, result.status);
		assertTrue(result.connections.size() > 0);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}

	@Test
	public void slowConnection() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.ANY, 0, null, "Ramsen Zoll!"), null, new Location(
				LocationType.ANY, 0, null, "Azuga!"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL, Accessibility.NEUTRAL);
		System.out.println(result);
		assertEquals(QueryConnectionsResult.Status.OK, result.status);
		assertTrue(result.connections.size() > 0);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}

	@Test
	public void connectionWithFootway() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.ANY, 0, null, "Graz, Haselweg!"), null, new Location(
				LocationType.ANY, 0, null, "Innsbruck, Gumppstraße 69!"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL, Accessibility.NEUTRAL);
		System.out.println(result);
		assertEquals(QueryConnectionsResult.Status.OK, result.status);
		assertTrue(result.connections.size() > 0);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}

	@Test
	public void connectionWithFootway2() throws Exception
	{
		final QueryConnectionsResult result = queryConnections(new Location(LocationType.ANY, 0, null, "Wien, Krottenbachstraße 110!"), null,
				new Location(LocationType.ADDRESS, 0, null, "Wien, Meidlinger Hauptstraße 1!"), new Date(), true, ALL_PRODUCTS, WalkSpeed.NORMAL,
				Accessibility.NEUTRAL);
		System.out.println(result);
		assertEquals(QueryConnectionsResult.Status.OK, result.status);
		assertTrue(result.connections.size() > 0);
		final QueryConnectionsResult laterResult = queryMoreConnections(result.context, true);
		System.out.println(laterResult);
	}
}
