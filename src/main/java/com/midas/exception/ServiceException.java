package com.midas.exception;

/**
 * 自定义异常处理
 * 
 * @author arron
 *
 */
public class ServiceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3102970830450989988L;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 错误详细内容
     */
    private String detail;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(String code, String msg, String detail) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    public ServiceException(String code, String msg, String detail, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
        this.msg = message;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(ServiceException e) {
        this(e.getCode(), e.getMsg(), e.getDetail(), e.fillInStackTrace());
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
