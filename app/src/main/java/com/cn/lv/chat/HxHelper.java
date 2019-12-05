package com.cn.lv.chat;

import android.content.Context;
import android.text.TextUtils;

import com.cn.frame.data.BaseData;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.UserInfoCallback;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.Map;

/**
 * @author dundun
 */
public class HxHelper {
    /**
     * 扩展消息-昵称
     */
    public static final String MSG_EXT_NICKNAME = "hx_nickname";
    /**
     * 扩展消息-头像
     */
    public static final String MSG_EXT_AVATAR = "hx_avatar";
    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) {
            resource = new Resource();
        }
        return resource;
    }

    public static class Resource {
        private Context context;
        /**
         * 所有的会话集合
         */
        private Map<String, EMConversation> mConvMap;

        private Resource() {
            if (null != resource) {
                throw new IllegalStateException("Can not instantiate singleton class.");
            }
        }

        /**
         * 初始化
         *
         * @param context context
         */
        public void init(Context context) {
            this.context = context;
        }

        public EaseUser getUser(String username, UserInfoCallback callback) {
            if (TextUtils.isEmpty(username)) { return null; }
            EaseUser user = new EaseUser(username);
            if (username.startsWith(BaseData.BASE_DOCTOR_CODE)) {
            }
            return user;
        }
    }

    /**
     * 配置项
     */
    public static class Opts {
        private boolean showChatTitle;

        public boolean isShowChatTitle() {
            return showChatTitle;
        }

        public void setShowChatTitle(boolean showChatTitle) {
            this.showChatTitle = showChatTitle;
        }
    }
}
