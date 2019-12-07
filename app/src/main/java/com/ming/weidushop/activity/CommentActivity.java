package com.ming.weidushop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.model.Permission;
import com.abner.ming.base.net.HttpUtils;
import com.abner.ming.base.utils.Logger;
import com.abner.ming.base.utils.ToastUtils;
import com.abner.ming.base.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.OrderAllBean;
import com.ming.weidushop.media.GridViewAddImageAdapter;
import com.ming.weidushop.media.MediaUtils;
import com.ming.weidushop.utils.PermissionUtil;
import com.ming.weidushop.utils.PhotoFromPhotoAlbum;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/10
 * 评论
 */
public class CommentActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private String mOrderId;
    private int mCommodityId;
    private SimpleDraweeView mSimpleDraweeView;
    private TextView mTitle,mPrice;
    private EditText mCommentContent;
    private GridViewAddImageAdapter mGridViewAddImageAdapter;
    private GridView mCommentGridView;
    private boolean mIsCircle = true;
    private ImageView mCommentCircle;

    @Override
    protected void initData() {
        OrderAllBean.OrderListBean.DetailListBean bean =
                (OrderAllBean.OrderListBean.DetailListBean) getIntent().getSerializableExtra("object");
        if (bean != null) {
            mOrderId = bean.getOrderId();
            mCommodityId = bean.getCommodityId();
            mSimpleDraweeView.setImageURI(bean.getCommodityPic().split(",")[0]);
            mTitle.setText(bean.getCommodityName());
            mPrice.setText("¥" + bean.getCommodityPrice());

        }

        mGridViewAddImageAdapter = new GridViewAddImageAdapter(datas, this);
        mGridViewAddImageAdapter.setMaxImages(7);
        mCommentGridView.setAdapter(mGridViewAddImageAdapter);
        mCommentGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isCamera = PermissionUtil.hasPermission(CommentActivity.this, Permission.CAMERA);
                boolean isRead = PermissionUtil.hasPermission(CommentActivity.this, Permission.READ_EXTERNAL_STORAGE);
                if (!isCamera || !isRead) {
                    String[] strings = {
                            Permission.CAMERA,
                            Permission.READ_EXTERNAL_STORAGE,
                            Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.MOUNT_UNMOUNT_FILESYSTEMS
                    };
                    PermissionUtil.requestPermissions(CommentActivity.this, strings, 1000);
                } else {
                    new MediaUtils(CommentActivity.this).showDialog();
                }
            }
        });

        mGridViewAddImageAdapter.setOnDeleteListener(new GridViewAddImageAdapter.OnDeleteListener() {
            @Override
            public void delete(int position) {
                mPictruePath.remove(position);
            }
        });

    }

    @Override
    protected void initView() {
        setShowTitle(false);
        setTitle("商品评价");
        isShowBack(true);
        setWindowTitleBlack(true);
        mSimpleDraweeView = (SimpleDraweeView) get(R.id.order_image);
        mTitle = (TextView) get(R.id.order_title);
        mPrice = (TextView) get(R.id.order_price);
        mCommentContent = (EditText) get(R.id.comment_content);
        mCommentGridView = (GridView) get(R.id.comment_gridview);
        mCommentCircle = (ImageView) get(R.id.comment_circle);
        get(R.id.send).setOnClickListener(this);
        get(R.id.layout_comment_circle).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send://发表
                String s = mCommentContent.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    toast("请输入您的评价内容");
                    return;
                }
                doComment(s);
                break;
            case R.id.layout_comment_circle://是否同步到圈子
                if (mIsCircle) {
                    mCommentCircle.setBackgroundResource(R.drawable.shop_car_all);
                    mIsCircle = false;
                } else {
                    mCommentCircle.setBackgroundResource(R.drawable.shop_car_cricle);
                    mIsCircle = true;
                }
                break;

        }
    }

    //评论圈子
    private void doCommentCircle(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("commodityId", String.valueOf(mCommodityId));
        map.put("content", s);
        Map<String, String> headMap = new HashMap<>();
        LoginBean.ResultBean bean = Utils.getUserInFo();
        if (bean != null) {
            headMap.put("userId", String.valueOf(bean.getUserId()));
            headMap.put("sessionId", bean.getSessionId());
        }
        new HttpUtils().isShowLoading(true).setContext(this).setHead(headMap).
                uploadMorePic(Api.CIRCLE_COMMENT_URL, map, mPictruePath)
                .result(new HttpUtils.HttpListener() {
                    @Override
                    public void success(String data) {
                        AppBean appBean = new Gson().fromJson(data, AppBean.class);
                        toast(appBean.getMessage());
                        if ("0000".equals(appBean.getStatus())) {
                            finish();
                        }
                    }

                    @Override
                    public void fail(String error) {

                    }
                });
    }


    private void doComment(final String s) {
        if (mPictruePath.isEmpty()) {
            ToastUtils.show("请最少选择一张图片");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("commodityId", String.valueOf(mCommodityId));
        map.put("orderId", mOrderId);
        map.put("content", s);
        Map<String, String> headMap = new HashMap<>();
        LoginBean.ResultBean bean = Utils.getUserInFo();
        if (bean != null) {
            headMap.put("userId", String.valueOf(bean.getUserId()));
            headMap.put("sessionId", bean.getSessionId());
        }

        new HttpUtils().isShowLoading(true).setContext(this).setHead(headMap).
                uploadMorePic(Api.COMMENT_URL, map, mPictruePath)
                .result(new HttpUtils.HttpListener() {
                    @Override
                    public void success(String data) {
                        AppBean appBean = new Gson().fromJson(data, AppBean.class);
                        if (!mIsCircle) {
                            if ("0000".equals(appBean.getStatus())) {
                                doCommentCircle(s);
                            }
                        } else {
                            toast(appBean.getMessage());
                            if ("0000".equals(appBean.getStatus())) {
                                finish();
                            }

                        }
                        Logger.i("CommentActivity", data);
                    }

                    @Override
                    public void fail(String error) {
                        Logger.i("CommentActivity", error);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == MediaUtils.CHOOSE_PHOTO && resultCode == RESULT_OK) {
                String photoPath = PhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                setPath(photoPath);

            } else {
                if (data != null) {
                    Bitmap bm = new MediaUtils(this).getBitmapFormUri(data.getData());
                    saveImage(bm);
                } else {
                    File pictrueFile = new MediaUtils(this).getPictrueFile();
                    if (pictrueFile != null) {
                        setPath(pictrueFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置路径
    private void setPath(String path){
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        mPictruePath.add(path);
        datas.add(map);
        mGridViewAddImageAdapter.notifyDataSetChanged(datas);
    }


    private List<Map<String, Object>> datas = new ArrayList<>();
    private List<String> mPictruePath = new ArrayList<>();


    //保存图片
    public void saveImage(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + "/path/" + MediaUtils.getPhotoFileName());
        Map<String, Object> map = new HashMap<>();
        map.put("path", file.getAbsolutePath());
        mPictruePath.add(file.getAbsolutePath());
        datas.add(map);
        mGridViewAddImageAdapter.notifyDataSetChanged(datas);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
