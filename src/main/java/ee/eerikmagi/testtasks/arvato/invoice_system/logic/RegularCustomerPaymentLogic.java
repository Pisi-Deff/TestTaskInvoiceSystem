package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment logic implementation for regular customers.
 */
public class RegularCustomerPaymentLogic extends AbstractCustomerPaymentLogic {
	public final static BigDecimal TIMEUNIT_COST_CHEAP = BigDecimal.valueOf(100, 2);
	public final static BigDecimal TIMEUNIT_COST_EXPENSIVE = BigDecimal.valueOf(150, 2);
	
	@Override
	protected BigDecimal getTimeUnitCost(LocalDateTime dateTime) {
		return isCheapTime(dateTime) ? TIMEUNIT_COST_CHEAP : TIMEUNIT_COST_EXPENSIVE;
	}
}
