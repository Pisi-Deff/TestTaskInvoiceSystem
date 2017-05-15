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

public abstract class AbstractCustomerPaymentLogic implements IPaymentLogic {
	private static final int HOUR_EXPENSIVE_TIME_END = 19;
	private static final int HOUR_EXPENSIVE_TIME_START = 7;
	private static final int TIMEUNIT_MINUTES = 30;

	@Override
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym) {
		List<InvoiceEntry> entries = new ArrayList<>();
		
		parkings.stream().forEach((p) -> entries.add(getParkingEntry(p)));
		
		BigDecimal total = entries.stream()
			.map((e) -> e.getCost())
			.reduce(BigDecimal.ZERO, (tot, cost) -> tot.add(cost));
		
		Invoice inv = new Invoice()
			.setEntries(entries)
			.setTotal(total)
			.setFinalSum(total)
			.setIncomplete(isCurrentMonth(ym));
		
		return inv;
	}

	protected InvoiceEntry getParkingEntry(Parking parking) {
		List<InvoiceParking> parkings = new ArrayList<>();
		
		LocalDateTime start = parking.getStartDateTime();
		LocalDateTime end = parking.getEndDateTime();
		
		LocalDateTime cursor = start;
		long ipTimeUnitsCount = 0;
		
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
				.reduce(BigDecimal.ZERO, (tot, cost) -> tot.add(cost)));
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
	
	protected boolean isCurrentMonth(YearMonth ym) {
		return YearMonth.now().equals(ym);
	}
	
	abstract protected BigDecimal getTimeUnitCost(LocalDateTime dateTime);

}
