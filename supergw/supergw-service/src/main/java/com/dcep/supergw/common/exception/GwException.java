/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.exception;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.common.enums.GwErrorEnum;

/**
 * 网关自定义异常类，基类
 * @author huyajun
 * @version $Id: GwBaseException.java, v 0.1 2019年6月12日 上午10:38:34 Administrator Exp $
 */
public class GwException extends RuntimeException {

    /** 这里存储code与msg的对应关系 */
    private final DcepException exception;

    /**  */
    private static final long serialVersionUID = -8097012051935961111L;

    /** 异常错误代码 */
    protected String code;

    /**
     * 创建一个<code>BaseException</code>对象
     */
    public GwException(DcepException e) {
        super(e.getMessage());
        code = e.getCode();
        exception = e;
    }

    /**
     * 创建一个<code>BaseException</code>对象
     */
    public GwException() {
        super();
        code = GwErrorEnum.UNKNOWN_EXCEPTION.getCode();
        exception = new DcepException(GwErrorEnum.UNKNOWN_EXCEPTION.getCode(),
            GwErrorEnum.UNKNOWN_EXCEPTION.getDescription());
    }

    /**
     * 创建一个<code>BaseException</code>对象
     */
    public GwException(String message) {
        super(message);
        code = GwErrorEnum.UNKNOWN_EXCEPTION.getCode();
        exception = new DcepException(GwErrorEnum.UNKNOWN_EXCEPTION.getCode(),
            GwErrorEnum.UNKNOWN_EXCEPTION.getDescription());
    }

    /**
     * 创建一个<code>BaseException</code>对象
     */
    public GwException(String message, Throwable cause) {
        super(message, cause);
        code = GwErrorEnum.UNKNOWN_EXCEPTION.getCode();
        exception = new DcepException(GwErrorEnum.UNKNOWN_EXCEPTION.getCode(),
            GwErrorEnum.UNKNOWN_EXCEPTION.getDescription());
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code 错误码
     */
    public GwException(GwErrorEnum code) {
        super(code.getDescription());
        this.code = code.getCode();
        exception = new DcepException(code.getCode(),
            code.getDescription());
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code 错误码
     * @param errorMessage 错误描述
     */
    public GwException(GwErrorEnum code, String errorMessage) {
        super(errorMessage);
        this.code = code.getCode();
        exception = new DcepException(code.getCode(),
            code.getDescription());
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code 错误码
     * @param cause 异常
     */
    public GwException(GwErrorEnum code, Throwable cause) {
        super(cause.getMessage(), cause);
        this.code = code.getCode();
        exception = new DcepException(code.getCode(),
            code.getDescription());
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code 错误码
     * @param errorMessage 错误描述
     * @param cause 异常
     */
    public GwException(GwErrorEnum code, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.code = code.getCode();
        exception = new DcepException(code.getCode(),
            code.getDescription());
    }

    public GwException(String code, String message) {
        super(message);
        this.code = code;
        exception = new DcepException(code, message);
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return
     */
    public String getDescription() {
        return exception.getMessage();
    }

}
