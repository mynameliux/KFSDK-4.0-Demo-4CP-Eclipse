package com.kfsdk.eclipse.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kf.framework.KFHelper;
import com.kf.framework.KFSDKListener;
import com.kf.framework.Params;
import com.kf.framework.PayWrapper;
import com.kf.framework.SDKPluginWrapper;
import com.kf.framework.UserWrapper;
import com.kf.framework.exception.KFAPIException;
import com.kf.framework.java.KFSDK;
import com.kf.framework.java.KFSDKPay;
import com.kf.framework.java.KFSDKStatistic;
import com.kf.framework.java.KFSDKUser;
import com.kf.utils.KFLog;
import com.kf.utils.ToastBuilder;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btnMainLogin;
    Button btnMainLogout;
    Button btnMainSwitchAccount;
    Button btnMainExit;
    Button btnMainPay;
    Button btnMainGetOrderInfo;
    Button btnMainRecordRoleUp;
    Button btnMainRecordRoleCreate;
    TextView tvMainResult;
    TextView tvMainBaseInfo;
    String waitingTestStr = "just testing please wait...";
    String sendStatisticFinish = "发送模拟统计完成";
    String testServerID = "1";
    String testServerName = "s1";
    String testRoleID = "2051";
    String testRoleName = "rolName";
    String testRoleLevel = "9527";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        initView();
        initKFSDK();
        refreshBaseInfo();
    }


    private void refreshBaseInfo() {
        tvMainBaseInfo.setText(getDemoBaseInfo());
    }

    private void initView() {
        btnMainLogin = bindButtonViewById(R.id.btn_main_login);
        btnMainLogout = bindButtonViewById(R.id.btn_main_logout);
        btnMainSwitchAccount = bindButtonViewById(R.id.btn_main_switch_account);
        btnMainExit = bindButtonViewById(R.id.btn_main_exit);
        btnMainPay = bindButtonViewById(R.id.btn_main_pay);
        btnMainGetOrderInfo = bindButtonViewById(R.id.btn_main_get_order_info);
        btnMainRecordRoleUp = bindButtonViewById(R.id.btn_main_record_role_up);
        btnMainRecordRoleCreate = bindButtonViewById(R.id.btn_main_record_role_create);
        tvMainResult = (TextView) findViewById(R.id.tv_main_result);
        tvMainBaseInfo = (TextView) findViewById(R.id.tv_main_base_info);
    }

    private Button bindButtonViewById(int id) {
        Button viewById = (Button) findViewById(id);
        viewById.setOnClickListener(this);
        return viewById;
    }

    private String getDemoBaseInfo() {
        StringBuilder sb = new StringBuilder();
        String appName = KFHelper.getAPPName();
        String packageName = KFHelper.getPackageName();
        sb.append("基础信息");
        sb.append("\n应用名称 ");
        sb.append(appName);
        sb.append(" 测试包名 ");
        sb.append(packageName);
        sb.append("\n");
        if (SDKPluginWrapper.getDeveloperInfo().get("debugMode").equals("0")) {
            sb.append("开启SDK日志");
        } else {
            sb.append("关闭SDK日志");
        }
        return sb.toString();
    }

    private void initKFSDK() {
        KFSDK.getInstance().init(this);
        KFSDKUser.getInstance().setListener(new KFSDKListener() {
            @Override
            public void onCallBack(int code, String msg) {
                printAtTextViewResult(code, "KFSDKUser " + msg);
                switch (code) {
                    case UserWrapper.ACTION_RET_LOGIN_FAIL:
                        //login fail job
                        break;
                    case UserWrapper.ACTION_RET_LOGIN_SUCCESS:
                        HashMap<String, String> params = new HashMap<>();
                        //用户标识：只用添加用户ID即可
                        params.put(Params.Statistic.KEY_ROLE_USERMARK, KFSDKUser.getInstance().userId + "@" + KFSDKUser.getInstance().getChannel());
                        params.put(Params.Statistic.KEY_ROLE_ID, testRoleID);
                        params.put(Params.Statistic.KEY_ROLE_SERVER_ID, testServerID);
                        params.put(Params.Statistic.KEY_ROLE_NAME, testRoleName);
                        // 服务器名称
                        params.put(Params.Statistic.KEY_ROLE_SERVER_NAME, testServerName);
                        // 角色等级
                        params.put(Params.Statistic.KEY_ROLE_LEVEL, testRoleLevel);
                        //角色等级
                        params.put(Params.Statistic.KEY_ROLE_GRADE, testRoleLevel);

                        KFSDKUser.getInstance().enterGame(params);


                        StringBuilder sb = new StringBuilder();
                        String openId = KFSDKUser.getInstance().getOpenId();
                        sb.append(checkCallBack2String("openId", openId));
                        String token = KFSDKUser.getInstance().getToken();
                        sb.append(checkCallBack2String("token", token));
                        String channel = KFSDKUser.getInstance().getChannel();
                        sb.append(checkCallBack2String("channel", channel));
                        String userId = KFSDKUser.getInstance().getUserId();
                        sb.append(checkCallBack2String("userId", userId));
                        String userName = KFSDKUser.getInstance().getUserName();
                        sb.append(checkCallBack2String("userName", userName));
                        String orderNo = KFSDKPay.getInstance().getOrderNo();
                        sb.append(checkCallBack2String("orderNo", orderNo));
                        printAtTextViewResult(code, msg + " login info: \n" + sb.toString());
                        break;
                    case UserWrapper.ACTION_RET_INIT_SUCCESS:
                        break;
                    case UserWrapper.ACTION_RET_EXIT_CANCEL:
                        break;
                    case UserWrapper.ACTION_RET_EXIT_SUCCESS:
                        break;
                    case UserWrapper.ACTION_RET_LOGOUT_FAIL:
                        break;
                    case UserWrapper.ACTION_RET_LOGOUT_SUCCESS:
                        break;
                    case UserWrapper.ACTION_RET_LOGOUT_CANCEL:
                        break;
                    case UserWrapper.ACTION_RET_INIT_FAIL:

                        break;
                    case UserWrapper.ACTION_RET_CHANGE_ACCOUNT_SUCCESS:
                        printAtTextViewResult(code, "KFSDKUser " + "切换成功");
                        ToastBuilder.make(MainActivity.this,"切换账号成功",ToastBuilder.DEFAULT_TOAST);
                        break;
                    case UserWrapper.ACTION_RET_CHANGE_ACCOUNT_FAIL:
                        break;
                    default:
                        throw new KFAPIException("登陆回调异常 Check you setting of " +
                                "KFSDKUser.getInstance().setListener code" +
                                "|this code is " + code + " |message is " + msg);
                }
            }

            private String checkCallBack2String(String key, String params) {
                String linePass = "\n--------------\n";
                if (null == params) {
                    return key + " is null. " + linePass;
                } else if (params.equals("")) {
                    return key + " is \"\" ." + linePass;
                } else {
                    return key + ": " + params + linePass;
                }
            }
        });
        KFSDKPay.getInstance().setListener(new KFSDKListener() {
            @Override
            public void onCallBack(int code, String msg) {
                switch (code) {
                    case PayWrapper.PAYRESULT_SUCCESS:
                        printPayBack(code, msg);
                        break;
                    case PayWrapper.PAYRESULT_FAIL:
                        printPayBack(code, msg);
                        break;
                    case PayWrapper.PAYRESULT_CANCEL:
                        printPayBack(code, msg);
                        break;
                    case PayWrapper.PAYRESULT_ORDER_INFO_SUCCESS:
                        printPayBack(code, msg);
                        if (KFSDKPay.getInstance().getOrderStatus() == 0) {
                            ToastBuilder.make(KFHelper.getAppContext(),
                                    KFSDKPay.getInstance().getOrderStatusMessage(), ToastBuilder.DEFAULT_TOAST_SINGLE);
                        } else {
                            ToastBuilder.make(KFHelper.getAppContext(),
                                    KFSDKPay.getInstance().getOrderStatusMessage(), ToastBuilder.DEFAULT_TOAST_SINGLE);
                        }
                        break;
                    case PayWrapper.PAYRESULT_ORDER_INFO_FAIL:
                        printPayBack(code, msg);
                    default:
                        ToastBuilder.make(KFHelper.getAppContext(),
                                "订单支付默认回复" +
                                        " code" + code +
                                        " msg: " + msg,
                                ToastBuilder.DEFAULT_TOAST_SINGLE);
                        break;
                }
            }

            private void printPayBack(int code, String msg) {
                String orderNo = KFSDKPay.getInstance().getOrderNo();
                KFLog.d(orderNo);
                if (!TextUtils.isEmpty(orderNo)) {
                    printAtTextViewResult(code, "KFSDKPay " + msg + "\norderNo: " + orderNo);
                } else {
                    printAtTextViewResult(code, "KFSDKPay " + msg);
                }
            }
        });
    }


    public void onClick(View view) {
        HashMap<String, Object> paramsObj = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        //tvMainResult.setText(waitingTestStr);
        switch (view.getId()) {
            case R.id.btn_main_login:
                KFSDKUser.getInstance().login();
                //test login print at tvMainResult.setText();
                break;
            case R.id.btn_main_logout:
                KFSDKUser.getInstance().logout();
                //test logout print at tvMainResult.setText();
                break;
            case R.id.btn_main_switch_account:
                KFSDKUser.getInstance().changeAccout();
                //test switch account print at tvMainResult.setText();
                break;
            case R.id.btn_main_exit:
                //test exit print at tvMainResult.setText();
                KFSDKUser.getInstance().exit();
                break;
            case R.id.btn_main_pay:
                //test pay print at tvMainResult.setText();
                paramsObj.put(Params.Pay.KEY_ORDER_NO, "");//订单号
                paramsObj.put(Params.Pay.KEY_AMOUNT, "1");//购买商品金额
                paramsObj.put(Params.Pay.KEY_SERVER_ID, testServerID);//服务器ID
                paramsObj.put(Params.Pay.KEY_PRODUCT_ID, "testPayID");//购买商品的商品ID
                paramsObj.put(Params.Pay.KEY_PRODUCT_NUM, "1");//购买商品的数量
                paramsObj.put(Params.Pay.KEY_GAMEEXTEND, "");//额外参数,没有传""
                paramsObj.put(Params.Pay.KEY_NOTIFY_URL, "");//应用方提供的支付结果通知uri,没有先传任意测试字符串
                paramsObj.put(Params.Pay.KEY_CONIN_NAME, "金币");
                paramsObj.put(Params.Pay.KEY_RATE, "10");
                paramsObj.put(Params.Pay.KEY_ROLE_ID, testRoleID);
                paramsObj.put(Params.Pay.KEY_ROLE_NAME, testRoleName);
                paramsObj.put(Params.Pay.KEY_ROLE_LEVEL, testRoleLevel);
                paramsObj.put(Params.Pay.KEY_PRODUCT_NAME, "商品名字");
                paramsObj.put(Params.Pay.KEY_SERVER_NAME, testServerName);//服务器ID
                KFSDKPay.getInstance().pay(paramsObj);
                break;
            case R.id.btn_main_get_order_info:
                //test order info print at tvMainResult.setText();
                if (!paramsObj.isEmpty()) {
                    paramsObj.clear();
                }
                String orderNo = KFSDKPay.getInstance().getOrderNo();
                if (!TextUtils.isEmpty(orderNo)) {
                    paramsObj.put(Params.Pay.KEY_PAY_ORDER_ID, orderNo);
                    KFSDKPay.getInstance().getOrderInfo(paramsObj);
                } else {
                    ToastBuilder.make(getApplicationContext(), "获取订单号错误，请确认有订单后再尝试", ToastBuilder.DEFAULT_TOAST_SINGLE);
                }
                break;
            case R.id.btn_main_record_role_up:
                //test record role up print at tvMainResult.setText();
                params.put(Params.Statistic.KEY_ROLE_USERMARK, KFSDKUser.getInstance().userId + "@" + KFSDKUser.getInstance().getChannel());
                params.put(Params.Statistic.KEY_ROLE_ID, "1");
                //角色账号
                params.put(Params.Statistic.KEY_ROLE_USERID, testRoleID);
                // 角色等级
                params.put(Params.Statistic.KEY_ROLE_LEVEL, testRoleLevel);
                // 服务器ID
                params.put(Params.Statistic.KEY_ROLE_SERVER_ID, testServerID);
                // 角色昵称
                params.put(Params.Statistic.KEY_ROLE_NAME, testRoleName);
                //角色等级
                params.put(Params.Statistic.KEY_ROLE_GRADE, testRoleLevel);
                // 服务器名称
                params.put(Params.Statistic.KEY_ROLE_SERVER_NAME, testServerName);

                KFSDKStatistic.getInstance().recordRoleUp(params);
                String ruRes = sendStatisticFinish + " 模拟角色升级";
                tvMainResult.setText(ruRes);
                break;
            case R.id.btn_main_record_role_create:
                //test role create print at tvMainResult.setText();
                params.put(Params.Statistic.KEY_ROLE_USERMARK, KFSDKUser.getInstance().userId + "@" + KFSDKUser.getInstance().getChannel());
                //角色id
                params.put(Params.Statistic.KEY_ROLE_ID, testRoleID);
                // 角色账号
                params.put(Params.Statistic.KEY_ROLE_USERID, testRoleID);
                // 角色等级
                params.put(Params.Statistic.KEY_ROLE_LEVEL, testRoleLevel);
                // 角色昵称
                params.put(Params.Statistic.KEY_ROLE_NAME, testRoleName);
                // 服务器id
                params.put(Params.Statistic.KEY_ROLE_SERVER_ID, testServerID);
                // 服务器名称
                params.put(Params.Statistic.KEY_ROLE_SERVER_NAME, testServerName);
                KFSDKStatistic.getInstance().recordRoleCreate(params);
                String rcRes = sendStatisticFinish + " 模拟创建角色";
                tvMainResult.setText(rcRes);
                break;
        }
    }

    @Override
    protected void onResume() {
        SDKPluginWrapper.onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SDKPluginWrapper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        SDKPluginWrapper.onPause();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        SDKPluginWrapper.onRestart();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        SDKPluginWrapper.onRestart();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        SDKPluginWrapper.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        SDKPluginWrapper.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onStop() {
        SDKPluginWrapper.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SDKPluginWrapper.onDestroy();
        super.onDestroy();
    }

    private void printAtTextViewResult(int code, String msg) {
        String resStr = "code: " + code + " |msg: " + msg;
        tvMainResult.setText(resStr);
    }

}
