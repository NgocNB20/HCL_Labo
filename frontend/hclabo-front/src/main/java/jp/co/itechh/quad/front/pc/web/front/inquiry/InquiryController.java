/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.inquiry;

import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSiteType;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.utility.InquiryUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryRegistRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.InquiryGroupApi;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import org.apache.commons.lang.StringUtils;
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * お問い合わせ画面 Controller<br/>
 *
 * @author ando-no
 */
@SessionAttributes(value = "inquiryModel")
@RequestMapping("/inquiry")
@Controller
public class InquiryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryController.class);

    /**
     * エラーメッセージコード：不正操作
     */
    public static final String MSGCD_ILLEGAL_OPERATION = "AIX000101";

    /**
     * Key for InquiryGroup Goods
     */
    public static final String INQUIRYGROUP_GOODS = "inquiry.inquiryGroup.goods";

    /**
     * 前画面が確認画面の「修正する」からであるかを判断するフラグ
     */
    public static final String FLASH_FROMCONFIRM = "fromConfirm";

    /**
     * フラッシュスコープパラメータ：商品詳細画面から商品グループ番号受取用
     */
    public static final String FLASH_GGCD = "ggcd";

    /**
     * フラッシュスコープパラメータ：商品詳細画面から商品番号受取用
     */
    public static final String FLASH_GCD = "gcd";

    /**
     * フラッシュスコープパラメータ：商品詳細画面から商品規格1受取用
     */
    public static final String FLASH_UNIT = "redirectU1Lbl";

    /**
     * helper
     */
    private final InquiryHelper inquiryHelper;

    /**
     * InquiryUtility
     */
    private final InquiryUtility inquiryUtility;

    /**
     * 問い合わせAPI
     */
    private final InquiryApi inquiryApi;

    /**
     * 問い合わせグループAPI
     */
    private final InquiryGroupApi inquiryGroupApi;

    /**
     * ユーザAPI
     */
    private final CustomerApi customerApi;

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * コンストラクタ
     *
     * @param inquiryHelper  問い合わせ Helperクラス
     * @param inquiryUtility 問合せ系ユーティリティ
     */
    @Autowired
    public InquiryController(InquiryHelper inquiryHelper,
                             InquiryUtility inquiryUtility,
                             InquiryApi inquiryApi,
                             InquiryGroupApi inquiryGroupApi,
                             CustomerApi customerApi,
                             ProductApi productApi) {
        this.inquiryHelper = inquiryHelper;
        this.inquiryUtility = inquiryUtility;
        this.inquiryApi = inquiryApi;
        this.inquiryGroupApi = inquiryGroupApi;
        this.customerApi = customerApi;
        this.productApi = productApi;
    }

    /**
     * 入力画面：初期処理
     *
     * @param inquiryModel       お問い合わせ Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 入力画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/index")
    protected String doLoadIndex(InquiryModel inquiryModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        try {
            prerender(inquiryModel, error, model);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MSGCD_ILLEGAL_OPERATION);
        }

        if (error.hasErrors()) {
            return "inquiry/index";
        }

        // 動的コンポーネント作成
        initComponents(inquiryModel, error);

        return "inquiry/index";
    }

    /**
     * 入力画面：問い合わせ確認画面に遷移
     *
     * @param inquiryModel       お問い合わせ Model
     * @param error              エラー
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 問い合わせ確認画面
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/index")
    public String doConfirm(@Validated InquiryModel inquiryModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        // 何らかの不正操作でセッションがクリアされている
        if (inquiryModel.getInquiryGroupItems() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/inquiry/";
        }

        if (error.hasErrors()) {
            return "inquiry/index";
        }

        // ページに変換。
        inquiryHelper.toPageForConfirm(inquiryModel);

        // 問い合わせ確認画面に遷移
        return "redirect:/inquiry/confirm";
    }

    /**
     * 確認画面：初期処理
     *
     * @param inquiryModel       お問い合わせ Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 確認画面
     */
    @GetMapping(value = "/confirm")
    protected String doLoadConfirm(InquiryModel inquiryModel, RedirectAttributes redirectAttributes, Model model) {

        // 何らかの不正操作でセッションがクリアされている
        if (inquiryModel.getInquiryGroup() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/inquiry/";
        }
        return "inquiry/confirm";
    }

    /**
     * 確認画面：入力画面に遷移
     *
     * @param inquiryModel       お問い合わせ Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return お問い合わせ入力画面
     */
    @PostMapping(value = "/confirm", params = "doIndex")
    public String doIndexConfirm(InquiryModel inquiryModel, RedirectAttributes redirectAttributes, Model model) {

        // 遷移元フラグ設定
        redirectAttributes.addFlashAttribute(FLASH_FROMCONFIRM, null);

        return "redirect:/inquiry/";
    }

    /**
     * ２重サブミットの防止<br />
     * 確認画面：DBに登録内容の登録<br />
     * 問い合わせ完了画面への遷移
     *
     * @param inquiryModel       お問い合わせ Model
     * @param sessionStatus      sessionStatus
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 問い合わせ完了画面
     */
    @PostMapping(value = "/confirm", params = "doOnceRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/confirm")
    public String doOnceRegist(InquiryModel inquiryModel,
                               BindingResult error,
                               SessionStatus sessionStatus,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // 問い合わせ登録リクエストに変換
        InquiryRegistRequest inquiryRegistRequest;
        try {
            inquiryRegistRequest = inquiryHelper.toRegistRequestForPage(inquiryModel);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/inquiry/";
        }

        try {
            // お問い合わせ登録サービス実行
            // メール送信も合わせて実施
            inquiryApi.regist(inquiryRegistRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("inquiryGroupSeq", "inquiryGroup");
            itemNameAdjust.put("lastUserInquiryTime", "date");
            itemNameAdjust.put("inquiryTime", "date");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "inquiry/confirm";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // お問い合わせ完了画面に遷移
        return "redirect:/inquiry/complete";
    }

    /**
     * 完了画面：画面表示処理
     *
     * @return 完了画面
     */
    @GetMapping(value = "/complete")
    protected String doLoadComplete() {
        return "inquiry/complete";
    }

    /**
     * 表示前処理<br/>
     *
     * @param inquiryModel お問い合わせ Model
     * @param model        Model
     * @return null;
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Class<?> prerender(InquiryModel inquiryModel, BindingResult error, Model model)
                    throws IllegalAccessException, InvocationTargetException {

        if (!model.containsAttribute(FLASH_FROMCONFIRM)) {
            // 確認画面からの遷移でなければ入力値をクリアする
            clearModel(InquiryModel.class, inquiryModel, model);
            // 会員情報取得、表示
            setMemberInfo(inquiryModel, error, model);

            if (error.hasErrors()) {
                return null;
            }

            setGoodsInfo(inquiryModel, error, model);
        }
        return null;
    }

    /**
     * 動的コンポーネント作成
     *
     * @param inquiryModel お問い合わせ Model
     */
    protected void initComponents(InquiryModel inquiryModel, BindingResult error) {
        createInquiryGroupRadio(inquiryModel, error);
    }

    /**
     * 会員情報を取得
     *
     * @param inquiryModel お問い合わせ Model
     * @param model        Model
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected void setMemberInfo(InquiryModel inquiryModel, BindingResult error, Model model)
                    throws IllegalAccessException, InvocationTargetException {
        // ログイン状態のとき、会員であるとみなして会員情報を取得
        CommonInfoUtility commonInfoUtility = ApplicationContextUtility.getBean(CommonInfoUtility.class);

        if (commonInfoUtility.isLogin(getCommonInfo())) {
            Integer mSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();

            CustomerResponse customerResponse = null;
            try {
                customerResponse = customerApi.getByMemberinfoSeq(mSeq);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }

            // 入会中の場合のみ、後続処理を実行
            if (!ObjectUtils.isEmpty(customerResponse)
                && HTypeMemberInfoStatus.ADMISSION == EnumTypeUtil.getEnumFromValue(
                            HTypeMemberInfoStatus.class, customerResponse.getMemberInfoStatus())) {
                inquiryHelper.toPageForLoad(customerResponse, inquiryModel);
            }
        }
    }

    /**
     * 問い合わせ分類ラジオボタン作成
     *
     * @param inquiryModel お問い合わせ Model
     */
    protected void createInquiryGroupRadio(InquiryModel inquiryModel, BindingResult error) {
        InquiryGroupListResponse inquiryGroupListResponse = null;
        try {
            InquiryGroupListRequest inquiryGroupListRequest = new InquiryGroupListRequest();
            inquiryGroupListRequest.setSiteType(HTypeSiteType.FRONT_PC.getValue());
            inquiryGroupListResponse = inquiryGroupApi.get(null, inquiryGroupListRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }
        inquiryModel.setInquiryGroupItems(inquiryUtility.makeInquiryGroupMap(inquiryGroupListResponse));
    }

    /**
     * Sets goodsInfo if from goods page
     *
     * @param inquiryModel お問い合わせ Model
     * @param model        model
     */
    protected void setGoodsInfo(InquiryModel inquiryModel, BindingResult error, Model model) {

        // 別サブアプリから受け取ったパラメータ設定
        if (model.containsAttribute(FLASH_GGCD) && model.containsAttribute(FLASH_GCD)) {
            inquiryModel.setGgcd((String) model.getAttribute(FLASH_GGCD));
            inquiryModel.setGcd((String) model.getAttribute(FLASH_GCD));
            if (model.containsAttribute(FLASH_UNIT)) {
                inquiryModel.setRedirectU1Lbl((String) model.getAttribute(FLASH_UNIT));
            }
        }

        // ggcdに値があるのは 商品詳細 ページから遷移したものです。
        if (StringUtils.isNotEmpty(inquiryModel.getGgcd())) {

            ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();
            productDisplayGetRequest.setGoodCode(inquiryModel.getGcd());
            productDisplayGetRequest.setGoodsGroupCode(inquiryModel.getGgcd());
            productDisplayGetRequest.setOpenStatus(HTypeOpenStatus.OPEN.getValue());
            productDisplayGetRequest.setSiteType(HTypeSiteType.FRONT_PC.getValue());

            ProductDisplayResponse productDisplayResponse = null;
            try {
                productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                itemNameAdjust.put("goodCode", "gcd");
                itemNameAdjust.put("goodsGroupCode", "ggcd");
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }

            if (!ObjectUtils.isEmpty(productDisplayResponse)) {
                if (!ObjectUtils.isEmpty(productDisplayResponse.getGoodsGroup())) {
                    inquiryModel.setGoodsGroupName(productDisplayResponse.getGoodsGroup().getGoodsGroupName());
                }
                if (!ObjectUtils.isEmpty(productDisplayResponse.getGoodsGroupDisplay())) {
                    inquiryModel.setUnitTitle1(productDisplayResponse.getGoodsGroupDisplay().getUnitTitle1());
                }

                if (StringUtils.isNotEmpty(inquiryModel.getGcd())) {
                    if (!ObjectUtils.isEmpty(productDisplayResponse.getGoodsGroupDisplay())) {
                        inquiryModel.setUnitTitle2(productDisplayResponse.getGoodsGroupDisplay().getUnitTitle2());
                    }

                    if (CollectionUtil.isNotEmpty(productDisplayResponse.getGoodsResponseList())) {
                        for (GoodsResponse goodsResponse : productDisplayResponse.getGoodsResponseList()) {
                            if (goodsResponse.getGoodsSub() != null && inquiryModel.getGcd()
                                                                                   .equals(goodsResponse.getGoodsSub()
                                                                                                        .getGoodsCode())) {
                                inquiryModel.setUnitSelect2Label(goodsResponse.getGoodsSub().getUnitValue2());
                                break;
                            }
                        }
                    }
                }

                inquiryModel.setInquiryGroup(PropertiesUtil.getSystemPropertiesValue(INQUIRYGROUP_GOODS));
            }
        }
    }

}