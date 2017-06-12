package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntry;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntryType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceParking;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;

/**
 * Abstract base class for customer payment logic.
 * 
 * Contains logic that is shared between {@link RegularCustomerPaymentLogic} and {@link PremiumCustomerPaymentLogic}
 */
public abstract class AbstractCustomerPaymentLogic implements IPaymentLogic {
	private static final int HOUR_EXPENSIVE_TIME_END = 19;
	private static final int HOUR_EXPENSIVE_TIME_START = 7;
	private static final int TIMEUNIT_MINUTES = 30;
	
	@Override
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym) {
		return calculateInvoice(customer, parkings, ym, LocalDateTime.now());
	}

	@Override
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym, LocalDateTime dt) {
		List<InvoiceEntry> entries = new ArrayList<>();
		
		parkings.stream().forEach((p) -> entries.add(getParkingEntry(p)));
		
		BigDecimal total = entries.stream()
			.map((e) -> e.getCost())
			.reduce(BigDecimal.ZERO, (tot, cost) -> tot.add(cost));
		
		Invoice inv = new Invoice()
			.setEntries(entries)
			.setTotal(total)
			.setFinalSum(total)
			.setIncomplete(isCurrentMonth(dt, ym));
		
		return inv;
	}

	/**
	 * Creates an {@link InvoiceEntry} based on the provided {@link Parking} object.
	 * Calculates the spans and costs of that parking.
	 * 
	 * @param parking The parking object to handle.
	 * @return The invoice entry for the given parking object.
	 */
	protected InvoiceEntry getParkingEntry(Parking parking) {
		List<InvoiceParking> parkings = new ArrayList<>();
		
		LocalDateTime start = parking.getStartDateTime();
		LocalDateTime end = parking.getEndDateTime();
		
		LocalDateTime cursor = start;
		long ipTimeUnitsCount = 0;
		
		// go through the time difference in TIMEUNIT_MINUTES chunks and split it into spans if
		// the parking goes through a cheap-expensive time change
		while (cursor.isBefore(end)) {
			ipTimeUnitsCount++;
			
			LocalDateTime next = cursor.plusMinutes(TIMEUNIT_MINUTES);
			if (next.isBefore(end) && hasTimerangeChanged(cursor, next)) {
				parkings.add(createParkingSpan(parking, start, next, ipTimeUnitsCount));
				ipTimeUnitsCount = 0;
				start = next;
			}
			
			cursor = next;
		}
		parkings.add(createParkingSpan(parking, start, end, ipTimeUnitsCount));
		
		return new InvoiceEntry()
			.setParking(parking)
			.setParkingSpans(parkings)
			.setType(InvoiceEntryType.PARKING)
			.setCost(parkings.stream()
				.map((p) -> p.getCost())
				.reduce(BigDecimal.ZERO, (tot, cost) -> tot.add(cost))
				.setScale(2));
	}

	protected boolean hasTimerangeChanged(LocalDateTime cursor, LocalDateTime next) {
		return isCheapTime(cursor) && !isCheapTime(next) ||
				!isCheapTime(cursor) && isCheapTime(next);
	}

	protected InvoiceParking createParkingSpan(Parking p, LocalDateTime start, LocalDateTime end, long ipTimeUnitsCount) {
		BigDecimal timeUnitCost = getTimeUnitCost(start);
		
		return new InvoiceParking()
			.setParking(new Parking()
				.setStartDateTime(start)
				.setEndDateTime(end)
				.setParkingHouse(p.getParkingHouse())
			)
			.setTimeUnitMinutes(TIMEUNIT_MINUTES)
			.setTimeUnitsCount(ipTimeUnitsCount)
			.setTimeUnitCost(timeUnitCost)
			.setCost(timeUnitCost.multiply(BigDecimal.valueOf(ipTimeUnitsCount)));
	}
	
	protected boolean isCheapTime(LocalDateTime dt) {
		return dt.getHour() < HOUR_EXPENSIVE_TIME_START || dt.getHour() >= HOUR_EXPENSIVE_TIME_END;
	}
	
	protected boolean isCurrentMonth(LocalDateTime dt, YearMonth ym) {
		return YearMonth.from(dt).equals(ym);
	}
	
	/**
	 * Returns the cost per time unit at the provided time. 
	 */
	abstract protected BigDecimal getTimeUnitCost(LocalDateTime dateTime);

}
