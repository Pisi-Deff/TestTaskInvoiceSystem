package ee.eerikmagi.testtasks.arvato.invoice_system.model;

/**
 * The type of the entry on the invoice.
 */
public enum InvoiceEntryType {
	PARKING("Parking"),
	MONTHLY_FEE("Monthly Fee");
	
	private String userFriendlyName;
	
	private InvoiceEntryType(String userFriendlyName) {
		this.userFriendlyName = userFriendlyName;
	}
	
	@Override
	public String toString() {
		return this.userFriendlyName;
	}
}
