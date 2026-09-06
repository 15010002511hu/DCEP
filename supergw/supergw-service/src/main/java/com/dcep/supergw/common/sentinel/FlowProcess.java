package com.dcep.supergw.common.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import java.util.List;

public interface FlowProcess {

    /**
     * 获取Entry
     */
    public List<Entry> obtainEntry(Object object) throws BlockException;

    /**
     * 释放entry
     */
    public void releaseEntry(List<Entry> entrys);
}
