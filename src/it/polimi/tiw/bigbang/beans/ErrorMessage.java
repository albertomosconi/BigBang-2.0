package it.polimi.tiw.bigbang.beans;

public class ErrorMessage {
	private final String title;
	private final String body;

	public ErrorMessage(String title, String body) {
		this.title = title;
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}
}
