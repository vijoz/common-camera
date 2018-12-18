package com.timingbar.android.library.module.entity;

import java.io.Serializable;

/**
 * Lesson
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/3/5
 */

public class Lesson implements Serializable {
    private int id;
    private String title;    // 标题
    private String fileUrl; // 视频教学地址
    private int up_id;  // 是否叶子节点 0:否 1:是
    private int apply_type;// app学习方式1：网络学习；2：集中学习；3：两种学习方式都可以用
    private int periods;//培训期数
    private int trainState;// 是否完. 0: 未完成; 1: 部分完成; 2: 全部完成
    private boolean lastLearn;//是否为最后一次学习

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
