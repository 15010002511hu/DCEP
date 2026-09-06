
package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.dips.wholesalepayment.common.utils.PartitionUtil;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import lombok.*;

import java.util.Date;


/**
 * @author wangxiaoyu
 */
@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRecordDO {
    /**
     * 报文标识号
     */
    private String msgId;

    /**
     * 报文编号
     */
    private String msgTp;

    /**
     * 原报文标识号
     */
    private String orgMsgId;

    /**
     * 原报文编号
     */
    private String orgMsgTp;

    /**
     * 报文档案 密文存储
     */
    private String document;

    /**
     * 分区时间 数据库分区
     */
    private Date partitionTime;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    public void setMsgId(String msgId) {
        this.msgId = msgId;
        this.partitionTime = PartitionUtil.msgCreateTime(this.msgId);
    }

    public CommonRecordDO(String msgId, String msgTp, ZeroOutReqDTO zeroOutReqDTO) {
        setMsgId(msgId);
        this.msgTp = msgTp;
        this.orgMsgId = null;
        this.orgMsgTp = null;
        this.document = zeroOutReqDTO.encode();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public CommonRecordDO(RecordDTO recordDTO) {
        setMsgId(recordDTO.recMsgId());
        this.msgTp = recordDTO.recMsgTp();
        this.orgMsgId = recordDTO.recOrgnlMsgId();
        this.orgMsgTp = recordDTO.recOrgnlMsgTp();
        this.document = recordDTO.encode();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public CommonRecordDO(String msgId, String msgTp, String document) {
        setMsgId(msgId);
        this.msgTp = msgTp;
        this.orgMsgId = null;
        this.orgMsgTp = null;
        this.document = document;
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public CommonRecordDO(RecordDTO recordDTO, String document) {
        setMsgId(recordDTO.recMsgId());
        this.msgTp = recordDTO.recMsgTp();
        this.orgMsgId = recordDTO.recOrgnlMsgId();
        this.orgMsgTp = recordDTO.recOrgnlMsgTp();
        this.document = document;
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public CommonRecordDO(RecordDTO recordDTO, String document, MbridgeReqDTO mbridgeReqDTO) {
        setMsgId(recordDTO.recMsgId());
        this.msgTp = recordDTO.recMsgTp();
        this.orgMsgId = mbridgeReqDTO.getMcbsMsgId();
        this.orgMsgTp = mbridgeReqDTO.getMcbsMsgTp();
        this.document = document;
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public CommonRecordDO(String msgId, String msgTp) {
        setMsgId(msgId);
        this.msgTp = msgTp;
    }
}
