package ee.eerikmagi.testtasks.arvato.invoice_system.rest.json;

public class JsonResponse {
	private boolean success;

	public boolean isSuccess() {
		return success;
	}
	public JsonResponse setSuccess(boolean success) {
		this.success = success;
		return this;
	}
}
