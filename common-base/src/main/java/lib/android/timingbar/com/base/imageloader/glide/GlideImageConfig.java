package lib.android.timingbar.com.base.imageloader.glide;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.Target;
import lib.android.timingbar.com.base.imageloader.ImageConfig;

/**
 * GlideImageConfig
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 这里放Glide专属的配置信息,可以一直扩展字段,如果外部调用时想让图片加载框架
 * 做一些操作,比如清除或则切换缓存策略,则可以定义一个int类型的变量,内部根据int做不同过的操作
 * 其他操作同理
 *
 * @author rqmei on 2018/1/25
 */

public class GlideImageConfig extends ImageConfig {
    private int cacheStrategy;//0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.DATA,3对应DiskCacheStrategy.RESOURCE,4对应DiskCacheStrategy.AUTOMATIC
    private Transformation<Bitmap> transformation;//glide用它来改变图形的形状
    private int transformationType;//图片的形状（0：没有形状； 1:圆形图片;2:矩形圆角；3：高斯模糊；4：高斯模糊+圆形；5：高斯模糊+矩形圆角）
    private Target[] targets;
    private ImageView[] imageViews;
    private View[] bgViews;
    private boolean isClearMemory;//清理内存缓存
    private boolean isClearDiskCache;//清理本地缓存
    private int loadAnimal;//图片加载动画

    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.bgView = builder.bgView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.transformationType = builder.transformationType;
        this.targets = builder.targets;
        this.imageViews = builder.imageViews;
        this.bgViews = builder.bgViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
        this.loadAnimal = builder.loadAnimal;
    }

    /**
     * 缓存策略
     *
     * @return 0：（all）缓存SOURC和RESULT, 1：什么都不缓存, 2：缓存原始数据,3：缓存变换后的资源数据;4:根据原始图片数据和资源编码策略来自动选择磁盘缓存策略
     */
    public int getCacheStrategy() {
        return cacheStrategy;
    }

    /**
     * glide设置图片的形状
     *
     * @return
     */
    public Transformation<Bitmap> getTransformation() {
        return transformation;
    }

    /**
     * 图片的形状
     *
     * @return 0：没有形状； 1:圆形图片;2:矩形圆角；3：高斯模糊；4：高斯模糊+圆形；5：高斯模糊+矩形圆角
     */
    public int getTransformationType() {
        return transformationType;
    }

    public Target[] getTargets() {
        return targets;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public View[] getBgViews() {
        return bgViews;
    }

    /**
     * 是否清除内存缓存
     *
     * @return true:清楚；false 不清除
     */
    public boolean isClearMemory() {
        return isClearMemory;
    }

    /**
     * 是否清除本地缓存
     *
     * @return true:清楚；false 不清除
     */
    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public int getLoadAnimal() {
        return loadAnimal;
    }

    public static Buidler builder() {
        return new Buidler ();
    }


    public static final class Buidler {
        private String url;//图片路径
        private ImageView imageView;//设置资源图片
        private View bgView;//设置背景图片
        private int placeholder;//默认占位展示的图片
        private int errorPic;//加载出错时展示的图片
        private int cacheStrategy;//0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
        private Transformation<Bitmap> transformation;//glide用它来改变图形的形状
        private int transformationType;//图片的形状（0：没有形状； 1:圆形图片;2:矩形圆角；3：高斯模糊；4：高斯模糊+圆形；5：高斯模糊+矩形圆角）
        private Target[] targets;
        private ImageView[] imageViews;
        private View[] bgViews;
        private boolean isClearMemory;//清理内存缓存
        private boolean isClearDiskCache;//清理本地缓存
        private int loadAnimal;//图片加载动画

        private Buidler() {
        }

        /**
         * 设置将要加载的图片的路径
         *
         * @param url 图片路径
         * @return Buidler
         */
        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 默认占位展示的图片
         *
         * @param placeholder 默认图片资源id
         * @return
         */
        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        /**
         * 加载出错时展示的图片
         *
         * @param errorPic 错误图片资源id
         * @return
         */
        public Buidler errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        /**
         * 设置要显示资源图片的ImageView
         *
         * @param imageView
         * @return
         */
        public Buidler imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        /**
         * 设置要加载背景图片的View
         *
         * @param bgView
         * @return
         */
        public Buidler bgView(View bgView) {
            this.bgView = bgView;
            return this;
        }

        /**
         * 缓存策略
         *
         * @param cacheStrategy 0：（all）缓存SOURC和RESULT, 1：什么都不缓存, 2：缓存原始数据,3：缓存变换后的资源数据
         * @return
         */
        public Buidler cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        /**
         * 改变图形的形状
         *
         * @param transformation 形状相关的控制参数
         * @return
         */
        public Buidler transformation(Transformation<Bitmap> transformation) {
            this.transformation = transformation;
            return this;
        }

        /**
         * 设置图片的形状
         *
         * @param transformationType 0：没有形状； 1:圆形图片;2:矩形圆角；3：高斯模糊；4：高斯模糊+圆形；5：高斯模糊+矩形圆角
         * @return
         */
        public Buidler transformationType(int transformationType) {
            this.transformationType = transformationType;
            return this;
        }

        /**
         * 设置target 方便取消任务
         *
         * @param targets 在执行的任务
         * @return 取消在执行的任务并且释放资源
         */
        public Buidler targets(Target... targets) {
            this.targets = targets;
            return this;
        }

        /**
         * 在加载资源图片的控件
         *
         * @param imageViews ImageView控件
         * @return 取消在加载资源图片的ImageView并且释放资源
         */
        public Buidler imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        /**
         * 在加载背景图片的控件
         *
         * @param bgViews 背景图片控件View
         * @return 取消在加载背景图片的View并且释放资源
         */
        public Buidler bgViews(View... bgViews) {
            this.bgViews = bgViews;
            return this;
        }

        /**
         * 是否清除内存缓存
         *
         * @param isClearMemory true:清楚；false 不清除
         * @return
         */
        public Buidler isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        /**
         * 是否清除本地缓存
         *
         * @param isClearDiskCache true:清楚；false 不清除
         * @return
         */
        public Buidler isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        /**
         * 图片加载动画
         *
         * @param loadAnimal 动画
         * @return
         */
        public Buidler loadAnimal(int loadAnimal) {
            this.loadAnimal = loadAnimal;
            return this;
        }

        public GlideImageConfig build() {
            return new GlideImageConfig (this);
        }
    }
}
