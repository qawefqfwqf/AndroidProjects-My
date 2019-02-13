package petrochina.cqzt.mc.utils;

/**
 * Created by zzh on 2018/7/27.
 */

public interface StringCallback {
    //访问成功的回调接口
    void onFinish(String response);

    void onError(String response);
}