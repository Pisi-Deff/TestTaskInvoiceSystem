package ee.eerikmagi.testtasks.arvato.invoice_system.persistence;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.CustomerType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.ParkingHouse;

/**
 * Minimum effort "database".
 * 
 * All data is stored in memory and stored/retrieved with zero thought on performance.
 */
public class DumbDB {
	private static final Map<Long, Customer> customers = new HashMap<>();
	static {
		customers.put(0l, new Customer()
			.setName("Jayne Cobb")
			.setType(CustomerType.REGULAR));
		customers.put(1l, new Customer()
			.setName("Malcolm Reynolds")
			.setType(CustomerType.PREMIUM));
	}
	private static final Map<Long, ParkingHouse> parkingHouses = new HashMap<>();
	static {
		parkingHouses.put(0l, new ParkingHouse()
				.setName("Hoban Washburne Memorial Parking House"));
	}
	private static final Map<Long, List<Parking>> customerParkings = new HashMap<>();
	private static final Map<Long, Map<YearMonth, Invoice>> invoices = new HashMap<>();
	
	public static Customer getCustomer(long id) {
		return customers.get(id);
	}
	
	public static Map<Long, Customer> getCustomers() {
		return customers;
	}
	
	public static ParkingHouse getParkingHouse(long id) {
		return parkingHouses.get(id);
	}
	
	public static Map<Long, ParkingHouse> getParkingHouses() {
		return parkingHouses;
	}
	
	public static List<Parking> getCustomerParkings(long customerID) {
		if (customerParkings.containsKey(customerID)) {
			return customerParkings.get(customerID);
		}
		return Collections.emptyList();
	}
	
	public static List<Parking> getCustomerParkingsForMonth(long customerID, YearMonth ym) {
		List<Parking> parkings = getCustomerParkings(customerID);
		return parkings.stream()
			.filter((p) -> ym.equals(YearMonth.of(p.getEndDateTime().getYear(), p.getEndDateTime().getMonth())))
			.collect(Collectors.toList());
	}
	
	public static void addCustomerParking(long customerID, Parking parking) {
		List<Parking> l;
		if (customerParkings.containsKey(customerID)) {
			l = customerParkings.get(customerID);
		} else {
			l = new ArrayList<>();
			customerParkings.put(customerID, l);
		}
		
		l.add(parking);
	}

	public static List<Parking> getCustomerRecentParkings(long customerID) {
		return getCustomerParkings(customerID).stream()
				.sorted(Collections.reverseOrder())
				.limit(10)
				.collect(Collectors.toList());
	}
	
	public static Map<YearMonth, Invoice> getCustomerInvoices(long customerID) {
		if (invoices.containsKey(customerID)) {
			return invoices.get(customerID);
		}
		
		return Collections.emptyMap();
	}
	
	public static Invoice getCustomerInvoice(long customerID, YearMonth ym) {
		return getCustomerInvoices(customerID).get(ym);
	}
	
	public static void setInvoice(long customerID, YearMonth ym, Invoice i) {
		Map<YearMonth, Invoice> m;
		if (invoices.containsKey(customerID)) {
			m = invoices.get(customerID);
		} else {
			m = new HashMap<>();
			invoices.put(customerID, m);
		}
		
		m.put(ym, i);
	}
}
