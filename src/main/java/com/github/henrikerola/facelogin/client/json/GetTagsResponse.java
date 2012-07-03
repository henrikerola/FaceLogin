package com.github.henrikerola.facelogin.client.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;

public class GetTagsResponse extends BaseResponse {

	protected GetTagsResponse() {
	}
	
	public final List<String> getTags() {
		List<String> tags = new ArrayList<String>();
		JsArray jsArray = getTagsArray();
		for (int i = 0; i < jsArray.length(); i++) {
			tags.add("" + jsArray.get(i));
		}
		return tags;
	}
	

	private final native JsArray getTagsArray() /*-{
		var res = [];
		for ( var i = 0; i < this.photos.length; i++) {
			for ( var j = 0; j < this.photos[i].tags.length; j++) {
				res.push(this.photos[i].tags[j].tid)
			}
		}
		return res;
	}-*/;

	public static final native GetTagsResponse getTagsResponse(String json) /*-{
		var stuff = eval('(' + json + ')');
		console.log(stuff);
		return stuff;
	}-*/;
}