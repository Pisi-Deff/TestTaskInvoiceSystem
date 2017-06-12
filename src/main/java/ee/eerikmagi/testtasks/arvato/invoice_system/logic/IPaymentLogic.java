package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;

/**
 * Interface for parking invoice calculation.
 * <br /><br />
 * Methods:
 * <br />
 * {@link #calculateInvoice(Customer, List, YearMonth)}<br />
 * {@link #calculateInvoice(Customer, List, YearMonth, LocalDateTime)} (for testing)
 */
public interface IPaymentLogic {
	/**
	 * Calculates the invoice.
	 * (Uses the current system time whenever the calculation logic requires current time.)
	 * 
	 * @param customer The customer to generate the invoice for.
	 * @param parkings A list of parkings.
	 * 		It is assumed that they are all for the year and month provided in the `ym` parameter.
	 * @param ym The year and month for which to generate the invoice.
	 * @return The generated invoice.
	 */
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym);
	
	/**
	 * Calculates the invoice using the provided time for any logic that uses the current time.
	 * 
	 * NOTE: Intended for use in testing only.
	 *  
	 * @param customer The customer to generate the invoice for.
	 * @param parkings A list of parkings.
	 * 		It is assumed that they are all for the year and month provided in the `ym` parameter.
	 * @param ym The year and month for which to generate the invoice.
	 * @param dt The date and time to use as the current time.
	 * @return The generated invoice.
	 */
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym, LocalDateTime dt);
}
