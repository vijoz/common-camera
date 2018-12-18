package lib.android.timingbar.com.http.cache.converter;

import lib.android.timingbar.com.http.util.HttpLog;
import lib.android.timingbar.com.http.util.Utils;

import java.io.*;
import java.lang.reflect.Type;

/**
 * SerializableDiskConverter
 * -----------------------------------------------------------------------------------------------------------------------------------
 * <p>描述：序列化对象的转换器</p>
 * 1.使用该转换器，对象&对象中的其它所有对象都必须是要实现Serializable接口（序列化）<br>
 * 优点：<br>
 * 速度快<br>
 *
 * @author rqmei on 2018/9/7
 */

public class SerializableDiskConverter implements IDiskConverter {

    @Override
    public <T> T load(InputStream source, Type type) {
        //序列化的缓存不需要用到clazz
        T value = null;
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream (source);
            value = (T) oin.readObject ();
        } catch (IOException | ClassNotFoundException e) {
            HttpLog.e (e);
        } finally {
            Utils.close (oin);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream (sink);
            oos.writeObject (data);
            oos.flush ();
            return true;
        } catch (IOException e) {
            HttpLog.e (e);
        } finally {
            Utils.close (oos);
        }
        return false;
    }
}
