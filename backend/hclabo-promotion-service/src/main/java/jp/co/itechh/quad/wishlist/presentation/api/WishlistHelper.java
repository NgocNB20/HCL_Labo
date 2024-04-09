package jp.co.itechh.quad.wishlist.presentation.api;

import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.wishlist.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistRegistResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * お気に入りエンドポイント Helper
 *
 * @author Doan THANG (VJP)
 * @version $Revision: 1.0 $
 *
 */
@Component
public class WishlistHelper {

    /**
     * お気に入り商品検索条件Dtoの作詞絵に変換
     *
     * @param memberinfoSeq 会員SEQ
     * @return お気に入り検索条件Dto
     */
    public WishlistSearchForDaoConditionDto toWishlistConditionDtoForSearchWishlistList(Integer memberinfoSeq) {
        // お気に入り検索条件Dtoの作成 公開状態＝指定なし
        WishlistSearchForDaoConditionDto wishlistConditionDto = new WishlistSearchForDaoConditionDto();

        wishlistConditionDto.setMemberInfoSeq(memberinfoSeq);

        return wishlistConditionDto;
    }

    /**
     * お気に入り情報一覧レスポンスに変換
     *
     * @param wishlistDtoList お気に入り情報リスト
     * @return お気に入り検索条件一覧レスポンス
     */
    public WishlistListResponse toWishlistListResponse(List<WishlistDto> wishlistDtoList) {
        WishlistListResponse wishlistListResponse = new WishlistListResponse();
        for (WishlistDto wishlistDto : wishlistDtoList) {
            WishlistResponse wishlistResponse = new WishlistResponse();
            wishlistResponse.setMemberInfoSeq(wishlistDto.getWishlistEntity().getMemberInfoSeq());
            wishlistResponse.setGoodsSeq(wishlistDto.getWishlistEntity().getGoodsSeq());
            wishlistResponse.setRegistTime(wishlistDto.getWishlistEntity().getRegistTime());
            wishlistResponse.setUpdateTime(wishlistDto.getWishlistEntity().getUpdateTime());
            wishlistResponse.setGoodsGroupSeq(wishlistDto.getGoodsDetailsDto().getGoodsGroupSeq());
            wishlistResponse.setVersionNo(wishlistDto.getGoodsDetailsDto().getVersionNo());
            wishlistResponse.setGoodsCode(wishlistDto.getGoodsDetailsDto().getGoodsCode());
            wishlistResponse.setGoodsTaxType(EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getGoodsTaxType()));
            wishlistResponse.setTaxRate(wishlistDto.getGoodsDetailsDto().getTaxRate());
            wishlistResponse.setAlcoholFlag(EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getAlcoholFlag()));
            wishlistResponse.setGoodsPriceInTax(wishlistDto.getGoodsDetailsDto().getGoodsPriceInTax());
            wishlistResponse.setGoodsPrice(wishlistDto.getGoodsDetailsDto().getGoodsPrice());
            wishlistResponse.setDeliveryType(wishlistDto.getGoodsDetailsDto().getDeliveryType());
            wishlistResponse.setSaleStatusPC(EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getSaleStatusPC()));
            wishlistResponse.setSaleStartTimePC(wishlistDto.getGoodsDetailsDto().getSaleStartTimePC());
            wishlistResponse.setSaleEndTimePC(wishlistDto.getGoodsDetailsDto().getSaleEndTimePC());
            wishlistResponse.setUnitManagementFlag(
                            EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getUnitManagementFlag()));
            wishlistResponse.setStockManagementFlag(
                            EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getStockManagementFlag()));
            wishlistResponse.setIndividualDeliveryType(
                            EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getIndividualDeliveryType()));
            wishlistResponse.setPurchasedMax(wishlistDto.getGoodsDetailsDto().getPurchasedMax());
            wishlistResponse.setFreeDeliveryFlag(
                            EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getFreeDeliveryFlag()));
            wishlistResponse.setOrderDisplay(wishlistDto.getGoodsDetailsDto().getOrderDisplay());
            wishlistResponse.setUnitValue1(wishlistDto.getGoodsDetailsDto().getUnitValue1());
            wishlistResponse.setUnitValue2(wishlistDto.getGoodsDetailsDto().getUnitValue2());
            wishlistResponse.setJanCode(wishlistDto.getGoodsDetailsDto().getJanCode());
            wishlistResponse.setSalesPossibleStock(wishlistDto.getGoodsDetailsDto().getSalesPossibleStock());
            wishlistResponse.setRealStock(wishlistDto.getGoodsDetailsDto().getRealStock());
            wishlistResponse.setOrderReserveStock(wishlistDto.getGoodsDetailsDto().getOrderReserveStock());
            wishlistResponse.setRemainderFewStock(wishlistDto.getGoodsDetailsDto().getRemainderFewStock());
            wishlistResponse.setOrderPointStock(wishlistDto.getGoodsDetailsDto().getOrderPointStock());
            wishlistResponse.setSafetyStock(wishlistDto.getGoodsDetailsDto().getSafetyStock());
            wishlistResponse.setGoodsGroupCode(wishlistDto.getGoodsDetailsDto().getGoodsGroupCode());
            wishlistResponse.setWhatsnewDate(wishlistDto.getGoodsDetailsDto().getWhatsnewDate());
            wishlistResponse.setGoodsOpenStatusPC(
                            EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getGoodsOpenStatusPC()));
            wishlistResponse.setOpenStartTimePC(wishlistDto.getGoodsDetailsDto().getOpenStartTimePC());
            wishlistResponse.setOpenEndTimePC(wishlistDto.getGoodsDetailsDto().getOpenEndTimePC());
            wishlistResponse.setGoodsGroupName(wishlistDto.getGoodsDetailsDto().getGoodsGroupName());
            wishlistResponse.setUnitTitle1(wishlistDto.getGoodsDetailsDto().getUnitTitle1());
            wishlistResponse.setUnitTitle2(wishlistDto.getGoodsDetailsDto().getUnitTitle2());
            wishlistResponse.setSnsLinkFlag(EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getSnsLinkFlag()));
            wishlistResponse.setMetaDescription(wishlistDto.getGoodsDetailsDto().getMetaDescription());
            wishlistResponse.setStockStatusPc(
                            EnumTypeUtil.getValue(wishlistDto.getGoodsDetailsDto().getStockStatusPc()));
            wishlistResponse.setGoodsNote1(wishlistDto.getGoodsDetailsDto().getGoodsNote1());
            wishlistResponse.setGoodsNote2(wishlistDto.getGoodsDetailsDto().getGoodsNote2());
            wishlistResponse.setGoodsNote3(wishlistDto.getGoodsDetailsDto().getGoodsNote3());
            wishlistResponse.setGoodsNote4(wishlistDto.getGoodsDetailsDto().getGoodsNote4());
            wishlistResponse.setGoodsNote5(wishlistDto.getGoodsDetailsDto().getGoodsNote5());
            wishlistResponse.setGoodsNote6(wishlistDto.getGoodsDetailsDto().getGoodsNote6());
            wishlistResponse.setGoodsNote7(wishlistDto.getGoodsDetailsDto().getGoodsNote7());
            wishlistResponse.setGoodsNote8(wishlistDto.getGoodsDetailsDto().getGoodsNote8());
            wishlistResponse.setGoodsNote9(wishlistDto.getGoodsDetailsDto().getGoodsNote9());
            wishlistResponse.setGoodsNote10(wishlistDto.getGoodsDetailsDto().getGoodsNote10());
            wishlistResponse.setOrderSetting1(wishlistDto.getGoodsDetailsDto().getOrderSetting1());
            wishlistResponse.setOrderSetting2(wishlistDto.getGoodsDetailsDto().getOrderSetting2());
            wishlistResponse.setOrderSetting3(wishlistDto.getGoodsDetailsDto().getOrderSetting3());
            wishlistResponse.setOrderSetting4(wishlistDto.getGoodsDetailsDto().getOrderSetting4());
            wishlistResponse.setOrderSetting5(wishlistDto.getGoodsDetailsDto().getOrderSetting5());
            wishlistResponse.setOrderSetting6(wishlistDto.getGoodsDetailsDto().getOrderSetting6());
            wishlistResponse.setOrderSetting7(wishlistDto.getGoodsDetailsDto().getOrderSetting7());
            wishlistResponse.setOrderSetting8(wishlistDto.getGoodsDetailsDto().getOrderSetting8());
            wishlistResponse.setOrderSetting9(wishlistDto.getGoodsDetailsDto().getOrderSetting9());
            wishlistResponse.setOrderSetting10(wishlistDto.getGoodsDetailsDto().getOrderSetting10());

            if (wishlistDto.getGoodsGroupImageEntityList() != null) {
                for (GoodsGroupImageEntity goodsGroupImageEntity : wishlistDto.getGoodsGroupImageEntityList()) {
                    GoodsGroupImageResponse goodsGroupImageResponse = new GoodsGroupImageResponse();
                    goodsGroupImageResponse.setGoodsGroupSeq(goodsGroupImageEntity.getGoodsGroupSeq());
                    goodsGroupImageResponse.setImageTypeVersionNo(goodsGroupImageEntity.getImageTypeVersionNo());
                    goodsGroupImageResponse.setImageFileName(goodsGroupImageEntity.getImageFileName());
                    goodsGroupImageResponse.setRegistTime(goodsGroupImageEntity.getRegistTime());
                    goodsGroupImageResponse.setUpdateTime(goodsGroupImageEntity.getUpdateTime());
                    if (wishlistResponse.getGoodsGroupImageResponseList() == null) {
                        wishlistResponse.setGoodsGroupImageResponseList(new ArrayList<>());
                    }
                    wishlistResponse.getGoodsGroupImageResponseList().add(goodsGroupImageResponse);
                }
            }

            if (wishlistDto.getGoodsInformationIconDetailsDtoList() != null) {
                for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : wishlistDto.getGoodsInformationIconDetailsDtoList()) {
                    GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse =
                                    new GoodsInformationIconDetailsResponse();
                    goodsInformationIconDetailsResponse.setGoodsGroupSeq(
                                    goodsInformationIconDetailsDto.getGoodsGroupSeq());
                    goodsInformationIconDetailsResponse.setIconSeq(goodsInformationIconDetailsDto.getIconSeq());
                    goodsInformationIconDetailsResponse.setIconName(goodsInformationIconDetailsDto.getIconName());
                    goodsInformationIconDetailsResponse.setColorCode(goodsInformationIconDetailsDto.getColorCode());
                    goodsInformationIconDetailsResponse.setOrderDisplay(
                                    goodsInformationIconDetailsDto.getOrderDisplay());

                    if (wishlistResponse.getGoodsInformationIconDetailsResponseList() == null) {
                        wishlistResponse.setGoodsInformationIconDetailsResponseList(new ArrayList<>());
                    }
                    wishlistResponse.getGoodsInformationIconDetailsResponseList()
                                    .add(goodsInformationIconDetailsResponse);
                }
            }

            wishlistResponse.setStockStatus(wishlistDto.getStockStatus());
            wishlistListResponse.addWishListListItem(wishlistResponse);
        }

        return wishlistListResponse;
    }

    /**
     * お気に入り情報登録レスポンスに変換
     *
     * @param wishlistEntity お気に入りクラス
     * @return お気に入り情報登録レスポンス
     */
    public WishlistRegistResponse toWishlistRegistResponse(WishlistEntity wishlistEntity) {
        WishlistRegistResponse wishlistRegistResponse = new WishlistRegistResponse();
        wishlistRegistResponse.setMemberInfoSeq(wishlistEntity.getMemberInfoSeq());
        wishlistRegistResponse.setGoodsSeq(wishlistEntity.getGoodsSeq());
        wishlistRegistResponse.setRegistTime(wishlistEntity.getRegistTime());
        wishlistRegistResponse.setUpdateTime(wishlistEntity.getRegistTime());

        return wishlistRegistResponse;
    }
}