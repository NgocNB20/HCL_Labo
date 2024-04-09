/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 問い合わせ詳細Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.1 $
 */
@Data
@Component
@Scope("prototype")
public class InquiryDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせ */
    private InquiryEntity inquiryEntity;

    /** 問い合わせ分類名 */
    private String inquiryGroupName;

    /** 問い合わせ内容リスト */
    private List<InquiryDetailEntity> inquiryDetailEntityList;

    /** 会員Entity */
    private MemberInfoEntity memberInfoEntity;

}