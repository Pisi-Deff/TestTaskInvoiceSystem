package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.CustomerType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntryType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.ParkingHouse;

/**
 * Test cases from the examples in the assignment text.
 */
public class ExamplePaymentLogicTests {
	private RegularCustomerPaymentLogic regularLogic = new RegularCustomerPaymentLogic();
	private PremiumCustomerPaymentLogic premiumLogic = new PremiumCustomerPaymentLogic();
	
	private Customer customer;
	private List<Parking> parkings;
	private ParkingHouse parkingHouse;
	
	@Before
	public void setUp() throws Exception {
		parkingHouse = new ParkingHouse()
			.setName("Ultimate Parking House 5000");
		parkings = new ArrayList<>();
	}
	
	/*
		Regular customer parks twice a month
		08:12 – 10:45 (6 * 1.50 = 9.00 EUR)
		19:40 – 20:35 (2 * 1.00 = 2.00 EUR)
		Total invoice: 9.00 + 2.00 = 11.00 EUR
	 */

	@Test
	public void testRegularCustomer() {
		customer = new Customer()
				.setName("Best customer")
				.setType(CustomerType.REGULAR);
		
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 1, 8, 12, 0))
			.setEndDateTime(LocalDateTime.of(2017, 4, 1, 10, 45, 0))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 1, 19, 40, 0))
			.setEndDateTime(LocalDateTime.of(2017, 4, 1, 20, 35, 0))
		);
		
		Invoice i = regularLogic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 2);
		assertEquals(i.getEntries().get(0).getCost(), BigDecimal.valueOf(9).setScale(2));
		assertEquals(i.getEntries().get(1).getCost(), BigDecimal.valueOf(2).setScale(2));
		assertEquals(i.getFinalSum(), BigDecimal.valueOf(11).setScale(2));
	}

	/*
		Premium customer parks four times a month
		08:12 – 10:45 (6 * 1.00 = 6.00 EUR)
		07:02 – 11:56 (10 * 1.00 = 10.00 EUR)
		22:10 – 22:35 (1 * 0.75 = 0.75 EUR)
		19:40 – 20:35 (2 * 0.75 = 1.50 EUR)
		Total invoice: 6.00 + 10.00 + 0.75 + 1.50 + 20.00 = 38.25 EUR
	 */
	@Test
	public void testPremiumCustomer() {
		customer = new Customer()
				.setName("Bestest customer")
				.setType(CustomerType.PREMIUM);
		
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 1, 8, 12, 0))
			.setEndDateTime(LocalDateTime.of(2017, 4, 1, 10, 45, 0))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 2, 7, 2, 0))
			.setEndDateTime(LocalDateTime.of(2017, 4, 2, 11, 56, 0))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 2, 22, 10, 0))
			.setEndDateTime(LocalDateTime.of(2017, 4, 2, 22, 35, 0))
		);
		parkings.add(new Parking()
			.setParkingHouse(parkingHouse)
			.setStartDateTime(LocalDateTime.of(2017, 4, 3, 19, 40, 0))
			.setEndDateTime(LocalDateTime.of(2017, 4, 3, 20, 35, 0))
		);
		
		Invoice i = premiumLogic.calculateInvoice(customer, parkings, YearMonth.of(2017, 4));
		assertEquals(i.getEntries().size(), 5);
		assertEquals(i.getEntries().get(0).getCost(), BigDecimal.valueOf(20).setScale(2));
		assertEquals(i.getEntries().get(0).getType(), InvoiceEntryType.MONTHLY_FEE);
		assertEquals(i.getEntries().get(1).getCost(), BigDecimal.valueOf(6).setScale(2));
		assertEquals(i.getEntries().get(2).getCost(), BigDecimal.valueOf(10).setScale(2));
		assertEquals(i.getEntries().get(3).getCost(), BigDecimal.valueOf(75, 2));
		assertEquals(i.getEntries().get(4).getCost(), BigDecimal.valueOf(150, 2));
		assertEquals(i.getFinalSum(), BigDecimal.valueOf(3825, 2));
	}

}
