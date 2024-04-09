/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.wishlist;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsIconItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsItem;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import jp.co.itechh.quad.wishlist.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 会員お気に入り Helperクラス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 */
@Component
public class MemberWishlistHelper {

    /**
     * 変換Helper
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    @Autowired
    public MemberWishlistHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * ページ変換、初期表示<br/>
     *
     * @param wishlistResponseList お気に入りDTOリスト
     * @param memberWishlistModel  ページ
     */
    public void toPageForLoad(List<WishlistResponse> wishlistResponseList, MemberWishlistModel memberWishlistModel) {

        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);

        List<GoodsItem> wishlistItems = new ArrayList<>();
        if (!wishlistResponseList.isEmpty()) {
            for (WishlistResponse wishlistResponse : wishlistResponseList) {

                GoodsItem wishlistItem = ApplicationContextUtility.getBean(GoodsItem.class);

                if (wishlistResponse != null) {

                    wishlistItem.setGoodsGroupSeq(wishlistResponse.getGoodsGroupSeq());
                    wishlistItem.setGoodsSeq(wishlistResponse.getGoodsSeq());
                    wishlistItem.setGcd(wishlistResponse.getGoodsCode());
                    wishlistItem.setGoodsGroupName(wishlistResponse.getGoodsGroupName());
                    wishlistItem.setGoodsDetailsNote(wishlistResponse.getGoodsNote1());
                    wishlistItem.setUnitTitle1(wishlistResponse.getUnitTitle1());
                    wishlistItem.setUnitValue1(wishlistResponse.getUnitValue1());
                    wishlistItem.setUnitTitle2(wishlistResponse.getUnitTitle2());
                    wishlistItem.setUnitValue2(wishlistResponse.getUnitValue2());
                    if (wishlistResponse.getGoodsOpenStatusPC() != null) {
                        wishlistItem.setGoodsOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                      wishlistResponse.getGoodsOpenStatusPC()
                                                                                     ));
                    }
                    wishlistItem.setOpenStartTime(conversionUtility.toTimestamp(wishlistResponse.getOpenStartTimePC()));
                    wishlistItem.setOpenEndTime(conversionUtility.toTimestamp(wishlistResponse.getOpenEndTimePC()));
                    if (wishlistResponse.getSaleStatusPC() != null) {
                        wishlistItem.setSaleStatus(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                                 wishlistResponse.getSaleStatusPC()
                                                                                ));
                    }
                    wishlistItem.setSaleStartTime(conversionUtility.toTimestamp(wishlistResponse.getSaleStartTimePC()));
                    wishlistItem.setSaleEndTime(conversionUtility.toTimestamp(wishlistResponse.getSaleEndTimePC()));
                    wishlistItem.setDeliveryType(wishlistResponse.getDeliveryType());
                    if (conversionUtility.toTimestamp(wishlistResponse.getWhatsnewDate()) != null) {
                        Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(
                                        conversionUtility.toTimestamp(wishlistResponse.getWhatsnewDate()));
                        wishlistItem.setWhatsnewDate(whatsnewDate);
                    }
                    wishlistItem.setStockStatusPc(wishlistResponse.getStockStatus());
                    wishlistItem.setGoodsPrice(wishlistResponse.getGoodsPrice());

                    // 税率、税種別の変換
                    wishlistItem.setTaxRate(wishlistResponse.getTaxRate());
                    if (wishlistResponse.getGoodsTaxType() != null) {
                        wishlistItem.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                                   wishlistResponse.getGoodsTaxType()
                                                                                  ));
                    }
                    // 税込価格の計算
                    wishlistItem.setGoodsPriceInTax(wishlistResponse.getGoodsPriceInTax());

                    // 商品画像リストを取り出す。
                    List<String> goodsImageList = new ArrayList<>();
                    if (wishlistResponse.getGoodsGroupImageResponseList() != null) {
                        for (GoodsGroupImageResponse goodsGroupImageEntity : wishlistResponse.getGoodsGroupImageResponseList()) {
                            goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                        }
                    }
                    wishlistItem.setGoodsImageItems(goodsImageList);

                    // アイコン情報の取得
                    List<GoodsIconItem> goodsIconList = new ArrayList<>();
                    if (wishlistResponse.getGoodsInformationIconDetailsResponseList() != null) {
                        for (GoodsInformationIconDetailsResponse goodsInformationIconDetailsDto : wishlistResponse.getGoodsInformationIconDetailsResponseList()) {
                            GoodsIconItem goodsIconItem = ApplicationContextUtility.getBean(GoodsIconItem.class);
                            goodsIconItem.setIconName(goodsInformationIconDetailsDto.getIconName());
                            goodsIconItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());
                            goodsIconList.add(goodsIconItem);
                        }
                    }
                    wishlistItem.setGoodsIconItems(goodsIconList);

                    // リスト追加
                    wishlistItems.add(wishlistItem);
                }
            }

            memberWishlistModel.setWishlistItems(wishlistItems);
        }
    }

}