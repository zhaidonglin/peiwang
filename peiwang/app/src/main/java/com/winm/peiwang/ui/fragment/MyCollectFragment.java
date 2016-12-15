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

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.winm.peiwang.adapter.CollectAdapter;
import com.winm.peiwang.domain.CollectData;
import com.winm.peiwang.ui.widget.EmptyLayout;
import com.winm.peiwang.ui.widget.listview.PullToRefreshBase;
import com.winm.peiwang.ui.widget.listview.PullToRefreshList;
import com.winm.peiwang.utils.UIHelper;

import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.ui.BindView;

import java.util.List;

/**
 * 用户收藏
 *
 * @author kymjs (http://www.kymjs.com/)
 */
public class MyCollectFragment extends TitleBarFragment {

    public static final String TAG = MyCollectFragment.class.getSimpleName();

    @BindView(id = com.winm.peiwang.R.id.empty_layout)
    private EmptyLayout mEmptyLayout;
    @BindView(id = com.winm.peiwang.R.id.listview)
    private PullToRefreshList mRefreshLayout;
    private ListView mListView;

    private CollectAdapter adapter;

    private KJDB kjdb;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container,
                                Bundle bundle) {
        View root = View.inflate(outsideAty,
                com.winm.peiwang.R.layout.frag_pull_refresh_listview, null);
        return root;
    }

    @Override
    protected void initData() {
        super.initData();
        kjdb = KJDB.create(outsideAty);
        mEmptyLayout.dismiss();
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        mListView = mRefreshLayout.getRefreshView();
        mListView.setDivider(new ColorDrawable(0x00000000));

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (parent.getAdapter() instanceof CollectAdapter) {
                    CollectData data = (CollectData) parent.getAdapter()
                            .getItem(position);
                    UIHelper.toBrowser(outsideAty, data.getUrl());
                }
            }
        });
        mRefreshLayout.setPullLoadEnabled(false);
        mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                adapter.refresh(kjdb.findAll(CollectData.class));
                mRefreshLayout.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
            }
        });
        fillUI();
    }

    private void fillUI() {
        List<CollectData> datas = kjdb.findAll(CollectData.class);
        adapter = new CollectAdapter(mListView, datas,
                com.winm.peiwang.R.layout.item_list_collect);
        mListView.setAdapter(adapter);
    }

    /**
     * 将在onResume方法中调用
     */
    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        super.setActionBarRes(actionBarRes);
        actionBarRes.title = getString(com.winm.peiwang.R.string.str_collect);
        actionBarRes.backImageId = com.winm.peiwang.R.drawable.titlebar_back;
    }

    @Override
    public void onBackClick() {
        super.onBackClick();
        outsideAty.finish();
    }
}
