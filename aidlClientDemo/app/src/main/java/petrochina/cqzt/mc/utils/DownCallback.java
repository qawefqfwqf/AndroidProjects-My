package petrochina.cqzt.mc.utils;

import java.util.Map;

/**
 * Created by zzh on 2018/7/27.
 */

public interface DownCallback {
    //访问成功的回调接口
    void onFinish(Map<String,Object> response);

    void onError(String response);
}