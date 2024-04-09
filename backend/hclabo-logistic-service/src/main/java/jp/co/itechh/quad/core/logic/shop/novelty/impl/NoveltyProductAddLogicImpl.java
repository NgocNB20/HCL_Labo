package jp.co.itechh.quad.core.logic.shop.novelty.impl;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.dao.goods.stock.StockDao;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentEnclosureGoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentJudgmentDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyProductAddLogic;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAdd;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * NoveltyProductAddLogic<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class NoveltyProductAddLogicImpl implements NoveltyProductAddLogic {

    /** ノベルティプレゼント同梱商品Dao */
    private final NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao;

    /** 在庫情報Dao */
    private final StockDao stockDao;

    /** 商品API */
    private final IProductAdapter productAdapter;

    /**
     * コンストラクター
     * @param noveltyPresentEnclosureGoodsDao
     * @param stockDao
     * @param productAdapter
     */
    @Autowired
    public NoveltyProductAddLogicImpl(NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao,
                                      StockDao stockDao,
                                      IProductAdapter productAdapter) {
        this.noveltyPresentEnclosureGoodsDao = noveltyPresentEnclosureGoodsDao;
        this.stockDao = stockDao;
        this.productAdapter = productAdapter;
    }

    /**
     * 受注商品にノベルティプレゼントの追加を行う（受注商品単位）
     *
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param noveltyGoodsRestOfStockMap ノベルティプレゼント残り在庫数マップ
     * @return ノベルティ商品追加一覧
     */
    @Override
    public List<NoveltyProductAdd> addOrderGoodsNovelty(NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                                                        NoveltyPresentJudgmentDto judgmentDto,
                                                        Map<String, Integer> noveltyGoodsRestOfStockMap) {

        List<NoveltyProductAdd> noveltyProductAddList = new ArrayList<>();

        // ノベルティ商品を取得する
        Integer noveltyPresentConditionSeq = noveltyPresentConditionEntity.getNoveltyPresentConditionSeq();

        // 付与するノベルティ商品の数量
        int noveltyGoodsCount = 0;

        // 付与するノベルティ商品の数量を算出
        List<SecuredShippingItem> tgtGoods =
                        judgmentDto.getConditionMatchOrderGoodsMap().get(noveltyPresentConditionSeq);
        for (SecuredShippingItem entity : tgtGoods) {
            noveltyGoodsCount += entity.getShippingCount().getValue();
        }

        List<String> goodsCodeNoveltyList = noveltyPresentEnclosureGoodsDao.getGoodsCodeListByConditionSeqForBatch(
                        noveltyPresentConditionSeq);
        if (goodsCodeNoveltyList.isEmpty()) {
            return noveltyProductAddList;
        }

        for (String noveltyGoodsSeq : goodsCodeNoveltyList) {
            // 残り在庫を加味した注文数量を取得
            Integer itemCount =
                            getItemCountFromRestOfStock(noveltyGoodsSeq, noveltyGoodsCount, noveltyGoodsRestOfStockMap);
            if (itemCount == 0) {
                // 割り当て可能な数量が無い（０）の場合はノベルティ付与を行わない
                continue;
            }

            NoveltyProductAdd noveltyProductAdd = new NoveltyProductAdd();
            noveltyProductAdd.setItemId(noveltyGoodsSeq);
            noveltyProductAdd.setItemCount(itemCount);
            noveltyProductAdd.setNoveltyPresentConditionId(noveltyPresentConditionSeq);
            noveltyProductAddList.add(noveltyProductAdd);

        }
        return noveltyProductAddList;
    }

    /**
     * 受注商品にノベルティプレゼントの追加を行う（受注単位）
     *
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件
     * @param noveltyGoodsRestOfStockMap ノベルティプレゼント残り在庫数マップ
     * @return ノベルティ商品追加一覧
     */
    @Override
    public List<NoveltyProductAdd> addOrderNovelty(NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                                                   Map<String, Integer> noveltyGoodsRestOfStockMap) {

        List<NoveltyProductAdd> noveltyProductAddList = new ArrayList<>();

        // ノベルティ商品を取得する
        Integer noveltyPresentConditionSeq = noveltyPresentConditionEntity.getNoveltyPresentConditionSeq();
        List<String> goodsCodeNoveltyList = noveltyPresentEnclosureGoodsDao.getGoodsCodeListByConditionSeqForBatch(
                        noveltyPresentConditionSeq);
        if (ObjectUtils.isNotEmpty(goodsCodeNoveltyList)) {
            List<GoodsDetailsDto> goodsDetailsDtoList = productAdapter.getDetails(goodsCodeNoveltyList);
            if (ObjectUtils.isNotEmpty(goodsDetailsDtoList)) {
                for (GoodsDetailsDto goodsDetailsDto : goodsDetailsDtoList) {
                    if (HTypeNoveltyGoodsType.NOVELTY_GOODS.equals(goodsDetailsDto.getNoveltyGoodsType())) {
                        String goodsSeq = goodsDetailsDto.getGoodsSeq().toString();

                        // 残り在庫を加味した注文数量を取得
                        Integer itemCount = getItemCountFromRestOfStock(goodsSeq, 1, noveltyGoodsRestOfStockMap);
                        if (itemCount == 0) {
                            // 割り当て可能な数量が無い（０）の場合はノベルティ付与を行わない
                            continue;
                        }

                        NoveltyProductAdd noveltyProductAdd = new NoveltyProductAdd();
                        noveltyProductAdd.setNoveltyPresentConditionId(noveltyPresentConditionSeq);
                        noveltyProductAdd.setItemId(goodsSeq);
                        noveltyProductAdd.setItemCount(itemCount);
                        noveltyProductAddList.add(noveltyProductAdd);
                    }
                }
            }
        }

        return noveltyProductAddList;
    }

    /**
     * ノベルティ商品数量を取得<br/>
     *
     * @param noveltyGoodsSeq ノベルティ商品の商品SEQ
     * @param itemCount ノベルティ商品に割り当てたい数量
     * @param noveltyGoodsRestOfStockMap ノベルティプレゼント残り在庫数マップ
     * @return 残り在庫数も加味した上で算出した商品数量
     */
    private Integer getItemCountFromRestOfStock(String noveltyGoodsSeq,
                                                Integer itemCount,
                                                Map<String, Integer> noveltyGoodsRestOfStockMap) {
        Integer newItemCount = null;
        Integer restOfStock = null;

        // -----------------------------------------------
        // 残り在庫数を取得
        // -----------------------------------------------
        if (noveltyGoodsRestOfStockMap.containsKey(noveltyGoodsSeq)) {
            // マップ登録済の場合はマップから残り在庫数を取得
            restOfStock = noveltyGoodsRestOfStockMap.get(noveltyGoodsSeq);
        } else {
            // マップ未登録の場合は在庫テーブルを検索
            StockDto stockDto = stockDao.getStock(Integer.valueOf(noveltyGoodsSeq));
            restOfStock = stockDto.getSalesPossibleStock().intValue();
        }

        // -----------------------------------------------
        // ノベルティ商品に割り当てる数量を算出
        // -----------------------------------------------
        // 残り在庫数を加味して、今回ノベルティ商品に割り当てることのできる数量を判定
        if (restOfStock < itemCount) {
            // 注文数量が残り在庫数を上回っていれば、残り在庫数が割り当て数量となる
            newItemCount = restOfStock;
        } else {
            // 在庫が十分であれば、もともとの注文数量が割り当て数量となる
            newItemCount = itemCount;
        }

        // -----------------------------------------------
        // 残り在庫数を更新（マップに登録）
        // -----------------------------------------------
        noveltyGoodsRestOfStockMap.put(noveltyGoodsSeq, (restOfStock - newItemCount));

        // 注文数量を返却
        return newItemCount;
    }
}