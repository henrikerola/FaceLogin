package com.github.henrikerola.facelogin.client.admin;

import java.util.Iterator;

import com.github.henrikerola.facelogin.client.FaceLogin;
import com.github.henrikerola.facelogin.client.RequestUtil;
import com.github.henrikerola.facelogin.client.RequestUtil.Callback;
import com.github.henrikerola.facelogin.client.json.UserResponse;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminView extends Composite {

	private VerticalPanel panel = new VerticalPanel();

	private Button cancelTagToUserButton = new Button("Cancel",
			new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					setPhotoTag(null);
				}
			});

	private String tag;

	private TakePhoto takePhoto;

	private NewUserPanel newUserPanel;

	public AdminView() {
		initWidget(panel);

		takePhoto = new TakePhoto(this);
		panel.add(takePhoto);

		panel.add(new Button("Status", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RequestUtil.getStatus();
			}
		}));

		refreshUsers();
	}
	
	public void refreshUsers() {
		Iterator<Widget> iter = panel.iterator();
		while (iter.hasNext()) {
			Widget widget = (Widget) iter.next();
			if (widget instanceof UserPanel) {
				iter.remove();
			}
		}
		
		RequestUtil.getUsers(new Callback() {
			@Override
			public void callback(String response) {
				JsArray<UserResponse> users = UserResponse.getUsersList(
						response, FaceLogin.NAMESPACE);
				for (int i = 0; i < users.length(); i++) {
					panel.add(new UserPanel(AdminView.this, users.get(i)));
				}
			}
		});
	}

	public void setPhotoTag(String tag) {
		this.tag = tag;
		updateUserPanels();
		if (tag != null) {
			newUserPanel = new NewUserPanel(this, tag);
			panel.add(newUserPanel);
			panel.add(cancelTagToUserButton);
		} else {
			if (newUserPanel != null) {
				panel.remove(newUserPanel);
				newUserPanel = null;
			}
			panel.remove(cancelTagToUserButton);
			takePhoto.setVideoMode(true);
		}
	}

	private void updateUserPanels() {
		Iterator<Widget> iter = panel.iterator();
		while (iter.hasNext()) {
			Widget widget = (Widget) iter.next();
			if (widget instanceof UserPanel) {
				UserPanel userPanel = (UserPanel) widget;
				userPanel.setTagId(tag);
			}
		}
	}

}
