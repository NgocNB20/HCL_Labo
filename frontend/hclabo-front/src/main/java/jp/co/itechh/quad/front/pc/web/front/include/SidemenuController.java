package jp.co.itechh.quad.front.pc.web.front.include;

import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeResponse;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.front.web.AbstractController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 共通サイドメニューAjax コントローラー
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
@RequestMapping("/")
public class SidemenuController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SidemenuController.class);

    /** カテゴリAPI */
    private final CategoryApi categoryApi;

    /** 共通サイドメニューAjax Helper */
    private final SidemenuHelper sidemenuHelper;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param categoryApi    カテゴリAPI
     * @param sidemenuHelper 共通サイドメニューAjax Helper
     * @param dateUtility
     */
    @Autowired
    public SidemenuController(CategoryApi categoryApi, SidemenuHelper sidemenuHelper, DateUtility dateUtility) {
        this.categoryApi = categoryApi;
        this.sidemenuHelper = sidemenuHelper;
        this.dateUtility = dateUtility;
    }

    /**
     * 全カテゴリ情報の取得(Ajax)
     *
     * @param viewLevel
     * @param cid 現在の表示カテゴリID
     * @param preTime   プレビュー日時（yyyyMMddHHmmss）
     * @return List<SidemenuModelItem>
     */
    @GetMapping("getCategoryJsonData")
    @ResponseBody
    public List<SidemenuModelItem> getMultipleCategoryData(@RequestParam(required = false) String viewLevel,
                                                           @RequestParam(required = false) String cid,
                                                           @RequestParam(required = false) String preTime) {

        List<SidemenuModelItem> sidemenuModelItems = new ArrayList<>();

        if (StringUtils.isEmpty(viewLevel)) {
            viewLevel = PropertiesUtil.getSystemPropertiesValue("sidemenu.category.view.level");
        }
        Date targetTime = null;
        if (StringUtils.isNotEmpty(preTime)) {
            targetTime = this.dateUtility.toTimestampValue(preTime, this.dateUtility.YMD_HMS);
        }

        // 全カテゴリ情報の取得
        CategoryTreeDto categoryTreeDto = getCategoryDto(viewLevel, targetTime);
        if (categoryTreeDto != null) {
            sidemenuHelper.toDataForLoad(categoryTreeDto, sidemenuModelItems, cid);
        }
        return sidemenuModelItems;
    }

    /**
     * ルートカテゴリ一覧情報の取得
     *
     * @param viewLevel カテゴリー階層数
     * @param preTime   プレビュー日時
     * @return カテゴリー木構造Dtoクラス
     */
    protected CategoryTreeDto getCategoryDto(String viewLevel, Date preTime) {

        // 検索条件
        CategoryTreeGetRequest categoryTreeGetRequest = new CategoryTreeGetRequest();

        categoryTreeGetRequest.setMaxHierarchical(Integer.parseInt(viewLevel));
        categoryTreeGetRequest.setOpenStatus(HTypeOpenStatus.OPEN.getValue());
        categoryTreeGetRequest.setFrontDisplayReferenceDate(preTime);

        try {
            CategoryTreeResponse categoryTreeResponse = categoryApi.getTreeNodes(categoryTreeGetRequest);

            CategoryTreeDto categoryTreeDto = new CategoryTreeDto();
            sidemenuHelper.toCategoryTreeDto(categoryTreeDto, categoryTreeResponse);
            return categoryTreeDto;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            e.printStackTrace();
            return null;
        }
    }
}