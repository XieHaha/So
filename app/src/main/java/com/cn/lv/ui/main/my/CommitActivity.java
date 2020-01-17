package com.cn.lv.ui.main.my;

import android.text.TextUtils;
import android.widget.EditText;

import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.ToastUtil;
import com.cn.lv.R;

import butterknife.BindView;
import butterknife.OnClick;

public class CommitActivity extends BaseActivity {
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;

    private String title, content;

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
        return R.layout.act_commit;
    }

    @OnClick(R.id.iv_commit)
    public void onViewClicked() {
        title = etTitle.getText().toString().trim();
        content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            ToastUtil.toast(this, "内容不能为空");
            return;
        }
        commit();
    }

    /**
     * 提交
     */
    private void commit() {
        RequestUtils.questionFeedback(this, signSession(InterfaceName.QUESTION_FEEDBACK), title,
                content, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.QUESTION_FEEDBACK) {
            ToastUtil.toast(this, response.getMsg());
            finish();
        }
    }
}
