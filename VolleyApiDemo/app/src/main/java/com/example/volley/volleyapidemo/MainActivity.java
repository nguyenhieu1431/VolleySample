package com.example.volley.volleyapidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String API_SECRET_KEY = "api_secret_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        login("trojan@gmail.com", "123456");
    }


    private void login(final String userName, final String pass) {
        String url = "http://vieclamtaytrai.vn/mb.login";
        final String password_md5 = MD5.encryptMD5(pass);
        String secret_key = "VLTTAPI&" + userName + "&" + password_md5;
        final String api_secret_key = MD5.encryptMD5(secret_key);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Response==> " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, userName);
                params.put(KEY_PASSWORD, password_md5);
                params.put(API_SECRET_KEY, api_secret_key);

                return params;
            }

            @Override
            public byte[] getBody() {
                StringBuilder encodedParams = new StringBuilder();

                try {
                    for (Map.Entry<String, String> entry : getParams().entrySet()) {
                        encodedParams.append(entry.getKey());
                        encodedParams.append('=');
                        encodedParams.append(entry.getValue());
                        encodedParams.append('&');
                    }
                    return encodedParams.toString().getBytes(getParamsEncoding());
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        DemoApplication.getInstance().addToRequestQueue(jsonObjReq, "login");
    }
}
