package com.cn.lv.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.NormImage;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.PicturePathBean;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.data.bean.VipDetailsBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.dialog.HintDialog;
import com.cn.frame.widgets.gridview.AutoGridView;
import com.cn.frame.widgets.menu.MenuItem;
import com.cn.frame.widgets.menu.TopRightMenu;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.ImagePreviewActivity;
import com.cn.lv.ui.main.my.AuthActivity;
import com.cn.lv.ui.main.my.ReportActivity;
import com.cn.lv.ui.main.my.VipActivity;
import com.cn.lv.utils.ImageUrlUtil;

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
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_life)
    TextView tvLife;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.iv_verify)
    ImageView ivVerify;
    @BindView(R.id.layout)
    RelativeLayout layout;

    private UserInfoBean userDetailBean;
    private String userId;
    private ArrayList<NormImage> images = new ArrayList<>();
    private ArrayList<NormImage> publicImages = new ArrayList<>();

    private ArrayList<PicturePathBean> paths;

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
            userId = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
        }
        gridViewPrivate.updateImg(images, false, false);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getUserInfo();
    }

    @Override
    public void initListener() {
        super.initListener();
        gridViewPrivate.setOnItemClickListener((parent, view, position, id) -> {
            VipDetailsBean detailsBean = loginBean.getVipDetails();
            if (paths.get(position).getPicture_type() == 1 || (detailsBean != null && TextUtils.equals(detailsBean.getUsage_state(), "2"))) {
                //查看大图
                Intent intent = new Intent(UserInfoActivity.this, ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.INTENT_URLS, publicImages);
                intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
            } else {
                HintDialog dialog = new HintDialog(UserInfoActivity.this);
                dialog.setTitleString("提示")
                        .setContentString("升级会员享受更好的服务")
                        .setCancelableAndTouch(false).setCancelBtnGone(true)
                        .setEnterBtnTxt("升级会员").setOnEnterClickListener(() -> {
                    startActivity(new Intent(UserInfoActivity.this, VipActivity.class));
                    finish();
                }).show();
            }
        });
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
            menuItems.add(new MenuItem(0, "取消屏蔽"));
        } else {
            menuItems.add(new MenuItem(0, "屏蔽用户"));
        }

        menuItems.add(new MenuItem(0, "举报TA"));
        mTopRightMenu.setHeight(BaseUtils.dp2px(this, 130)).addMenuList(menuItems).setOnMenuItemClickListener(this).showAsDropDown(ivMenu, -BaseUtils.dp2px(this, 94), 10);
    }

    @OnClick({R.id.tv_message, R.id.tv_follow, R.id.iv_menu})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_message:
                if (userInfo.getIs_auth() == 1) {
                    startActivity(new Intent(this, AuthActivity.class));
                } else {
                    //设置当前用户信息
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(String.valueOf(userDetailBean.getUser_id()), userDetailBean.getNickname(), Uri.parse(ImageUrlUtil.addTokenToUrl(userDetailBean.getHead_portrait()))));
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_TITLE, userDetailBean.getNickname());
                    intent.putExtra(CommonData.KEY_CHAT_ID,
                            String.valueOf(userDetailBean.getUser_id()));
                    startActivity(intent);
                }
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
        } else {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra(CommonData.KEY_PUBLIC, userId);
            startActivity(intent);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_USER_INFO) {
            userDetailBean = (UserInfoBean) response.getData();
            bindData();
        } else if (task == Tasks.SHIELD_USER) {
            isBlack = !isBlack;
            ToastUtil.toast(this, response.getMsg());
        }
    }

    private void bindData() {
        Glide.with(this).load(ImageUrlUtil.addTokenToUrl(userDetailBean.getHead_portrait())).apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(this), 4))).into(ivHeader);
        tvName.setText(userDetailBean.getNickname());
        if (userDetailBean.getIs_auth() == 1) {
            ivVerify.setVisibility(View.GONE);
        } else {
            ivVerify.setVisibility(View.VISIBLE);
        }
        int vip = userDetailBean.getUsage_state();
        if (vip == BASE_ONE) {
            layout.setBackground(null);
            ivVip.setVisibility(View.GONE);
            tvName.setSelected(false);
        } else {
            layout.setBackgroundResource(R.drawable.corner5_211d1d_bg);
            ivVip.setVisibility(View.VISIBLE);
            tvName.setSelected(true);
        }

        tvAge.setText(userDetailBean.getAge() + "");
        tvSign.setText(userDetailBean.getIndividuality_signature());
        tvCity.setText(userDetailBean.getAddress());
        if (userDetailBean.getAttribute() == 2) {
            tvLocation.setText(SweetApplication.getInstance().getCity());
        } else {
            tvLocation.setText(userDetailBean.getLocation());
        }
        tvAddress.setText(userDetailBean.getAddress());
        tvHeight.setText(userDetailBean.getHeight() + "");
        try {
            tvJob.setText(dataDictBean.getOccupationInfo().get(userDetailBean.getOccupation()));
            tvLife.setText(dataDictBean.getLifeStyle().get(userDetailBean.getLife_style()));
            tvMoney.setText(dataDictBean.getIncome().get(userDetailBean.getNet_assets()));
            tvIncome.setText(dataDictBean.getIncome().get(userDetailBean.getAnnual_income()));
            tvWho.setText(dataDictBean.getContactObject().get(userDetailBean.getContact_object()));
            tvBodyType.setText(dataDictBean.getSomatotype().get(userDetailBean.getSomatotype()));
            tvRace.setText(dataDictBean.getRace().get(userDetailBean.getRace()));
            tvEducation.setText(dataDictBean.getEducation().get(userDetailBean.getEducation()));
            tvMarriage.setText(dataDictBean.getMarriage().get(userDetailBean.getMarriage()));
            tvChild.setText(dataDictBean.getChildren().get(userDetailBean.getChildren()));
            tvSmoke.setText(dataDictBean.getSmokeOrDrink().get(userDetailBean.getSmoke()));
            tvDrink.setText(dataDictBean.getSmokeOrDrink().get(userDetailBean.getDrink()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int state = userDetailBean.getFollow_state();
        if (state == BASE_ONE) {
            tvFollow.setSelected(true);
        } else {
            tvFollow.setSelected(false);
        }
        paths = userDetailBean.getAlbum();
        if (paths != null && paths.size() > 0) {
            for (PicturePathBean path : paths) {
                NormImage normImage = new NormImage();
                normImage.setImageUrl(ImageUrlUtil.addTokenToUrl(path.getPicture_path()));
                VipDetailsBean detailsBean = loginBean.getVipDetails();
                if (path.getPicture_type() == 1) {
                    publicImages.add(normImage);
                } else {
                    //vip
                    if (detailsBean != null && TextUtils.equals(detailsBean.getUsage_state(),
                            "2")) {
                        publicImages.add(normImage);
                    } else {
                        normImage.setHide(true);
                    }
                }
                images.add(normImage);
            }
            gridViewPrivate.updateImg(images, false, false);
        }

        isBlack = (userDetailBean.getShield_state() == 1);
    }

}
