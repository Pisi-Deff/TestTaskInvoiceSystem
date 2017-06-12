package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.security.InvalidParameterException;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.CustomerType;

/**
 * Factory class to create and fetch a suitable payment logic implementation.
 */
public class PaymentLogicFactory {
	/**
	 * Create and return the payment logic implementation that fits the provided customer type.
	 * 
	 * @param customerType The type of the customer for whom the payment logic is needed.
	 * @return Payment logic implementation.
	 */
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
