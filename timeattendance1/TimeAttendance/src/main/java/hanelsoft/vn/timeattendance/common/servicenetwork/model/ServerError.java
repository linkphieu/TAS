package hanelsoft.vn.timeattendance.common.servicenetwork.model;

public class ServerError {
	private String message;

	public ServerError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}
}
