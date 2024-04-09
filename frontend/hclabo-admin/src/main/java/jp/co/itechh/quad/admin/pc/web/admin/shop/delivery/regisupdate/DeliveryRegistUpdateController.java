package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelException;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.NumberUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate.validation.DeliveryRegistUpdateValidator;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 配送方法登録更新
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery")
@Controller
@SessionAttributes(value = "deliveryRegistUpdateModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryRegistUpdateController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryRegistUpdateController.class);

    /**
     * メッセージコード：不正操作
     */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AYD000201";

    /**
     * メッセージコード：不足金額は割引送料が0円の場合のみ表示可能
     */
    protected static final String MSGCD_NO_SHORTFALL_DISPLAY = "AYD000208";

    /**
     * メッセージコード：配送追跡URLの伝票番号引換コードエラー
     */
    protected static final String MSGCD_CHASEURL_ERROR = "AYD000801";

    /**
     * 入力チェック：不足金額比較用
     */
    private static final BigDecimal ZERO = new BigDecimal(0);

    /**
     * 配送方法Api
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 配送方法登録・更新入力画面Helper
     */
    private final DeliveryRegistUpdateHelper deliveryRegistUpdateHelper;

    /**
     * 配送登録更新の動的バリデータ
     */
    private final DeliveryRegistUpdateValidator deliveryRegistUpdateValidator;

    /**
     * コンストラクタ
     *
     * @param deliveryRegistUpdateHelper    配送方法登録・更新入力画面Helper
     * @param deliveryRegistUpdateValidator 配送登録更新の動的バリデータ
     * @param shippingMethodApi             配送方法Api
     */
    @Autowired
    public DeliveryRegistUpdateController(DeliveryRegistUpdateHelper deliveryRegistUpdateHelper,
                                          DeliveryRegistUpdateValidator deliveryRegistUpdateValidator,
                                          ShippingMethodApi shippingMethodApi) {
        this.deliveryRegistUpdateHelper = deliveryRegistUpdateHelper;
        this.deliveryRegistUpdateValidator = deliveryRegistUpdateValidator;
        this.shippingMethodApi = shippingMethodApi;
    }

    /**
     * 配送登録更新の動的バリデータ
     *
     * @param error WebDataBinder
     */
    @InitBinder(value = "deliveryRegistUpdateModel")
    public void initBinder(WebDataBinder error) {
        // メール件名の動的バリデータをセット
        error.addValidators(deliveryRegistUpdateValidator);
    }

    /**
     * 初期処理
     *
     * @param dmcd                      配送方法SEQ
     * @param from                      From
     * @param deliveryRegistUpdateModel 配送方法登録・更新画面ページ
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     * @return 自画面
     */
    @GetMapping(value = "/registupdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/registupdate/index")
    public String doLoadIndex(@RequestParam(required = false) String dmcd,
                              @RequestParam(required = false) Optional<String> from,
                              DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (from.isPresent() && from.get().equals("confirm")) {
            return "delivery/registupdate/index";
        }

        try {
            // 数値関連Helper取得
            NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

            // 初期遷移の時。（確認画面からの遷移でない場合）
            if (!deliveryRegistUpdateModel.isEditFlag()) {
                clearModel(DeliveryRegistUpdateModel.class, deliveryRegistUpdateModel, model);

                DeliveryMethodDetailsDto deliveryMethodDetailsDto =
                                ApplicationContextUtility.getBean(DeliveryMethodDetailsDto.class);

                // 更新モード
                if (!StringUtil.isEmpty(dmcd) && numberUtility.isNumber(dmcd)) {
                    Integer deliveryMethodSeq = IntegerConversionUtil.toInteger(dmcd);

                    // 配送方法詳細取得サービス実行
                    ShippingMethodResponse shippingMethodResponse =
                                    shippingMethodApi.getByDeliveryMethodSeq(deliveryMethodSeq);
                    deliveryMethodDetailsDto =
                                    deliveryRegistUpdateHelper.toDeliveryMethodDetailsDto(shippingMethodResponse);
                }
                deliveryRegistUpdateHelper.toPageForLoadIndex(deliveryRegistUpdateModel, deliveryMethodDetailsDto);
            }

            // 修正画面の場合、画面用配送方法SEQを設定
            if (!StringUtil.isEmpty(dmcd) && numberUtility.isNumber(dmcd)) {
                deliveryRegistUpdateModel.setScDeliveryMethodSeq(deliveryRegistUpdateModel.getDeliveryMethodDetailsDto()
                                                                                          .getDeliveryMethodEntity()
                                                                                          .getDeliveryMethodSeq());
            }

            deliveryRegistUpdateModel.setEditFlag(false);

            // 実行前処理
            String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
            if (StringUtils.isNotEmpty(check)) {
                return check;
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // 画面初期描画時のデフォルト値を設定
        deliveryRegistUpdateHelper.setDefaultValueForLoad(deliveryRegistUpdateModel);

        return "delivery/registupdate/index";
    }

    /**
     * 「配送種別」プルダウンを変更した時の処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     * @return 自画面
     */
    @PostMapping(value = "/registupdate", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/registupdate/index")
    public String doDisplayChange(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        // 実行前処理
        String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 不正操作チェック
        if (!deliveryRegistUpdateModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }
        return "delivery/registupdate/index";
    }

    /**
     * 「確認」ボタン押下処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param error                     BindingResult
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     * @return 自画面
     */
    @PostMapping(value = "/registupdate", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/registupdate/index")
    public String doConfirm(@Validated(ConfirmGroup.class) DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 実行前処理
        String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "delivery/registupdate/index";
        }

        // 不正操作チェック
        if (!deliveryRegistUpdateModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        // 配送追跡URLチェック
        if (inputDeliveryChaseURLCheck(deliveryRegistUpdateModel)) {
            throwMessage(MSGCD_CHASEURL_ERROR);
        }

        // 入力情報を変換
        deliveryRegistUpdateHelper.toPageForConfirmIndex(deliveryRegistUpdateModel);

        // 入力内容チェック
        checkContents(deliveryRegistUpdateModel, error);
        if (error.hasErrors()) {
            return "delivery/registupdate/index";
        }

        // 確認画面へ遷移
        return "redirect:/delivery/registupdate/confirm";
    }

    /**
     * 初期処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     * @return 自画面
     */
    @GetMapping(value = "/registupdate/confirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/delivery/registupdate/?from=confirm")
    protected String doLoadConfirm(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        // ブラウザバックの場合、処理しない
        if (deliveryRegistUpdateModel.getDeliveryMethodEntity() == null) {
            return "redirect:/delivery/";
        }

        // 実行前処理
        String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 確認画面に直接飛んできた
        if (deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryMethodName() == null) {
            throwMessage(MSGCD_ILLEGAL_OPERATION);
        }

        // ページへ変換
        deliveryRegistUpdateHelper.toPageForLoadConfirm(deliveryRegistUpdateModel);

        // ※金額別送料は現在未使用
        if (HTypeDeliveryMethodType.AMOUNT.getValue().equals(deliveryRegistUpdateModel.getDeliveryMethodType())) {
            // 金額別送料設定リストを上限金額でソート
            sortAmountCarriage(deliveryRegistUpdateModel);
        }

        // 修正の場合、画面用配送方法SEQを設定
        Integer scDeliveryMethodSeq = null;
        try {
            scDeliveryMethodSeq = deliveryRegistUpdateModel.getDeliveryMethodDetailsDto()
                                                           .getDeliveryMethodEntity()
                                                           .getDeliveryMethodSeq();
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            // NULLチェックを省く目的のtry-catchであるため、例外キャッチ時の処理は行わない
        }
        if (scDeliveryMethodSeq != null) {
            deliveryRegistUpdateModel.setScDeliveryMethodSeq(scDeliveryMethodSeq);
        }

        return "delivery/registupdate/confirm";
    }

    /**
     * 登録処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param error                     BindingResult
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param sessionStatus             セクションステータス
     * @param model                     モデル
     * @return 配送方法設定画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doOnceRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/registupdate/confirm")
    public String doOnceRegist(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus,
                               Model model) {

        // 実行前処理
        String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "delivery/registupdate/index";
        }

        // 不正操作
        if (!deliveryRegistUpdateModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/registupdate";
        }

        // 配送方法登録サービス実行
        ShippingMethodRegistRequest shippingMethodRegistRequest =
                        deliveryRegistUpdateHelper.toShippingMethodRegistRequest(deliveryRegistUpdateModel);
        try {
            shippingMethodApi.regist(shippingMethodRegistRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/registupdate/confirm";
        }
        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/delivery/";
    }

    /**
     * 更新処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param error                     BindingResult
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param sessionStatus             セクションステータス
     * @param model                     モデル
     * @return 配送方法設定画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doOnceUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/registupdate/confirm")
    public String doOnceUpdate(@Validated DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus,
                               Model model) {

        // 実行前処理
        String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "delivery/registupdate/index";
        }

        try {
            // 不正操作
            if (!deliveryRegistUpdateModel.isNormality()) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/registupdate";
            }

            ShippingMethodUpdateRequest shippingMethodUpdateRequest =
                            deliveryRegistUpdateHelper.toShippingMethodUpdateRequest(deliveryRegistUpdateModel);

            // 配送方法更新サービス実行
            shippingMethodApi.update(deliveryRegistUpdateModel.getScDeliveryMethodSeq(), shippingMethodUpdateRequest);

            // Modelをセッションより破棄
            sessionStatus.setComplete();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "scDeliveryMethodSeq");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/registupdate/confirm";
        }
        return "redirect:/delivery/";
    }

    /**
     * 「キャンセル」ボタン押下処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     * @return 配送方法登録・更新入力画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doIndex")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/registupdate/confirm")
    public String doIndex(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        // 実行前処理
        String check = preDoAction(deliveryRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 不正操作
        if (!deliveryRegistUpdateModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/registupdate";
        }
        deliveryRegistUpdateModel.setEditFlag(true);
        return "redirect:/delivery/registupdate";
    }

    /**
     * 配送追跡URLのエラーチェック
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @return true エラー /false 正常
     */
    private boolean inputDeliveryChaseURLCheck(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {
        if (StringUtil.isNotEmpty(deliveryRegistUpdateModel.getDeliveryChaseURL())) {
            try {
                MessageFormat.format(deliveryRegistUpdateModel.getDeliveryChaseURL(), "");
            } catch (IllegalArgumentException e) {
                LOGGER.error("例外処理が発生しました", e);
                return true;
            }
        }
        return false;
    }

    /**
     * アクション実行前処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     */
    public String preDoAction(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        return checkIllegalOperation(deliveryRegistUpdateModel, redirectAttributes, model);
    }

    /**
     * 不正操作チェック
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param redirectAttributes        リダイレクトアトリビュート
     * @param model                     モデル
     */
    protected String checkIllegalOperation(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        Integer scDeliveryMethodSeq = deliveryRegistUpdateModel.getScDeliveryMethodSeq();
        Integer dbDeliveryMethodSeq = null;
        if (deliveryRegistUpdateModel.getDeliveryMethodDetailsDto() != null
            && deliveryRegistUpdateModel.getDeliveryMethodDetailsDto().getDeliveryMethodEntity() != null) {
            dbDeliveryMethodSeq = deliveryRegistUpdateModel.getDeliveryMethodDetailsDto()
                                                           .getDeliveryMethodEntity()
                                                           .getDeliveryMethodSeq();
        }

        boolean isError = false;

        // 登録画面にも関わらず、配送方法SEQのDB情報を保持している場合エラー
        if (scDeliveryMethodSeq == null && dbDeliveryMethodSeq != null) {
            isError = true;

            // 修正画面にも関わらず、配送方法SEQのDB情報を保持していない場合エラー
        } else if (scDeliveryMethodSeq != null && dbDeliveryMethodSeq == null) {
            isError = true;

            // 画面用配送方法SEQとDB用配送方法SEQが異なる場合エラー
        } else if (scDeliveryMethodSeq != null && !scDeliveryMethodSeq.equals(dbDeliveryMethodSeq)) {
            isError = true;
        }

        if (isError) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        return null;
    }

    /**
     * 入力内容チェック
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     */
    protected void checkContents(DeliveryRegistUpdateModel deliveryRegistUpdateModel, BindingResult error) {

        DeliveryMethodEntity deliveryMethodEntity = deliveryRegistUpdateModel.getDeliveryMethodEntity();

        List<AppLevelException> errorList = new ArrayList<>();

        // 配送方法データチェックサービス実行
        try {
            ShippingMethodCheckRequest shippingMethodCheckRequest =
                            deliveryRegistUpdateHelper.toShippingMethodCheckRequest(deliveryMethodEntity);
            shippingMethodApi.check(shippingMethodCheckRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // 不足金額表示チェック
        try {
            checkShortfallDisplay(deliveryRegistUpdateModel);
        } catch (AppLevelListException e) {
            LOGGER.error("例外処理が発生しました", e);
            errorList.addAll(e.getErrorList());
        }

        if (!errorList.isEmpty()) {
            throw new AppLevelListException(errorList);
        }

    }

    /**
     * 不足金額表示チェック<br/>
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     */
    private void checkShortfallDisplay(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {
        BigDecimal discountCarriage = BigDecimalConversionUtil.toBigDecimal(
                        deliveryRegistUpdateModel.getLargeAmountDiscountCarriage());
        if (deliveryRegistUpdateModel.isShortfallDisplayFlag() && (discountCarriage == null
                                                                   || discountCarriage.compareTo(ZERO) != 0)) {
            // フロントでの送料無料メッセージは送料が無料の場合のみ表示するため、表示かつ高額割引送料が0円以外の場合はエラー
            addErrorMessage(MSGCD_NO_SHORTFALL_DISPLAY);
            throwMessage();
        }
    }

    /**
     * 金額別送料設定リストを上限金額でソート（昇順）
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     */
    protected void sortAmountCarriage(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {
        List<DeliveryAmountCarriageItem> amountCarriageItems =
                        deliveryRegistUpdateModel.getDeliveryAmountCarriageItems();
        DeliveryAmountCarriageItem amountCarriageItem = null;
        DeliveryAmountCarriageItem nextAmountCarriageItem = null;
        BigDecimal maxPrice = null;
        BigDecimal nextMaxPrice = null;
        boolean adjust = false;

        for (int i = 0; i < amountCarriageItems.size(); i++) {
            if (i == amountCarriageItems.size() - 1) {
                if (adjust) {
                    // ソート完了するまで再起
                    sortAmountCarriage(deliveryRegistUpdateModel);
                }

                // ソート完了

            } else {
                amountCarriageItem = amountCarriageItems.get(i);
                nextAmountCarriageItem = amountCarriageItems.get(i + 1);

                if (StringUtil.isEmpty(amountCarriageItem.getMaxPrice()) && !StringUtil.isEmpty(
                                nextAmountCarriageItem.getMaxPrice())) {
                    amountCarriageItems.add(i, amountCarriageItems.remove(i + 1));
                    adjust = true;

                } else if (!StringUtil.isEmpty(amountCarriageItem.getMaxPrice()) && !StringUtil.isEmpty(
                                nextAmountCarriageItem.getMaxPrice())) {
                    maxPrice = BigDecimalConversionUtil.toBigDecimal(amountCarriageItem.getMaxPrice());
                    nextMaxPrice = BigDecimalConversionUtil.toBigDecimal(nextAmountCarriageItem.getMaxPrice());
                    if (maxPrice.compareTo(nextMaxPrice) > 0) {
                        amountCarriageItems.add(i, amountCarriageItems.remove(i + 1));
                        adjust = true;
                    }
                }
            }
        }
    }
}