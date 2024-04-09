/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCustomerExclusionRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipUtility;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.AddressGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.ShippingAddressIdGroup;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipAddressUpdateRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeCheckRequest;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * お届け先住所の登録 Controller
 *
 * @author Pham Quang Dieu
 */
@SessionAttributes({"addressSelectModel", "orderCommonModel"})
@Controller
public class AddressSelectController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressSelectController.class);

    /** エラーメッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AOX000501";

    /** エラーメッセージコード：都道府県の値をいじくられた */
    protected static final String MSGCD_ILLEGAL_PREFECTURE = "AOX000202";

    protected static final String MSGCD_UPDATE_ADDRESS = "LOGISTIC-ADDRESS-UPDATE-001";

    /** お届け先住所の選択画面からのフラグ */
    public static final String FROM_ADDRESS_SELECT = "fromAddressselect";

    /** お届け先住所の登録画面からのフラグ */
    public static final String FROM_ADDRESS_SELECT_NEW = "fromAddressselectNew";

    /** デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** お届け先住所の登録 : ソート項目 */
    private static final String DEFAULT_ADDRESSSELECTSEARCH_ORDER_FIELD = "customerid";

    /** 郵便番号API */
    private final ZipcodeApi zipcodeApi;

    /** 住所Api */
    private final AddressBookApi addressBookApi;

    /** 配送伝票Api */
    private final ShippingSlipApi shippingSlipApi;

    /** 販売伝票Helper */
    private final SalesSlipUtility salesSlipUtility;

    /** お届け先入力画面Helper */
    private final AddressSelectHelper addressSelectHelper;

    /** コンストラクタ */
    @Autowired
    public AddressSelectController(ZipcodeApi zipcodeApi,
                                   SalesSlipUtility salesSlipUtility,
                                   AddressBookApi addressBookApi,
                                   ShippingSlipApi shippingSlipApi,
                                   AddressSelectHelper addressSelectHelper) {
        this.zipcodeApi = zipcodeApi;
        this.salesSlipUtility = salesSlipUtility;
        this.addressBookApi = addressBookApi;
        this.shippingSlipApi = shippingSlipApi;
        this.addressSelectHelper = addressSelectHelper;
    }

    /**
     * お届け先住所の選択の初期化
     *
     * @param addressSelectModel
     * @param orderCommonModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return お届け先住所の選択画面
     */
    @GetMapping(value = "/order/addressselect")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/addressselect")
    public String doLoadAddressSelect(AddressSelectModel addressSelectModel,
                                      BindingResult error,
                                      OrderCommonModel orderCommonModel,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        // セッションが切れた後の操作、注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        if (!model.containsAttribute(FROM_ADDRESS_SELECT_NEW)) {
            // モデル初期化
            clearModel(AddressSelectModel.class, addressSelectModel, model);

            // 配送伝票取得
            ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();

            shippingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());

            ShippingSlipResponse shippingSlipResponse = null;
            try {
                shippingSlipResponse = shippingSlipApi.get(shippingSlipGetRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }

            if (error.hasErrors()) {
                return "order/addressselect";
            }

            addressSelectHelper.toAddressSelectModel(shippingSlipResponse, addressSelectModel);
        }

        // 販売伝票取得
        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        try {
            // 住所リスト取得
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE,
                                            DEFAULT_ADDRESSSELECTSEARCH_ORDER_FIELD, true
                                           );
            AddressBookAddressListResponse addressBookAddressListResponse =
                            addressBookApi.get(new AddressBookCustomerExclusionRequest(), pageInfoRequest);

            addressSelectHelper.toAddressSelectModel(addressBookAddressListResponse, addressSelectModel);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/addressselect";
        }

        return "order/addressselect";
    }

    /**
     * お届け先住所の選択
     *
     * @return 届け先入力画面
     */
    @PostMapping(value = "/order/addressselect")
    public String doAdd(AddressSelectModel addressSelectModel,
                        OrderCommonModel orderCommonModel,
                        RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(FROM_ADDRESS_SELECT, addressSelectModel.getShippingAddressId());
        return "redirect:/order/addressselect/new";
    }

    /**
     * お届け先住所の選択
     *
     * @return お届け先住所の選択画面
     */
    @PostMapping(value = "/order/addressselect/new")
    public String doCancelAdd(AddressSelectModel addressSelectModel,
                              OrderCommonModel orderCommonModel,
                              RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(FROM_ADDRESS_SELECT_NEW, null);
        return "redirect:/order/addressselect";
    }

    /**
     * お届け先住所の選択画面：「選択した住所に届ける」ボタン押下
     *
     * @param addressSelectModel
     * @param orderCommonModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 配送方法選択画面
     */
    @PostMapping(value = "/order/addressselect", params = "doDeliveryReceiverSelect")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/addressselect")
    public String doSelectAddress(@Validated(ShippingAddressIdGroup.class) AddressSelectModel addressSelectModel,
                                  BindingResult error,
                                  OrderCommonModel orderCommonModel,
                                  RedirectAttributes redirectAttributes,
                                  SessionStatus sessionStatus,
                                  Model model) {

        if (error.hasErrors()) {
            return "order/addressselect";
        }

        // 配送先設定
        ShippingSlipAddressUpdateRequest shippingSlipAddressUpdateRequest = new ShippingSlipAddressUpdateRequest();

        shippingSlipAddressUpdateRequest.setTransactionId(orderCommonModel.getTransactionId());
        shippingSlipAddressUpdateRequest.setShippingAddressId(addressSelectModel.getShippingAddressId());

        try {
            shippingSlipApi.updateAddress(shippingSlipAddressUpdateRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/addressselect";
        }

        // 取引IDをFlashAttributesに保存
        redirectAttributes.addFlashAttribute(FROM_ADDRESS_SELECT, orderCommonModel.getTransactionId());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // 注文確認画面へ遷移
        return "redirect:/order/confirm";
    }

    /**
     * 届け先入力画面：初期処理
     * @param addressSelectModel
     * @param orderCommonModel
     * @param model
     * @param redirectAttributes
     * @return 届け先入力画面
     */
    @GetMapping(value = "/order/addressselect/new")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/addressselect/new")
    protected String doLoadAdress(AddressSelectModel addressSelectModel,
                                  OrderCommonModel orderCommonModel,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        // セッションが切れた後の操作、注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        if (model.containsAttribute(FROM_ADDRESS_SELECT)) {
            // モデル初期化
            clearModel(AddressSelectModel.class, addressSelectModel, model);

            addressSelectModel.setShippingAddressId((String) model.getAttribute(FROM_ADDRESS_SELECT));
        }

        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        // 描画前処理
        prerenderReceiver(addressSelectModel);

        return "order/addressselect_new";
    }

    /**
     * 「この住所に届ける」ボタン押下
     * @param addressSelectModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 注文確認画面へ遷移
     */
    @PostMapping(value = "/order/addressselect/new", params = "doRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/addressselect/new")
    public String doRegist(@Validated(AddressGroup.class) AddressSelectModel addressSelectModel,
                           BindingResult error,
                           OrderCommonModel orderCommonModel,
                           RedirectAttributes redirectAttributes,
                           SessionStatus sessionStatus,
                           Model model) {

        // 都道府県と郵便番号が不一致の場合
        checkPrefectureAndZipCodeReceiver(addressSelectModel, error);

        if (error.hasErrors()) {
            salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);
            return "order/addressselect_new";
        }

        // 都道府県の区分値不正チェック
        checkPrefectureReceiver(addressSelectModel);

        // アドレス帳登録フラグのチェック
        checkReceiverRegistFlg(addressSelectModel);

        AddressBookAddressRegistResponse response = null;
        try {
            // 住所登録
            AddressBookAddressRegistRequest addressBookAddressRegistRequest =
                            addressSelectHelper.toAddressBookRegistRequest(addressSelectModel);
            response = addressBookApi.regist(addressBookAddressRegistRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("tel", "addressTel");
            itemNameAdjust.put("zipCode", "addressZipCode1" + "addressZipCode2");
            itemNameAdjust.put("prefecture", "addressPrefecture");
            itemNameAdjust.put("address1", "addressAddress1");
            itemNameAdjust.put("address2", "addressAddress2");
            itemNameAdjust.put("address3", "addressAddress3");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/addressselect_new";
        }

        String addressId = null;
        if (response != null) {
            addressId = response.getAddressId();
        }

        try {
            // 配送先設定
            ShippingSlipAddressUpdateRequest shippingSlipAddressUpdateRequest =
                            addressSelectHelper.toShippingSlipAddressUpdateRequest(orderCommonModel, addressId);
            shippingSlipApi.updateAddress(shippingSlipAddressUpdateRequest);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_UPDATE_ADDRESS, redirectAttributes, model);
            return "redirect:/order/addressselect";
        }

        // 取引IDをFlashAttributesに保存
        redirectAttributes.addFlashAttribute(FROM_ADDRESS_SELECT, orderCommonModel.getTransactionId());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // ご注文内容確認画面へ遷移
        return "redirect:/order/confirm";
    }

    /**
     * 描画前処理<br/>
     * @param addressSelectModel
     */
    public void prerenderReceiver(AddressSelectModel addressSelectModel) {

        // 都道府県区分値を取得（北海道：北海道）
        Map<String, String> prefectureTypeItems = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        addressSelectModel.setAddressPrefectureItems(prefectureTypeItems);
    }

    /**
     * 都道府県と郵便番号の不整合チェック<br/>
     * @param addressSelectModel
     */
    protected void checkPrefectureAndZipCodeReceiver(AddressSelectModel addressSelectModel, BindingResult error) {

        // nullの場合service未実行(必須チェックは別タスク)
        if (StringUtils.isEmpty(addressSelectModel.getAddressZipCode1()) || StringUtils.isEmpty(
                        addressSelectModel.getAddressZipCode2()) || StringUtils.isEmpty(
                        addressSelectModel.getAddressPrefecture())) {
            return;
        }

        // 郵便番号住所情報取得サービス実行
        try {
            String zipCode = addressSelectModel.getAddressZipCode1() + addressSelectModel.getAddressZipCode2();
            ZipCodeCheckRequest zipCodeCheckRequest = addressSelectHelper.toZipCodeCheckRequest(zipCode,
                                                                                                addressSelectModel.getAddressPrefecture()
                                                                                               );
            zipcodeApi.check(zipCodeCheckRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, new HashMap<>());
        }
    }

    /**
     * 都道府県の入力チェック<br/>
     *
     * @param addressSelectModel
     */
    protected void checkPrefectureReceiver(AddressSelectModel addressSelectModel) {

        // 都道府県区分値を取得
        Map<String, String> prefectureMap = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        boolean existFlag = prefectureMap.containsKey(addressSelectModel.getAddressPrefecture());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_PREFECTURE);
        }
    }

    /**
     * アドレス帳保存フラグのチェック<br/>
     *
     * @param addressSelectModel
     */
    protected void checkReceiverRegistFlg(AddressSelectModel addressSelectModel) {

        // アドレス帳登録フラグのチェック アドレス帳を選択してる場合やアドレス帳MAXの場合登録しない
        if (!StringUtils.isEmpty(addressSelectModel.getAddressAddressBook())
            && !addressSelectModel.isAddressBookFull()) {
            addressSelectModel.setAddressRegistFlg(false);
        } else {
            addressSelectModel.setAddressRegistFlg(true);
        }
    }
}