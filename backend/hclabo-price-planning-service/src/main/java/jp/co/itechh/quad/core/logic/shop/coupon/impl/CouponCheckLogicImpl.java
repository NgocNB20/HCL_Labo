/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon.impl;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponCheckLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponCodeCheckLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponTimeCheckLogic;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.TargetMembersListRequest;
import jp.co.itechh.quad.customer.presentation.api.param.TargetMembersListResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsCodeListRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsCodeListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * クーポンチェックLogic実装クラス
 *
 * @author s_tsuru
 */
@Component
public class CouponCheckLogicImpl extends AbstractShopLogic implements CouponCheckLogic {

    /** クーポンDAO */
    private final CouponDao couponDao;

    /** クーポンコードチェックロジック */
    private final CouponCodeCheckLogic couponCodeCheckLogic;

    /** クーポン開催日時チェックロジック */
    private final CouponTimeCheckLogic couponTimeCheckLogic;

    /** 日付Utility */
    private final DateUtility dateUtility;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    private final ProductApi productApi;

    private final CustomerApi customerApi;

    @Autowired
    public CouponCheckLogicImpl(CouponDao couponDao,
                                CouponCodeCheckLogic couponCodeCheckLogic,
                                CouponTimeCheckLogic couponTimeCheckLogic,
                                DateUtility dateUtility,
                                ConversionUtility conversionUtility,
                                ProductApi productApi,
                                CustomerApi customerApi) {
        this.couponDao = couponDao;
        this.couponCodeCheckLogic = couponCodeCheckLogic;
        this.couponTimeCheckLogic = couponTimeCheckLogic;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
        this.productApi = productApi;
        this.customerApi = customerApi;
    }

    /**
     * 新規登録クーポンチェック処理。
     *
     * @param coupon チェック対象のクーポン
     */
    @Override
    public void checkForRegist(CouponEntity coupon) {

        clearErrorList();
        // 対象商品重複入力チェック
        targetGoodsDuplicationCheck(coupon);
        // 対象会員重複入力チェック
        targetMembersDuplicationCheck(coupon);

        // クーポンIDが既存のクーポンと重複していないことを確認
        // 重複している場合はエラーメッセージをセットする
        String couponId = coupon.getCouponId();
        Integer shopSeq = 1001;
        int idCount = couponDao.checkCouponId(couponId, shopSeq);
        // 登録しようとしたクーポンIDで1件以上のクーポンが取得できた場合はエラーとする
        if (idCount != 0) {
            addErrorMessage(MSGCD_REPETITION_COUPONID);
        }

        // 開始日時が現在より未来であることを確認
        if (couponTimeCheckLogic.execute(coupon.getCouponStartTime()) <= 0) {
            addErrorMessage(MSGCD_CANNOT_SET_STRATTIME);
        }

        // クーポンコードが利用不可であればエラーメッセージをセット
        if (!couponCodeCheckLogic.execute(coupon)) {
            addErrorMessage(MSGCD_REPETITION_COUPONCODE);
        }

        // 対象商品存在チェック
        targetGoodsNotExistCheck(coupon);
        // 対象会員存在チェック
        targetMembersNotExistCheck(coupon);

        // エラーを表示
        if (hasErrorList()) {
            throwMessage();
        }
    }

    /**
     * 更新クーポンチェック処理。
     *
     * @param preUpdateCoupon  更新前のクーポン
     * @param postUpdateCoupon 更新後のクーポン
     */
    @Override
    public void checkForUpdate(CouponEntity preUpdateCoupon, CouponEntity postUpdateCoupon) {

        clearErrorList();
        // 対象商品重複入力チェック
        targetGoodsDuplicationCheck(postUpdateCoupon);
        // 対象会員重複入力チェック
        targetMembersDuplicationCheck(postUpdateCoupon);

        // 利用期間が終了しているものはエラーメッセージをセット
        if (!dateUtility.isOpen(null, preUpdateCoupon.getCouponEndTime())) {
            this.throwMessage(MSGCD_CANNOT_CAHNGE_COUPONDATA);
        }

        // クーポンの開催が終了していないものにチェックを行う
        // 開催中の場合は以下のチェックを行う
        if (couponTimeCheckLogic.execute(preUpdateCoupon.getCouponStartTime()) <= 0) {

            // 開始日時が変更されていないこと
            if (preUpdateCoupon.getCouponStartTime().compareTo(postUpdateCoupon.getCouponStartTime()) != 0) {
                this.addErrorMessage(MSGCD_CANNOT_CHANGE_STARTTIME);
            }

            // クーポンコードが変更されていないこと
            if (StringUtils.isNotEmpty(preUpdateCoupon.getCouponCode()) && !preUpdateCoupon.getCouponCode()
                                                                                           .equals(postUpdateCoupon.getCouponCode())) {
                this.addErrorMessage(MSGCD_CANNOT_CHANGE_COUPONCODE);
            }

            //割引種別が変更されていないこと
            if (ObjectUtils.isNotEmpty(preUpdateCoupon.getDiscountType()) && !preUpdateCoupon.getDiscountType()
                                                                                             .equals(postUpdateCoupon.getDiscountType())) {
                this.addErrorMessage(MSGCD_CANNOT_CHANGE_DISCOUNTTYPE);
            }

            // 開催前の場合は以下のチェックを行う
        } else {

            // 開始日時が現在より未来であることを確認
            if (couponTimeCheckLogic.execute(postUpdateCoupon.getCouponStartTime()) <= 0) {
                this.addErrorMessage(MSGCD_CANNOT_SET_STRATTIME);
            }

            // クーポンコードが利用不可であればエラーメッセージをセット
            if (StringUtils.isNotEmpty(preUpdateCoupon.getCouponCode()) && !preUpdateCoupon.getCouponCode()
                                                                                           .equals(postUpdateCoupon.getCouponCode())) {
                if (!couponCodeCheckLogic.execute(postUpdateCoupon)) {
                    this.addErrorMessage(MSGCD_REPETITION_COUPONCODE);
                }
            }
        }
        // クーポンコードが利用不可であればエラーメッセージをセット
        if (!couponCodeCheckLogic.execute(postUpdateCoupon)) {
            addErrorMessage(MSGCD_REPETITION_COUPONCODE);
        }

        // 対象商品存在チェック
        targetGoodsNotExistCheck(postUpdateCoupon);
        // 対象会員存在チェック
        targetMembersNotExistCheck(postUpdateCoupon);

        // エラーを表示
        if (hasErrorList()) {
            throwMessage();
        }
    }

