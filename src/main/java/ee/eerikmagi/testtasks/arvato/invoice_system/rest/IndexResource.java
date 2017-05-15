package ee.eerikmagi.testtasks.arvato.invoice_system.rest;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

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
	public Viewable indexWithCustomer(@PathParam("customerID") long customerID) {
		Map<String, Object> context = new HashMap<>();
		
		context.put("customers", DumbDB.getCustomers());
		context.put("selectedCustomerID", customerID);
		context.put("customer", DumbDB.getCustomer(customerID));
		context.put("totalParkings", DumbDB.getCustomerParkings(customerID).size());
		context.put("recentParkings", DumbDB.getCustomerRecentParkings(customerID));
		context.put("invoiceYMs", DumbDB.getCustomerInvoices(customerID).keySet());
		context.put("currentYear", LocalDateTime.now().getYear());
		context.put("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		return new Viewable("/index.ftl", context);
	}
	
	@GET
	@Path("{customerID}/invoice/{year}/{month}")
	@Produces(MediaType.TEXT_HTML)
	public Object invoice(
			@PathParam("customerID") long customerID,
			@PathParam("year") long year,
			@PathParam("month") int month
		) {
		Invoice i = DumbDB.getCustomerInvoice(customerID, YearMonth.of((int) year, month));
		
		if (i == null) {
			return Response.status(404).entity("Invoice not found").type(MediaType.TEXT_PLAIN).build();
		}
		
		Customer c = DumbDB.getCustomer(customerID);
		
		Map<String, Object> context = new HashMap<>();
		
		context.put("customer", c);
		context.put("invoice", i);
		context.put("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		context.put("month", Month.of(month).getDisplayName(TextStyle.FULL, Locale.US));
		context.put("year", year);
		
		return new Viewable("/invoice.ftl", context);
	}
	
	@GET
	@Path("{customerID}/genInvoices")
	public Response generateInvoices(
		@PathParam("customerID") long customerID
	) { // don't care about performance right now
		Set<YearMonth> yms = new HashSet<>();
		List<Parking> allParkings = DumbDB.getCustomerParkings(customerID);
		Map<YearMonth, Invoice> invoices = DumbDB.getCustomerInvoices(customerID);
		Customer c = DumbDB.getCustomer(customerID);
		
		for (Parking p : allParkings) {
			yms.add(YearMonth.of(p.getEndDateTime().getYear(), p.getEndDateTime().getMonth()));
		}
		for (YearMonth ym : yms) {
			// skip already calculated invoices but ignore incomplete invoices
			if (invoices.containsKey(ym) && !invoices.get(ym).isIncomplete()) {
				continue;
			}
			
			List<Parking> parkings = DumbDB.getCustomerParkingsForMonth(customerID, ym);
			Invoice i = PaymentLogicFactory.get(c.getType()).calculateInvoice(c, parkings, ym);
			DumbDB.setInvoice(customerID, ym, i);
		}
		
		return Response.seeOther(UriBuilder.fromMethod(IndexResource.class, "indexWithCustomer").build(Long.valueOf(customerID))).build();
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
		List<Parking> parkings = DumbDB.getCustomerParkingsForMonth(customerID, YearMonth.of((int) year, month));
		
		Map<String, Object> context = new HashMap<>();
		
		context.put("customer", c);
		context.put("parkings", parkings);
		context.put("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		context.put("month", Month.of(month).getDisplayName(TextStyle.FULL, Locale.US));
		context.put("year", year);
		
		return new Viewable("/parkings.ftl", context);
	}
}
