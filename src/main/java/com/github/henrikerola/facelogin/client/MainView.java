package com.github.henrikerola.facelogin.client;

import com.github.henrikerola.facelogin.client.json.RecognizeResponse;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class MainView extends Composite {

	public MainView(RecognizeResponse user) {
		initWidget(new HTML("<h1>Welcome " + user.getName() + "!</h1>" + Util.getAttributesString(user)));
	}

}
