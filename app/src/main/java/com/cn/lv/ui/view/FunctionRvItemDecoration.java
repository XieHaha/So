package com.cn.lv.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 格子分割线
 */
public class FunctionRvItemDecoration extends RecyclerView.ItemDecoration {
    private int horizontalSpacing;
    private int verticalSpacing;

    public FunctionRvItemDecoration(int horizontalSpacing, int verticalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = (horizontalSpacing / 2);
        outRect.right = (horizontalSpacing / 2);

        outRect.bottom = verticalSpacing;

    }

}
