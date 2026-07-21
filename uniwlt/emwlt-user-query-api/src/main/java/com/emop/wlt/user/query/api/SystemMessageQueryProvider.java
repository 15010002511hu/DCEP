package com.emop.wlt.user.query.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.pojo.SystemMessage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@ZoneRouter(ZoneRouter.Type.CZ)
public interface SystemMessageQueryProvider {

    List<SystemMessage> selectAll();

    List<SystemMessage> selectByInstAndGtTimeStampAndGeValidTime(String inst, Timestamp timeStamp, LocalDateTime validTime);
}
