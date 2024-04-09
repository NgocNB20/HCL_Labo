package jp.co.itechh.quad.core.service.shop.novelty;

import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentValidateDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;

/**
 * ノベルティプレゼント登録更新サービス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface NoveltyPresentRegistService {
    /**
     * ノベルティプレゼント条件を登録する
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     * @param noveltyPresentValidateDto ノベルティプレゼント同梱商品エンティティのリスト
     * @return 登録件数
     */
    int regist(NoveltyPresentConditionEntity conditionEntity, NoveltyPresentValidateDto noveltyPresentValidateDto);

    /**
     * ノベルティプレゼント条件を更新する
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     * @param noveltyPresentValidateDto ノベルティプレゼント同梱商品エンティティのリスト
     * @return 更新件数
     */
    int update(NoveltyPresentConditionEntity conditionEntity, NoveltyPresentValidateDto noveltyPresentValidateDto);

    /**
     * ノベルティプレゼント条件を削除する
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return 削除件数
     */
    int delete(Integer noveltyPresentConditionSeq);
}
