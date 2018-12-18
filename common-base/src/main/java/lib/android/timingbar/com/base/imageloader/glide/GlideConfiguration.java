package lib.android.timingbar.com.base.imageloader.glide;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.*;
import com.bumptech.glide.module.GlideModule;
import lib.android.timingbar.com.base.util.DataHelper;

import java.io.File;

/**
 * GlideConfiguration
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 重写glide图片加载的相关配置信息
 * <p/>
 * 在AndroidManifest.xml中的<application>标签下定义<meta-data>，这样Glide才能知道我们定义了这么一个类，
 * 其中android:name是我们自定义的GlideModule的完整路径，而android:value就固定写死GlideModule。
 *
 * @author rqmei on 2018/1/25
 */
public class GlideConfiguration implements GlideModule {
    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;//图片缓存文件最大值为100Mb

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        //通过builder.setXXX进行配置.
        //设置磁盘缓存，需要实现DiskCache.Factory,默认实现是InternalCacheDiskCacheFactory
        builder.setDiskCache (new DiskCache.Factory () {
            @Override
            public DiskCache build() {
                return DiskLruCacheWrapper.get (DataHelper.makeDirs (new File (DataHelper.getCacheFile (context), "Glide")), IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder (context)
                .setMemoryCacheScreens (2)
                .build ();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize ();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize ();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
        //设置内存缓存的大小
        builder.setMemoryCache (new LruResourceCache (customMemoryCacheSize));
        //设置Bitmap的缓存池，用来重用Bitmap
        builder.setBitmapPool (new LruBitmapPool (customBitmapPoolSize));
        builder.setDiskCache (new InternalCacheDiskCacheFactory (context, IMAGE_DISK_CACHE_MAX_SIZE));
    }


    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}
