/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.inquiry;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.front.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.front.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.front.pc.web.front.common.inquiry.AbstractInquiryController;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.PageInfoResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * お問い合わせ一覧画面 Controller
 *
 * @author Pham Quang Dieu
 */

@RequestMapping("/member/inquiry")
@Controller
@SessionAttributes(value = "memberInquiryModel")
public class MemberInquiryController extends AbstractInquiryController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInquiryController.class);

    /** 問い合わせ一覧：デフォルトページ番号 */
    private static final String DEFAULT_INQUIRY_PNUM = "1";

    /** 問い合わせ一覧：１ページ当たりのデフォルト最大表示件数 */
    private static final int DEFAULT_INQUIRY_LIMIT = 10;

    /** モデルクリア時のクリア対象外フィールド */
    private static final String[] CLEAR_EXCLUDED_FIELDS = {"pnum", "limit"};

    /** デフォルト戻り先ページ*/
    private static final String BACKPAGE_VIEW = "redirect:/member/inquiry/";

    /** 不正遷移 */
    private static final String MSGCD_REFERER_FAIL = "AMH000201";

    /** メッセージコード：会員の問い合わせでない場合 */
    private static final String MSGCD_NOT_MEMBER_INQUIRY = "PKG-3720-027-A-";

    /** 問い合わせ一覧画面Helper */
    private final MemberInquiryHelper memberInquiryHelper;

    /** お問い合わせAPI */
    private final InquiryApi inquiryApi;

    /**
     * コンストラクタ
     *
     * @param inquiryApi
     * @param memberInquiryHelper
     */
    @Autowired
    public MemberInquiryController(InquiryApi inquiryApi, MemberInquiryHelper memberInquiryHelper) {
        super(inquiryApi, memberInquiryHelper);
        this.inquiryApi = inquiryApi;
        this.memberInquiryHelper = memberInquiryHelper;
    }

    /**
     * 一覧画面：初期処理
     *
     * @param memberInquiryModel 会員問い合わせModel
     * @param model Model
     * @return 一覧画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/inquiry/index")
    protected String doLoadIndex(MemberInquiryModel memberInquiryModel, BindingResult error, Model model) {

        // ページング情報初期化
        if (memberInquiryModel.getPnum() == null) {
            memberInquiryModel.setPnum(DEFAULT_INQUIRY_PNUM);
        }
        if (memberInquiryModel.getLimit() == 0) {
            memberInquiryModel.setLimit(DEFAULT_INQUIRY_LIMIT);
        }

        // モデル初期化
        clearModel(MemberInquiryModel.class, memberInquiryModel, CLEAR_EXCLUDED_FIELDS, model);

        // 問い合わせ一覧の検索
        searchInquiryList(memberInquiryModel, error, model);

        if (error.hasErrors()) {
            return "member/inquiry/index";
        }

        return "member/inquiry/index";
    }

    /**
     * 詳細画面：初期処理
     *
     * @param icd 一般問い合わせURLパラメータ
     * @param memberInquiryModel 会員問い合わせModel
     * @param redirectAttributes
     * @param model Model
     * @return 詳細画面
     */
    @GetMapping(value = "/detail")
    @HEHandler(exception = AppLevelListException.class, returnView = BACKPAGE_VIEW)
    protected String doLoadDetail(@RequestParam(required = false) String icd,
                                  MemberInquiryModel memberInquiryModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        if (StringUtils.isEmpty(icd)) {
            // URLパラメータが不足した遷移の場合（session破棄ができないため、こちらで制御）
            addMessage(getMsgcdRefererFail(), redirectAttributes, model);
            return getBackpage();
        }

        String viewName = super.doLoad(memberInquiryModel, error, redirectAttributes, model);

        if (viewName == null) {
            viewName = "member/inquiry/detail";
        }

        return viewName;
    }

    /**
     * 詳細画面：問い合わせ更新処理<br/>
     *
     * @param memberInquiryModel 会員問い合わせModel
     * @param redirectAttributes
     * @param error BindingResult
     * @param model Model
     * @return 詳細画面
     */
    @PostMapping(value = "/detail", params = "doOnceInquiryUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/inquiry/detail")
    public String doOnceInquiryUpdate(@Validated MemberInquiryModel memberInquiryModel,
                                      BindingResult error,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (error.hasErrors()) {
            return "member/inquiry/detail";
        }

        String viewName = super.doOnceInquiryUpdate(memberInquiryModel, error, redirectAttributes, model);

        if (error.hasErrors()) {
            return "member/inquiry/detail";
        }

        if (viewName == null) {
            viewName = "redirect:/member/inquiry/detail?icd=" + memberInquiryModel.getIcd();
        }

        return viewName;
    }

    /**
     * 問い合わせ一覧の検索<br/>
     *
     * @param memberInquiryModel 会員問い合わせModel
     * @param model Model
     */
    protected void searchInquiryList(MemberInquiryModel memberInquiryModel, BindingResult error, Model model) {

        // PageInfoモジュール取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);

        // 問合せ情報を取得し画面に設定
        InquirySearchDaoConditionDto conditionDto =
                        memberInquiryHelper.toInquirySearchDaoConditionDtoForLoad(memberInquiryModel);
        InquiryListGetRequest inquiryListGetRequest = memberInquiryHelper.toInquiryListGetRequest(conditionDto);

        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        pageInfoRequest.setLimit(memberInquiryModel.getLimit());
        pageInfoRequest.setPage(Integer.valueOf(memberInquiryModel.getPnum()));

        InquiryListResponse inquiryListResponse = null;
        try {
            inquiryListResponse = inquiryApi.get(inquiryListGetRequest, pageInfoRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        if (!ObjectUtils.isEmpty(inquiryListResponse)) {
            // ページングセットアップ
            PageInfoResponse pageInfoResponse = inquiryListResponse.getPageInfo();
            pageInfoModule.setupPageInfo(memberInquiryModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages(), null, null,
                                         null, false, null
                                        );

            List<InquirySearchResultDto> resultList =
                            memberInquiryHelper.toInquirySearchResultDtos(inquiryListResponse.getInquiryList());
            memberInquiryHelper.toPageForLoad(resultList, conditionDto, memberInquiryModel);
        }

        // ぺージャーセットアップ
        pageInfoModule.setupViewPager(memberInquiryModel.getPageInfo(), model);
    }

    /**
     * 問い合せ情報の会員情報についてチェックを行う
     * <pre>
     * アクセス中の会員の問い合せかチェック
     * </pre>
     *
     * @param dto 問い合わせ詳細Dto
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @return true：上記チェックでエラーがある場合
     */
    @Override
    protected boolean checkInquiryMember(InquiryDetailsDto dto, RedirectAttributes redirectAttributes, Model model) {

        Integer memberInfoSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();

        if (!dto.getInquiryEntity().getMemberInfoSeq().equals(memberInfoSeq)) {
            addErrorMessage(MSGCD_NOT_MEMBER_INQUIRY);
            return true;
        }
        return false;
    }

    /**
     * @return 戻り先ページ取得
     */
    @Override
    public String getBackpage() {
        return BACKPAGE_VIEW;
    }

    /**
     * エラーメッセージ取得
     *
     * @return MSGCD_REFERER_FAIL
     */
    @Override
    public String getMsgcdRefererFail() {
        return MSGCD_REFERER_FAIL;
    }
}
