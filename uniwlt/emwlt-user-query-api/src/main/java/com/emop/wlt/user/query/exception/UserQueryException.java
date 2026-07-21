package com.emop.wlt.user.query.exception;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.exception.EmwltException;

public class UserQueryException extends EmwltException {

    private static final long serialVersionUID = -5847074552928889231L;

    public UserQueryException(BaseErrorEnum errorEnum) {
        super(errorEnum);
    }

    public UserQueryException(BaseErrorEnum errorEnum, Throwable throwable) {
        super(errorEnum, throwable);
    }

}
