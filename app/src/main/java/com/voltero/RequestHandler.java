package com.voltero;

import org.json.JSONException;

public interface RequestHandler
{
    void processResponse(String response) throws JSONException;
}
