package com.ming.weidushop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.model.Permission;
import com.abner.ming.base.net.HttpUtils;
import com.abner.ming.base.utils.Logger;
import com.abner.ming.base.utils.SharedPreUtils;
import com.abner.ming.base.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.InFoPictrueBean;
import com.ming.weidushop.media.MediaUtils;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.utils.PermissionUtil;
import com.ming.weidushop.utils.PhotoFromPhotoAlbum;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/3
 * 我的资料
 */
public class MyInfoActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView mInfoNick;
    private ImageView mInfoPic;
    private RelativeLayout mLayoutChange;
    private EditText mEtUserName;
    private Button mLoginOut;
    private String mNickName;

    @Override
    protected void initData() {
        setShowTitle(false);
        setTitle("我的资料");
        isShowBack(true);

        LoginBean.ResultBean infoBean = AppUtils.getUserInFo();
        if (infoBean != null) {
            String nick = SharedPreUtils.getString(MyInfoActivity.this, "nick");
            if (!TextUtils.isEmpty(nick)) {
                infoBean.setNickName(nick);
            }
            String head = SharedPreUtils.getString(MyInfoActivity.this, "head");
            if (!TextUtils.isEmpty(head)) {
                infoBean.setHeadPic(head);
            }
            AppUtils.setGlideCircle(this, infoBean.getHeadPic(), mInfoPic);
            mInfoNick.setText(infoBean.getNickName());
            mEtUserName.setText(infoBean.getNickName());
            mEtUserName.setSelection(mEtUserName.length());//将光标移至文字末尾

        }
    }

    @Override
    protected void initView() {
        mInfoNick = (TextView) get(R.id.info_nickname);
        mInfoPic = (ImageView) get(R.id.info_pic);
        mLayoutChange = (RelativeLayout) get(R.id.layout_change_nick);
        get(R.id.layout_click_nick).setOnClickListener(this);
        get(R.id.iv_delete).setOnClickListener(this);
        setWindowTitleBlack(true);
        mLoginOut = (Button) get(R.id.login_out);
        get(R.id.login_out).setOnClickListener(this);
        get(R.id.layout_pictrue).setOnClickListener(this);
        get(R.id.change_name).setOnClickListener(this);
        mEtUserName = (EditText) get(R.id.et_user_name);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_click_nick:
                mLayoutChange.setVisibility(View.VISIBLE);
                mLoginOut.setVisibility(View.GONE);
                break;
            case R.id.login_out:
                SharedPreUtils.put(MyInfoActivity.this, "nick", "");
                SharedPreUtils.put(MyInfoActivity.this, "head", "");
                SharedPreUtils.put(MyInfoActivity.this, "islogin", "0");
                Utils.loginOut();
                finish();
                break;
            case R.id.iv_delete:
                mLayoutChange.setVisibility(View.GONE);
                mLoginOut.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_pictrue://点击修改头像
                boolean isCamera = PermissionUtil.hasPermission(MyInfoActivity.this, Permission.CAMERA);
                boolean isSd = PermissionUtil.hasPermission(MyInfoActivity.this, Permission.READ_EXTERNAL_STORAGE);
                if (!isCamera || !isSd) {
                    String[] strings = {
                            Permission.CAMERA,
                            Permission.READ_EXTERNAL_STORAGE,
                            Permission.WRITE_EXTERNAL_STORAGE
                    };
                    PermissionUtil.requestPermissions(MyInfoActivity.this, strings, 1000);
                } else {
                    new MediaUtils(MyInfoActivity.this).showDialog();
                }
                break;
            case R.id.change_name://修改昵称
                String s = mEtUserName.getText().toString();
                changeNickName(s);
                break;
        }
    }


    private void changeNickName(String s) {
        mNickName = s;
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("nickName", s);
        net(true, false, AppBean.class).put(0, Api.CHANGE_NICK_URL, map);
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            AppBean appBean = (AppBean) o;
            toast(appBean.getMessage());
            if ("0000".equals(appBean.getStatus())) {
                mLayoutChange.setVisibility(View.GONE);
                mLoginOut.setVisibility(View.VISIBLE);
                mInfoNick.setText(mNickName);
                mEtUserName.setText(mNickName);
                SharedPreUtils.put(MyInfoActivity.this, "nick", mNickName);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == MediaUtils.CHOOSE_PHOTO && resultCode == RESULT_OK) {
                String photoPath = PhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                upload(new File(photoPath));
            } else {
                if (data != null) {
                    Bitmap bm = new MediaUtils(this).getBitmapFormUri(data.getData());
                    SaveImage(bm);
                } else {
                    File pictrueFile = new MediaUtils(this).getPictrueFile();
                    if (pictrueFile != null) {
                        upload(pictrueFile);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //保存图片
    public void SaveImage(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + "/path/" + MediaUtils.getPhotoFileName() + ".jpg");
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            upload(file);
        }
    }

    //上传
    private void upload(File file) {
        Map<String, String> headMap = new HashMap<>();
        LoginBean.ResultBean bean = Utils.getUserInFo();
        if (bean != null) {
            headMap.put("userId", String.valueOf(bean.getUserId()));
            headMap.put("sessionId", bean.getSessionId());
        }
        new HttpUtils()
                .setContext(this)
                .isShowLoading(true)
                .upload(Api.CHANGE_PICTRUE, headMap, file.getAbsolutePath())
                .result(new HttpUtils.HttpListener() {
                    @Override
                    public void success(String data) {
                        try {
                            InFoPictrueBean appBean = new Gson().fromJson(data, InFoPictrueBean.class);
                            if ("0000".equals(appBean.getStatus())) {
                                toast("修改成功");
                                String headPath = appBean.getHeadPath();
                                SharedPreUtils.put(MyInfoActivity.this, "head", headPath);
                                AppUtils.setGlideCircle(MyInfoActivity.this, headPath, mInfoPic);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void fail(String error) {
                        Logger.i("error", error);
                    }
                });
    }


}
