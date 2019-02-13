package petrochina.cqzt.aidlClientDemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import petrochina.cqzt.mc.IMyAidl;
import petrochina.cqzt.mc.utils.McUtils;
import petrochina.cqzt.mc.utils.StringCallback;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String url =IPConfig.baseUrl;

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.imgs)
    LinearLayout linearLayout;

    public static final int REQUEST_CODE_SELECT = 100;
    public List<ImageItem> images = null;
    public static IMyAidl ri;
    private int dotimes = 0;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ri = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dotimes++;
            if (dotimes < 2) {
                Log.d("BindService Try Again", "==============================重试绑定服务  "+dotimes+1+"/2================================");
                McUtils.bind(conn, BaseActivity.this);
            } else {
                Toast.makeText(BaseActivity.this, "无法连接到井下移动客户端SDK服务", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(getLayout());//获取布局
        super.onCreate(savedInstanceState);
        //动态申请权限
        MyUtils.getAuthor(this);
        McUtils.bind(conn, this);
        ButterKnife.bind(this);
    }

    protected abstract int getLayout();

    //当activity销毁时，解绑服务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            McUtils.unbind(conn, this);
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {

                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                List<Map<String, String>> files = new ArrayList<>();
                for (ImageItem image : images) {
                    String filePath = image.path;
                    Map<String, String> file = new HashMap<>();
                    file.put("filepath", filePath);
                    file.put("filename", filePath.substring(filePath.lastIndexOf("/") + 1));
                    files.add(file);
                }

                McUtils.upload(ri)
                        .url(url)
                        .tag(this)
                        .files(files)
                        .param("flag", "uploadImg")
                        .execute(new StringCallback() {
                            @Override
                            public void onFinish(final String response) {
                                tv.setText("上传文件结果：" + response);
                            }

                            @Override
                            public void onError(String response) {
                                tv.setText("上传文件结果：" + response);
                            }
                        });
            }
        }

    }
}
