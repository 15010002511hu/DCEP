package com.dcep.supergw.common.sentinel;

import com.alibaba.csp.sentinel.Entry;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : maxinyu
 * @version : AbstractFlowProcess.java v 0.1 2021-04-20
 * @description :
 */
public abstract class AbstractFlowProcess implements FlowProcess {

    /**
     * LinkedList 储存 申请下来的entry，后申请的entry加在先申请的之前
     */
    LinkedList<Entry> entries = new LinkedList<>();

    /**
     * 释放entry
     */
    @Override
    public void releaseEntry(List<Entry> entries) {
        //list为null直接return
        if (entries == null) {
            return;
        }
        //遍历list，执行entry.exit(),释放Entry
        for (Entry entry : entries) {
            entry.exit();
        }
        entries.clear();
    }

    /**
     * 释放entry
     */
    public void releaseEntry() {
        releaseEntry(entries);
    }

}
