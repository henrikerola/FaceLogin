package com.github.henrikerola.facelogin.client;

import com.github.henrikerola.facelogin.client.admin.AdminView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

public class FaceLogin implements EntryPoint {

	public static final String NAMESPACE = "vaadinfacelogin";

	public void onModuleLoad() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				openView(event.getValue());
			}
		});

		openView(History.getToken());
	}

	private void openView(String token) {
		RootPanel.get().clear();
		if ("admin".equals(token)) {
			RootPanel.get().add(new AdminView());
		} else {
			RootPanel.get().add(new LoginView());
		}

	}
}
