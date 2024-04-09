/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.mailmagazine.presentation.api;

import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberListResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * メルマガ Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class MailMagazineHelper {

    /**
     * レスポンスに変換
     *
     * @param mailMagazineMemberEntity  メルマガ購読者クラス
     * @return メルマガ会員レスポンス
     */
    public MailmagazineMemberResponse toMailmagazineMemberResponse(MailMagazineMemberEntity mailMagazineMemberEntity) {

        MailmagazineMemberResponse mailmagazineMemberResponse = new MailmagazineMemberResponse();

        if (mailMagazineMemberEntity != null) {

            mailmagazineMemberResponse.setMail(mailMagazineMemberEntity.getMail());
            mailmagazineMemberResponse.setMemberinfoSeq(mailMagazineMemberEntity.getMemberinfoSeq());
            mailmagazineMemberResponse.setRegistTime(mailMagazineMemberEntity.getRegistTime());
            mailmagazineMemberResponse.setUpdateTime(mailMagazineMemberEntity.getUpdateTime());
            mailmagazineMemberResponse.setUniqueMail(mailMagazineMemberEntity.getUniqueMail());
            mailmagazineMemberResponse.setSendStatus(EnumTypeUtil.getValue(mailMagazineMemberEntity.getSendStatus()));
        } else {
            mailmagazineMemberResponse = null;
        }

        return mailmagazineMemberResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param mailMagazineMemberEntityList メルマガ購読者クラス
     * @return メルマガ会員一覧レスポンス
     */
    public MailmagazineMemberListResponse toMailmagazineMemberListResponse(List<MailMagazineMemberEntity> mailMagazineMemberEntityList) {
        MailmagazineMemberListResponse mailmagazineMemberListResponse = new MailmagazineMemberListResponse();
        List<MailmagazineMemberResponse> mailmagazineMemberList = new ArrayList<>();

        for (MailMagazineMemberEntity mailMagazineMemberEntity : mailMagazineMemberEntityList) {
            MailmagazineMemberResponse mailmagazineMemberResponse =
                            toMailmagazineMemberResponse(mailMagazineMemberEntity);
            mailmagazineMemberList.add(mailmagazineMemberResponse);
        }

        mailmagazineMemberListResponse.setMailmagazineMemberList(mailmagazineMemberList);

        return mailmagazineMemberListResponse;
    }
}