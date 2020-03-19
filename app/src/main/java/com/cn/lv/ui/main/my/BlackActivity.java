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
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.RolesBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.widgets.loadview.CustomLoadMoreView;
import com.cn.lv.R;
import com.cn.lv.ui.adapter.NearbyAdapter;
import com.cn.lv.ui.main.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BlackActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.tv_none_message)
    TextView tvNoneMessage;
    private NearbyAdapter nearbyAdapter;

    private BaseListData<RolesBean> baseListData;
    private List<RolesBean> rolesBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_black;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
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

    /**
     * 获取数据
     */
    private void getData() {
        if (BaseUtils.isNetworkAvailable(this)) {
            RequestUtils.shieldList(this, signSession(InterfaceName.SHIELD_LIST), page, PAGE_SIZE,
                    this);
            tvNoneMessage.setVisibility(View.GONE);
        } else {
            tvNoneMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        nearbyAdapter = new NearbyAdapter(R.layout.item_black, rolesBeans);
        nearbyAdapter.setLoadMoreView(new CustomLoadMoreView());
        nearbyAdapter.setOnLoadMoreListener(this, recyclerView);
        nearbyAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(nearbyAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, rolesBeans.get(position).getUser_id());
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.SHIELD_LIST) {
            baseListData = (BaseListData<RolesBean>) response.getData();
            List<RolesBean> list = baseListData.getData();
            if (page == BaseData.BASE_ONE) {
                rolesBeans.clear();
            }
            rolesBeans.addAll(list);
            nearbyAdapter.setNewData(rolesBeans);
            if (list != null && list.size() >= BaseData.PAGE_SIZE) {
                nearbyAdapter.loadMoreComplete();
            } else {
                nearbyAdapter.loadMoreEnd();
            }
            if (rolesBeans != null && rolesBeans.size() > 0) {
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }

}
