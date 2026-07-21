package com.emop.wlt.user.management.manager;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.enums.IDTypeEnum;
import com.emop.wlt.id.db.service.EmwltDBIdGenerator;
import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.entity.Customer;
import com.emop.wlt.user.management.dto.CustomerSaveDTO;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.service.impl.CidIndexCustomerManagementServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CustomerSaveManager {

    private static final String CID = "cid";

    private static final String FLAG = "flag";

    private static final String CREATETIME = "createDatetime";

    private static final int COUNTRY_CODE_LENGTH = 2;

    @Autowired
    private EmwltDBIdGenerator emwltDBIdGenerator;

    @Autowired
    private CidIndexCustomerManagementServiceImpl cidIndexCustomerManagementService;

    /**
     * 插入/更新客户信息
     * @param in
     * @return
     */
    public String saveCustomer(CustomerSaveDTO in) {
        log.info("CustomerSaveDTO: {}", in);
        //证件号大写首尾去空格保存
        in.setIdNumber(in.getIdNumber().toUpperCase().trim());
        //根据用户信息提取或生成用户ID等信息
        final Map map = getCustom(in);
        String cid = map.get(CID).toString();
        //是否是新生成用户ID
        final Boolean flag = Boolean.valueOf(map.get(FLAG).toString());
        log.info("用户ID新生成Flag:{}", flag);
        //记录实名个人用户信息
        Customer customer = walletChangeTransformToPersonalUsers(in);
        //获取用户ID
        customer.setCid(cid);
        //如果发生主键或唯一键冲突，则比较用户相关要素是否一致，不一致，则更新
        if (flag) {
            cidIndexCustomerManagementService.saveCidIndexCustom(walletChangeTransformToCredential(customer), customer);
        } else {
            customer.setCreateDatetime((LocalDateTime) map.get(CREATETIME));
            if (cidIndexCustomerManagementService.checkExistByCid(customer)) {
                log.info("###更新###客户信息");
                cidIndexCustomerManagementService.updateByPrimaryKeyCustomer(customer);
            } else {
                log.info("避免cid_index有记录而customer无记录的兜底措施，记录客户信息");
                cidIndexCustomerManagementService.insertCustomer(customer);
            }
        }

        return cid;
    }

    private Map getCustom(CustomerSaveDTO in) {
        Map map = new HashMap<>();
        //判断客户ID是否是新生成
        Boolean flag = true;
        String cid;
        LocalDateTime createDatetime = null;
        //获取控制位编号
        int walletControlBeginIndex = 13;
        int walletControlEndIndex = 15;
        String walletControlCode = in.getWalletId().substring(walletControlBeginIndex, walletControlEndIndex);

        //根据用户实名证件号码和证件类型查询实名信息表  有信息则取实名用户ID
        log.info("根据证件号查询CustomerId");
        CidIndex cidIndex = idIndex(in.getIdNumber(), in.getIdType());
        if (cidIndex == null) {
            log.info("根据证件号没有查询到CustomerId");
            cid = generateCustomId(in.getCountryAndRegionCode(), in.getCustomerType(), walletControlCode);
        } else {
            log.info("根据证件号查询到CustomerId");
            cid = cidIndex.getCid();
            createDatetime = cidIndex.getCreateDatetime();
            flag = false;
        }
        map.put(CID, cid);
        map.put(CREATETIME, createDatetime);
        map.put(FLAG, flag);
        return map;
    }

    /**
     * 根据用户编号生成规则生成用户ID
     */
    private String generateCustomId(String country, String custTypeEnum, String walletControlCode) {
        //生成规则：用户ID编号以数字字母编号，18位，前2位码国家标识
        // 2位用户类型（对公/个人匿名/个人实名），中间9位顺序码， 4位钱包控制位 保留1位校验码
        //2020-09-09
        //考虑实名信息存储量扩容现cid生成规则改为如下
        //生成规则：用户ID编号以数字字母编号，18位
        //前2位码国家标识
        //2位用户类型（对公/个人匿名/个人实名）
        //11位顺序码前10位数字
        //2位钱包控制位
        //11位顺序码最后1位数字
        //保留1位校验码
        if (country.length() != COUNTRY_CODE_LENGTH) {
            log.error(String.format("传入的国家标识是:%s", country));
            ExceptionCast.cast(BaseErrorEnum.S02021, "国家标识传入长度有误");
        }
        StringBuilder str = new StringBuilder();
        String sequence = emwltDBIdGenerator.getCustomerId();
        //国家标识 用户类型标识 11位顺序码
        str.append(country)
            .append(custTypeEnum)
            .append(sequence, 0, 10)
            .append(walletControlCode)
            .append(sequence.substring(10));

        //取前17位数求和取模值作为校验位
        int sum = 0;
        for (byte b : str.toString().getBytes()) {
            sum = sum + b;
        }
        //一位校验码
        str.append(sum % 10);

        return str.toString();
    }

    /**
     * 从钱包输入提取实名用户信息
     */
    private Customer walletChangeTransformToPersonalUsers(CustomerSaveDTO in) {
        //实名用户信息
        Customer customer = new Customer();
        //实名用户信息实体字段赋值
        customer.setIdentityType(in.getIdType());
        customer.setIdentityNumber(in.getIdNumber());
        LocalDateTime now = LocalDateTime.now();
        customer.setCreateDatetime(now);
        customer.setUpdateDatetime(now);
        return customer;
    }

    /**
     * 根据机构上送信息提取身份证号-客户ID的关联关系

     */
    private CidIndex walletChangeTransformToCredential(Customer customer) {
        CidIndex cidIndex = new CidIndex();
        cidIndex.setIdentityNumber(customer.getIdentityNumber());
        cidIndex.setIdentityType(customer.getIdentityType());
        cidIndex.setCid(customer.getCid());
        cidIndex.setCreateDatetime(LocalDateTime.now());
        cidIndex.setUpdateDatetime(LocalDateTime.now());
        return cidIndex;
    }

    private CidIndex idIndex(String identity, String identityType) {
        if (StringUtils.isBlank(identityType)) {
            identityType = IDTypeEnum.IT01.getCode();
        }
        CidIndex cidIndex = new CidIndex();
        cidIndex.setIdentityNumber(identity);
        cidIndex.setIdentityType(identityType);
        return cidIndexCustomerManagementService.selectCidIndex(cidIndex);
    }
}
