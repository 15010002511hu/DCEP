package com.dcep.supergw.manager.channel;

import com.dcep.supergw.manager.channel.action.AbstractAction;

/**
 * 责任链：将各个Action进行组装
 *
 * @author linlu
 * @date 20190824
 */
public class ChannelBuilder {

    private AbstractAction root;

    private ChannelBuilder() {

    }

    /**
     * 构造实例
     */
    public static ChannelBuilder getInstance() {
        return new ChannelBuilder();
    }

    /**
     * 在链尾添加Action
     */
    public ChannelBuilder addAction(AbstractAction action) {
        if (null == root) {
            root = action;
        } else {
            addLast(root, action);
        }
        return this;
    }

    /**
     * 递归在链尾添加Action
     */
    private void addLast(AbstractAction root, AbstractAction action) {
        if (null != root.getNextAction()) {
            addLast(root.getNextAction(), action);
        } else {
            root.setNextAction(action);
            action.setPreAction(root);
        }
    }

    /**
     * 返回链头
     */
    public AbstractAction build() {
        return root;
    }

}
