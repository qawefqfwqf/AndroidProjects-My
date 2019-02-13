// IMyAidl.aidl
package petrochina.cqzt.mc;

// Declare any non-default types here with import statements
import petrochina.cqzt.mc.call.IMyDownloadCallBack;

import petrochina.cqzt.mc.call.IMyUploadCallBack;

import petrochina.cqzt.mc.call.IMyJsonCallBack;

interface IMyAidl {
    /**
     * 除了基本数据类型，其他类型的参数都需要标上方向类型：in(输入), out(输出), inout(输入输出)
     */
    Map getUser();

    void jsonRegisterListener(String jsonPara,IMyJsonCallBack callBack);
    void downloadRegisterListener(String jsonPara,IMyDownloadCallBack downloadCallBack);
    void uploadRegisterListener(String jsonPara,IMyUploadCallBack uploadCallBack);
    void doInBackGround(int type);

    void unRegisterListener(IMyJsonCallBack jsonCallBack);
}
