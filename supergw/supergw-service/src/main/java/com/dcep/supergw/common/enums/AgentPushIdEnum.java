package com.dcep.supergw.common.enums;

public enum AgentPushIdEnum {

    PUSHID_0200023("0200023", "1"),
    PUSHID_0200024("0200024", "1"),
    PUSHID_0200025("0200025", "1"),
    PUSHID_0200026("0200026", "1"),
    PUSHID_0200027("0200027", "1"),
    PUSHID_0200028("0200028", "1"),
    PUSHID_DEFAULT("ohter","0")
    ;

    String pushId;

    String agentId;

    public String getPushId() {
        return pushId;
    }

    public String getAgentId() {
        return agentId;
    }

    AgentPushIdEnum(String pushId, String agentId) {
        this.pushId = pushId;
        this.agentId = agentId;
    }

    public static AgentPushIdEnum getEnum(String code) {
        for (AgentPushIdEnum en : AgentPushIdEnum.values()) {
            if (en.getPushId().equals(code)) {
                return en;
            }
        }
        return AgentPushIdEnum.PUSHID_DEFAULT;
    }
}
