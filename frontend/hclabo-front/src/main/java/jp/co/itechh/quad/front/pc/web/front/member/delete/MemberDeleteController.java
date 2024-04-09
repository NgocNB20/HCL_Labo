/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.delete;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerDeleteRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.HeaderParamsHelper;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipMergeRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.wishlist.presentation.api.WishlistApi;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 会員登録解除 controller
 *
 * @author Pham Quang Dieu
 */
@RequestMapping("/member/delete")
@Controller
@SessionAttributes(value = "memberDeleteModel")
public class MemberDeleteController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberDeleteController.class);

    /** Persistent Token方式を利用する場合のトークンリポジトリ */
    private final PersistentTokenBasedRememberMeServices rememberMeTokenService;

    /** ユーザー詳細サービス */
    private final HmFrontUserDetailsServiceImpl userDetailsService;

    /** ユーザーAPI */
    private final CustomerApi customerApi;

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** お気に入りAPI */
    private final WishlistApi wishlistApi;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** メンバー削除 helper */
    private final MemberDeleteHelper memberDeleteHelper;

    /** 住所録Api */
    private final AddressBookApi addressBookApi;

    private static final String MSGCD_ADDRESS_CLOSE = "LOGISTIC-ADDRESS-CLOSE-001";

    /** コンストラクタ */
    @Autowired
    public MemberDeleteController(PersistentTokenBasedRememberMeServices rememberMeTokenService,
                                  HmFrontUserDetailsServiceImpl userDetailsService,
                                  CustomerApi customerApi,
                                  OrderSlipApi orderSlipApi,
                                  WishlistApi wishlistApi,
                                  HeaderParamsHelper headerParamsHelper,
                                  MemberDeleteHelper memberDeleteHelper,
                                  AddressBookApi addressBookApi) {
        this.rememberMeTokenService = rememberMeTokenService;
        this.userDetailsService = userDetailsService;
        this.customerApi = customerApi;
        this.orderSlipApi = orderSlipApi;
        this.wishlistApi = wishlistApi;
        this.headerParamsHelper = headerParamsHelper;
        this.memberDeleteHelper = memberDeleteHelper;
        this.addressBookApi = addressBookApi;
    }

    /**
     * 入力画面：初期処理
     *
     * @param memberDeleteModel
     * @param model
     * @return 入力画面
     */
    @GetMapping(value = "/")
    protected String doLoadIndex(MemberDeleteModel memberDeleteModel, Model model) {

        // モデル初期化
        clearModel(MemberDeleteModel.class, memberDeleteModel, model);

        return "member/delete/index";
    }

    /**
     * 会員登録解除処理<br/>
     *
     * @param memberDeleteModel
     * @param error
     * @param sessionStatus
     * @param model
     * @return 完了画面
     */
    @PostMapping(value = "/", params = "doOnceMemberInfoDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/delete/index")
    public String doOnceMemberInfoDelete(@Validated MemberDeleteModel memberDeleteModel,
                                         BindingResult error,
                                         SessionStatus sessionStatus,
                                         Model model,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {

        if (error.hasErrors()) {
            return "member/delete/index";
        }

        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        String accessUid = commonInfo.getCommonInfoBase().getAccessUid();

        // 会員SEQ取得
        Integer memberInfoSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();
        CustomerDeleteRequest customerDeleteRequest =
                        memberDeleteHelper.toCustomerDeleteRequest(memberDeleteModel, accessUid);

        try {
            this.headerParamsHelper.setMemberSeq(String.valueOf(memberInfoSeq));
            customerApi.delete(memberInfoSeq, customerDeleteRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "member/delete/index";
            }
        }

        // カートマージ
        this.replaceMemberCartToGuestCart(memberInfoSeq);

        try {
            this.headerParamsHelper.setMemberSeq(String.valueOf(memberInfoSeq));
            this.addressBookApi.allClose();
        } catch (Exception e) {
            throwMessage(MSGCD_ADDRESS_CLOSE);
        }

        // お気に入り商品の削除
        this.wishlistApi.deleteByMemberInfoSeq(memberInfoSeq);

        // ログアウト通知
        try {
            rememberMeTokenService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            LOGGER.error("例外発生のため、退会処理は成立したが、リメンバーミートークンの削除またはログアウトに失敗", e);
        }

        // TODO エラー対応 このタイミングで例外制御はNG。管理者に別途メール送信であったり、非同期処理に切り替えるなどが必要では？ ※ここまで

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // 完了画面へ遷移
        return "redirect:/delete/complete";
    }

    /**
     * 会員⇒ゲストカート移行<br/>
     * <br/>
     * 退会会員の注文商品がのこっていれば、ゲスト用の注文商品に移行する
     */
    private void replaceMemberCartToGuestCart(Integer memberInfoSeq) {
        // まずは下書き注文票に注文商品が残っているかどうかを確認
        if (hasOrderItems()) {
            // 残っている場合は、ゲスト用顧客IDを新規に払い出し、APIヘッダーにセット
            String guestCustomerId = this.userDetailsService.getOrCreateCustomerId();
            this.headerParamsHelper.setMemberSeq(guestCustomerId);

            // カートマージ
            // カートの移行を行う
            OrderSlipMergeRequest mergeRequest = new OrderSlipMergeRequest();
            mergeRequest.setCustomerIdFrom(String.valueOf(memberInfoSeq));
            mergeRequest.setCustomerIdTo(guestCustomerId);
            this.orderSlipApi.merge(mergeRequest);

        } else {
            // APIヘッダーから退会会員の顧客IDをクリアしておく
            this.headerParamsHelper.setMemberSeq(null);
        }
    }

    /**
     * 注文商品が残っているか判定
     *
     * @return true...注文商品が残っている
     */
    private boolean hasOrderItems() {
        // 下書き注文票取得
        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(null);
        if (!ObjectUtils.isEmpty(orderSlipResponse)) {
            // 注文商品数量を取得
            Integer cartGoodsSumCount = orderSlipResponse.getTotalItemCount();
            if (!ObjectUtils.isEmpty(cartGoodsSumCount)) {
                if (cartGoodsSumCount > 0) {
                    // １件以上件数があればtrue（保持している）
                    return true;
                }
            }
        }
        // 上記以外はfalse（保持していない）
        return false;
    }
}