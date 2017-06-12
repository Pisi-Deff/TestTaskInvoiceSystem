package ee.eerikmagi.testtasks.arvato.invoice_system.rest.json;

import javax.validation.constraints.NotNull;

/**
 * Request object for the Add Parking REST API request.
 */
public class AddParkingRequest {
	@NotNull
	private Long customerID;
	@NotNull
	private Long parkingHouseID;
	@NotNull
	private DateObj start;
	@NotNull
	private DateObj end;
	
	public Long getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}
	
	public Long getParkingHouseID() {
		return parkingHouseID;
	}
	public void setParkingHouseID(Long parkingHouseID) {
		this.parkingHouseID = parkingHouseID;
	}
	
	public DateObj getStart() {
		return start;
	}
	public void setStart(DateObj start) {
		this.start = start;
	}
	
	public DateObj getEnd() {
		return end;
	}
	public void setEnd(DateObj end) {
		this.end = end;
	}
}
