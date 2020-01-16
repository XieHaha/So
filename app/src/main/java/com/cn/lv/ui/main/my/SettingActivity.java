package com.cn.lv.ui.main.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.lv.R;

import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

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
        return R.layout.act_setting;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    private void signOut() {
        RequestUtils.signOut(this, BaseUtils.signSpan(this,
                userInfo.getMobile_number(), loginBean.getSession_id(), InterfaceName.SIGN_OUT)
                , this);
    }


    @OnClick({R.id.layout_clear, R.id.layout_about, R.id.layout_device, R.id.iv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_clear:
                ToastUtil.toast(this, "清除完成");
                break;
            case R.id.layout_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.layout_device:
                break;
            case R.id.iv_exit:
                new HintDialog(this).setTitleString(getString(R.string.APP_NAME))
                        .setContentString(getString(R.string.txt_exit_sure))
                        .setEnterBtnTxt(getString(R.string.txt_exit))
                        .setEnterSelect(true)
                        .setOnEnterClickListener(this::signOut)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case SIGN_OUT:
                exit();
                break;
            default:
                break;
        }
    }

}
