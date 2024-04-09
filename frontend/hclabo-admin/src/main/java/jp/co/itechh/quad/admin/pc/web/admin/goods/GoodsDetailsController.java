package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.icon.IconHelper;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessKeyResponse;
import jp.co.itechh.quad.icon.presentation.api.IconApi;
import jp.co.itechh.quad.icon.presentation.api.param.IconListResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductDeleteRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.ListUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 商品管理：商品詳細コントローラー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/goods/details")
@Controller
@SessionAttributes(value = "goodsDetailsModel")
@PreAuthorize("hasAnyAuthority('GOODS:4')")
public class GoodsDetailsController extends AbstractGoodsRegistUpdateController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsDetailsController.class);

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 商品登録更新確認ページDxo<br/>
     */
    private final GoodsDetailsHelper goodsDetailsHelper;

    /**
     * アイコン Helper<br/>
     */
    private final IconHelper iconHelper;

    /**
     * アイコンAPI<br/>
     */
    private final IconApi iconApi;

    /**
     *  商品API<br/>
     */
    private final ProductApi productApi;

    /** 権限グループAPI **/
    private final AuthorizationApi authorizationApi;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /**
     * コンストラクタ
     * @param productApi
     * @param relationApi
     * @param goodsDetailsHelper
     * @param iconHelper
     * @param iconApi
     * @param authorizationApi
     * @param conversionUtility
     * @param dateUtility
     * @param headerParamsHelper
     */
    public GoodsDetailsController(ProductApi productApi,
                                  RelationApi relationApi,
                                  GoodsDetailsHelper goodsDetailsHelper,
                                  IconHelper iconHelper,
                                  IconApi iconApi,
                                  AuthorizationApi authorizationApi,
                                  ConversionUtility conversionUtility,
                                  DateUtility dateUtility,
                                  HeaderParamsHelper headerParamsHelper) {
        super(productApi, relationApi);
        this.goodsDetailsHelper = goodsDetailsHelper;
        this.iconHelper = iconHelper;
        this.iconApi = iconApi;
        this.productApi = productApi;
        this.authorizationApi = authorizationApi;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.headerParamsHelper = headerParamsHelper;
    }

    /**
     * 画像表示処理<br/>
     * 初期表示用メソッド<br/>
     *
     * @param goodsGroupCode
     * @param goodsDetailsModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/details")
    public String doLoadDetails(@RequestParam(required = false) Optional<String> goodsGroupCode,
                                GoodsDetailsModel goodsDetailsModel,
                                BindingResult error,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // モデル初期化
        clearModel(GoodsDetailsModel.class, goodsDetailsModel, model);

        if (goodsGroupCode.isPresent()) {
            goodsDetailsModel.setRedirectGoodsGroupCode(goodsGroupCode.get());
        }

        // 共通初期表示処理
        String errorClass = super.loadPage(goodsDetailsModel, error, goodsDetailsHelper, redirectAttributes, model);

        if (error.hasErrors()) {
            return "goods/details";
        }

        // 共通初期表示エラー時
        if (errorClass != null) {
            return errorClass;
        }

        // 商品グループが存在しない場合エラー
        if (goodsDetailsModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq() == null) {
            // 商品グループコード未指定の場合
            addMessage("AGG000002", redirectAttributes, model);
            return "redirect:/error";
        }

        // カテゴリー・商品タグ設定
        List<CategoryEntity> categoryEntityList = new ArrayList<>();

        // 画面表示用商品グループ取得リクエスト
        ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();
        productDisplayGetRequest.setGoodsGroupCode(goodsDetailsModel.getRedirectGoodsGroupCode());
        productDisplayGetRequest.setGoodCode(null);
        productDisplayGetRequest.setOpenStatus(EnumTypeUtil.getValue(HTypeOpenDeleteStatus.OPEN));
        productDisplayGetRequest.setSiteType(EnumTypeUtil.getValue(HTypeSiteType.BACK));

        // 画面表示用商品グループ取得レスポンス
        ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();
        try {
            // 画面表示用商品グループ取得
            productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "goods/details";
        }

        List<CategoryEntity> categoryOfGoodsList = goodsDetailsHelper.toCategoryEntitiesFromResponse(
                        productDisplayResponse.getCategoryGoodsResponseList());
        if (!ListUtils.isEmpty(categoryOfGoodsList)) {
            goodsDetailsModel.setRegistCategoryEntityList(categoryOfGoodsList);

        } else {
            goodsDetailsModel.setRegistCategoryEntityList(null);
        }

        // ひもづいている商品タグ
        if (productDisplayResponse.getGoodsGroupDisplay() != null
            && productDisplayResponse.getGoodsGroupDisplay().getGoodsTag() != null) {
            goodsDetailsModel.setGoodsTagList(productDisplayResponse.getGoodsGroupDisplay().getGoodsTag());
        }

        // 商品アイコン情報リスト取得
        if (goodsDetailsModel.getIconList() == null) {
            IconListResponse iconListResponse = new IconListResponse();
            try {
                iconListResponse = iconApi.getList(null);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "goods/details";
            }
            List<GoodsInformationIconDto> goodsInformationIconDtos =
                            iconHelper.toGoodsInformationIconDtosFromIconListResponse(iconListResponse);
            goodsDetailsModel.setIconList(goodsInformationIconDtos);
        }

        // ページ表示情報の編集
        goodsDetailsHelper.toPageForLoad(goodsDetailsModel);

        goodsDetailsModel.setRedirectRecycle(null);

        return "goods/details";
    }

    /**
     * 戻るイベント<br/>
     *
     * @param goodsDetailsModel
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return 遷移元
     */
    @PostMapping(value = "/", params = "doBack")
    public String doBack(GoodsDetailsModel goodsDetailsModel,
                         RedirectAttributes redirectAttributes,
                         SessionStatus sessionStatus,
                         Model model) {

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
        // Modelをセッションより破棄
        sessionStatus.setComplete();
        return "redirect:/goods/";
    }

    /**
     * 削除イベント<br/>
     *
     * @param goodsDetailsModel
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return 商品検索画面
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doOnceDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/details")
    public String doOnceDelete(GoodsDetailsModel goodsDetailsModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus,
                               Model model) {
        // 入力情報チェック
        checkDataForDelete(goodsDetailsModel);

        goodsDetailsHelper.toPageForDelete(goodsDetailsModel);

        try {
            ProductDeleteRequest productDeleteRequest = goodsDetailsHelper.toProductDeleteRequest();
            productApi.delete(goodsDetailsModel.getGoodsGroupCode(), productDeleteRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 削除失敗時の公開状態戻し処理
            if (goodsDetailsModel.getOldGoodsOpenStatusPC() != null) {
                restoreData(goodsDetailsModel);
            }
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "goods/details";
        }

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);

        // Modelをセッションより破棄
        sessionStatus.setComplete();
        return "redirect:/goods/";
    }

    /**
     * プレビュー表示イベント<br/>
     *
     * @param goodsDetailsModel
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "doPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doPreviewAjax(@Validated(PreviewGroup.class) GoodsDetailsModel goodsDetailsModel,
                                           BindingResult error,
                                           RedirectAttributes redirectAttrs,
                                           Model model) {

        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }
        if (StringUtils.isBlank(goodsDetailsModel.getPreviewTime())) {
            // 日付のみ入力の場合、時間を設定
            goodsDetailsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }

        try {
            // 管理者SEQをヘッダーに設定
            Integer seq = getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq();
            this.headerParamsHelper.setAdminSeq(ObjectUtils.isEmpty(seq) ? null : seq.toString());

            PreviewAccessKeyResponse response = this.authorizationApi.issuePreviewAccessKey();
            if (ObjectUtils.isEmpty(response) || StringUtils.isEmpty(response.getPreviewAccessKey())) {
                return ResponseEntity.internalServerError().body("/error");
            }
            goodsDetailsModel.setPreKey(response.getPreviewAccessKey());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            return ResponseEntity.internalServerError().body("/error");
        }
        // Timestampに変換後、指定の書式（yyyyMMddHHmmss）に変換
        Timestamp tmp = this.dateUtility.toTimestampValue(
                        goodsDetailsModel.getPreviewDate() + " " + goodsDetailsModel.getPreviewTime(),
                        this.dateUtility.YMD_SLASH_HMS
                                                         );
        goodsDetailsModel.setPreTime(this.dateUtility.format(tmp, this.dateUtility.YMD_HMS));
        return ResponseEntity.ok(goodsDetailsModel);
    }

    /**
     * 入力情報チェック（削除処理用）<br/>
     * Validatorで対応できないもの
     *
     * @param goodsDetailsModel
     */
    private void checkDataForDelete(GoodsDetailsModel goodsDetailsModel) {
        // 既に削除済みエラー
        if (HTypeOpenDeleteStatus.DELETED == goodsDetailsModel.getGoodsGroupDto()
                                                              .getGoodsGroupEntity()
                                                              .getGoodsOpenStatusPC()) {
            addErrorMessage("AGG001101");
        }
        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * エラー時のデータ復元<br/>
     *
     * @param goodsDetailsModel
     */
    private void restoreData(GoodsDetailsModel goodsDetailsModel) {
        GoodsGroupEntity goodsGroupEntity = goodsDetailsModel.getGoodsGroupDto().getGoodsGroupEntity();
        goodsGroupEntity.setGoodsOpenStatusPC(goodsDetailsModel.getOldGoodsOpenStatusPC());
    }

}