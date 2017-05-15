package ee.eerikmagi.testtasks.arvato.invoice_system.logic;

import java.time.YearMonth;
import java.util.List;

import ee.eerikmagi.testtasks.arvato.invoice_system.model.Customer;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Invoice;
import ee.eerikmagi.testtasks.arvato.invoice_system.model.Parking;

public interface IPaymentLogic {
	public Invoice calculateInvoice(Customer customer, List<Parking> parkings, YearMonth ym);
}
