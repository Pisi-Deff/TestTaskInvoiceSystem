package ee.eerikmagi.testtasks.arvato.invoice_system.persistence;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.CustomerType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.ParkingHouse;

/**
 * Minimum effort "database".
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
	
	public static List<Parking> getCustomerParkingsForMonth(long customerID, long year, Month month) {
		List<Parking> parkings = getCustomerParkings(customerID);
		return parkings.stream()
			.filter((p) -> month.equals(p.getEndDateTime().getMonth()) && year == p.getEndDateTime().getYear())
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
}
