package com.cn.lv.ui.main.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseListData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.HelpBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.widgets.loadview.CustomLoadMoreView;
import com.cn.lv.R;
import com.cn.lv.ui.adapter.HelpAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.tv_none_message)
    TextView tvNoneMessage;

    private HelpAdapter helpAdapter;
    private BaseListData<HelpBean> baseListData;
    private List<HelpBean> helpBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;

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
        return R.layout.act_help;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(this)) {
            helpsList();
            tvNoneMessage.setVisibility(View.GONE);
        } else {
            tvNoneMessage.setVisibility(View.VISIBLE);
        }
    }

    private void helpsList() {
        RequestUtils.helpsList(this, signSession(InterfaceName.HELPS_LIST), page,
                BaseData.PAGE_SIZE, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        helpAdapter = new HelpAdapter(R.layout.item_help, helpBeans);
        helpAdapter.setLoadMoreView(new CustomLoadMoreView());
        helpAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(helpAdapter);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.HELPS_LIST) {
            baseListData = (BaseListData<HelpBean>) response.getData();
            ArrayList<HelpBean> list = baseListData.getData();
            if (page == BaseData.BASE_ONE) {
                helpBeans.clear();
            }
            helpBeans.addAll(list);
            helpAdapter.setNewData(helpBeans);
            if (list != null && list.size() >= BaseData.PAGE_SIZE) {
                helpAdapter.loadMoreComplete();
            } else {
                helpAdapter.loadMoreEnd();
            }
            if (helpBeans != null && helpBeans.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                tvNoneMessage.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                tvNoneMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = 1;
        helpsList();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        helpsList();
    }

    @OnClick(R.id.iv_need)
    public void onViewClicked() {
        startActivity(new Intent(this, CommitActivity.class));
    }
}
