package com.emop.wlt.user.query.exception;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.exception.EmwltBuzException;

public class UserQueryBuzException extends EmwltBuzException {

    private static final long serialVersionUID = 7931516663087062534L;

    public UserQueryBuzException() {
    }

    public UserQueryBuzException(BaseErrorEnum errorEnum) {
        super(errorEnum);
    }

    public UserQueryBuzException(BaseErrorEnum errorEnum, String customMessage) {
        super(errorEnum.getCode(), customMessage);
    }


}
