package lib.android.timingbar.com.base.imageloader.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.*;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import lib.android.timingbar.com.base.imageloader.IBaseImageLoaderStrategy;
import lib.android.timingbar.com.base.util.BaseLog;


/**
 * GlideImageLoaderStrategy 策略模式接口的相关具体实现（IBaseImageLoaderStrategy的子类）
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 这里是图片加载配置信息的基类,可以定义一些所有图片加载框架都可以用的通用参数
 *
 * @author rqmei on 2018/1/25
 */
public class GlideImageLoaderStrategy implements IBaseImageLoaderStrategy<GlideImageConfig> {
    /**
     * 加载图片
     *
     * @param ctx    上下文
     * @param config 需加载图片的配置信息
     */
    @Override
    public void loadImage(Context ctx, final GlideImageConfig config) {
        if (ctx == null)
            throw new IllegalStateException ("Context is required");
        if (config == null)
            throw new IllegalStateException ("GlideImageConfig is required");
        if (TextUtils.isEmpty (config.getUrl ()))
            throw new IllegalStateException ("Url is required");
        if (config.getImageView () == null && config.getBgView () == null)
            throw new IllegalStateException ("Imageview and vgView is required");
        int loadAnimal = config.getLoadAnimal ();
        TransitionOptions transitionOptions = loadAnimal == 0 ? GenericTransitionOptions.withNoTransition () : GenericTransitionOptions.with (loadAnimal);
        //设置ImageView的资源图片
        if (config.getImageView () != null) {
            Glide.with (ctx).load (config.getUrl ())
                //    .transition (DrawableTransitionOptions.withCrossFade ())
                    .apply (setImageConfig (ctx, config)).into (config.getImageView ());
        }
        //设置View的背景图片
        if (config.getBgView () != null) {
            Glide.with (ctx).asBitmap ().load (config.getUrl ()).transition (transitionOptions).into (new SimpleTarget<Bitmap> () {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    BitmapDrawable bd = new BitmapDrawable (resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        config.getBgView ().setBackground (bd);
                    }
                }
            });
        }

    }

    /**
     * 设置加载图片的通用设置
     *
     * @param config
     */
    @SuppressLint("CheckResult")
    private RequestOptions setImageConfig(Context ctx, GlideImageConfig config) {
        RequestOptions requestOptions = new RequestOptions ();

        switch (config.getCacheStrategy ()) {//缓存策略
            case 0:
                requestOptions.diskCacheStrategy (DiskCacheStrategy.ALL);
                break;
            case 1:
                requestOptions.diskCacheStrategy (DiskCacheStrategy.NONE);
                break;
            case 2:
                requestOptions.diskCacheStrategy (DiskCacheStrategy.DATA);
                break;
            case 3:
                requestOptions.diskCacheStrategy (DiskCacheStrategy.RESOURCE);
                break;
            case 4:
                requestOptions.diskCacheStrategy (DiskCacheStrategy.AUTOMATIC);
                break;
        }
        BaseLog.i ("GlideImageLoaderStrategy getTransformationType=" + config.getTransformationType () + "," + config.getTransformation ());
        if (config.getTransformation () != null) {
            requestOptions.transform (config.getTransformation ());
        } else if (config.getTransformationType () != 0) {
            switch (config.getTransformationType ()) {
                //图片形状
                case 1://圆形
                    requestOptions.circleCrop ();
                    break;
                case 2://矩形圆角
                    requestOptions.transform (new RoundedCornersTransformation (15, 0, RoundedCornersTransformation.CornerType.ALL));
                    break;
                case 3://高斯模糊
                    requestOptions.transform (new BlurTransformation ());
                    break;
                case 4:       //高斯模糊+圆形
                    requestOptions.transform (new MultiTransformation<> (new BlurTransformation (), new CropCircleTransformation ()));
                    break;
                case 5: //高斯模糊+矩形圆角
                    requestOptions.transform (new MultiTransformation<> (new BlurTransformation (), new RoundedCornersTransformation (15, 0, RoundedCornersTransformation.CornerType.ALL)));
                    break;
            }
        }
        if (config.getPlaceholder () != 0)//设置占位符
            requestOptions.placeholder (config.getPlaceholder ());

        if (config.getErrorPic () != 0)//设置错误的图片
            requestOptions.error (config.getErrorPic ());

        return requestOptions;
    }

    /**
     * 根据相关配置信息清空加载图片时产生的缓存信息
     *
     * @param ctx    上下文
     * @param config 需清空图片的配置信息
     */
    @Override
    public void clear(final Context ctx, GlideImageConfig config) {
        if (ctx == null)
            throw new IllegalStateException ("Context is required");
        if (config == null)
            throw new IllegalStateException ("GlideImageConfig is required");

        if (config.getImageViews () != null && config.getImageViews ().length > 0) {//取消在执行的任务并且释放资源
            for (ImageView imageView : config.getImageViews ()) {
                Glide.with (ctx).clear (imageView);
            }
        }
        if (config.getBgViews () != null && config.getBgViews ().length > 0) {
            for (View bgView : config.getBgViews ()) {
                Glide.with (ctx).clear (bgView);
            }
        }
        if (config.getTargets () != null && config.getTargets ().length > 0) {//取消在执行的任务并且释放资源
            for (Target target : config.getTargets ())
                Glide.with (ctx).clear (target);
        }


        if (config.isClearDiskCache ()) {//清除本地缓存
            Observable.just (0)
                    .observeOn (Schedulers.io ())
                    .subscribe (new Consumer<Integer> () {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Glide.get (ctx).clearDiskCache ();
                        }
                    });
        }

        if (config.isClearMemory ()) {//清除内存缓存
            Glide.get (ctx).clearMemory ();
        }

    }
}
