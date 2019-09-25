package io.hustler.wallzy.networkhandller;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Formatter;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.hustler.wallzy.R;
import io.hustler.wallzy.model.wallzy.request.ReqAddCategory;
import io.hustler.wallzy.model.wallzy.request.ReqAddCollection;
import io.hustler.wallzy.model.wallzy.request.ReqEmailLogin;
import io.hustler.wallzy.model.wallzy.request.ReqEmailSignup;
import io.hustler.wallzy.model.wallzy.request.ReqGetCollectionorCategoryImages;
import io.hustler.wallzy.model.wallzy.request.ReqGoogleSignup;
import io.hustler.wallzy.model.wallzy.request.ReqUpdateFcmToken;
import io.hustler.wallzy.model.wallzy.request.ReqUploadImages;
import io.hustler.wallzy.model.wallzy.request.ReqUserImage;

public class RestUtilities {
    private String TAG = this.getClass().getSimpleName();
    private final String ROOT_IP = "http://192.168.1.4:8080/private";

    /**
     * AUTH API PATHS
     */
    private final String AUTH = ROOT_IP + "/onBoard/v0/";
    private final String EMAIL_LOGIN = AUTH + "signUpUser";
    private final String GMAIL_LOGIN = AUTH + "googleAuth";
    private final String EMAIL_SIGNUP = AUTH + "signUpUser";
    private final String GUEST_LOGIN = AUTH + "guestLoginUser";
    private final String UPDATE_FCM_TOKEN = AUTH + "updateFcmToken";

    /**
     * CATEGORY API PATHS
     */
    private final String CATGORY = ROOT_IP + "/category";
    private final String ADD_CATEGORY = CATGORY + "/addCategory";
    private final String GET_CATEGORY = CATGORY + "/getCategories";
    private final String GET_CATEGORY_IMAGES = CATGORY + "/getCategoryImages";
    private final String GET_CAT_COUNT = CATGORY + "/getCatCount";
    private final String GET_LATEST_CATS = CATGORY + "/getLatestCats";


    /**
     * COLLECTION API PATHS
     */
    private final String COLLECTION = ROOT_IP + "/Collections";
    private final String ADD_COLLECTION = COLLECTION + "/addCollection";
    private final String GET_COLLECTION = COLLECTION + "/getCollections";
    private final String GET_COLLECTION_IMAGES = COLLECTION + "/getCollectionImages";

    /**
     * IMAGE APIS
     */
    private final String IMAGEPATH = ROOT_IP + "/image";
    private final String UPLOAD_IMAGE = IMAGEPATH + "/uploadImages";
    private final String GET_IMAGES = IMAGEPATH + "/getAllImages";

    /**
     * USER APIS
     */
    private final String USERPATH = ROOT_IP + "/User/v0";
    private final String LIKE_IMAGES = USERPATH + "/likeImage";
    private final String DOWNLOAD_IMAGES = USERPATH + "/downloadImage";
    private final String WALL_IMAGES = USERPATH + "/setWall";
    private final String REPORT_IMAGES = USERPATH + "/reportImage";
    private final String GET_USER_FAVS = USERPATH + "/getUserFavs";
    private final String IS_LIKED = USERPATH + "/isLiked";

    /**
     * IMAGE KIT APIS
     */
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
    public void uploadImageToIK(String filepath, String fileName, Context context, OnSuccessListener onSuccessListener) {
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
                    Log.i(TAG, "Error" + response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSuccessListener.onError(error.getMessage());
                Log.i(TAG, "Error" + error.getMessage());

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

    public void EmailLogin(Context context, ReqEmailLogin reqEmailLogin,
                           OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqEmailLogin), EMAIL_LOGIN);
    }

    public void guestLogin(Context context, ReqEmailLogin reqEmailLogin,
                           OnSuccessListener onSuccessListener) {
        postJsonObjectApi(context, onSuccessListener, getJSONObject(reqEmailLogin), GUEST_LOGIN);
    }

