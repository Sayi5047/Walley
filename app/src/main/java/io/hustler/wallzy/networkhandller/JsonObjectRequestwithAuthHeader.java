package io.hustler.wallzy.networkhandller;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.utils.SharedPrefsUtils;

/**
 * Created by Sayi on 07-10-2017.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class JsonObjectRequestwithAuthHeader extends JsonObjectRequest {
    Context mContext;

    public JsonObjectRequestwithAuthHeader(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errorListener, Context mContext) {
        super(method, url, jsonRequest, listener, errorListener);
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("content-type", "application/json");
        hashMap.put("accept-version", "v1");
        hashMap.put("Authorization", new SharedPrefsUtils(mContext).getString(Constants.SHARED_PREFS_SYSTEM_AUTH_KEY));
        return hashMap;
    }
}
