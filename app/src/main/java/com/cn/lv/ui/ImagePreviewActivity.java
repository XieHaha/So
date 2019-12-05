package com.cn.lv.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cn.frame.data.bean.NormImage;
import com.cn.frame.widgets.imagepreview.transformer.ImageTransformer;
import com.cn.frame.widgets.imagepreview.utils.NavigatorPageIndex;
import com.cn.frame.widgets.imagepreview.view.ImageLoadingView;
import com.cn.frame.widgets.imagepreview.view.ImagePreviewView;
import com.cn.lv.R;
import com.cn.lv.utils.FileUrlUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author dundun
 */
public class ImagePreviewActivity extends Activity implements ViewPager.OnPageChangeListener {
    public static final String INTENT_URLS = "intent_urls";
    public static final String INTENT_POSITION = "intent_position";
    /**
     * 图片对象
     */
    private ArrayList<NormImage> urls;
    private ArrayList<ImagePreviewView> imgPreViews = new ArrayList<>();
    /**
     * 图片加载动画view
     */
    private ImageLoadingView mLoadingView;
    /**
     * 页面游标
     */
    private NavigatorPageIndex indicator;
    /**
     * 当前page 游标
     */
    private int currentIndex;
    /**
     * 图片加载  开始
     */
    private static final int LOAD_START = 0;
    /**
     * 图片加载  失败
     */
    private static final int LOAD_ERROR = 100;
    /**
     * 图片加载  成功
     */
    private static final int LOAD_SUCCESS = 200;
    /**
     * 图片加载  取消
     */
    private static final int LOAD_CANCEL = 300;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_START:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_ERROR:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case LOAD_SUCCESS:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case LOAD_CANCEL:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_image_view);
        Intent intent = getIntent();
        if (intent != null) {
            urls = (ArrayList<NormImage>)intent.getSerializableExtra(INTENT_URLS);
            currentIndex = intent.getIntExtra(INTENT_POSITION, 0);
        }
        mLoadingView = findViewById(R.id.act_image_view_loading);
        //图片游标
        indicator = findViewById(R.id.act_image_view_page_indicator);
        indicator.initPageIndex(urls.size());
        //当只有一张图，不显示图片游标
        if (urls.size() == 1) {
            indicator.setVisibility(View.GONE);
        }
        ViewPager imgViewPager = findViewById(R.id.act_image_view_viewpager);
        //滑动效果
        imgViewPager.setPageTransformer(true, new ImageTransformer());
        imgViewPager.addOnPageChangeListener(this);
        imgViewPager.setAdapter(new TouchImageAdapter());
        imgViewPager.setCurrentItem(currentIndex);
        if (imgPreViews.size() > 0) {
            imgPreViews.clear();
        }
        for (int i = 0; i < urls.size(); i++) {
            imgPreViews.add(i, new ImagePreviewView(this, mHandler));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        indicator.changePageIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class TouchImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return urls.size();
        }

        @NotNull
        @Override
        public View instantiateItem(@NotNull final ViewGroup container, final int position) {
            ImagePreviewView currentPreviewView = imgPreViews.get(position);
            //                        currentPreviewView.loadingImageAsync(urls.get(position).getImagePath(), urls.get(position).getImageUrl(),
            //                                                             position);
            currentPreviewView.loadingImageAsync(urls.get(position).getImagePath(),
                                                 FileUrlUtil.addTokenToUrl(urls.get(position).getImageUrl()));
            container.addView(currentPreviewView, LinearLayout.LayoutParams.MATCH_PARENT,
                              LinearLayout.LayoutParams.MATCH_PARENT);
            return currentPreviewView;
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
            return view == object;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.keep, R.anim.fade_out);
    }
}
