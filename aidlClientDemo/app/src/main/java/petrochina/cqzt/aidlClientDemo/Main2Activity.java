package petrochina.cqzt.aidlClientDemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrochina.cqzt.mc.IMyAidl;
import petrochina.cqzt.mc.utils.DownCallback;
import petrochina.cqzt.mc.utils.StringCallback;
import petrochina.cqzt.mc.utils.McUtils;

public class Main2Activity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn6})
    public void onViewClicked(View view) {
        MyUtils.cleanData(linearLayout, tv);
        switch (view.getId()) {
            case R.id.btn://获取数据
                McUtils.message(ri)
                        .url(url)
                        .tag(this)
                        .param("flag", "getData")
                        .param("apply", "申请上路")
                        .execute(new StringCallback() {
                            @Override
                            public void onFinish(final String response) {
                                tv.setText("获取数据结果：" + response);
                            }

                            @Override
                            public void onError(String response) {
                                tv.setText("获取数据结果：" + response);
                            }
                        });

                break;
            case R.id.btn2://获取用户信息
                McUtils.getUserInfo(ri)
                        .execute(new StringCallback() {
                            @Override
                            public void onFinish(String response) {
                                tv.setText("获取数据结果：" + response);
                            }

                            @Override
                            public void onError(String response) {
                                tv.setText("获取数据结果：" + response);
                            }
                        });

                break;
            case R.id.btn3://下载文件
                final String savePath = Environment.getExternalStorageDirectory().getPath() + "/donwload/";
                McUtils.download(ri)
                        .url(url)
                        .tag(this)
                        .param("flag", "downloadImg")
                        .param("fileIds", "1,2,3,4,5,6")
                        .savePath(savePath)
                        .execute(new DownCallback() {
                            @Override
                            public void onFinish(final Map<String, Object> response) {
                                @SuppressWarnings("unchecked")
                                List<String> filenames = (List<String>) response.get("filenames");
                                for (String filename : filenames) {
                                    ImageView iv = new ImageView(Main2Activity.this);

                                    File f = new File(savePath, filename);
                                    Glide.with(Main2Activity.this).load(f).into(iv);

                                    linearLayout.addView(iv);
                                }
                                tv.setText("下载文件结果：" + response.get("info"));
                            }

                            @Override
                            public void onError(String response) {
                                tv.setText("下载文件结果：" + response);
                            }
                        });

                break;
            case R.id.btn4://选择图片并上传
                ImagePicker.getInstance().setSelectLimit(4);
                Intent intent1 = new Intent(Main2Activity.this, ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                break;
            case R.id.btn6://登陆
                McUtils.message(ri)
                        .url(url)
                        .tag(this)
                        .param("flag", "login")
                        .param("urlusername", "王小军")
                        .param("userpassword", "wxj81213218")
                        .execute(new StringCallback() {
                            @Override
                            public void onFinish(final String response) {
                                tv.setText("登陆结果：" + response);
                            }

                            @Override
                            public void onError(String response) {
                                tv.setText("登陆结果：" + response);
                            }
                        });

                break;
        }
    }

}