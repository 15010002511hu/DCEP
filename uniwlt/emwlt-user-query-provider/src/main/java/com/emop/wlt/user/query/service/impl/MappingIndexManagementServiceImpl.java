package com.emop.wlt.user.query.service.impl;


import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.MappingIndex;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.service.MappingIndexManagementService;
import com.emop.wlt.user.service.MappingIndexService;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanjianyu
 * @title
 * @description MappingIndexManagementServiceImpl
 * @date 2021/7/15
 */
@Service
public class MappingIndexManagementServiceImpl implements MappingIndexManagementService {

    @Autowired
    private MappingIndexService mappingIndexService;

    @Override
    @DDS(value = DatabaseConstant.USER_QUERY_DATABASE, rule = SingleRule.class)
    public String selectByPhone(String phone) {

        if (Strings.isBlank(phone)) {
            return null;
        }
        MappingIndex mappingIndex = new MappingIndex();
        mappingIndex.setMappingKey(phone);
        MappingIndex result = mappingIndexService.selectByPhone(mappingIndex);
        if (Objects.isNull(result)) {
            return null;
        }

        return result.getMappingValue();
    }
}
