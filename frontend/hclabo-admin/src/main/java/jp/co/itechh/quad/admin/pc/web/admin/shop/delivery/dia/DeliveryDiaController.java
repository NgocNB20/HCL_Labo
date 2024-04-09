/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.dia;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.OnceZipCodeAddGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.ReDisplayGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.ZipCodeSearchGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.impossiblearea.presentation.api.ImpossibleAreaApi;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaDeleteRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListGetRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListResponse;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaRegistRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
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
 * 配送方法設定：配送不可能エリア設定検索画面用コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery/dia")
@Controller
@SessionAttributes(value = "deliveryDiaModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryDiaController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryDiaController.class);

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
    private static final String MSGCD_ILLEGAL_OPERATION = "AYD000601";

    /**
     * メッセージコード：チェックボックスがチェックされていない
     */
    private static final String MSGCD_NO_CHECK = "AYD000602";

    /**
     * 配送方法SEQ
     */
    private static final String FLASH_DMCD = "dmcd";

    /**
     * 配送方法設定：配送不可能エリア設定検索画面用 Helper
     */
    private final DeliveryDiaHelper deliveryDiaHelper;

    /**
     * 配送方法Api
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 配送不可能エリアApi
     */
    private final ImpossibleAreaApi impossibleAreaApi;

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
     * @param deliveryDiaHelper 配送方法設定：配送不可能エリア設定検索画面用 Helper
     * @param shippingMethodApi 配送方法Api
     * @param impossibleAreaApi 配送不可能エリアApi
     * @param zipcodeApi        郵便番号整合性Api
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public DeliveryDiaController(DeliveryDiaHelper deliveryDiaHelper,
                                 ShippingMethodApi shippingMethodApi,
                                 ImpossibleAreaApi impossibleAreaApi,
                                 ZipcodeApi zipcodeApi,
                                 ConversionUtility conversionUtility) {
        this.deliveryDiaHelper = deliveryDiaHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.impossibleAreaApi = impossibleAreaApi;
        this.zipcodeApi = zipcodeApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理を実行します
     *
     * @param dmcd
     * @param deliveryDiaModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<Integer> dmcd,
                              @RequestParam(required = false) Optional<String> prefectureName,
                              DeliveryDiaModel deliveryDiaModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (deliveryDiaModel.isReloadFlag()) {
            deliveryDiaModel.setReloadFlag(false);
            return "delivery/dia/index";
        }

        // モデルのクリア処理
        if (!prefectureName.isPresent()) {
            clearModel(DeliveryDiaModel.class, deliveryDiaModel, model);
        } else {
            deliveryDiaModel.setPrefectureName(prefectureName.get());
        }

        // 都道府県種別設定
        deliveryDiaModel.setPrefectureNameItems(EnumTypeUtil.getEnumMap(HTypePrefectureType.class));

        // 不正操作チェック
        if ((deliveryDiaModel.getDmcd() == null) && (!dmcd.isPresent() && (!model.containsAttribute(FLASH_DMCD)))) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        // 配送方法SEQ取得
        if (dmcd.isPresent()) {
            deliveryDiaModel.setDmcd(dmcd.get());
        } else if (model.containsAttribute(FLASH_DMCD)) {
            deliveryDiaModel.setDmcd((Integer) model.getAttribute(FLASH_DMCD));
        }

        try {
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryDiaModel.getDmcd());
            DeliveryMethodEntity resultEntity = deliveryDiaHelper.toDeliveryMethodEntity(shippingMethodResponse);

            // 不正操作チェック。配送マスタは物理削除されないので、結果がないのはURLパラメータをいじられた以外ありえない。
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/";
            } else {
                deliveryDiaHelper.convertToRegistPageForResult(deliveryDiaModel, resultEntity);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/dia/index";
        }

        // pageNumber,limitを初期化
        deliveryDiaModel.setPageNumber(DEFAULT_PNUM);
        deliveryDiaModel.setLimit(DEFAULT_LIMIT);

        // 検索処理
        try {
            search(deliveryDiaModel, error, model);
            if (error.hasErrors()) {
                return "delivery/dia/index";
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        return "delivery/dia/index";
    }

    /**
     * 選択された都道府県で再検索を行います
     *
     * @param deliveryDiaModel
     * @param model
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String reSearch(DeliveryDiaModel deliveryDiaModel, BindingResult error, Model model)
                    throws IllegalAccessException, InvocationTargetException {
        search(deliveryDiaModel, error, model);
        return "delivery/dia/index";
    }

    /**
     * 一覧を再表示します
     *
     * @param deliveryDiaModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping(value = "/", params = "doReDisplay")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/index")
    public String doReDisplay(@Validated(ReDisplayGroup.class) DeliveryDiaModel deliveryDiaModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (error.hasErrors()) {
            return "delivery/dia/index";
        }

        // 不正操作チェック
        if (deliveryDiaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        // pageNumber初期化
        deliveryDiaModel.setPageNumber(DEFAULT_PNUM);

        try {
            String result = reSearch(deliveryDiaModel, error, model);
            if (error.hasErrors()) {
                return "delivery/dia/index";
            }
            return result;

        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }
    }

    /**
     * 画面より選択された情報を削除します
     *
     * @param deliveryDiaModel
     * @param model
     * @return
     */
    @PostMapping(value = "/", params = "doOnceDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/index")
    public String doOnceDelete(DeliveryDiaModel deliveryDiaModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // 不正操作チェック
        if (deliveryDiaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        ImpossibleAreaDeleteRequest impossibleAreaDeleteRequest =
                        deliveryDiaHelper.toImpossibleAreaDeleteRequest(deliveryDiaModel);

        try {
            if (CollectionUtil.isNotEmpty(impossibleAreaDeleteRequest.getDeleteList())) {
                impossibleAreaApi.delete(deliveryDiaModel.getDmcd(), impossibleAreaDeleteRequest);
            } else {
                throwMessage(MSGCD_NO_CHECK);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deleteList", "resultItems");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/dia/index";
        }

        try {
            reSearch(deliveryDiaModel, error, model);
            if (error.hasErrors()) {
                return "delivery/dia/index";
            }

            deliveryDiaModel.setReloadFlag(true);
            String dmcdDia = deliveryDiaModel.getDmcd() != null ? String.valueOf(deliveryDiaModel.getDmcd()) : "";
            String prefectureNameDia = StringUtils.isNotEmpty(deliveryDiaModel.getPrefectureName()) ?
                            deliveryDiaModel.getPrefectureName() :
                            "";
            return "redirect:/delivery/dia/?dmcd=" + dmcdDia + "&prefectureName=" + prefectureNameDia;
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

    }

    /**
     * 検索処理
     *
     * @param deliveryDiaModel
     * @param model
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected void search(DeliveryDiaModel deliveryDiaModel, BindingResult error, Model model)
                    throws IllegalAccessException, InvocationTargetException {
        try {

            DeliveryImpossibleAreaConditionDto conditionDto =
                            ApplicationContextUtility.getBean(DeliveryImpossibleAreaConditionDto.class);

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            // リクエスト用のページャーを生成
            PageInfoRequest pageInfoRequest = new PageInfoRequest();

            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest,
                                            conversionUtility.toInteger(deliveryDiaModel.getPageNumber()),
                                            deliveryDiaModel.getLimit(), null, true
                                           );

            ImpossibleAreaListGetRequest impossibleAreaListGetRequest =
                            deliveryDiaHelper.toImpossibleAreaListGetRequest(deliveryDiaModel);

            ImpossibleAreaListResponse impossibleAreaListResponse =
                            impossibleAreaApi.getByDeliveryMethodSeq(deliveryDiaModel.getDmcd(),
                                                                     impossibleAreaListGetRequest, pageInfoRequest
                                                                    );

            PageInfoResponse pageInfoResponse = impossibleAreaListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(deliveryDiaModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages()
                                        );

            List<DeliveryImpossibleAreaResultDto> resultList =
                            deliveryDiaHelper.toDeliveryImpossibleAreaResultDtoList(impossibleAreaListResponse);
            deliveryDiaHelper.convertToIndexPageItemForResult(resultList, deliveryDiaModel, conditionDto);

            // ページャーセットアップ
            pageInfoHelper.setupViewPager(deliveryDiaModel.getPageInfo(), deliveryDiaModel);

            deliveryDiaModel.setTotalCount(pageInfoResponse.getTotal());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("prefecture", "prefectureName");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * ページング処理を行います
     *
     * @param deliveryDiaModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/index")
    public String doDisplayChange(DeliveryDiaModel deliveryDiaModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        try {
            String result = reSearch(deliveryDiaModel, error, model);
            if (error.hasErrors()) {
                return "delivery/dia/index";
            }
            return result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }
    }

    /**
     * 登録画面：初期処理
     *
     * @param deliveryDiaModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @GetMapping(value = "/regist/")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/index")
    public String doLoadRegistIndex(DeliveryDiaModel deliveryDiaModel,
                                    BindingResult error,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        // 不正操作チェック
        if (deliveryDiaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        try {
            // 初期表示処理
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryDiaModel.getDmcd());
            DeliveryMethodEntity resultEntity = deliveryDiaHelper.toDeliveryMethodEntity(shippingMethodResponse);

            if (resultEntity != null) {
                deliveryDiaHelper.convertToRegistPageForResult(deliveryDiaModel, resultEntity);
            }

            // 初期化
            deliveryDiaModel.setZipCode(null);
            deliveryDiaModel.setAddress(null);

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            if (error.hasErrors()) {
                return "delivery/dia/index";
            }
        }
        return "delivery/dia/regist";
    }

    /**
     * 登録画面：配送特別料金を追加登録します
     *
     * @param deliveryDiaModel
     * @param error
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return
     */
    @PostMapping(value = "/regist/", params = "doOnceZipCodeAdd")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/regist")
    public String doOnceZipCodeAdd(@Validated(OnceZipCodeAddGroup.class) DeliveryDiaModel deliveryDiaModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   SessionStatus sessionStatus,
                                   Model model) {

        if (error.hasErrors()) {
            return "delivery/dia/regist";
        }

        // 不正操作チェック
        if (deliveryDiaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        // 登録内容を設定
        ImpossibleAreaRegistRequest impossibleAreaRegistRequest =
                        deliveryDiaHelper.toImpossibleAreaRegistRequest(deliveryDiaModel);

        // 登録処理を実行
        try {
            impossibleAreaApi.regist(deliveryDiaModel.getDmcd(), impossibleAreaRegistRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/dia/regist";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_DMCD, deliveryDiaModel.getDmcd());

        String dmcdDia = deliveryDiaModel.getDmcd() != null ? String.valueOf(deliveryDiaModel.getDmcd()) : "";
        String prefectureNameDia = StringUtils.isNotEmpty(deliveryDiaModel.getPrefectureName()) ?
                        deliveryDiaModel.getPrefectureName() :
                        "";

        if (StringUtils.isNotEmpty(deliveryDiaModel.getPrefectureName())) {
            return "redirect:/delivery/dia/?dmcd=" + dmcdDia + "&prefectureName=" + prefectureNameDia;
        } else {
            return "redirect:/delivery/dia/?dmcd=" + dmcdDia;
        }
    }

    /**
     * 登録画面：入力された郵便番号で住所を検索します
     *
     * @param deliveryDiaModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping(value = "/regist/", params = "doZipCodeSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/dia/regist")
    public String doZipCodeSearch(@Validated(ZipCodeSearchGroup.class) DeliveryDiaModel deliveryDiaModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (error.hasErrors()) {
            return "delivery/dia/regist";
        }

        // 不正操作チェック
        if (deliveryDiaModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        try {
            ZipCodeAddressGetRequest zipCodeAddressGetRequest =
                            deliveryDiaHelper.toZipCodeAddressGetRequest(deliveryDiaModel.getZipCode());

            ZipCodeAddressResponse zipCodeAddressResponse = zipcodeApi.get(zipCodeAddressGetRequest);
            List<ZipCodeAddressDto> addressList = deliveryDiaHelper.toZipCodeAddressDtoList(zipCodeAddressResponse);
            deliveryDiaHelper.convertToRegistPageForZipCodeResult(deliveryDiaModel, addressList);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return "delivery/dia/regist";
    }
}