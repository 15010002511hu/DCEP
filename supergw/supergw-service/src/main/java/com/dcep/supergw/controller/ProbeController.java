package com.dcep.supergw.controller;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.DCEPPrcInf;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.dto.dc991.Dcep99100101DTO;
import com.dcep.supergw.dto.dc992.CZone;
import com.dcep.supergw.dto.dc992.CtrlNbLst;
import com.dcep.supergw.dto.dc992.Dcep99200101DTO;
import com.dcep.supergw.dto.dc992.GZone;
import com.dcep.supergw.dto.dc992.ProbeRspnInf;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.generator.ZoneRuleConfig;
import com.dubbo.ldc.model.Zone;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author duzhong
 * @version $Id: ExploringController.java, v 0.1 2021年4月19日 Administrator Exp $ 探测代码
 */

@Slf4j
@WebServlet(urlPatterns = "/exploring", asyncSupported = true, name = "exploringserver")
@BeansCondition(name = "beans.controller", havingValue = "ExploringController")
public class ProbeController extends HttpServlet {

    private static final long serialVersionUID = -274001323729666886L;
    private final static String VER = "01";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //开始验签
        doExecute(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void doExecute(HttpServletRequest req, HttpServletResponse resp) {
        String instgId = "";
        SoapHeader header = (SoapHeader) req.getAttribute("header");
        byte[] xml = (byte[]) req.getAttribute("xml");
        try {
            instgId = ((Dcep99100101DTO) SoapUtils.toDto(header, xml).getSoapBody().getT())
                .getProbeInf().getInstgId();
            /*
             * 获取所有Zone的信息
             */
            Class<?> clz = ZoneRuleConfig.class;
            Field f1 = clz.getDeclaredField("zoneMetaMap");
            f1.setAccessible(true);
            final Map<String, Zone> zoneMetaMap = (Map<String, Zone>) f1.get(clz);
            /*
             * 获取所有GZONE信息
             *  <GZone>
                    <IDCInf>bj-1|bj-2</IDCInf>
                </GZone>
             */
            String gzoneString = ZoneRuleConfig.getInstance().getGZAvailable().stream()
                .map(e -> zoneMetaMap.get(e.getZoneName()).getIdc())
                .collect(Collectors.joining("|"));
            /*
             * 获取所有CZONE信息
             *  <CZone>
                    <IDCInf>bj-1|bj-2|su-1</IDCInf>
                </CZone>
             */
            String czoneString = ZoneRuleConfig.getInstance().getCZAvailable().stream()
                .map(e -> zoneMetaMap.get(e.getZoneName()).getIdc())
                .collect(Collectors.joining("|"));
            /*
             * 拼接所有分片信息,内容为分片号 + 该分片所在idc + 该分片所在城市的所有其他分片的
             * Rzone的IdcInf规则为，当前分片所在的idc | 同城其他的idc | 异地其他的idc
             * 其中同城其他的idc以及异地其他的idc均为字典排序
             * <RZone>
                    <CtrlNbLst>
                        <CtrlNbRng>88</CtrlNbRng>
                        <IDCInf>su-1|bj-1|bj-2</IDCInf>
                    </CtrlNbLst>
                    ......
                </Rzone>

             */
            Map<String, String> shardingMap = ZoneClient.getInstance().getAllShardingInfo(Constant.RZONEGROUP);
            Collection<String> rz = shardingMap.values();
            List<CtrlNbLst> resList = new ArrayList<CtrlNbLst>();
            for (Entry<String, String> shardingEnt : shardingMap.entrySet()) {
                Zone tempZone = zoneMetaMap.get(shardingEnt.getValue());
                String idc = tempZone.getIdc();
                //String otherIdcInSameCity = idc + "|"
                String otherIdc = zoneMetaMap.entrySet().stream()
                    // 相同city
//                        .filter(e -> e.getValue().getCity().equals(tempZone.getCity()))
                    // 去除本身
                    .filter(e -> !e.getValue().getIdc().equals(idc))
                    // 提供服务的（即有处理分片）
                    .filter(e -> rz.contains(e.getValue().getZoneName()))
                    .distinct().sorted(new Comparator<Entry<String, Zone>>() {
                        /**
                         * 比较大小
                         * @param o1
                         * @param o2
                         * @return
                         */
                        @Override
                        public int compare(Entry<String, Zone> o1, Entry<String, Zone> o2) {
                            Zone zone1 = o1.getValue();
                            Zone zone2 = o2.getValue();
                            if (zone1.getCity().equals(tempZone.getCity()) && !zone2.getCity()
                                .equals(tempZone.getCity())) {
                                //当zone1的城市和当前分片的城市为同一个，zone2的城市和当前分片的城市不为同一个，则zone1应该小于zone2，即返回-1
                                return -1;
                            } else if (!zone1.getCity().equals(tempZone.getCity()) && zone2.getCity()
                                .equals(tempZone.getCity())) {
                                //当zone1的城市和当前分片的城市不为同一个，zone2的城市和当前分片的城市为同一个，则zone1应该大于zone2，即返回1
                                return 1;
                            } else {
                                //当zone1和zone2与当前分片同为一个城市，或者zone1和zone2与当前分片不为同一个城市，按照字典大小返回。
                                return zone1.getIdc().compareTo(zone2.getIdc());
                            }
                        }
                    })
                    .map(e -> e.getValue().getIdc())
                    .collect(Collectors.joining("|"));

                resList.add(
                    new CtrlNbLst(shardingEnt.getKey(), idc + (StringUtils.isEmpty(otherIdc) ? "" : ("|" + otherIdc))));
            }

            ProbeRspnInf probeRspnInf = new ProbeRspnInf(instgId, new GZone(gzoneString), new CZone(czoneString),
                resList);
            Dcep99200101DTO dto992 = new Dcep99200101DTO(new DCEPPrcInf("PR00", "I0000", null), probeRspnInf);
            send992DTO(resp, header, dto992);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
            log.error("报文{},获取zone信息出错{}", new String(xml), e2);
            EnvelopeDTO<Dcep91100101DTO> dto911 = GwMsgUtils.dcep911(header.getMsgSN(), InfoCacheUtils.getPbocInf(),
                header.getSender(), GwErrorEnum.ZONE_INFO_ERROR.getCode(),
                GwErrorEnum.ZONE_INFO_ERROR.getDescription(), InfoCacheUtils.getPbocInf(), e2.getMessage());
            GwMsgUtils.writerXmlToInst(resp, SoapUtils.toXml(dto911), (header.getSender()));
        } catch (Exception e3) {
            log.error("侦测报文{}处理失败{}", new String(xml), e3);
            EnvelopeDTO<Dcep91100101DTO> dto911 = GwMsgUtils.dcep911(header.getMsgSN(), InfoCacheUtils.getPbocInf(),
                header.getSender(), GwErrorEnum.VALIDATION_ERROR.getCode(),
                GwErrorEnum.VALIDATION_ERROR.getDescription(), InfoCacheUtils.getPbocInf(), e3.getMessage());
            GwMsgUtils.writerXmlToInst(resp, SoapUtils.toXml(dto911), (header.getSender()));
        } finally {
            MDC.clear();
        }
    }

    public void send992DTO(HttpServletResponse resp, SoapHeader header, Dcep99200101DTO dto992) {

        SoapHeader soapHeader = new SoapHeader(VER, DcepDateUtils.getDcepDateStrNow(), "dcep.992.001.01",
            header.getMsgSN(), InfoCacheUtils.getPbocInf(), header.getSender());

        SoapBody<Dcep99200101DTO> soapBody = new SoapBody<>();
        soapBody.setT(dto992);

        EnvelopeDTO<Dcep99200101DTO> envelopeDTO = new EnvelopeDTO<>();
        envelopeDTO.setSoapHeader(soapHeader);
        envelopeDTO.setSoapBody(soapBody);

        GwMsgUtils.writerXmlToInst(resp, SoapUtils.toXml(envelopeDTO), (header.getSender()));
    }
}
