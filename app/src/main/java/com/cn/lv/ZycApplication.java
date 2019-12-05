package com.cn.lv;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.cn.frame.api.ApiManager;
import com.cn.frame.api.CrashHandler;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.bean.LoginBean;
import com.cn.frame.http.retrofit.RetrofitManager;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.SharePreferenceUtil;
import com.cn.lv.chat.HxHelper;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.yanzhenjie.nohttp.NoHttp;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;


/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class ZycApplication extends LitePalApplication {
    private static ZycApplication instance;
    private LoginBean loginBean;
    /**
     * 调试模式
     * 1、微信登录
     * 2、自定义URL
     * 3、自动切换环境（如果线上版本比当前版本更低，就切换到灰度环境）
     */
    public final boolean debugMode = true;
    /**
     * baseUrl
     */
    private String baseUrl;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        setBaseUrl(BuildConfig.BASE_BASIC_URL);
        registerActivityLifecycleCallbacks(new LifecycleHandler());
        //app 帮助类
        ApiManager.getInstance().init(this, debugMode);
        //网络
        RetrofitManager.getInstance().init(BuildConfig.BASE_BASIC_URL);
        //日志捕捉
        CrashHandler.init(this);
        //环信
        initEase();
        //数据库
        LitePal.initialize(this);
        NoHttp.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 环信初始化
     */
    private void initEase() {
        //环信初始化
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        EaseUI.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(debugMode);
        //设置头像为圆形
        EaseAvatarOptions avatarOpts = new EaseAvatarOptions();
        //0：默认，1：圆形，2：矩形
        avatarOpts.setAvatarShape(2);
        avatarOpts.setAvatarRadius(BaseUtils.dp2px(this, 4));
        EaseUI.getInstance().setAvatarOptions(avatarOpts);
        //设置有关环信自定义的相关配置  titlebar、头像、名字处理
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.setShowChatTitle(false);
    }


    public LoginBean getLoginBean() {
        String userStr = (String) SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_BEAN,
                "");
        if (!TextUtils.isEmpty(userStr)) {
            loginBean = new Gson().fromJson(userStr, LoginBean.class);
        }
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
        SharePreferenceUtil.putObject(this, CommonData.KEY_LOGIN_BEAN, loginBean);
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 动态更新url
     */
    public void updateBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        RetrofitManager.getInstance().updateBaseUrl(baseUrl);
    }


    public static ZycApplication getInstance() {
        return instance;
    }

    /**
     * app字体不随系统改变而改变
     *
     * @return 字体
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}
