package io.hustler.wallzy.networkhandller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Formatter;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.R;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.wallzy.request.ReqEmailLogin;
import io.hustler.wallzy.model.wallzy.request.ReqEmailSignup;
import io.hustler.wallzy.model.wallzy.request.ReqGoogleSignup;
import io.hustler.wallzy.utils.MessageUtils;

public class RestUtilities {
    private String TAG = this.getClass().getSimpleName();
    private final String ROOT_IP = "http://192.168.1.9:8080/private";
    private final String AUTH = ROOT_IP + "/onBoard/v0/";
    private final String EMAIL_LOGIN = AUTH + "signUpUser";
    private final String GMAIL_LOGIN = AUTH + "googleAuth";
    private final String EMAIL_SIGNUP = AUTH + "signUpUser";
    private final String PRIVATE_API_KEY = "nUACXVe0hJCl6ozDToEOur6lxPw=";
    private final String PUBLIC_API_KEY = "C7OjVdkwf9vZKg4FktMTWuF+WRo=";
    private final String IMAGEKIT_UPLOAD_IMAGE = "https://api.imagekit.io/v1/files/upload";


    /**
     * CALLBACK INTERFACE
     */
    public interface OnSuccessListener {
        void onSuccess(Object onSuccessResponse);

        void onError(String error);
    }

    /**
     * Image kit apis
     */
    public BaseResponse uploadImageToIK(String filepath, String fileName, Activity context, OnSuccessListener onSuccessListener) {
        String token = UUID.randomUUID().toString();
        long timesecs = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant instant = Instant.now();
            timesecs = instant.getEpochSecond() + 60 * 60;
        }

        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, IMAGEKIT_UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess(response);
                    MessageUtils.showShortToast(context, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSuccessListener.onError(error.getMessage());
                MessageUtils.showShortToast(context, error.getMessage());
            }
        });
        simpleMultiPartRequest.addFile("file", filepath);
        Log.i(TAG, "uploadImageToIK: path " + filepath);
        simpleMultiPartRequest.addStringParam("publicKey", PUBLIC_API_KEY);
        Log.i(TAG, "uploadImageToIK: PK " + PUBLIC_API_KEY);
        simpleMultiPartRequest.addStringParam("expire", String.valueOf(timesecs));
        Log.i(TAG, "uploadImageToIK: expire " + timesecs);
        simpleMultiPartRequest.addStringParam("token", token);
        Log.i(TAG, "uploadImageToIK: TOKEN " + token);
        simpleMultiPartRequest.addStringParam("fileName", fileName);
        Log.i(TAG, "uploadImageToIK: filenName " + fileName);
        try {
            String signature = encode(PRIVATE_API_KEY, token + timesecs);
            simpleMultiPartRequest.addStringParam("signature", signature);
            Log.i(TAG, "uploadImageToIK: SIGNATURE " + signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
        return null;
    }

    /**
     * AUTH API CALLS
     */

    public void googleSignup(Context context, ReqGoogleSignup reqGoogleSignup, OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqGoogleSignup), GMAIL_LOGIN);
    }

    public void EmailSignup(Context context, ReqEmailSignup reqEmailSignuo, OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqEmailSignuo), EMAIL_SIGNUP);
    }

    public void EmailLogin(Context context, ReqEmailLogin reqEmailLogin, OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqEmailLogin), EMAIL_LOGIN);
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
                        (VolleyError error) -> {
                            onSuccessListener.onError(getRelevantVolleyErrorMessage(context, error));
                        });

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

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String encode(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));

//        return new String(Hex.encodeHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8))));

    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }
}
