/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.sca;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliverySpecialChargeAreaConditionDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.OnceZipCodeAddGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.ReDisplayGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.ZipCodeSearchGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.SpecialChargeAreaApi;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaDeleteRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListGetRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaRegistRequest;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressGetRequest;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 配送方法設定：特別料金エリア設定検索画面用コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery/sca")
@Controller
@SessionAttributes(value = "deliveryScaModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryScaController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryScaController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト最大表示件数
     */
    private static final int DEFAULT_LIMIT = 100;

    /**
     * メッセージコード：不正操作
     */
    private static final String MSGCD_ILLEGAL_OPERATION = "AYD000401";

    /**
     * メッセージコード：チェックボックスがチェックされていない
     */
    protected static final String MSGCD_NO_CHECK = "AYD000402";

    /**
     * 配送方法SEQ
     */
    private static final String FLASH_DMCD = "dmcd";

    /**
     * 配送方法設定：特別料金エリア設定検索画面用 Helper
     */
    private final DeliveryScaHelper deliveryScaHelper;

    /**
     * 配送方法Api
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 特別料金エリアApi
     */
    private final SpecialChargeAreaApi specialChargeAreaApi;

    /**
     * 郵便番号整合性Api
     */
    private final ZipcodeApi zipcodeApi;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param deliveryScaHelper    配送方法設定：特別料金エリア設定検索画面用 Helper
     * @param shippingMethodApi    配送方法Api
     * @param specialChargeAreaApi 特別料金エリアApi
     * @param zipcodeApi           郵便番号整合性Api
     * @param conversionUtility    変換ユーティリティクラス
     */
    @Autowired
    public DeliveryScaController(DeliveryScaHelper deliveryScaHelper,
                                 ShippingMethodApi shippingMethodApi,
                                 SpecialChargeAreaApi specialChargeAreaApi,
                                 ZipcodeApi zipcodeApi,
                                 ConversionUtility conversionUtility) {
        this.deliveryScaHelper = deliveryScaHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.specialChargeAreaApi = specialChargeAreaApi;
        this.zipcodeApi = zipcodeApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理を実行します
     *
     * @param dmcd               配送方法SEQ
     * @param prefectureName     エリア
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return string
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/delivery/sca")
    public String doLoadIndex(@RequestParam(required = false) Optional<Integer> dmcd,
                              @RequestParam(required = false) Optional<String> prefectureName,
                              DeliveryScaModel deliveryScaModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (deliveryScaModel.isReloadFlag()) {
            deliveryScaModel.setReloadFlag(false);
            return "delivery/sca/index";
        }

        // モデルのクリア処理
        if (!prefectureName.isPresent()) {
            clearModel(DeliveryScaModel.class, deliveryScaModel, model);
        } else {
            deliveryScaModel.setPrefectureName(prefectureName.get());
        }

        // 都道府県種別設定
        deliveryScaModel.setPrefectureNameItems(EnumTypeUtil.getEnumMap(HTypePrefectureType.class));

        // 不正操作チェック
        if ((deliveryScaModel.getDmcd() == null) && (!dmcd.isPresent() && (!model.containsAttribute(FLASH_DMCD)))) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        // 配送方法SEQ取得
        if (dmcd.isPresent()) {
            deliveryScaModel.setDmcd(dmcd.get());
        } else if (model.containsAttribute(FLASH_DMCD)) {
            deliveryScaModel.setDmcd((Integer) model.getAttribute(FLASH_DMCD));
        }

        try {
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryScaModel.getDmcd());
            DeliveryMethodEntity resultEntity = deliveryScaHelper.toDeliveryMethodEntity(shippingMethodResponse);

            // 不正操作チェック。配送マスタは物理削除されないので、結果がないのはURLパラメータをいじられた以外ありえない。
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/";
            } else {
                deliveryScaHelper.convertToRegistPageForResult(deliveryScaModel, resultEntity);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/delivery/sca";
        }

        // pageNumber,limitを初期化
        deliveryScaModel.setPageNumber(DEFAULT_PNUM);
        deliveryScaModel.setLimit(DEFAULT_LIMIT);

        // 検索処理
        try {
            search(deliveryScaModel, error, model);
            if (error.hasErrors()) {
                return "redirect:/delivery/sca";
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        return "delivery/sca/index";
    }

    /**
     * 選択された都道府県で再検索を行います
     *
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return
     */
    private String reSearch(DeliveryScaModel deliveryScaModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        try {
            search(deliveryScaModel, error, model);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }
        return "delivery/sca/index";
    }

    /**
     * 一覧を再表示します
     *
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return
     */
    @PostMapping(value = "/", params = "doReDisplay")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/sca/index")
    public String doReDisplay(@Validated(ReDisplayGroup.class) DeliveryScaModel deliveryScaModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (error.hasErrors()) {
            return "delivery/sca/index";
        }

        // 不正操作チェック
        if (deliveryScaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }
        // pageNumber初期化
        deliveryScaModel.setPageNumber(DEFAULT_PNUM);

        String result = reSearch(deliveryScaModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "delivery/sca/index";
        }
        return result;
    }

    /**
     * 画面より選択された情報を削除します
     *
     * @param deliveryScaModel 特別料金エリア設定検索画面用モデル
     * @param model            the model
     * @return
     */
    @PostMapping(value = "/", params = "doOnceDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/sca/index")
    public String doOnceDelete(DeliveryScaModel deliveryScaModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // 不正操作チェック
        if (deliveryScaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";

        }

        SpecialChargeAreaDeleteRequest specialChargeAreaDeleteRequest =
                        deliveryScaHelper.toSpecialChargeAreaDeleteRequest(deliveryScaModel);

        if (CollectionUtil.isNotEmpty(specialChargeAreaDeleteRequest.getDeleteList())) {
            try {
                specialChargeAreaApi.delete(deliveryScaModel.getDmcd(), specialChargeAreaDeleteRequest);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                itemNameAdjust.put("deliveryMethodSeq", "dmcd");
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
        } else {
            throwMessage(MSGCD_NO_CHECK);
        }
        reSearch(deliveryScaModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "delivery/sca/index";
        }
        deliveryScaModel.setReloadFlag(true);
        String dmcdSca = deliveryScaModel.getDmcd() != null ? String.valueOf(deliveryScaModel.getDmcd()) : "";
        String prefectureNameSca = StringUtils.isNotEmpty(deliveryScaModel.getPrefectureName()) ?
                        deliveryScaModel.getPrefectureName() :
                        "";
        return "redirect:/delivery/sca/?dmcd=" + dmcdSca + "&prefectureName=" + prefectureNameSca;
    }

    /**
     * 検索処理
     *
     * @param deliveryScaModel 特別料金エリア設定検索画面用モデル
     * @param model            the model
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected void search(DeliveryScaModel deliveryScaModel, BindingResult error, Model model)
                    throws IllegalAccessException, InvocationTargetException {
        try {
            DeliverySpecialChargeAreaConditionDto conditionDto =
                            ApplicationContextUtility.getBean(DeliverySpecialChargeAreaConditionDto.class);
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            // リクエスト用のページャーを生成
            PageInfoRequest pageInfoRequest = new PageInfoRequest();

            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest,
                                            conversionUtility.toInteger(deliveryScaModel.getPageNumber()),
                                            deliveryScaModel.getLimit(), null, true
                                           );

            SpecialChargeAreaListGetRequest specialChargeAreaListGetRequest =
                            deliveryScaHelper.toSpecialChargeAreaListGetRequest(deliveryScaModel);
            SpecialChargeAreaListResponse specialChargeAreaListResponse =
                            specialChargeAreaApi.get(deliveryScaModel.getDmcd(), specialChargeAreaListGetRequest,
                                                     pageInfoRequest
                                                    );

            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfoResponse = specialChargeAreaListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(deliveryScaModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages()
                                        );

            List<DeliverySpecialChargeAreaResultDto> resultList =
                            deliveryScaHelper.toDeliverySpecialChargeAreaResultDtoList(specialChargeAreaListResponse);
            deliveryScaHelper.convertToIndexPageItemForResult(resultList, deliveryScaModel, conditionDto);

            // ページャーセットアップ
            pageInfoHelper.setupViewPager(deliveryScaModel.getPageInfo(), deliveryScaModel);

            deliveryScaModel.setTotalCount(pageInfoResponse.getTotal());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("prefecture", "prefectureName");
            itemNameAdjust.put("page", "pageNumber");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * ページング処理を行います
     *
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/sca/index")
    public String doDisplayChange(DeliveryScaModel deliveryScaModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        String result = reSearch(deliveryScaModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "delivery/sca/index";
        }
        return result;
    }

    /**
     * 登録画面：初期処理
     *
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return
     */
    @GetMapping(value = "/regist/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/delivery/sca/")
    public String doLoadRegistIndex(DeliveryScaModel deliveryScaModel,
                                    BindingResult error,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        // 不正操作チェック
        if (deliveryScaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        try {
            // 初期表示処理
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryScaModel.getDmcd());

            DeliveryMethodEntity resultEntity = deliveryScaHelper.toDeliveryMethodEntity(shippingMethodResponse);
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/sca/";
            } else {
                deliveryScaHelper.convertToRegistPageForResult(deliveryScaModel, resultEntity);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "redirect:/delivery/sca/";
        }

        // 初期化
        deliveryScaModel.setZipCode(null);
        deliveryScaModel.setAddress(null);
        deliveryScaModel.setCarriage(null);

        return "delivery/sca/regist";
    }

    /**
     * 登録画面：配送特別料金を追加登録します
     *
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param error              the error
     * @param redirectAttributes the redirect attributes
     * @param sessionStatus      the session status
     * @param model              the model
     * @return string
     */
    @PostMapping(value = "/regist/", params = "doOnceZipCodeAdd")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/sca/regist")
    public String doOnceZipCodeAdd(@Validated(OnceZipCodeAddGroup.class) DeliveryScaModel deliveryScaModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   SessionStatus sessionStatus,
                                   Model model) {

        if (error.hasErrors()) {
            return "delivery/sca/regist";
        }

        // 不正操作チェック
        if (deliveryScaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        // 登録内容を設定
        SpecialChargeAreaRegistRequest specialChargeAreaRegistRequest =
                        deliveryScaHelper.toSpecialChargeAreaRegistRequest(deliveryScaModel);

        // 登録処理を実行
        try {
            specialChargeAreaApi.regist(deliveryScaModel.getDmcd(), specialChargeAreaRegistRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/sca/regist";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_DMCD, deliveryScaModel.getDmcd());

        String prefectureNameSca = StringUtils.isNotEmpty(deliveryScaModel.getPrefectureName()) ?
                        deliveryScaModel.getPrefectureName() :
                        "";
        String dmcdSca = deliveryScaModel.getDmcd() != null ? String.valueOf(deliveryScaModel.getDmcd()) : "";

        if (StringUtils.isNotEmpty(deliveryScaModel.getPrefectureName())) {
            return "redirect:/delivery/sca/?dmcd=" + dmcdSca + "&prefectureName=" + prefectureNameSca;
        } else {
            return "redirect:/delivery/sca/?dmcd=" + dmcdSca;
        }
    }

    /**
     * 登録画面：入力された郵便番号で住所を検索します
     *
     * @param deliveryScaModel   特別料金エリア設定検索画面用モデル
     * @param error              the error
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return string
     */
    @PostMapping(value = "/regist/", params = "doZipCodeSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/sca/regist")
    public String doZipCodeSearch(@Validated(ZipCodeSearchGroup.class) DeliveryScaModel deliveryScaModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        if (error.hasErrors()) {
            return "delivery/sca/regist";
        }

        // 不正操作チェック
        if (deliveryScaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        ZipCodeAddressGetRequest zipCodeAddressGetRequest =
                        deliveryScaHelper.toZipCodeAddressGetRequest(deliveryScaModel.getZipCode());

        ZipCodeAddressResponse zipCodeAddressResponse = new ZipCodeAddressResponse();
        try {
            zipCodeAddressResponse = zipcodeApi.get(zipCodeAddressGetRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/sca/regist";
        }

        List<ZipCodeAddressDto> addressList = deliveryScaHelper.toZipCodeAddressDtoList(zipCodeAddressResponse);

        deliveryScaHelper.convertToRegistPageForZipCodeResult(deliveryScaModel, addressList);

        return "delivery/sca/regist";
    }
}