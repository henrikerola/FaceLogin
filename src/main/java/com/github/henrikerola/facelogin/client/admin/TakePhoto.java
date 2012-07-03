package com.github.henrikerola.facelogin.client.admin;

import com.github.henrikerola.facelogin.client.RequestUtil;
import com.github.henrikerola.facelogin.client.RequestUtil.Callback;
import com.github.henrikerola.facelogin.client.Util;
import com.github.henrikerola.facelogin.client.json.GetTagsResponse;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.media.client.Video;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TakePhoto extends Composite {
	
	private Panel panel = new VerticalPanel();
	
	private int cnt = 3;
	private Label countLabel = new Label();

	private final AdminView adminView;
	
	public TakePhoto(final AdminView adminView) {
		this.adminView = adminView;
		initWidget(panel);
		
		panel.add(new Label("Click the video to take a photo"));
		
		countLabel.setVisible(false);
		panel.add(countLabel);
		
		
		video = Video.createIfSupported();
		video.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				startCounting();
			}
		});
		video.setAutoplay(true);
		panel.add(video);
		Util.bindVideoToUserMedia(video);

		canvas = Canvas.createIfSupported();
		canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				adminView.setPhotoTag(null);
			}
		});
		canvas.setVisible(false);
		panel.add(canvas);
	}
	
	private Timer countTimer = new Timer() {
		@Override
		public void run() {
			cnt--;
			if (cnt == 0) {
				countTimer.cancel();
				countLabel.setVisible(false);
				takePhoto();
			}
			countLabel.setText("" + cnt);
		}
	};

	private Video video;

	private Canvas canvas;
	
	private void startCounting() {
		cnt = 3;
		countLabel.setText("" + cnt);
		countLabel.setVisible(true);
		countTimer.scheduleRepeating(1000);
	}
	
	private void takePhoto() {
		RequestUtil.sendDetectRequest(Util.photo(video, canvas), new Callback() {
			@Override
			public void callback(String response) {
				GetTagsResponse res = GetTagsResponse.getTagsResponse(response);
				if (res.getTags().size() == 1) {
					adminView.setPhotoTag(res.getTags().get(0));
				} else {
					Window.alert("Error on takePhoto");
				}
				
			}
		});
		setVideoMode(false);
	}
	
	public void setVideoMode(boolean enabled) {
		canvas.setVisible(!enabled);
		video.setVisible(enabled);
	}

}
