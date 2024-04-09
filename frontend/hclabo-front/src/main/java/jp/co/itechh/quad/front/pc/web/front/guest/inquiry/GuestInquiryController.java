package jp.co.itechh.quad.front.pc.web.front.guest.inquiry;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.front.pc.web.front.common.inquiry.AbstractInquiryController;
import jp.co.itechh.quad.front.utility.InquiryUtility;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ゲスト問合せ Controller<br/>
 *
 * @author Pham Quang Dieu
 *
 */
@SessionAttributes(value = "guestInquiryModel")
@RequestMapping("/guest/inquiry/")
@Controller
public class GuestInquiryController extends AbstractInquiryController {

    /** デフォルト戻り先ページ*/
    private static final String BACKPAGE_VIEW = "redirect:/login/inquiry/";

    /**
     * 不正遷移<br/>
     */
    private static final String MSGCD_REFERER_FAIL = "PKG-3720-001-A-";

    /** メッセージコード：会員の問合わせの場合 */
    private static final String MSGCD_MEMBER_INQUIRY = "PKG-3720-001-A-";

    /** 問合せ系ユーティリティ */
    private final InquiryUtility inquiryUtility;

    /**
     * コンストラクタ
     *
     * @param inquiryApi 問い合わせAPI
     * @param guestInquiryHelper ゲスト問合せ Helper
     * @param inquiryUtility 問合せ系ユーティリティ
     */
    @Autowired
    public GuestInquiryController(InquiryApi inquiryApi,
                                  GuestInquiryHelper guestInquiryHelper,
                                  InquiryUtility inquiryUtility) {
        super(inquiryApi, guestInquiryHelper);
        this.inquiryUtility = inquiryUtility;
    }

    /**
     * ゲスト問合せ画面：初期処理
     *
     * @param guestInquiryModel ゲスト問合せ Model
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @return ゲスト問合せ詳細画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = BACKPAGE_VIEW)
    protected String doLoadIndex(GuestInquiryModel guestInquiryModel,
                                 @RequestParam(required = false) String icd,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // FlashAttributeからお問い合わせ番号を受け渡されていた場合
        // Modelにセットする（saveIcdに保存）
        if (model.containsAttribute("icd")) {
            guestInquiryModel.setSaveIcd((String) model.getAttribute("icd"));
            guestInquiryModel.setIcd((String) model.getAttribute("icd"));
        }

        String viewName = doLoad(guestInquiryModel, error, redirectAttributes, model);

        if (viewName != null && StringUtils.isNotEmpty(icd)) {
            return BACKPAGE_VIEW + "?icd=" + icd;
        }

        if (viewName == null) {
            viewName = "guest/inquiry/detail";
        }

        return viewName;
    }

    /**
     * 登録処理
     *
     * @param guestInquiryModel ゲスト問合せ Model
     * @param error Error
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @return ゲスト問合せ詳細画面
     */
    @PostMapping(value = "/", params = "doOnceInquiryUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "guest/inquiry/detail")
    public String doOnceInquiryUpdate(@Validated GuestInquiryModel guestInquiryModel,
                                      BindingResult error,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (error.hasErrors()) {
            return "guest/inquiry/detail";
        }

        String viewName = super.doOnceInquiryUpdate(guestInquiryModel, error, redirectAttributes, model);

        if (error.hasErrors()) {
            return "guest/inquiry/detail";
        }

        if (error.hasErrors()) {
            return "guest/inquiry/detail";
        }

        if (viewName == null) {
            viewName = "redirect:/guest/inquiry/?icd=" + guestInquiryModel.getIcd();
        }

        return viewName;
    }

    /**
     * 問合せ情報の会員情報についてチェックを行う
     * <pre>
     * ゲストの問合せかをチェック
     * </pre>
     *
     * @param dto 問い合わせ詳細Dto
     * @param redirectAttributes
     * @return true：上記チェックでエラーがある場合
     */
    @Override
    protected boolean checkInquiryMember(InquiryDetailsDto dto, RedirectAttributes redirectAttributes, Model model) {
        Integer memberInfoSeq = dto.getInquiryEntity().getMemberInfoSeq();
        if (!(memberInfoSeq == null || memberInfoSeq == 0)) {
            addMessage(MSGCD_MEMBER_INQUIRY, redirectAttributes, model);
            return true;
        }
        return false;
    }

    /**
     * 問合せ認証済かをチェック
     *
     * @param inquiryCode 注文番号
     * @return true：認証済
     */
    @Override
    protected boolean isAttested(String inquiryCode) {
        return inquiryUtility.isAttested(inquiryCode);
    }

    /**
     * @return 戻り先ページ取得
     */
    @Override
    public String getBackpage() {
        return BACKPAGE_VIEW;
    }

    /**
     *
     * エラーメッセージ取得
     *
     * @return MSGCD_REFERER_FAIL
     */
    @Override
    public String getMsgcdRefererFail() {
        return MSGCD_REFERER_FAIL;
    }

}
