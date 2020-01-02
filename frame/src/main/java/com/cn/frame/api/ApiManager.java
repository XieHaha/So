package com.cn.frame.api;

import android.content.Context;

import com.cn.frame.api.notify.NotifyChangeListenerManager;
import com.cn.frame.utils.SweetLog;

/**
 * SDK 初始化
 *
 * @author dundun
 */
public class ApiManager {
    private static final String TAG = ApiManager.class.getName();
    private static Context sContext;
    private static boolean isLogEnable = true;
    private static ApiManager mInstance;

    private ApiManager() {
    }

    public static synchronized ApiManager getInstance() {
        if (null == mInstance) {
            mInstance = new ApiManager();
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, boolean isEnable) {
        sContext = context;
        isLogEnable = isEnable;
        SweetLog.setIsEnableLog(isEnable);
    }

    public Context getContext() {
        if (null == sContext) {
            SweetLog.w(TAG, "api Context is null, must be initial!");
        }
        return sContext;
    }

    public NotifyChangeListenerManager.NotifyChangeListenerServer getServer() {
        return NotifyChangeListenerManager.getInstance();
    }

    public void close() {
        sContext = null;
        mInstance = null;
    }
}
