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
package com.winm.peiwang.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winm.peiwang.ui.widget.dobmenu.CurtainItem;
import com.winm.peiwang.utils.PullTip;
import com.winm.peiwang.ui.widget.dobmenu.CurtainView;
import com.winm.peiwang.utils.KJAnimations;

import org.kymjs.kjframe.KJActivity;

/**
 * 应用Activity基类
 * 
 * @author kymjs (https://www.kymjs.com/)
 * @since 2015-3
 */
public abstract class TitleBarActivity extends KJActivity {

    public ImageView mImgBack;
    public TextView mTvTitle;
    public TextView mTvDoubleClickTip;
    public ImageView mImgMenu;
    public RelativeLayout mRlTitleBar;

    protected final Handler mMainLoopHandler = new Handler(
            Looper.getMainLooper());

    // Sliding menu object
    private CurtainView mCurtainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        try {
            mRlTitleBar = (RelativeLayout) findViewById(com.winm.peiwang.R.id.titlebar);
            mImgBack = (ImageView) findViewById(com.winm.peiwang.R.id.titlebar_img_back);
            mTvTitle = (TextView) findViewById(com.winm.peiwang.R.id.titlebar_text_title);
            mTvDoubleClickTip = (TextView) findViewById(com.winm.peiwang.R.id.titlebar_text_exittip);
            mImgMenu = (ImageView) findViewById(com.winm.peiwang.R.id.titlebar_img_menu);
            mImgBack.setOnClickListener(this);
            mImgMenu.setOnClickListener(this);
            initCurtainView();
        } catch (NullPointerException e) {
            throw new NullPointerException(
                    "TitleBar Notfound from Activity layout");
        }
        super.onStart();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
        case com.winm.peiwang.R.id.titlebar_img_back:
            onBackClick();
            break;
        case com.winm.peiwang.R.id.titlebar_img_menu:
            onMenuClick();
            break;
        default:
            break;
        }
    }

    protected void onBackClick() {}

    protected void onMenuClick() {}

    public void onCurtainPull() {}

    public void onCurtainPush() {}

    /********************** 窗帘视图相关 *****************************/

    private static int count = 0;

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            count = 0;
        }
    };

    public CurtainView getCurtainView() {
        return mCurtainView;
    }

    private void initCurtainView() {
        mCurtainView = new CurtainView(this, com.winm.peiwang.R.id.titlebar);
        mCurtainView.setSlidingView(com.winm.peiwang.R.layout.dob_sliding_menu);
        ImageView mImgLockScreen = (ImageView) mCurtainView.getContentView()
                .findViewById(com.winm.peiwang.R.id.lockscreen);
        mImgLockScreen
                .setImageResource(PullTip.lockScreen[(int) (Math.random() * PullTip.lockScreen.length)]);
        mCurtainView.setMaxDuration(1000);
        mCurtainView.setSlidingType(CurtainItem.SlidingType.MOVE);

        mCurtainView.setOnSwitchListener(new CurtainItem.OnSwitchListener() {
            @Override
            public void onCollapsed() {
                onCurtainPush();
            }

            @Override
            public void onExpanded() {
                onCurtainPull();
                mMainLoopHandler.postDelayed(timerRunnable, 3000);
                count++;
                if (count > 3) {
                    mMainLoopHandler.removeCallbacks(timerRunnable);
                    Toast.makeText(
                            aty,
                            PullTip.toast[(int) (Math.random() * PullTip.toast.length)],
                            Toast.LENGTH_SHORT).show();
                    count = 0;
                } else if (count > 1) {
                    mMainLoopHandler.removeCallbacks(timerRunnable);
                    mMainLoopHandler.postDelayed(timerRunnable, 3000);
                }
            }
        });
        mCurtainView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KJAnimations.clickCurtain(mCurtainView.getContentParentView());
                mMainLoopHandler.postDelayed(timerRunnable, 2000);
                count++;
                if (count > 3) {
                    mMainLoopHandler.removeCallbacks(timerRunnable);
                    Toast.makeText(
                            aty,
                            PullTip.toast[(int) (Math.random() * PullTip.toast.length)],
                            Toast.LENGTH_SHORT).show();
                    count = 0;
                } else if (count > 1) {
                    mMainLoopHandler.removeCallbacks(timerRunnable);
                    mMainLoopHandler.postDelayed(timerRunnable, 2000);
                }
            }
        });
    }
}
