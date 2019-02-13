package petrochina.cqzt.mc.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import petrochina.cqzt.mc.IMyAidl;
import petrochina.cqzt.mc.call.IMyUploadCallBack;

/**
 * Created by zzh on 2018/7/27.
 */

public class UpRequest extends McRequest {
    private List<Map<String, String>> files;

    public UpRequest(IMyAidl ri) {
        super(ri);
    }

    public UpRequest files(List<Map<String, String>> files) {
        this.files = files;
        return this;
    }

    public UpRequest url(String url) {
        this.url = url;
        return this;
    }

    public UpRequest tag(Context context) {
        this.context = context;
        return this;
    }

    public UpRequest param(String key, String value) {
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

            JSONArray list = new JSONArray();
            for (Object obj : files) {
                @SuppressWarnings("unchecked")
                Map<String, String> filemap = (Map<String, String>) obj;
                JSONObject file = new JSONObject();
                for (String key : filemap.keySet()) {
                    file.put(key, filemap.get(key));
                }
                list.put(file);
            }
            dfMsg.put("files", list);
            String jsonPara = dfMsg.toString();

            upload(ri, callback, jsonPara);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void upload(final IMyAidl ri, final StringCallback callback, final String jsonPara) {
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
                    ri.uploadRegisterListener(jsonPara, new IMyUploadCallBack.Stub() {
                        @Override
                        public void uploadCallBack(final String result) throws RemoteException {
                            Message msg = Message.obtain();
                            msg.obj = result;
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
