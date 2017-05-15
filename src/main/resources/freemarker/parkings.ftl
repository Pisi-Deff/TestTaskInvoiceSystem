<#include "page.ftl" />

<@page title="Parkings">
	<p>
		Customer: ${customer.name}
	</p>
	
	<h3>Parkings for ${month} ${year?c}</h3>
	<#if parkings?has_content>
		<ul>
			<#list parkings as p>
				<li>
					${p.parkingHouse.name}: 
					${p.startDateTime.format(dtFormatter)}
					-
					${p.endDateTime.format(dtFormatter)}
				</li>
			</#list>
		</ul>
	<#else>
		None.
	</#if>
</@page>