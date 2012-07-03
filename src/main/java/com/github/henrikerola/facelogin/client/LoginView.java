package com.github.henrikerola.facelogin.client;

import com.github.henrikerola.facelogin.client.RequestUtil.Callback;
import com.github.henrikerola.facelogin.client.json.RecognizeResponse;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ProgressEvent;
import com.google.gwt.event.dom.client.ProgressHandler;
import com.google.gwt.media.client.Video;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginView extends Composite {

	private Video video;
	private Canvas canvas;

	private Button loginButton;

	public LoginView() {
		Panel panel = new VerticalPanel();
		initWidget(panel);

		video = Video.createIfSupported();
		video.addProgressHandler(new ProgressHandler() {
			@Override
			public void onProgress(ProgressEvent event) {
				scheduleTakePhoto();
			}
		});
		video.setAutoplay(true);
		panel.add(video);

		canvas = Canvas.createIfSupported();
		canvas.setVisible(false);
		panel.add(canvas);

		Util.bindVideoToUserMedia(video);

		loginButton = new Button("Login", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scheduleTakePhoto();
			}
		});
		//loginButton.setVisible(false);
		panel.add(loginButton);
	}

	private int photoCnt = 0;
	private Timer timer = new Timer() {
		@Override
		public void run() {
			takePhoto();
		}
	};

	private void scheduleTakePhoto() {
		timer.schedule(1500);
	}

	private void takePhoto() {
		photoCnt++;
		JavaScriptObject photo = Util.photo(video, canvas);
		RequestUtil.recognize(photo, new Callback() {
			@Override
			public void callback(String response) {
				RecognizeResponse r = RecognizeResponse.getUser(response);
				video.setVisible(false);
				canvas.setVisible(true);
				new Timer() {
					@Override
					public void run() {
						video.setVisible(true);
						canvas.setVisible(false);
					}
				}.schedule(500);
				if (r != null) {
					RootPanel.get().clear();
					RootPanel.get().add(new MainView(r));
					photoCnt = 0;
				} else {
					if (photoCnt < 3) {
						takePhoto();
					} else {
						Window.alert("Login failed, please try again!");
						//loginButton.setVisible(true);
						photoCnt = 0;
					}
				}
			}
		});
	}

	public final native void log(String msg) /*-{
		console.log(msg);
	}-*/;

}
