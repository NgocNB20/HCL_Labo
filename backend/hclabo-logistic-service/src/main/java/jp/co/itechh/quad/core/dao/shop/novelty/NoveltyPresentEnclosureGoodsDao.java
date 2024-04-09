package jp.co.itechh.quad.core.dao.shop.novelty;

import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultGoodsDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentEnclosureGoodsEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * ノベルティプレゼント同梱商品Daoクラス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Dao
@ConfigAutowireable
public interface NoveltyPresentEnclosureGoodsDao {
    /**
     * インサート
     *
     * @param noveltyPresentEnclosureGoods ノベルティプレゼント同梱商品エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(NoveltyPresentEnclosureGoodsEntity noveltyPresentEnclosureGoods);

    /**
     * アップデート
     *
     * @param noveltyPresentEnclosureGoods ノベルティプレゼント同梱商品エンティティ
     * @return 処理件数
     */
    @Update
    int update(NoveltyPresentEnclosureGoodsEntity noveltyPresentEnclosureGoods);

    /**
     * デリート
     *
     * @param noveltyPresentEnclosureGoods ノベルティプレゼント同梱商品エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(NoveltyPresentEnclosureGoodsEntity noveltyPresentEnclosureGoods);

    /**
     * ノベルティプレゼント条件SEQを指定して削除
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteBySeq(Integer noveltyPresentConditionSeq);

    /**
     * ノベルティプレゼント条件SEQをキーにレコードを取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return 検索結果
     */
    @Select
    List<String> getGoodsCodeListByConditionSeq(Integer noveltyPresentConditionSeq);

    /**
     * ノベルティプレゼント条件SEQをキーにレコードを取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return 検索結果
     */
    @Select
    List<String> getGoodsCodeListByConditionSeqForBatch(Integer noveltyPresentConditionSeq);

    /**
     *
     * @param noveltyPresentConditionSeqList ノベルティプレゼント条件SEQのList
     * @return 検索結果（商品・在庫情報）
     */
    @Select
    List<NoveltyPresentSearchResultGoodsDto> getGoodsListBySeqList(List<Integer> noveltyPresentConditionSeqList);

}

