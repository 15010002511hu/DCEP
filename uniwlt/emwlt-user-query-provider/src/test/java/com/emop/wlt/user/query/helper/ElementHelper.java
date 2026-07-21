package com.emop.wlt.user.query.helper;

import com.emop.infocache.api.dto.ReleaseHistoryDTO;
import com.emop.wlt.user.query.model.vo.Element;

public class ElementHelper {

    public static ReleaseHistoryDTO buildElementData() {
        ReleaseHistoryDTO releaseHistoryDTO = new ReleaseHistoryDTO();
        releaseHistoryDTO.setBizModule("100");
        releaseHistoryDTO.setCurrentReleaseSnapshotId("a61dad90f84648d780a4d6fa7833a27a");
        releaseHistoryDTO.setGrayReleaseSnapshotId("b4c5709784f24a5282910504f81e6564");
        releaseHistoryDTO.setCurTime(null);
        releaseHistoryDTO.setGrayTime(null);
        releaseHistoryDTO.setCurrentContent("");
        releaseHistoryDTO.setGrayContent("");
        return releaseHistoryDTO;
    }

    public static Element buildExpectedElementOfficial() {
        return Element.builder()
            .releaseId("a61dad90f84648d780a4d6fa7833a27a")
            .updateTime("")
            .content(null).build();
    }

    public static Element buildExpectedElementGray() {
        return Element.builder()
            .releaseId("b4c5709784f24a5282910504f81e6564")
            .updateTime("")
            .content(null).build();
    }

}
