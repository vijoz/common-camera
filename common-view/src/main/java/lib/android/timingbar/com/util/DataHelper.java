package lib.android.timingbar.com.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * DataHelper
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/10/9
 */

public class DataHelper {
    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null)
            return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close ();
                }
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

}
