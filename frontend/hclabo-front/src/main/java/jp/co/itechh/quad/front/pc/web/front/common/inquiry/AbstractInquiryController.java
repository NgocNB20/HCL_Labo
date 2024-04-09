/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.common.inquiry;

import jp.co.itechh.quad.front.base.constant.ValidatorConstants;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.front.utility.InquiryUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryRegistRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 問合せコントローラ基底クラス<br/>
 *
 * @author Pham Quang Dieu
 *
 */
public abstract class AbstractInquiryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInquiryController.class);

    /** メッセージコード：不正遷移 */
    private static final String MSGCD_REFERER_FAIL = "AIX000201";

    /** メッセージコード：該当の問合わせ情報がない場合 */
    private static final String MSGCD_GET_INQUIRY_FAIL = "PKG-3720-027-A-";

    /** メッセージコード：受付完了 */
    private static final String MSGCD_RECEIVED_INFO = "AIX000202I";

    /** お問合せ詳細画面Helper */
    private final AbstractInquiryHelper abstractInquiryHelper;

    /** お問い合わせAPI */
    private final InquiryApi inquiryApi;

    /**
     * コンストラクタ
     *
     * @param inquiryApi
     * @param abstractInquiryHelper
     */
    public AbstractInquiryController(InquiryApi inquiryApi, AbstractInquiryHelper abstractInquiryHelper) {
        this.inquiryApi = inquiryApi;
        this.abstractInquiryHelper = abstractInquiryHelper;
    }

    /**
     * 初期処理<br/>
     *
     * @param abstractInquiryModel
     * @param redirectAttributes
     * @param model
     * @return 遷移先画面
     */
    protected String doLoad(AbstractInquiryModel abstractInquiryModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 認証画面から遷移した時や、URLにクエリーがついている場合は icd に値が存在するが
        // 当画面でリロードを行うと icd にはないため、保管用の変数から取得する。
        String inquiryCode = abstractInquiryModel.getIcd() == null ?
                        abstractInquiryModel.getSaveIcd() :
                        abstractInquiryModel.getIcd();

        // パラメータ：お問い合わせ番号のチェックを行う
        if (inquiryCode == null || !inquiryCode.matches(ValidatorConstants.REGEX_INQUIRY_CODE)) {
            addMessage(getMsgcdRefererFail(), redirectAttributes, model);
            return getBackpage();
        }

        // 指定した問合せが認証済かをチェック、未認証の場合は認証画面へ遷移
        if (!isAttested(inquiryCode)) {
            return getBackpage();
        }

        try {
            return initInquiry(inquiryCode, abstractInquiryModel, error, redirectAttributes, model);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }
    }

    /**
     * 登録処理
     *
     * @param abstractInquiryModel
     * @param redirectAttributes
     * @param  model
     * @return 遷移先画面
     */
    public String doOnceInquiryUpdate(AbstractInquiryModel abstractInquiryModel,
                                      BindingResult error,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (abstractInquiryModel.getReedOnlyDto() == null) {
            // セッションタイムアウトが発生した場合
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        // 問い合わせ・問い合わせ内容の登録
        InquiryDetailsDto inquiryDetailsDto = abstractInquiryHelper.toInquiryDetailsDtoForPage(abstractInquiryModel);

        try {
            InquiryRegistRequest inquiryRegistRequest = abstractInquiryHelper.toInquiryRegistRequest(inquiryDetailsDto);
            inquiryApi.regist(inquiryRegistRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("inquiryBody", "inputInquiryBody");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        InquiryUtility inquiryUtility = ApplicationContextUtility.getBean(InquiryUtility.class);
        // ログ出力
        inquiryUtility.writeInquiryLog(inquiryDetailsDto.getInquiryEntity().getInquiryCode());

        // 画面を受付完了状態にする
        abstractInquiryHelper.changeAccepted(abstractInquiryModel);
        addInfoMessage(MSGCD_RECEIVED_INFO, null, redirectAttributes, model);

        try {
            return initInquiry(inquiryDetailsDto.getInquiryEntity().getInquiryCode(), abstractInquiryModel, error,
                               redirectAttributes, model
                              );
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }
    }

    /**
     * 問合せ詳細画面の初期処理<br>
     * 問合せ情報を取得しPageに設定
     *
     * @param inquiryCode お問い合わせ番号
     * @param abstractInquiryModel
     * @param redirectAttributes
     * @param model
     * @return 遷移先画面
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected String initInquiry(String inquiryCode,
                                 AbstractInquiryModel abstractInquiryModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) throws IllegalAccessException, InvocationTargetException {
        InquiryGetRequest inquiryGetRequest = new InquiryGetRequest();

        InquiryResponse inquiryResponse = null;
        try {
            inquiryResponse = inquiryApi.getByInquiryCode(inquiryCode, inquiryGetRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // 問い合わせ詳細DTOの取得
        InquiryDetailsDto inquiryDetailsDto = abstractInquiryHelper.toInquiryDetailsDto(inquiryResponse);

        if (inquiryDetailsDto == null) {
            addMessage(MSGCD_GET_INQUIRY_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }
        if (checkInquiryMember(inquiryDetailsDto, redirectAttributes, model)) {
            addMessage(getMsgcdRefererFail(), redirectAttributes, model);
            return "redirect:/error";
        }

        abstractInquiryHelper.toPageForLoad(abstractInquiryModel, inquiryDetailsDto);

        return null;
    }

    /**
     * 問合せ情報の会員情報についてチェックを行う
     * <pre>
     * 会員問合せ詳細の場合、アクセス中の会員の問合せかをチェック
     * ゲスト問合せ詳細の場合、ゲストの問合せかをチェック
     * </pre>
     *
     * @param dto 問い合わせ詳細Dto
     * @param redirectAttributes
     * @param model
     * @return true：上記チェックでエラーがある場合
     */
    protected boolean checkInquiryMember(InquiryDetailsDto dto, RedirectAttributes redirectAttributes, Model model) {
        return false;
    }

    /**
     * 問合せ認証済かをチェック<br>
     * ゲスト問合せ詳細の場合に使用する
     *
     * @param inquiryCode お問い合わせ番号
     * @return true：認証済
     */
    protected boolean isAttested(String inquiryCode) {
        return true;
    }

    /**
     * 戻り先ページ取得
     *
     * @return url
     */
    public abstract String getBackpage();

    /**
     * エラーメッセージ取得
     * @return String
     */
    public abstract String getMsgcdRefererFail();

}
