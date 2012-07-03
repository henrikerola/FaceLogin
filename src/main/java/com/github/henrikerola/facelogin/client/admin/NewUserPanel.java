package com.github.henrikerola.facelogin.client.admin;

import java.util.ArrayList;
import java.util.List;

import com.github.henrikerola.facelogin.client.FaceLogin;
import com.github.henrikerola.facelogin.client.RequestUtil;
import com.github.henrikerola.facelogin.client.RequestUtil.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class NewUserPanel extends Composite {

	HorizontalPanel panel = new HorizontalPanel();

	private Button actioButton;

	private TextBox textBox;

	private final String tagId;

	private final AdminView adminView;

	public NewUserPanel(AdminView adminView, String tag) {
		this.adminView = adminView;
		this.tagId = tag;
		initWidget(panel);
		textBox = new TextBox();
		panel.add(textBox);
		actioButton = new Button("Create new user", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createUser("" + textBox.getValue());
			}
		});
		panel.add(actioButton);
	}
	
	private void createUser(final String userId) {
		List<String> tIds = new ArrayList<String>();
		tIds.add(tagId);
		RequestUtil.save(userId + "@" + FaceLogin.NAMESPACE, tIds, new Callback() {
			@Override
			public void callback(String response) {
				Window.alert(response);
				RequestUtil.train(userId + "@" + FaceLogin.NAMESPACE);
				adminView.setPhotoTag(null);
				adminView.refreshUsers();
			}
		});
	}
}
