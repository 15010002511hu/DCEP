package com.dcep.dips.wholesalepayment.dto;

import java.io.Serializable;
import java.util.List;

import com.dcep.dips.wholesalepayment.enums.TransTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ContractInfo implements Serializable {

    private static final long serialVersionUID = -3389554878496338200L;

    private boolean hasContract;
    private TransTypeEnum transType;
    private String dbtrWalletId;
    private List<String> dbtrContractIds;
    private String cdtrWalletId;
    private String cdtrContractId;
}
