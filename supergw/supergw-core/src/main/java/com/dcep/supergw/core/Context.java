package com.dcep.supergw.core;

import com.dcep.common.exception.DcepException;

public interface Context {

    void fireInvokeAction();

    void fireInvokeException(DcepException e);

    void fireInvokeCallBack();
}
