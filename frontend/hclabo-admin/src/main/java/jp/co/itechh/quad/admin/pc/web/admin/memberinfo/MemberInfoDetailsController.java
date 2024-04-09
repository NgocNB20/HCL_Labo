/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.admin.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryCheckResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistoryListResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 会員詳細情報取得 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RequestMapping("/memberinfo")
@Controller
@SessionAttributes(value = "memberInfoDetailsModel")
@PreAuthorize("hasAnyAuthority('MEMBER:4')")
public class MemberInfoDetailsController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInfoDetailsController.class);

    /** 会員詳細：受注履歴：デフォルトページ番号 */
    private static final String DEFAULT_ORDERHISTORY_PNUM = "1";

    /** 会員詳細：受注履歴：デフォルト：最大表示件数 */
    private static final int DEFAULT_ORDERHISTORY_LIMIT = 10;

    /** 会員詳細：受注履歴：デフォルト：ソート項目 */
    private static final String DEFAULT_ORDERHISTORY_ORDER_FIELD = "orderTime";

    /** 会員詳細：受注履歴：デフォルト：ソート条件(昇順/降順) */
    private static final boolean DEFAULT_ORDERHISTORY_ORDER_ASC = false;

    /** 会員検索Helper<br/> */
    private final MemberInfoDetailsHelper memberInfoDetailsHelper;

    /** 会員API */
    private final CustomerApi customerApi;

    /** 住所録Api */
    private final AddressBookApi addressBookApi;

    /** 問合せAPI */
    private final InquiryApi inquiryApi;

    /** メルマガAPI */
    private final MailMagazineApi mailmagazineApi;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** コンストラクタ */
    @Autowired
    public MemberInfoDetailsController(MemberInfoDetailsHelper memberInfoDetailsHelper,
                                       CustomerApi customerApi,
                                       AddressBookApi addressBookApi,
                                       InquiryApi inquiryApi,
                                       MailMagazineApi mailmagazineApi,
                                       OrderReceivedApi orderReceivedApi,
                                       ConversionUtility conversionUtility,
                                       HeaderParamsHelper headerParamsHelper) {
        this.memberInfoDetailsHelper = memberInfoDetailsHelper;
        this.customerApi = customerApi;
        this.addressBookApi = addressBookApi;
        this.inquiryApi = inquiryApi;
        this.mailmagazineApi = mailmagazineApi;
        this.orderReceivedApi = orderReceivedApi;
        this.conversionUtility = conversionUtility;
        this.headerParamsHelper = headerParamsHelper;
    }

    /**
     * 詳細画面：初期処理
     *
     * @param memberInfoDetailsModel 会員詳細モデル
     * @param model                  Model
     * @return 自画面
     */
    @GetMapping(value = "/details")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/memberinfo/")
    public String doLoadDetails(@RequestParam(required = false) Optional<Integer> memberInfoSeq,
                                @RequestParam(required = false) Optional<String> from,
                                @RequestParam(required = false) Optional<String> orderCode,
                                MemberInfoDetailsModel memberInfoDetailsModel,
                                BindingResult error,
                                Model model) {

        // モデル初期化
        clearModel(MemberInfoDetailsModel.class, memberInfoDetailsModel, model);
        memberInfoSeq.ifPresent(memberInfoDetailsModel::setMemberInfoSeq);
        from.ifPresent(memberInfoDetailsModel::setFrom);
        // 受注詳細から遷移した際に「戻る」ボタンで元の画面に戻るため受注コードを格納
        orderCode.ifPresent(memberInfoDetailsModel::setOrderCode);

        // 会員SEQ必須の画面です。
        if (memberInfoDetailsModel.getMemberInfoSeq() == null) {
            // アカウントロックを解除したときは、getパラメータではなくunlockTargetSeqを利用して会員SEQを本画面に渡す
            if (memberInfoDetailsModel.getUnlockTargetSeq() != null) {
                memberInfoDetailsModel.setMemberInfoSeq(memberInfoDetailsModel.getUnlockTargetSeq());
            } else {
                return "redirect:/error";
            }
        }

        MemberInfoDetailsDto memberInfoDetailsDto = null;
        AddressBookAddressResponse addressResponse = null;
        try {
            // 会員詳細取得サービス実行
            CustomerResponse customerResponse =
                            customerApi.getByMemberinfoSeq(memberInfoDetailsModel.getMemberInfoSeq());

            // 会員住所取得サービス実行
            if (customerResponse.getMemberInfoAddressId() != null) {
                addressResponse = addressBookApi.getAddressById(customerResponse.getMemberInfoAddressId());
            }
            memberInfoDetailsDto = memberInfoDetailsHelper.toMemberInfoDetailsDto(customerResponse);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/memberinfo/";
        }

        // ページに反映
        memberInfoDetailsHelper.toModelDetailsForLoad(memberInfoDetailsDto, memberInfoDetailsModel, addressResponse);

        // ページング情報初期化（DetailsModel）
        memberInfoDetailsModel.setPageNumber(DEFAULT_ORDERHISTORY_PNUM);
        memberInfoDetailsModel.setLimit(DEFAULT_ORDERHISTORY_LIMIT);
        memberInfoDetailsModel.setOrderField(DEFAULT_ORDERHISTORY_ORDER_FIELD);
        memberInfoDetailsModel.setOrderAsc(DEFAULT_ORDERHISTORY_ORDER_ASC);

        // 注文履歴検索
        searchOrderHistory(memberInfoDetailsModel);

        // 遷移元チェック
        // fromが無い場合は、判定不能な為、「member」を設定
        if (memberInfoDetailsModel.getFrom() == null) {
            memberInfoDetailsModel.setFrom("member");
            // fromが「member」でない場合、検索条件を破棄する為、mdに空文字をセット
        } else if (!memberInfoDetailsModel.getFrom().equals("member")) {
            memberInfoDetailsModel.setMd("");
        }

        try {
            // 会員に紐付く問い合わせがあるかチェックし、結果を取得
            InquiryCheckResponse inquiryCheckResponse = inquiryApi.check(memberInfoDetailsModel.getMemberInfoSeq());
            if (inquiryCheckResponse.getInquiryFlag() != null) {
                memberInfoDetailsModel.setInquiryFlag(inquiryCheckResponse.getInquiryFlag());
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/memberinfo/";
        }
        // メルマガ会員情報を設定
        setMailMagazineMemberForDetail(memberInfoDetailsModel, error, memberInfoDetailsModel.getMemberInfoEntity());
        if (error.hasErrors()) {
            return "redirect:/memberinfo/";
        }

        return "memberinfo/details";
    }

    /**
     * 受注履歴の表示切替
     *
     * @param memberInfoDetailsModel 会員検索モデル
     * @param model                  Model
     * @return 自画面
     */
    @PostMapping(value = "/details", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    public String doDisplayChangeForOrderHistory(MemberInfoDetailsModel memberInfoDetailsModel, Model model) {
        searchOrderHistory(memberInfoDetailsModel);
        return "memberinfo/details";
    }

    /**
     * 受注履歴　検索処理
     *
     * @param memberInfoDetailsModel 会員詳細モデル
     */
    protected void searchOrderHistory(MemberInfoDetailsModel memberInfoDetailsModel) {

        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest,
                                        this.conversionUtility.toInteger(memberInfoDetailsModel.getPageNumber()),
                                        memberInfoDetailsModel.getLimit(), memberInfoDetailsModel.getOrderField(),
                                        memberInfoDetailsModel.isOrderAsc()
                                       );
        // 会員SEQをヘッダーに設定
        this.headerParamsHelper.setHeader(
                        this.orderReceivedApi.getApiClient(), memberInfoDetailsModel.getMemberInfoSeq().toString());

        // 注文履歴リストを取得
        CustomerHistoryListResponse customerHistoryListResponse =
                        this.orderReceivedApi.getCustomerHistoryList(pageInfoRequest);

        if (customerHistoryListResponse == null) {
            return;
        }

        // ページに反映
        this.memberInfoDetailsHelper.toModelForOrderHistory(customerHistoryListResponse, memberInfoDetailsModel);

        // ページャーにレスポンス情報をセット
        PageInfoResponse pageInfoResponse = customerHistoryListResponse.getPageInfo();
        if (pageInfoResponse != null) {
            pageInfoHelper.setupPageInfo(memberInfoDetailsModel, pageInfoResponse.getPage(),
                                         pageInfoResponse.getLimit(), pageInfoResponse.getNextPage(),
                                         pageInfoResponse.getPrevPage(), pageInfoResponse.getTotal(),
                                         pageInfoResponse.getTotalPages()
                                        );

            // ページャーをセット
            pageInfoHelper.setupViewPager(memberInfoDetailsModel.getPageInfo(), memberInfoDetailsModel);

            // 件数をセット
            memberInfoDetailsModel.setTotalCount(memberInfoDetailsModel.getPageInfo().getTotal());
        }
    }

    /**
     * メルマガ会員情報を設定<br/>
     * 将来的には、会員とメルマガ会員のテーブルは統合されるので、このあたりのメソッドは不要となる
     *
     * @param memberInfoDetailsModel 会員詳細モデル
     * @param memberInfoEntity       会員クラス
     */
    protected void setMailMagazineMemberForDetail(MemberInfoDetailsModel memberInfoDetailsModel,
                                                  BindingResult error,
                                                  MemberInfoEntity memberInfoEntity) {
        try {
            // メルマガ会員情報を取得
            MailmagazineMemberResponse mailmagazineMemberResponse =
                            mailmagazineApi.getByMemberinfoSeq(memberInfoEntity.getMemberInfoSeq());

            memberInfoDetailsModel.setMailMagazine(
                            mailmagazineMemberResponse != null && mailmagazineMemberResponse.getSendStatus() != null &&
                            EnumTypeUtil.getEnumFromValue(HTypeSendStatus.class,
                                                          mailmagazineMemberResponse.getSendStatus()
                                                         ) == HTypeSendStatus.SENDING);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }
}