package com.dcep.supergw.manager.channel.action;

public class CombActionTest extends CombAction {

    @Override
    void init() {
        root = new AsyncActionTest("报钱柜");
        AbstractAction action = new AsyncActionTest("直接清算");
        tail = new AsyncActionTest("报同业");

        root.setNextAction(action);
        action.setPreAction(root);
        action.setNextAction(tail);
        tail.setPreAction(action);
    }

}
