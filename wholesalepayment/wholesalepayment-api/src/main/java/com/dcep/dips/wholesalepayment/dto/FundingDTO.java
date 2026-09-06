package com.dcep.dips.wholesalepayment.dto;

public interface FundingDTO extends RecordDTO {
    // 资金调整产品表
    String msgId();
    String msgTp();
    String adjustPtyId();
    String adjustTp();
    String adjustAmt();
    String currency();
    String cdtDbtInd();
    String creDtTm();
    String sendPtyId();
    String recvPtyId();
    String endToEndId();

    // 大额对接产品表
    String hvpsMsgTp();
    String hvpsBizTp();
    String hvpsBizKind();

    // 记账指令表
    String actgBizTp();
    String actgBizKind();
    String bizPrty();
    String mgmtTp();
    String dbtrPtyId();
    String abstractCd();
    String abstractDesc();

    default String presumeTm() { return null; }
}
