package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCustomerExclusionRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipAddressUpdateRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.front.pc.web.front.order.common.AddressBookModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipUtility;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.BillingAddressIdGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.BillingGroup;
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
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注文 Controller
 *
 * @author Pham Quang Dieu
 */
@RequestMapping("/order")
@Controller
@SessionAttributes({"billingAddressModel", "orderCommonModel"})
public class BillingAddressController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingAddressController.class);

    /** エラーメッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AOX000501";

    /** エラーメッセージコード：都道府県の値をいじくられた */
    protected static final String MSGCD_ILLEGAL_PREFECTURE = "AOX000202";

    protected static final String MSGCD_UPDATE_ADDRESS = "LOGISTIC-ADDRESS-UPDATE-001";

    /** ご請求先住所の選択画面からのフラグ */
    public static final String FROM_BILLING_SELECT = "fromBillingselect";

    /** ご請求先住所の登録画面からのフラグ */
    public static final String FROM_BILLING_SELECT_NEW = "fromBillingselectNew";

    /** デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** ご請求先住所の選択 : ソート項目 */
    private static final String DEFAULT_BILLINGADDRESSSEARCH_ORDER_FIELD = "customerid";

    /** お届け先入力画面Helper */
    private AddressSelectHelper addressSelectHelper;

    /** 郵便番号API */
    private ZipcodeApi zipcodeApi;

    /** 住所Api */
    private AddressBookApi addressBookApi;

    /** 請求伝票（注文決済情報） API */
    private final BillingSlipApi billingSlipApi;

    /** お客様情報入力画面 Helper */
    private BillingAddressHelper billingAddressHelper;

    /**  */
    private SalesSlipUtility salesSlipUtility;

    /** コンストラクタ */
    @Autowired
    public BillingAddressController(AddressSelectHelper addressSelectHelper,
                                    ZipcodeApi zipcodeApi,
                                    AddressBookApi addressBookApi,
                                    BillingSlipApi billingSlipApi,
                                    BillingAddressHelper billingAddressHelper,
                                    SalesSlipUtility salesSlipUtility) {
        this.addressSelectHelper = addressSelectHelper;
        this.zipcodeApi = zipcodeApi;
        this.addressBookApi = addressBookApi;
        this.billingSlipApi = billingSlipApi;
        this.billingAddressHelper = billingAddressHelper;
        this.salesSlipUtility = salesSlipUtility;
    }

    /**
     * 依頼主入力画面：初期処理
     * @param billingAddressModel
     * @param orderCommonModel
     * @param redirectAttributes
     * @paran model
     * @return 依頼主入力画面
     */
    @GetMapping(value = "/billingaddressselect")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/billingaddressselect")
    protected String doLoadBillingSelect(BillingAddressModel billingAddressModel,
                                         BindingResult error,
                                         OrderCommonModel orderCommonModel,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {

        // セッションが切れた後の操作、注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        if (!model.containsAttribute(FROM_BILLING_SELECT_NEW)) {
            // モデル初期化
            clearModel(BillingAddressModel.class, billingAddressModel, model);

            selectedAddressBook(billingAddressModel, error, orderCommonModel);
        }

        if (error.hasErrors()) {
            return "order/billingaddressselect";
        }

        createAddressBookItemsForSelect(billingAddressModel, error);
        if (error.hasErrors()) {
            return "order/billingaddressselect";
        }
        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);
        return "order/billingaddressselect";
    }

    /**
     * 届け先入力画面：「このお届け先へ送る」ボタン押下処理
     * @param billingAddressModel
     * @param orderCommonModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 配送方法選択画面
     */
    @PostMapping(value = "/billingaddressselect", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/confirm")
    public String doDeliveryReceiverSelect(
                    @Validated(BillingAddressIdGroup.class) BillingAddressModel billingAddressModel,
                    BindingResult error,
                    OrderCommonModel orderCommonModel,
                    RedirectAttributes redirectAttributes,
                    SessionStatus sessionStatus,
                    Model model) {

        if (error.hasErrors()) {
            return "order/billingaddressselect";
        }

        BillingSlipAddressUpdateRequest billingSlipAddressUpdateRequest = new BillingSlipAddressUpdateRequest();
        billingSlipAddressUpdateRequest.setTransactionId(orderCommonModel.getTransactionId());
        billingSlipAddressUpdateRequest.setBillingAddressId(billingAddressModel.getBillingAddressId());

        try {
            billingSlipApi.updateAddress(billingSlipAddressUpdateRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/confirm";
        }

        // 取引IDをFlashAttributesに保存
        redirectAttributes.addFlashAttribute(FROM_BILLING_SELECT, orderCommonModel.getTransactionId());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/order/confirm";
    }

    /**
     * 届け先入力画面
     *
     * @return 届け先入力画面
     */
    @PostMapping(value = "/billingaddressselect")
    public String doAdd(BillingAddressModel billingAddressModel,
                        OrderCommonModel orderCommonModel,
                        RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(FROM_BILLING_SELECT, billingAddressModel.getBillingAddressId());
        return "redirect:/order/billingaddressselect/new";
    }

    /**
     * 届け先入力画面
     *
     * @return 依頼主入力画面
     */
    @PostMapping(value = "/billingaddressselect/new")
    public String doCancelAdd(BillingAddressModel billingAddressModel,
                              OrderCommonModel orderCommonModel,
                              RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(FROM_BILLING_SELECT_NEW, null);
        return "redirect:/order/billingaddressselect";
    }

    /**
     * 依頼主入力画面：初期処理
     * @param billingAddressModel
     * @param model
     * @param orderCommonModel
     * @paran redirectAttributes
     * @return 依頼主入力画面
     */
    @GetMapping(value = "/billingaddressselect/new")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/billingaddressselect_new")
    protected String doLoadBillingRegist(BillingAddressModel billingAddressModel,
                                         Model model,
                                         OrderCommonModel orderCommonModel,
                                         RedirectAttributes redirectAttributes) {

        // セッションが切れた後の操作、注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        if (model.containsAttribute(FROM_BILLING_SELECT)) {
            // モデル初期化
            clearModel(BillingAddressModel.class, billingAddressModel, model);

            billingAddressModel.setBillingAddressId((String) model.getAttribute(FROM_BILLING_SELECT));
        }

        // 都道府県区分値を取得（北海道：北海道）
        Map<String, String> prefectureTypeItems = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        billingAddressModel.setBillingAddressPrefectureItems(prefectureTypeItems);

        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        return "order/billingaddressselect_new";
    }

    /**
     * 届け先入力画面：「このお届け先へ送る」ボタン押下処理
     *
     * @param billingAddressModel
     * @param orderCommonModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 配送方法選択画面
     */
    @PostMapping(value = "/billingaddressselect/new", params = "doRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/billingaddressselect/new")
    public String doRegist(@Validated(BillingGroup.class) BillingAddressModel billingAddressModel,
                           BindingResult error,
                           OrderCommonModel orderCommonModel,
                           RedirectAttributes redirectAttributes,
                           SessionStatus sessionStatus,
                           Model model) {

        // 都道府県と郵便番号が不一致の場合
        checkPrefectureAndZipCodeBilling(billingAddressModel, error);

        if (error.hasErrors()) {
            salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);
            return "order/billingaddressselect_new";
        }

        // 都道府県の区分値不正チェック
        checkPrefectureBilling(billingAddressModel);

        AddressBookAddressRegistResponse response = null;
        try {
            // 住所登録
            AddressBookAddressRegistRequest addressBookAddressRegistRequest =
                            billingAddressHelper.toAddressBookRegistRequest(billingAddressModel);
            response = addressBookApi.regist(addressBookAddressRegistRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("lastName", "addressLastName");
            itemNameAdjust.put("firstName", "addressFirstName");
            itemNameAdjust.put("firstKana", "addressFirstKana");
            itemNameAdjust.put("lastKana", "addressLastKana");
            itemNameAdjust.put("tel", "addressTel");
            itemNameAdjust.put("zipCode", "addressZipCode");
            itemNameAdjust.put("prefecture", "addressPrefecture");
            itemNameAdjust.put("address1", "addressAddress1");
            itemNameAdjust.put("address2", "addressAddress2");
            itemNameAdjust.put("address3", "addressAddress3");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/billingaddressselect_new";
        }

        String addressId = null;
        if (response != null) {
            addressId = response.getAddressId();
        }

        // 請求先設定
        BillingSlipAddressUpdateRequest shippingSlipAddressUpdateRequest = new BillingSlipAddressUpdateRequest();
        shippingSlipAddressUpdateRequest.setTransactionId(orderCommonModel.getTransactionId());
        shippingSlipAddressUpdateRequest.setBillingAddressId(addressId);
        try {
            billingSlipApi.updateAddress(shippingSlipAddressUpdateRequest);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_UPDATE_ADDRESS, redirectAttributes, model);
            return "redirect:/order/billingaddressselect";
        }

        // 取引IDをFlashAttributesに保存
        redirectAttributes.addFlashAttribute(FROM_BILLING_SELECT, orderCommonModel.getTransactionId());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/order/confirm";
    }

    /**
     * アドレス帳プルダウン作成
     *
     * @param billingAddressModel
     */
    protected void createAddressBookItemsForSelect(BillingAddressModel billingAddressModel, BindingResult error) {

        try {
            // アドレス帳取得サービス実行
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE,
                                            DEFAULT_BILLINGADDRESSSEARCH_ORDER_FIELD, true
                                           );
            AddressBookAddressListResponse addressBookAddressListResponse =
                            addressBookApi.get(new AddressBookCustomerExclusionRequest(), pageInfoRequest);
            List<AddressBookModel> addressBookModelList =
                            billingAddressHelper.convertToAddressBookEntityList(addressBookAddressListResponse);

            billingAddressModel.setAddressBookModelList(addressBookModelList);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 選択済みご請求先設定
     *
     * @param billingAddressModel
     * @param orderCommonModel
     */
    protected void selectedAddressBook(BillingAddressModel billingAddressModel,
                                       BindingResult error,
                                       OrderCommonModel orderCommonModel) {

        // アドレス帳取得サービス実行
        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());

        BillingSlipResponse billingSlipResponse = null;
        try {
            billingSlipResponse = billingSlipApi.get(billingSlipGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        if (!ObjectUtils.isEmpty(billingSlipResponse)) {
            billingAddressModel.setBillingAddressId(billingSlipResponse.getBillingAddressId());
        }
    }

    /**
     * 都道府県と郵便番号の不整合チェック<br/>
     * @param billingAddressModel
     * @return true...OK / false...NG
     */
    protected void checkPrefectureAndZipCodeBilling(BillingAddressModel billingAddressModel, BindingResult error) {

        // nullの場合service未実行(必須チェックは別タスク)
        if (StringUtils.isEmpty(billingAddressModel.getBillingAddressZipCode1()) || StringUtils.isEmpty(
                        billingAddressModel.getBillingAddressZipCode2()) || StringUtils.isEmpty(
                        billingAddressModel.getBillingAddressPrefecture())) {
            return;
        }

        // 郵便番号住所情報取得サービス実行
        try {
            String zipCode = billingAddressModel.getBillingAddressZipCode1()
                             + billingAddressModel.getBillingAddressZipCode2();
            ZipCodeCheckRequest zipCodeCheckRequest = addressSelectHelper.toZipCodeCheckRequest(zipCode,
                                                                                                billingAddressModel.getBillingAddressPrefecture()
                                                                                               );
            zipcodeApi.check(zipCodeCheckRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, new HashMap<>());
        }
    }

    /**
     * 都道府県の入力チェック<br/>
     * @param billingAddressModel
     */
    protected void checkPrefectureBilling(BillingAddressModel billingAddressModel) {
        // 都道府県区分値を取得
        Map<String, String> prefectureMap = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        boolean existFlag = prefectureMap.containsKey(billingAddressModel.getBillingAddressPrefecture());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_PREFECTURE);
        }
    }
}