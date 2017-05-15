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

public class PremiumCustomerPaymentLogic extends AbstractCustomerPaymentLogic {
	public static final BigDecimal COST_MAX_MONTHLY = BigDecimal.valueOf(300);
	public static final BigDecimal COST_MONTHLY_FEE = BigDecimal.valueOf(20);
	public static final BigDecimal TIMEUNIT_COST_CHEAP = BigDecimal.valueOf(75, 2);
	public static final BigDecimal TIMEUNIT_COST_EXPENSIVE = BigDecimal.ONE;
	
	@Override
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym) {
		Invoice invoice = super.calculateInvoice(customer, parkings, ym);
		
		InvoiceEntry monthlyFeeEntry = new InvoiceEntry();

		BigDecimal cost = COST_MONTHLY_FEE;
		if (isCurrentMonth(ym)) {
			LocalDateTime now = LocalDateTime.now();
			int monthDays = ym.getMonth().length(now.toLocalDate().isLeapYear());
			
			BigDecimal ratio = BigDecimal.valueOf(now.getDayOfMonth())
				.divide(BigDecimal.valueOf(monthDays), 8, RoundingMode.DOWN);
			
			cost = cost.multiply(ratio).setScale(2, RoundingMode.DOWN);
			monthlyFeeEntry.setComment("for " + now.getDayOfMonth() + " out of " + monthDays + " days");
		}
		
		invoice.getEntries().add(0, monthlyFeeEntry
			.setCost(cost)
			.setType(InvoiceEntryType.MONTHLY_FEE)
		);
		
		invoice.setTotal(invoice.getTotal().add(cost));
		invoice.setFinalSum(invoice.getTotal().min(COST_MAX_MONTHLY));
		
		return invoice;
	}

	@Override
	protected BigDecimal getTimeUnitCost(LocalDateTime dateTime) {
		return isCheapTime(dateTime) ? TIMEUNIT_COST_CHEAP : TIMEUNIT_COST_EXPENSIVE;
	}
}
