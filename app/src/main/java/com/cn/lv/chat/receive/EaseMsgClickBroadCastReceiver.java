package com.cn.lv.chat.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author dundun
 * @date 19/2/23
 */
public class EaseMsgClickBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (BaseData.EASE_MSG_ANDROID_INTENT_CLICK.equals(intent.getAction())) {
//            if (ZycApplication.getInstance().getLoginBean() == null) {
//                return;
//            }
//            String chatId = intent.getStringExtra(CommonData.KEY_CHAT_ID);
//            Intent mainIntent, baseIntent;
//            Intent[] intents;
//            mainIntent = new Intent(context, MainActivity.class);
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            baseIntent = new Intent(context, ChatContainerActivity.class);
//            baseIntent.putExtra(CommonData.KEY_CHAT_ID, chatId.toUpperCase());
//            if (chatId.toUpperCase().startsWith(BaseData.BASE_DOCTOR_CODE)) {
//                baseIntent.putExtra(CommonData.KEY_DOCTOR_CHAT, true);
//            }
//            else {
//                baseIntent.putExtra(CommonData.KEY_PATIENT_CHAT, true);
//            }
//            intents = new Intent[] { mainIntent, baseIntent };
//            context.startActivities(intents);
//        }
    }
}
