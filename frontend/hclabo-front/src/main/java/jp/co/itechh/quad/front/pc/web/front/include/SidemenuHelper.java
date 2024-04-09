package jp.co.itechh.quad.front.pc.web.front.include;

import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeResponse;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.front.utility.CategoryUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 共通サイドメニューAjax Helper
 *
 * @author kaneda
 */
@Component
public class SidemenuHelper {

    /**
     * カテゴリ情報をサイドメニューModelアイテムクラスに変換<br />
     *
     * @param categoryTreeDto        カテゴリー木構造Dtoクラス
     * @param sidemenuModelItems 共通サイドメニューAjax Modelアイテム
     * @param currentCid         現在の表示カテゴリID
     */
    public void toDataForLoad(CategoryTreeDto categoryTreeDto,
                              List<SidemenuModelItem> sidemenuModelItems,
                              String currentCid) {

        String currentCategorySeqPath = null;

        // カテゴリSEQパス
        if (!StringUtils.isEmpty(currentCid)) {
            currentCategorySeqPath = getCurrentCategorySeqPath(categoryTreeDto, currentCid);
        }

        int index = 0;

        // サイドのカテゴリ一覧を設定(dummyカテゴリーが対象外)
        if (categoryTreeDto.getCategoryTreeDtoList() != null) {
            index++;
            for (int i = 0; i < categoryTreeDto.getCategoryTreeDtoList().size(); i++) {
                CategoryTreeDto childCategoryDto = categoryTreeDto.getCategoryTreeDtoList().get(i);
                sidemenuModelItems =
                                getCategoryList(childCategoryDto, sidemenuModelItems, currentCategorySeqPath, index,
                                                categoryTreeDto.getCategoryId()
                                               );
            }
        }
    }

    /**
     * カテゴリ一覧情報の作成<br/>
     *
     * @param categoryTreeDto            カテゴリー木構造Dtoクラス
     * @param categoryPageItemList   カテゴリページ情報リスト
     * @param currentCategorySeqPath 現在カテゴリSEQパス
     * @return カテゴリ情報一覧
     */
    protected List<SidemenuModelItem> getCategoryList(CategoryTreeDto categoryTreeDto,
                                                      List<SidemenuModelItem> categoryPageItemList,
                                                      String currentCategorySeqPath,
                                                      int index,
                                                      String cidParent) {

        SidemenuModelItem sideMenuModelItem = ApplicationContextUtility.getBean(SidemenuModelItem.class);
        // サイドメニューページアイテムへカテゴリ情報を設定する
        setDataSidemenuPageItem(categoryTreeDto, sideMenuModelItem, currentCategorySeqPath, index, cidParent);
        // カテゴリ情報一覧へサイドメニューページアイテムを設定する
        categoryPageItemList.add(sideMenuModelItem);

        // 子カテゴリ情報の設定
        if (categoryTreeDto.getCategoryTreeDtoList() != null) {
            int levelIndex = index + 1;
            for (CategoryTreeDto childCategoryDto : categoryTreeDto.getCategoryTreeDtoList()) {
                categoryPageItemList = getCategoryList(childCategoryDto, categoryPageItemList, currentCategorySeqPath,
                                                       levelIndex, categoryTreeDto.getCategoryId()
                                                      );
            }
        }
        return categoryPageItemList;
    }

    /**
     * サイドメニューページアイテムへカテゴリ情報を設定する<br/>
     *
     * @param categoryTreeDto            カテゴリー木構造Dtoクラス
     * @param sideMenuModelItem      サイドメニューページアイテム
     * @param currentCategorySeqPath 現在カテゴリSEQパス
     * @param index
     * @param cidParent カテゴリID
     * @param customParams           案件用引数
     */
    protected void setDataSidemenuPageItem(CategoryTreeDto categoryTreeDto,
                                           SidemenuModelItem sideMenuModelItem,
                                           String currentCategorySeqPath,
                                           int index,
                                           String cidParent,
                                           Object... customParams) {
        // カテゴリ情報設定
        sideMenuModelItem.setCid(categoryTreeDto.getCategoryId());
        sideMenuModelItem.setHsn(categoryTreeDto.getHierarchicalSerialNumber());
        sideMenuModelItem.setDisplayName(categoryTreeDto.getDisplayName());
        sideMenuModelItem.setHierarchicalSerialNumber(categoryTreeDto.getHierarchicalSerialNumber());
        sideMenuModelItem.setCidParent(cidParent);
        sideMenuModelItem.setCategoryLevel(index);
    }

