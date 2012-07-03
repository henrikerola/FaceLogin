package com.github.henrikerola.facelogin.client.json;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class BaseResponse extends JavaScriptObject {

    protected BaseResponse() { }

    public static final native <T extends BaseResponse> T getResponse(String responseString) /*-{
        return eval('(' + responseString + ')');
    }-*/;
}