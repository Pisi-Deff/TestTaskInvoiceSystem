package ee.eerikmagi.testtasks.arvato.invoice_system.model;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceEntry {
	private Parking parking;
	private List<InvoiceParking> parkingSpans;
	private BigDecimal cost;
	private InvoiceEntryType type;
	
	public Parking getParking() {
		return parking;
	}
	public InvoiceEntry setParking(Parking parking) {
		this.parking = parking;
		return this;
	}
	
	public List<InvoiceParking> getParkingSpans() {
		return parkingSpans;
	}
	public InvoiceEntry setParkingSpans(List<InvoiceParking> parkingSpans) {
		this.parkingSpans = parkingSpans;
		return this;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	public InvoiceEntry setCost(BigDecimal cost) {
		this.cost = cost;
		return this;
	}
	
	public InvoiceEntryType getType() {
		return type;
	}
	public InvoiceEntry setType(InvoiceEntryType type) {
		this.type = type;
		return this;
	}
}
