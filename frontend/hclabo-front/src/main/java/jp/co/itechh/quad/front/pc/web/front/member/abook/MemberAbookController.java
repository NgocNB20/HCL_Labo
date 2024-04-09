/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.abook;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCloseRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCustomerExclusionRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeCheckRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * アドレス帳画面 Controller<br/>
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@RequestMapping("/member/abook")
@Controller
@SessionAttributes(value = "memberAbookModel")
public class MemberAbookController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberAbookController.class);

    /** アドレス帳一覧：デフォルトページ番号 */
    public static final String DEFAULT_ABOOK_PNUM = "1";

    /** お気に入り一覧：１ページ当たりのデフォルト最大表示件数 */

    public static final int DEFAULT_ABOOK_LIMIT = 10;

    /** モデルクリア時のクリア対象外フィールド */
    public static final String[] CLEAR_EXCLUDED_FIELDS = {"pnum", "limit"};

    /** 不正遷移<br/> */
    protected static final String MSGCD_REFERER_FAIL = "AMA000201";

    /** 都道府県のプルダウンいじられた<br/> */
    protected static final String MSGCD_ILLEGAL_PREFECTURE = "AMA000202";

    /** 都道府県と郵便番号の整合性エラー ※W付与 <br/> */
    protected static final String MSGCD_PREFECTURE_CONSISTENCY = "AMA000203W";

    /** 都道府県フィールド名 **/
    protected static final String FILED_NAME_PREFECTURE = "prefecture";

    /** 住所録Api */
    private final AddressBookApi addressBookApi;

    /** 郵便番号API */
    private final ZipcodeApi zipcodeApi;

    /** helper */
    private MemberAbookHelper memberAbookHelper;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 住所録取得チェック */
    private boolean isNullAddressSearchResponseFlag = false;

    /** コンストラクタ */
    @Autowired
    public MemberAbookController(AddressBookApi addressBookApi,
                                 ZipcodeApi zipcodeApi,
                                 MemberAbookHelper memberAbookHelper,
                                 ConversionUtility conversionUtility) {
        this.addressBookApi = addressBookApi;
        this.zipcodeApi = zipcodeApi;
        this.memberAbookHelper = memberAbookHelper;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 一覧画面：初期処理
     *
     * @param memberAbookModel アドレス帳Model
     * @param model
     * @return 一覧画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/abook/index")
    protected String doLoadIndex(MemberAbookModel memberAbookModel, Model model) {

        // ページング情報初期化
        if (memberAbookModel.getPnum() == null) {
            memberAbookModel.setPnum(DEFAULT_ABOOK_PNUM);
        }
        if (memberAbookModel.getLimit() == 0) {
            memberAbookModel.setLimit(DEFAULT_ABOOK_LIMIT);
        }

        // アドレス帳Modelをクリア
        clearModel(MemberAbookModel.class, memberAbookModel, CLEAR_EXCLUDED_FIELDS, model);

        // 住所録一覧の検索
        searchAddressBookList(memberAbookModel, model);

        return "member/abook/index";
    }

    /**
     * 一覧画面：削除（非公開）処理<br/>
     *
     * @param memberAbookModel   アドレス帳Model
     * @param error
     * @param model
     * @return 一覧画面
     */
    @PostMapping(value = "/", params = "doOnceDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/abook/index")
    public String doOnceDelete(MemberAbookModel memberAbookModel, BindingResult error, Model model) {

        try {
            int index = memberAbookModel.getAddressBookIndex();

            if (memberAbookModel.getAddressBookItems().size() > index) {

                String addressBookSeq = memberAbookModel.getAddressBookItems().get(index).getAbid();

                // 削除（非公開）モード
                if (StringUtils.isNotBlank(addressBookSeq)) {

                    // 住所録を非公開にする　※顧客情報と紐づく住所IDの場合は非公開不可
                    AddressBookCloseRequest request = new AddressBookCloseRequest();
                    request.setAddressId(addressBookSeq);

                    this.addressBookApi.close(
                                    request, memberAbookModel.getCommonInfo()
                                                             .getCommonInfoUser()
                                                             .getMemberInfoAddressId());
                }
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            // handleErrorが何らかの理由で捌ききれなかった場合
            throwMessage(MSGCD_REFERER_FAIL);

        } finally {
            // 住所録一覧の検索
            searchAddressBookList(memberAbookModel, model);
        }

        if (DEFAULT_ABOOK_PNUM.equals(memberAbookModel.getPnum())) {

            return "redirect:/member/abook/";
        } else if (isNullAddressSearchResponseFlag) {
            Integer pNumNew = Integer.parseInt(memberAbookModel.getPnum()) - 1;

            memberAbookModel.setPnum(String.valueOf(pNumNew));

            searchAddressBookList(memberAbookModel, model);

            return "member/abook/index";
        } else {

            return "member/abook/index";
        }
    }

    /**
     * 詳細画面：初期表示<br/>
     *
     * @param memberAbookModel アドレス帳Model
     * @param model
     * @return 詳細画面
     */
    @GetMapping(value = "/detail")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/abook/index")
    protected String doLoadDetail(MemberAbookModel memberAbookModel, Model model) {

        // チェックエラー発生後のリダイレクト時→セッションに保持したModelをそのまま使いたいので、何もせずreturn
        if (model.containsAttribute(FLASH_MESSAGES)) {
            return "member/abook/detail";
        }

        // アドレス帳Modelをクリア
        clearModel(MemberAbookModel.class, memberAbookModel, CLEAR_EXCLUDED_FIELDS, model);

        Map<String, String> prefectureTypeItems = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        memberAbookModel.setPrefectureItems(prefectureTypeItems);

        return "member/abook/detail";
    }

    /**
     * 詳細画面：アドレス帳登録処理<br/>
     *
     * @param memberAbookModel アドレス帳Model
     * @param error
     * @param model
     * @return 一覧画面
     */
    @PostMapping(value = "/detail", params = "doOnceAddressBookRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/abook/detail")
    public String doOnceAddressBookRegist(@Validated MemberAbookModel memberAbookModel,
                                          BindingResult error,
                                          Model model) {

        // 都道府県と郵便番号が不一致の場合
        if (!checkPrefectureAndZipCode(memberAbookModel, error)) {
            error.rejectValue(FILED_NAME_PREFECTURE, MSGCD_PREFECTURE_CONSISTENCY);
        }

        if (error.hasErrors()) {
            return "member/abook/detail";
        }

        // 都道府県の入力チェック。POSTの値の書き換えチェック
        checkPrefecture(memberAbookModel);

        // 登録
        try {
            this.addressBookApi.regist(this.memberAbookHelper.toRequestForAddressBookRegist(memberAbookModel));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "member/abook/detail";
            }
        }

        // 一覧に遷移
        return "redirect:/member/abook/";
    }

    /**
     * 住所録一覧の検索<br/>
     *
     * @param memberAbookModel アドレス帳Model
     * @param model
     */
    protected void searchAddressBookList(MemberAbookModel memberAbookModel, Model model) {

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // リクエスト用のページャー項目をセット
        pageInfoModule.setupPageRequest(pageInfoRequest, this.conversionUtility.toInteger(memberAbookModel.getPnum()),
                                        memberAbookModel.getLimit(), pageInfoRequest.getOrderBy(),
                                        pageInfoRequest.getSort()
                                       );

        AddressBookCustomerExclusionRequest addressBookCustomerExclusionRequest =
                        new AddressBookCustomerExclusionRequest();
        addressBookCustomerExclusionRequest.setAddressBookCustomerExclusionFlag(true);

        // 住所録一覧の取得
        AddressBookAddressListResponse response =
                        this.addressBookApi.get(addressBookCustomerExclusionRequest, pageInfoRequest);

        if (response == null || CollectionUtils.isEmpty(response.getAddressList())) {
            isNullAddressSearchResponseFlag = true;
            return;
        } else {
            isNullAddressSearchResponseFlag = false;
        }

        // ページの反映
        this.memberAbookHelper.toPageForLoad(response.getAddressList(), memberAbookModel);

        // ページャーにレスポンス情報をセット
        PageInfoResponse pageInfoResponse = response.getPageInfo();
        pageInfoModule.setupPageInfo(memberAbookModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                     pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                     pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages(), null, null, null,
                                     false, null
                                    );

        // ページャーをセット
        pageInfoModule.setupViewPager(memberAbookModel.getPageInfo(), model);

        // 総件数取得（アドレス帳最大件数到達しているかどうかのチェック用）
        memberAbookModel.setTotalCount(pageInfoResponse.getTotal());
    }

    /**
     * 都道府県の入力チェック<br/>
     * POSTされるプルダウンの値の書き換え対策
     *
     * @param memberAbookModel アドレス帳Model
     */
    protected void checkPrefecture(MemberAbookModel memberAbookModel) {

        Map<String, String> prefectureMap = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        boolean existFlag = prefectureMap.containsKey(memberAbookModel.getPrefecture());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_PREFECTURE);
        }
    }

    /**
     * 都道府県と郵便番号の不整合チェック<br/>
     *
     * @param memberAbookModel アドレス帳Model
     * @param error
     */
    protected boolean checkPrefectureAndZipCode(MemberAbookModel memberAbookModel, BindingResult error) {

        // nullの場合未チェック(必須チェックは別タスク)
        if (StringUtils.isEmpty(memberAbookModel.getZipCode1()) || StringUtils.isEmpty(memberAbookModel.getZipCode2())
            || StringUtils.isEmpty(memberAbookModel.getPrefecture())) {
            return true;
        }

        // 都道府県と郵便番号の不整合チェック実行
        try {
            String zipCode = memberAbookModel.getZipCode1() + memberAbookModel.getZipCode2();
            ZipCodeCheckRequest zipCodeCheckRequest = new ZipCodeCheckRequest();
            zipCodeCheckRequest.setZipCode(zipCode);
            zipCodeCheckRequest.setPrefecture(memberAbookModel.getPrefecture());
            this.zipcodeApi.check(zipCodeCheckRequest);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return false;
    }

}