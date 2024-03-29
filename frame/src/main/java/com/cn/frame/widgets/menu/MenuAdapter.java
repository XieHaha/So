package com.cn.frame.widgets.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.frame.R;

import java.util.List;

/**
 * Author：Bro0cL on 2016/12/26.
 *
 * @author dundun
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private Context mContext;
    private List<MenuItem> menuItemList;
    private boolean showIcon;
    private TopRightMenu mTopRightMenu;
    private TopRightMenu.OnMenuItemClickListener onMenuItemClickListener;

    public MenuAdapter(Context context, TopRightMenu topRightMenu, List<MenuItem> menuItemList, boolean show) {
        this.mContext = context;
        this.mTopRightMenu = topRightMenu;
        this.menuItemList = menuItemList;
        this.showIcon = show;
    }

    public void setData(List<MenuItem> data) {
        menuItemList = data;
        notifyDataSetChanged();
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        notifyDataSetChanged();
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trm_popup_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        final MenuItem menuItem = menuItemList.get(position);
        if (showIcon) {
            holder.icon.setVisibility(View.VISIBLE);
            int resId = menuItem.getIcon();
            holder.icon.setImageResource(resId < 0 ? 0 : resId);
        }
        else {
            holder.icon.setVisibility(View.GONE);
        }
        holder.text.setText(menuItem.getText());
        if (position == 0) {
            holder.container.setBackground(addStateDrawable(mContext, -1, R.mipmap.trm_popup_top_pressed));
        }
        else if (position == menuItemList.size() - 1) {
            holder.container.setBackground(addStateDrawable(mContext, -1, R.mipmap.trm_popup_bottom_pressed));
        }
        else {
            holder.container.setBackground(addStateDrawable(mContext, -1, R.mipmap.trm_popup_middle_pressed));
        }
        final int pos = holder.getAdapterPosition();
        holder.container.setOnClickListener(v -> {
            if (onMenuItemClickListener != null) {
                mTopRightMenu.dismiss();
                onMenuItemClickListener.onMenuItemClick(pos);
            }
        });
    }

    private StateListDrawable addStateDrawable(Context context, int normalId, int pressedId) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = normalId == -1 ? null : context.getResources().getDrawable(normalId);
        Drawable pressed = pressedId == -1 ? null : context.getResources().getDrawable(pressedId);
        sd.addState(new int[] { android.R.attr.state_pressed }, pressed);
        sd.addState(new int[] {}, normal);
        return sd;
    }

    @Override
    public int getItemCount() {
        return menuItemList == null ? 0 : menuItemList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        ViewGroup container;
        ImageView icon;
        TextView text;

        MenuViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup)itemView;
            icon = itemView.findViewById(R.id.trm_menu_item_icon);
            text = itemView.findViewById(R.id.trm_menu_item_text);
        }
    }

    public void setOnMenuItemClickListener(TopRightMenu.OnMenuItemClickListener listener) {
        this.onMenuItemClickListener = listener;
    }
}
