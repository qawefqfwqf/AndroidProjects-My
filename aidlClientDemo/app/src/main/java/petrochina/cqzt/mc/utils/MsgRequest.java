package petrochina.cqzt.mc.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import petrochina.cqzt.mc.IMyAidl;
import petrochina.cqzt.mc.call.IMyJsonCallBack;

/**
 * Created by zzh on 2018/7/27.
 */

public class MsgRequest extends McRequest {
    public MsgRequest(IMyAidl ri) {
        super(ri);
    }

    public MsgRequest url(String url) {
        this.url = url;
        return this;
    }

    public MsgRequest tag(Context context) {
        this.context = context;
        return this;
    }

    public MsgRequest param(String key, String value) {
        params.put(key, value);
        return this;
    }

    public void execute(StringCallback callback) {
        try {
            JSONObject dfMsg = setParams();
            JSONObject parameters = new JSONObject();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.put(entry.getKey(), entry.getValue());
            }

            dfMsg.put("parameters", parameters);
            dfMsg.put("server", url);
            dfMsg.put("sessionid", McUtils.getSession(context));

            String jsonPara = dfMsg.toString();
            message(ri, callback, jsonPara);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void message(final IMyAidl ri, final StringCallback callback, final String jsonPara) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    JSONObject dfmsg = new JSONObject((String) msg.obj);
                    if (dfmsg.has("errormsg")) {
                        callback.onError((String) dfmsg.get("errormsg"));
                    } else {
                        String msgtoken = (String) dfmsg.get("msgToken");
                        if (msgtoken.equals(msgToken)) {
                            String sessionid = (String) dfmsg.get("sessionid");
                            String info = (String) dfmsg.get("info");
                            McUtils.saveSession(context, sessionid);
                            callback.onFinish(info);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ri.jsonRegisterListener(jsonPara, new IMyJsonCallBack.Stub() {
                        @Override
                        public void jsonCallBack(final String jsonData) throws RemoteException {
                            Message msg = Message.obtain();
                            msg.obj = jsonData;
                            handler.sendMessage(msg);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
