package com.github.henrikerola.facelogin.client.json;

public class RecognizeResponse extends BaseResponse {

	protected RecognizeResponse() {
	}

	public final String getName() {
		return getUid().substring(0, getUid().indexOf("@"));
	}

	public final native String getUid() /*-{
		return this.photos[0].tags[0].uids[0].uid;
	}-*/;

	public final native int getAgeEst() /*-{
		return this.photos[0].tags[0].attributes.age_est.value;
	}-*/;

	public final native String getGender() /*-{
		return this.photos[0].tags[0].attributes.gender.value;
	}-*/;
	
	public final native boolean getGlasses() /*-{
		return this.photos[0].tags[0].attributes.glasses.value;
	}-*/;
	
	public final native boolean getSmiling() /*-{
		return this.photos[0].tags[0].attributes.smiling.value;
	}-*/;

	public final native String getMood() /*-{
		return this.photos[0].tags[0].attributes.mood.value;
	}-*/;

	public static final native RecognizeResponse getUser(String json) /*-{
		var stuff = eval('(' + json + ')');
		console.log(stuff);

		if (stuff.photos.length == 1 && stuff.photos[0].tags
				&& stuff.photos[0].tags.length == 1
				&& stuff.photos[0].tags[0].uids
				&& stuff.photos[0].tags[0].uids.length == 1) {
			return stuff;
		}

		return null;
	}-*/;
}