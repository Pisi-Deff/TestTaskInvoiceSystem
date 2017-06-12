package ee.eerikmagi.testtasks.arvato.invoice_system.model;

import java.time.LocalDateTime;

/**
 * A single record of parking that is tied to a specific parking house and has start and end dates.
 */
public class Parking implements Comparable<Parking> {
	private ParkingHouse parkingHouse;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	
	public ParkingHouse getParkingHouse() {
		return parkingHouse;
	}
	public Parking setParkingHouse(ParkingHouse parkingHouse) {
		this.parkingHouse = parkingHouse;
		return this;
	}
	
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	public Parking setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
		return this;
	}
	
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}
	public Parking setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
		return this;
	}
	
	@Override
	public int compareTo(Parking o) {
		return this.endDateTime.compareTo(o.getEndDateTime());
	}
}
