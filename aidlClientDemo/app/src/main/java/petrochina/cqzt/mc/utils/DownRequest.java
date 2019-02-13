package petrochina.cqzt.mc.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petrochina.cqzt.mc.IMyAidl;
import petrochina.cqzt.mc.call.IMyDownloadCallBack;

/**
 * Created by zzh on 2018/7/27.
 */

public class DownRequest extends McRequest {
    private String savePath;

    public DownRequest(IMyAidl ri) {
        super(ri);
    }

    public DownRequest savePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public DownRequest url(String url) {
        this.url = url;
        return this;
    }

    public DownRequest tag(Context context) {
        this.context = context;
        return this;
    }

    public DownRequest param(String key, String value) {
        params.put(key, value);
        return this;
    }

    public void execute(DownCallback callback) {
        try {
            JSONObject dfMsg = setParams();
            JSONObject parameters = new JSONObject();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.put(entry.getKey(), entry.getValue());
            }

            dfMsg.put("parameters", parameters);
            dfMsg.put("server", url);
            dfMsg.put("savePath", savePath);

            String jsonPara = dfMsg.toString();

            download(ri, callback, jsonPara);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void download(final IMyAidl ri, final DownCallback callback, final String jsonPara) {
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
                            List<String> filenames = new ArrayList<>();
                            JSONArray files = (JSONArray) dfmsg.get("files");
                            for (int i = 0; i < files.length(); i++) {
                                JSONObject file = (JSONObject) files.get(i);
                                String filename = (String) file.get("filename");
                                filenames.add(filename);
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("info", info);
                            map.put("filenames", filenames);
                            callback.onFinish(map);
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
                    ri.downloadRegisterListener(jsonPara, new IMyDownloadCallBack.Stub() {
                        @Override
                        public void downloadCallBack(final String result) throws RemoteException {
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
