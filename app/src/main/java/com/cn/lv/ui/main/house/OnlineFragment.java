package com.cn.lv.ui.main.house;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.frame.api.notify.NotifyChangeListenerManager;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseListData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.RolesBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.loadview.CustomLoadMoreView;
import com.cn.lv.R;
import com.cn.lv.ui.adapter.OnlineAdapter;
import com.cn.lv.ui.main.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnlineFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener {
    private static final int REQUEST_CODE_FOLLOW = 100;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.tv_none_message)
    TextView tvNoneMessage;
    private OnlineAdapter onlineAdapter;

    private BaseListData<RolesBean> baseListData;
    private List<RolesBean> rolesBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_nearby;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(getContext())) {
            getData(true);
            tvNoneMessage.setVisibility(View.GONE);
        } else {
            tvNoneMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取数据
     */
    private void getData(boolean show) {
        RequestUtils.getHomeInfo(getContext(), signSession(InterfaceName.HOME_INFO), "on_line",
                userInfo, page, PAGE_SIZE, show, this);
    }

    /**
     * 关注
     */
    private void renewCollection(int userId, int state) {
        RequestUtils.renewCollection(getContext(), signSession(InterfaceName.RENEW_COLLECTION),
                userId, state, this);
    }


    /**
     * 适配器处理
     */
    private void initAdapter() {
        onlineAdapter = new OnlineAdapter(R.layout.item_roles, rolesBeans);
        onlineAdapter.setLoadMoreView(new CustomLoadMoreView());
        onlineAdapter.setOnLoadMoreListener(this, recyclerView);
        onlineAdapter.setOnItemClickListener(this);
        onlineAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(onlineAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        curRolesBean = rolesBeans.get(position);
        curPosition = position;
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, rolesBeans.get(position).getUser_id());
        startActivityForResult(intent, REQUEST_CODE_FOLLOW);
    }

    private RolesBean curRolesBean;
    private int curPosition;

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        curRolesBean = rolesBeans.get(position);
        curPosition = position;
        int state;
        if (curRolesBean.getCollection_state() == BASE_ONE) {
            state = 2;
        } else {
            state = 1;
        }
        updateFollow(state);
    }

    private void updateFollow(int state) {
        renewCollection(curRolesBean.getUser_id(), state);
        //本地更新
        curRolesBean.setCollection_state(state);
        onlineAdapter.notifyItemChanged(curPosition);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_HOME_INFO:
                baseListData = (BaseListData<RolesBean>) response.getData();
                List<RolesBean> list = baseListData.getData();
                if (page == BaseData.BASE_ONE) {
                    rolesBeans.clear();
                }
                rolesBeans.addAll(list);
                onlineAdapter.setNewData(rolesBeans);
                if (list != null && list.size() >= BaseData.PAGE_SIZE) {
                    onlineAdapter.loadMoreComplete();
                } else {
                    onlineAdapter.loadMoreEnd();
                }
                if (rolesBeans != null && rolesBeans.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoneMessage.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvNoneMessage.setVisibility(View.VISIBLE);
                }
                break;
            case RENEW_COLLECTION:
                ToastUtil.toast(getContext(), response.getMsg());
                NotifyChangeListenerManager.getInstance().notifyFollowListChange("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOLLOW) {
                int state = data.getIntExtra(CommonData.KEY_PUBLIC, 2);
                updateFollow(state);
            }
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getData(false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getData(false);
    }

}
