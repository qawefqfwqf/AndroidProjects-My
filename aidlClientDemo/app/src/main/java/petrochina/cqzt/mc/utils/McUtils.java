package petrochina.cqzt.mc.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.util.Random;

import petrochina.cqzt.mc.IMyAidl;

/**
 * Created by zzh on 2018/3/15.
 */

public class McUtils {

    private static final String TAG = "McService";
    private static final String PackageName = "petrochina.cqzt.mc";
    private static final String MainActivityName = "petrochina.cqzt.mc.MainActivity";
    private static final String ServiceName = "petrochina.cqzt.mc.service.MyAidlService";

    //绑定服务
    public static void bind(ServiceConnection conn, Context context) {
        Intent intent = new Intent();

        ComponentName cn = new ComponentName(PackageName, ServiceName);
        intent.setAction(ServiceName);
        intent.setComponent(cn);

        if(Build.VERSION.SDK_INT < 26) {
            context.startService(intent);
        }

        boolean result = context.bindService(intent, conn, context.BIND_AUTO_CREATE);
        if (result) {
            Log.d(TAG, "==============================绑定服务成功================================");
        } else {
            Log.d(TAG, "==============================绑定服务失败==============================");
        }
    }

    //解绑服务
    public static void unbind(ServiceConnection conn, Context context) {
        if (conn != null) {
            context.unbindService(conn);
        }

    }

    public static UserRequest getUserInfo(IMyAidl ri) {
        return new UserRequest(ri);
    }

    public static MsgRequest message(IMyAidl ri) {
        return new MsgRequest(ri);
    }

    public static UpRequest upload(IMyAidl ri) {
        return new UpRequest(ri);
    }

    public static DownRequest download(IMyAidl ri) {
        return new DownRequest(ri);
    }

    //启动井下客户端再回来
    public static void goToAppHall(String MainBundleId, Context context) {
        Intent intent = new Intent();
        intent.setClassName(PackageName, MainActivityName);
        intent.putExtra("MainBundleId", MainBundleId);
        context.startActivity(intent);
    }

    /***
     * 产生随机数的方法
     *
     * @param length
     * @return
     */
    protected static String getCharAndNumr(int length) {
        if (length >= 3) {
            String val = "";
            Random random = new Random();
            // t0、t1、t2用来标识大小写和数字是否在产生的随机数中出现
            int t0 = 0;
            int t1 = 0;
            int t2 = 0;
            for (int i = 0; i < length; i++) {
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
                // 产生的是字母
                if ("char".equalsIgnoreCase(charOrNum)) // 字符串
                {
                    // int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    // //取得大写字母还是小写字母
                    int choice = 0;
                    if (random.nextInt(2) % 2 == 0) {
                        choice = 65;
                        t0 = 1;
                    } else {
                        choice = 97;
                        t1 = 1;
                    }
                    val += (char) (choice + random.nextInt(26));
                }
                // 产生的是数字
                else if ("num".equalsIgnoreCase(charOrNum)) // 数字
                {
                    val += String.valueOf(random.nextInt(10));
                    t2 = 1;
                }
            }
            // 用于判断是是否包括大写字母、小写字母、数字
            if (t0 == 0 || t1 == 0 || t2 == 0) {
                val = getCharAndNumr(length); // 不满足则递归调用该方法
                return val;
            } else
                return val;

        } else {

            return null;
        }
    }

    //保存session信息
    protected static void saveSession(Context context, String sessionid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sessionid", sessionid);
        editor.commit();
    }

    //获取session信息
    protected static String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        String sessionid = preferences.getString("sessionid", "");
        return sessionid;
    }

}
