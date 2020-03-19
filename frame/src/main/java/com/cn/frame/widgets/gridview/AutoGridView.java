package com.cn.frame.widgets.gridview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cn.frame.R;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.NormImage;
import com.cn.frame.utils.BaseUtils;
import com.cn.frame.utils.glide.GlideHelper;

import java.util.ArrayList;

/**
 * @author dundun
 * @date 16/10/21
 */
public class AutoGridView extends RelativeLayout {
    private GridView gridView;
    private int gvWidth, gvHeight, imgWidth, imgSpace, imgPadding, numColumns = 4;
    private ArrayList<NormImage> images;
    private Context context;
    /**
     * 是否显示添加按钮
     */
    private boolean isAdd;
    /**
     * 是否显示删除按钮
     */
    private boolean isShowDelete;
    /**
     * 是否显示数量统计
     */
    private boolean showNum;
    /**
     * 最大数
     */
    private int maxTotal;

    public AutoGridView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AutoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        imgWidth = getResources().getDimensionPixelSize(R.dimen.image_width);
        imgSpace = getResources().getDimensionPixelSize(R.dimen.image_space);
        imgPadding = getResources().getDimensionPixelSize(R.dimen.image_padding);
        gvWidth = numColumns * imgWidth + (numColumns + 1) * imgSpace;
        images = new ArrayList<>();
        gridView = new CustomGridView(getContext());
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setNumColumns(numColumns);
        gridView.setPadding(imgPadding, 0, imgPadding, 0);
        gridView.setVerticalSpacing(imgSpace);
        updateImg(images, true, true);
        addView(gridView);
    }

    public void initGridView() {
        if (gridView != null) {
            int rows;
            int size = images.size();
            if (size <= numColumns) {
                rows = 1;
            } else if (size <= (numColumns + numColumns)) {
                rows = 2;
            } else {
                rows = 3;
            }
            gvHeight = rows * imgWidth + (rows + 1) * imgSpace;
            gridView.setLayoutParams(new LayoutParams(gvWidth, gvHeight));
            gridView.invalidate();
        }
    }

    public void setShowNum(boolean showNum, int maxTotal) {
        this.maxTotal = maxTotal;
        this.showNum = showNum;
    }

    public void updateImg(ArrayList<NormImage> bitmaps, boolean showDelete, boolean isAdd) {
        this.isAdd = isAdd;
        this.isShowDelete = showDelete;
        images.clear();
        images.addAll(bitmaps);
        if (bitmaps.size() < BaseData.BASE_IMAGE_SIZE_MAX && isAdd) {
            images.add(new NormImage());
        }
        initGridView();
        ImgAdapter adapter = new ImgAdapter();
        gridView.setAdapter(adapter);
        gridView.invalidate();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        gridView.setOnItemLongClickListener(listener);
    }

    private class ImgAdapter extends BaseAdapter {
        private ViewHolder holder;

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_add_image_two,
                        parent, false);
                holder.imageView = convertView.findViewById(R.id.image);
                holder.ivUpload = convertView.findViewById(R.id.iv_upload);
                holder.ivDelete = convertView.findViewById(R.id.iv_delete);
                holder.lookLayout = convertView.findViewById(R.id.look_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NormImage normImage = images.get(position);
            String url = normImage.getImageUrl();
            String path = normImage.getImagePath();
            if ((!TextUtils.isEmpty(url) || !TextUtils.isEmpty(path)) && isShowDelete) {
                holder.ivDelete.setVisibility(VISIBLE);
                holder.ivDelete.setOnClickListener(v -> {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                });
            } else {
                holder.ivDelete.setVisibility(GONE);
            }
            if (isAdd) {
                holder.imageView.setVisibility(VISIBLE);
            } else {
                holder.imageView.setVisibility(GONE);
            }
            if (normImage.isHide()) {
                holder.lookLayout.setVisibility(VISIBLE);
            } else {
                holder.lookLayout.setVisibility(GONE);
            }

            String value;
            if (TextUtils.isEmpty(url)) {
                value = path;
            } else {
                value = url;
            }
            Glide.with(context)
                    .load(value)
                    .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(context, 4)))
                    .into(holder.ivUpload);
            return convertView;
        }
    }

    private class ViewHolder {
        private ImageView imageView, ivUpload, ivDelete;
        private RelativeLayout lookLayout;
    }

    public interface OnDeleteClickListener {
        /**
         * 删除
         */
        void onDeleteClick(int position);
    }

    private OnDeleteClickListener onDeleteClickListener;
}
