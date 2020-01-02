package com.cn.lv.version.model;

import android.content.Context;

import com.yanzhenjie.nohttp.download.DownloadListener;
import com.cn.frame.api.DirHelper;
import com.cn.frame.api.FileTransferServer;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.VersionBean;
import com.cn.frame.http.listener.AbstractResponseAdapter;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.utils.SocialLog;

import java.io.File;
public class VersionModel extends AbstractResponseAdapter<BaseResponse> implements VersionModelListener {
    private static final String TAG = "VersionModel";
    private Context context;
    private String token;
    private NewestVersionCallBack callBack;
    private DownloadAPKCallBack downloadAPKCallBack;

    public VersionModel(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override
    public void getNewestVersion(NewestVersionCallBack callBack) {
        this.callBack = callBack;
        //获取最新版本
        RequestUtils.getVersion(context, token, this);
    }

    @Override
    public void downloadAPK(String url, DownloadAPKCallBack downloadAPKCallBack) {
        this.downloadAPKCallBack = downloadAPKCallBack;
        File file = new File(DirHelper.getPathFile() + "/ZYC.apk");
        if (file.exists()) {
            if (!file.delete()) {
                SocialLog.e(TAG, "delete error");
            }
        }
    }

    /**
     * 下载最新的apk
     */
    public void downloadAPK(String url, DownloadListener downloadListener) {
        File file = new File(DirHelper.getPathFile() + "/ZYC.apk");
        if (file.exists()) {
            if (!file.delete()) {
                SocialLog.e(TAG, "delete error");
            }
        }
        FileTransferServer.getInstance(context)
                          .downloadFile("", url, DirHelper.getPathFile(), "ZYC" + ".apk", downloadListener);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        if (callBack != null) {
            VersionBean bean = (VersionBean)response.getData();
            callBack.result(bean);
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (callBack != null) {
            callBack.error(response.getMsg());
        }
    }
}
