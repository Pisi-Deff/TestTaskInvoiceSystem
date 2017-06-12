package ee.eerikmagi.testtasks.arvato.invoice_system.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Holds all data needed to render an invoice for the customer.
 */
public class Invoice {
	private List<InvoiceEntry> entries;
	private BigDecimal total;
	private BigDecimal finalSum;
	private boolean incomplete;
	
	public List<InvoiceEntry> getEntries() {
		return entries;
	}
	public Invoice setEntries(List<InvoiceEntry> entries) {
		this.entries = entries;
		return this;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	public Invoice setTotal(BigDecimal total) {
		this.total = total;
		return this;
	}
	
	public BigDecimal getFinalSum() {
		return finalSum;
	}
	public Invoice setFinalSum(BigDecimal finalSum) {
		this.finalSum = finalSum;
		return this;
	}
	
	public boolean isIncomplete() {
		return incomplete;
	}
	public Invoice setIncomplete(boolean incomplete) {
		this.incomplete = incomplete;
		return this;
	}
}
