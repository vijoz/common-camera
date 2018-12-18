package lib.android.timingbar.com.base.mvp;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RepositoryManager
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 管理网络请求相关仓库实例对象的具体实现
 *
 * @author rqmei on 2018/1/29
 */

public class RepositoryManagerImpl implements IRepositoryManager {
    //保存已实例化的仓库对象
    private final Map<String, IModel> mRepositoryCache = new LinkedHashMap<> ();
    //保存Retrofit创建的代理对象
    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<> ();
    //保存缓存配置对应的缓存接口的对象集合
    private final Map<String, Object> mCacheServiceCache = new LinkedHashMap<> ();


    /**
     * 根据传入的Class创建对应的仓库
     *
     * @param repository
     * @param <T>
     * @return 网络请求仓库集合
     */
    @Override
    public <T extends IModel> T createRepository(Class<T> repository) {
        T repositoryInstance;
        synchronized (mRepositoryCache) {
            repositoryInstance = (T) mRepositoryCache.get (repository.getName ());
            if (repositoryInstance == null) {
                Constructor<? extends IModel> constructor = findConstructorForClass (repository);
                try {
                    //将构造函数实例化并获取其对象
                    repositoryInstance = (T) constructor.newInstance (this);
                } catch (InstantiationException e) {
                    throw new RuntimeException ("Unable to invoke " + constructor, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException ("Unable to invoke " + constructor, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException ("create repository error", e);
                }
                //将实例化的对象保存到激活中
                mRepositoryCache.put (repository.getName (), repositoryInstance);
            }
        }
        return repositoryInstance;
    }

    /**
     * 通过反射的方式获取对应类中所有的构造函数
     *
     * @param cls 将被反射的class
     * @return 被反射的class中所有的构造函数
     */
    private static Constructor<? extends IModel> findConstructorForClass(Class<?> cls) {
        Constructor<? extends IModel> bindingCtor;
        String clsName = cls.getName ();
        try {
            //获得所有构造函数
            bindingCtor = (Constructor<? extends IModel>) cls.getConstructor (IRepositoryManager.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException ("Unable to find constructor for " + clsName, e);
        }

        return bindingCtor;
    }
}
