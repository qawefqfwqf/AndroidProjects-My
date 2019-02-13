package petrochina.cqzt.aidlClientDemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zzh on 2018/7/25.
 */

public class MyUtils {

    //跳转activity
    public static void OpenNew(Context context, Class cl) {
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent = new Intent(context, cl);

        //用Bundle携带数据
        Bundle bundle = new Bundle();
        //传递name参数为tinyphp
        bundle.putString("name", "xxxx");
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    public static void getAuthor(Context context){
        //还要动态获取权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
        }
    }

    //清除显示的数据
    public static void cleanData(LinearLayout linearLayout, TextView tv){
        linearLayout.removeAllViews();
        tv.setText("");
    }
}
