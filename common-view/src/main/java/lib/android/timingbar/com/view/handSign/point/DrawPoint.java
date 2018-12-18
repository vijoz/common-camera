package lib.android.timingbar.com.view.handSign.point;

/**
 * DrawPoint
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/5
 */

public class DrawPoint {
    public float x;
    public float y;
    public float width;

    public DrawPoint set(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
        return this;
    }
}
