package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.refresh.material.Util;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.AddressBean;

/**
 * author:AbnerMing
 * date:2019/9/15
 */
public class SubmitOrderAddressAdapter extends BaseRecyclerAdapter<AddressBean.ResultBean> {
    public SubmitOrderAddressAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_order_address_list;
    }

    @Override
    public void bindViewDataPosition(BaseViewHolder baseViewHolder, final AddressBean.ResultBean resultBean, final int i) {
        super.bindViewDataPosition(baseViewHolder, resultBean, i);
        TextView mName = (TextView) baseViewHolder.get(R.id.order_name);
        mName.setText(resultBean.getRealName());
        mName.setTextColor(context.getResources().getColor(R.color.colorFFF));

        TextView mPhone = (TextView) baseViewHolder.get(R.id.order_phone);
        mPhone.setText(resultBean.getPhone());
        mPhone.setTextColor(context.getResources().getColor(R.color.colorFFF));

        TextView mAddress = (TextView) baseViewHolder.get(R.id.order_address);
        mAddress.setText(resultBean.getAddress());
        mAddress.setTextColor(context.getResources().getColor(R.color.colorFFF));
        baseViewHolder.get(R.id.address_down).setVisibility(View.GONE);
        baseViewHolder.get(R.id.order_line).setVisibility(View.VISIBLE);
        baseViewHolder.get(R.id.layout_selected).setVisibility(View.VISIBLE);
        RelativeLayout mAddressLayout = (RelativeLayout) baseViewHolder.get(R.id.address_layout);
        mAddressLayout.setBackgroundResource(R.color.color757);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAddressLayout.getLayoutParams();
        params.height = Util.dip2px(context, 80);
        params.topMargin = 0;
        params.bottomMargin = 0;
        mAddressLayout.setLayoutParams(params);

        TextView mTextSelected = (TextView) baseViewHolder.get(R.id.layout_selected_tv);
        if (resultBean.getWhetherDefault() == 1) {
            mTextSelected.setText("当前");
        } else {
            mTextSelected.setText("选择");
        }
        baseViewHolder.get(R.id.layout_selected).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < getList().size(); j++) {
                    if (j == i) {
                        getList().get(j).setWhetherDefault(1);
                    } else {
                        getList().get(j).setWhetherDefault(2);
                    }
                }
                notifyDataSetChanged();
                if (mOnSelectedDataListener != null) {
                    mOnSelectedDataListener.selectedData(resultBean);
                }
            }
        });


    }

    private OnSelectedDataListener mOnSelectedDataListener;

    public void setOnSelectedDataListener(OnSelectedDataListener mOnSelectedDataListener) {
        this.mOnSelectedDataListener = mOnSelectedDataListener;
    }

    public interface OnSelectedDataListener {
        void selectedData(AddressBean.ResultBean resultBean);
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, AddressBean.ResultBean resultBean) {

    }
}
