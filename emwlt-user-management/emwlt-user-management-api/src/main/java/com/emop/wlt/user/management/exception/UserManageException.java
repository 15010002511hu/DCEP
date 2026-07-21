package com.emop.wlt.user.management.exception;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.exception.EmwltException;

public class UserManageException extends EmwltException {

    private static final long serialVersionUID = -3849515414029635936L;

    public UserManageException() {
    }

    public UserManageException(BaseErrorEnum errorEnum) {
        super(errorEnum);
    }

    public UserManageException(BaseErrorEnum errorEnum, String customMessage) {
        super(errorEnum.getCode(), customMessage);
    }

    public UserManageException(BaseErrorEnum errorEnum, Throwable throwable) {
        super(errorEnum, throwable);
    }

}
