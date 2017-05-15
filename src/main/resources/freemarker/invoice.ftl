<#include "page.ftl" />

<@page title="Invoice">
	<h3>Invoice for ${month} ${year?c}</h3>
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
					${e.type}
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
										${ps.parking.endDateTime.format(dtFormatter)}
									</li>
								</#list>
							</ul>
						</#if>
					</#if>
				</td>
				<td>${e.cost}</td>
			</tr>
		</#list>
	</table>
	
	<p>Total: ${invoice.total?c}</p>
	<p>Final Sum: ${invoice.finalSum?c}</p>
	
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