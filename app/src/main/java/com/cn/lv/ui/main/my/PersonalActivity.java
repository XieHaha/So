package com.cn.lv.ui.main.my;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.frame.data.NormImage;
import com.cn.frame.permission.Permission;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.glide.GlideHelper;
import com.cn.frame.widgets.dialog.DownDialog;
import com.cn.frame.widgets.dialog.listener.OnMediaItemClickListener;
import com.cn.frame.widgets.gridview.AutoGridView;
import com.cn.lv.R;
import com.cn.lv.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends BaseActivity implements OnMediaItemClickListener {
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_age)
    TextView tvAge;
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
    TextView tvHeight;
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

    private int age, address, who, life, money, income, height, job, bodyType, race,
            education, marriage, child, smoke, drink;

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

    @OnClick({R.id.iv_header, R.id.iv_add, R.id.layout_age, R.id.layout_address, R.id.layout_who,
            R.id.layout_life, R.id.layout_money, R.id.layout_income, R.id.layout_height,
            R.id.layout_job, R.id.layout_body_type, R.id.layout_race, R.id.layout_education,
            R.id.layout_marriage, R.id.layout_child, R.id.layout_smoke, R.id.layout_drink,
            R.id.iv_next})
    public void onViewClicked(View view) {
        List<String> data;
        switch (view.getId()) {
            case R.id.iv_header:
            case R.id.iv_add:
                imageType = BASE_ONE;
                permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});
                break;
            case R.id.layout_age:
                data = Arrays.asList(getResources().getStringArray(R.array.age));
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_age, this).show();
                break;
            case R.id.layout_address:
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
                break;
            case R.id.layout_income:
                data = dataDictBean.getIncome();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_income, this).show();
                break;
            case R.id.layout_height:
                break;
            case R.id.layout_job:
                data = dataDictBean.getOccupationInfo();
                new DownDialog(this).setData(data)
                        .setOnMediaItemClickListener(R.id.layout_job, this).show();
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
                break;
            default:
                break;
        }
    }

    @Override
    public void onMediaItemClick(int type, int value) {
        switch (type) {
            case R.id.layout_age:
                age = value;
                break;
            case R.id.layout_address:

                break;
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
                break;
            case R.id.layout_income:
                income = value;
                tvIncome.setText(dataDictBean.getIncome().get(income));
                break;
            case R.id.layout_height:
                height = value;
                break;
            case R.id.layout_job:
                job = value;
                tvJob.setText(dataDictBean.getOccupationInfo().get(job));
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
