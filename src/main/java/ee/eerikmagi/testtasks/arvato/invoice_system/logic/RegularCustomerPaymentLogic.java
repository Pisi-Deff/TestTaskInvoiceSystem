package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegularCustomerPaymentLogic extends AbstractCustomerPaymentLogic {
	public final static BigDecimal TIMEUNIT_COST_CHEAP = BigDecimal.ONE;
	public final static BigDecimal TIMEUNIT_COST_EXPENSIVE = BigDecimal.valueOf(150, 2);
	
	@Override
	protected BigDecimal getTimeUnitCost(LocalDateTime dateTime) {
		return isCheapTime(dateTime) ? TIMEUNIT_COST_CHEAP : TIMEUNIT_COST_EXPENSIVE;
	}
}
