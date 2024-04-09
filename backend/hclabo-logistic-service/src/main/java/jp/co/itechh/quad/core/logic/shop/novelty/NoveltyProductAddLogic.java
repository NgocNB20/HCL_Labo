package jp.co.itechh.quad.core.logic.shop.novelty;

import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentJudgmentDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAdd;

import java.util.List;
import java.util.Map;

/**
 *
 * NoveltyProductAddLogic<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface NoveltyProductAddLogic {

    /**
     * 受注商品にノベルティプレゼントの追加を行う（受注商品単位）
     *
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param noveltyGoodsRestOfStockMap ノベルティプレゼント残り在庫数マップ
     * @return ノベルティ商品追加一覧
     */
    public List<NoveltyProductAdd> addOrderGoodsNovelty(NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                                                        NoveltyPresentJudgmentDto judgmentDto,
                                                        Map<String, Integer> noveltyGoodsRestOfStockMap);

    /**
     * 受注商品にノベルティプレゼントの追加を行う（受注単位）
     *
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件
     * @param noveltyGoodsRestOfStockMap ノベルティプレゼント残り在庫数マップ
     * @return ノベルティ商品追加一覧
     */
    public List<NoveltyProductAdd> addOrderNovelty(NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                                                   Map<String, Integer> noveltyGoodsRestOfStockMap);
}