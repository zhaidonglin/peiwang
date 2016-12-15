package com.winm.peiwang.ui.fragment.coordinate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.winm.peiwang.AppConfig;
import com.winm.peiwang.R;
import com.winm.peiwang.domain.PointInfo;
import com.winm.peiwang.domain.SimpleBackPage;
import com.winm.peiwang.ui.EventsActivity;
import com.winm.peiwang.ui.SimpleBackActivity;
import com.winm.peiwang.ui.fragment.TitleBarFragment;
import com.winm.peiwang.utils.MailSenderInfo;
import com.winm.peiwang.utils.SimpleMailSender;
import com.winm.peiwang.utils.Utils;

import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.database.DaoConfig;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class DefinePointInfoFragment extends TitleBarFragment {

    private String longitude;
    private String latitude;


    @BindView(id = R.id.spinner_same)
    private Spinner sSame;
    @BindView(id = R.id.bt_submit, click = true)
    private Button btSubmit;
    @BindView(id = R.id.bt_quit, click = true)
    private Button btSend;

    @BindView(id = R.id.editText1)
    private EditText etName;


    @BindView(id = R.id.TextView02)
    private TextView tvJD;
    @BindView(id = R.id.TextView03)
    private TextView tvWD;
    @BindView(id = R.id.spinner)
    private Spinner tvNames;
    @BindView(id = R.id.spinner1)
    private Spinner sp_pole;
    @BindView(id = R.id.spinner_same)
    private Spinner sp_hlPole;
    @BindView(id = R.id.rb_original,click = true)
    private Button bt_original;
    @BindView(id = R.id.rb_new,click = true)
    private Button bt_new;
    @BindView(id = R.id.checkBox,click = true)
    private Button bt_checkBox;
    @BindView(id = R.id.spinner2)
    private Spinner sp_Two_epi;
    @BindView(id = R.id.spinner4)
    private Spinner sp_four_epi;
    @BindView(id = R.id.spinner8)
    private Spinner sp_eight_epi;
    @BindView(id = R.id.et_zmxh_num)
    private EditText et_users;
    @BindView(id = R.id.spinner_zmxh)
    private Spinner sp_next_moudle;
    @BindView(id = R.id.et_dlxh_num)
    private EditText et_next_users;
    @BindView(id = R.id.spinner_dlxh)
    private Spinner sp_next_moudle1;

    @BindView(id = R.id.et_zmxh_num)
    private EditText etZmxh;
    @BindView(id = R.id.et_dlxh_num)
    private EditText etDlxh;



    private KJDB kjdb;
    private PointInfo javaBean;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container,
                                Bundle bundle) {
        View view = View.inflate(getActivity(), R.layout.frag_define_pointinfo, null);
        return view;
    }


    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        actionBarRes.title = getString(com.winm.peiwang.R.string.app_name);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        tvJD.setText(this.longitude);
        tvWD.setText(this.latitude);
    }

    @Override
    protected void initData() {
        super.initData();
        DaoConfig config = new DaoConfig();
        config.setContext(outsideAty);
        config.setDbName("pw.db");
        config.setDebug(true);
        String sdCardPath = FileUtils.getSavePath(AppConfig.dbPath);
        config.setTargetDirectory(sdCardPath);
        kjdb = KJDB.create(config);
    }

    @Override
    protected void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.bt_submit: {
                try {
                    javaBean = new PointInfo();
                    javaBean.setName(etName.getText().toString());
                    javaBean.setPoint_x(Double.parseDouble(tvJD.getText().toString()));
                    javaBean.setPoint_y(Double.parseDouble(tvWD.getText().toString()));
                    javaBean.setNames(tvNames.getSelectedItem().toString());
                    javaBean.setPole(sp_pole.getSelectedItem().toString());
                    javaBean.setHl_pole(sp_hlPole.getSelectedItem().toString());
                    javaBean.setTwo_epi(Integer.parseInt(sp_Two_epi.getSelectedItem().toString()));
                    javaBean.setFour_epi(Integer.parseInt(sp_four_epi.getSelectedItem().toString()));
                    javaBean.setEight_epi(Integer.parseInt(sp_eight_epi.getSelectedItem().toString()));
                    javaBean.setUsers(Integer.parseInt(etZmxh.getText().toString()));
                    javaBean.setNext_moudle(sp_next_moudle.getSelectedItem().toString());
                    javaBean.setNext_users(Integer.parseInt(etDlxh.getText().toString()));
                    javaBean.setNext_moudle1(sp_next_moudle1.getSelectedItem().toString());
                    kjdb.save(javaBean);

                    ViewInject.toast("保存成功");


                }
                catch (Exception e){
                    e.printStackTrace();
                }

                //页面跳转
                Intent jumpIntent = new Intent();
                jumpIntent.setClass(outsideAty, EventsActivity.class);

                startActivity(jumpIntent);
                outsideAty.finish();
                break;
            }
            case R.id.bt_quit: {

                //页面跳转
                Intent jumpIntent = new Intent();
                jumpIntent.setClass(outsideAty, EventsActivity.class);

                startActivity(jumpIntent);
                outsideAty.finish();
                break;
            }
            default:
                break;
        }
    }


    public void setValue(String longitude,String latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }


}
