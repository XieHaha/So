package com.cn.frame.widgets.dialog.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.frame.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des
 */
public class SingleTextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int curPosition = -1;

    public SingleTextAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.tv_single_txt);
        textView.setText(item);
        if (curPosition == helper.getAdapterPosition()) {
            textView.setSelected(true);
        } else {
            textView.setSelected(false);
        }
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }
}
