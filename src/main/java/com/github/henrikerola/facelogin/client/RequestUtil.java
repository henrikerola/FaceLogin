package com.github.henrikerola.facelogin.client;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

import elemental.client.Browser;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.FormData;

public class RequestUtil {

	private static String API_KEY = "your_key";
	private static String API_SECRET = "your_secret";
	private static String API_AUTH = "&api_key=" + API_KEY + "&api_secret="
			+ API_SECRET;

	public interface Callback {
		void callback(String response);
	}

	private static void doRequest(String url, final Callback callback) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,
				URL.encode(url + API_AUTH));

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("error");
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						callback.callback(response.getText());
					} else {
						Window.alert("" + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("" + e);
		}
	}

	public static void save(String userId, List<String> tIds, Callback callback) {
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for (String tId : tIds) {
			if (first) {
				b.append(tId);
				first = false;
			} else {
				b.append("," + tId);
			}
		}
		doRequest("http://api.face.com/tags/save.json?uid=" + userId + "&tids="
				+ b.toString(), callback);
	}

	public static void removeTags(List<String> tIds, Callback callback) {
		if (tIds.isEmpty()) {
			return;
		}
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for (String tId : tIds) {
			if (first) {
				b.append(tId);
				first = false;
			} else {
				b.append("," + tId);
			}
		}
		doRequest("http://api.face.com/tags/remove.json?tids=" + b.toString(),
				callback);
	}

	public static void getTags(String userId, Callback callback) {
		doRequest("http://api.face.com/tags/get.json?uids=" + userId, callback);
	}

	public static void train(String userId) {
		doRequest("http://api.face.com/faces/train.json?uids=" + userId,
				new Callback() {
					@Override
					public void callback(String response) {
						Window.alert(response);
					}
				});
	}

	public static void getUsers(Callback callback) {
		doRequest("http://api.face.com/account/users.json?namespaces="
				+ FaceLogin.NAMESPACE, callback);
	}

	public static void getStatus() {
		doRequest("http://api.face.com/faces/status.json?uids=all@"
				+ FaceLogin.NAMESPACE, new Callback() {

			@Override
			public void callback(String response) {
				Window.alert(response);
			}
		});
	}

	private static final native elemental.html.FormData createFormData() /*-{
		return new FormData();
	}-*/;

	private static final native void append(elemental.html.FormData formData,
			String name, String value) /*-{
		formData.append(name, value);
	}-*/;

	private static final native void append(elemental.html.FormData formData,
			String name, JavaScriptObject value) /*-{
		formData.append(name, value);
	}-*/;

	public static void recognize(JavaScriptObject imageBlob,
			final Callback callback) {
		final elemental.xml.XMLHttpRequest request = Browser.getWindow()
				.newXMLHttpRequest();
		String url = URL.encode("http://api.face.com/faces/recognize.json");
		elemental.html.FormData formData = createFormData();
		append(formData, "api_key", API_KEY);
		append(formData, "api_secret", API_SECRET);
		append(formData, "attributes", "age_est,gender,glasses,smiling,mood");
		append(formData, "uids", "all@" + FaceLogin.NAMESPACE);
		append(formData, "filename", "temp.jpg");
		append(formData, "file", imageBlob);
		request.open("POST", url);
		request.setOnloadend(new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				callback.callback(request.getResponseText());
			}
		});
		request.send(formData);
	}

	public static void sendDetectRequest(JavaScriptObject imageBlob,
			final Callback callback) {
		final elemental.xml.XMLHttpRequest request = Browser.getWindow()
				.newXMLHttpRequest();
		String url = URL.encode("http://api.face.com/faces/detect.json");
		FormData formData = createFormData();
		append(formData, "api_key", API_KEY);
		append(formData, "api_secret", API_SECRET);
		append(formData, "attributes", "age_est,gender,glasses,smiling,mood");
		append(formData, "filename", "temp.jpg");
		append(formData, "file", imageBlob);
		request.open("POST", url);
		request.setOnloadend(new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				callback.callback(request.getResponseText());
			}
		});
		request.send(formData);
	}
}
