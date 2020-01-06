package com.cn.lv;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.cn.frame.api.ApiManager;
import com.cn.frame.api.CrashHandler;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.bean.UserBaseBean;
import com.cn.frame.http.retrofit.RetrofitManager;
import com.cn.frame.utils.SharePreferenceUtil;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class SweetApplication extends LitePalApplication {
    private static SweetApplication instance;
    private UserBaseBean loginBean;
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
        registerActivityLifecycleCallbacks(new LifecycleHandler());
        //app 帮助类
        ApiManager.getInstance().init(this, debugMode);
        //网络
        RetrofitManager.getInstance().init(BuildConfig.BASE_BASIC_URL);
        //日志捕捉
        CrashHandler.init(this);
        //数据库
        LitePal.initialize(this);
        //下载
        NoHttp.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public UserBaseBean getLoginBean() {
        String userStr = (String) SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_BEAN,
                "");
        if (!TextUtils.isEmpty(userStr)) {
            loginBean = new Gson().fromJson(userStr, UserBaseBean.class);
        }
        return loginBean;
    }

    public void setLoginBean(UserBaseBean loginBean) {
        this.loginBean = loginBean;
        SharePreferenceUtil.putObject(this, CommonData.KEY_LOGIN_BEAN, loginBean);
    }

    public static SweetApplication getInstance() {
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
