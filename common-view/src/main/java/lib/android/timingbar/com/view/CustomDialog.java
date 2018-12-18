package lib.android.timingbar.com.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.*;
import lib.android.timingbar.com.util.ConvertUtils;
import lib.android.timingbar.com.util.DisplayUtils;

/**
 * CustomDialog1
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/6/14
 */

@SuppressLint("ValidFragment")
public class CustomDialog extends DialogFragment {
    private Context context;
    private int height, width;
    private boolean cancelTouchout;
    //dialog对应的style
    private int resStyle = -1;
    private View view;
    //弹框显示的位置
    private int gravity = Gravity.BOTTOM;
    /**
     * 水平的左右边距
     */
    private int horizontalMargin = 0;
    /**
     * dialog 依附的activity的透明值0：全透明啊；1：不透明
     */
    private float defaultAlpha = 0.5f;
    private String fragmentTag = "custom_dialog";

    private CustomDialog(CustomDialog.Builder builder) {
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
        resStyle = builder.resStyle;
        gravity = builder.gravity;
        horizontalMargin = builder.horizontalMargin;
    }

    /**
     * 开始附着在宿主Activity中
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        Log.i ("CustomDialog", " onAttach");
    }

    /**
     * 宿主Activity正在创建，它此时在初始化一些对象
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setStyle (DialogFragment.STYLE_NO_TITLE, resStyle);
    }

    /**
     * Fargment创建本身的视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog ().setCanceledOnTouchOutside (cancelTouchout);
        return view;
    }

    /**
     * 宿主Activity准备呈现视图
     */
    @Override
    public void onStart() {
        super.onStart ();
        Window window = getDialog ().getWindow ();
        WindowManager.LayoutParams params = window.getAttributes ();
        params.alpha = defaultAlpha;
        if (width > 0) {
            params.width = width;
        } else {
            if (horizontalMargin > 0) {
                params.width = DisplayUtils.getScreenWidthPixels (context);
                params.width -= horizontalMargin * 2;
            } else {
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
            }
        }
        if (height > 0) {
            params.height = height;
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = gravity;

        if (defaultAlpha == 1) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug  
        } else {
            window.addFlags (WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug  
        }
        window.setBackgroundDrawable (new BitmapDrawable ());
        window.setAttributes (params);
    }

    /**
     * 宿主Activity呈现视图，自身也呈现出来
     */
    @Override
    public void onResume() {
        super.onResume ();
        Log.i ("CustomDialog", "onResume =");
    }

    /**
     * 宿主Activity与自身都进入暂停状态，从前端进入后台
     */
    @Override
    public void onPause() {
        super.onPause ();
        Log.i ("CustomDialog", " onPause =");
    }

    /**
     * 宿主Activity销毁完毕，
     */
    @Override
    public void onDestroy() {
        super.onDestroy ();
        Log.i ("CustomDialog", " onDestroy =");
    }

    /**
     * 判断弹窗是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return getDialog () != null && getDialog ().isShowing ();
    }

    public void dimiss() {
        if (isShowing ()) {
            super.dismiss ();
        }
    }

    public void show(FragmentManager manager) {
        if (!isShowing ()) {
            Log.i ("CustomDialog", " show isAdded===" + isAdded ());
            //正常显示
            show (manager, fragmentTag);
        }

    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public void setDefaultAlpha(float defaultAlpha) {
        this.defaultAlpha = defaultAlpha;
    }

    public static final class Builder {
        private Context context;
        //dialog对应的高和宽
        private int height, width;
        //dialog外部区域被电击，弹框是否消失
        private boolean cancelTouchout;
        //dialog对应的view
        private View view;
        //dialog对应的style
        private int resStyle = -1;
        //弹框显示的位置
        private int gravity;
        /**
         * 水平的左右边距
         */
        private int horizontalMargin = 0;

        public Builder(Context context) {
            this.context = context;
        }

        public CustomDialog.Builder view(int resView) {
            view = LayoutInflater.from (context).inflate (resView, null);
            return this;
        }

        public CustomDialog.Builder heightpx(int val) {
            height = val;
            return this;
        }

        public CustomDialog.Builder widthpx(int val) {
            width = val;
            return this;
        }

        public CustomDialog.Builder heightdp(int val) {
            height = ConvertUtils.dp2px (context, val);
            return this;
        }

        public CustomDialog.Builder widthdp(int val) {
            width = ConvertUtils.dp2px (context, val);
            return this;
        }

        public CustomDialog.Builder heightDimenRes(int dimenRes) {
            height = context.getResources ().getDimensionPixelOffset (dimenRes);
            return this;
        }

        public CustomDialog.Builder widthDimenRes(int dimenRes) {
            width = context.getResources ().getDimensionPixelOffset (dimenRes);
            return this;
        }

        public CustomDialog.Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public CustomDialog.Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public CustomDialog.Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public CustomDialog.Builder horizontalMargin(int horizontalMargin) {
            this.horizontalMargin = horizontalMargin;
            return this;
        }

        public CustomDialog.Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById (viewRes).setOnClickListener (listener);
            return this;
        }


        public CustomDialog build() {
            return new CustomDialog (this);
        }
    }
}
