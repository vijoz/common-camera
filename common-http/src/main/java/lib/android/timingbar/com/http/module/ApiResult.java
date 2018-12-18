
package lib.android.timingbar.com.http.module;

/**
 * ApiResult
 * -----------------------------------------------------------------------------------------------------------------------------------
 * <p>描述：提供的默认的标注返回api</p>
 *
 * @author rqmei on 2018/5/2
 */
public class ApiResult<T> {
    private T data;//数据内容
    private int code;//标识code
    private String msg;//标识msg
    private boolean success;//是否成功的标识
    private String errors;//错误信息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    /**
     * @return 请求成功的标识
     */
    public boolean isOk() {
        return code == 1 || success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "success='" + success + '\'' +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", errors='" + errors + '\'' +
                ", data=" + data +
                '}';
    }
}
