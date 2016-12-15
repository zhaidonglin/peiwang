/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.winm.peiwang.ui.fragment;

import com.winm.peiwang.AppContext;
import com.winm.peiwang.domain.SimpleBackPage;
import com.winm.peiwang.ui.Login;
import com.winm.peiwang.ui.SimpleBackActivity;
import com.winm.peiwang.ui.TitleBarActivity;
import com.winm.peiwang.ui.widget.KJScrollView;
import com.winm.peiwang.utils.UIHelper;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.RoundImageView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 发现界面
 * 
 * @author kymjs (http://www.kymjs.com/)
 * @since 2015-3
 * 
 */
public class FindFragment extends TitleBarFragment {

    @BindView(id = com.winm.peiwang.R.id.find_img_zone)
    private ImageView mImgZone;
    @BindView(id = com.winm.peiwang.R.id.find_img_head, click = true)
    private RoundImageView mImgHead;
    @BindView(id = com.winm.peiwang.R.id.find_tv_name)
    private TextView mTvName;
    @BindView(id = com.winm.peiwang.R.id.find_root)
    private KJScrollView rootView;

    @BindView(id = com.winm.peiwang.R.id.find_plugin_1, click = true)
    private TextView mTvTweet;
    @BindView(id = com.winm.peiwang.R.id.find_plugin_2, click = true)
    private TextView mTvTodayMessage;
    @BindView(id = com.winm.peiwang.R.id.find_plugin_3, click = true)
    private TextView mTvJokeList;
    @BindView(id = com.winm.peiwang.R.id.find_plugin_4, click = true)
    private TextView mTvActive;
    @BindView(id = com.winm.peiwang.R.id.find_plugin_5, click = true)
    private TextView mTvFollow;
    @BindView(id = com.winm.peiwang.R.id.find_plugin_6, click = true)
    private TextView mTvSticky;

    KJBitmap kjb = new KJBitmap();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        View view = View.inflate(getActivity(), com.winm.peiwang.R.layout.frag_find, null);
        return view;
    }

    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        actionBarRes.title = getString(com.winm.peiwang.R.string.app_name);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        LayoutParams params = (LayoutParams) mImgZone.getLayoutParams();
        int h = params.height = (int) (AppContext.screenH * 0.3);
        params.width = AppContext.screenW;
        mImgZone.setLayoutParams(params);
        kjb.displayLoadAndErrorBitmap(
                mImgZone,
                "http://www.kymjs.com/app/user_center_bg"
                        + StringUtils.getDataTime("MMdd") + ".png",
                com.winm.peiwang.R.drawable.user_center_bg, com.winm.peiwang.R.drawable.user_center_bg);

        int space65 = (int) getResources().getDimension(com.winm.peiwang.R.dimen.space_65);

        LayoutParams headParams = (LayoutParams) mImgHead.getLayoutParams();
        headParams.topMargin = (h - space65) / 2 - 20;
        mImgHead.setLayoutParams(headParams);

        LayoutParams nameParams = (LayoutParams) mTvName.getLayoutParams();
        nameParams.topMargin = (h + space65) / 2;// 在头像底部间距半个头像的大小
        mTvName.setLayoutParams(nameParams);

        rootView.setOnViewTopPullListener(new KJScrollView.OnViewTopPull() {
            @Override
            public void onPull() {
                if (outsideAty instanceof TitleBarActivity) {
                    outsideAty.getCurtainView().expand();
                }
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
        case com.winm.peiwang.R.id.find_plugin_1:
            SimpleBackActivity.postShowWith(outsideAty,
                    SimpleBackPage.OSC_TWEET_LIST);
            break;
        case com.winm.peiwang.R.id.find_plugin_2:
            SimpleBackActivity.postShowWith(outsideAty, SimpleBackPage.COMMENT);
            break;
        case com.winm.peiwang.R.id.find_plugin_3:
            SimpleBackActivity.postShowWith(outsideAty,
                    SimpleBackPage.OSC_BLOG_LIST);
            break;
        case com.winm.peiwang.R.id.find_plugin_4:
            SimpleBackActivity.postShowWith(outsideAty,
                    SimpleBackPage.OSC_ACTIVE);
            break;
        case com.winm.peiwang.R.id.find_plugin_5:
            SimpleBackActivity.postShowWith(outsideAty,
                    SimpleBackPage.BLOG_AUTHOR);
            break;
        case com.winm.peiwang.R.id.find_plugin_6:
            SimpleBackActivity.postShowWith(outsideAty, SimpleBackPage.STICKY);
            break;
        case com.winm.peiwang.R.id.find_img_head:
            int id = UIHelper.getUser(outsideAty).getUid();
            if (id == 2332925 || id == 1) {
                outsideAty.showActivity(outsideAty, Login.class);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new KJBitmap().displayLoadAndErrorBitmap(mImgHead,
                UIHelper.getUser(outsideAty).getPortrait(),
                com.winm.peiwang.R.drawable.default_head, com.winm.peiwang.R.drawable.default_head);
        mTvName.setText(UIHelper.getUser(outsideAty).getName());
    }
}
