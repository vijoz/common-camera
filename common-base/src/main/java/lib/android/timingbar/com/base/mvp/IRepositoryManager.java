package lib.android.timingbar.com.base.mvp;

/**
 * IRepositoryManager
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层,专门提供给 Model 层做数据处理
 *
 * @author rqmei on 2018/1/29
 */

public interface IRepositoryManager {
    /**
     * 根据传入的Class创建对应的仓库
     *
     * @param repository
     * @param <T>
     * @return
     */
    <T extends IModel> T createRepository(Class<T> repository);
}
