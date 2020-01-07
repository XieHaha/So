package com.cn.lv.utils;

import com.cn.lv.BuildConfig;

public class FileUrlUtil {
    /**
     * 添加header token
     */
    public static String addTokenToUrl(String url) {
        return BuildConfig.BASE_BASIC_URL + url;
        //        if (TextUtils.isEmpty(url)) {
        //            return null;
        //        }
        //        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader
        //        ("token", SweetApplication.getInstance()
        //                                                                                                         .getLoginBean()
        //                                                                                                         .getToken())
        //                                                                       .build());
        //        return glideUrl;
    }
}
