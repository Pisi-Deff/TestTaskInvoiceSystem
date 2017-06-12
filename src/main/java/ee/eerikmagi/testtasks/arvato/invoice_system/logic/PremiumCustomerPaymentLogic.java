package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntry;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.InvoiceEntryType;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;

/**
 * Payment logic implementation for premium customers.
 */
public class PremiumCustomerPaymentLogic extends AbstractCustomerPaymentLogic {
	public static final BigDecimal COST_MAX_MONTHLY = BigDecimal.valueOf(300).setScale(2);
	public static final BigDecimal COST_MONTHLY_FEE = BigDecimal.valueOf(20).setScale(2);
	public static final BigDecimal TIMEUNIT_COST_CHEAP = BigDecimal.valueOf(75, 2);
	public static final BigDecimal TIMEUNIT_COST_EXPENSIVE = BigDecimal.valueOf(100, 2);
	
	@Override
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym, LocalDateTime dt) {
		Invoice invoice = super.calculateInvoice(customer, parkings, ym, dt);
		
		InvoiceEntry monthlyFeeEntry = createMonthlyFeeEntry(ym, dt);
		invoice.getEntries().add(0, monthlyFeeEntry);
		invoice.setTotal(invoice.getTotal().add(monthlyFeeEntry.getCost()));
		
		invoice.setFinalSum(invoice.getTotal().min(COST_MAX_MONTHLY));
		
		return invoice;
	}

	private InvoiceEntry createMonthlyFeeEntry(YearMonth ym, LocalDateTime dt) {
		InvoiceEntry monthlyFeeEntry = new InvoiceEntry()
			.setType(InvoiceEntryType.MONTHLY_FEE);

		BigDecimal cost = COST_MONTHLY_FEE;
		if (isCurrentMonth(dt, ym)) {
			int monthDays = ym.getMonth().length(dt.toLocalDate().isLeapYear());
			
			BigDecimal ratio = BigDecimal.valueOf(dt.getDayOfMonth())
				.divide(BigDecimal.valueOf(monthDays), 8, RoundingMode.DOWN);
			
			cost = cost.multiply(ratio).setScale(2, RoundingMode.DOWN);
			monthlyFeeEntry.setComment("for " + dt.getDayOfMonth() + " out of " + monthDays + " days");
		}
		
		return monthlyFeeEntry.setCost(cost);
	}

	@Override
	protected BigDecimal getTimeUnitCost(LocalDateTime dateTime) {
		return isCheapTime(dateTime) ? TIMEUNIT_COST_CHEAP : TIMEUNIT_COST_EXPENSIVE;
	}
}
