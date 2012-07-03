package com.github.henrikerola.facelogin.client.json;

import com.google.gwt.core.client.JsArray;

public class UserResponse extends BaseResponse {

	protected UserResponse() {
	}
	
	public final String getName() {
		return getUserId().substring(0, getUserId().indexOf("@"));
	}

	public final native String getUserId() /*-{
		return this;
	}-*/;

	public static final native JsArray<UserResponse> getUsersList(String json, String namespace) /*-{
		var stuff = eval('(' + json + ')');
		return stuff.users[namespace];
	}-*/;
}