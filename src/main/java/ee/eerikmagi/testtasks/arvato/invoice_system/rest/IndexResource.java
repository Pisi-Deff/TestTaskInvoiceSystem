package ee.eerikmagi.testtasks.arvato.invoice_system.rest;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

import ee.eerikmagi.testtasks.arvato.invoice_system.logic.PaymentLogicFactory;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;
import ee.eerikmagi.testtasks.arvato.invoice_system.persistence.DumbDB;

@Path("")
public class IndexResource {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable index() {
		Map<String, Object> context = new HashMap<>();
		
		context.put("customers", DumbDB.getCustomers());
		
		return new Viewable("/index.ftl", context);
	}
	
	@GET
	@Path("{customerID}")
	@Produces(MediaType.TEXT_HTML)
	public Viewable index(@PathParam("customerID") long customerID) {
		Map<String, Object> context = new HashMap<>();
		
		context.put("customers", DumbDB.getCustomers());
		context.put("selectedCustomerID", customerID);
		context.put("customer", DumbDB.getCustomer(customerID));
		context.put("totalParkings", DumbDB.getCustomerParkings(customerID).size());
		context.put("recentParkings", DumbDB.getCustomerRecentParkings(customerID));
		context.put("currentYear", LocalDateTime.now().getYear());
		context.put("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		return new Viewable("/index.ftl", context);
	}
	
	@GET
	@Path("{customerID}/invoice/{year}/{month}")
	@Produces(MediaType.TEXT_HTML)
	public Viewable invoice(
			@PathParam("customerID") long customerID,
			@PathParam("year") long year,
			@PathParam("month") int month
		) {
		Customer c = DumbDB.getCustomer(customerID);
		List<Parking> parkings = DumbDB.getCustomerParkingsForMonth(customerID, year, Month.of(month));
		Invoice i = PaymentLogicFactory.get(c.getType()).calculateInvoice(c, parkings);
		
		Map<String, Object> context = new HashMap<>();
		
		context.put("customer", c);
		context.put("invoice", i);
		context.put("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		context.put("month", Month.of(month).getDisplayName(TextStyle.FULL, Locale.US));
		context.put("year", year);
		
		return new Viewable("/invoice.ftl", context);
	}
	
	@GET
	@Path("{customerID}/parkings/{year}/{month}")
	@Produces(MediaType.TEXT_HTML)
	public Viewable parkings(
			@PathParam("customerID") long customerID,
			@PathParam("year") long year,
			@PathParam("month") int month
		) {
		Customer c = DumbDB.getCustomer(customerID);
		List<Parking> parkings = DumbDB.getCustomerParkingsForMonth(customerID, year, Month.of(month));
		
		Map<String, Object> context = new HashMap<>();
		
		context.put("customer", c);
		context.put("parkings", parkings);
		context.put("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		context.put("month", Month.of(month).getDisplayName(TextStyle.FULL, Locale.US));
		context.put("year", year);
		
		return new Viewable("/parkings.ftl", context);
	}
}
