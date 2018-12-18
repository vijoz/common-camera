package lib.android.timingbar.com.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import lib.android.timingbar.com.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * GuideView1
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 膜层引导页对应的view
 *
 * @author rqmei on 2018/6/13
 */

public class GuideView extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private final String TAG = getClass ().getSimpleName ();
    private Context mContext;
    private boolean first = true;
    /**
     * targetView前缀。版本号 + targetView.getId()作为保存在SP文件的key。
     */
    private final String SHOW_GUIDE_PREFIX = "v" + getVersionName () + "_";
    /**
     * GuideView 偏移量
     */
    private int offsetX, offsetY;
    /**
     * targetView 的外切圆半径
     */
    private int radius;
    /**
     * 需要显示提示信息的View
     */
    private View targetView;
    /**
     * 自定义View
     */
    private View customGuideView;
    /**
     * 透明圆形画笔
     */
    private Paint mCirclePaint;
    /**
     * 背景色画笔
     */
    private Paint mBackgroundPaint;
    /**
     * targetView是否已测量
     */
    private boolean isMeasured;
    /**
     * targetView圆心
     */
    private int[] center;
    /**
     * 绘图层叠模式
     */
    private PorterDuffXfermode porterDuffXfermode;
    /**
     * 绘制前景bitmap
     */
    private Bitmap bitmap;
    /**
     * 背景色和透明度，格式 #aarrggbb
     */
    private int backgroundColor;
    /**
     * Canvas,绘制bitmap
     */
    private Canvas temp;
    /**
     * 相对于targetView的位置.在target的那个方向
     */
    private Direction direction;

    /**
     * 目标View的形状
     */
    private TargetViewShape myShape;
    /**
     * targetView左上角坐标
     */
    private int[] location;
    /**
     * 版本号
     */
    private String versionName;
    /**
     * 显示一次
     */
    private boolean showOnce;
    private OnClickCallback listener;

    public String getVersionName() {
        try {
            PackageManager packageManager = getContext ().getPackageManager ();
            PackageInfo packInfo = packageManager.getPackageInfo (getContext ().getPackageName (), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
            return "v0";
        }
    }

    public void restoreState() {
        Log.v (TAG, "restoreState");
        offsetX = offsetY = 0;
        radius = 0;
        mCirclePaint = null;
        mBackgroundPaint = null;
        isMeasured = false;
        center = null;
        porterDuffXfermode = null;
        bitmap = null;
        temp = null;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public GuideView(Context context) {
        super (context);
        this.mContext = context;
        init ();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setShape(TargetViewShape shape) {
        this.myShape = shape;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setCustomGuideView(View customGuideView) {
        this.customGuideView = customGuideView;
        if (!first) {
            restoreState ();
        }
    }

    public void setBgColor(int background_color) {
        this.backgroundColor = background_color;
    }

    public View getTargetView() {
        return targetView;
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
        //        restoreState();
        if (!first) {
            //            guideViewLayout.removeAllViews();
        }
    }

    private void init() {
    }

    public boolean isShowOnce() {
        return showOnce;
    }

    public void setShowOnce(boolean showOnce) {
        this.showOnce = showOnce;
    }

    private boolean hasShown() {
        if (targetView == null)
            return true;
        return mContext.getSharedPreferences (TAG, Context.MODE_PRIVATE).getBoolean (generateUniqId (targetView), false);
    }

    private String generateUniqId(View v) {
        return SHOW_GUIDE_PREFIX + v.getId ();
    }

    public int[] getCenter() {
        return center;
    }

    public void setCenter(int[] center) {
        this.center = center;
    }

    @SuppressLint("NewApi")
    public void hide() {
        Log.v (TAG, "hide");
        if (customGuideView != null) {
            targetView.getViewTreeObserver ().removeOnGlobalLayoutListener (this);
            this.removeAllViews ();
            ((FrameLayout) ((Activity) mContext).getWindow ().getDecorView ()).removeView (this);
            restoreState ();
        }
    }

    public boolean show() {
        Log.v (TAG, "show");
        if (StringUtils.isEmpty (versionName) || versionName.equals (getVersionName ())) {
            if (isShowOnce ()) {
                if (!hasShown ()) {
                    if (targetView != null) {
                        showHint ();
                        mContext.getSharedPreferences (TAG, Context.MODE_PRIVATE).edit ().putBoolean (generateUniqId (targetView), true).commit ();
                        return true;
                    }
                }
                return false;
            } else {
                showHint ();
                return true;
            }
        } else {
            return false;
        }
    }

    private void showHint() {
        if (targetView != null) {
            targetView.getViewTreeObserver ().addOnGlobalLayoutListener (this);
        }

        this.setBackgroundResource (R.color.transparent);

        ((FrameLayout) ((Activity) mContext).getWindow ().getDecorView ()).addView (this);
        first = false;
    }

    /**
     * 添加提示文字，位置在targetView的下边
     * 在屏幕窗口，添加蒙层，蒙层绘制总背景和透明圆形，圆形下边绘制说明文字
     */
    private void createGuideView() {
        Log.v (TAG, "createGuideView");

        // 添加到蒙层
        //        if (guideViewLayout == null) {
        //            guideViewLayout = new RelativeLayout(mContext);
        //        }

        // Tips布局参数
        LayoutParams guideViewParams;
        guideViewParams = new LayoutParams (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        guideViewParams.setMargins (0, center[1] + radius + 10, 0, 0);

        if (customGuideView != null) {

            //            LayoutParams guideViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (direction != null) {
                int width = this.getWidth ();
                int height = this.getHeight ();

                int left = center[0] - radius;
                int right = center[0] + radius;
                int top = center[1] - radius;
                int bottom = center[1] + radius;
                switch (direction) {
                    case TOP:
                        this.setGravity (Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                        guideViewParams.setMargins (offsetX, offsetY - height + top, -offsetX, height - top - offsetY);
                        break;
                    case LEFT:
                        this.setGravity (Gravity.RIGHT);
                        guideViewParams.setMargins (offsetX - width + left, top + offsetY, width - left - offsetX, -top - offsetY);
                        break;
                    case BOTTOM:
                        this.setGravity (Gravity.CENTER_HORIZONTAL);
                        guideViewParams.setMargins (offsetX, bottom + offsetY, -offsetX, -bottom - offsetY);
                        break;
                    case RIGHT:
                        guideViewParams.setMargins (right + offsetX, top + offsetY, -right - offsetX, -top - offsetY);
                        break;
                    case LEFT_TOP:
                        this.setGravity (Gravity.RIGHT | Gravity.BOTTOM);
                        guideViewParams.setMargins (offsetX - width + left, offsetY - height + top, width - left - offsetX, height - top - offsetY);
                        break;
                    case LEFT_BOTTOM:
                        this.setGravity (Gravity.RIGHT);
                        guideViewParams.setMargins (offsetX - width + left, bottom + offsetY, width - left - offsetX, -bottom - offsetY);
                        break;
                    case RIGHT_TOP:
                        this.setGravity (Gravity.BOTTOM);
                        guideViewParams.setMargins (right + offsetX, offsetY - height + top, -right - offsetX, height - top - offsetY);
                        break;
                    case RIGHT_BOTTOM:
                        guideViewParams.setMargins (right + offsetX, bottom + offsetY, -right - offsetX, -top - offsetY);
                        break;
                }
            } else {
                guideViewParams = new LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                guideViewParams.setMargins (offsetX, offsetY, -offsetX, -offsetY);
            }

            //            guideViewLayout.addView(customGuideView);

            this.addView (customGuideView, guideViewParams);
        }
    }

    /**
     * 获得targetView 的宽高，如果未测量，返回｛-1， -1｝
     *
     * @return
     */
    private int[] getTargetViewSize() {
        int[] location = {-1, -1};
        if (isMeasured) {
            location[0] = targetView.getWidth ();
            location[1] = targetView.getHeight ();
        }
        return location;
    }

    /**
     * 获得targetView 的半径
     *
     * @return
     */
    private int getTargetViewRadius() {
        if (isMeasured) {
            int[] size = getTargetViewSize ();
            int x = size[0];
            int y = size[1];

            return (int) (Math.sqrt (x * x + y * y) / 2);
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw (canvas);
        if (!isMeasured) {
            return;
        }
        if (targetView == null) {
            return;
        }
        drawBackground (canvas);
    }

    /**
     * 绘制膜层的背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        // 先绘制bitmap，再将bitmap绘制到屏幕
        bitmap = Bitmap.createBitmap (canvas.getWidth (), canvas.getHeight (), Bitmap.Config.ARGB_8888);
        temp = new Canvas (bitmap);

        // 背景画笔
        Paint bgPaint = new Paint ();
        if (backgroundColor != 0) {
            bgPaint.setColor (backgroundColor);
        } else {
            bgPaint.setColor (ContextCompat.getColor (getContext (), R.color.shadow));
        }
        bgPaint.setAlpha (220);

        // 绘制屏幕背景
        temp.drawRect (0, 0, temp.getWidth (), temp.getHeight (), bgPaint);

        // targetView 的透明圆形画笔
        if (mCirclePaint == null)
            mCirclePaint = new Paint ();
        porterDuffXfermode = new PorterDuffXfermode (PorterDuff.Mode.SRC_OUT);// 或者CLEAR
        mCirclePaint.setXfermode (porterDuffXfermode);
        mCirclePaint.setAntiAlias (true);

        if (myShape != null) {
            RectF oval = new RectF ();
            switch (myShape) {
                case CIRCULAR://圆形
                    temp.drawCircle (center[0], center[1], radius, mCirclePaint);//绘制圆形
                    break;
                case ELLIPSE://椭圆
                    //RectF对象
                    oval.left = center[0] - targetView.getWidth () / 2 - 10;                              //左边
                    oval.top = center[1] - targetView.getHeight () / 2 - 10;                              //上边
                    oval.right = center[0] + targetView.getWidth () / 2 + 10;                             //右边
                    oval.bottom = center[1] + targetView.getHeight () / 2 + 10;                           //下边
                    temp.drawOval (oval, mCirclePaint);                   //绘制椭圆
                    break;
                case RECTANGULAR://圆角矩形
                    //RectF对象
                    oval.left = center[0] - targetView.getWidth () / 2 - 10;                              //左边
                    oval.top = center[1] - targetView.getHeight () / 2 - 10;                              //上边
                    oval.right = center[0] + targetView.getWidth () / 2 + 10;                             //右边
                    oval.bottom = center[1] + targetView.getHeight () / 2 + 10;                           //下边
                    temp.drawRoundRect (oval, 10, 10, mCirclePaint);                   //绘制圆角矩形
                    break;
                default:
            }
        }
        // 绘制到屏幕
        canvas.drawBitmap (bitmap, 0, 0, bgPaint);
        bitmap.recycle ();
    }

    public void setClickListener(final OnClickCallback listener) {
        this.listener = listener;
        setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                listener.onGuideViewClicked ();
            }
        });
    }

    /**
     * 监听视图树的观察者,获得一个视图的真实高度
     */
    @Override
    public void onGlobalLayout() {
        if (isMeasured) {
            return;
        }
        if (targetView.getHeight () > 0 && targetView.getWidth () > 0) {
            isMeasured = true;
        }

        // 获取targetView的中心坐标
        if (center == null) {
            // 获取左上角坐标
            location = new int[2];
            //获取以弹框为原点的targetView的坐标
            targetView.getLocationInWindow (location);
            center = new int[2];
            // 获取中心坐标
            center[0] = location[0] + targetView.getWidth () / 2;
            center[1] = location[1] + targetView.getHeight () / 2;
        }
        // 获取targetView外切圆半径
        if (radius == 0) {
            radius = getTargetViewRadius ();
        }
        // 添加GuideView
        createGuideView ();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return listener == null ? true : super.onTouchEvent (event);
    }

    /**
     * 定义GuideView相对于targetView的方位，共八种。不设置则默认在targetView下方
     */
    public enum Direction {
        LEFT, TOP, RIGHT, BOTTOM,
        LEFT_TOP, LEFT_BOTTOM,
        RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 定义目标控件的形状，共3种。圆形，椭圆，带圆角的矩形（可以设置圆角大小），不设置则默认是圆形
     */
    public enum TargetViewShape {
        CIRCULAR, ELLIPSE, RECTANGULAR
    }

    /**
     * GuideView点击Callback
     */
    public interface OnClickCallback {
        void onGuideViewClicked();
    }

    public static class Builder {
        Context mContext;
        //存放要显示的膜层View
        private List<GuideView> viewList = new ArrayList<> ();
        //当前显示的膜层下标
        private int currentIndex = 0;
        //是否只显示一次
        private boolean isShowOnce;
        //字体大小
        private float textSize = 0f;
        //apk版本名字
        private String versionName;

        private Builder() {
        }

        public Builder(Context context) {
            this (context, "", true);
        }

        public Builder(Context context, String versionName) {
            this (context, versionName, true);
        }

        public Builder(Context context, boolean isShowOnce) {
            this (context, "", isShowOnce);
        }

        public Builder(Context context, String versionName, boolean isShowOnce) {
            mContext = context;
            this.versionName = versionName;
            this.isShowOnce = isShowOnce;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 添加膜层对应的view
         *
         * @param targetView 目标view
         * @param hintView   提示view
         * @param dir        显示的相对位置
         * @param shape      目标view的形状
         * @return
         */
        public Builder addHintView(View targetView, View hintView, Direction dir, TargetViewShape shape) {
            addHintView (targetView, hintView, dir, shape, 0, 0, null);
            return this;
        }

        public Builder addHintView(View targetView, View hintView, Direction dir, TargetViewShape shape, int xOffset, int yOffset) {
            addHintView (targetView, hintView, dir, shape, xOffset, yOffset, null);
            return this;
        }

        public Builder addHintView(View targetView, View hintView, Direction dir, TargetViewShape shape, OnClickCallback listener) {
            addHintView (targetView, hintView, dir, shape, 0, 0, listener);
            return this;
        }

        public Builder addHintView(View targetView, View hintView, Direction dir, TargetViewShape shape, int xOffset, int yOffset, OnClickCallback listener) {
            GuideView guideView = onCreatGuideView (targetView, hintView, dir, shape);
            if (xOffset != 0) {
                guideView.setOffsetX (xOffset);
            }
            if (yOffset != 0) {
                guideView.setOffsetY (yOffset);
            }
            if (listener != null) {
                guideView.setClickListener (listener);
            }
            viewList.add (guideView);
            return this;
        }

        public Builder addHintView(View targetView, int hintImg, Direction dir, TargetViewShape shape, int xOffset, int yOffset) {
            ImageView imageView = new ImageView (mContext);
            imageView.setImageResource (hintImg);
            imageView.setScaleType (ImageView.ScaleType.CENTER);
            imageView.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {
                    showNext ();
                }
            });
            addHintView (targetView, imageView, dir, shape, xOffset, yOffset);
            return this;
        }

        public Builder addHintView(View targetView, int hintImg, Direction dir, TargetViewShape shape) {
            addHintView (targetView, hintImg, dir, shape, 0, 0);
            return this;
        }


        /**
         * 创建通用的guideView
         *
         * @param targetView 目标view
         * @param hintView   提示view
         * @param dir        显示的相对位置
         * @param shape      目标view的形状
         * @return
         */
        private GuideView onCreatGuideView(View targetView, View hintView, Direction dir, TargetViewShape shape) {
            GuideView guideView = new GuideView (mContext);
            guideView.setTargetView (targetView);
            guideView.setCustomGuideView (hintView);
            guideView.setDirection (dir);
            guideView.setShape (shape);
            guideView.setVersionName (versionName);
            guideView.setShowOnce (isShowOnce);
            return guideView;
        }

        /**
         * 显示引导页膜层
         */
        public void show() {
            if (viewList.size () > currentIndex) {
                if (!viewList.get (currentIndex).show ()) {
                    currentIndex++;
                    show ();
                }
            } else {
                viewList.clear ();
                viewList = null;
            }
        }

        /**
         * 显示下一个膜层
         */
        public void showNext() {
            viewList.get (currentIndex).hide ();
            if (++currentIndex < viewList.size ()) {
                viewList.get (currentIndex).show ();
            } else {
                viewList.clear ();
                viewList = null;
            }
        }
    }
}