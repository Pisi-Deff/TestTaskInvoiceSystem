<#include "page.ftl" />
<#include "customerSelect.ftl" />

<@page title="Parking Invoice System">
	<h2>Welcome to the Minimal Effort (TM) UI!</h2>
	
	<@customerSelect />
	
	<#if selectedCustomerID??>
		<h3>Info</h3>
		Customer: ${customer.name}<br />
		Total times parked: ${totalParkings}<br />
	
		<h3>Invoices</h3>
		<form id="invoices" autocomplete="off">
			<select name="ym">
				<option value="" selected disabled>- Select invoice -</option>
				<#list invoiceYMs as ym>
					<option value="${ym.year?c}-${ym.monthValue?c}">${ym.year?c}-${ym.monthValue?c}</option>
				</#list>
			</select>
			<button type="submit">Go</button>
		</form>
		
		<br />
		<form action="./${selectedCustomerID}/genInvoices" method="GET">
			<button type="submit">Generate Invoices</button>
		</form>
		
		<h3>Parkings</h3>
		<@dateForm id="parkings" />
		
		<h3>Recent parkings</h3>
		<#if recentParkings?has_content>
			<ul>
				<#list recentParkings as p>
					<li>
						${p.parkingHouse.name}: 
						${p.startDateTime.format(dtFormatter)} - ${p.endDateTime.format(dtFormatter)}
					</li>
				</#list>
			</ul>
		<#else>
			None.
		</#if>
		
		<script>
			$(() => {
				$('#invoices').submit((ev) => {
					ev.preventDefault();
					const form = $('#invoices').serializeArray().reduce((o, el) => {o[el.name] = el.value; return o;}, {});
					if (form.ym) {
						const [y, m] = form.ym.split('-');
						let newURL = document.location.href.substring(0, document.location.href.indexOf('invoice_system') + 'invoice_system'.length);
						newURL += '/${selectedCustomerID}/invoice/' + y + '/' + m;
						
						document.location.href = newURL;
					}
				});
				$('#parkings').submit((ev) => {
					ev.preventDefault();
					const form = $('#parkings').serializeArray().reduce((o, el) => {o[el.name] = el.value; return o;}, {});
					if (form.year && form.month) {
						let newURL = document.location.href.substring(0, document.location.href.indexOf('invoice_system') + 'invoice_system'.length);
						newURL += '/${selectedCustomerID}/parkings/' + form.year + '/' + form.month;
						
						document.location.href = newURL;
					}
				});
			});
		</script>
	</#if>
</@page>

<#macro dateForm id>
	<form method="post" action="#" id="${id}" autocomplete="off">
		<label>
			Year
			<select name="year">
				<option value="" selected>- Select Year -</option>
				<#list currentYear..*-10 as y>
					<option value="${y?c}">${y?c}</option>
				</#list>
			</select>
		</label>
		<label>
			Month
			<select name="month">
				<option value="" selected>- Select Month -</option>
				<option value="1">January</option>
				<option value="2">February</option>
				<option value="3">March</option>
				<option value="4">April</option>
				<option value="5">May</option>
				<option value="6">June</option>
				<option value="7">July</option>
				<option value="8">August</option>
				<option value="9">September</option>
				<option value="10">October</option>
				<option value="11">November</option>
				<option value="12">December</option>
			</select>
		</label>
		<button type="submit">Go</button>
	</form>
</#macro>