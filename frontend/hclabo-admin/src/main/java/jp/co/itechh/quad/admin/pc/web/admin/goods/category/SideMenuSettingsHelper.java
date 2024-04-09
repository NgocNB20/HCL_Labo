package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.menu.presentation.api.param.MenuResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * サイドメニュー設定ヘルパー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class SideMenuSettingsHelper {

    /** サイドメニュー設定の登録デフォルト種別 */
    public static final String SIDE_MENU_REGIST_TYPE_DEFAULT = "0";

    /**
     * カテゴリーツリーDTOリスト設定
     *
     * @param sideMenuSettingsModel サイドメニュー設定モデル
     * @param menuResponse メニューレスポンス
     * @throws JsonProcessingException e
     */
    public void setCategoryTreeDtoList(SideMenuSettingsModel sideMenuSettingsModel, MenuResponse menuResponse)
                    throws JsonProcessingException {
        String jsonData = menuResponse.getCategoryTreeJson();
        ObjectMapper objectMapper =
                        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CategoryTreeDto categoryTreeDto;
        categoryTreeDto = objectMapper.readValue(jsonData, CategoryTreeDto.class);

        if (ObjectUtils.isNotEmpty(categoryTreeDto) && ObjectUtils.isNotEmpty(
                        categoryTreeDto.getCategoryTreeDtoList())) {
            sideMenuSettingsModel.setCategoryTreeDtoList(categoryTreeDto.getCategoryTreeDtoList());
        }
        sideMenuSettingsModel.setSideMenuSettingsList(jsonData);
    }

    /**
     * カテゴリーアイテムリスト取得
     *
     * @param registType 登録種別
     * @param categoryListResponse カテゴリーリストレスポンス
     * @param categoryTreeDtoList カテゴリーツリーDtoリスト
     * @return カテゴリーアイテムリスト
     */
    public List<SideMenuSettingsCategoryItem> getCategoryItemList(String registType,
                                                                  CategoryListResponse categoryListResponse,
                                                                  List<CategoryTreeDto> categoryTreeDtoList) {

        // 登録種別が一意登録の場合は、カテゴリーIdリスト取得
        List<String> categoryIdList = new ArrayList<>();
        if (!registType.equals(SIDE_MENU_REGIST_TYPE_DEFAULT)) {
            getListCategoryId(categoryTreeDtoList, categoryIdList);
        }

        List<SideMenuSettingsCategoryItem> sideMenuSettingsCategoryItemList = new ArrayList<>();
        if (categoryListResponse.getCategoryList() == null) {
            return sideMenuSettingsCategoryItemList;
        }

        for (CategoryResponse categoryResponse : Objects.requireNonNull(categoryListResponse.getCategoryList())) {
            // 登録種別が一意登録の場合は、categoryTreeDtoに存在しているカテゴリーIdをカテゴリーアイテムリストに追加しない
            if (!registType.equals(SIDE_MENU_REGIST_TYPE_DEFAULT) && categoryIdList.contains(
                            categoryResponse.getCategoryId())) {
                continue;
            }
            // カテゴリーアイテムリスト追加
            SideMenuSettingsCategoryItem sideMenuSettingsCategoryItem = new SideMenuSettingsCategoryItem();
            sideMenuSettingsCategoryItem.setCategoryId(categoryResponse.getCategoryId());
            sideMenuSettingsCategoryItem.setCategoryName(categoryResponse.getCategoryName());
            sideMenuSettingsCategoryItem.setDisplayName(categoryResponse.getCategoryName());
            sideMenuSettingsCategoryItemList.add(sideMenuSettingsCategoryItem);
        }
        return sideMenuSettingsCategoryItemList;
    }

    /**
     * カテゴリーIdリスト取得
     *
     * @param categoryTreeDtoList カテゴリー木構造Dtoリスト
     * @param categoryIdList カテゴリーIdリスト
     */
    private void getListCategoryId(List<CategoryTreeDto> categoryTreeDtoList, List<String> categoryIdList) {
        if (ObjectUtils.isEmpty(categoryTreeDtoList)) {
            return;
        }
        for (CategoryTreeDto categoryTreeDto : categoryTreeDtoList) {
            categoryIdList.add(categoryTreeDto.getCategoryId());
            if (ObjectUtils.isNotEmpty(categoryTreeDto.getCategoryTreeDtoList())) {
                getListCategoryId(categoryTreeDto.getCategoryTreeDtoList(), categoryIdList);
            }
        }
    }
}