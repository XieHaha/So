package com.cn.lv.ui.main;

import android.content.Intent;
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
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.gridview.AutoGridView;
import com.cn.lv.R;
import com.cn.lv.utils.FileUrlUtil;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.iv_header)
    ImageView ivHeader;
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

    @OnClick({R.id.tv_message, R.id.tv_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_message:
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
                Intent intent = new Intent();
                intent.putExtra(CommonData.KEY_PUBLIC, follow);
                setResult(RESULT_OK, intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_USER_INFO) {
            userDetailBean = (UserDetailBean) response.getData();
            bindData();
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
