package ee.eerikmagi.testtasks.arvato.invoice_system.app;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import ee.eerikmagi.testtasks.arvato.invoice_system.app.context.ObjectMapperProvider;

@ApplicationPath("")
public class InvoiceSystemApplication extends ResourceConfig {
	public InvoiceSystemApplication() {
		property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH, "freemarker");
		register(FreemarkerMvcFeature.class);
		register(ObjectMapperProvider.class);
		register(JacksonFeature.class);
		packages("ee.eerikmagi.testtasks.arvato.invoice_system.rest");
	}
}
