<#include "page.ftl" />

<@page title="Invoice">
	<h3>Invoice for ${month} ${year?c}<#if invoice.incomplete> (<strong>INCOMPLETE</strong>)</#if></h3>
	<p>
		Customer: ${customer.name}
	</p>
	
	<table>
		<tr>
			<th>Description</th>
			<th>Cost</th>
		</tr>
		<#list invoice.entries as e>
			<tr>
				<td>
					<strong>${e.type}</strong>
					<#if e.type.name() == "PARKING">
						<br />
						(
						${e.parking.parkingHouse.name}: 
						${e.parking.startDateTime.format(dtFormatter)}
						-
						${e.parking.endDateTime.format(dtFormatter)}
						)
						
						<#if e.parkingSpans?size gt 1>
							<br />
							<ul>
								<#list e.parkingSpans as ps>
									<li>
										${ps.parking.startDateTime.format(dtFormatter)}
										-
										${ps.parking.endDateTime.format(dtFormatter)}:
										&nbsp;
										${ps.timeUnitCost} per ${ps.timeUnitMinutes} minutes
										<strong>*</strong>
										${ps.timeUnitsCount} counts
										=
										<strong>${ps.cost}</strong>
									</li>
								</#list>
							</ul>
						<#else>
							<br />
							${e.parkingSpans[0].timeUnitCost} per
							${e.parkingSpans[0].timeUnitMinutes} minutes
							<strong>*</strong>
							${e.parkingSpans[0].timeUnitsCount} counts
							=
							<strong>${e.parkingSpans[0].cost}</strong>
						</#if>
					</#if>
					<#if e.comment?has_content>
						<br />
						(${e.comment})
					</#if>
				</td>
				<td>${e.cost}</td>
			</tr>
		</#list>
	</table>
	
	<h4>Total: ${invoice.total}</h4>
	<h4>Final Sum: ${invoice.finalSum}</h4>
	
	<#if invoice.incomplete>
		<strong>NOTE:</strong>
		This invoice is incomplete because when it was generated the month had not yet ended.
	</#if>
	
	<style>
		table {
			border-collapse: collapse;
		}
		
		table th,
		table td {
			min-width: 100px;
			padding: 10px;
			border: 1px solid black;
		}
		
		table tr > :first-child {
			min-width: 300px;
		}
	</style>
</@page>