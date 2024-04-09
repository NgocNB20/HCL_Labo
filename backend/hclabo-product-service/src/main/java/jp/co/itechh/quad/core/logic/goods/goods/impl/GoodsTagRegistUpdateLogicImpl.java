package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.utility.ArrayFactoryUtility;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDisplayDao;
import jp.co.itechh.quad.core.dao.goods.goodstag.GoodsTagDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTagRegistUpdateLogic;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品タグ更新Logic処理
 *
 * @author Pham Quang Dieu
 */
@Component
public class GoodsTagRegistUpdateLogicImpl extends AbstractShopLogic implements GoodsTagRegistUpdateLogic {

    /** 商品タグDao */
    private final GoodsTagDao goodsTagDao;

    /** 商品グループ表示Daoクラス */
    private final GoodsGroupDisplayDao goodsGroupDisplayDao;

    /** 配列工場ユーティリティクラス */
    private final ArrayFactoryUtility arrayFactoryUtility;

    /**
     * 商品タグ登録
     *
     * @param arrayFactoryUtility 配列工場ユーティリティクラス
     * @param goodsTagDao 商品タグDaoクラス
     * @param goodsGroupDisplayDao 商品グループ表示Daoクラス
     */
    public GoodsTagRegistUpdateLogicImpl(ArrayFactoryUtility arrayFactoryUtility,
                                         GoodsTagDao goodsTagDao,
                                         GoodsGroupDisplayDao goodsGroupDisplayDao) {
        this.arrayFactoryUtility = arrayFactoryUtility;
        this.goodsTagDao = goodsTagDao;
        this.goodsGroupDisplayDao = goodsGroupDisplayDao;
    }

    /**
     * 商品タグ更新Logic処理
     * @param newGoodsGroupDisplayEntity 商品グループ表示クラス
     * @throws Exception
     */
    @Override
    public void execute(GoodsGroupDisplayEntity newGoodsGroupDisplayEntity) throws Exception {

        // 更新前の商品グループ表示EntityをDB検索して取得
        GoodsGroupDisplayEntity oldGoodsGroupDisplayEntity =
                        goodsGroupDisplayDao.getEntity(newGoodsGroupDisplayEntity.getGoodsGroupSeq());

        // 追加タグ、削除タグを選別

        // 商品タグ登録用
        if (ObjectUtils.isEmpty(oldGoodsGroupDisplayEntity)) {
            if (ObjectUtils.isNotEmpty(newGoodsGroupDisplayEntity.getGoodsTag())) {
                List<String> newList = arrayFactoryUtility.arrayToList(newGoodsGroupDisplayEntity.getGoodsTag());
                upsertGoodsTagData(newList, 1);
            }

            // 商品タグの更新について
        } else {

            if (ObjectUtils.isEmpty(newGoodsGroupDisplayEntity.getGoodsTag()) && ObjectUtils.isEmpty(
                            oldGoodsGroupDisplayEntity.getGoodsTag())) {
                return;

            } else if (ObjectUtils.isEmpty(oldGoodsGroupDisplayEntity.getGoodsTag()) && ObjectUtils.isNotEmpty(
                            newGoodsGroupDisplayEntity.getGoodsTag())) {
                List<String> newList = arrayFactoryUtility.arrayToList(newGoodsGroupDisplayEntity.getGoodsTag());
                upsertGoodsTagData(newList, 1);

            } else if (ObjectUtils.isNotEmpty(oldGoodsGroupDisplayEntity.getGoodsTag()) && ObjectUtils.isEmpty(
                            newGoodsGroupDisplayEntity.getGoodsTag())) {
                List<String> oldList = arrayFactoryUtility.arrayToList(oldGoodsGroupDisplayEntity.getGoodsTag());
                upsertGoodsTagData(oldList, -1);

            } else if (ObjectUtils.isNotEmpty(oldGoodsGroupDisplayEntity.getGoodsTag()) && ObjectUtils.isNotEmpty(
                            newGoodsGroupDisplayEntity.getGoodsTag())) {
                List<String> oldList = arrayFactoryUtility.arrayToList(oldGoodsGroupDisplayEntity.getGoodsTag());
                List<String> newList = arrayFactoryUtility.arrayToList(newGoodsGroupDisplayEntity.getGoodsTag());
                upsertGoodsTagData(oldList, -1);
                upsertGoodsTagData(newList, 1);
            }
        }
    }

    /**
     * 追加タグまた削除タグ
     *
     * @param count
     * @param goodsTagList
     */
    protected void upsertGoodsTagData(List<String> goodsTagList, int count) {

        for (String goodsTag : goodsTagList) {
            // 削除タグを更新
            goodsTagDao.upsertGoodsTag(goodsTag, count);
        }
    }
}