package com.ming.weidushop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/2
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private Context mContext;
    private List<AccountBean> mAccountBeanList = new ArrayList<>();

    public AccountAdapter(Context context, List<AccountBean> mAccountBeanList) {
        this.mContext = context;
        this.mAccountBeanList = mAccountBeanList;
    }

    @NonNull
    @Override
    public AccountAdapter.AccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.account_item, null);
        AccountViewHolder accountViewHolder = new AccountViewHolder(view);
        return accountViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.AccountViewHolder accountViewHolder, final int i) {
        accountViewHolder.accountDesc.setText(mAccountBeanList.get(i).getName());
        accountViewHolder.accountImage.setImageResource(mAccountBeanList.get(i).getImage());
        accountViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.itemClick(i);
                }
            }
        });
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }

    @Override
    public int getItemCount() {
        return mAccountBeanList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView accountImage;
        TextView accountDesc;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            accountImage = (SimpleDraweeView) itemView.findViewById(R.id.account_image);
            accountDesc = (TextView) itemView.findViewById(R.id.account_desc);
        }
    }
}
