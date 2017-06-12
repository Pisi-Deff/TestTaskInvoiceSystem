package ee.eerikmagi.testtasks.arvato.invoice_system.app;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import ee.eerikmagi.testtasks.arvato.invoice_system.app.context.ObjectMapperProvider;

@ApplicationPath("")
public class InvoiceSystemApplication extends ResourceConfig {
	public InvoiceSystemApplication() {
		// Freemarker templating support for rendering html
		property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH, "freemarker");
		register(FreemarkerMvcFeature.class);
		
		// for JSON support used by REST API
		register(ObjectMapperProvider.class);
		register(JacksonFeature.class);
		
		// read resource classes
		packages("ee.eerikmagi.testtasks.arvato.invoice_system.rest");
	}
}
