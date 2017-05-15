package ee.eerikmagi.testtasks.arvato.invoice_system.persistence;

public class YearMonth {
	private long year;
	private long month;
	
	public long getYear() {
		return this.year;
	}
	public YearMonth setYear(long year) {
		this.year = year;
		return this;
	}
	
	public long getMonth() {
		return this.month;
	}
	public YearMonth setMonth(long month) {
		this.month = month;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.month ^ (this.month >>> 32));
		result = prime * result + (int) (this.year ^ (this.year >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YearMonth other = (YearMonth) obj;
		if (this.month != other.month)
			return false;
		if (this.year != other.year)
			return false;
		return true;
	}
}
