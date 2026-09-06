package com.dcep.supergw.common.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.common.model.soap.SoapHeader;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : DcepFlowProcess.java v 0.1 2021-04-20
 * @description :
 */
public class DcepFlowProcess extends AbstractFlowProcess {

    private static final String GLOBAL_FLOW = "global";

    /**
     * 获取Entry
     */
    @Override
    public List<Entry> obtainEntry(Object object) throws BlockException {
        if (object == null) {
            throw new IllegalArgumentException("obj can not be null");
        }

        SoapHeader header = (SoapHeader) object;
        //声明资源

        Entry globalEntry = SphU.entry(GLOBAL_FLOW);
        entries.add(globalEntry);
        Entry entry = null;
        //创建名称为“机构编号”的entry
        if (StringUtils.isNotBlank(header.getSender()) && StringUtils.isNotBlank(header.getMsgTp())) {
            entry = SphU.entry(header.getSender() + "-" + header.getMsgTp());
            entries.addFirst(entry);
        }
        return entries;
    }
}
