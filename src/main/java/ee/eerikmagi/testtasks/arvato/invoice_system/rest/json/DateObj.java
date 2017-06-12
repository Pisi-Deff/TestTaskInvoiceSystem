package ee.eerikmagi.testtasks.arvato.invoice_system.rest.json;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * An easily (de-)serializable POJO for a full date without having to deal with Jackson-Java8 magic.
 */
public class DateObj {
	@NotNull
	@Min(2000)
	private Long year;
	@NotNull
	@Min(1)
	@Max(12)
	private Long month;
	@NotNull
	@Min(1)
	@Max(31)
	private Long day;
	@NotNull
	@Min(0)
	@Max(23)
	private Long hour;
	@NotNull
	@Min(0)
	@Max(59)
	private Long minute;
	@NotNull
	@Min(0)
	@Max(59)
	private Long second;
	
	public Long getYear() {
		return year;
	}
	public void setYear(Long year) {
		this.year = year;
	}
	
	public Long getMonth() {
		return month;
	}
	public void setMonth(Long month) {
		this.month = month;
	}
	
	public Long getDay() {
		return day;
	}
	public void setDay(Long day) {
		this.day = day;
	}
	
	public Long getHour() {
		return hour;
	}
	public void setHour(Long hour) {
		this.hour = hour;
	}
	
	public Long getMinute() {
		return minute;
	}
	public void setMinute(Long minute) {
		this.minute = minute;
	}
	
	public Long getSecond() {
		return second;
	}
	public void setSecond(Long second) {
		this.second = second;
	}
}
