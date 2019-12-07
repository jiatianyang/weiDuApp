package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.refresh.material.Util;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.AddAddressActivity;
import com.ming.weidushop.bean.AddressBean;
import com.ming.weidushop.utils.AppUtils;

/**
 * author:AbnerMing
 * date:2019/9/9
 */
public class AddressAdapter extends BaseRecyclerAdapter<AddressBean.ResultBean> {
    public AddressAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_address;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, final AddressBean.ResultBean resultBean) {
        baseViewHolder.setText(R.id.address_name, resultBean.getRealName());
        baseViewHolder.setText(R.id.address_phone, resultBean.getPhone());
        baseViewHolder.setText(R.id.tv_all_address, resultBean.getAddress());
        ImageView imageView = (ImageView) baseViewHolder.get(R.id.iv_cricle);
        TextView mTvCricle = (TextView) baseViewHolder.get(R.id.tv_cricle);
        if (resultBean.getWhetherDefault() == 1) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.shop_car_all));
            baseViewHolder.setText(R.id.tv_cricle, "当前地址");
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.shop_car_cricle));
            baseViewHolder.setText(R.id.tv_cricle, "设为默认地址");
        }
        click(resultBean, imageView, 0);
        click(resultBean, mTvCricle, 0);
        baseViewHolder.get(R.id.address_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startObject(context, AddAddressActivity.class, resultBean);
            }
        });
        final TextView mTextDelete = (TextView) baseViewHolder.get(R.id.address_delete);
        baseViewHolder.get(R.id.address_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(resultBean, mTextDelete, 1);
            }
        });

        LinearLayout layoutAddress = (LinearLayout) baseViewHolder.get(R.id.layout_address);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutAddress.getLayoutParams();
        if (position == getList().size() - 1) {
            params.bottomMargin = Util.dip2px(context, 100);
        } else {
            params.bottomMargin = Util.dip2px(context, 0);
        }
        layoutAddress.setLayoutParams(params);

    }

    private int position;

    @Override
    public void createPosition(int i) {
        super.createPosition(i);
        position = i;
    }

    private void click(final AddressBean.ResultBean resultBean, View view, final int type) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.itemClick(resultBean.getId(), type, position);
                }
            }
        });
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void itemClick(int id, int type, int position);
    }
}
