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
package com.winm.peiwang.domain;

import com.winm.peiwang.ui.fragment.AboutFragment;
import com.winm.peiwang.ui.fragment.ActiveFragment;
import com.winm.peiwang.ui.fragment.NoteBookFragment;
import com.winm.peiwang.ui.fragment.NoteEditFragment;
import com.winm.peiwang.ui.fragment.OSCBlogDetailFragment;
import com.winm.peiwang.ui.fragment.TweetFragment;
import com.winm.peiwang.ui.fragment.TweetRecordFragment;
import com.winm.peiwang.ui.fragment.WeChatFragment;
import com.winm.peiwang.ui.fragment.BlogAuthorFragment;
import com.winm.peiwang.ui.fragment.MyCollectFragment;
import com.winm.peiwang.ui.fragment.OSCBlogListFragment;
import com.winm.peiwang.ui.fragment.coordinate.DefinePointInfoFragment;

/**
 * 返回页的基本信息注册 (其实就是将原本会在Manifest中注册的Activity转成Fragment在这里注册)
 * 
 * @author kymjs (https://www.kymjs.com/)
 * @since 2015-3
 */
public enum SimpleBackPage {
    COMMENT(1, WeChatFragment.class),

    OSC_BLOG_DETAIL(2, OSCBlogDetailFragment.class),

    OSC_BLOG_LIST(3, OSCBlogListFragment.class),

    OSC_TWEET_LIST(4, TweetFragment.class),

    OSC_TWEET_SEND(5, TweetRecordFragment.class),

    OSC_ACTIVE(6, ActiveFragment.class),

    STICKY(7, NoteBookFragment.class),

    STICKY_EDIT(8, NoteEditFragment.class),

    BLOG_AUTHOR(9, BlogAuthorFragment.class),

    ABOUT(10, AboutFragment.class),

    COLLECT(11, MyCollectFragment.class),

    DEFINEPT(12, DefinePointInfoFragment.class);

    private Class<?> clazz;
    private int value;

    private SimpleBackPage(int value, Class<?> cls) {
        this.clazz = cls;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static Class<?> getPageByValue(int value) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == value)
                return p.getClazz();
        }
        return null;
    }

}
