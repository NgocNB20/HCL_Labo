/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.regist;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookDeleteRequest;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerForBackResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.customer.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.application.listener.AuthenticationEventListeners;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.front.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.HeaderParamsHelper;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import jp.co.itechh.quad.temp.presentation.api.TempApi;
import jp.co.itechh.quad.temp.presentation.api.param.TempCustomerResponse;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeCheckRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 本会員登録 Controller
 *
 * @author PHAM QUANG DIEU
 */
@RequestMapping("/regist")
@Controller
@SessionAttributes(value = "registModel")
public class RegistController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistController.class);

    /** 不正遷移:AMR000401 */
    protected static final String MSGCD_REFERER_FAIL = "AMR000401";

    /** 有効な確認メール取得エラー */
    protected static final String MSGCD_CONFIRMMAILENTITYDTO_NULL = "AMR000503";

    /** 都道府県のプルダウンいじられた:AMR000403 */
    protected static final String MSGCD_ILLEGAL_PREFECTURE = "AMR000403";

    /** 性別のラジオボタンいじられた:AMR000405 */
    protected static final String MSGCD_ILLEGAL_SEX = "AMR000405";

    /** 住所登録に失敗した場合:AMR000405 */
    protected static final String MSGCD_ADDRESS_REGIST_FAIL = "AMR000504";

    /** 前画面が確認画面の「修正する」からであるかを判断するフラグ */
    protected static final String FLAG_FROMCONFIRM = "fromConfirm";

    /** 本会員登録: １ページ当たりのデフォルト最大表示件数 */
    private static final Integer DEFAULT_REGISTSEARCH_LIMIT = 1;

    /** 本会員登録 Helperクラス */
    private final RegistHelper registHelper;

    /** 仮会員登録サービス */
    private final TempApi tempApi;

    /** 郵便番号Api */
    private final ZipcodeApi zipcodeApi;

    /** メールAPI */
    private final MailMagazineApi mailMagazineApi;

    /** 顧客Api */
    private final CustomerApi customerApi;

    /** 住所録Api */
    private final AddressBookApi addressBookApi;

    /** ユーザー詳細サービス */
    private final HmFrontUserDetailsServiceImpl hmFrontUserDetailsService;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** コンストラクタ */
    @Autowired
    public RegistController(RegistHelper registHelper,
                            TempApi tempApi,
                            ZipcodeApi zipcodeApi,
                            MailMagazineApi mailMagazineApi,
                            CustomerApi customerApi,
                            AddressBookApi addressBookApi,
                            HmFrontUserDetailsServiceImpl hmFrontUserDetailsService,
                            HeaderParamsHelper headerParamsHelper) {
        this.registHelper = registHelper;
        this.tempApi = tempApi;
        this.zipcodeApi = zipcodeApi;
        this.mailMagazineApi = mailMagazineApi;
        this.customerApi = customerApi;
        this.addressBookApi = addressBookApi;
        this.hmFrontUserDetailsService = hmFrontUserDetailsService;
        this.headerParamsHelper = headerParamsHelper;
    }

    /**
     * 入力画面：初期処理
     *
     * @param mid         会員登録案内メールパラメータ
     * @param registModel 本会員登録
     * @param model       model
     * @return 入力画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "regist/index")
    protected String doLoadIndex(@RequestParam(required = false) String mid,
                                 RegistModel registModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // 本登録確認画面からの遷移の場合 セッション情報を表示
        if (model.containsAttribute(FLAG_FROMCONFIRM)) {
            // パラメータチェック
            if (checkInput(registModel)) {
                registModel.setErrorUrl(true);
                addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
                return "redirect:/error";
            }
            return "regist/index";
        }

        // モデル初期化
        clearModel(RegistModel.class, registModel, model);

        // パラメータセット
        registModel.setMid(mid);

        // パラメータチェック
        if (StringUtils.isEmpty(registModel.getMid())) {
            // 何らかの不正操作の場合
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        // 動的コンポーネント作成
        initComponents(registModel);

        TempCustomerResponse tempCustomerResponse = null;
        try {
            // 仮会員情報取得
            tempCustomerResponse = tempApi.getMemberTemp(registModel.getMid());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "regist/index";
        }

        if (tempCustomerResponse == null || tempCustomerResponse.getMemberInfo() == null) {
            addMessage(MSGCD_CONFIRMMAILENTITYDTO_NULL, redirectAttributes, model);
            return "redirect:/error";
        }

        // 画面へセット
        registHelper.toPageForLoad(tempCustomerResponse, registModel);

        if (!ObjectUtils.isEmpty(tempCustomerResponse) && !ObjectUtils.isEmpty(tempCustomerResponse.getMemberInfo())) {
            // メルマガ情報をセット
            setMailMagazineMember(registModel, error, tempCustomerResponse.getMemberInfo().getMemberInfoSeq());
        }

        if (error.hasErrors()) {
            return "regist/index";
        }

        // 有効なURLだった
        registModel.setErrorUrl(false);

        return "regist/index";
    }

    /**
     * 入力画面：確認画面に遷移
     *
     * @param registModel        本会員登録 Model
     * @param error              error
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 確認画面
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "regist/index")
    public String doConfirm(@Validated RegistModel registModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // パラメータチェック
        if (StringUtils.isEmpty(registModel.getMemberInfoMail())) {
            addMessage(RegistModel.MSGCD_SESSION_DESTROY, redirectAttributes, model);
            return "redirect:/error";
        }

        // 都道府県と郵便番号が不一致の場合
        checkPrefectureAndZipCode(registModel, error);

        if (error.hasErrors()) {
            return "regist/index";
        }

        // 入力チェック。POSTの値の書き換えチェック
        checkIllegalParameter(registModel);

        return "redirect:/regist/confirm";
    }

    /**
     * 確認画面：初期処理
     *
     * @param registModel        本会員登録 Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 確認画面
     */
    @GetMapping(value = "/confirm")
    protected String doLoadConfirm(RegistModel registModel, RedirectAttributes redirectAttributes, Model model) {

        // 不正遷移チェック 必須項目の有無で入力画面へ遷移
        if (checkInput(registModel)) {
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }
        return "regist/confirm";
    }

    /**
     * 確認画面：入力画面に遷移
     *
     * @param registModel        本会員登録 Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 入力画面
     */
    @PostMapping(value = "/confirm", params = "doIndex")
    public String doIndexConfirm(RegistModel registModel, RedirectAttributes redirectAttributes, Model model) {

        // 遷移元フラグ設定
        redirectAttributes.addFlashAttribute(FLAG_FROMCONFIRM, null);

        return "redirect:/regist/";
    }

    /**
     * 本会員登録処理
     *
     * @param registModel        本会員登録 Model
     * @param sessionStatus      sessionStatus
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 完了画面
     */
    @PostMapping(value = "/confirm", params = "doOnceMemberInfoRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "regist/confirm")
    public String doOnceMemberInfoRegist(HttpServletRequest request,
                                         RegistModel registModel,
                                         BindingResult error,
                                         SessionStatus sessionStatus,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {

        CustomerResponse customerResponse = null;
        // 会員エンティティ情報の作成
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        String accessUid = commonInfo.getCommonInfoBase().getAccessUid();

        AddressBookAddressRegistResponse addressResponse = null;
        try {
            // 顧客IDを取得し、APIヘッダーにセット
            String customerId = getCustomerIdForRegistApiHeader(registModel);
            this.headerParamsHelper.setMemberSeq(customerId);

            // 住所の登録
            addressResponse = this.addressBookApi.regist(
                            this.registHelper.toRegistAddressRequestForMemberInfoRegist(registModel));

            if (addressResponse == null || StringUtils.isBlank(addressResponse.getAddressId())) {
                // 住所未登録の場合は入力画面に戻す
                addMessage(MSGCD_ADDRESS_REGIST_FAIL, redirectAttributes, model);
                return "regist/index";
            }

            // 本会員登録
            customerResponse = customerApi.regist(
                            registHelper.toMemberInfoEntityForMemberInfoRegist(accessUid, registModel,
                                                                               addressResponse
                                                                              ));

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);

            // 【暫定対応】住所録APIからのレスポンスモデルが存在しつつ、ユーザーサービスでエラーが起きた場合、該当住所を削除する
            if (addressResponse != null && StringUtils.isNotBlank(addressResponse.getAddressId())) {

                AddressBookDeleteRequest addressBookDeleteRequest = new AddressBookDeleteRequest();
                addressBookDeleteRequest.setAddressId(addressResponse.getAddressId());
                this.addressBookApi.delete(addressBookDeleteRequest);
            }

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            // handleErrorが何らかの理由で捌ききれなかった場合
            throwMessage(MSGCD_REFERER_FAIL);
        }

        // 会員登録したら自動ログイン処理
        request.setAttribute("isCheckInfo", true);
        if (customerResponse != null) {
            hmFrontUserDetailsService.updateUserInfo(customerResponse.getMemberInfoId());
        }

        // カートマージのため、ログインイベントリスナーを直接呼び出す（ゲスト⇒会員カート移行）
        AuthenticationEventListeners authenticationEventListeners = new AuthenticationEventListeners();
        authenticationEventListeners.replaceGuestCartToMemberCart();

        // Cookie上の顧客IDは不要となるため、クリアする
        hmFrontUserDetailsService.clearCustomerId();

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // 完了画面へ遷移
        return "redirect:/regist/complete";
    }

    /**
     * 完了画面：画面表示処理
     *
     * @return 完了画面
     */
    @GetMapping(value = "/complete")
    public String doLoadComplete() {

        return "regist/complete";
    }

    /**
     * 動的コンポーネント作成
     *
     * @param registModel 本会員登録 Model
     */
    protected void initComponents(RegistModel registModel) {

        // 都道府県区分値を取得（北海道：北海道）
        Map<String, String> prefectureTypeItems = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        registModel.setMemberInfoPrefectureItems(prefectureTypeItems);

        // 性別(未回答)区分値を取得
        Map<String, String> memberInfoSexItems = EnumTypeUtil.getEnumMap(HTypeSexUnnecessaryAnswer.class);
        registModel.setMemberInfoSexItems(memberInfoSexItems);
    }

    /**
     * メルマガ会員情報を設定
     * 新規会員登録のみ画面表示の初期値は常にONのまま
     * 将来的には、会員とメルマガ会員のテーブルは統合されるので、このあたりのメソッドは不要となる
     *
     * @param registModel   本会員登録 Model
     * @param memberInfoSeq 会員SEQ
     */
    protected void setMailMagazineMember(RegistModel registModel, BindingResult error, Integer memberInfoSeq) {

        // 会員SEQがnullでない場合
        if (!Objects.isNull(memberInfoSeq)) {

            if (memberInfoSeq.intValue() == 0) {
                // 会員SEQが未設定
                registModel.setPreMailMagazine(false);
                return;
            }

            MailmagazineMemberResponse mailmagazineMemberResponse = null;
            try {
                // メルマガ会員情報を取得
                mailmagazineMemberResponse = mailMagazineApi.getByMemberinfoSeq(memberInfoSeq);

            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }

            if (mailmagazineMemberResponse != null && StringUtils.equals(
                            mailmagazineMemberResponse.getSendStatus(), HTypeSendStatus.SENDING.getValue())) {
                registModel.setPreMailMagazine(true);
            } else {
                registModel.setPreMailMagazine(false);
            }
        }
    }

    /**
     * 必須項目を全てチェックし、不正遷移かどうかをチェック
     *
     * @param registModel 本会員登録 Model
     * @return true=不正、false=正常
     */
    protected boolean checkInput(RegistModel registModel) {

        // メール
        if (StringUtils.isEmpty(registModel.getMemberInfoMail())) {
            return true;
        }
        // 氏名(姓)
        if (StringUtils.isEmpty(registModel.getMemberInfoLastName())) {
            return true;
        }
        // 氏名(名)
        if (StringUtils.isEmpty(registModel.getMemberInfoFirstName())) {
            return true;
        }
        // フリガナ(セイ)
        if (StringUtils.isEmpty(registModel.getMemberInfoLastKana())) {
            return true;
        }
        // フリガナ(メイ)
        if (StringUtils.isEmpty(registModel.getMemberInfoFirstKana())) {
            return true;
        }
        // 性別
        if (StringUtils.isEmpty(registModel.getMemberInfoSex())) {
            return true;
        }
        // 電話番号
        if (StringUtils.isEmpty(registModel.getMemberInfoTel())) {
            return true;
        }
        // 郵便番号1
        if (StringUtils.isEmpty(registModel.getMemberInfoZipCode1())) {
            return true;
        }
        // 郵便番号2
        if (StringUtils.isEmpty(registModel.getMemberInfoZipCode2())) {
            return true;
        }
        // 都道府県
        if (StringUtils.isEmpty(registModel.getMemberInfoPrefecture())) {
            return true;
        }
        // 住所1
        if (StringUtils.isEmpty(registModel.getMemberInfoAddress1())) {
            return true;
        }
        // 住所2
        if (StringUtils.isEmpty(registModel.getMemberInfoAddress2())) {
            return true;
        }
        // パスワード
        if (StringUtils.isEmpty(registModel.getMemberInfoPassWord())) {
            return true;
        }
        return false;
    }

    /**
     * 都道府県と郵便番号の不整合チェック
     *
     * @param registModel 本会員登録 Model
     */
    protected void checkPrefectureAndZipCode(RegistModel registModel, BindingResult error) {

        // nullの場合service未実行(必須チェックは別タスク)
        if (StringUtils.isEmpty(registModel.getMemberInfoZipCode1()) || StringUtils.isEmpty(
                        registModel.getMemberInfoZipCode2()) || StringUtils.isEmpty(
                        registModel.getMemberInfoPrefecture())) {
            return;
        }

        try {

            ZipCodeCheckRequest zipCodeCheckRequest = new ZipCodeCheckRequest();

            zipCodeCheckRequest.setZipCode(registModel.getMemberInfoZipCode1() + registModel.getMemberInfoZipCode2());
            zipCodeCheckRequest.setPrefecture(registModel.getMemberInfoPrefecture());
            zipcodeApi.check(zipCodeCheckRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, new HashMap<>());
        }
    }

    /**
     * 入力チェック
     *
     * @param registModel 本会員登録 Model
     */
    protected void checkIllegalParameter(RegistModel registModel) {

        // 都道府県入力値チェック
        checkPrefecture(registModel);
        // 性別入力値チェック
        checkSex(registModel);

    }

    /**
     * 都道府県の入力チェック
     * POSTされるプルダウンの値の書き換え対策
     *
     * @param registModel 本会員登録 Model
     */
    protected void checkPrefecture(RegistModel registModel) {

        Map<String, String> prefectureMap = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        boolean existFlag = prefectureMap.containsKey(registModel.getMemberInfoPrefecture());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_PREFECTURE);
        }
    }

    /**
     * 性別(未回答)の入力チェック
     * POSTされる値の書き換え対策
     *
     * @param registModel 本会員登録 Model
     */
    protected void checkSex(RegistModel registModel) {

        Map<String, String> memberInfoSexMap = EnumTypeUtil.getEnumMap(HTypeSexUnnecessaryAnswer.class);
        boolean existFlag = memberInfoSexMap.containsKey(registModel.getMemberInfoSex());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_SEX);
        }
        return;
    }

    /**
     * 会員登録APIヘッダーセット用の、顧客IDを取得
     *
     * @param registModel 本会員登録Model
     * @return 会員登録APIヘッダーセット用の顧客ID
     */
    protected String getCustomerIdForRegistApiHeader(RegistModel registModel) {
        // 暫定会員登録済の場合
        String customerId = getCustomerIdFromZanteiMember(registModel.getMemberInfoMail());
        if (StringUtils.isNotEmpty(customerId)) {
            // 暫定会員の顧客IDを返却
            return customerId;
        }

        // 暫定会員未登録の場合
        // ⇒顧客IDを新規で払い出す
        //   ◆会員登録前にカートINしていた場合は、
        //     カートINした時点で、払い出された顧客IDを返却する（Cookieからゲスト用顧客ID「GCI」を取得）
        //   ◆会員登録前にカートINしていなかった場合は、
        //     このタイミングでユーザーサービスのAPIから顧客IDを新規で払い出して返却する
        //     ★このとき、Cookieにゲスト用顧客ID「GCI」としての保存は行わない（引数false）
        //       ※GCIはゲスト専用のCookieであり、正式会員の顧客IDはその後SpringSecurityの管理化となるから
        return hmFrontUserDetailsService.getOrCreateCustomerId(false);
    }

    /**
     * 暫定会員より顧客IDを取得<br/>
     * <p>
     * 現在会員登録しようとしているメールアドレスについて、
     * すでにメルマガ会員登録済（暫定会員登録済）の可能性がある<br/>
     * 　↓<br/>
     * 本メソッドでは、暫定会員登録済かどうかをチェックし、<br/>
     * 登録済だった場合は、暫定会員の顧客IDを返却する<br/>
     * <br/>
     * 未登録だった場合は、nullを返却する<br/>
     *
     * @param mailAddress メールアドレス
     * @return 顧客ID　※暫定会員登録済の場合は値あり／未登録の場合は値無し（null返却）
     */
    private String getCustomerIdFromZanteiMember(String mailAddress) {
        // 会員検索APIより、同一メールアドレスの暫定会員情報を取得する
        CustomerListGetRequest customerListGetRequest = new CustomerListGetRequest();
        // 検索条件･･･本会員では無い×メールアドレスが画面入力値と一致
        customerListGetRequest.setMainMemberFlag(false);
        customerListGetRequest.setMemberInfoId(mailAddress);
        // ページング設定・・・１件のみ取得
        //（指定無しでも良いが、検索対象想定が１件なので、１件を設定）
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageRequest(pageInfoRequest, null, DEFAULT_REGISTSEARCH_LIMIT, null, true);

        // 【API呼出】
        CustomerListResponse response = customerApi.get(customerListGetRequest, pageInfoRequest);

        if (CollectionUtil.isNotEmpty(response.getCustomerList())) {
            // =========================================================
            // API取得結果が存在する場合は、該当会員情報の顧客IDを返却する
            // =========================================================
            CustomerForBackResponse customer = response.getCustomerList().get(0);
            return String.valueOf(customer.getMemberInfoSeq());
        }
        // 取得結果が存在しない場合は、nullを返却
        return null;
    }
}