    /**
     * カテゴリファイル名設定<br/>
     *
     * @param categoryId         カテゴリID
     * @param sideMenuModelItem      サイドメニューページアイテム
     */
    protected void setCategoryFileName(String categoryId, SidemenuModelItem sideMenuModelItem) {

        CategoryUtility categoryUtility = ApplicationContextUtility.getBean(CategoryUtility.class);
        // カテゴリ画像名を設定
        sideMenuModelItem.setCategoryFileName(categoryUtility.getLnavCategoryImageName(categoryId, false));
    }

    /**
     * 現在カテゴリSEQパスを取得<br/>
     *
     * @param categoryTreeDto       カテゴリー木構造Dto
     * @param currentCategoryId 現在カテゴリID
     * @return 現在カテゴリSEQパス
     */
    protected String getCurrentCategorySeqPath(CategoryTreeDto categoryTreeDto, String currentCategoryId) {
        if (!ObjectUtils.isEmpty(categoryTreeDto.getCategoryEntity()) && !ObjectUtils.isEmpty(
                        categoryTreeDto.getCategoryEntity().getCategoryId()) && categoryTreeDto.getCategoryEntity()
                                                                                               .getCategoryId()
                                                                                               .equals(currentCategoryId)) {
            return categoryTreeDto.getCategoryEntity().getCategorySeqPath();
        }

        if (!CollectionUtils.isEmpty(categoryTreeDto.getCategoryTreeDtoList())) {
            String currentCategorySeqPath = null;
            for (int i = 0; i < categoryTreeDto.getCategoryTreeDtoList().size(); i++) {
                CategoryTreeDto newCategoryDto = categoryTreeDto.getCategoryTreeDtoList().get(i);
                currentCategorySeqPath = getCurrentCategorySeqPath(newCategoryDto, currentCategoryId);
                if (currentCategorySeqPath != null) {
                    return currentCategorySeqPath;
                }
            }
        }

        return null;
    }

    /**
     * カテゴリー木構造Dto
     *
     * @param categoryTreeDto カテゴリー木構造Dtoクラス
     * @param categoryTreeResponse カテゴリ木構造レスポンス
     */
    public void toCategoryTreeDto(CategoryTreeDto categoryTreeDto, CategoryTreeResponse categoryTreeResponse) {

        if (!ObjectUtils.isEmpty(categoryTreeResponse)) {
            categoryTreeDto.setCategoryId(categoryTreeResponse.getCategoryId());
            categoryTreeDto.setDisplayName(categoryTreeResponse.getDisplayName());
            categoryTreeDto.setHierarchicalSerialNumber(categoryTreeResponse.getHierarchicalSerialNumber());
            if (categoryTreeResponse.getCategoryTreeResponse() != null) {
                categoryTreeDto.setCategoryTreeDtoList(
                                recursiveCategoryTree(categoryTreeResponse.getCategoryTreeResponse()));
            }
        }
    }

    /**
     * カテゴリーツリーの要素分、再帰的に処理をする
     *
     * @param categoryTreeResponse カテゴリ木構造レスポンスリスト
     * @return カテゴリー木構造Dtoリスト
     */
    private List<CategoryTreeDto> recursiveCategoryTree(List<CategoryTreeResponse> categoryTreeResponse) {
        List<CategoryTreeDto> categoryTreeDtoList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(categoryTreeResponse)) {
            for (int i = 0; i < categoryTreeResponse.size(); i++) {
                CategoryTreeDto categoryTreeDto = new CategoryTreeDto();
                CategoryTreeResponse categoryTreeResponseElement = categoryTreeResponse.get(i);

                categoryTreeDto.setCategoryId(categoryTreeResponseElement.getCategoryId());
                categoryTreeDto.setDisplayName(categoryTreeResponseElement.getDisplayName());
                categoryTreeDto.setHierarchicalSerialNumber(categoryTreeResponseElement.getHierarchicalSerialNumber());
                if (!ListUtils.isEmpty(categoryTreeResponseElement.getCategoryTreeResponse())) {
                    categoryTreeDto.setCategoryTreeDtoList(
                                    recursiveCategoryTree(categoryTreeResponseElement.getCategoryTreeResponse()));
                }

                categoryTreeDtoList.add(categoryTreeDto);
            }
        }
        return categoryTreeDtoList;
    }

}