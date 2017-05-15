<#macro customerSelect>
	<label>
		Select customer:
		<select id="customerSelect">
			<option value="" <#if selectedCustomerID??><#else>selected</#if>>- Select Customer -</option>
			<#list customers as id, c>
				<option value="${id}" <#if selectedCustomerID?? && selectedCustomerID == id>selected</#if>>${c.name}</option>
			</#list>
		</select>
	</label>
	
	<script>
		$(() => {
			$('#customerSelect').change((ev) => {
				const id = $(ev.target).val();
				
				let newURL = document.location.href.substring(0, document.location.href.indexOf('invoice_system') + 'invoice_system'.length);
				if (id && id.length) {
					newURL += '/' + id;
				}
				
				document.location.href = newURL;
			});
		});
	</script>
</#macro>