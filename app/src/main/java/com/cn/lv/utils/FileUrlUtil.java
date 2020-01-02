package com.cn.lv.utils;
public class FileUrlUtil {
    /**
     * 添加header token
     *
     * @param url
     * @return
     */
    public static String addTokenToUrl(String url) {
        return url;
        //        if (TextUtils.isEmpty(url)) {
        //            return null;
        //        }
        //        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("token", ZycApplication.getInstance()
        //                                                                                                         .getLoginBean()
        //                                                                                                         .getToken())
        //                                                                       .build());
        //        return glideUrl;
    }
}
