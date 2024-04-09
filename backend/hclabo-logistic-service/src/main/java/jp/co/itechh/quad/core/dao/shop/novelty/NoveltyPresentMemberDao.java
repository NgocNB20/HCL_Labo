package jp.co.itechh.quad.core.dao.shop.novelty;

import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentMemberEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * ノベルティプレゼント対象会員Daoクラス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 */

@Dao
@ConfigAutowireable
public interface NoveltyPresentMemberDao {

    /**
     * エンティティ取得
     *
     * @param goodsSeq 商品SEQ
     * @return ノベルティプレゼント対象会員クラス
     */
    @Select
    NoveltyPresentMemberEntity getEntity(Integer noveltyPresentConditionId,
                                         Integer memberInfoSeq,
                                         String orderreceivedId,
                                         Integer goodsSeq);

    /**
     * 追加
     *
     * @param entity ノベルティプレゼント対象会員エンティティ
     * @return 処理件数
     */
    @Insert
    int insert(NoveltyPresentMemberEntity entity);

    /**
     * 削除
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteBySeq(Integer noveltyPresentConditionSeq);

    /**
     * ノベルティプレゼント条件SEQと会員SEQからエンティティのリストを取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @param memberInfoSeq 会員SEQ
     * @return エンティティのリスト
     */
    @Select
    List<Integer> getEntityListByMemberInfoSeq(Integer noveltyPresentConditionSeq, Integer memberInfoSeq);
}