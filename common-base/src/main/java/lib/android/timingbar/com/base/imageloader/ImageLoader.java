package lib.android.timingbar.com.base.imageloader;

import android.content.Context;
import lib.android.timingbar.com.base.imageloader.glide.GlideImageLoaderStrategy;

/**
 * ImageLoader
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 实现策略接口（图片的相关操作实现）
 *
 * @author rqmei on 2018/1/25
 * @singleton 表示单例模式
 * @inject 标识需要依赖注入的构造函数和字段(注意：接口不能够创建；第三方库的类不能够创建；*配置对象必须配置！)
 * 注解构造函数：通过标记构造函数，告诉Dagger 2可以创建该类的实例(Dagger2通过Inject标记可以在需要这个类实例的时候来找到这个构造函数并把相关实例new出来)从而提供依赖关系。
 * 注解依赖变量：通过标记依赖变量，Dagger2提供依赖关系，注入变量
 */
public final class ImageLoader {
    private IBaseImageLoaderStrategy strategy;

    public ImageLoader() {
        if (strategy == null) {
            strategy = new GlideImageLoaderStrategy ();
        }
    }

    /**
     * 加载图片
     *
     * @param context 上下文
     * @param config  （ImageConfig）图片相关配置
     * @param <T>     通配泛型
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        strategy.loadImage (context, config);
    }

    /**
     * 清空图片
     *
     * @param context 上下文
     * @param config  （ImageConfig）图片相关配置
     * @param <T>     通配泛型
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        strategy.clear (context, config);
    }
}
