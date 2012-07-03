package com.github.henrikerola.facelogin.client;

import com.github.henrikerola.facelogin.client.json.RecognizeResponse;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.media.client.Video;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

import elemental.client.Browser;
import elemental.dom.LocalMediaStream;
import elemental.html.NavigatorUserMediaError;
import elemental.html.NavigatorUserMediaErrorCallback;
import elemental.html.NavigatorUserMediaSuccessCallback;
import elemental.js.util.JsMappable;
import elemental.util.Mappable;

public abstract class Util {

	public static void bindVideoToUserMedia(final Video video) {

		final Mappable map = (Mappable) JsMappable.createObject();
		map.setAt("video", true);

		Browser.getWindow()
				.getNavigator()
				.webkitGetUserMedia(map,
						new NavigatorUserMediaSuccessCallback() {
							public boolean onNavigatorUserMediaSuccessCallback(
									LocalMediaStream stream) {
								// video.setSrc(stream);
								setVideoSrc(video.getElement(), stream);
								return true;
							}
						}, new NavigatorUserMediaErrorCallback() {
							@Override
							public boolean onNavigatorUserMediaErrorCallback(
									NavigatorUserMediaError error) {
								Window.alert("fail");
								return false;
							}
						});
	}

	private static native void setVideoSrc(Element videoElement,
			LocalMediaStream stream) /*-{
		videoElement.src = window.webkitURL.createObjectURL(stream);
	}-*/;

	public static JavaScriptObject photo(Video video, Canvas canvas) {
		return photo(video.getElement(), canvas.getElement());
	}

	private native static JavaScriptObject photo(Element video, Element canvas) /*-{

		//credit http://stackoverflow.com/a/8782422/52160
		function dataURItoBlob(dataURI, callback) {
			// convert base64 to raw binary data held in a string
			// doesn't handle URLEncoded DataURIs
			var byteString;
			if (dataURI.split(",")[0].indexOf("base64") >= 0) {
				byteString = atob(dataURI.split(",")[1]);
			} else {
				byteString = unescape(dataURI.split(",")[1]);
			}
			// separate out the mime component
			var mimeString = dataURI.split(",")[0].split(":")[1].split(";")[0];
			// write the bytes of the string to an ArrayBuffer
			var ab = new ArrayBuffer(byteString.length);
			var ia = new Uint8Array(ab);
			for ( var i = 0; i < byteString.length; i++) {
				ia[i] = byteString.charCodeAt(i);
			}
			// write the ArrayBuffer to a blob, and you're done
			var BlobBuilder = $wnd.WebKitBlobBuilder || $wnd.MozBlobBuilder;
			var bb = new BlobBuilder();
			bb.append(ab);

			var blobb = bb.getBlob(mimeString);
			console.log(blobb);
			console.log(blobb.toString());
			return blobb;
		}

		canvas.width = video.videoWidth;
		canvas.height = video.videoHeight;
		var ctx = canvas.getContext('2d');
		ctx.drawImage(video, 0, 0);

		return dataURItoBlob(canvas.toDataURL('image/jpeg'))
	}-*/;
	
	public static String getAttributesString(RecognizeResponse user) {
		StringBuilder b = new StringBuilder();
		b.append("Age: ").append(user.getAgeEst()).append("</br>");
		b.append("Gender: ").append(user.getGender()).append("</br>");
		b.append("Glasses: ").append(user.getGlasses()).append("</br>");
		b.append("Smiling: ").append(user.getSmiling()).append("</br>");
		b.append("Mood: ").append(user.getMood()).append("</br>");
		return b.toString();
	}

}
