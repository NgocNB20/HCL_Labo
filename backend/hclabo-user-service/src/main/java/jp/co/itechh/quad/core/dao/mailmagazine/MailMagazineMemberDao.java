/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.mailmagazine;

import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * メルマガ購読者Dao<br/>
 *
 * @author kimura
 */
@Dao
@ConfigAutowireable
public interface MailMagazineMemberDao {

    /**
     * 追加<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(MailMagazineMemberEntity mailMagazineMemberEntity);

    /**
     * 更新<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者
     * @return 更新件数
     */
    @Update
    int update(MailMagazineMemberEntity mailMagazineMemberEntity);

    /**
     * 削除<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者
     * @return 削除件数
     */
    @Delete
    int delete(MailMagazineMemberEntity mailMagazineMemberEntity);

    /**
     * エンティティ取得<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @return メルマガ購読者エンティティ
     */
    @Select
    MailMagazineMemberEntity getEntity(Integer memberInfoSeq);

    /**
     * メールマガジン購読者情報リストをメールで取得<br/>
     *
     * @param memberInfoUniqueId 一意制約用メールアドレス
     * @return ニュースレター購読者エンティティ リスト
     */
    @Select
    List<MailMagazineMemberEntity> getByMemberInfoUniqueId(String memberInfoUniqueId);

    /**
     * エンティティリスト取得
     *
     * @return メルマガ購読者エンティティリスト
     */
    @Select
    List<MailMagazineMemberEntity> getEntityList();
}
