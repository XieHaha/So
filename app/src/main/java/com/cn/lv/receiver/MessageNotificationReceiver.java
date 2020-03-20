package com.cn.lv.receiver;

import android.content.Context;

import com.cn.frame.utils.SweetLog;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @date 20/3/20 14:02
 * @des
 */
public class MessageNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType,
                                                PushNotificationMessage pushNotificationMessage) {
        SweetLog.i("Sweet", "push  message");
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType,
                                                PushNotificationMessage pushNotificationMessage) {
        SweetLog.i("Sweet", "push  click");
        return false;
    }
}
