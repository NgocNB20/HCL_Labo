/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;

import java.util.List;

/**
 * 商品グループデータチェック
 *
 * @author hirata
 * @version $Revision: 1.7 $
 */
public interface GoodsGroupDataCheckLogic {
    // LGP0006

    /**
     * 商品グループコード重複エラー
     * <code>MSGCD_GOODSGROUPCODE_REPETITION_FAIL</code>
     */
    public static final String MSGCD_GOODSGROUPCODE_REPETITION_FAIL = "LGP000601";

    /**
     * カテゴリなしエラー
     * <code>MSGCD_CATEGORY_NONE_FAIL</code>
     */
    public static final String MSGCD_CATEGORY_NONE_FAIL = "LGP000602";

    /**
     * 関連商品グループなしエラー
     * <code>MSGCD_RELATION_GOODSGROUP_NONE_FAIL</code>
     */
    public static final String MSGCD_RELATION_GOODSGROUP_NONE_FAIL = "LGP000603";

    /**
     * 全商品削除エラー
     * <code>MSGCD_ALL_GOODS_DELETED_FAIL</code>
     */
    public static final String MSGCD_ALL_GOODS_DELETED_FAIL = "LGP000604";

    /**
     * インフォメーションアイコンPCなしエラー
     * <code>MSGCD_INFORMATIONICONPC_NONE_FAIL</code>
     */
    public static final String MSGCD_INFORMATIONICONPC_NONE_FAIL = "LGP000609";

    /**
     * 登録カテゴリ重複登録エラー
     * <code>MSGCD_CATEGORY_MULTI_REGIST_FAIL</code>
     */
    public static final String MSGCD_CATEGORY_MULTI_REGIST_FAIL = "LGP000611";

    /**
     * 関連商品重複登録エラー
     * <code>MSGCD_GOODSRELATION_MULTI_REGIST_FAIL</code>
     */
    public static final String MSGCD_GOODSRELATION_MULTI_REGIST_FAIL = "LGP000612";

    /**
     * 商品グループ更新時の新着日付未設定エラー
     * <code>MSGCD_WHATSNEWDATE_NONE_FAIL</code>
     */
    public static final String MSGCD_WHATSNEWDATE_NONE_FAIL = "LGP000613";

    /**
     *
     * 商品グループデータチェック
     * 商品グループDTOの登録・更新前チェックを行う。
     *
     * @param goodsGroupDto 商品グループDto
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param shopSeq ショップSEQ
     */
    void execute(GoodsGroupDto goodsGroupDto, List<GoodsRelationEntity> goodsRelationEntityList, Integer shopSeq);

    /**
     * 商品が利用可能か検査する
     * <p></p>
     * @param goodsGroupSeq 商品グループSEQ
     * @param siteType PC/MB/Admin
     * @return trueの場合は指定した商品を利用可能.それ以外は不可
     */
    boolean isAvailable(Integer goodsGroupSeq, HTypeSiteType siteType);
}