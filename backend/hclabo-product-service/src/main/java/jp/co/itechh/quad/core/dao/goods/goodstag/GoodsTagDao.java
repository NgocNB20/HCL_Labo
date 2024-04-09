package jp.co.itechh.quad.core.dao.goods.goodstag;

import jp.co.itechh.quad.core.dto.goods.goodstag.GoodsTagDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.sql.Timestamp;
import java.util.List;

/**
 * 商品タグ Daoクラス
 *
 * @author Pham Quang Dieu
 */
@Dao
@ConfigAutowireable
public interface GoodsTagDao {

    /**
     * 商品タグリスト取得
     *
     * @param dto タグ商品Dtoクラス
     * @return タグ商品リスト
     */
    @Select
    List<GoodsTagEntity> getGoodsTagList(GoodsTagDto dto, SelectOptions selectOptions);

    /**
     * 商品タグクリア
     *
     * @param deleteTime クリア基準日時
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int clearGoodsTag(Timestamp deleteTime);

    /**
     * 商品タグアップサート
     *
     * @param tag      タグ
     * @param addCount 追加
     * @return 登録件数
     */
    @Update(sqlFile = true)
    int upsertGoodsTag(String tag, int addCount);
}