package com.ming.weidushop.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.utils.Logger;
import com.bumptech.glide.Glide;
import com.ming.weidushop.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/11
 */
public class GridViewAddImageAdapter extends BaseAdapter {
    private List<Map<String, Object>> mDatas=new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    /**
     * 可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
     * 默认5张
     */
    private int maxImages = 5;

    public GridViewAddImageAdapter(List<Map<String, Object>> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 获取最大上传张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     * @param maxImages
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    /**
     * 让GridView中的数据数目加1最后一个显示+号
     *
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {
        int count = mDatas == null ? 1 : mDatas.size() + 1;
        if (count >= maxImages) {
            return mDatas.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<Map<String, Object>> mDatas) {
        this.mDatas = mDatas;
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_published_grida, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.ivimage.getLayoutParams();
        params.topMargin = Util.dip2px(mContext, 10);
        if (mDatas != null && position < mDatas.size()) {
            final File file = new File(mDatas.get(position).get("path").toString());
            Logger.i("File",file.getPath());
            params.height = Util.dip2px(mContext, 80);
            params.topMargin = 0;
            Glide.with(mContext).load(file).into(viewHolder.ivimage);
            viewHolder.btdel.setVisibility(View.VISIBLE);///storage/emulated/0/IMG_20190917_173329.png
            viewHolder.btdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        file.delete();
                    }
                    if (mOnDeleteListener != null) {
                        mOnDeleteListener.delete(position);
                    }
                    mDatas.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            params.height = Util.dip2px(mContext, 60);
            params.topMargin = Util.dip2px(mContext, 10);
            Glide.with(mContext).load(R.drawable.comment_pictrue).into(viewHolder.ivimage);
            viewHolder.ivimage.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.btdel.setVisibility(View.GONE);
        }
        viewHolder.ivimage.setLayoutParams(params);


        return convertView;

    }

    public class ViewHolder {
        ImageView ivimage;
        Button btdel;
        View root;

        public ViewHolder(View root) {
            ivimage = (ImageView) root.findViewById(R.id.iv_image);
            btdel = (Button) root.findViewById(R.id.bt_del);
            this.root = root;
        }
    }

    private OnDeleteListener mOnDeleteListener;

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }

    public interface OnDeleteListener {
        void delete(int position);
    }
}
