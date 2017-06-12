package ee.eerikmagi.testtasks.arvato.invoice_system.model;

import java.math.BigDecimal;

/**
 * A single span of parking listed on the {@link Invoice}.
 * A single parking record may be split into multiple invoice parking entries.
 */
public class InvoiceParking {
	private Parking parking;
	private long timeUnitsCount;
	private long timeUnitMinutes;
	private BigDecimal timeUnitCost;
	private BigDecimal cost;
	
	public Parking getParking() {
		return parking;
	}
	public InvoiceParking setParking(Parking parking) {
		this.parking = parking;
		return this;
	}
	
	public long getTimeUnitsCount() {
		return timeUnitsCount;
	}
	public InvoiceParking setTimeUnitsCount(long timeUnitsCount) {
		this.timeUnitsCount = timeUnitsCount;
		return this;
	}
	
	public long getTimeUnitMinutes() {
		return timeUnitMinutes;
	}
	public InvoiceParking setTimeUnitMinutes(long timeUnitMinutes) {
		this.timeUnitMinutes = timeUnitMinutes;
		return this;
	}
	
	public BigDecimal getTimeUnitCost() {
		return timeUnitCost;
	}
	public InvoiceParking setTimeUnitCost(BigDecimal timeUnitCost) {
		this.timeUnitCost = timeUnitCost;
		return this;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	public InvoiceParking setCost(BigDecimal cost) {
		this.cost = cost;
		return this;
	}
}
