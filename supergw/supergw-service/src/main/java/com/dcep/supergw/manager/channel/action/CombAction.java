package com.dcep.supergw.manager.channel.action;

import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;

public abstract class CombAction extends AbstractAction {

    private final static String actionName = "Combination";

    protected static AbstractAction root;
    protected static AbstractAction tail;

    public CombAction() {
        super(actionName);
        init();
        if (null == root || null == tail) {
            throw new GwException(GwErrorEnum.SYSTEM_CONFIG_ERROR,
                "CombAction(root, tail) should be inited as a link!");
        }
        super.setNextAction(root);
        super.setPreAction(tail);
    }

    // 需要初始化comb里面的小链
    abstract void init();

    @Override
    public void setPreAction(AbstractAction action) {
        root.setPreAction(action);
    }

    @Override
    public void setNextAction(AbstractAction action) {
        tail.setNextAction(action);
    }

}
