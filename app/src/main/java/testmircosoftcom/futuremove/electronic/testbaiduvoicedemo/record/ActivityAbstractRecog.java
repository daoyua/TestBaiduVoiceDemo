package testmircosoftcom.futuremove.electronic.testbaiduvoicedemo.record;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.speech.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import testmircosoftcom.futuremove.electronic.testbaiduvoicedemo.record.recog.MyRecognizer;
import testmircosoftcom.futuremove.electronic.testbaiduvoicedemo.record.recog.listener.IRecogListener;
import testmircosoftcom.futuremove.electronic.testbaiduvoicedemo.record.recog.listener.MessageStatusRecogListener;

/**
 * 识别activity 主要逻辑。
 * ActivityUiRecog为UI部分，不必细看
 */
public  class ActivityAbstractRecog extends Activity {

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;

    /*
     * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
     */
    protected boolean enableOffline =true;

    private static final String TAG = "ActivityAbstractRecog";

    /**
     * @param
     * @param enableOffline 展示的activity是否支持离线命令词
     */
    public ActivityAbstractRecog(boolean enableOffline) {
        this.enableOffline = true;
//        this.enableOffline = enableOffline;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    LogUtil.e(msg.obj.toString() + "\n");
                }
            }
        };
        // DEMO集成步骤 1.1 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        IRecogListener listener = new MessageStatusRecogListener(handler);
        // DEMO集成步骤 1.2 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例
        myRecognizer = new MyRecognizer(this, listener);
        if (enableOffline) {
            // 集成步骤 1.3（可选）加载离线资源。offlineParams是固定值，复制到您的代码里即可
            Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
            myRecognizer.loadOfflineEngine(offlineParams);
        }
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
//        final Map<String, Object> params = fetchParams();
         Map<String, Object> params = new HashMap<String, Object>() ;
        params.put("decoder",2);
        params.put("accept-audio-volume",false);

        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印

        // 复制此段可以自动检测常规错误
//        (new AutoCheck(getApplicationContext(), new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n");
//                        ; // 可以用下面一行替代，在logcat中查看代码
//                        // Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//        }, enableOffline)).checkAsr(params);
//        params.
        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        myRecognizer.start(params);
    }
//    protected Map<String, Object> fetchParams() {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
//        Map<String, Object> params = apiParams.fetch(sp);
//        //  集成时不需要上面的代码，只需要params参数。
//        return params;
//    }
    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     */
    protected void stop() {
        // DEMO集成步骤4 (可选） 停止录音
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     */
    protected void cancel() {
        // DEMO集成步骤5 (可选） 取消本次识别
        myRecognizer.cancel();
    }

    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    protected void onDestroy() {
        // DEMO集成步骤3 释放资源
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        myRecognizer.release();
        Log.i(TAG, "onDestory");
        super.onDestroy();
    }

}
