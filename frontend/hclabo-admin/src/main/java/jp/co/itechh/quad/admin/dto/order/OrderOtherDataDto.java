/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.entity.shop.questionnaire.QuestionReplyEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 受注その他情報Dtoクラス<br/>
 * 新規受注入力機能のお客様情報入力画面で入力される受注その他情報を保持する
 *
 * @author DtoGenerator
 *
 */
@Data
@Component
@Scope("prototype")
public class OrderOtherDataDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注サイト種別 */
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** メモ */
    private String memo;

    /** 受注確認メール送信フラグ */
    private boolean sendMailFlag;

    /** アンケート設問回答 */
    private List<QuestionReplyEntity> questionReplyEntityList;
}