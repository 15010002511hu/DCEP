package com.emop.wlt.user.query.exception;

import com.emop.common.enums.errorcode.BaseErrorEnum;

public class ExceptionCast {

    public static void cast(BaseErrorEnum errorEnum) {
        throw new UserQueryException(errorEnum);
    }

    public static void cast(BaseErrorEnum errorEnum, Throwable throwable) {
        throw new UserQueryException(errorEnum, throwable);
    }

    public static void buzCast(BaseErrorEnum errorEnum) {
        throw new UserQueryBuzException(errorEnum);
    }

    public static void buzCast(BaseErrorEnum errorEnum, String customMessage) {
        throw new UserQueryBuzException(errorEnum, customMessage);
    }

}
