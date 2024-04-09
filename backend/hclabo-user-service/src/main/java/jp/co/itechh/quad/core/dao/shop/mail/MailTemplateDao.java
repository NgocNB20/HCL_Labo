/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.mail;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.shop.mail.MailTemplateIndexDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * メールテンプレートDaoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface MailTemplateDao {
    /**
     * タイプでエンティティを取得
     * 標準を１件だけほしい。
     * 標準がないのであれば一般でもよい。
     *
     * @param shopSeq           間違ってとらないように保険
     * @param mailTemplateType  タイプ
     * @return エンティティ
     */
    @Select
    MailTemplateEntity getEntityByType(Integer shopSeq, HTypeMailTemplateType mailTemplateType);

    /**
     * シーケンスでエンティティを取得
     *
     * @param shopSeq           間違ってとらないように保険
     * @param mailTemplateSeq   シーケンス
     * @return エンティティ
     */
    @Select
    MailTemplateEntity getEntityBySeq(Integer shopSeq, Integer mailTemplateSeq);

    /**
     * メールテンプレート見出しを取得する
     *
     * @param shopSeq           ショップSEQ
     * @return 見出し一覧
     */
    @Select
    List<MailTemplateIndexDto> selectIndexList(Integer shopSeq);
}