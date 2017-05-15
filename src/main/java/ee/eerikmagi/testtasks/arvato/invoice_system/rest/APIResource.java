package ee.eerikmagi.testtasks.arvato.invoice_system.rest;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.ParkingHouse;
import ee.eerikmagi.testtasks.arvato.invoice_system.persistence.DumbDB;
import ee.eerikmagi.testtasks.arvato.invoice_system.rest.json.AddParkingRequest;
import ee.eerikmagi.testtasks.arvato.invoice_system.rest.json.DateObj;
import ee.eerikmagi.testtasks.arvato.invoice_system.rest.json.JsonResponse;

@Path("api")
public class APIResource {
	@Path("addParking")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addParking(@Valid AddParkingRequest req) {
		Customer c = DumbDB.getCustomer(req.getCustomerID());
		ParkingHouse ph = DumbDB.getParkingHouse(req.getParkingHouseID());
		
		if (c == null) {
			throw new RuntimeException("Invalid customer ID");
		}
		if (ph == null) {
			throw new RuntimeException("Invalid parking house ID");
		}
		
		DateObj start = req.getStart();
		DateObj end = req.getEnd();
		
		Parking p = new Parking()
			.setParkingHouse(ph)
			.setStartDateTime(LocalDateTime.of(
					start.getYear().intValue(), start.getMonth().intValue(), start.getDay().intValue(),
					start.getHour().intValue(), start.getMinute().intValue(), start.getSecond().intValue()))
			.setEndDateTime(LocalDateTime.of(
					end.getYear().intValue(), end.getMonth().intValue(), end.getDay().intValue(),
					end.getHour().intValue(), end.getMinute().intValue(), end.getSecond().intValue()));
		
		DumbDB.addCustomerParking(req.getCustomerID(), p);
		
		return Response.ok(new JsonResponse()
			.setSuccess(true)).build();
	}
}
