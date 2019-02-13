package petrochina.cqzt.mc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import petrochina.cqzt.mc.IMyAidl;

/**
 * Created by zzh on 2018/7/27.
 */

public class McRequest {
    public String url, msgToken;
    public Context context;
    public Map<String, String> params = new HashMap<>();
    public IMyAidl ri;
    public McRequest mc;

    public McRequest(IMyAidl ri) {
        params.put("a", "b");
        this.ri = ri;
    }

    public McRequest url(String url) {
        this.url = url;
        return this;
    }

    public McRequest tag(Context context) {
        this.context = context;
        return this;
    }

    public McRequest param(String key, String value) {
        params.put(key, value);
        return this;
    }

    public JSONObject setParams() {
        msgToken = McUtils.getCharAndNumr(30);
        SharedPreferences preferences = context.getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        String sessionid = preferences.getString("sessionid", "");
        JSONObject dfmsg = new JSONObject();
        try {
            dfmsg.put("api", "1");
            dfmsg.put("clientid", "1");
            dfmsg.put("clientlabel", "1");
            dfmsg.put("clientname", "1");
            dfmsg.put("user", "1");
            dfmsg.put("token", "1");
            dfmsg.put("from", "1");
            dfmsg.put("version", "1.0.1.1");
            dfmsg.put("msgToken", msgToken);
            dfmsg.put("sessionid", sessionid

            );
            dfmsg.put("requestMethod", "post");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dfmsg;
    }

}
