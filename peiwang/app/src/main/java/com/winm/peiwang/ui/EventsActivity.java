package com.winm.peiwang.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.VisibleRegion;
import com.winm.peiwang.AppConfig;
import com.winm.peiwang.R;
import com.winm.peiwang.domain.PointInfo;
import com.winm.peiwang.ui.fragment.coordinate.DefinePointInfoFragment;
import com.winm.peiwang.utils.MailSenderInfo;
import com.winm.peiwang.utils.SimpleMailSender;

import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.database.DaoConfig;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;

import java.util.List;

/**
 * AMapV1地图中简单介绍OnMapClickListener, OnMapLongClickListener,
 * OnCameraChangeListener三种监听器用法
 */

public class EventsActivity extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnCameraChangeListener,LocationSource,AMapLocationListener,AMap.OnMarkerClickListener {
	private AMap aMap;
	private MapView mapView;
	//private TextView mTapTextView;
	private TextView mCameraTextView;
	private Button mBtCoordinate;
	private Button mBtSend;

	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private KJDB kjdb;
	private PointInfo javaBean;

	private String longitude;
	private String latitude;
	private LatLng point1;
	private LatLng point2;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		//mTapTextView = (TextView) findViewById(R.id.tap_text);
		mCameraTextView = (TextView) findViewById(R.id.camera_text);
		mBtCoordinate = (Button) findViewById(R.id.bt_coordinate);
		mBtSend = (Button) findViewById(R.id.bt_datasend);
		mBtCoordinate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO:

				if(latitude.isEmpty() || longitude.isEmpty()){
					ViewInject.toast("定位中，请稍后");
					return;
				}

				//页面跳转
				Intent jumpIntent = new Intent();
				jumpIntent.setClass( EventsActivity.this,Main.class);

				jumpIntent.putExtra("latitude",latitude);
				jumpIntent.putExtra("longitude",longitude);

				startActivity(jumpIntent);
				finish();
			}
		});

		mBtSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sdCardPath = FileUtils.getSavePath(AppConfig.dbPath);
				final String path = sdCardPath + "/pw.db";
				new Thread(new Runnable() {
					@Override
					public void run() {
						uploadDB(path);
					}
				}).start();
			}
		});

		//读取已定义的点
		DaoConfig config = new DaoConfig();
		config.setContext(EventsActivity.this);
		config.setDbName("pw.db");
		config.setDebug(true);
		String sdCardPath = FileUtils.getSavePath(AppConfig.dbPath);
		config.setTargetDirectory(sdCardPath);
		kjdb = KJDB.create(config);

		List<PointInfo> list = kjdb.findAll(PointInfo.class);


		LatLng p1=null;
		for(PointInfo pt: list){

			double lat = pt.getPoint_x();
			double lng = pt.getPoint_y();
			String name = pt.getName();

			LatLng p = new LatLng(lng, lat);// 经纬度
			aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.position(p).title(name)
					.snippet(name +":"+String.valueOf(lng)+","+String.valueOf(lat)).draggable(false));

			if(p1 == null){
				p1 = p;
			}else{
				aMap.addPolyline(new PolylineOptions().add(p1,p));
				p1 = p;
			}


		}

	}

	private void uploadDB(String info) {
		try {
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setMailServerHost("smtp.163.com");
			mailInfo.setMailServerPort("25");
			mailInfo.setValidate(true);
			mailInfo.setUserName("peiwang_send@163.com");
			mailInfo.setPassword("peiwang2016");
			mailInfo.setFromAddress("peiwang_send@163.com");
			mailInfo.setToAddress("peiwang_receive@163.com");
			mailInfo.setSubject("配网数据");
			mailInfo.setContent("配网数据");
			String[] files = {info};
			mailInfo.setAttachFileNames(files);

			// 这个类主要来发送邮件
			SimpleMailSender sms = new SimpleMailSender();
			sms.sendTextMail(mailInfo);// 发送文体格式


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * amap添加一些事件监听器
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()

		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;
	}


	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 对单击地图事件回调
	 */
	@Override
	public void onMapClick(LatLng point) {
//		mTapTextView.setText("tapped, point=" + point);
	}

	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng point) {
//		mTapTextView.setText("long pressed, point=" + point);
	}

	/**
	 * 对正在移动地图事件回调
	 */
	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
//		mCameraTextView.setText("onCameraChange:" + cameraPosition.toString());
	}

	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
//		mCameraTextView.setText("onCameraChangeFinish:"
//				+ cameraPosition.toString());
		VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、

//		LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
//		boolean isContain = latLngBounds.contains(Constants.SHANGHAI);// 判断上海经纬度是否包括在当前地图可见区域
//		if (isContain) {
//			ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
//		} else {
//			ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
//		}
	}


	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

				latitude = String.valueOf(amapLocation.getLatitude());
				longitude = String.valueOf(amapLocation.getLongitude());

				mCameraTextView.setText("纬度：" + latitude + ",  经度：" + longitude);

			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}


}
