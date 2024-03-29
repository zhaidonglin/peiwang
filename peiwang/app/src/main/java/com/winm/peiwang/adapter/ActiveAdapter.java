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
package com.winm.peiwang.adapter;

import java.util.List;

import com.winm.peiwang.domain.Active;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import android.widget.AbsListView;

/**
 * 猿活动适配器
 * 
 * @author kymjs (http://www.kymjs.com/)
 * 
 */
public class ActiveAdapter extends KJAdapter<Active> {
    private final KJBitmap kjb = new KJBitmap();

    public ActiveAdapter(AbsListView view, List<Active> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }

    @Override
    public void convert(AdapterHolder helper, Active item, boolean isScrolling) {
        if (isScrolling) {
            kjb.displayCacheOrDefult(helper.getView(com.winm.peiwang.R.id.iv_event_img),
                    item.getCover(), com.winm.peiwang.R.drawable.pic_bg);
        } else {
            helper.setImageByUrl(kjb, com.winm.peiwang.R.id.iv_event_img, item.getCover());
        }
        helper.setText(com.winm.peiwang.R.id.tv_event_date, item.getStartTime());
        helper.setText(com.winm.peiwang.R.id.tv_event_title, item.getTitle());
        helper.setText(com.winm.peiwang.R.id.tv_event_time, item.getSpot());
    }
}
