package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup.registupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.inquirygroup.presentation.api.InquiryGroupApi;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRegistRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupUpdateRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 問い合わせ分類更新コントローラー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("inquiry/inquirygroup")
@Controller
@SessionAttributes(value = "inquiryGroupRegistUpdateModel")
@PreAuthorize("hasAnyAuthority('SHOP:8')")
public class InquiryGroupRegistUpdateController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryGroupRegistUpdateController.class);

    /**
     * 問い合わせ分類設定API
     */
    private final InquiryGroupApi inquiryGroupApi;

    /**
     * メッセージコード：不正操作
     */
    protected static final String MSGCD_ILLEGAL_OPERATION = "ASI000501";

    /**
     * 問い合わせ分類更新ヘルパー
     */
    private final InquiryGroupRegistUpdateHelper inquiryGroupRegistUpdateHelper;

    /**
     * コンストラクタ
     *
     * @param inquiryGroupApi                問い合わせ分類設定API
     * @param inquiryGroupRegistUpdateHelper 問い合わせ分類更新ヘルパー
     */
    public InquiryGroupRegistUpdateController(InquiryGroupApi inquiryGroupApi,
                                              InquiryGroupRegistUpdateHelper inquiryGroupRegistUpdateHelper) {
        this.inquiryGroupApi = inquiryGroupApi;
        this.inquiryGroupRegistUpdateHelper = inquiryGroupRegistUpdateHelper;
    }

    /**
     * 初期処理
     *
     * @return 自画面
     */
    @GetMapping(value = "/registupdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/inquirygroup/registupdate/index")
    protected String doLoadIndex(@RequestParam(required = false) Optional<Integer> seq,
                                 InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel,
                                 BindingResult error,
                                 Model model) {

        initDropDownValue(inquiryGroupRegistUpdateModel);
        // 複数タブ無効化Helper取得
        InquiryGroupEntity inquiryGroupEntity = null;

        // 確認画面からの遷移でなければ
        if (!inquiryGroupRegistUpdateModel.isFromConfirm()) {

            clearModel(InquiryGroupRegistUpdateModel.class, inquiryGroupRegistUpdateModel, model);
            initDropDownValue(inquiryGroupRegistUpdateModel);

            // パラメータの問い合わせ分類SEQを取得
            if (seq.isPresent()) {
                // 指定時、対象データ取得
                try {
                    InquiryGroupResponse inquiryGroupResponse = inquiryGroupApi.getByInquiryGroupSeq(seq.get());
                    inquiryGroupEntity = inquiryGroupRegistUpdateHelper.toInquiryGroupEntityFromInquiryGroupResponse(
                                    inquiryGroupResponse);

                } catch (HttpServerErrorException | HttpClientErrorException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // 取得失敗時はエラー画面へ
                    Map<String, String> itemNameAdjust = new HashMap<>();
                    handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                }
                if (error.hasErrors()) {
                    return "inquiry/inquirygroup/registupdate/index";
                }
                // 変更前情報
                inquiryGroupRegistUpdateModel.setOriginalInquiryGroupEntity(CopyUtil.deepCopy(inquiryGroupEntity));
            } else {
                // 指定がない場合、新規登録として処理
                inquiryGroupEntity = ApplicationContextUtility.getBean(InquiryGroupEntity.class);
            }
        }
        // ページ反映
        inquiryGroupRegistUpdateHelper.toPageForLoad(inquiryGroupRegistUpdateModel, inquiryGroupEntity);

        inquiryGroupRegistUpdateModel.setFromConfirm(false);

        return "inquiry/inquirygroup/registupdate/index";
    }

    /**
     * 確認画面へ遷移
     *
     * @return 確認画面
     */
    @PostMapping(value = "/registupdate", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/inquirygroup/registupdate/index")
    public String doConfirm(@Validated InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel,
                            BindingResult error,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        checkInquiryGroupSeq(inquiryGroupRegistUpdateModel, redirectAttributes, model);

        if (error.hasErrors()) {
            return "inquiry/inquirygroup/registupdate/index";
        }
        // 登録内容チェック
        inquiryGroupRegistUpdateHelper.toInquiryGroupEntityForInquiryGroupRegist(inquiryGroupRegistUpdateModel);

        return "redirect:/inquiry/inquirygroup/registupdate/confirm";
    }

    /**
     * 問い合わせ分類情報整合性チェック
     * チェックメソッド
     *
     * @return エラーの場合のみエラーページを返す
     */
    public String checkInquiryGroupSeq(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        // 問い合わせ分類SEQが取得できない場合は、データ不整合とみなしエラーとする
        if (!inquiryGroupRegistUpdateModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/inquiry/inquirygroup/";
        }

        return "inquiry/inquirygroup/registupdate/index";
    }

    /**
     * 初期処理
     *
     * @return 自画面 or エラー画面
     */
    @GetMapping(value = "/registupdate/confirm")
    protected String doLoadConfirm(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel) {

        // ブラウザバックの場合、処理しない
        if (inquiryGroupRegistUpdateModel.getInquiryGroupEntity() == null) {
            return "redirect:/error";
        }

        // 整合性チェック
        if (this.hasErrorInput(inquiryGroupRegistUpdateModel)) {
            return "redirect:/error";
        }

        // 入力値からエンティティを作成（変更後データ）
        InquiryGroupEntity modifiedInquiryGroupEntity = inquiryGroupRegistUpdateModel.getInquiryGroupEntity();

        // 変更前データと変更後データの差異リスト作成
        if (inquiryGroupRegistUpdateModel.getInquiryGroupSeq() != null) {
            List<String> modifiedList = DiffUtil.diff(inquiryGroupRegistUpdateModel.getOriginalInquiryGroupEntity(),
                                                      modifiedInquiryGroupEntity
                                                     );
            inquiryGroupRegistUpdateModel.setModifiedList(modifiedList);
        } else {
            inquiryGroupRegistUpdateModel.setModifiedList(null);
        }
        return "inquiry/inquirygroup/registupdate/confirm";
    }

    /**
     * キャンセル
     *
     * @return 問い合わせ分類登録更新画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doCancel")
    public String doCancel(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel) {
        inquiryGroupRegistUpdateModel.setFromConfirm(true);

        return "redirect:/inquiry/inquirygroup/registupdate";
    }

    /**
     * 問い合わせ分類登録/更新
     * 正常終了後は問い合わせ分類一覧画面へ遷移
     *
     * @return 問い合わせ分類一覧画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doOnceInquiryGroupRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/inquirygroup/registupdate/confirm")
    public String doOnceInquiryGroupRegist(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel,
                                           BindingResult error,
                                           SessionStatus sessionStatus) {

        // 整合性チェック
        if (this.hasErrorInput(inquiryGroupRegistUpdateModel)) {
            return "redirect:/error";
        }

        // 画面内容反映
        InquiryGroupEntity inquiryGroupEntity = inquiryGroupRegistUpdateModel.getInquiryGroupEntity();
        Integer inquiryGroupSeq = inquiryGroupEntity.getInquiryGroupSeq();
        try {
            if (inquiryGroupSeq == null) {
                // SEQ未設定時
                InquiryGroupRegistRequest inquiryGroupRegistRequest =
                                inquiryGroupRegistUpdateHelper.toInquiryGroupRegistRequestFromInquiryGroupEntity(
                                                inquiryGroupEntity);
                inquiryGroupApi.regist(inquiryGroupRegistRequest);
            } else {
                // SEQ指定時
                InquiryGroupUpdateRequest inquiryGroupUpdateRequest =
                                inquiryGroupRegistUpdateHelper.toInquiryGroupUpdateRequestFromInquiryGroupEntity(
                                                inquiryGroupEntity);
                inquiryGroupApi.update(inquiryGroupSeq, inquiryGroupUpdateRequest);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "inquiry/inquirygroup/registupdate/confirm";
        }
        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/inquiry/inquirygroup";
    }

    /**
     * 必須入力項目が存在するかチェック
     *
     * @return エラーがあった場合:true // 正常な場合:false
     */
    protected boolean hasErrorInput(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel) {

        if (inquiryGroupRegistUpdateModel.getInquiryGroupEntity() == null) {
            // 登録・更新問わずエンティティはセットされてるはず
            return true;

        } else if (inquiryGroupRegistUpdateModel.getInquiryGroupName() == null
                   || inquiryGroupRegistUpdateModel.getOpenStatus() == null) {
            // 入力必須項目
            return true;
        }

        return false;
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param inquiryGroupRegistUpdateModel フリーエリア登録・更新画面
     */
    private void initDropDownValue(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel) {
        inquiryGroupRegistUpdateModel.setOpenStatusItems(EnumTypeUtil.getEnumMap(HTypeOpenStatus.class));
    }
}