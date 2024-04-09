package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty.registupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import jp.co.itechh.quad.admin.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.CheckGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.RegistUpdateGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.icon.presentation.api.IconApi;
import jp.co.itechh.quad.icon.presentation.api.param.IconListResponse;
import jp.co.itechh.quad.icon.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.novelty.presentation.api.LogisticNoveltyApi;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionDeleteRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionExclusionsListGetRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionRegistRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionUpdateRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentGoodsCodeGetListResponse;
import jp.co.itechh.quad.productnovelty.presentation.api.ProductNoveltyApi;
import jp.co.itechh.quad.productnovelty.presentation.api.param.NoveltyPresentConditionTargetGoodsCountGetRequest;
import jp.co.itechh.quad.productnovelty.presentation.api.param.NoveltyPresentConditionTargetGoodsCountResponse;
import org.apache.commons.lang3.ObjectUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/novelty/registupdate")
@Controller
@SessionAttributes(value = "noveltyRegistUpdateModel")
@PreAuthorize("hasAnyAuthority('SETTING:4')")
public class NoveltyRegistUpdateController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoveltyRegistUpdateController.class);

    /** 物流ノベルティAPI */
    private final LogisticNoveltyApi logisticNoveltyApi;

    /** アイコン一API */
    private final IconApi iconApi;

    /** ノベルティ登録更新Helper */
    private final NoveltyRegistUpdateHelper noveltyRegistUpdateHelper;

    /** 日付関連Helper取得 */
    public DateUtility dateUtility;

    /**
     * 商品ノベルティAPI
     */
    private final ProductNoveltyApi productNoveltyApi;

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 変換ユーティリティクラス
     */
    public NoveltyRegistUpdateController(LogisticNoveltyApi logisticNoveltyApi,
                                         IconApi iconApi,
                                         NoveltyRegistUpdateHelper noveltyRegistUpdateHelper,
                                         ProductNoveltyApi productNoveltyApi) {
        this.logisticNoveltyApi = logisticNoveltyApi;
        this.iconApi = iconApi;
        this.noveltyRegistUpdateHelper = noveltyRegistUpdateHelper;
        this.productNoveltyApi = productNoveltyApi;
    }

    /**
     * 初期表示
     *
     * @param md                         認証画面からの返却値
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @param noveltyRegistUpdateModel   ノベルティプレゼント条件登録/更新画面
     * @param error                      エラーメッセージ
     * @param model                      モデル
     * @return ノベルティプレゼント検索画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "novelty/registupdate/index")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                                 @RequestParam(required = false) Optional<String> noveltyPresentConditionSeq,
                                 NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                                 BindingResult error,
                                 Model model) {
        clearModel(NoveltyRegistUpdateModel.class, noveltyRegistUpdateModel, model);

        if (noveltyPresentConditionSeq.isPresent()) {
            noveltyRegistUpdateModel.setNoveltyPresentConditionSeq(Integer.parseInt(noveltyPresentConditionSeq.get()));
        }
        Integer seq = noveltyRegistUpdateModel.getNoveltyPresentConditionSeq();
        List<String> enclosureGoodsCodeList = new ArrayList<>();

        NoveltyPresentConditionResponse noveltyPresentConditionResponse = null;
        if (seq != null) {

            noveltyPresentConditionResponse = logisticNoveltyApi.getByNoveltyPresentConditionSeq(seq);

            NoveltyPresentGoodsCodeGetListResponse noveltyPresentGoodsCodeGetListResponse =
                            logisticNoveltyApi.getGoodsSeqListByConditionSeq(seq);

            enclosureGoodsCodeList = noveltyPresentGoodsCodeGetListResponse.getGoodsCodeList();
        }

        noveltyRegistUpdateModel.setNoveltyPresentStateItems(EnumTypeUtil.getEnumMap(HTypeNoveltyPresentState.class));

        // 除外条件データ取得
        NoveltyPresentConditionExclusionsListGetRequest noveltyPresentConditionExclusionsListGetRequest =
                        new NoveltyPresentConditionExclusionsListGetRequest();
        noveltyPresentConditionExclusionsListGetRequest.setNoveltyPresentConditionSeq(seq);

        NoveltyPresentConditionListResponse noveltyPresentConditionListResponse =
                        logisticNoveltyApi.getExclusions(noveltyPresentConditionExclusionsListGetRequest);

        // アイコン情報取得
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        IconListResponse iconListResponse = iconApi.getList(pageInfoRequest);

        NoveltyPresentConditionEntity noveltyPresentConditionEntity =
                        noveltyRegistUpdateHelper.toNoveltyPresentConditionEntity(noveltyPresentConditionResponse);

        // ページに反映
        noveltyRegistUpdateHelper.toPageForLoad(noveltyRegistUpdateModel, noveltyPresentConditionEntity,
                                                enclosureGoodsCodeList, noveltyPresentConditionListResponse,
                                                iconListResponse.getIconList()
                                               );

        noveltyRegistUpdateModel.setGoodsCountFlag(false);
        noveltyRegistUpdateModel.setMd("list");

        // 画面初期描画時のデフォルト値を設定
        noveltyRegistUpdateHelper.setDefaultValueForLoad(noveltyRegistUpdateModel);

        return "novelty/registupdate/index";
    }

    /**
     * 登録処理
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @param error                    エラーメッセージ
     * @param redirectAttributes       リダイレクトアトリビュート
     * @param sessionStatus            セクションステータス
     * @param model                    モデル
     * @return ノベルティプレゼント検索画面
     */
    @PostMapping(value = "/", params = "doOnceRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "novelty/registupdate/index")
    protected String doOnceRegist(@Validated(RegistUpdateGroup.class) NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  SessionStatus sessionStatus,
                                  Model model) {

        noveltyRegistUpdateModel.setGoodsCountFlag(false);
        try {
            if (error.hasErrors()) {
                return "novelty/registupdate/index";
            }

            Integer seq = noveltyRegistUpdateModel.getNoveltyPresentConditionSeq();

            noveltyRegistUpdateHelper.setIconChecked(noveltyRegistUpdateModel);

            if (seq != null) {
                NoveltyPresentConditionUpdateRequest noveltyPresentConditionUpdateRequest =
                                noveltyRegistUpdateHelper.toNoveltyPresentConditionUpdateRequest(
                                                noveltyRegistUpdateModel);

                logisticNoveltyApi.update(noveltyPresentConditionUpdateRequest);

                // 再検索フラグをセット
                redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
            } else {
                NoveltyPresentConditionRegistRequest noveltyPresentConditionRegistRequest =
                                noveltyRegistUpdateHelper.toNoveltyPresentConditionRegistRequest(
                                                noveltyRegistUpdateModel);

                logisticNoveltyApi.regist(noveltyPresentConditionRegistRequest);
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/novelty/";
    }

    /**
     * 削除処理
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @param error                    エラーメッセージ
     * @param redirectAttributes       リダイレクトアトリビュート
     * @param sessionStatus            セクションステータス
     * @param model                    モデル
     * @return ノベルティプレゼント検索画面
     */
    @PostMapping(value = "/", params = "doOnceDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "novelty/registupdate/index")
    protected String doOnceDelete(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  SessionStatus sessionStatus,
                                  Model model) {
        // Modelをセッションより破棄
        sessionStatus.setComplete();

        try {
            NoveltyPresentConditionDeleteRequest noveltyPresentConditionDeleteRequest =
                            new NoveltyPresentConditionDeleteRequest();
            noveltyPresentConditionDeleteRequest.setNoveltyPresentConditionSeq(
                            noveltyRegistUpdateModel.getNoveltyPresentConditionSeq());
            logisticNoveltyApi.delete(noveltyPresentConditionDeleteRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
        return "redirect:/novelty/";
    }

    /**
     * 商品数確認処理
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @param error                    エラーメッセージ
     * @param redirectAttributes       リダイレクトアトリビュート
     * @param sessionStatus            セクションステータス
     * @param model                    モデル
     * @return ノベルティプレゼント検索画面
     */
    @PostMapping(value = "/", params = "doCheckGoods")
    protected String doCheckGoods(@Validated(CheckGoodsGroup.class) NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  SessionStatus sessionStatus,
                                  Model model) {
        //商品数確認フラグ初期値
        noveltyRegistUpdateModel.setGoodsCountFlag(false);

        if (error.hasErrors()) {
            return "novelty/registupdate/index";
        }
        noveltyRegistUpdateHelper.setIconChecked(noveltyRegistUpdateModel);
        NoveltyPresentConditionTargetGoodsCountGetRequest noveltyPresentConditionTargetGoodsCountGetRequest =
                        noveltyRegistUpdateHelper.toNoveltyPresentConditionTargetGoodsCountGetRequest(
                                        noveltyRegistUpdateModel);

        NoveltyPresentConditionTargetGoodsCountResponse noveltyPresentConditionTargetGoodsCountResponse =
                        productNoveltyApi.getTargetGoodsCount(noveltyPresentConditionTargetGoodsCountGetRequest);

        if (ObjectUtils.isNotEmpty(noveltyPresentConditionTargetGoodsCountResponse)) {
            noveltyRegistUpdateModel.setGoodsCountFlag(true);
            noveltyRegistUpdateModel.setGoodsCount(noveltyPresentConditionTargetGoodsCountResponse.getCount());
        }

        return "novelty/registupdate/index";
    }
}