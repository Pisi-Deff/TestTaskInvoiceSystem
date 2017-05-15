package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.CustomerType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntry;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntryType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceParking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.ParkingHouse;

public class RegularCustomerPaymentLogicTests {
	private RegularCustomerPaymentLogic logic = new RegularCustomerPaymentLogic();
	
	private Customer customer;
	private List<Parking> parkings;
	private ParkingHouse parkingHouse;
	
	@Before
	public void setUp() throws Exception {
		customer = new Customer()
			.setName("Best customer")
			.setType(CustomerType.REGULAR);
		parkingHouse = new ParkingHouse()
			.setName("Ultimate Parking House 5000");
		parkings = new ArrayList<>();
	}

	@Test
	public void testCheapTimeCost() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 5, 10, 4, 8, 15))
			.setEndDateTime(LocalDateTime.of(2017, 5, 10, 4, 8, 16))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings);
		assertEquals(i.getEntries().size(), 1);
		assertEquals(i.getEntries().get(0).getParkingSpans().size(), 1);
		assertEquals(i.getEntries().get(0).getCost(), i.getTotal());
		assertEquals(i.getEntries().get(0).getCost(), RegularCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		assertEquals(i.getEntries().get(0).getType(), InvoiceEntryType.PARKING);
		assertEquals(i.getTotal(), RegularCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		assertEquals(i.getTotal(), i.getFinalSum());
	}

	@Test
	public void testExpensiveTimeCost() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 5, 10, 16, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 5, 10, 16, 23, 43))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings);
		assertEquals(i.getEntries().size(), 1);
		assertEquals(i.getEntries().get(0).getParkingSpans().size(), 1);
		assertEquals(i.getEntries().get(0).getCost(), i.getTotal());
		assertEquals(i.getEntries().get(0).getCost(), RegularCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE);
		assertEquals(i.getEntries().get(0).getType(), InvoiceEntryType.PARKING);
		assertEquals(i.getTotal(), RegularCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE);
		assertEquals(i.getTotal(), i.getFinalSum());
	}

	@Test
	public void testMultiSpanParkingSplitting() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 5, 10, 6, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 5, 10, 20, 23, 43))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings);
		assertEquals(i.getEntries().size(), 1);
		InvoiceEntry e = i.getEntries().get(0);
		assertEquals(e.getParkingSpans().size(), 3);
		
		InvoiceParking p0 = e.getParkingSpans().get(0);
		assertEquals(p0.getParking().getStartDateTime(), parkings.get(0).getStartDateTime());
		assertEquals(p0.getParking().getEndDateTime(), LocalDateTime.of(2017, 5, 10, 7, 23, 42));
		assertEquals(p0.getTimeUnitCost(), RegularCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		InvoiceParking p1 = e.getParkingSpans().get(1);
		assertEquals(p1.getParking().getStartDateTime(), LocalDateTime.of(2017, 5, 10, 7, 23, 42));
		assertEquals(p1.getParking().getEndDateTime(), LocalDateTime.of(2017, 5, 10, 19, 23, 42));
		assertEquals(p1.getTimeUnitCost(), RegularCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE);
		InvoiceParking p2 = e.getParkingSpans().get(2);
		assertEquals(p2.getParking().getStartDateTime(), LocalDateTime.of(2017, 5, 10, 19, 23, 42));
		assertEquals(p2.getParking().getEndDateTime(), parkings.get(0).getEndDateTime());
		assertEquals(p2.getTimeUnitCost(), RegularCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		
		assertEquals(e.getCost(), i.getTotal());
		assertEquals(e.getType(), InvoiceEntryType.PARKING);
		assertEquals(i.getTotal(), 
				RegularCustomerPaymentLogic.TIMEUNIT_COST_CHEAP
					.multiply(BigDecimal.valueOf(2))
			.add(
				RegularCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE
					.multiply(BigDecimal.valueOf(24)))
			.add(
				RegularCustomerPaymentLogic.TIMEUNIT_COST_CHEAP
					.multiply(BigDecimal.valueOf(3)))
		);
		assertEquals(i.getTotal(), i.getFinalSum());
	}
	
	@Test
	public void testMultipleParkings() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 5, 10, 4, 8, 15))
			.setEndDateTime(LocalDateTime.of(2017, 5, 10, 4, 8, 16))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 5, 10, 16, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 5, 10, 16, 23, 43))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 5, 10, 6, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 5, 10, 20, 23, 43))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings);
		assertEquals(i.getEntries().size(), 3);
		for (InvoiceEntry e : i.getEntries()) {
			assertEquals(e.getType(), InvoiceEntryType.PARKING);
			assertNotNull(e.getParking());
			assertNotNull(e.getParkingSpans());
		}
	}

}
