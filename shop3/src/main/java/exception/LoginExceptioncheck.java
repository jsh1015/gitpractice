package exception;

public class LoginExceptioncheck extends RuntimeException {
	private String url;
	public LoginExceptioncheck(String msg, String url) {
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url; //login.shop
	}
}