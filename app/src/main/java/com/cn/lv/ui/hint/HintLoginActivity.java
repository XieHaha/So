package com.cn.lv.ui.hint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.cn.frame.api.LitePalHelper;
import com.cn.frame.data.CommonData;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.SharePreferenceUtil;
import com.cn.lv.R;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dundun
 * @date 15/12/31
 */
public class HintLoginActivity extends BaseActivity {
    @BindView(R.id.dialog_simple_hint_content)
    TextView dialogSimpleHintContent;
    private String errorHint;

    @Override
    public int getLayoutID() {
        return R.layout.act_hint;
    }

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            errorHint = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        dialogSimpleHintContent.setText(errorHint);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        //清除本地数据
        SharePreferenceUtil.clear(this);
        //清除数据库数据
        LitePal.deleteDatabase(LitePalHelper.DATA_BASE_NAME);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.keep, R.anim.anim_fade_out);
    }

    @OnClick(R.id.dialog_simple_hint_enter)
    public void onViewClicked() {
        exit();
    }
}
