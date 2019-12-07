package com.ming.weidushop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.refresh.material.Util;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.ShopCarBean;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/6
 */
public class ShopCarAdapter extends BaseRecyclerAdapter<ShopCarBean.ResultBean> {
    private Context context;

    public ShopCarAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_shopcar;
    }



    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, ShopCarBean.ResultBean resultBean) {
        baseViewHolder.setText(R.id.shop_car_name, resultBean.getCategoryName());
        SwipeRecyclerView mRecyclerView = (SwipeRecyclerView) baseViewHolder.get(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ShopCarItemAdapter shopCarItemAdapter = new ShopCarItemAdapter(context);
        mRecyclerView.setAdapter(shopCarItemAdapter);
        shopCarItemAdapter.setList(resultBean.getShoppingCartList());

        shopCarItemAdapter.setOnChangeDataListener(new ShopCarItemAdapter.OnChangeDataListener() {
            @Override
            public void changeData(List<ShopCarBean.ResultBean.ShoppingCartListBean> list) {
                List<ShopCarBean.ResultBean> list1 = getList();
                if (mOnChangeDataListener != null) {
                    mOnChangeDataListener.changeData(list1);
                }
            }
        });

//      mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //    mRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
    }

    private OnChangeDataListener mOnChangeDataListener;

    public void setOnChangeDataListener(OnChangeDataListener mOnChangeDataListener) {
        this.mOnChangeDataListener = mOnChangeDataListener;
    }

    public interface OnChangeDataListener {
        void changeData(List<ShopCarBean.ResultBean> list);
    }


    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = context.getResources().getDimensionPixelSize(R.dimen.dp_margin_50);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            SwipeMenuItem deleteItem = new SwipeMenuItem(context).setBackground(R.drawable.index_seach)
                    .setImage(R.drawable.index_seach)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(context, "list第" + position + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

}
