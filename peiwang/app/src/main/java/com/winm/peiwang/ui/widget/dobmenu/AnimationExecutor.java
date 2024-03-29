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
package com.winm.peiwang.ui.widget.dobmenu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

/**
 * 执行开关窗帘的动画
 * 
 * @author kymjs (https://github.com/kymjs)
 * @since 2015-3
 * 
 */
public class AnimationExecutor {

    private final CurtainViewController mCurtainViewController;

    public AnimationExecutor(CurtainViewController vSlidingMenuController) {
        super();
        this.mCurtainViewController = vSlidingMenuController;
    }

    /**
     * 两种动画类型
     */
    public enum MovingType {
        TOP_TO_BOTTOM, BOTTOM_TO_TOP
    }

    public void animateView(int duration, int fromY, int toY) {
        if (mCurtainViewController.getSlidingItem().getMaxDuration() > CurtainViewController.DEFAULT_INT) {
            duration = Math.min(duration, mCurtainViewController
                    .getSlidingItem().getMaxDuration());
        }

        // 移动方式
        final MovingType movingType = toY == 0 ? MovingType.BOTTOM_TO_TOP
                : MovingType.TOP_TO_BOTTOM;

        String propertyName = "";
        if (mCurtainViewController.getSlidingItem().getSlidingType() == CurtainItem.SlidingType.SIZE) {
            propertyName = "viewHeight";

        } else if (mCurtainViewController.getSlidingItem().getSlidingType() == CurtainItem.SlidingType.MOVE) {
            propertyName = "viewTop";
        }

        ValueAnimator sizeAnim = ObjectAnimator.ofInt(mCurtainViewController,
                propertyName, fromY, toY);
        sizeAnim.setDuration(duration);
        sizeAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (movingType == AnimationExecutor.MovingType.BOTTOM_TO_TOP) {
                    if (mCurtainViewController.getSlidingItem()
                            .getOnSwitchListener() != null) {
                        mCurtainViewController.getSlidingItem()
                                .getOnSwitchListener().onCollapsed();
                    }
                } else if (movingType == AnimationExecutor.MovingType.TOP_TO_BOTTOM) {
                    if (mCurtainViewController.getSlidingItem()
                            .getOnSwitchListener() != null) {
                        mCurtainViewController.getSlidingItem()
                                .getOnSwitchListener().onExpanded();
                    }
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {}
        });
        sizeAnim.start();
    }
}
