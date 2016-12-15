package com.winm.peiwang.ui.fragment.coordinate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.winm.peiwang.AppContext;
import com.winm.peiwang.R;
import com.winm.peiwang.domain.SimpleBackPage;
import com.winm.peiwang.ui.Login;
import com.winm.peiwang.ui.SimpleBackActivity;
import com.winm.peiwang.ui.TitleBarActivity;
import com.winm.peiwang.ui.fragment.TitleBarFragment;
import com.winm.peiwang.ui.widget.KJScrollView;
import com.winm.peiwang.utils.UIHelper;
import com.winm.peiwang.utils.Utils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class LocationFragment extends TitleBarFragment implements
        RadioGroup.OnCheckedChangeListener, View.OnClickListener, AMapLocationListener {

    @BindView(id = com.winm.peiwang.R.id.rg_location, click = true)
    private RadioGroup rgLocation;
    @BindView(id = com.winm.peiwang.R.id.rb_continueLocation)
    private RadioButton rbLocationContinue;
    @BindView(id = com.winm.peiwang.R.id.rb_onceLocation)
    private RadioButton rbLocationOnce;
    @BindView(id = com.winm.peiwang.R.id.layout_interval)
    private View layoutInterval;
    @BindView(id = com.winm.peiwang.R.id.et_interval)
    private EditText etInterval;
    @BindView(id = com.winm.peiwang.R.id.cb_needAddress)
    private CheckBox cbAddress;
    @BindView(id = com.winm.peiwang.R.id.cb_gpsFirst)
    private CheckBox cbGpsFirst;
    @BindView(id = com.winm.peiwang.R.id.cb_cacheAble)
    private CheckBox cbCacheAble;
    @BindView(id = com.winm.peiwang.R.id.tv_result)
    private TextView tvReult;
    @BindView(id = com.winm.peiwang.R.id.bt_location, click = true)
    private Button btLocation;
    @BindView(id = R.id.bt_define, click = true)
    private Button btDefine;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container,
                                Bundle bundle) {
        View view = View.inflate(getActivity(), R.layout.frag_location, null);
        return view;
    }

    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        actionBarRes.title = getString(com.winm.peiwang.R.string.app_name);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        locationClient = new AMapLocationClient(outsideAty);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImgZone.getLayoutParams();
//        int h = params.height = (int) (AppContext.screenH * 0.3);
//        params.width = AppContext.screenW;
//        mImgZone.setLayoutParams(params);
//        kjb.displayLoadAndErrorBitmap(
//                mImgZone,
//                "http://www.kymjs.com/app/user_center_bg"
//                        + StringUtils.getDataTime("MMdd") + ".png",
//                com.winm.peiwang.R.drawable.user_center_bg, com.winm.peiwang.R.drawable.user_center_bg);
//
//        int space65 = (int) getResources().getDimension(com.winm.peiwang.R.dimen.space_65);
//
//        RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) mImgHead.getLayoutParams();
//        headParams.topMargin = (h - space65) / 2 - 20;
//        mImgHead.setLayoutParams(headParams);
//
//        RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) mTvName.getLayoutParams();
//        nameParams.topMargin = (h + space65) / 2;// 在头像底部间距半个头像的大小
//        mTvName.setLayoutParams(nameParams);
//
//        rootView.setOnViewTopPullListener(new KJScrollView.OnViewTopPull() {
//            @Override
//            public void onPull() {
//                if (outsideAty instanceof TitleBarActivity) {
//                    outsideAty.getCurtainView().expand();
//                }
//            }
//        });
    }

    @Override
    protected void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.bt_location: {
                if (btLocation.getText().equals(
                        getResources().getString(R.string.startLocation))) {
                    setViewEnable(false);
                    initOption();
                    btLocation.setText(getResources().getString(
                            R.string.stopLocation));
                    // 设置定位参数
                    locationClient.setLocationOption(locationOption);
                    // 启动定位
                    locationClient.startLocation();
                    mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
                } else {
                    setViewEnable(true);
                    btLocation.setText(getResources().getString(
                            R.string.startLocation));
                    // 停止定位
                    locationClient.stopLocation();
                    mHandler.sendEmptyMessage(Utils.MSG_LOCATION_STOP);
                }
                break;
            }
            case R.id.bt_define:{
                SimpleBackActivity.postShowWith(outsideAty, SimpleBackPage.DEFINEPT);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 设置控件的可用状态
     */
    private void setViewEnable(boolean isEnable) {
        rbLocationContinue.setEnabled(isEnable);
        rbLocationOnce.setEnabled(isEnable);
        etInterval.setEnabled(isEnable);
        cbAddress.setEnabled(isEnable);
        cbGpsFirst.setEnabled(isEnable);
        cbCacheAble.setEnabled(isEnable);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_continueLocation:
                //只有持续定位设置定位间隔才有效，单次定位无效
                layoutInterval.setVisibility(View.VISIBLE);
                locationOption.setOnceLocation(false);
                break;
            case R.id.rb_onceLocation:
                //只有持续定位设置定位间隔才有效，单次定位无效
                layoutInterval.setVisibility(View.GONE);
                locationOption.setOnceLocation(true);
                break;
        }
    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(cbAddress.isChecked());
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(cbGpsFirst.isChecked());
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(cbCacheAble.isChecked());
        String strInterval = etInterval.getText().toString();
        if (!TextUtils.isEmpty(strInterval)) {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(Long.valueOf(strInterval));
        }

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //开始定位
                case Utils.MSG_LOCATION_START:
                    tvReult.setText("正在定位...");
                    break;
                // 定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    String result = Utils.getLocationStr(loc);
                    tvReult.setText(result);
                    break;
                //停止定位
                case Utils.MSG_LOCATION_STOP:
                    tvReult.setText("定位停止");
                    break;
                default:
                    break;
            }
        };
    };

}
