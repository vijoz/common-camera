package lib.android.timingbar.com.base.integration;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * ManifestParser
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 获取application中metaData注入的配置信息
 *
 * @author rqmei on 2018/1/30
 */

public class ManifestParsers {
    private static final String MODULE_VALUE = "ConfigModule";

    private final Context context;

    public ManifestParsers(Application context) {
        this.context = context;
    }

    public List<IConfigModule> parse() {
        List<IConfigModule> modules = new ArrayList<IConfigModule> ();
        try {
            //获取指定应用程序(当前应用) ApplicationInfo
            ApplicationInfo appInfo = context.getPackageManager ().getApplicationInfo (
                    context.getPackageName (), PackageManager.GET_META_DATA);
            //获取Application中的meta-data.
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet ()) {
                    Log.i ("ManifestParser", appInfo.metaData.getString (key));
                    if (MODULE_VALUE.equals (appInfo.metaData.get (key))) {
                        modules.add (parseModule (key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException ("Unable to find metadata to parse IConfigModule", e);
        }

        return modules;
    }

    /**
     * @param className
     * @return
     */
    private static IConfigModule parseModule(String className) {
        Log.i ("ManifestParser", "IConfigModule=" + className);
        Class<?> clazz;
        try {
            clazz = Class.forName (className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException ("Unable to find IConfigModule implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance ();
        } catch (InstantiationException e) {
            throw new RuntimeException ("Unable to instantiate IConfigModule implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException ("Unable to instantiate IConfigModule implementation for " + clazz, e);
        }

        if (!(module instanceof IConfigModule)) {
            throw new RuntimeException ("Expected instanceof IConfigModule, but found: " + module);
        }
        return (IConfigModule) module;
    }
}
