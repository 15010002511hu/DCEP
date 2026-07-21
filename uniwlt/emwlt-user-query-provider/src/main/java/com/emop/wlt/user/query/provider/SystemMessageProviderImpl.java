package com.emop.wlt.user.query.provider;


import com.emop.wlt.user.pojo.SystemMessage;
import com.emop.wlt.user.query.api.SystemMessageQueryProvider;
import com.emop.wlt.user.query.service.SystemMessageQueryServie;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class SystemMessageProviderImpl implements SystemMessageQueryProvider {

    @Autowired
    private SystemMessageQueryServie systemMessageServiceQuery;

    @Override
    public List<SystemMessage> selectAll() {
        return systemMessageServiceQuery.selectAll();
    }

    @Override
    public List<SystemMessage> selectByInstAndGtTimeStampAndGeValidTime(String inst, Timestamp timeStamp,
        LocalDateTime validTime) {
        return systemMessageServiceQuery.selectByInstAndGtTimeStampAndGeValidTime(inst, timeStamp, validTime);
    }
}
