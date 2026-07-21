package com.emop.wlt.user.management.exception;

import com.emop.common.enums.errorcode.BaseErrorEnum;

public class ExceptionCast {

    public static void cast(BaseErrorEnum errorEnum) {
        throw new UserManageException(errorEnum);
    }

    public static void cast(BaseErrorEnum errorEnum, String customMessage) {
        throw new UserManageException(errorEnum, customMessage);
    }

    public static void buzCast(BaseErrorEnum errorEnum) {
        throw new UserManageBuzException(errorEnum);
    }

}
