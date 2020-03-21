package com.cn.lv.ui.main.attention;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.frame.api.ApiManager;
import com.cn.frame.api.notify.IChange;
import com.cn.frame.api.notify.NotifyChangeListenerManager;
import com.cn.frame.api.notify.RegisterType;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.BaseListData;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.FollowNumBean;
import com.cn.frame.data.bean.RolesBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseFragment;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.widgets.loadview.CustomLoadMoreView;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.adapter.FollowAdapter;
import com.cn.lv.ui.main.ChatActivity;
import com.cn.lv.ui.main.UserInfoActivity;
import com.cn.lv.ui.main.my.UpActivity;
import com.cn.lv.ui.view.FunctionRvItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class IFollowFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener, IChange<String> {
    private static final int REQUEST_CODE_FOLLOW = 100;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.tv_none_message)
    TextView tvNoneMessage;
    private FollowAdapter followAdapter;

    private BaseListData<RolesBean> baseListData;
    private List<RolesBean> rolesBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;

    @Override
    public void onChange(String data) {
        getData();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_follow;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new FunctionRvItemDecoration(0, 0));
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        if (BaseUtils.isNetworkAvailable(getContext())) {
            getData();
            tvNoneMessage.setVisibility(View.GONE);
        } else {
            tvNoneMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        iNotifyChangeListenerServer.registerFollowList(this, RegisterType.REGISTER);
    }

    /**
     * 获取数据
     */
    private void getData() {
        RequestUtils.collectionList(getContext(), signSession(InterfaceName.COLLECTION_LIST), 2,
                page, PAGE_SIZE, this);
    }

    /**
     * 关注
     */
    private void renewCollection(String userId, int state) {
        RequestUtils.renewCollection(getContext(), signSession(InterfaceName.RENEW_COLLECTION),
                userId, state, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        followAdapter = new FollowAdapter(R.layout.item_follow_roles, rolesBeans);
        followAdapter.setLoadMoreView(new CustomLoadMoreView());
        followAdapter.setOnLoadMoreListener(this, recyclerView);
        followAdapter.setOnItemClickListener(this);
        followAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(followAdapter);
    }

    private RolesBean curRolesBean;

    private void updateFollow(int state) {
        renewCollection(curRolesBean.getUser_id(), state);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        curRolesBean = rolesBeans.get(position);
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, rolesBeans.get(position).getUser_id());
        startActivityForResult(intent, REQUEST_CODE_FOLLOW);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        RolesBean bean = rolesBeans.get(position);
        switch (view.getId()) {
            case R.id.iv_attention:
                renewCollection(bean.getUser_id(), 2);
                break;
            case R.id.iv_message:
                if (userInfo.getIs_auth() == 1) {
                    startActivity(new Intent(getContext(), UpActivity.class));
                } else {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_TITLE, bean.getNickname());
                    intent.putExtra(CommonData.KEY_CHAT_ID, bean.getRong_cloud_user_id());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case COLLECTION_LIST:
                baseListData = (BaseListData<RolesBean>) response.getData();
                List<RolesBean> list = baseListData.getData();
                if (page == BaseData.BASE_ONE) {
                    rolesBeans.clear();
                }
                rolesBeans.addAll(list);
                followAdapter.setNewData(rolesBeans);
                if (list != null && list.size() >= BaseData.PAGE_SIZE) {
                    followAdapter.loadMoreComplete();
                } else {
                    followAdapter.setEnableLoadMore(false);
                }
                if (rolesBeans != null && rolesBeans.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoneMessage.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvNoneMessage.setVisibility(View.VISIBLE);
                }
                userInfo.setAttention_num(rolesBeans.size());
                loginBean.setUserInfo(userInfo);
                SweetApplication.getInstance().setLoginBean(loginBean);
                NotifyChangeListenerManager.getInstance().notifyFollowNumChanged("");
                break;
            case RENEW_COLLECTION:
                ToastUtil.toast(getContext(), response.getMsg());
                //更新数据
                getData();
                //更新关注数
                FollowNumBean numBean = (FollowNumBean) response.getData();
                userInfo.setAttention_num(numBean.getAttention_num());
                loginBean.setUserInfo(userInfo);
                SweetApplication.getInstance().setLoginBean(loginBean);
                NotifyChangeListenerManager.getInstance().notifyFollowNumChanged("");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        iNotifyChangeListenerServer.registerFollowList(this, RegisterType.UNREGISTER);
    }
}
