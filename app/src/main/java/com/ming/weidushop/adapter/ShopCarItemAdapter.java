package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.ShopCarBean;
import com.ming.weidushop.view.ShopAddView;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/6
 */
public class ShopCarItemAdapter extends BaseRecyclerAdapter<ShopCarBean.ResultBean.ShoppingCartListBean> {
    private Context context;
    private int mPosition;

    public ShopCarItemAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_shopcar_item;
    }

    @Override
    public void bindViewDataPosition(BaseViewHolder baseViewHolder, final ShopCarBean.ResultBean.ShoppingCartListBean shoppingCartListBean, final int i) {
        super.bindViewDataPosition(baseViewHolder, shoppingCartListBean, i);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) baseViewHolder.get(R.id.shop_car_image);
        simpleDraweeView.setImageURI(shoppingCartListBean.getPic());
        baseViewHolder.setText(R.id.shop_car_title, shoppingCartListBean.getCommodityName());
        baseViewHolder.setText(R.id.shop_car_price, "¥" + shoppingCartListBean.getPrice());
        ShopAddView shopAddView = (ShopAddView) baseViewHolder.get(R.id.shop_add);
        ImageView mCricle = (ImageView) baseViewHolder.get(R.id.iv_cricle);
        if (shoppingCartListBean.isSelected()) {
            mCricle.setImageDrawable(context.getResources().getDrawable(R.drawable.shop_car_all));
        } else {
            mCricle.setImageDrawable(context.getResources().getDrawable(R.drawable.shop_car_cricle));
        }

        //选中点击事件
        mCricle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCartListBean.setSelected(!shoppingCartListBean.isSelected());
                notifyItemChanged(i);
                if (mOnChangeDataListener != null) {
                    mOnChangeDataListener.changeData(getList());
                }
            }
        });
        shopAddView.setCount(shoppingCartListBean.getCount());
        shopAddView.setOnNumListener(new ShopAddView.OnNumListener() {
            @Override
            public void count(int count) {
                shoppingCartListBean.setCount(count);
                if (mOnChangeDataListener != null) {
                    mOnChangeDataListener.changeData(getList());
                }
            }
        });
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, final ShopCarBean.ResultBean.ShoppingCartListBean shoppingCartListBean) {

    }

    private OnChangeDataListener mOnChangeDataListener;

    public void setOnChangeDataListener(OnChangeDataListener mOnChangeDataListener) {
        this.mOnChangeDataListener = mOnChangeDataListener;
    }

    public interface OnChangeDataListener {
        void changeData(List<ShopCarBean.ResultBean.ShoppingCartListBean> list);
    }

    @Override
    public void createPosition(int i) {
        super.createPosition(i);
        mPosition = i;
    }
}
