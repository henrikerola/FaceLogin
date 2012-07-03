package com.github.henrikerola.facelogin.client.admin;

import java.util.ArrayList;
import java.util.List;

import com.github.henrikerola.facelogin.client.RequestUtil;
import com.github.henrikerola.facelogin.client.RequestUtil.Callback;
import com.github.henrikerola.facelogin.client.json.GetTagsResponse;
import com.github.henrikerola.facelogin.client.json.UserResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class UserPanel extends Composite {

	HorizontalPanel panel = new HorizontalPanel();

	private Button addPhotoButton;
	private Button setPhotoButton;

	private String tagId;

	private final UserResponse user;

	private final AdminView adminView;

	public UserPanel(final AdminView adminView, final UserResponse user) {
		this.adminView = adminView;
		this.user = user;
		initWidget(panel);
		panel.add(new Label(user.getName()));
		addPhotoButton = new Button("Add to recognition photos",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						addPhoto();
						adminView.setPhotoTag(null);
					}
				});
		addPhotoButton.setVisible(false);
		panel.add(addPhotoButton);

		setPhotoButton = new Button("Set as a recognition photo",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						setPhoto();
						adminView.setPhotoTag(null);
					}
				});
		setPhotoButton.setVisible(false);
		panel.add(setPhotoButton);
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
		setPhotoButton.setVisible(tagId != null);
		addPhotoButton.setVisible(tagId != null);
	}

	private void addPhoto() {
		final String userId = user.getUserId();
		List<String> tIds = new ArrayList<String>();
		tIds.add(tagId);
		RequestUtil.save(userId, tIds, new Callback() {
			@Override
			public void callback(String response) {
				Window.alert(response);
				RequestUtil.train(userId);
			}
		});
	}

	private void setPhoto() {
		final String userId = user.getUserId();
		RequestUtil.getTags(userId, new Callback() {
			@Override
			public void callback(String response) {
				List<String> tags = GetTagsResponse.getTagsResponse(response)
						.getTags();
				RequestUtil.removeTags(tags, new Callback() {
					@Override
					public void callback(String response) {
						addPhoto();
						RequestUtil.train(userId);
					}
				});
			}
		});
	}
}