    public void update_fcm_token(Context context, ReqUpdateFcmToken reqUpdateFcmToken, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUpdateFcmToken), UPDATE_FCM_TOKEN);
    }

    /**
     * COLLECTION METHODS
     */

    public void addCollection(Context context, ReqAddCollection reqAddCollection, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqAddCollection), ADD_COLLECTION);
    }

    public void getCollection(Context context, OnSuccessListener onSuccessListener) {
        getJsonObjectWithAuthHeaderApi(context, onSuccessListener, GET_COLLECTION);
    }

    public void getCollectionImsges(Context context, ReqGetCollectionorCategoryImages reqGetCategoryImages, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqGetCategoryImages), GET_COLLECTION_IMAGES);
    }


    /**
     * CATEGORY METHODS
     */

    public void addCategory(Context context, ReqAddCategory reqAddCategory, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqAddCategory), ADD_CATEGORY);
    }

    public void getCategory(Context context, OnSuccessListener onSuccessListener) {
        getJsonObjectApi(context, onSuccessListener, GET_CATEGORY);
    }

    public void getCategoryImages(Context context, ReqGetCollectionorCategoryImages reqGetCategoryImages, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqGetCategoryImages), GET_CATEGORY_IMAGES);
    }

    public void getCategoryCount(Context context, OnSuccessListener onSuccessListener) {
        getJsonObjectWithAuthHeaderApi(context, onSuccessListener, GET_CAT_COUNT);
    }

    public void getLatestCats(Context context, OnSuccessListener onSuccessListener, long id) {
        id += 1;
        getJsonObjectWithAuthHeaderApi(context, onSuccessListener, GET_LATEST_CATS + "/" + id);
    }

    /**
     * IMAGE METHODS
     */

    public void uploadImages(Context context, ReqUploadImages reqUploadImages, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUploadImages), UPLOAD_IMAGE);
    }

    public void getAllImages(Context context, long pageId, OnSuccessListener onSuccessListener) {
        getJsonObjectWithAuthHeaderApi(context, onSuccessListener, GET_IMAGES + "/" + pageId);
    }

    /**
     * USER APIS
     */

    public void getUserFavs(Context context, ReqUserImage reqUserImage, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUserImage), GET_USER_FAVS);
    }

    public void reportImage(Context context, ReqUserImage reqUserImage, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUserImage), REPORT_IMAGES);
    }

    public void likeImage(Context context, ReqUserImage reqUserImage, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUserImage), LIKE_IMAGES);
    }

    public void downloadImage(Context context, ReqUserImage reqUserImage, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUserImage), DOWNLOAD_IMAGES);
    }

    public void setWall(Context context, ReqUserImage reqUserImage, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUserImage), WALL_IMAGES);
    }

    public void isImageLiked(Context context, ReqUserImage reqUserImage, OnSuccessListener onSuccessListener) {
        postJsonObjectWithAuthHeaderApi(context, onSuccessListener, getJSONObject(reqUserImage), IS_LIKED);

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

    private void getJsonObjectApi(Context context,
                                  OnSuccessListener onSuccessListener,
                                  String url) {
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

    private void getJsonObjectWithAuthHeaderApi(Context context,
                                                OnSuccessListener onSuccessListener,
                                                String url) {

        Log.e(TAG, "getJsonObjectWithAuthHeaderApi: " + url);
        JsonObjectRequest jsonObjectRequest
                =
                new JsonObjectRequestwithAuthHeader(Request.Method.GET,
                        url,
                        null,
                        response -> {
                            logTheResponse(response, "API RESPONSE --> ");
                            if (onSuccessListener != null) {
                                onSuccessListener.onSuccess(response);
                            }
                        },
                        error ->
                                onSuccessListener.onError(getRelevantVolleyErrorMessage(context, error)), context);

        jsonObjectRequest.setShouldCache(false);
        MySingleton.addJsonObjRequest(context, jsonObjectRequest);
    }


    private JSONObject getJSONObject(Object checkDriverRequest) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(checkDriverRequest));
            logTheResponse(jsonObject, "API REQUEST DATA --> ");
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void logTheResponse(JSONObject response, String API_NAME) {
        Log.i(API_NAME + " --> ", response.toString());
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
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }
}
