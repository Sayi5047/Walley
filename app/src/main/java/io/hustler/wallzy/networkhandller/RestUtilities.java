package io.hustler.wallzy.networkhandller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.R;
import io.hustler.wallzy.model.wallzy.request.ReqEmailLogin;
import io.hustler.wallzy.model.wallzy.request.ReqEmailSignup;
import io.hustler.wallzy.model.wallzy.request.ReqGoogleSignup;

public class RestUtilities {
    private final String ROOT_IP = "http://192.168.1.9:8080/private";
    private final String AUTH = ROOT_IP + "/onBoard/v0/";
    private final String EMAIL_LOGIN = AUTH + "signUpUser";
    private final String GMAIL_LOGIN = AUTH + "googleAuth";
    private final String EMAIL_SIGNUP = AUTH + "signUpUser";


    /**
     * CALLBACK INTERFACE
     */
    public interface OnSuccessListener {
        void onSuccess(Object onSuccessResponse);

        void onError(String error);
    }

    /**
     * AUTH API CALLS
     */

    public void googleSignup(Context context, ReqGoogleSignup reqGoogleSignup, OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqGoogleSignup), GMAIL_LOGIN);
    }

    public void EmailSignup(Context context, ReqEmailSignup reqEmailSignuo, OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqEmailSignuo),EMAIL_SIGNUP);
    }

    public void EmailLogin(Context context, ReqEmailLogin reqEmailLogin, OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqEmailLogin),EMAIL_LOGIN);
    }


    /**
     * CALLER METHODS
     */
    private void postJsonObjectApi(Context context, OnSuccessListener onSuccessListener, JSONObject jsonObject, String url) {
        logTheResponse(jsonObject, "API REQUEST --> ");
        JsonObjectRequest jsonObjectRequest
                =
                new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        response -> {
                            logTheResponse(response, "API RESPONSE --> ");
                            if (onSuccessListener != null) {
                                onSuccessListener.onSuccess(response);
                            }
                        },
                        error -> onSuccessListener.onError(getRelevantVolleyErrorMessage(context, error)));

        MySingleton.addJsonObjRequest(context, jsonObjectRequest);
    }

    private void getJsonObjectApi(Context context, OnSuccessListener onSuccessListener, String url) {
        JsonObjectRequest jsonObjectRequest
                =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            logTheResponse(response, "API RESPONSE --> ");
                            if (onSuccessListener != null) {
                                onSuccessListener.onSuccess(response);
                            }
                        },
                        error -> onSuccessListener.onError(getRelevantVolleyErrorMessage(context, error)));

        MySingleton.addJsonObjRequest(context, jsonObjectRequest);
    }

    private void postJsonObjectWithAuthHeaderApi(Context context, OnSuccessListener onSuccessListener, JSONObject jsonObject, String url) {
        JsonObjectRequest jsonObjectRequest
                =
                new JsonObjectRequestwithAuthHeader(Request.Method.POST, url, jsonObject,
                        response -> {
                            logTheResponse(response, "API RESPONSE --> ");
                            if (onSuccessListener != null) {
                                onSuccessListener.onSuccess(response);
                            }
                        },
                        error -> onSuccessListener.onError(getRelevantVolleyErrorMessage(context, error)), context);

        MySingleton.addJsonObjRequest(context, jsonObjectRequest);
    }

    private void getJsonObjectWithAuthHeaderApi(Context context, OnSuccessListener onSuccessListener, String url) {
        JsonObjectRequest jsonObjectRequest
                =
                new JsonObjectRequestwithAuthHeader(Request.Method.GET, url, null,
                        response -> {
                            logTheResponse(response, "API RESPONSE --> ");
                            if (onSuccessListener != null) {
                                onSuccessListener.onSuccess(response);
                            }
                        },
                        error -> onSuccessListener.onError(getRelevantVolleyErrorMessage(context, error)), context);

        MySingleton.addJsonObjRequest(context, jsonObjectRequest);
    }


    private JSONObject getJSONObject(Object checkDriverRequest) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(checkDriverRequest));
            logTheResponse(jsonObject, "REQUEST DATA --> ");
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void logTheResponse(JSONObject response, String API_NAME) {
        if (BuildConfig.DEBUG) {
            Log.i(API_NAME + " --> ", response.toString());
        }
    }

    @Nullable
    private String getRelevantVolleyErrorMessage(@NonNull Context context, @NonNull VolleyError volleyError) {
        try {
            volleyError.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        try {
            if (volleyError instanceof NoConnectionError) {
                return context.getString(R.string.NO_CONNECTION_ERROR);
            } else if (volleyError instanceof TimeoutError) {
                return context.getString(R.string.TIMEOUT_ERROR);
            } else if (volleyError instanceof AuthFailureError) {
                return context.getString(R.string.AUTH_FAILURE_ERROR);
            } else if (volleyError instanceof ServerError) {
                return context.getString(R.string.SERVER_ERROR);
            } else if (volleyError instanceof NetworkError) {
                return context.getString(R.string.NETWORK_ERROR);
            } else if (volleyError instanceof ParseError) {
                return context.getString(R.string.PARSE_ERROR);
            }
            return null;
        } catch (Exception e) {
//            FirebaseCrash.log(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
