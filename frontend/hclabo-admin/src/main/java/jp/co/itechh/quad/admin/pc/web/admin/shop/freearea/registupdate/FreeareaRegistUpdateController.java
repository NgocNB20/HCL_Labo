/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.registupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaRegistRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaUpdateRequest;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * フリーエリア登録・更新画面
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@SessionAttributes(value = "freeareaRegistUpdateModel")
@RequestMapping("/freearea")
@Controller
@PreAuthorize("hasAnyAuthority('SHOP:8')")
public class FreeareaRegistUpdateController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeareaRegistUpdateController.class);

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * メッセージコード：不正操作
     */
    private static final String MSGCD_ILLEGAL_OPERATION = "ASF000202";

    /**
     * メッセージコード：更新中データ削除
     */
    private static final String MSGCD_DATA_NOT_EXIST = "ASF000203";

    /**
     * 定数データが存在しないプロパティ
     */
    public static final String DATA_NOT_EXIST = "dataNotExist";

    /**
     * フリーエリアAPI
     */
    private final FreeareaApi freeareaApi;

    /**
     * フリーエリア登録・更新画面のHelper
     */
    private final FreeareaRegistUpdateHelper freeareaRegistUpdateHelper;

    /**
     * コンストラクタ
     *
     * @param freeareaApi
     * @param freeareaRegistUpdateHelper
     */
    @Autowired
    public FreeareaRegistUpdateController(FreeareaApi freeareaApi,
                                          FreeareaRegistUpdateHelper freeareaRegistUpdateHelper) {
        this.freeareaApi = freeareaApi;
        this.freeareaRegistUpdateHelper = freeareaRegistUpdateHelper;
    }

    /**
     * 初期処理
     *
     * @return 自画面
     */
    @GetMapping(value = "/registupdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/registupdate/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<Integer> freeAreaSeq,
                              FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // プルダウンアイテム情報を取得
        initComponentValue(freeareaRegistUpdateModel);

        FreeAreaEntity freeAreaEntity = null;

        // 確認画面からの遷移でなければ
        if (!freeareaRegistUpdateModel.isFromConfirm()) {
            // モデルのクリア処理
            clearModel(FreeareaRegistUpdateModel.class, freeareaRegistUpdateModel, model);
            initComponentValue(freeareaRegistUpdateModel);

            // 指定時
            if (freeAreaSeq.isPresent()) {

                // 更新の場合再検索フラグをセット
                try {
                    // フリーエリアエンティティに変換
                    FreeAreaResponse freeAreaResponse = freeareaApi.getByFreeAreaSeq(freeAreaSeq.get());
                    freeAreaEntity = freeareaRegistUpdateHelper.toFreeAreaEntity(freeAreaResponse);
                } catch (HttpServerErrorException | HttpClientErrorException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // 取得失敗時、エラー画面へ遷移
                    Map<String, String> itemNameAdjust = new HashMap<>();
                    handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                }

                if (error.hasErrors()) {
                    return "freearea/registupdate/index";
                }
                // 変更前情報
                freeareaRegistUpdateModel.setOriginalFreeAreaEntity(CopyUtil.deepCopy(freeAreaEntity));
            } else {
                freeAreaEntity = ApplicationContextUtility.getBean(FreeAreaEntity.class);
                freeareaRegistUpdateModel.setOriginalFreeAreaEntity(null);
            }
            freeareaRegistUpdateModel.setViewableMemberList(null);
        }

        // ページ反映
        freeareaRegistUpdateHelper.toPageForLoad(freeareaRegistUpdateModel, freeAreaEntity);

        // 修正画面の場合、画面用フリーエリアSEQを設定
        if (freeareaRegistUpdateModel.getFreeAreaEntity() != null
            && freeareaRegistUpdateModel.getFreeAreaEntity().getFreeAreaSeq() != null) {
            freeareaRegistUpdateModel.setScFreeAreaSeq(freeareaRegistUpdateModel.getFreeAreaEntity().getFreeAreaSeq());
        }

        // フラグリセット
        freeareaRegistUpdateModel.setFromConfirm(false);
        // フリーエリアデータ存在チェック
        String check = preDoAction(freeareaRegistUpdateModel, error, redirectAttributes, model, false);
        if (error.hasErrors()) {
            return "freearea/registupdate/index";
        }

        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        return "freearea/registupdate/index";
    }

    /**
     * 確認画面へ遷移
     *
     * @return 確認画面
     */
    @PostMapping(value = "/registupdate", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/registupdate/index")
    public String doConfirm(@Validated FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // フリーエリアデータ存在チェック
        String check = preDoAction(freeareaRegistUpdateModel, error, redirectAttributes, model, false);
        if (error.hasErrors()) {
            return "freearea/registupdate/index";
        }

        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "freearea/registupdate/index";
        }

        // 不正操作チェック
        if (!freeareaRegistUpdateModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/freearea/";
        }

        freeareaRegistUpdateHelper.toPageForConfirm(freeareaRegistUpdateModel);

        return "redirect:/freearea/registupdate/confirm";
    }

    /**
     * 確認画面：初期処理
     *
     * @param freeareaRegistUpdateModel フリーエリア登録・更新画面
     * @param model                     Model
     * @return 自画面(エラー時 、 検索画面)
     */
    @GetMapping(value = "/registupdate/confirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/registupdate/confirm")
    public String doLoadConfirm(FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                                BindingResult error,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // フリーエリアデータ存在チェック
        String check = preDoAction(freeareaRegistUpdateModel, error, redirectAttributes, model, true);
        if (error.hasErrors()) {
            return "freearea/registupdate/confirm";
        }
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (this.hasErrorInput(freeareaRegistUpdateModel)) {
            // 入力値の整合性が取れない場合、検索画面へ
            return "redirect:/freearea/";
        }
        // 修正の場合、画面用フリーエリアSEQを設定
        if (freeareaRegistUpdateModel.getFreeAreaEntity().getFreeAreaSeq() != null) {
            freeareaRegistUpdateModel.setScFreeAreaSeq(freeareaRegistUpdateModel.getFreeAreaEntity().getFreeAreaSeq());

            // 入力値からエンティティを作成（変更後データ）
            FreeAreaEntity modifiedFreeAreaEntity =
                            freeareaRegistUpdateHelper.toFreeAreaEntityForNewsRegist(freeareaRegistUpdateModel);

            // 変更前データと変更後データの差異リスト作成
            List<String> modifiedList = DiffUtil.diff(freeareaRegistUpdateModel.getOriginalFreeAreaEntity(),
                                                      modifiedFreeAreaEntity
                                                     );
            freeareaRegistUpdateModel.setModifiedList(modifiedList);
        }
        return "freearea/registupdate/confirm";
    }

    /**
     * フリーエリア登録更新処理
     * 正常終了後はフリーエリア検索画面へ遷移
     *
     * @return フリーエリア検索画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doOnceFreeAreaRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/registupdate/confirm")
    public String doOnceFreeAreaRegist(FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                                       BindingResult error,
                                       RedirectAttributes redirectAttributes,
                                       SessionStatus sessionStatus,
                                       Model model) {
        // フリーエリアデータ存在チェック
        String check = preDoAction(freeareaRegistUpdateModel, error, redirectAttributes, model, true);
        if (error.hasErrors()) {
            return "freearea/registupdate/confirm";
        }
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (this.hasErrorInput(freeareaRegistUpdateModel)) {
            // 入力値の整合性が取れない場合、検索画面へ
            return "redirect:/freearea/";
        }
        try {
            // フリーエリアSEQを保持していない場合
            if (freeareaRegistUpdateModel.getFreeAreaSeq() == null) {
                FreeAreaRegistRequest freeAreaRegistRequest =
                                freeareaRegistUpdateHelper.toFreeAreaRequestRegist(freeareaRegistUpdateModel);
                // 登録処理
                freeareaApi.regist(freeAreaRegistRequest);
            } else {
                FreeAreaUpdateRequest freeAreaUpdateRequest =
                                freeareaRegistUpdateHelper.toFreeAreaRequestUpdate(freeareaRegistUpdateModel);
                // 更新処理
                freeareaApi.update(freeareaRegistUpdateModel.getFreeAreaSeq(), freeAreaUpdateRequest);

                // 再検索フラグをセット
                redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "freearea/registupdate/confirm";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/freearea/";
    }

    /**
     * 登録更新画面へ遷移
     *
     * @return 登録更新画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doCancel")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/registupdate/confirm")
    public String doCancel(FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        // フリーエリアデータ存在チェック
        String check = preDoAction(freeareaRegistUpdateModel, error, redirectAttributes, model, true);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        freeareaRegistUpdateModel.setFromConfirm(true);

        return "redirect:/freearea/registupdate";
    }

    /**
     * アクション実行前処理
     *
     * @param freeareaRegistUpdateModel フリーエリア登録・更新画面Model
     * @param redirectAttributes        RedirectAttributes
     * @param model                     Model
     * @return チェック結果（チェックNGの場合は、遷移先画面Viewを返却。チェックOK時はNULL返却）
     */
    public String preDoAction(FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model,
                              Boolean isConfirm) {
        String returnView = null;
        // 不正操作チェック
        returnView = checkIllegalOperation(freeareaRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(returnView)) {
            return returnView;
        }

        // フリーエリアデータ存在チェック
        returnView = checkFreeAreaDataExist(freeareaRegistUpdateModel, error, redirectAttributes, model, isConfirm);
        if (StringUtils.isNotEmpty(returnView)) {
            return returnView;
        }

        // チェックＯＫ（null返却）
        return null;
    }

    /**
     * フリーエリアデータ存在チェック<br />
     * フリーエリア更新時に該当フリーエリアが削除されていた場合、エラーメッセージを登録する
     *
     * @param freeareaRegistUpdateModel フリーエリア登録・更新画面Model
     * @param redirectAttributes        RedirectAttributes
     * @param model                     Model
     * @return チェック結果（チェックNGの場合は、遷移先画面Viewを返却。チェックOK時はNULL返却）
     */
    protected String checkFreeAreaDataExist(FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                                            BindingResult error,
                                            RedirectAttributes redirectAttributes,
                                            Model model,
                                            Boolean isConfirm) {
        // フリーエリアSEQ取得
        // ※FreeAreaEntityにインスタンスが設定されている場合のみ
        Integer freeAreaSeq = null;
        if (freeareaRegistUpdateModel.getFreeAreaEntity() != null) {
            freeAreaSeq = freeareaRegistUpdateModel.getFreeAreaEntity().getFreeAreaSeq();
        }
        if (freeAreaSeq == null) {
            // 新規登録処理の場合（フリーエリアSEQを保持していない場合）、チェック終了
            return null;
        }
        try {
            // 編集中フリーエリア取得
            FreeAreaResponse freeAreaResponse = freeareaApi.getByFreeAreaSeq(freeAreaSeq);
            if (freeAreaResponse == null) {
                // 編集中フリーエリアが削除されている場合、エラー
                String appComplementUrl = PropertiesUtil.getSystemPropertiesValue("app.complement.url");
                addMessage(MSGCD_DATA_NOT_EXIST, new Object[] {appComplementUrl}, redirectAttributes, model);
                model.addAttribute(DATA_NOT_EXIST, true);
                if (isConfirm) {
                    return "freearea/registupdate/confirm";
                }
                return "freearea/registupdate/index";
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return null;
    }

    /**
     * 不正操作チェック
     *
     * @param freeareaRegistUpdateModel フリーエリア登録・更新画面Model
     * @param redirectAttributes        RedirectAttributes
     * @param model                     Model
     * @return チェック結果（チェックNGの場合は、遷移先画面Viewを返却。チェックOK時はNULL返却）
     */
    protected String checkIllegalOperation(FreeareaRegistUpdateModel freeareaRegistUpdateModel,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        Integer scFreeAreaSeq = freeareaRegistUpdateModel.getScFreeAreaSeq();
        Integer dbFreeAreaSeq = null;
        if (freeareaRegistUpdateModel.getFreeAreaEntity() != null) {
            dbFreeAreaSeq = freeareaRegistUpdateModel.getFreeAreaEntity().getFreeAreaSeq();
        }

        boolean isError = false;

        if (scFreeAreaSeq == null && dbFreeAreaSeq != null) {
            // 登録画面にも関わらず、フリーエリアSEQのDB情報を保持している場合エラー
            isError = true;

        } else if (scFreeAreaSeq != null && dbFreeAreaSeq == null) {
            // 修正画面にも関わらず、フリーエリアSEQのDB情報を保持していない場合エラー
            isError = true;

        } else if (scFreeAreaSeq != null && !scFreeAreaSeq.equals(dbFreeAreaSeq)) {
            // 画面用フリーエリアSEQとDB用フリーエリアSEQが異なる場合エラー
            isError = true;
        }

        if (isError) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/freearea/";
        }
        // エラーがない場合はnull返却
        return null;
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param freeareaRegistUpdateModel フリーエリア登録・更新画面
     */
    private void initComponentValue(FreeareaRegistUpdateModel freeareaRegistUpdateModel) {
        freeareaRegistUpdateModel.setSiteMapFlagItems(EnumTypeUtil.getEnumMap(HTypeSiteMapFlag.class));
    }

    /**
     * 必須保持値・入力項目の有無で整合性をチェックする
     *
     * @return true:エラーあり false:エラーなし
     */
    private boolean hasErrorInput(FreeareaRegistUpdateModel freeareaRegistUpdateModel) {

        FreeAreaEntity freeAreaEntity = freeareaRegistUpdateModel.getFreeAreaEntity();
        if (freeAreaEntity == null) {
            return true;
        }

        if (freeareaRegistUpdateModel.getFreeAreaKey() == null || freeareaRegistUpdateModel.getOpenStartDate() == null
            || freeareaRegistUpdateModel.getOpenStartTime() == null) {
            return true;
        }

        return false;

    }

}