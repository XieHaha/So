package com.cn.lv.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.ChatBean;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.frame.widgets.dialog.listener.OnEnterClickListener;
import com.cn.frame.widgets.menu.MenuItem;
import com.cn.frame.widgets.menu.TopRightMenu;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.main.my.ReportActivity;
import com.cn.lv.ui.main.my.VipActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Message;

public class ChatActivity extends BaseActivity implements TopRightMenu.OnMenuItemClickListener {

    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.public_title_bar_right_img)
    ImageView publicTitleBarRightImg;
    private String title, targetId;

    private ChatBean chatBean;
    private boolean black;
    private int msgCount = 0;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_chat;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            targetId = getIntent().getStringExtra(CommonData.KEY_CHAT_ID);
            title = getIntent().getStringExtra(CommonData.KEY_CHAT_TITLE);
            black = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
            if (TextUtils.isEmpty(title)) {
                title = getIntent().getData().getQueryParameter("title");
            }
            if (TextUtils.isEmpty(targetId)) {
                targetId = getIntent().getData().getQueryParameter("targetId");
            }
            publicTitleBarTitle.setText(title);
        }
        publicTitleBarRightImg.setVisibility(View.VISIBLE);
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationFragment fragement =
                (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath("private")
                .appendQueryParameter("title", title)
                .appendQueryParameter("targetId", targetId).build();
        fragement.setUri(uri);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (!SweetApplication.getInstance().isVip()) {
            List<ChatBean> chatBeans = LitePal.where("chatId = ?", targetId).find(ChatBean.class);
            if (chatBeans != null && chatBeans.size() > 0) {
                chatBean = chatBeans.get(0);
                msgCount = chatBean.getMsgCount();
            } else {
                chatBean = new ChatBean();
                chatBean.setChatId(targetId);
                msgCount = 0;
                save();
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        publicTitleBarRightImg.setOnClickListener(v -> initMenu());

        /**
         * 设置发送消息的监听。
         */
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                if (!SweetApplication.getInstance().isVip()) {
                    if (msgCount >= 5) {
                        showDialog();
                        return null;
                    } else {
                        msgCount++;
                    }
                    save();
                }
                return message;
            }

            @Override
            public boolean onSent(Message message,
                                  RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });
    }

    private void showDialog() {
        save();
        HintDialog dialog = new HintDialog(this);
        dialog.setTitleString("提示").setContentString("升级会员享受更好的服务").setCancelableAndTouch(false).setCancelBtnGone(true).setEnterBtnTxt("升级会员").setOnEnterClickListener(new OnEnterClickListener() {
            @Override
            public void onEnter() {
                startActivity(new Intent(ChatActivity.this, VipActivity.class));
                finish();
            }
        }).show();
    }

    private void save() {
        chatBean.setMsgCount(msgCount);
        if (chatBean.save()) {
            SweetLog.i(TAG, "存储成功");
        } else {
            SweetLog.i(TAG, "存储失败");
        }
    }

    private void initMenu() {
        TopRightMenu mTopRightMenu = new TopRightMenu(this);
        List<MenuItem> menuItems = new ArrayList<>();
        if (black) {
            menuItems.add(new MenuItem(0, "取消屏蔽"));
        } else {
            menuItems.add(new MenuItem(0, "屏蔽用户"));
        }
        menuItems.add(new MenuItem(0, "举报TA"));
        mTopRightMenu.setHeight(BaseUtils.dp2px(this, 130)).addMenuList(menuItems).setOnMenuItemClickListener(this).showAsDropDown(publicTitleBarRightImg, -BaseUtils.dp2px(this, 94), 10);
    }

    @Override
    public void onMenuItemClick(int position) {
        if (position == 0) {
            if (black) {
                shieldUser(2);
            } else {
                shieldUser(1);
            }
        } else {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra(CommonData.KEY_PUBLIC, targetId);
            startActivity(intent);
        }
    }

    private void shieldUser(int state) {
        RequestUtils.shieldUser(this, signSession(InterfaceName.SHIELD_USER), targetId, state,
                this);
    }


    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.SHIELD_USER) {
            ToastUtil.toast(this, response.getMsg());
            black = !black;
        }
    }

}
