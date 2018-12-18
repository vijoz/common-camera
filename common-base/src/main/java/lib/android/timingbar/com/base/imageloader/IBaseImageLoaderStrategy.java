package lib.android.timingbar.com.base.imageloader;

import android.content.Context;

/**
 * BaseImageLoaderStrategy
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 公开策略模式（对图片加载+清空）的接口
 *
 * @author rqmei on 2018/1/25
 */

public interface IBaseImageLoaderStrategy<T extends ImageConfig> {
    /**
     * 加载图片
     *
     * @param ctx    上下文
     * @param config 需加载图片的配置信息
     */
    void loadImage(Context ctx, T config);

    /**
     * 清空图片
     *
     * @param ctx    上下文
     * @param config 需清空图片的配置信息
     */
    void clear(Context ctx, T config);
}
