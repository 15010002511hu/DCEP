package com.dcep.dips.wholesalepayment.dto;

import java.io.Serializable;

public interface BaseDTO extends Serializable {

    default String getReqMsgId(){return null;} ;
    default String getReqMsgSn(){return null;};
    default String getReqMsgTp(){return null;};
    default String getReqSender(){return null;};
    default String getReqReceiver(){return null;};
}
