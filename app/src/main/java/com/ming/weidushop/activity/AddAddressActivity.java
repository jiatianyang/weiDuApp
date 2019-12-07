package com.ming.weidushop.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.utils.Utils;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.AddressBean;
import com.ming.weidushop.bean.JsonBean;
import com.ming.weidushop.utils.GetJsonDataUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/9
 * 添加收获地址
 */
public class AddAddressActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView mTvAddress, mAddress, mAddressAdd;
    private EditText mAddressName, mPhone, mAllAddress, mPostCode;
    private int mAddRessId = 0;
    private ArrayList<JsonBean> mOptions1Items = new ArrayList<>(); //省
    private ArrayList<ArrayList<String>> mOptions2Items = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>> mOptions3Items = new ArrayList<>();//区

    @Override
    protected void initData() {
        setShowTitle(false);
        isShowBack(true);
        setTitle("新增收货地址");
        setWindowTitleBlack(true);
        initJsonData();
        createData();
    }

    private void createData() {
        try {
            AddressBean.ResultBean bean = (AddressBean.ResultBean) getIntent().getSerializableExtra("object");
            if (bean != null) {
                setTitle("修改收货地址");
                mAddressName.setText(bean.getRealName());
                mPhone.setText(bean.getPhone());
                String[] address = bean.getAddress().split(" ");
                mAddress.setText(address[0] + " " + address[1] + " " + address[2] + " " + address[3]);
                String msg = "";
                for (int i = 4; i < address.length; i++) {
                    msg = msg + address[i];
                }
                mAllAddress.setText(msg);
                mAddRessId = bean.getId();
                mPostCode.setText(bean.getZipCode());
                mAddressAdd.setText("确认修改");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        mTvAddress = (TextView) get(R.id.address);
        mAddressName = (EditText) get(R.id.address_name);
        mPhone = (EditText) get(R.id.phone);
        mAddress = (TextView) get(R.id.address);
        mAllAddress = (EditText) get(R.id.all_address);
        mPostCode = (EditText) get(R.id.post_code);
        mAddressAdd = (TextView) get(R.id.address_add);
        get(R.id.layout_address).setOnClickListener(this);
        get(R.id.address_add).setOnClickListener(this);
    }

    //添加地址
    private void addAddress() {
        String addressName = mAddressName.getText().toString();
        String phone = mPhone.getText().toString();
        String address = mAddress.getText().toString();
        String allAddress = mAllAddress.getText().toString();
        String postCode = mPostCode.getText().toString();
        if (TextUtils.isEmpty(addressName)) {
            toast("请输入收件人");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            toast("请输入手机号");
            return;
        }
        if (!Utils.isMobilePhone(phone)) {
            toast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            toast("请选择所在地区");
            return;
        }

        if (TextUtils.isEmpty(allAddress)) {
            toast("请选择详细地址");
            return;
        }
        if (TextUtils.isEmpty(postCode)) {
            toast("请选择邮政编码");
            return;
        }

        initHeadMap();
        Map<String, String> map = new HashMap<>();
        if ("确认修改".equals(mAddressAdd.getText())) {
            map.put("id", String.valueOf(mAddRessId));
        }
        map.put("realName", addressName);
        map.put("phone", phone);
        map.put("address", address + " " + allAddress);
        map.put("zipCode", postCode);

        if ("确认修改".equals(mAddressAdd.getText())) {
            net(false, false, AppBean.class)
                    .put(0, Api.CHANGE_ADDRESS_URL, map);

        } else {
            net(false, false, AppBean.class)
                    .post(0, Api.ADD_ADDRESS_URL, map);
        }


    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            AppBean appBean = (AppBean) o;
            toast(appBean.getMessage());
            if ("0000".equals(appBean.getStatus())) {
                //添加成功
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_address://选择地址
                hintInputMethodManager(mAddressName);
                hintInputMethodManager(mPhone);
                hintInputMethodManager(mAllAddress);
                hintInputMethodManager(mPostCode);
                showPickerView();
                break;
            case R.id.address_add://新增收货地址
                addAddress();
                break;
        }
    }


    private void showPickerView() {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                mTvAddress.setText(mOptions1Items.get(options1).getPickerViewText() + " "
                        + mOptions2Items.get(options1).get(options2) + " "
                        + mOptions3Items.get(options1).get(options2).get(options3) + " ");

            }
        }).setTitleText("请选择所在地区 ")
                .setDividerColor(Color.parseColor("#f2f2f2"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(mOptions1Items, mOptions2Items, mOptions3Items);//三级选择器
        pvOptions.show();

    }

    //解析数据 （省市区三级联动）
    private void initJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        mOptions1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            mOptions2Items.add(CityList);

            /**
             * 添加地区数据
             */
            mOptions3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    //隐藏
    public void hintInputMethodManager(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //隐藏键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
