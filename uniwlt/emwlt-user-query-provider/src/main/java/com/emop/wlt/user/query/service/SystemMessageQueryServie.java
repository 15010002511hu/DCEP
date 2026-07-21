package com.emop.wlt.user.query.service;

import com.emop.wlt.user.pojo.SystemMessage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface SystemMessageQueryServie {

    List<SystemMessage> selectAll();

    List<SystemMessage> selectByInstAndGtTimeStampAndGeValidTime(String inst, Timestamp timeStamp, LocalDateTime validTime);
}
