package ee.eerikmagi.testtasks.arvato.invoice_system.app.context;

import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper;
	
	public ObjectMapperProvider() {
		this.mapper = createMapper();
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}
	
	private static ObjectMapper createMapper() {
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper;
	}

}
