package com.dcep.supergw.core.action;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.core.ChannelContext;

/**
 * @author : maxinyu
 * @version : AbstractAction.java v 0.1 2020-05-11
 * @description :
 */
public abstract class AbstractAction implements Action {

    private AbstractAction nextAction = null;
    private AbstractAction preAction = null;

    private final String actionName;

    public AbstractAction(String actionName) {
        this.actionName = actionName;
    }

    /**
     * 由子类自定义业务操作，默认让数据向前传递
     */
    @Override
    public void doInvoke(ChannelContext context) {
        context.fireInvokeAction();
    }

    /**
     * 由子类自定义异常操作
     */
    @Override
    public void doException(ChannelContext context, DcepException e) {
    	context.setStatus(ChannelContext.Status.EXCEPTION);
        context.fireInvokeException(e);
    }

    /**
     * 由子类自定义异常操作，默认让数据向后传递
     */
    @Override
    public void doCallBack(ChannelContext context) {
        context.fireInvokeCallBack();
    }

    public AbstractAction getPreAction() {
        return this.preAction;
    }

    public AbstractAction getNextAction() {
        return this.nextAction;
    }

    public void setPreAction(AbstractAction action) {
        this.preAction = action;
    }

    public void setNextAction(AbstractAction action) {
        this.nextAction = action;
    }

    public String getActionName() {
        return this.actionName;
    }
}
