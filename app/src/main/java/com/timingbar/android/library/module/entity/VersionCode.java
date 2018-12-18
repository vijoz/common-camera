package com.timingbar.android.library.module.entity;

import java.io.Serializable;

/**
 * RefreshTrainResultInfo
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/2/5
 */

public class VersionCode implements Serializable {
    private int id;
    private String name;
    private String path;
    private String versionCode;
    private String versionName;
    private int type;
    private int isMustUpdate;
    private String updatime;
    private String deittime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsMustUpdate() {
        return isMustUpdate;
    }

    public void setIsMustUpdate(int isMustUpdate) {
        this.isMustUpdate = isMustUpdate;
    }

    public String getUpdatime() {
        return updatime;
    }

    public void setUpdatime(String updatime) {
        this.updatime = updatime;
    }

    public String getDeittime() {
        return deittime;
    }

    public void setDeittime(String deittime) {
        this.deittime = deittime;
    }

    @Override
    public String toString() {
        return "VersionCode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", type=" + type +
                ", isMustUpdate=" + isMustUpdate +
                ", updatime='" + updatime + '\'' +
                ", deittime='" + deittime + '\'' +
                '}';
    }
}
