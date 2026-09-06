package com.dcep.supergw.manager.channel.action;

public class PostWithSigAction extends CombAction {

    @Override
    void init() {
        root = new HttpsForwardPostAction();
        tail = new CheckSignAction();

        root.setNextAction(tail);
        tail.setPreAction(root);
    }

}
