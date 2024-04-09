/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.memberinfo.confirmmail;

import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 確認メールDaoクラス<br/>
 *
 * @author thang
 *
 */
@Dao
@ConfigAutowireable
public interface ConfirmMailDao {

    /**
     * 削除<br/>
     * @param confirmMailEntity 確認メール情報
     * @return 削除件数
     */
    @Delete
    int delete(ConfirmMailEntity confirmMailEntity);

    /**
     * 確認メール情報取得<br/>
     * @param confirmMailSeq 確認メールSEQ
     * @return 確認メールエンティティ
     */
    @Select
    ConfirmMailEntity getEntity(Integer confirmMailSeq);

    /**
     * 登録<br/>
     * @param confirmMailEntity 確認メール情報
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(ConfirmMailEntity confirmMailEntity);

    /**
     * 更新<br/>
     * @param confirmMailEntity 確認メール情報
     * @return 更新件数
     */
    @Update
    int update(ConfirmMailEntity confirmMailEntity);

    /**
     * 確認メールSEQを取得する<br/>
     * @return 確認メールSEQ
     */
    @Select
    Integer getConfirmMailSeqNextVal();

    /**
     * パスワードが一致し、現在日時が有効期限内にある確認メール情報を取得する<br/>
     *
     * @param password パスワード
     * @return 確認メールエンティティ
     */
    @Select
    ConfirmMailEntity getEntityByPassword(String password);

    /**
     * パスワードと確認メール種別が一致し、現在日時が有効期限内にある確認メール情報を取得する<br/>
     *
     * @param password パスワード
     * @param confirmMailType 確認メール種別
     * @return 確認メールエンティティ
     */
    @Select
    ConfirmMailEntity getEntityByType(String password, HTypeConfirmMailType confirmMailType);

    /**
     * 確認メールパスワードが一致する確認メール情報を削除する<br/>
     *
     * @param confirmMailPassword 確認メールパスワード
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteByConfirmMailPassword(String confirmMailPassword);

}