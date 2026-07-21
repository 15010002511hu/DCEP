package com.emop.wlt.user.query.helper;

import com.emop.wlt.common.model.mapp.MessageHeader;
import com.emop.wlt.common.model.mapp.RequestModel;

public class RequestModelHelper {

    public static <T> RequestModel<T> buildRequestModel(T t) {
        RequestModel<T> request = new RequestModel<>();
        MessageHeader header = new MessageHeader();
        header.setUserId("123456");
        request.setMessageHeader(header);
        request.setMessageBody(t);
        return request;
    }

}
