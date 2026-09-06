package com.dcep.supergw.dto.dc443;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check443Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 8.3.18 对公钱包绑定账户通知报文<dcep.443.001.01>
 *
 * @author huangyang
 */
@JacksonXmlRootElement(localName = "BndngAcctPtcMgmtNtfctn", namespace = "http://www.dcep.com/dcep/44300101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.443.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check443Biz(groups = Priority.Lowest.class)
public class Dcep44300101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = -4488796462184974006L;


    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;


    /**
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;


    /**
     * 【回执信息】
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf rspsnInf;

    /**
     * 【管理类型】
     * MT01：身份认证
     * MT02：身份确认
     * MT03：解约申请
     * MT04：解约通知
     * MT05：网关签约
     * MT06：普通签约
     */
    @JacksonXmlProperty(localName = "MgmtTp")
    @Pattern(regexp = "MT01||MT02||MT03||MT04||MT05||MT06")
    @NotBlank(groups = Priority.Highest.class)
    private String mgmtTp;

    /**
     * 【协议签约信息】
     */
    @JacksonXmlProperty(localName = "PtcInf")
    @NotNull
    @Valid
    private PtcInf ptcInf;


    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override

    public boolean check(SoapHeader header) {

//        //当管理类型为“MT03”，“MT05”和“MT06”时，OrgnlGrpHdr必填
//        if(("MT03".equals(this.getMgmtTp()) ||"MT06".equals(this.getMgmtTp()) || "MT05".equals(this.getMgmtTp())) && this.getOrgnlGrpHdr()==null)
//            throw new DcepException(ErrorEnum.NULL_ERROR);
//
//        //签约行业务处理失败时，“业务回执状态”填写“PR01”，“业务拒绝码”根据实际情况填写，
//        // “业务拒绝原因”填写详细的拒绝原因信息；“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、
//        // “单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、
//        // “法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、
//        // “单笔兑出业务金额上限”、“日累计业务兑出笔数上限”、“日累计兑出金额上限”、“年累计兑出业务笔数上限”、
//        // “年累计兑出金额上限”等域不填写。
//        if ("PR01".equals(this.getRspsnInf().getRspsnSts())) {
//            if(StringUtils.isAnyBlank(this.getRspsnInf().getRjctCd(),this.getRspsnInf().getRjctInf()))
//                throw new DcepException(ErrorEnum.NULL_ERROR);
//            if(!StringUtils.isAllBlank(this.getPtcInf().getSgnAcctTp(),this.getPtcInf().getSgnAcctId(),this.getPtcInf().getSgnAcctNm(),this.getPtcInf().getCorprtnNm(),
//                    this.getPtcInf().getCorprtnIDTp(),this.getPtcInf().getCorprtnIDNo(),this.getPtcInf().getLglRepNm(),this.getPtcInf().getLglRepIDTp(),this.getPtcInf().getLglRepIDNo(),
//                    this.getPtcInf().getTel(),this.getPtcInf().getDlTtlCnt(),this.getPtcInf().getAnlTtlCnt())
//            || (null != this.getPtcInf().getSnglTxAmtLmt() || (null != this.getPtcInf().getDlTtlAmtLmt()) || (null != this.getPtcInf().getAnlTtlAmtLmt()) ))
//                throw new DcepException(ErrorEnum.VALIDATION_ERROR);
//        }
////账户开户行业务处理成功，且签约成功时：
////“业务回执状态”填写“PR00”，“业务拒绝码”、“业务拒绝原因”等域不填写；
////“签约协议号”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、
//// “法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、“协议生效日期”等域必须填写；
////“单笔兑出业务金额上限”、“日累计业务兑出笔数上限”、“日累计兑出金额上限”、“年累计兑出业务笔数上限”、“年累计兑出金额上限”等域则根据协议内容选择性填写。
//        if (("MT05".equals(this.getMgmtTp())
//                || "MT06".equals(this.getMgmtTp())) && "PR00".equals(this.getRspsnInf().getRspsnSts())) {
//            if(!StringUtils.isAllBlank(this.getRspsnInf().getRjctCd(),this.getRspsnInf().getRjctInf()))
//                throw new DcepException(ErrorEnum.VALIDATION_ERROR);
//            if(StringUtils.isAnyBlank(this.getPtcInf().getPtcId(),this.getPtcInf().getSgnAcctTp(),this.getPtcInf().getSgnAcctId(),this.getPtcInf().getSgnAcctNm(),this.getPtcInf().getCorprtnNm(),
//                    this.getPtcInf().getCorprtnIDTp(),this.getPtcInf().getCorprtnIDNo(),this.getPtcInf().getLglRepNm(),this.getPtcInf().getLglRepIDTp(),this.getPtcInf().getLglRepIDNo(),
//                    this.getPtcInf().getTel(),this.getPtcInf().getPtcFctvDt()))
//                throw new DcepException(ErrorEnum.NULL_ERROR);
//        }
////账户开户行业务处理成功，且解约成功时：
////“业务回执状态”填写“PR00”，“业务拒绝码”、“业务拒绝原因”等域不填写；
////“签约协议号”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、
//// “法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、等域必须填写。
//
//        if (("MT03".equals(this.getMgmtTp()) || "MT04".equals(this.getMgmtTp()))
//               && "PR00".equals(this.getRspsnInf().getRspsnSts())) {
//            if(!StringUtils.isAllBlank(this.getRspsnInf().getRjctCd(),this.getRspsnInf().getRjctInf()))
//                throw new DcepException(ErrorEnum.VALIDATION_ERROR);
//            if(StringUtils.isAnyBlank(this.getPtcInf().getPtcId(),this.getPtcInf().getSgnAcctTp(),this.getPtcInf().getSgnAcctId(),this.getPtcInf().getSgnAcctNm(),this.getPtcInf().getCorprtnNm(),
//                    this.getPtcInf().getCorprtnIDTp(),this.getPtcInf().getCorprtnIDNo(),this.getPtcInf().getLglRepNm(),this.getPtcInf().getLglRepIDTp(),this.getPtcInf().getLglRepIDNo(),
//                    this.getPtcInf().getTel()))
//                throw new DcepException(ErrorEnum.NULL_ERROR);
//        }


//当“管理类型”为“MT03-解约申请”时：“签约渠道”<SgnChnl>填写原申请报文的“签约渠道”。
//        if ("MT03".equals(this.getMgmtTp()) && this.getPtcInf().getSgnChnl().equals(this.))


        if ("MT03".equals(this.getMgmtTp()) && orgnlGrpHdr != null) {
            return CheckUtils.responseMsgChk(header, grpHdr, orgnlGrpHdr);
        } else {
            return CheckUtils.requestMsgChk(header, grpHdr);
        }

    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList =  fetchSecretFactor();
        List<String> cipherTextList =  encryptionHelper.encrypt(plainTextList);
        secretAssign(cipherTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList =  fetchSecretFactor();
        List<String> plainTextList =  encryptionHelper.decrypt(cipherTextList);
        secretAssign(plainTextList);
    }

    public List<String> fetchSecretFactor() {
        List<String> result = new ArrayList<>();
        if (this.ptcInf != null) {
            if (this.ptcInf.getSgnAcctId() != null) {
                result.add(this.ptcInf.getSgnAcctId());
            } else {
                result.add(null);
            }
            if (this.ptcInf.getSgnAcctNm() != null) {
                result.add(this.ptcInf.getSgnAcctNm());
            } else {
                result.add(null);
            }
            if (this.ptcInf.getLglRepNm() != null) {
                result.add(this.ptcInf.getLglRepNm());
            } else {
                result.add(null);
            }
            if (this.ptcInf.getLglRepIDNo() != null) {
                result.add(this.ptcInf.getLglRepIDNo());
            } else {
                result.add(null);
            }
            if (this.ptcInf.getTel() != null) {
                result.add(this.ptcInf.getTel());
            } else {
                result.add(null);
            }
            if (this.ptcInf.getWltId() != null) {
                result.add(this.ptcInf.getWltId());
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (this.ptcInf != null) {
            if (this.ptcInf.getSgnAcctId() != null) {
                this.ptcInf.setSgnAcctId(secretList.get(0));
            }
            if (this.ptcInf.getSgnAcctNm() != null) {
                this.ptcInf.setSgnAcctNm(secretList.get(1));
            }
            if (this.ptcInf.getLglRepNm() != null) {
                this.ptcInf.setLglRepNm(secretList.get(2));
            }
            if (this.ptcInf.getLglRepIDNo() != null) {
                this.ptcInf.setLglRepIDNo(secretList.get(3));
            }
            if (this.ptcInf.getTel() != null) {
                this.ptcInf.setTel(secretList.get(4));
            }
            if (this.ptcInf.getWltId() != null) {
                this.ptcInf.setWltId(secretList.get(5));
            }
        }
    }
}