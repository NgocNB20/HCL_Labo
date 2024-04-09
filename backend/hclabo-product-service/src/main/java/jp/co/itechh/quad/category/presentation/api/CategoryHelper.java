package jp.co.itechh.quad.category.presentation.api;

import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionDetailRegistUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionDetailResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryExclusiveResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryGoodsRegistUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryItemListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryItemResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryRegistRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategorySeqListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathListResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathResponse;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeSortByType;
import jp.co.itechh.quad.core.dto.goods.category.CategoryConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryExclusiveDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CategoryUtility;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * カテゴリ Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CategoryHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryHelper.class);

    /** カテゴリーヘルパークラス */
    private final CategoryUtility categoryUtility;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクター
     *
     * @param categoryUtility カテゴリーヘルパークラス
     * @param conversionUtility 変換ユーティリティクラス
     * @param dateUtility 日付関連Utilityクラス
     */
    @Autowired
    public CategoryHelper(CategoryUtility categoryUtility,
                          ConversionUtility conversionUtility,
                          DateUtility dateUtility) {
        this.categoryUtility = categoryUtility;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * カテゴリレスポンスクラスに変換
     *
     * @param categoryUpdateRequest カテゴリ登録リクエスト
     * @param originalCategoryDto           カテゴリ情報DTO
     * @return カテゴリDtoクラス
     */
    protected CategoryDto toCategoryUpdate(CategoryUpdateRequest categoryUpdateRequest,
                                           CategoryDto originalCategoryDto) {

        //使用するパラメータをディープコピーする
        CategoryDto modifyCategoryDto = CopyUtil.deepCopy(originalCategoryDto);

        Timestamp currentTime = dateUtility.getCurrentTime();

        if (modifyCategoryDto.getCategoryEntity() != null) {
            modifyCategoryDto.getCategoryEntity().setCategorySeq(categoryUpdateRequest.getCategorySeq());
            modifyCategoryDto.getCategoryEntity().setCategoryName(categoryUpdateRequest.getCategoryName());
            modifyCategoryDto.getCategoryEntity()
                             .setCategoryOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class,
                                                                                    categoryUpdateRequest.getCategoryOpenStatus()
                                                                                   ));
            modifyCategoryDto.getCategoryEntity()
                             .setCategoryOpenStartTimePC(conversionUtility.toTimeStamp(
                                             categoryUpdateRequest.getCategoryOpenStartTime()));
            modifyCategoryDto.getCategoryEntity()
                             .setCategoryOpenEndTimePC(conversionUtility.toTimeStamp(
                                             categoryUpdateRequest.getCategoryOpenEndTime()));
        }

        if (categoryUpdateRequest.getCategoryDisplayRequest() != null
            && modifyCategoryDto.getCategoryDisplayEntity() != null) {

            modifyCategoryDto.getCategoryDisplayEntity()
                             .setCategorySeq(categoryUpdateRequest.getCategoryDisplayRequest().getCategorySeq());
            modifyCategoryDto.getCategoryDisplayEntity()
                             .setCategoryNamePC(categoryUpdateRequest.getCategoryDisplayRequest().getCategoryName());
            modifyCategoryDto.getCategoryDisplayEntity()
                             .setCategoryNotePC(categoryUpdateRequest.getCategoryDisplayRequest().getCategoryNote());
            modifyCategoryDto.getCategoryDisplayEntity()
                             .setFreeTextPC(categoryUpdateRequest.getCategoryDisplayRequest().getFreeText());
            modifyCategoryDto.getCategoryDisplayEntity()
                             .setMetaDescription(
                                             categoryUpdateRequest.getCategoryDisplayRequest().getMetaDescription());
            modifyCategoryDto.getCategoryDisplayEntity()
                             .setCategoryImagePC(categoryUpdateRequest.getCategoryDisplayRequest().getCategoryImage());
        }

        if (categoryUpdateRequest.getCategoryGoodsManagement() != null
            && modifyCategoryDto.getCategoryGoodsSortEntity() != null) {

            modifyCategoryDto.getCategoryGoodsSortEntity()
                             .setGoodsSortColumn(EnumTypeUtil.getEnumFromValue(HTypeSortByType.class,
                                                                               categoryUpdateRequest.getCategoryGoodsManagement()
                                                                                                    .getGoodsSortColumn()
                                                                              ));
            modifyCategoryDto.getCategoryGoodsSortEntity()
                             .setGoodsSortOrder(categoryUpdateRequest.getCategoryGoodsManagement().getGoodsSortOrder());
            if (categoryUpdateRequest.getCategoryGoodsManagement().getManualyRegistGoodsList() != null) {

                List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();
                for (CategoryGoodsRegistUpdateRequest categoryGoodsRegistUpdateRequest : categoryUpdateRequest.getCategoryGoodsManagement()
                                                                                                              .getManualyRegistGoodsList()) {
                    CategoryGoodsEntity categoryGoodsEntity = new CategoryGoodsEntity();
                    categoryGoodsEntity.setCategorySeq(categoryUpdateRequest.getCategorySeq());
                    categoryGoodsEntity.setGoodsGroupSeq(categoryGoodsRegistUpdateRequest.getGoodsGroupSeq());
                    categoryGoodsEntity.setRegistTime(currentTime);
                    categoryGoodsEntity.setUpdateTime(currentTime);
                    categoryGoodsEntity.setManualOrderDisplay(categoryGoodsRegistUpdateRequest.getManualOrderDisplay());
                    categoryGoodsEntityList.add(categoryGoodsEntity);
                }
                modifyCategoryDto.setCategoryGoodsEntityList(categoryGoodsEntityList);
            }
        }

        if (categoryUpdateRequest.getCategoryCondition() != null
            && modifyCategoryDto.getCategoryConditionEntity() != null) {

            modifyCategoryDto.getCategoryConditionEntity()
                             .setConditionType(categoryUpdateRequest.getCategoryCondition().getConditionType());
            if (categoryUpdateRequest.getCategoryCondition().getConditionDetailList() != null) {

                List<CategoryConditionDetailEntity> categoryConditionDetailEntityList = new ArrayList<>();

                int conditionNoCount = 0;
                for (CategoryConditionDetailRegistUpdateRequest categoryConditionDetailRegistUpdateRequest : categoryUpdateRequest.getCategoryCondition()
                                                                                                                                  .getConditionDetailList()) {
                    CategoryConditionDetailEntity categoryConditionDetailEntity = new CategoryConditionDetailEntity();
                    categoryConditionDetailEntity.setCategorySeq(categoryUpdateRequest.getCategorySeq());
                    categoryConditionDetailEntity.setConditionNo(conditionNoCount);
                    categoryConditionDetailEntity.setConditionColumn(
                                    categoryConditionDetailRegistUpdateRequest.getConditionColumn());
                    categoryConditionDetailEntity.setConditionOperator(
                                    categoryConditionDetailRegistUpdateRequest.getConditionOperator());
                    categoryConditionDetailEntity.setConditionValue(
                                    categoryConditionDetailRegistUpdateRequest.getConditionValue());
                    categoryConditionDetailEntityList.add(categoryConditionDetailEntity);
                    conditionNoCount++;
                }
                modifyCategoryDto.setCategoryConditionDetailEntityList(categoryConditionDetailEntityList);
            }
        }

        return modifyCategoryDto;
    }

    /**
     * カテゴリレスポンスクラスに変換
     *
     * @param categoryResponse カテゴリレスポンス
     * @param categoryEntity   カテゴリクラス
     */
    protected void toCategoryResponse(CategoryResponse categoryResponse, CategoryEntity categoryEntity) {
        categoryResponse.setCategorySeq(categoryEntity.getCategorySeq());
        categoryResponse.setCategoryId(categoryEntity.getCategoryId());
        categoryResponse.setCategoryName(categoryEntity.getCategoryName());
        categoryResponse.setCategoryOpenStatus(EnumTypeUtil.getValue(categoryEntity.getCategoryOpenStatusPC()));
        categoryResponse.setCategoryOpenStartTime(categoryEntity.getCategoryOpenStartTimePC());
        categoryResponse.setCategoryOpenEndTime(categoryEntity.getCategoryOpenEndTimePC());
        categoryResponse.setCategoryType(EnumTypeUtil.getValue(categoryEntity.getCategoryType()));
        // categorySeqPathを再更新する
        categoryResponse.setRegistTime(categoryEntity.getRegistTime());
        categoryResponse.setUpdateTime(categoryEntity.getUpdateTime());
    }

    /**
     * カテゴリレスポンスクラスに変換
     *
     * @param categoryResponse カテゴリレスポンス
     * @param categoryDto      カテゴリDtoクラス
     */
    protected void toCategoryResponse(CategoryResponse categoryResponse, CategoryDto categoryDto) {

        CategoryEntity categoryEntity = categoryDto.getCategoryEntity();

        toCategoryResponse(categoryResponse, categoryEntity);

        categoryResponse.setCategoryNote(categoryDto.getCategoryDisplayEntity().getCategoryNotePC());
        categoryResponse.setFreeText(categoryDto.getCategoryDisplayEntity().getFreeTextPC());
        categoryResponse.setMetaDescription(categoryDto.getCategoryDisplayEntity().getMetaDescription());
        categoryResponse.setCategoryImage(categoryDto.getCategoryDisplayEntity().getCategoryImagePC());
        categoryResponse.setCategoryCondition(toCategoryConditionResponse(categoryDto.getCategoryConditionEntity(),
                                                                          categoryDto.getCategoryConditionDetailEntityList()
                                                                         ));
        categoryResponse.setGoodsSortColumn(categoryDto.getCategoryGoodsSortEntity().getGoodsSortColumn().getValue());
        categoryResponse.setGoodsSortOrder(categoryDto.getCategoryGoodsSortEntity().getGoodsSortOrder());
        categoryResponse.setDisplayRegistTime(categoryDto.getCategoryDisplayEntity().getRegistTime());
        categoryResponse.setDisplayUpdateTime(categoryDto.getCategoryDisplayEntity().getUpdateTime());
        categoryResponse.setFrontDisplay(EnumTypeUtil.getValue(categoryDto.getFrontDisplay()));
    }

    /**
     * カテゴリDtoクラスに変換
     *
     * @param categoryRegistRequest 　カテゴリ登録リクエスト
     * @param categorySeq           　カテゴリSEQ
     * @return カテゴリDtoクラス
     */
    protected CategoryDto toCategoryDto(CategoryRegistRequest categoryRegistRequest, Integer categorySeq) {
        CategoryDto categoryDto = ApplicationContextUtility.getBean(CategoryDto.class);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setShopSeq(1001);
        categoryEntity.setCategoryId(categoryRegistRequest.getCategoryRequest().getCategoryId());
        categoryEntity.setCategoryName(categoryRegistRequest.getCategoryRequest().getCategoryName());
        categoryEntity.setCategorySeq(categorySeq);
        categoryEntity.setCategoryOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class,
                                                                             categoryRegistRequest.getCategoryRequest()
                                                                                                  .getCategoryOpenStatus()
                                                                            ));
        categoryEntity.setCategoryOpenStartTimePC(conversionUtility.toTimeStamp(
                        categoryRegistRequest.getCategoryRequest().getCategoryOpenStartTime()));
        categoryEntity.setCategoryOpenEndTimePC(conversionUtility.toTimeStamp(
                        categoryRegistRequest.getCategoryRequest().getCategoryOpenEndTime()));
        categoryEntity.setCategoryType(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class,
                                                                     categoryRegistRequest.getCategoryRequest()
                                                                                          .getCategoryType()
                                                                    ));
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        // 登録/更新時刻の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        categoryEntity.setRegistTime(currentTime);
        categoryEntity.setUpdateTime(currentTime);
        categoryDto.setCategoryEntity(categoryEntity);

        CategoryDisplayEntity categoryDisplayEntity = new CategoryDisplayEntity();
        categoryDisplayEntity.setCategorySeq(categorySeq);
        categoryDisplayEntity.setCategoryNamePC(categoryRegistRequest.getCategoryDisplayRequest().getCategoryName());
        categoryDisplayEntity.setCategoryNotePC(categoryRegistRequest.getCategoryDisplayRequest().getCategoryNote());
        categoryDisplayEntity.setFreeTextPC(categoryRegistRequest.getCategoryDisplayRequest().getFreeText());
        categoryDisplayEntity.setMetaDescription(
                        categoryRegistRequest.getCategoryDisplayRequest().getMetaDescription());
        categoryDisplayEntity.setCategoryImagePC(categoryRegistRequest.getCategoryDisplayRequest().getCategoryImage());
        categoryDisplayEntity.setRegistTime(currentTime);
        categoryDisplayEntity.setUpdateTime(currentTime);
        categoryDto.setCategoryDisplayEntity(categoryDisplayEntity);

        CategoryGoodsSortEntity categoryGoodsSortEntity = new CategoryGoodsSortEntity();
        categoryGoodsSortEntity.setCategorySeq(categorySeq);
        if (HTypeCategoryType.AUTO.equals(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class,
                                                                        categoryRegistRequest.getCategoryRequest()
                                                                                             .getCategoryType()
                                                                       ))) {
            categoryGoodsSortEntity.setGoodsSortColumn(HTypeSortByType.POPULARITY);
            categoryGoodsSortEntity.setGoodsSortOrder(false);
        }
        if (HTypeCategoryType.NORMAL.equals(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class,
                                                                          categoryRegistRequest.getCategoryRequest()
                                                                                               .getCategoryType()
                                                                         ))) {
            categoryGoodsSortEntity.setGoodsSortColumn(HTypeSortByType.RECOMMEND);
            categoryGoodsSortEntity.setGoodsSortOrder(true);
        }
        categoryGoodsSortEntity.setRegistTime(currentTime);
        categoryGoodsSortEntity.setUpdateTime(currentTime);
        categoryDto.setCategoryGoodsSortEntity(categoryGoodsSortEntity);

        if (HTypeCategoryType.AUTO.equals(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class,
                                                                        categoryRegistRequest.getCategoryRequest()
                                                                                             .getCategoryType()
                                                                       ))) {
            // カテゴリ条件エンティティにセット
            CategoryConditionEntity categoryConditionEntity = new CategoryConditionEntity();
            categoryConditionEntity.setCategorySeq(categorySeq);
            categoryConditionEntity.setConditionType(categoryRegistRequest.getCategoryCondition().getConditionType());
            categoryConditionEntity.setRegistTime(currentTime);
            categoryConditionEntity.setUpdateTime(currentTime);
            categoryDto.setCategoryConditionEntity(categoryConditionEntity);

            // カテゴリ条件詳細エンティティリストにセット
            List<CategoryConditionDetailEntity> categoryConditionDetailEntityList = new ArrayList<>();
            int conditionCount = 0;
            for (CategoryConditionDetailRegistUpdateRequest categoryConditionDetailRegistUpdateRequest : categoryRegistRequest.getCategoryCondition()
                                                                                                                              .getConditionDetailList()) {
                CategoryConditionDetailEntity categoryConditionDetailEntity = new CategoryConditionDetailEntity();
                categoryConditionDetailEntity.setCategorySeq(categorySeq);
                categoryConditionDetailEntity.setConditionNo(conditionCount);
                categoryConditionDetailEntity.setConditionColumn(
                                categoryConditionDetailRegistUpdateRequest.getConditionColumn());
                categoryConditionDetailEntity.setConditionOperator(
                                categoryConditionDetailRegistUpdateRequest.getConditionOperator());
                categoryConditionDetailEntity.setConditionValue(
                                categoryConditionDetailRegistUpdateRequest.getConditionValue());
                categoryConditionDetailEntity.setRegistTime(currentTime);
                categoryConditionDetailEntity.setUpdateTime(currentTime);

                categoryConditionDetailEntityList.add(categoryConditionDetailEntity);
                conditionCount++;
            }
            categoryDto.setCategoryConditionDetailEntityList(categoryConditionDetailEntityList);
        }
        return categoryDto;
    }

    /**
     * カテゴリ一覧レスポンスクラスに変換
     *
     * @param categoryDetailsDtos 　カテゴリ詳細Dtoクラスリスト
     * @return カテゴリ一覧レスポンス
     */
    public CategoryListResponse toCategoryListResponse(List<CategoryDetailsDto> categoryDetailsDtos) {
        CategoryListResponse categoryListResponse = new CategoryListResponse();
        List<CategoryResponse> categoryList = new ArrayList<>();
        for (CategoryDetailsDto categoryDetailsDto : categoryDetailsDtos) {
            CategoryResponse categoryResponse = new CategoryResponse();

            categoryResponse.setCategorySeq(categoryDetailsDto.getCategorySeq());
            categoryResponse.setCategoryId(categoryDetailsDto.getCategoryId());
            categoryResponse.setCategoryName(categoryDetailsDto.getCategoryName());
            categoryResponse.setCategoryOpenStatus(EnumTypeUtil.getValue(categoryDetailsDto.getCategoryOpenStatusPC()));
            categoryResponse.setCategoryOpenStartTime(categoryDetailsDto.getCategoryOpenStartTimePC());
            categoryResponse.setCategoryOpenEndTime(categoryDetailsDto.getCategoryOpenEndTimePC());
            categoryResponse.setCategoryType(EnumTypeUtil.getValue(categoryDetailsDto.getCategoryType()));
            categoryResponse.setVersionNo(categoryDetailsDto.getVersionNo());
            categoryResponse.setRegistTime(categoryDetailsDto.getRegistTime());
            categoryResponse.setUpdateTime(categoryDetailsDto.getUpdateTime());
            categoryResponse.setOpenGoodsCount(categoryDetailsDto.getOpenGoodsCount());
            categoryResponse.setCategoryNote(categoryDetailsDto.getCategoryNotePC());
            categoryResponse.setFreeText(categoryDetailsDto.getFreeTextPC());
            categoryResponse.setMetaDescription(categoryDetailsDto.getMetaDescription());
            categoryResponse.setCategoryImage(categoryDetailsDto.getCategoryImagePC());
            categoryResponse.setCategoryCondition(toCategoryConditionResponse(categoryDetailsDto.getConditionType(),
                                                                              categoryDetailsDto.getConditionDetailList()
                                                                             ));
            categoryResponse.setGoodsSortColumn(categoryResponse.getGoodsSortColumn());
            categoryResponse.setGoodsSortOrder(categoryResponse.getGoodsSortOrder());
            categoryResponse.setDisplayRegistTime(categoryDetailsDto.getDisplayRegistTime());
            categoryResponse.setDisplayUpdateTime(categoryDetailsDto.getDisplayUpdateTime());
            categoryResponse.setFrontDisplay(EnumTypeUtil.getValue(categoryDetailsDto.getFrontDisplay()));

            categoryList.add(categoryResponse);
        }
        categoryListResponse.setCategoryList(categoryList);

        return categoryListResponse;
    }

    /**
     * カテゴリ条件クラスに変換
     *
     * @param conditionType       条件種別
     * @param conditionDetailList カテゴリ条件詳細レスポンス
     * @return カテゴリ条件クラス
     */
    protected CategoryConditionResponse toCategoryConditionResponse(String conditionType, String conditionDetailList) {

        if (ObjectUtils.isEmpty(conditionDetailList)) {
            return null;
        }

        List<CategoryConditionDetailResponse> categoryConditionDetailResponseList = new ArrayList<>();

        CategoryConditionResponse categoryConditionResponse = new CategoryConditionResponse();

        String[] categoryConditionArr = conditionDetailList.split("\\|");

        for (String categoryCondition : categoryConditionArr) {
            String[] categoryConditionDetailArr = categoryCondition.split(" ");
            CategoryConditionDetailResponse categoryConditionDetailResponse = new CategoryConditionDetailResponse();

            categoryConditionDetailResponse.setConditionColumn(categoryConditionDetailArr[0]);
            categoryConditionDetailResponse.setConditionOperator(categoryConditionDetailArr[1]);
            if (categoryConditionDetailArr.length > 2) {
                categoryConditionDetailResponse.setConditionValue(categoryConditionDetailArr[2]);
            }

            categoryConditionDetailResponseList.add(categoryConditionDetailResponse);
        }

        categoryConditionResponse.setConditionType(conditionType);
        categoryConditionResponse.setConditionDetailList(categoryConditionDetailResponseList);

        return categoryConditionResponse;
    }

    /**
     * カテゴリ情報Dao用検索条件Dtoクラスに変換
     *
     * @param categoryTreeGetRequest カテゴリ木構造取得リクエスト
     * @return カテゴリ情報Dao用検索条件Dtoクラス
     */
    public CategorySearchForDaoConditionDto toCategorySearchForDaoConditionDto(CategoryTreeGetRequest categoryTreeGetRequest) {
        CategorySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(CategorySearchForDaoConditionDto.class);

        conditionDto.setMaxHierarchical(categoryTreeGetRequest.getMaxHierarchical());
        conditionDto.setOpenStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, categoryTreeGetRequest.getOpenStatus()));
        conditionDto.setFrontDisplayReferenceDate(
                        this.conversionUtility.toTimeStamp(categoryTreeGetRequest.getFrontDisplayReferenceDate()));

        return conditionDto;
    }

    /**
     * @param categoryListGetRequest カテゴリ木構造取得リクエスト
     * @return カテゴリ情報Dao用検索条件Dtoクラス
     */
    public CategorySearchForDaoConditionDto toCategorySearchForDaoConditionDto(CategoryListGetRequest categoryListGetRequest) {
        CategorySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(CategorySearchForDaoConditionDto.class);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // TODO SiteTYPE
        HTypeSiteType siteType = HTypeSiteType.BACK;

        conditionDto.setShopSeq(1001);
        conditionDto.setSiteType(siteType);
        if (categoryListGetRequest.getCategorySearchKeyword() != null) {
            conditionDto.setCategorySearchKeyword(categoryListGetRequest.getCategorySearchKeyword());
        } else {
            conditionDto.setCategorySearchKeyword("");
        }
        conditionDto.setCategoryCondition1(toCategoryConditionDto(categoryListGetRequest.getConditionColumn1(),
                                                                  categoryListGetRequest.getConditionValue1()
                                                                 ));
        conditionDto.setCategoryCondition2(toCategoryConditionDto(categoryListGetRequest.getConditionColumn2(),
                                                                  categoryListGetRequest.getConditionValue2()
                                                                 ));
        conditionDto.setCategoryCondition3(toCategoryConditionDto(categoryListGetRequest.getConditionColumn3(),
                                                                  categoryListGetRequest.getConditionValue3()
                                                                 ));
        conditionDto.setCategoryCondition4(toCategoryConditionDto(categoryListGetRequest.getConditionColumn4(),
                                                                  categoryListGetRequest.getConditionValue4()
                                                                 ));
        conditionDto.setCategoryCondition5(toCategoryConditionDto(categoryListGetRequest.getConditionColumn5(),
                                                                  categoryListGetRequest.getConditionValue5()
                                                                 ));
        conditionDto.setOpenStartTimeFrom(
                        dateUtility.convertDateToTimestamp(categoryListGetRequest.getOpenStartTimeFrom()));
        conditionDto.setOpenStartTimeTo(dateUtility.getEndOfDate(
                        dateUtility.convertDateToTimestamp(categoryListGetRequest.getOpenStartTimeTo())));
        conditionDto.setOpenEndTimeFrom(
                        dateUtility.convertDateToTimestamp(categoryListGetRequest.getOpenEndTimeFrom()));
        conditionDto.setOpenEndTimeTo(dateUtility.getEndOfDate(
                        dateUtility.convertDateToTimestamp(categoryListGetRequest.getOpenEndTimeTo())));
        conditionDto.setOpenStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, categoryListGetRequest.getOpenStatus()));
        conditionDto.setCategoryTypeList(categoryListGetRequest.getCategoryType());
        conditionDto.setFrontDisplayList(categoryListGetRequest.getFrontDisplayList());
        conditionDto.setFrontDisplayReferenceDate(
                        this.conversionUtility.toTimeStamp(categoryListGetRequest.getFrontDisplayReferenceDate()));
        return conditionDto;
    }

    /**
     * カテゴリ条件Dtoクラスに変換
     *
     * @param conditionColumn 条件項目
     * @param conditionValue 条件値
     * @return カテゴリ条件Dtoクラス
     */
    private CategoryConditionDto toCategoryConditionDto(String conditionColumn, String conditionValue) {

        if (conditionColumn == null && conditionValue == null) {
            return null;
        }

        CategoryConditionDto dto = new CategoryConditionDto();

        dto.setConditionColumn(conditionColumn);
        dto.setConditionValue(conditionValue);

        return dto;
    }

    /**
     * カテゴリ木構造クラスに変換
     *
     * @param categoryTreeDto カテゴリー木構造Dtoクラス
     * @param categoryTreeResponse カテゴリ木構造レスポンス
     */
    public void toCategoryTreeResponse(CategoryTreeDto categoryTreeDto, CategoryTreeResponse categoryTreeResponse) {

        categoryTreeResponse.setCategoryId(categoryTreeDto.getCategoryId());
        categoryTreeResponse.setDisplayName(categoryTreeDto.getDisplayName());
        categoryTreeResponse.setHierarchicalSerialNumber(categoryTreeDto.getHierarchicalSerialNumber());
        if (categoryTreeDto.getCategoryTreeDtoList() != null) {
            categoryTreeResponse.setCategoryTreeResponse(
                            recursiveCategoryTree(categoryTreeDto.getCategoryTreeDtoList()));
        }
    }

    /**
     * カテゴリーツリーの要素分、再帰的に処理をする
     *
     * @param categoryTreeDtoList カテゴリDTOリスト
     *
     * @return カテゴリ木構造レスポンスリスト
     */
    private List<CategoryTreeResponse> recursiveCategoryTree(List<CategoryTreeDto> categoryTreeDtoList) {
        List<CategoryTreeResponse> categoryTreeResponseList = new ArrayList<>();

        for (int i = 0; i < categoryTreeDtoList.size(); i++) {
            CategoryTreeResponse categoryTreeResponse = new CategoryTreeResponse();
            CategoryTreeDto categoryTreeDtoObject = categoryTreeDtoList.get(i);

            categoryTreeResponse.setCategoryId(categoryTreeDtoObject.getCategoryId());
            categoryTreeResponse.setHierarchicalSerialNumber(categoryTreeDtoObject.getHierarchicalSerialNumber());
            categoryTreeResponse.setDisplayName(categoryTreeDtoObject.getDisplayName());
            if (!ListUtils.isEmpty(categoryTreeDtoObject.getCategoryTreeDtoList())) {
                categoryTreeResponse.setCategoryTreeResponse(
                                recursiveCategoryTree(categoryTreeDtoObject.getCategoryTreeDtoList()));
            }

            categoryTreeResponseList.add(categoryTreeResponse);
        }
        return categoryTreeResponseList;
    }

    /**
     * カテゴリ一覧レスポンスクラスに変換
     *
     * @param categoryGoodsDetailsDtoList 　カテゴリ内商品詳細Dtoクラスリスト
     * @return カテゴリ内商品一覧レスポンス
     */
    public CategoryItemListResponse toCategoryItemListResponses(List<CategoryGoodsDetailsDto> categoryGoodsDetailsDtoList) {
        CategoryItemListResponse categoryItemListResponse = new CategoryItemListResponse();
        List<CategoryItemResponse> categoryList = new ArrayList<>();

        if (!ListUtils.isEmpty(categoryGoodsDetailsDtoList)) {
            for (CategoryGoodsDetailsDto categoryGoodsDetailsDto : categoryGoodsDetailsDtoList) {
                CategoryItemResponse categoryResponse = new CategoryItemResponse();

                categoryResponse.setCategorySeq(categoryGoodsDetailsDto.getCategorySeq());
                categoryResponse.setGoodsGroupSeq(categoryGoodsDetailsDto.getGoodsGroupSeq());
                categoryResponse.setManualOrderDisplay(categoryGoodsDetailsDto.getManualOrderDisplay());
                categoryResponse.setRegistTime(categoryGoodsDetailsDto.getRegistTime());
                categoryResponse.setUpdateTime(categoryGoodsDetailsDto.getUpdateTime());
                categoryResponse.setGoodsGroupCode(categoryGoodsDetailsDto.getGoodsGroupCode());
                categoryResponse.setGoodsGroupName(categoryGoodsDetailsDto.getGoodsGroupName());
                categoryResponse.setGoodsPrice(categoryGoodsDetailsDto.getGoodsPrice());
                categoryResponse.setWhatsnewDate(categoryGoodsDetailsDto.getWhatsnewDate());
                categoryResponse.setGoodsOpenStatus(
                                EnumTypeUtil.getValue(categoryGoodsDetailsDto.getGoodsOpenStatusPC()));
                categoryResponse.setOpenStartTime(categoryGoodsDetailsDto.getOpenStartTimePC());
                categoryResponse.setOpenEndTime(categoryGoodsDetailsDto.getOpenEndTimePC());
                categoryResponse.setVersionNo(categoryGoodsDetailsDto.getVersionNo());
                categoryResponse.setPopularityCount(categoryGoodsDetailsDto.getPopularityCount());
                categoryResponse.setFrontDisplay(EnumTypeUtil.getValue(categoryGoodsDetailsDto.getFrontDisplay()));

                categoryList.add(categoryResponse);
            }
        }
        categoryItemListResponse.setCategoryItemList(categoryList);

        return categoryItemListResponse;
    }

    /**
     * カテゴリ排他Dtoクラスレスポンスクラスに変換
     *
     * @param categoryExclusiveDto 　カテゴリ排他Dtoクラス
     * @return カテゴリ排他クラス
     */
    public CategoryExclusiveResponse toCategoryExclusiveResponse(CategoryExclusiveDto categoryExclusiveDto) {
        CategoryExclusiveResponse categoryExclusiveResponse = new CategoryExclusiveResponse();

        if (categoryExclusiveDto != null) {
            categoryExclusiveResponse.setSumCategory(categoryExclusiveDto.getSumCategory());
            categoryExclusiveResponse.setMaxUpdateTime(categoryExclusiveDto.getMaxUpdateTime());
        }

        return categoryExclusiveResponse;
    }

    /**
     * リストカテゴリをカテゴリSEQリスト レスポンスに変換する
     *
     * @param categoryEntityList 　カテゴリクラスリスト
     * @return カテゴリSEQリストレスポンス
     */
    public CategorySeqListResponse toCategorySeqListResponse(List<CategoryEntity> categoryEntityList) {

        if (CollectionUtils.isEmpty(categoryEntityList)) {
            return new CategorySeqListResponse();
        }
        CategorySeqListResponse categorySeqListResponse = new CategorySeqListResponse();
        List<Integer> categorySeqList = new ArrayList<>();

        for (CategoryEntity entity : categoryEntityList) {
            categorySeqList.add(entity.getCategorySeq());
        }
        categorySeqListResponse.setCategorySeqList(categorySeqList);

        return categorySeqListResponse;
    }

    /**
     * カテゴリレスポンスに変換
     *
     * @param categoryDto カテゴリDtoクラス
     * @return カテゴリレスポンス
     */
    public CategoryResponse toCategoryResponse(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        CategoryResponse categoryResponse = new CategoryResponse();
        CategoryConditionResponse categoryConditionResponse = new CategoryConditionResponse();

        // カテゴリエンティティ
        CategoryEntity categoryEntity = categoryDto.getCategoryEntity();

        // カテゴリ表示エンティティ
        CategoryDisplayEntity categoryDisplayEntity = categoryDto.getCategoryDisplayEntity();

        // カテゴリ条件クラス
        CategoryConditionEntity categoryConditionEntity = categoryDto.getCategoryConditionEntity();

        // カテゴリ条件詳細リスト
        CategoryGoodsSortEntity categoryGoodsSortEntity = categoryDto.getCategoryGoodsSortEntity();

        if (!ObjectUtils.isEmpty(categoryEntity)) {
            categoryResponse.setCategorySeq(categoryEntity.getCategorySeq());
            categoryResponse.setCategoryId(categoryEntity.getCategoryId());
            categoryResponse.setCategoryName(categoryEntity.getCategoryName());
            categoryResponse.setCategoryOpenStatus(EnumTypeUtil.getValue(categoryEntity.getCategoryOpenStatusPC()));
            categoryResponse.setCategoryOpenStartTime(categoryEntity.getCategoryOpenStartTimePC());
            categoryResponse.setCategoryOpenEndTime(categoryEntity.getCategoryOpenEndTimePC());
            categoryResponse.setCategoryType(EnumTypeUtil.getValue(categoryEntity.getCategoryType()));
            categoryResponse.setRegistTime(categoryEntity.getRegistTime());
            categoryResponse.setUpdateTime(categoryEntity.getUpdateTime());

            if (!ObjectUtils.isEmpty(categoryDisplayEntity)) {
                categoryResponse.setCategoryNote(categoryDisplayEntity.getCategoryNotePC());
                categoryResponse.setFreeText(categoryDisplayEntity.getFreeTextPC());
                categoryResponse.setMetaDescription(categoryDisplayEntity.getMetaDescription());
                // カテゴリ画像配置パスの取得
                categoryResponse.setCategoryImage(
                                categoryUtility.getCategoryImagePath(categoryDisplayEntity.getCategoryImagePC()));
                // カテゴリ条件クラスに変換
                categoryConditionResponse = toCategoryConditionResponse(categoryConditionEntity,
                                                                        categoryDto.getCategoryConditionDetailEntityList()
                                                                       );
                categoryResponse.setCategoryCondition(categoryConditionResponse);

                categoryResponse.setGoodsSortColumn(categoryGoodsSortEntity.getGoodsSortColumn().getValue());
                categoryResponse.setGoodsSortOrder(categoryGoodsSortEntity.getGoodsSortOrder());
                categoryResponse.setDisplayRegistTime(categoryDisplayEntity.getRegistTime());
                categoryResponse.setDisplayUpdateTime(categoryDisplayEntity.getUpdateTime());
            }
        }

        categoryResponse.setFrontDisplay(EnumTypeUtil.getValue(categoryDto.getFrontDisplay()));
        categoryResponse.setWarningMessage(categoryDto.getWarningMessage());

        return categoryResponse;
    }

    /**
     * カテゴリ条件クラスに変換
     *
     * @param categoryConditionEntity カテゴリ条件クラス
     * @param categoryConditionDetailEntityList カテゴリ条件詳細リスト
     * @return カテゴリ条件クラス
     */
    private CategoryConditionResponse toCategoryConditionResponse(CategoryConditionEntity categoryConditionEntity,
                                                                  List<CategoryConditionDetailEntity> categoryConditionDetailEntityList) {
        CategoryConditionResponse categoryConditionResponse = new CategoryConditionResponse();

        if (!ObjectUtils.isEmpty(categoryConditionEntity)) {
            categoryConditionResponse.setConditionType(categoryConditionEntity.getConditionType());
        }
        // カテゴリ条件詳細レスポンスに変換
        List<CategoryConditionDetailResponse> categoryConditionDetailResponseList =
                        toCategoryConditionDetailResponse(categoryConditionDetailEntityList);
        categoryConditionResponse.setConditionDetailList(categoryConditionDetailResponseList);

        return categoryConditionResponse;
    }

    /**
     * カテゴリ条件詳細レスポンスに変換
     *
     * @param categoryConditionDetailEntityList カテゴリ条件詳細リスト
     * @return カテゴリ条件詳細レスポンス
     */
    private List<CategoryConditionDetailResponse> toCategoryConditionDetailResponse(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList) {
        List<CategoryConditionDetailResponse> categoryConditionDetailResponseList = new ArrayList<>();

        if (!ListUtils.isEmpty(categoryConditionDetailEntityList)) {
            for (CategoryConditionDetailEntity categoryConditionDetailEntity : categoryConditionDetailEntityList) {
                CategoryConditionDetailResponse categoryConditionDetailResponse = new CategoryConditionDetailResponse();

                categoryConditionDetailResponse.setConditionNo(categoryConditionDetailEntity.getConditionNo());
                categoryConditionDetailResponse.setConditionColumn(categoryConditionDetailEntity.getConditionColumn());
                categoryConditionDetailResponse.setConditionOperator(
                                categoryConditionDetailEntity.getConditionOperator());
                categoryConditionDetailResponse.setConditionValue(categoryConditionDetailEntity.getConditionValue());

                categoryConditionDetailResponseList.add(categoryConditionDetailResponse);
            }
        }

        return categoryConditionDetailResponseList;
    }

    /**
     * パンくずリストレスポンスクラスに変換
     *
     * @param categoryId カテゴリId
     * @param categoryDto カテゴリDtoクラス
     * @param categoryTreeDto カテゴリDtoクラス
     * @param topicPathGetRequest パンくずリスト取得リクエスト
     * @return パンくずリストレスポンス
     */
    public TopicPathListResponse toTopicPathListResponse(String categoryId,
                                                         CategoryDto categoryDto,
                                                         CategoryTreeDto categoryTreeDto,
                                                         TopicPathGetRequest topicPathGetRequest) {
        TopicPathListResponse topicPathListResponse = new TopicPathListResponse();
        List<TopicPathResponse> topicPathList = new ArrayList<>();

        if (categoryTreeDto.getCategoryTreeDtoList() != null) {
            getTopicPathResponseList(categoryTreeDto.getCategoryTreeDtoList(), topicPathList);

            if (topicPathGetRequest.getHierarchicalSerialNumber() == null) {
                for (TopicPathResponse topicPathResponse : topicPathList) {
                    if (topicPathResponse.getCategoryId().equals(categoryId)) {
                        List<TopicPathResponse> responses =
                                        getResponse(topicPathResponse.getHierarchicalSerialNumber(), topicPathList);
                        topicPathListResponse.setTopicPathList(responses);
                        return topicPathListResponse;
                    }
                }
                return getDefaultTopicPathListResponse(categoryDto);
            } else {
                if (!checkCategoryId(categoryId, topicPathGetRequest.getHierarchicalSerialNumber(), topicPathList)) {
                    return getDefaultTopicPathListResponse(categoryDto);
                }

                List<TopicPathResponse> responses =
                                getResponse(topicPathGetRequest.getHierarchicalSerialNumber(), topicPathList);
                topicPathListResponse.setTopicPathList(responses);
                return topicPathListResponse;
            }
        }
        return topicPathListResponse;
    }

    /**
     * TopicPathリストを生成（再帰）
     *
     * @param categoryTreeDtoList カテゴリー木構造Dtoクラスリスト
     * @param topicPathResponseList パンくずリスト項目レスポンスリスト
     */
    private void getTopicPathResponseList(List<CategoryTreeDto> categoryTreeDtoList,
                                          List<TopicPathResponse> topicPathResponseList) {
        if (categoryTreeDtoList == null) {
            return;
        }
        for (CategoryTreeDto categoryTreeDto : categoryTreeDtoList) {
            TopicPathResponse topicPathResponse = new TopicPathResponse();
            topicPathResponse.setCategoryId(categoryTreeDto.getCategoryId());
            topicPathResponse.setDisplayName(categoryTreeDto.getDisplayName());
            topicPathResponse.setHierarchicalSerialNumber(categoryTreeDto.getHierarchicalSerialNumber());
            topicPathResponseList.add(topicPathResponse);
            if (categoryTreeDto.getCategoryTreeDtoList() != null) {
                getTopicPathResponseList(categoryTreeDto.getCategoryTreeDtoList(), topicPathResponseList);
            }
        }
    }

    /**
     * TopicPathリストを生成
     *
     * @param cat 階層通番
     * @param topicPathResponseList パンくずリスト項目レスポンスリスト
     * @return パンくずリスト項目レスポンスリスト
     */
    private List<TopicPathResponse> getResponse(String cat, List<TopicPathResponse> topicPathResponseList) {
        List<TopicPathResponse> ret = new ArrayList<>();
        String[] catArray = cat.split("-");
        List<String> regex = new ArrayList<>();
        StringBuilder regexOrigin = new StringBuilder(catArray[0]);
        regex.add(regexOrigin.toString());
        if (catArray.length > 1) {
            for (int i = 1; i < catArray.length; i++) {
                regexOrigin.append("-").append(catArray[i]);
                regex.add(regexOrigin.toString());
            }
        }
        for (TopicPathResponse topicPathResponse : topicPathResponseList) {
            if (regex.contains(topicPathResponse.getHierarchicalSerialNumber())) {
                ret.add(topicPathResponse);
            }
        }
        return ret;
    }

    /**
     * カテゴリーIDチェック
     *
     * @param categoryId カテゴリーID
     * @param hierarchicalSerialNumber 階層通番
     * @param topicPathList TopicPathリスト
     * @return true: カテゴリーID存在
     */
    private boolean checkCategoryId(String categoryId,
                                    String hierarchicalSerialNumber,
                                    List<TopicPathResponse> topicPathList) {
        for (TopicPathResponse topicPathResponse : topicPathList) {
            if (hierarchicalSerialNumber.equals(topicPathResponse.getHierarchicalSerialNumber())) {
                if (categoryId.equals(topicPathResponse.getCategoryId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * CategoryTreeDtoに該当するDtoが無いパターン
     *
     * @param categoryDto 　カテゴリDtoクラス
     * @return CategoryEntityを使って、TopicPathListを作成する
     */
    private TopicPathListResponse getDefaultTopicPathListResponse(CategoryDto categoryDto) {
        TopicPathListResponse topicPathListResponse = new TopicPathListResponse();
        List<TopicPathResponse> responses = new ArrayList<>();
        TopicPathResponse topicPathResponse = new TopicPathResponse();
        topicPathResponse.setCategoryId(categoryDto.getCategoryEntity().getCategoryId());
        topicPathResponse.setDisplayName(categoryDto.getCategoryEntity().getCategoryName());
        responses.add(topicPathResponse);
        topicPathListResponse.setTopicPathList(responses);
        return topicPathListResponse;
    }
}