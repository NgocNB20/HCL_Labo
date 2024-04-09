/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.application.listener;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.application.commoninfo.impl.HmFrontUserDetails;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.web.HeaderParamsHelper;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipMergeRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * 認証用イベントリスナ
 */
public class AuthenticationEventListeners {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEventListeners.class);

    /**
     * ログイン処理がすべて成功したことを通知するためのイベントクラス<br/>
     * ゲストのカートを更新
     *
     * @param event
     */
    @EventListener
    public void interactiveAuthenticationSuccessEvent(InteractiveAuthenticationSuccessEvent event) {

        // Remember-Me機能だけを利用する
        boolean isRemember = event.getAuthentication() instanceof RememberMeAuthenticationToken;
        if (!isRemember) {
            return;
        }

        // TODO 認証成功処理を実装するクラスが２つあるが、どっちかに統一したほうが良いかも？
        //      ※「HmAuthenticationSuccessHandler」と「AuthenticationEventListeners」の２クラス
        //        何か理由があって２クラス必要なら、その旨どこかに明記したいですね。。

        // ヘッダに会員SEQを設定する
        if (event.getAuthentication() != null) {
            setHeaderParameter(event.getAuthentication());
        }

        // ゲスト⇒会員カート移行処理
        replaceGuestCartToMemberCart();
    }

    /**
     * ゲスト⇒会員カート移行<br/>
     * <br/>
     * ゲスト会員の注文商品がのこっていれば、会員用の注文商品に移行する
     */
    public void replaceGuestCartToMemberCart() {
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        // SpringSecutiryの会員情報から取得
        Integer memberInfoSeq = commonInfo.getCommonInfoUser().getMemberInfoSeq();

        // ゲスト顧客IDを取得
        HmFrontUserDetailsServiceImpl userDetailsService =
                        ApplicationContextUtility.getBean(HmFrontUserDetailsServiceImpl.class);
        String guestCustomerId = userDetailsService.getCustomerId();
        if (StringUtils.isNotEmpty(guestCustomerId)) {
            // ゲスト⇒会員へカートの移行を行う
            OrderSlipApi orderSlipApi = ApplicationContextUtility.getBean(OrderSlipApi.class);
            OrderSlipMergeRequest mergeRequest = new OrderSlipMergeRequest();
            mergeRequest.setCustomerIdFrom(guestCustomerId);
            mergeRequest.setCustomerIdTo(String.valueOf(memberInfoSeq));

            try {
                orderSlipApi.merge(mergeRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                // マージでエラーが起きた場合に、ここでキャッチしないと、ログイン自体に失敗する。
                // マージに失敗したことをログに出力する
                ApplicationLogUtility applicationLogUtility =
                                ApplicationContextUtility.getBean(ApplicationLogUtility.class);
                applicationLogUtility.writeExceptionLog(new RuntimeException("ゲストから会員へのカートマージに失敗しました。", e));
            }

            // ゲスト顧客IDはコレで不要となったため、クリアする
            userDetailsService.clearCustomerId();
        }
    }

    /**
     * ヘッダに会員SEQを設定する
     *
     * @param authentication Authentication
     */
    public void setHeaderParameter(Authentication authentication) {
        // ログイン済の場合は、ヘッダに会員SEQを設定する
        Object principal = authentication.getPrincipal();
        if (principal instanceof HmFrontUserDetails) {
            // SpringSecurityの管理する領域から、ユーザ情報が取れればそのインスタンスを返却
            HmFrontUserDetails userDetails = (HmFrontUserDetails) authentication.getPrincipal();
            HeaderParamsHelper headerParamsHelper = ApplicationContextUtility.getBean(HeaderParamsHelper.class);
            headerParamsHelper.setMemberSeq(String.valueOf(userDetails.getMemberInfoEntity().getMemberInfoSeq()));
        }
    }

}
