[TOC]

# 接入准备

* 配置开发环境
* 获取SDK接入Demo
* 接入

[Java Demo 工程地址](https://github.com/KuaiFaMaster/KFSDK-4.0-Demo-4CP-Eclipse)

# 配置

开发环境需求

**Eclispe ADT**

* 编码 UTF-8
* jdk 1.8 及以上
* Android Development Tools   23.0.7.2120684
* Version: Mars.2 Release (4.5.2)
* Complie `API 19` `Android 4.4`
* less `API 16` `Android 4.1.2`

# 使用注意

- 一切以 demo 为最终效果
- 先阅读文档到 `KFSDK 使用` 之前后再进行调用！

## 权限配置

配置权限 不配置将闪退

```xml

    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <uses-permission android:name="android.permission.READ_SMS" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

```
如果需要额外权限，例如 在`KFSDK4EclipseDemo` 的`AndroidManifest.xml` 中添加

## 将快发提供的4.0sdkLib工程加入到你的项目中

工程包括

- Lib-kf-utils
- Lib-volleyplus
- Lib-KFSDK

依赖关系是
```sh
        apk
         |
    Lib-volleyplus
    /           \
  Lib-kf-utils  Lib-volleyplus
```



## Application配置

请在module `app`的`AndroidManifest.xml` 中按如下配置

```xml
 <application
        android:name="com.kf.framework.KFApplication"
        android:allowBackup="true">

</application>
```

如果有自定义Application请在自定义的Application继承KFApplication


```java
@Override
public void onCreate() {
    super.onCreate();

@Override
public void attachBaseContext(Context base) {
    super.attachBaseContext(base);

@Override
public void onConfigurationChanged(Configuration config) {
    super.onConfigurationChanged(config);
}
```

## 配置参数

1. 从快发提供的 KFSDK4EclipseDemo 中拷贝`assets`目录下的`developer.properties`到你的游戏工程中assets目录或者在assets目录新建developer.properties文件并拷贝如下示例文件的内容到新建的文件中
1. 设置HJR_GAMEKEY的值为你从快发申请的值(联系商务)
1. 开启快发的日志请将debugMode设置为0,关闭设置为1


## developer.properties示例文件内容
```java

[static]
#快发gameKey,自己在快发官网申请或联系快发商务获取
HJR_GAMEKEY = c39697dd79df766cbf0834e1471cc1ae

[dynamic]
#日志开关 0:打开日志 1:关闭日志
debugMode = 0
channel = KF
PluginUser= UserKF
PluginIAP= PayKF
PluginStatistic= StatisticKF
screen_oriention = 1 //0:横屏 1:竖屏

```

# KFSDK 使用

## Activity生命周期函数

```java
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

    @Override
    protected void onConfigurationChanged(Configuration newConfig){
        SDKPluginWrapper.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

```

## SDK初始化

初始化都在 Activity 的方法 `onCreate`中

```java
KFSDK.getInstance().init(this);
```

> init() 要求传入上下文是Activity的上下文

## SDK登陆
```java
KFSDKUser.getInstance().login();
```

## SDK注销
```java
KFSDKUser.getInstance().logout();
```

## SDK退出
```java
KFSDKUser.getInstance().exit();
```

## SDK切换账号
```java
KFSDKUser.getInstance().changeAccout();
```

### 用户模块调用接口之后获取回调信息
```java
KFSDKUser.getInstance().setListener(new KFSDKListener() {
	@Override
	public void onCallBack(int code, String msg) {
			//这里响应用户模块的回调
		}
	}
);
```

用户模块回调参数表

|参数|描述|
|:--|:--|
|UserWrapper.ACTION_RET_INIT_SUCCESS|渠道SDK初始化成功|
|UserWrapper.ACTION_RET_INIT_FAIL|渠道SDK初始化失败|
|UserWrapper.ACTION_RET_LOGIN_SUCCESS|登录成功|
|UserWrapper.ACTION_RET_LOGIN_FAIL|登录失败|
|UserWrapper.ACTION_RET_LOGOUT_SUCCESS|登出成功|
|UserWrapper.ACTION_RET_LOGOUT_FAIL|登出失败|
|UserWrapper.ACTION_RET_CHANGE_ACCOUNT_SUCCESS|切换帐户成功|
|UserWrapper.ACTION_RET_CHANGE_ACCOUNT_FAIL|切换帐户失败|
|UserWrapper.ACTION_RET_EXIT_SUCCESS|退出成功|
|UserWrapper.ACTION_RET_EXIT_FAIL|退出失败|


## 支付模块接入

## 调用支付
```java
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Params.Pay.KEY_AMOUNT, "1");//购买商品金额
        params.put(Params.Pay.KEY_ORDER_NO, "");//订单号
        params.put(Params.Pay.KEY_SERVER_ID, "1");//服务器ID
        params.put(Params.Pay.KEY_PRODUCT_ID, "1");//购买商品的商品ID
        params.put(Params.Pay.KEY_PRODUCT_NUM, "1");//购买商品的数量
        params.put(Params.Pay.KEY_GAMEEXTEND, "");//额外参数,没有传""
        params.put(Params.Pay.KEY_NOTIFY_URL, "");//应用方提供的支付结果通知uri,没有先传任意测试字符串
        params.put(Params.Pay.KEY_CONIN_NAME, "金币");
        params.put(Params.Pay.KEY_RATE, "10");
        params.put(Params.Pay.KEY_ROLE_ID, "1");
        params.put(Params.Pay.KEY_ROLE_NAME, "11");
        params.put(Params.Pay.KEY_ROLE_LEVEL, "1");
        params.put(Params.Pay.KEY_PRODUCT_NAME, "商品名字");
        params.put(Params.Pay.KEY_SERVER_NAME, "dd");//服务器ID

        KFSDKPay.getInstance().pay(params);
```

## 根据订单号查询订单状态
```java
        String orderId = "1610139300002000";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Params.Pay.KEY_PAY_ORDER_ID, orderId);
        KFSDKPay.getInstance().getOrderInfo(params);
```


### 支付模块调用接口之后获取回调信息
```java
    KFSDKPay.getInstance().setListener(new KFSDKListener() {
            @Override
            public void onCallBack(int code, String msg) {
			//这里响应支付的结果
			}
	}
);
```

支付模块参数回调表

|参数|描述|
|:--|:--|
|PayWrapper.PAYRESULT_SUCCESS|支付成功|
|PayWrapper.PAYRESULT_FAIL|支付失败|
|PayWrapper.PAYRESULT_CANCEL|支付取消|
|PayWrapper.PAYRESULT_NETWORK_ERROR|支付网络错误|

## 响应数据

模块数据是一个个响应式变量，具体参照对应的对象表格

### 用户模块响应数据

|变量|类型|描述|
|:--|:--|:--|
|KFSDKUser.getInstance().getOpenId();|String|OpenId|
|KFSDKUser.getInstance().getUserId();|String|UserId|
|KFSDKUser.getInstance().getCid();|String|cId|
|KFSDKUser.getInstance().getToken();|String|Token|
|KFSDKUser.getInstance().getUserName();|String|UserName|

### 支付模块响应数据

|变量|类型|描述|
|:--|:--|:--|
|KFSDKPay.getInstance().getOrderNo();|String|订单号|
|KFSDKPay.getInstance().getOrderStatus();|int|支付状态 0为成功，其他为失败|
|KFSDKPay.getInstance().getOrderStatusMessage();|String|支付消息|

## 统计功能接入(必接的三个统计接口)

### 进入游戏统计

```java
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
```

### 统计升级
```java
HashMap<String, String> params = new HashMap<String, String>();
params.put(Params.Statistic.KEY_ROLE_USERMARK, KFSDKUser.getInstance().getUserId() + "@"+KFSDKUser.getInstance().getChannel());
params.put(Params.Statistic.KEY_ROLE_ID, "1");
//角色账号
params.put(Params.Statistic.KEY_ROLE_USERID, "lewan10086");
// 角色等级
params.put(Params.Statistic.KEY_ROLE_LEVEL, "15");
// 服务器ID
params.put(Params.Statistic.KEY_ROLE_SERVER_ID, "2");
// 角色昵称
params.put(Params.Statistic.KEY_ROLE_NAME, "lewan10086");
//角色等级
params.put(Params.Statistic.KEY_ROLE_GRADE, "10");
// 服务器名称
params.put(Params.Statistic.KEY_ROLE_SERVER_NAME, "服务器名称");
KFSDKStatistic.getInstance().recordRoleUp(params);
```

### 统计角色创建

```java
HashMap<String, String> params = new HashMap<String, String>();
params.put(Params.Statistic.KEY_ROLE_USERMARK, KFSDKUser.getInstance().getUserId() + "@"+KFSDKUser.getInstance().getChannel());
//角色id
params.put(Params.Statistic.KEY_ROLE_ID, "1222");
// 角色账号
params.put(Params.Statistic.KEY_ROLE_USERID, "lewan10086");
// 角色等级
params.put(Params.Statistic.KEY_ROLE_LEVEL, "15");
// 角色昵称
params.put(Params.Statistic.KEY_ROLE_NAME, "角色昵称");
// 服务器id
params.put(Params.Statistic.KEY_ROLE_SERVER_ID, "2");
// 服务器名称
params.put(Params.Statistic.KEY_ROLE_SERVER_NAME, "服务器名称");
KFSDKStatistic.getInstance().recordRoleCreate(params);
```

----


Copyright 2017 kuaifazs.com

Licensed http://www.kuaifazs.com/licenses/LICENSE-1.0.txt
