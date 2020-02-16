package com.cn.lv.ui.main.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.frame.api.ThreadPoolHelper;
import com.cn.frame.data.BaseResponse;
import com.cn.frame.data.CommonData;
import com.cn.frame.data.NormImage;
import com.cn.frame.data.Tasks;
import com.cn.frame.data.bean.CityBean;
import com.cn.frame.data.bean.PaymentBean;
import com.cn.frame.data.bean.ProvinceBean;
import com.cn.frame.data.bean.UserInfoBean;
import com.cn.frame.http.InterfaceName;
import com.cn.frame.http.retrofit.RequestUtils;
import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.SweetLog;
import com.cn.frame.utils.ToastUtil;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.dialog.DownDialog;
import com.cn.frame.widgets.dialog.listener.OnMediaItemClickListener;
import com.cn.frame.widgets.gridview.AutoGridView;
import com.cn.lv.R;
import com.cn.lv.SweetApplication;
import com.cn.lv.ui.WebViewActivity;
import com.cn.lv.utils.FileUtils;
import com.cn.lv.utils.ImageUrlUtil;
import com.cn.lv.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends BaseActivity implements OnMediaItemClickListener {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_age)
    EditText tvAge;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_who)
    TextView tvWho;
    @BindView(R.id.tv_life)
    TextView tvLife;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_height)
    EditText tvHeight;
    @BindView(R.id.tv_job)
    TextView tvJob;
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
    @BindView(R.id.grid_view_public)
    AutoGridView gridViewPublic;
    @BindView(R.id.grid_view_private)
    AutoGridView gridViewPrivate;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private File headerFile;
    private ArrayList<NormImage> publicPaths = new ArrayList<>();
    private ArrayList<NormImage> privatePaths = new ArrayList<>();
    /**
     * 公开照片
     */
    private ArrayList<File> publicFiles = new ArrayList<>();
    /**
     * 私密照片
     */
    private ArrayList<File> privateFiles = new ArrayList<>();

    private ArrayList<ProvinceBean> provinceBeans = new ArrayList<>();
    private ArrayList<String> provinceNames = new ArrayList<>();
    private ArrayList<CityBean> cityBeans = new ArrayList<>();
    private ArrayList<String> cityNames = new ArrayList<>();

    private String name, introduction, purpose, address;
    private int age, who, life, money, income, height, weight, job, bodyType, race, education,
            marriage, child, smoke, drink, addressPro, addressCity;

    /**
     * 图片类型
     */
    private int imageType;

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
        return R.layout.act_personal;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getUserInfo();
        getProvinceData();
    }

    @Override
    public void initListener() {
        super.initListener();
        gridViewPublic.setOnItemClickListener((parent, view, position, id) -> {
            imageType = BASE_TWO;
            permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});

        });
        gridViewPrivate.setOnItemClickListener((parent, view, position, id) -> {
            imageType = BASE_THREE;
            permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});
        });

    }

    private void bindData(UserInfoBean data) {
        if (!TextUtils.isEmpty(data.getNickname())) {
            etName.setText(data.getNickname());
            etName.setSelection(data.getNickname().length());
        }
        int status = data.getIs_auth();
        if (status == BASE_ONE) {
            ivNext.setVisibility(View.VISIBLE);
            tvNext.setVisibility(View.GONE);
        } else {
            tvNext.setVisibility(View.VISIBLE);
            ivNext.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(data.getHead_portrait())) {
            ivAdd.setVisibility(View.VISIBLE);
        } else {
            ivAdd.setVisibility(View.GONE);
            Glide.with(this).load(ImageUrlUtil.addTokenToUrl(data.getHead_portrait())).apply(GlideHelper.getOptionsPic()).into(ivHeader);
        }
        //赋值
        name = data.getNickname();
        ThreadPoolHelper.getInstance().execInSingle(() -> {
            headerFile =
                    FileUtils.getFileByUrl(ImageUrlUtil.addTokenToUrl(data.getHead_portrait()));
            SweetLog.i(TAG, "file:" + headerFile);
        });
        height = data.getHeight();
        weight = data.getWeight();
        bodyType = data.getSomatotype();
        race = data.getRace();
        education = data.getEducation();
        marriage = data.getMarriage();
        child = data.getChildren();
        smoke = data.getSmoke();
        drink = data.getDrink();
        income = data.getAnnual_income();
        money = data.getNet_assets();
        life = data.getLife_style();
        who = data.getContact_object();
        purpose = data.getMaking_friends_goals();
        introduction = data.getIndividuality_signature();

        tvHeight.setText(String.valueOf(height));
        tvAge.setText(String.valueOf(age));
        etTitle.setText(introduction);
        etContent.setText(purpose);
        tvWho.setText(dataDictBean.getBeInterestedIn().get(who));
        tvLife.setText(dataDictBean.getLifeStyle().get(life));
        tvMoney.setText(dataDictBean.getIncome().get(money));
        tvIncome.setText(dataDictBean.getIncome().get(income));
        tvJob.setText(dataDictBean.getOccupationInfo().get(job));
        tvAddress.setText(data.getAddress());
        tvBodyType.setText(dataDictBean.getSomatotype().get(bodyType));
        tvRace.setText(dataDictBean.getRace().get(race));
        tvEducation.setText(dataDictBean.getEducation().get(education));
        tvMarriage.setText(dataDictBean.getMarriage().get(marriage));
        tvChild.setText(dataDictBean.getChildren().get(child));
        tvSmoke.setText(dataDictBean.getSmokeOrDrink().get(smoke));
        tvDrink.setText(dataDictBean.getSmokeOrDrink().get(drink));
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        RequestUtils.getUserInfo(this, signSession(InterfaceName.GET_USER_INFO),
                userInfo.getUser_id(), this);
    }

    /**
     * 获取省信息
     */
    private void getProvinceData() {
        RequestUtils.getProvinceData(this, signSession(InterfaceName.GET_PROVINCE_INFO), this);
    }

    /**
     * 获取市信息
     */
    private void getCityData(int parentId) {
        RequestUtils.getCityData(this, signSession(InterfaceName.GET_CITY_INFO), parentId, this);
    }

    private void auth() {
        RequestUtils.auth(this, signSession(InterfaceName.AUTH), name, headerFile, age, height, 0
                , bodyType, race, education, marriage, child, smoke, drink,
                userInfo.getBe_interested_in(), income, money, life, who, purpose, introduction,
                publicFiles, privateFiles, this);
    }

    private void edit() {
        RequestUtils.edit(this, signSession(InterfaceName.EDIT_USER_INFO), name, headerFile, age,
                height, 0, bodyType, race, education, marriage, child, smoke, drink,
                userInfo.getBe_interested_in(), income, money, life, who, purpose, introduction,
                publicFiles, privateFiles, this);
    }

    @OnClick({R.id.iv_header, R.id.iv_add, R.id.layout_who, R.id.layout_life, R.id.layout_money,
            R.id.layout_income, R.id.layout_job, R.id.layout_body_type, R.id.layout_race,
            R.id.layout_address, R.id.layout_education, R.id.layout_marriage, R.id.layout_child,
            R.id.layout_smoke, R.id.layout_drink, R.id.iv_next, R.id.tv_next})
    public void onViewClicked(View view) {
        List<String> data;
        switch (view.getId()) {
            case R.id.iv_header:
            case R.id.iv_add:
                imageType = BASE_ONE;
                permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});
                break;
            case R.id.layout_who:
                data = dataDictBean.getBeInterestedIn();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_who, this).show();
                break;
            case R.id.layout_life:
                data = dataDictBean.getLifeStyle();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_life, this).show();
                break;
            case R.id.layout_money:
                data = dataDictBean.getIncome();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_money, this).show();
                break;
            case R.id.layout_income:
                data = dataDictBean.getIncome();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_income, this).show();
                break;
            case R.id.layout_job:
                data = dataDictBean.getOccupationInfo();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_job, this).show();
                break;
            case R.id.layout_address:
                new DownDialog(this).setData(provinceNames)
                        .setOnMediaItemClickListener(R.id.layout_address, this).show();
                break;
            case R.id.layout_body_type:
                data = dataDictBean.getSomatotype();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_body_type, this).show();
                break;
            case R.id.layout_race:
                data = dataDictBean.getRace();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_race, this).show();
                break;
            case R.id.layout_education:
                data = dataDictBean.getEducation();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_education, this).show();
                break;
            case R.id.layout_marriage:
                data = dataDictBean.getMarriage();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_marriage, this).show();
                break;
            case R.id.layout_child:
                data = dataDictBean.getChildren();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_child, this).show();
                break;
            case R.id.layout_smoke:
                data = dataDictBean.getSmokeOrDrink();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_smoke, this).show();
                break;
            case R.id.layout_drink:
                data = dataDictBean.getSmokeOrDrink();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_drink, this).show();
                break;
            case R.id.iv_next:
                //认证
                commit(1);
                break;
            case R.id.tv_next:
                //编辑资料
                commit(2);
                break;
            default:
                break;
        }
    }

    /**
     * 提交
     */
    private void commit(int type) {
        if (headerFile == null) {
            ToastUtil.toast(this, "头像不能为空");
            return;
        }
        name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.toast(this, "昵称不能为空");
            return;
        }
        introduction = etTitle.getText().toString().trim();
        if (TextUtils.isEmpty(introduction)) {
            ToastUtil.toast(this, "自我介绍不能为空");
            return;
        }
        purpose = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(purpose)) {
            ToastUtil.toast(this, "交友目的不能为空");
            return;
        }
        String ageStr = tvAge.getText().toString().trim();
        if (TextUtils.isEmpty(ageStr)) {
            ToastUtil.toast(this, "年龄不能为空");
            return;
        } else {
            age = Integer.valueOf(ageStr);
        }
        String str = tvHeight.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtil.toast(this, "身高不能为空");
            return;
        } else {
            height = Integer.valueOf(str);
        }
        switch (type) {
            case BASE_ONE:
                auth();
                break;
            case BASE_TWO:
                edit();
                break;
            default:
                break;
        }
    }


    @Override
    public void onMediaItemClick(int type, int value) {
        switch (type) {
            case R.id.layout_who:
                who = value;
                tvWho.setText(dataDictBean.getBeInterestedIn().get(who));
                break;
            case R.id.layout_life:
                life = value;
                tvLife.setText(dataDictBean.getLifeStyle().get(life));
                break;
            case R.id.layout_money:
                money = value;
                tvMoney.setText(dataDictBean.getIncome().get(money));
                break;
            case R.id.layout_income:
                income = value;
                tvIncome.setText(dataDictBean.getIncome().get(income));
                break;
            case R.id.layout_job:
                job = value;
                tvJob.setText(dataDictBean.getOccupationInfo().get(job));
                break;
            case R.id.layout_address:
                addressPro = value;
                getCityData(provinceBeans.get(value).getId());
                break;
            case R.id.text:
                addressCity = value;
                tvAddress.setText(provinceNames.get(addressPro) + cityNames.get(addressCity));
                break;
            case R.id.layout_body_type:
                bodyType = value;
                tvBodyType.setText(dataDictBean.getSomatotype().get(bodyType));
                break;
            case R.id.layout_race:
                race = value;
                tvRace.setText(dataDictBean.getRace().get(race));
                break;
            case R.id.layout_education:
                education = value;
                tvEducation.setText(dataDictBean.getEducation().get(education));
                break;
            case R.id.layout_marriage:
                marriage = value;
                tvMarriage.setText(dataDictBean.getMarriage().get(marriage));
                break;
            case R.id.layout_child:
                child = value;
                tvChild.setText(dataDictBean.getChildren().get(child));
                break;
            case R.id.layout_smoke:
                smoke = value;
                tvSmoke.setText(dataDictBean.getSmokeOrDrink().get(smoke));
                break;
            case R.id.layout_drink:
                drink = value;
                tvDrink.setText(dataDictBean.getSmokeOrDrink().get(drink));
                break;
            default:
                break;
        }
    }


    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_USER_INFO:
                UserInfoBean data = (UserInfoBean) response.getData();
                bindData(data);
                break;
            case AUTH:
                userInfo.setIs_auth(2);
                userInfo.setNickname(name);
                userInfo.setIndividuality_signature(introduction);
                userInfo.setAge(age);
                loginBean.setUserInfo(userInfo);
                SweetApplication.getInstance().setLoginBean(loginBean);
                setResult(RESULT_OK);
                finish();
                break;
            case EDIT_USER_INFO:
                userInfo.setNickname(name);
                userInfo.setIndividuality_signature(introduction);
                userInfo.setAge(age);
                loginBean.setUserInfo(userInfo);
                SweetApplication.getInstance().setLoginBean(loginBean);
                setResult(RESULT_OK);
                finish();
                break;
            case GET_PROVINCE_INFO:
                provinceBeans = (ArrayList<ProvinceBean>) response.getData();
                provinceNames.clear();
                for (ProvinceBean bean : provinceBeans) {
                    provinceNames.add(bean.getName());
                }
                break;
            case GET_CITY_INFO:
                cityBeans = (ArrayList<CityBean>) response.getData();
                cityNames.clear();
                for (CityBean bean : cityBeans) {
                    cityNames.add(bean.getName());
                }
                new DownDialog(this).setData(cityNames)
                        .setOnMediaItemClickListener(R.id.text, this).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        if (response.getCode() == 202) {
            PaymentBean paymentBean = (PaymentBean) response.getData();
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(CommonData.KEY_PUBLIC, paymentBean.getPayment_address());
            intent.putExtra(CommonData.KEY_TITLE, "认证");
            startActivity(intent);
            //            Uri uri = Uri.parse(paymentBean.getPayment_address());
            //            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        List<String> paths;
        if (requestCode == RC_PICK_IMG) {
            paths = Matisse.obtainPathResult(data);
            switch (imageType) {
                case BASE_ONE:
                    if (paths != null && paths.size() > 0) {
                        ivAdd.setVisibility(View.GONE);
                        headerFile = new File(paths.get(0));
                        Glide.with(this).load(paths.get(0)).apply(GlideHelper.getOptionsPic()).into(ivHeader);
                    }
                    break;
                case BASE_TWO:
                    if (paths != null && paths.size() > 0) {
                        for (String path : paths) {
                            publicFiles.add(new File(path));
                            NormImage normImage = new NormImage();
                            normImage.setImageUrl(path);
                            publicPaths.add(normImage);
                        }
                        boolean isAdd = false;
                        if (publicPaths.size() < 4) {
                            isAdd = true;
                        }
                        gridViewPublic.updateImg(publicPaths, isAdd);
                    }
                    break;
                case BASE_THREE:
                    if (paths != null && paths.size() > 0) {
                        for (String path : paths) {
                            privateFiles.add(new File(path));
                            NormImage normImage = new NormImage();
                            normImage.setImageUrl(path);
                            privatePaths.add(normImage);
                        }
                        boolean isAdd = false;
                        if (privatePaths.size() < 4) {
                            isAdd = true;
                        }
                        gridViewPrivate.updateImg(privatePaths, isAdd);
                    }
                    break;
                default:
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[]) permissionName)[0])) {
                openPhoto();
            }
        }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        switch (imageType) {
            case BASE_ONE:
                MatisseUtils.open(this, true, 1);
                break;
            case BASE_TWO:
                MatisseUtils.open(this, true, 4 - publicFiles.size());
                break;
            case BASE_THREE:
                MatisseUtils.open(this, true, 4 - privateFiles.size());
                break;
            default:
                break;
        }
    }

}
