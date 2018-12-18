package lib.android.timingbar.com.base.imageloader;

import android.view.View;
import android.widget.ImageView;


/**
 * ImageConfig
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 这里是图片加载配置信息的基类,可以定义一些所有图片加载框架都可以用的通用参数
 *
 * @author rqmei on 2018/1/25
 */
public class ImageConfig {
    protected String url;// 图片路径
    protected ImageView imageView; //加载资源图片控件ImageView
    protected View bgView;//加载背景图片控件View
    protected int placeholder;//默认占位展示的图片id
    protected int errorPic; //加载出错展示的图片id

    /**
     * @return 图片路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return 加载资源图片控件ImageView
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @return 加载背景图片控件View
     */
    public View getBgView() {
        return bgView;
    }

    /**
     * @return 默认占位展示的图片id
     */
    public int getPlaceholder() {
        return placeholder;
    }

    /**
     * @ 加载出错展示的图片id
     */
    public int getErrorPic() {
        return errorPic;
    }

}
