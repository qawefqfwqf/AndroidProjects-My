package petrochina.cqzt.mc.utils;

import android.os.RemoteException;

import org.json.JSONObject;

import java.util.Map;

import petrochina.cqzt.mc.IMyAidl;

/**
 * Created by zzh on 2018/7/27.
 */

public class UserRequest extends McRequest {
    public UserRequest(IMyAidl ri) {
        super(ri);
    }

    public UserRequest url(String url) {
        this.url = url;
        return this;
    }

    public UserRequest param(String key, String value) {
        params.put(key, value);
        return this;
    }

    public void execute(StringCallback callback) {
        getUserInfo(ri, callback);
    }

    @SuppressWarnings("unchecked")
    public static void getUserInfo(final IMyAidl ri, final StringCallback callback) {
        Map<String, String> userMap = null;
        try {

            userMap = ri.getUser();
            JSONObject obj = new JSONObject(userMap);
            callback.onFinish(obj.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
