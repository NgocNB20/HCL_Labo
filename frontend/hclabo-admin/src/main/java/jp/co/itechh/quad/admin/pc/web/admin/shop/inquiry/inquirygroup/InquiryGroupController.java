package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.inquirygroup.presentation.api.InquiryGroupApi;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.ListUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 問い合わせ分類コントローラー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("inquiry/inquirygroup")
@Controller
@SessionAttributes(value = "inquiryGroupModel")
@PreAuthorize("hasAnyAuthority('SHOP:8')")
public class InquiryGroupController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryGroupController.class);

    /**
     * メッセージコード：表示順の保存に成功
     */
    protected static final String MSGCD_SAVE_SUCCESS = "AYD000102";

    /**
     * 問い合わせ分類設定API
     */
    private final InquiryGroupApi inquiryGroupApi;

    /**
     * 問い合わせ分類ヘルパー
     */
    private final InquiryGroupHelper inquiryGroupHelper;

    /**
     * コンストラクタ
     *
     * @param inquiryGroupApi    問い合わせ分類設定API
     * @param inquiryGroupHelper 問い合わせ分類ヘルパー
     */
    public InquiryGroupController(InquiryGroupApi inquiryGroupApi, InquiryGroupHelper inquiryGroupHelper) {
        this.inquiryGroupApi = inquiryGroupApi;
        this.inquiryGroupHelper = inquiryGroupHelper;
    }

    /**
     * 初期処理
     *
     * @return 自画面
     */
    @GetMapping("")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/inquirygroup/index")
    protected String doLoad(InquiryGroupModel inquiryGroupModel, BindingResult error, Model model) {

        clearModel(InquiryGroupModel.class, inquiryGroupModel, model);

        this.search(inquiryGroupModel, error);

        return "inquiry/inquirygroup/index";
    }

    /**
     * 登録更新画面へ遷移(登録)
     *
     * @return 登録更新画面
     */
    @PostMapping(value = "", params = "doRegist")
    public String doRegist() {
        return "redirect:/inquiry/inquirygroup/registupdate/";
    }

    /**
     * 表示順反映
     *
     * @return 自画面
     */
    @PostMapping(value = "", params = "doOnceOrderDisplayUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/inquirygroup/index")
    public String doOnceOrderDisplayUpdate(InquiryGroupModel inquiryGroupModel,
                                           BindingResult error,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {

        InquiryGroupListUpdateRequest inquiryGroupListUpdateRequest =
                        inquiryGroupHelper.toInquiryGroupListUpdateRequestFromInquiryGroupModel(inquiryGroupModel);

        try {
            if (!ListUtils.isEmpty(inquiryGroupListUpdateRequest.getInquiryGroupListUpdate())) {
                // 要素があった場合のみ
                inquiryGroupApi.updateList(inquiryGroupListUpdateRequest);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("orderDisplay", "orderDisplayRadio");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "inquiry/inquirygroup/index";
        }

        // 最新情報取得
        addInfoMessage(MSGCD_SAVE_SUCCESS, null, redirectAttributes, model);

        return "redirect:/inquiry/inquirygroup/";
    }

    /**
     * 表示順変更(一つ上)
     *
     * @return 自画面
     */
    @PostMapping(value = "", params = "doOrderDisplayUp")
    public String doOrderDisplayUp(@Validated InquiryGroupModel inquiryGroupModel, BindingResult error) {
        if (error.hasErrors()) {
            return "inquiry/inquirygroup/index";
        }
        // ラジオボタン選択時のみ対応
        if (inquiryGroupModel.getOrderDisplay() != null) {
            int index = inquiryGroupModel.getOrderDisplay() - 1;
            inquiryGroupHelper.toPageForChangeOrderDisplay(index, index - 1, inquiryGroupModel);
        }

        return "inquiry/inquirygroup/index";
    }

    /**
     * 表示順変更(一つ下)
     *
     * @return 自画面
     */
    @PostMapping(value = "", params = "doOrderDisplayDown")
    public String doOrderDisplayDown(@Validated InquiryGroupModel inquiryGroupModel, BindingResult error) {
        if (error.hasErrors()) {
            return "inquiry/inquirygroup/index";
        }
        // ラジオボタン選択時のみ対応
        if (inquiryGroupModel.getOrderDisplay() != null) {
            int index = inquiryGroupModel.getOrderDisplay() - 1;
            inquiryGroupHelper.toPageForChangeOrderDisplay(index, index + 1, inquiryGroupModel);
        }

        return "inquiry/inquirygroup/index";
    }

    /**
     * 表示順変更(一番上)
     *
     * @return 自画面
     */
    @PostMapping(value = "", params = "doOrderDisplayTop")
    public String doOrderDisplayTop(@Validated InquiryGroupModel inquiryGroupModel, BindingResult error) {
        if (error.hasErrors()) {
            return "inquiry/inquirygroup/index";
        }
        // ラジオボタン選択時のみ対応
        if (inquiryGroupModel.getOrderDisplay() != null) {
            int index = inquiryGroupModel.getOrderDisplay() - 1;
            inquiryGroupHelper.toPageForChangeOrderDisplay(index, 0, inquiryGroupModel);
        }

        return "inquiry/inquirygroup/index";
    }

    /**
     * 表示順変更(一番下)
     *
     * @return 自画面
     */
    @PostMapping(value = "", params = "doOrderDisplayBottom")
    public String doOrderDisplayBottom(@Validated InquiryGroupModel inquiryGroupModel, BindingResult error) {
        if (error.hasErrors()) {
            return "inquiry/inquirygroup/index";
        }
        // ラジオボタン選択時のみ対応
        if (inquiryGroupModel.getOrderDisplay() != null) {
            int index = inquiryGroupModel.getOrderDisplay() - 1;
            inquiryGroupHelper.toPageForChangeOrderDisplay(
                            index, inquiryGroupModel.getResultItems().size() - 1, inquiryGroupModel);
        }
        return "inquiry/inquirygroup/index";
    }

    /**
     * 検索処理
     *
     * @param inquiryGroupModel
     */
    protected void search(InquiryGroupModel inquiryGroupModel, BindingResult error) {
        try {
            InquiryGroupListRequest inquiryGroupListRequest = new InquiryGroupListRequest();
            inquiryGroupListRequest.setSiteType(HTypeSiteType.BACK.getValue());
            InquiryGroupListResponse inquiryGroupListResponse = inquiryGroupApi.get(null, inquiryGroupListRequest);
            inquiryGroupHelper.toPageForLoad(inquiryGroupListResponse, inquiryGroupModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }
}