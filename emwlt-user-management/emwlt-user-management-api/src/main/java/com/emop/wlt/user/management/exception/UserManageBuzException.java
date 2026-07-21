package com.emop.wlt.user.management.exception;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.exception.EmwltBuzException;

public class UserManageBuzException extends EmwltBuzException {

    private static final long serialVersionUID = 5817511263870912797L;

    public UserManageBuzException() {
    }

    public UserManageBuzException(BaseErrorEnum errorEnum) {
        super(errorEnum);
    }

}
