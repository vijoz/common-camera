package lib.android.timingbar.com.view.handSign.point;

/**
 * TimedPoint
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/5
 */

public class TimedPoint {
    public float x;
    public float y;
    public long timestamp;

    public TimedPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis ();
    }

    public TimedPoint set(float x, float y) {
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis ();
        return this;
    }

    public float distanceTo(TimedPoint point) {
        return (float) Math.sqrt (Math.pow (point.x - x, 2) + Math.pow (point.y - y, 2));
    }

    public float velocityTo(TimedPoint point) {
        long t = point.timestamp - timestamp;
        if (t == 0)
            return 0;
        else
            return distanceTo (point) / t;
    }
}
