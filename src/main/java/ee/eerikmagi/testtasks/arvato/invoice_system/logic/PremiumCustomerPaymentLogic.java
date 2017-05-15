package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings) {
		Invoice invoice = super.calculateInvoice(customer, parkings);
		
		invoice.getEntries().add(0, new InvoiceEntry()
			.setCost(COST_MONTHLY_FEE)
			.setType(InvoiceEntryType.MONTHLY_FEE)
		);
		
		invoice.setTotal(invoice.getTotal().add(COST_MONTHLY_FEE));
		invoice.setFinalSum(invoice.getTotal().min(COST_MAX_MONTHLY));
		
		return invoice;
	}

	@Override
	protected BigDecimal getTimeUnitCost(LocalDateTime dateTime) {
		return isCheapTime(dateTime) ? TIMEUNIT_COST_CHEAP : TIMEUNIT_COST_EXPENSIVE;
	}
}