    /**
     * 対象商品重複入力チェック
     *
     * @param coupon チェック対象のクーポン
     */
    protected void targetGoodsDuplicationCheck(CouponEntity coupon) {
        if (coupon.getTargetGoods() == null) {
            return;
        }
        // 対象商品リスト作成
        List<String> targetGoodsList = Arrays.asList(conversionUtility.toDivArray(coupon.getTargetGoods()));
        dataDuplicationCheck(targetGoodsList, MSGCD_DUPLICATION_TARGET_GOODS);
    }

    /**
     * 対象会員重複入力チェック
     *
     * @param coupon チェック対象のクーポン
     */
    protected void targetMembersDuplicationCheck(CouponEntity coupon) {
        if (coupon.getTargetMembers() == null) {
            return;
        }
        // 対象会員リスト作成
        List<String> targetMembersList = Arrays.asList(conversionUtility.toDivArray(coupon.getTargetMembers()));
        dataDuplicationCheck(targetMembersList, MSGCD_DUPLICATION_TARGET_MEMBERS);
    }

    /**
     * 入力データ重複チェック処理。
     *
     * @param checkList   重複チェックリスト
     * @param messageCode メッセージコード
     */
    protected void dataDuplicationCheck(List<String> checkList, String messageCode) {
        Set<String> checkSet = new HashSet<>();
        Set<String> messageSet = new HashSet<>();
        for (String data : checkList) {
            if (checkSet.contains(data)) {
                if (!messageSet.contains(data)) {
                    messageSet.add(data);
                    addErrorMessage(messageCode, new Object[] {data});
                }
            } else {
                checkSet.add(data);
            }
        }
    }

    /**
     * 対象商品存在チェック
     *
     * @param coupon チェック対象のクーポン
     */
    protected void targetGoodsNotExistCheck(CouponEntity coupon) {
        if (coupon.getTargetGoods() == null) {
            return;
        }
        // 対象商品リスト作成
        List<String> targetGoodsList = Arrays.asList(conversionUtility.toDivArray(coupon.getTargetGoods()));
        // DB存在チェック用リスト取得
        GoodsCodeListRequest goodsCodeListRequest = new GoodsCodeListRequest();
        goodsCodeListRequest.setGoodsCodeList(targetGoodsList);
        GoodsCodeListResponse goodsCodeListResponse = productApi.getEntityListByGoodsCodeList(goodsCodeListRequest);
        if (goodsCodeListResponse != null) {
            List<String> checkGoodsList = goodsCodeListResponse.getCheckGoodsList();
            for (String goodsCode : targetGoodsList) {
                if (!checkGoodsList.contains(goodsCode)) {
                    addErrorMessage(MSGCD_NOT_EXIST_TARGET_GOODS, new Object[] {goodsCode});
                }
            }
        }
    }

    /**
     * 対象会員存在チェック
     *
     * @param coupon チェック対象のクーポン
     */
    protected void targetMembersNotExistCheck(CouponEntity coupon) {
        if (coupon.getTargetMembers() == null) {
            return;
        }
        // 対象会員リスト作成
        List<String> targetMembersList = Arrays.asList(conversionUtility.toDivArray(coupon.getTargetMembers()));
        // DB存在チェック用リスト取得
        TargetMembersListRequest targetMembersListRequest = new TargetMembersListRequest();

        targetMembersListRequest.setTargetMembersList(targetMembersList);

        TargetMembersListResponse targetMembersListResponse =
                        customerApi.getEntityListByMemberInfoIdList(targetMembersListRequest);

        List<String> checkMembersList = targetMembersListResponse.getCheckMembersList();

        for (String memberId : targetMembersList) {
            if (!checkMembersList.contains(memberId)) {
                addErrorMessage(MSGCD_NOT_EXIST_TARGET_MEMBERS, new Object[] {memberId});
            }
        }
    }

}