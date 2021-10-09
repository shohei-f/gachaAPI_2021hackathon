package webservices.gacha;

public class GachaBean {

	private String message;

	public GachaBean(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GachaBean [message=" + message + "]";
	}
}
