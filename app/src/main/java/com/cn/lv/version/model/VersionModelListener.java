package com.cn.lv.version.model;

import com.cn.frame.data.bean.VersionBean;

import java.io.File;

public interface VersionModelListener {
    void getNewestVersion(NewestVersionCallBack callBack);

    void downloadAPK(String url, DownloadAPKCallBack downloadAPKCallBack);

    interface NewestVersionCallBack {
        void result(VersionBean version);

        void error(String s);
    }

    interface DownloadAPKCallBack {
        void downEnd(File file);

        void downloading(long total, long currnetData);

        void downloadError(String s);
    }
}
