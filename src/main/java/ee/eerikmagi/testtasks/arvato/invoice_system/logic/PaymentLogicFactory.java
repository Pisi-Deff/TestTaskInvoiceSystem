package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.security.InvalidParameterException;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.CustomerType;

public class PaymentLogicFactory {
	public static IPaymentLogic get(CustomerType customerType) {
		switch(customerType) {
		case REGULAR:
			return new RegularCustomerPaymentLogic();
		case PREMIUM:
			return new PremiumCustomerPaymentLogic();
		default:
			throw new InvalidParameterException("Invalid customer type: " + customerType);
		}
	}
}
