package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

public class PremiumCustomerPaymentLogicTests {
	private PremiumCustomerPaymentLogic logic = new PremiumCustomerPaymentLogic();
	
	private Customer customer;
	private List<Parking> parkings;
	private ParkingHouse parkingHouse;
	
	@Before
	public void setUp() throws Exception {
		customer = new Customer()
			.setName("Bestestest customer")
			.setType(CustomerType.PREMIUM);
		parkingHouse = new ParkingHouse()
			.setName("Ultimate Parking House 5001");
		parkings = new ArrayList<>();
	}
	
	@Test
	public void testMonthlyFee() {
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 1);
		InvoiceEntry e = i.getEntries().get(0);
		assertEquals(e.getCost(), PremiumCustomerPaymentLogic.COST_MONTHLY_FEE);
		assertEquals(e.getType(), InvoiceEntryType.MONTHLY_FEE);
		assertNull(e.getParking());
		assertNull(e.getParkingSpans());
		
		assertEquals(i.getTotal(), PremiumCustomerPaymentLogic.COST_MONTHLY_FEE);
		checkFinalSum(i);
	}
	
	@Test
	public void testIncompleteInvoice() {
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.now());
		assertTrue(i.isIncomplete());
	}

	@Test
	public void testCheapTimeCost() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 10, 4, 8, 15))
			.setEndDateTime(LocalDateTime.of(2017, 4, 10, 4, 8, 16))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 2);
		assertEquals(i.getEntries().get(1).getParkingSpans().size(), 1);
		assertEquals(i.getEntries().get(1).getCost(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		assertEquals(i.getEntries().get(1).getType(), InvoiceEntryType.PARKING);
		assertEquals(i.getTotal(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_CHEAP.add(PremiumCustomerPaymentLogic.COST_MONTHLY_FEE));
		checkFinalSum(i);
	}

	@Test
	public void testExpensiveTimeCost() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 10, 16, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 4, 10, 16, 23, 43))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 2);
		assertEquals(i.getEntries().get(1).getParkingSpans().size(), 1);
		assertEquals(i.getEntries().get(1).getCost(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE);
		assertEquals(i.getEntries().get(1).getType(), InvoiceEntryType.PARKING);
		assertEquals(i.getTotal(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE.add(PremiumCustomerPaymentLogic.COST_MONTHLY_FEE));
		checkFinalSum(i);
	}

	@Test
	public void testMultiSpanParkingSplitting() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 10, 6, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 4, 10, 20, 23, 43))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 2);
		InvoiceEntry e = i.getEntries().get(1);
		assertEquals(e.getParkingSpans().size(), 3);
		
		InvoiceParking p0 = e.getParkingSpans().get(0);
		assertEquals(p0.getParking().getStartDateTime(), parkings.get(0).getStartDateTime());
		assertEquals(p0.getParking().getEndDateTime(), LocalDateTime.of(2017, 4, 10, 7, 23, 42));
		assertEquals(p0.getTimeUnitCost(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		InvoiceParking p1 = e.getParkingSpans().get(1);
		assertEquals(p1.getParking().getStartDateTime(), LocalDateTime.of(2017, 4, 10, 7, 23, 42));
		assertEquals(p1.getParking().getEndDateTime(), LocalDateTime.of(2017, 4, 10, 19, 23, 42));
		assertEquals(p1.getTimeUnitCost(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE);
		InvoiceParking p2 = e.getParkingSpans().get(2);
		assertEquals(p2.getParking().getStartDateTime(), LocalDateTime.of(2017, 4, 10, 19, 23, 42));
		assertEquals(p2.getParking().getEndDateTime(), parkings.get(0).getEndDateTime());
		assertEquals(p2.getTimeUnitCost(), PremiumCustomerPaymentLogic.TIMEUNIT_COST_CHEAP);
		
		BigDecimal eCost = 
			PremiumCustomerPaymentLogic.TIMEUNIT_COST_CHEAP
					.multiply(BigDecimal.valueOf(2))
			.add(
				PremiumCustomerPaymentLogic.TIMEUNIT_COST_EXPENSIVE
					.multiply(BigDecimal.valueOf(24)))
			.add(
				PremiumCustomerPaymentLogic.TIMEUNIT_COST_CHEAP
					.multiply(BigDecimal.valueOf(3)));
		assertEquals(e.getCost(), eCost);
		
		assertEquals(e.getType(), InvoiceEntryType.PARKING);
		assertEquals(i.getTotal(), eCost.add(PremiumCustomerPaymentLogic.COST_MONTHLY_FEE));
		checkFinalSum(i);
	}
	
	@Test
	public void testMonthlyCostLimit() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 2, 4, 8, 15))
			.setEndDateTime(LocalDateTime.of(2017, 4, 20, 4, 8, 16))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		
		assertEquals(1, i.getTotal().compareTo(PremiumCustomerPaymentLogic.COST_MAX_MONTHLY));
		assertEquals(PremiumCustomerPaymentLogic.COST_MAX_MONTHLY, i.getFinalSum());
	}
	
	@Test
	public void testMultipleParkings() {
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 10, 4, 8, 15))
			.setEndDateTime(LocalDateTime.of(2017, 4, 10, 4, 8, 16))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 10, 16, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 4, 10, 16, 23, 43))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 10, 6, 23, 42))
			.setEndDateTime(LocalDateTime.of(2017, 4, 10, 20, 23, 43))
		);
		
		Invoice i = logic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 4);
		for (InvoiceEntry e : i.getEntries().stream().skip(1).collect(Collectors.toList())) {
			assertEquals(e.getType(), InvoiceEntryType.PARKING);
			assertNotNull(e.getParking());
			assertNotNull(e.getParkingSpans());
		}
	}

	private void checkFinalSum(Invoice i) {
		assertEquals(PremiumCustomerPaymentLogic.COST_MAX_MONTHLY.min(i.getTotal()), i.getFinalSum());
	}
}
