package com.cn.lv.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.NormImage;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.PicturePathBean;
import com.cn.frame.data.bean.UserDetailBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.gridview.AutoGridView;
import com.cn.frame.widgets.menu.MenuItem;
import com.cn.frame.widgets.menu.TopRightMenu;
import com.cn.lv.R;
import com.cn.lv.utils.FileUrlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class UserInfoActivity extends BaseActivity implements TopRightMenu.OnMenuItemClickListener {
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_who)
    TextView tvWho;
    @BindView(R.id.grid_view_private)
    AutoGridView gridViewPrivate;
    @BindView(R.id.tv_body_type)
    TextView tvBodyType;
    @BindView(R.id.tv_race)
    TextView tvRace;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.tv_marriage)
    TextView tvMarriage;
    @BindView(R.id.tv_child)
    TextView tvChild;
    @BindView(R.id.tv_smoke)
    TextView tvSmoke;
    @BindView(R.id.tv_drink)
    TextView tvDrink;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_follow)
    TextView tvFollow;

    private UserDetailBean userDetailBean;
    private int userId;
    private ArrayList<NormImage> images = new ArrayList<>();

    /**
     * 是否已屏蔽
     */
    private boolean isBlack;

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
        return R.layout.act_user_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            userId = getIntent().getIntExtra(CommonData.KEY_PUBLIC, 0);
            isBlack = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        gridViewPrivate.updateImg(images, false);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getUserInfo();
    }

    private void getUserInfo() {
        RequestUtils.getUserInfo(this, signSession(InterfaceName.GET_USER_INFO), userId, this);
    }

    private void shieldUser(int state) {
        RequestUtils.shieldUser(this, signSession(InterfaceName.SHIELD_USER), userId, state, this);
    }

    private void initMenu() {
        TopRightMenu mTopRightMenu = new TopRightMenu(this);
        List<MenuItem> menuItems = new ArrayList<>();
        if (isBlack) {
            menuItems.add(new MenuItem(R.mipmap.ic_blacklist, "取消屏蔽"));
        } else {
            menuItems.add(new MenuItem(R.mipmap.ic_blacklist, "屏蔽用户"));
        }
        mTopRightMenu.setHeight(BaseUtils.dp2px(this, 70)).addMenuList(menuItems).setOnMenuItemClickListener(this).showAsDropDown(ivMenu, -BaseUtils.dp2px(this, 124), 10);
    }

    @OnClick({R.id.tv_message, R.id.tv_follow, R.id.iv_menu})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_message:
                //设置当前用户信息
                RongIM.getInstance().setCurrentUserInfo(new UserInfo(String.valueOf(userDetailBean.getUser_id()), userDetailBean.getNickname(), Uri.parse(FileUrlUtil.addTokenToUrl(userDetailBean.getHead_portrait()))));
                intent = new Intent(this, ChatActivity.class);
                intent.putExtra(CommonData.KEY_CHAT_TITLE, userDetailBean.getNickname());
                intent.putExtra(CommonData.KEY_CHAT_ID,
                        String.valueOf(userDetailBean.getUser_id()));
                startActivity(intent);
                break;
            case R.id.tv_follow:
                int follow;
                if (tvFollow.isSelected()) {
                    tvFollow.setSelected(false);
                    follow = 2;
                } else {
                    tvFollow.setSelected(true);
                    follow = 1;

                }
                intent = new Intent();
                intent.putExtra(CommonData.KEY_PUBLIC, follow);
                setResult(RESULT_OK, intent);
                break;
            case R.id.iv_menu:
                initMenu();
                break;
            default:
                break;
        }
    }

    /**
     * 菜单
     */
    @Override
    public void onMenuItemClick(int position) {
        if (position == BASE_ZERO) {
            if (isBlack) {
                shieldUser(2);
            } else {
                shieldUser(1);
            }
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_USER_INFO) {
            userDetailBean = (UserDetailBean) response.getData();
            bindData();
        } else if (task == Tasks.SHIELD_USER) {
            ToastUtil.toast(this, response.getMsg());
        }
    }

    private void bindData() {
        Glide.with(this).load(FileUrlUtil.addTokenToUrl(userDetailBean.getHead_portrait())).apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(this), 4))).into(ivHeader);
        tvName.setText(userDetailBean.getNickname());
        tvSign.setText(userDetailBean.getIndividuality_signature());
        tvCity.setText(userDetailBean.getAddress());
        tvWho.setText(dataDictBean.getContactObject().get(userDetailBean.getContact_object()));
        tvBodyType.setText(dataDictBean.getSomatotype().get(userDetailBean.getSomatotype()));
        tvRace.setText(dataDictBean.getRace().get(userDetailBean.getRace()));
        tvEducation.setText(dataDictBean.getEducation().get(userDetailBean.getEducation()));
        tvMarriage.setText(dataDictBean.getMarriage().get(userDetailBean.getMarriage()));
        tvChild.setText(dataDictBean.getChildren().get(userDetailBean.getChildren()));
        tvSmoke.setText(dataDictBean.getSmokeOrDrink().get(userDetailBean.getSmoke()));
        tvDrink.setText(dataDictBean.getSmokeOrDrink().get(userDetailBean.getDrink()));
        int state = userDetailBean.getFollow_state();
        if (state == BASE_ONE) {
            tvFollow.setSelected(true);
        } else {
            tvFollow.setSelected(false);
        }
        ArrayList<PicturePathBean> paths = userDetailBean.getAlbum();
        if (paths != null && paths.size() > 0) {
            for (PicturePathBean path : paths) {
                NormImage normImage = new NormImage();
                normImage.setImageUrl(FileUrlUtil.addTokenToUrl(path.getPicture_path()));
                images.add(normImage);
            }
            gridViewPrivate.updateImg(images, false);
        }
    }
}
