package ee.eerikmagi.testtasks.arvato.invoice_system.model;

/**
 * A single customer.
 */
public class Customer {
	private String name;
	private CustomerType type;
	
	public String getName() {
		return name;
	}
	public Customer setName(String name) {
		this.name = name;
		return this;
	}
	
	public CustomerType getType() {
		return type;
	}
	public Customer setType(CustomerType type) {
		this.type = type;
		return this;
	}
}
