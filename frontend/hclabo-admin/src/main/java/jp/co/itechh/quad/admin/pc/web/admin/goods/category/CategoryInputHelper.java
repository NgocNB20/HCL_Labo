/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryGoodsManualSort;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryGoodsSort;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionOperatorType;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSortByType;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.GoodsSearchModel;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.CategoryUtility;
import jp.co.itechh.quad.admin.utility.ImageUtility;
import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionDetailRegistUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionRegistUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryDisplayRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryGoodsManagementRegistUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryGoodsRegistUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryItemListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryRegistRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * カテゴリ管理：カテゴリ入力
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CategoryInputHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryInputHelper.class);

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /** 全角、半角の変換を行うヘルパークラス */
    private final ZenHanConversionUtility zenHanConversionUtility;

    /** カテゴリーヘルパークラス */
    private final CategoryUtility categoryUtility;

    /** ファイルの コピー / 移動 / 削除 用ヘルパークラス */
    private final FileOperationUtility fileOperationUtility;

    /** 画像操作ヘルパークラス */
    private final ImageUtility imageUtility;

    /** ディレクトリセパレータ */
    private static final String SEPARATOR = "/";

    /** したコネクションパス取得 **/
    private static final String CONTEXT_PATH = PropertiesUtil.getSystemPropertiesValue("server.contextPath");

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     * @param dateUtility 日付関連Utilityクラス
     * @param zenHanConversionUtility 全角、半角の変換を行うヘルパークラス
     * @param categoryUtility カテゴリーヘルパークラス
     * @param fileOperationUtility ファイルの コピー / 移動 / 削除 用ヘルパークラス
     * @param imageUtility 画像操作ヘルパークラス
     */
    @Autowired
    public CategoryInputHelper(ConversionUtility conversionUtility,
                               DateUtility dateUtility,
                               ZenHanConversionUtility zenHanConversionUtility,
                               CategoryUtility categoryUtility,
                               FileOperationUtility fileOperationUtility,
                               ImageUtility imageUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.zenHanConversionUtility = zenHanConversionUtility;
        this.categoryUtility = categoryUtility;
        this.fileOperationUtility = fileOperationUtility;
        this.imageUtility = imageUtility;
    }

    /**
     * セッションがある場合の初期値設定<br/>
     *
     * @param model ページ
     */
    public void sessionInit(CategoryInputModel model) {
        CategoryDto dto = model.getCategoryDto();

        model.setCategoryId(dto.getCategoryEntity().getCategoryId());
        model.setCategoryName(dto.getCategoryEntity().getCategoryName());
        model.setCategoryType(dto.getCategoryEntity().getCategoryType().getValue());
        model.setMetaDescription(dto.getCategoryDisplayEntity().getMetaDescription());
        model.setMetaKeyword(dto.getCategoryDisplayEntity().getMetaKeyword());
        model.setManualDisplayFlag(dto.getCategoryEntity().getManualDisplayFlag().getValue());
        model.setSiteMapFlag(dto.getCategoryEntity().getSiteMapFlag().getValue());

        model.setCategoryOpenFromDatePC(
                        dateUtility.formatYmdWithSlash(dto.getCategoryEntity().getCategoryOpenStartTimePC()));
        model.setCategoryOpenFromTimePC(dateUtility.formatHMS(dto.getCategoryEntity().getCategoryOpenStartTimePC()));
        model.setCategoryOpenToDatePC(
                        dateUtility.formatYmdWithSlash(dto.getCategoryEntity().getCategoryOpenEndTimePC()));
        model.setCategoryOpenToTimePC(dateUtility.formatHMS(dto.getCategoryEntity().getCategoryOpenEndTimePC()));

        model.setCategoryOpenStatusPC(dto.getCategoryEntity().getCategoryOpenStatusPC().getValue());

        model.setFreeTextPC(dto.getCategoryDisplayEntity().getFreeTextPC());

        model.setCategoryImagePC(dto.getCategoryDisplayEntity().getCategoryImagePC());
        model.setFileNamePC(this.updateCategoryImagePath(dto.getCategoryDisplayEntity().getCategoryImagePC()));
        model.setTmpImagePC(false);

        // PCテンプ画像がある場合は、テンプ画像を表示
        if (model.isTmpImagePC()) {
            // PC画像が指定されていれば設定
            if (StringUtils.isNotEmpty(model.getCategoryImagePC())) {
                model.setCategoryImagePathPC(categoryUtility.getCategoryImageTempPath(model.getCategoryImagePC()));
            }
        } else {
            // PC画像が指定されていれば設定
            if (StringUtils.isNotEmpty(model.getCategoryImagePC())) {
                model.setCategoryImagePathPC(CONTEXT_PATH + model.getCategoryImagePC());
            } else {
                model.setCategoryImagePathPC("");
            }
        }

        // フロント表示設定
        model.setFrontDisplay(ObjectUtils.isEmpty(dto.getFrontDisplay()) ? null : dto.getFrontDisplay().getValue());

        // プレビュー日時設定
        setPreviewForm(model);
    }

    /**
     * 次画面へ遷移のためDTOへ設定<br/>
     *
     * @param model ページ
     */
    public void toDto(CategoryInputModel model) {
        CategoryDto dto = model.getCategoryDto();

        dto.getCategoryEntity().setCategoryId(model.getCategoryId());
        dto.getCategoryEntity().setCategoryName(model.getCategoryName());

        dto.getCategoryEntity()
           .setCategoryOpenStatusPC(
                           EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, model.getCategoryOpenStatusPC()));
        dto.getCategoryEntity()
           .setCategoryOpenStartTimePC(conversionUtility.toTimeStampWithDefaultHms(model.getCategoryOpenFromDatePC(),
                                                                                   model.getCategoryOpenFromTimePC(),
                                                                                   ConversionUtility.DEFAULT_START_TIME
                                                                                  ));

        dto.getCategoryEntity()
           .setCategoryOpenEndTimePC(conversionUtility.toTimeStampWithDefaultHms(model.getCategoryOpenToDatePC(),
                                                                                 model.getCategoryOpenToTimePC(),
                                                                                 ConversionUtility.DEFAULT_END_TIME
                                                                                ));

        dto.getCategoryEntity()
           .setCategoryType(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, model.getCategoryType()));
        dto.getCategoryEntity()
           .setManualDisplayFlag(
                           EnumTypeUtil.getEnumFromValue(HTypeManualDisplayFlag.class, model.getManualDisplayFlag()));
        dto.getCategoryEntity()
           .setSiteMapFlag(EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, model.getSiteMapFlag()));
        dto.getCategoryDisplayEntity().setCategoryNamePC(model.getCategoryName());
        dto.getCategoryDisplayEntity().setFreeTextPC(model.getFreeTextPC());
        dto.getCategoryDisplayEntity().setMetaDescription(model.getMetaDescription());
        dto.getCategoryDisplayEntity().setMetaKeyword(model.getMetaKeyword());
    }

    /**
     * 初期値設定(新規登録用)<br/>
     *
     * @param inputmodel ページ
     */
    public void registInit(CategoryInputModel inputmodel) {
        CategoryDto categoryDto = ApplicationContextUtility.getBean(CategoryDto.class);
        CategoryEntity categoryEntity = ApplicationContextUtility.getBean(CategoryEntity.class);
        CategoryDisplayEntity categoryDisplayEntity = ApplicationContextUtility.getBean(CategoryDisplayEntity.class);
        categoryDto.setCategoryEntity(categoryEntity);
        categoryDto.setCategoryDisplayEntity(categoryDisplayEntity);
        inputmodel.setCategoryDto(categoryDto);

    }

    /**
     * 修正で一覧からきた場合の設定(修正用)<br/>
     *
     * @param inputmodel ページ
     */
    public void toModigyFromList(CategoryInputModel inputmodel) {
        CategoryTreeItem categoryTreeItems = ApplicationContextUtility.getBean(CategoryTreeItem.class);
        String categoryPathName =
                        makeNodeName(inputmodel, inputmodel.getCategoryDto().getCategoryEntity().getCategorySeqPath());
        categoryTreeItems.setListScreen(inputmodel.isListScreen());
        categoryTreeItems.setCategoryId(inputmodel.getCategoryDto().getCategoryEntity().getCategoryId());
        categoryTreeItems.setCategoryPathName(categoryPathName);
        inputmodel.setCategoryPathName(categoryPathName);
        inputmodel.setTargetParentCategory(categoryTreeItems);
    }

    /**
     * 連結文字を生成し設定<br/>
     *
     * @param inputmodel      ページ
     * @param categorySeqPath カテゴリSEQパス
     * @return カテゴリ連結文字
     */
    protected String makeNodeName(CategoryInputModel inputmodel, String categorySeqPath) {
        Iterator<String> ite = divide(categorySeqPath).iterator();
        StringBuilder buff = new StringBuilder();
        if (inputmodel.isListScreen()) {
            // 更新画面用
            while (ite.hasNext()) {
                String categorySeq = ite.next();
                if (!categorySeq.equals(
                                categorySeqPath.substring(categorySeqPath.length() - 8, categorySeqPath.length()))) {
                    allNodeName(inputmodel.getCategoryDtoDB(), categorySeq, buff);
                }
            }
            return buff.toString();
        }
        // 登録画面用
        while (ite.hasNext()) {
            allNodeName(inputmodel.getCategoryDtoDB(), ite.next(), buff);
        }
        return buff.toString();
    }

    /**
     * 再帰処理にて連続名称を生成
     *
     * @param categoryDto カテゴリDTO
     * @param categorySeq カテゴリSeqPath
     * @param buff        カテゴリ連結文字列
     */
    protected void allNodeName(CategoryDto categoryDto, String categorySeq, StringBuilder buff) {
        if (categorySeq.equals(categoryDto.getCategoryEntity().getCategorySeq().toString())) {
            if (!categoryUtility.getCategoryIdTop().equals(categoryDto.getCategoryEntity().getCategoryId())) {
                buff.append(" > ");
            }
            buff.append(categoryDto.getCategoryEntity().getCategoryName());
        }
        for (CategoryDto subNode : categoryDto.getCategoryDtoList()) {
            allNodeName(subNode, categorySeq, buff);
        }
    }

    /**
     * カテゴリSEQを分割
     *
     * @param categorySeqPath カテゴリSeqPath
     * @return list カテゴリSEQリスト
     */
    protected List<String> divide(String categorySeqPath) {
        int length = 8;
        int i = 0;
        int j = 8;
        List<String> list = new ArrayList<>();
        if (categorySeqPath != null && categorySeqPath.length() >= length) {
            while (categorySeqPath.length() >= j) {
                list.add(categorySeqPath.substring(i, j));
                i += length;
                j += length;
            }
        }
        return list;
    }

    /**
     * コピー用に半角変換した値を返す
     *
     * @param str 変換対象文字列
     * @return 変換後文字列
     */
    protected String convert(String str) {
        return zenHanConversionUtility.toHankaku(str, new Character[] {'＆', '＜', '＞', '”', '’', '￥'});
    }

    /**
     * ファイル移動処理<br/>
     *
     * @param src       移動元ファイル名
     * @param dest      移動先ファイル名
     * @param removeSrc 元ファイル削除有無
     */
    protected void putFile(String src, String dest, boolean removeSrc) {
        try {
            fileOperationUtility.put(src, dest, removeSrc);
        } catch (IOException e) {
            LOGGER.warn("カテゴリー画像のアップロードに失敗しました。(" + src + ")", e);
        }
    }

    /**
     * 画像ファイルの処理
     *
     * @param model ページ
     */
    public void fileMovement(CategoryInputModel model) {
        String realTmpPath = imageUtility.getRealTempPath();
        String realPath = categoryUtility.getCategoryImageRealPath();

        // PCテンプ画像がある場合は、テンプから画像を移動
        if (model.isTmpImagePC()) {
            String pcFileName =
                            "p_" + model.getCategoryDto().getCategoryEntity().getCategoryId() + model.getFileNamePC()
                                                                                                     .substring(model.getFileNamePC()
                                                                                                                     .lastIndexOf("."));
            putFile(
                            realTmpPath + SEPARATOR + model.getCategoryDto()
                                                           .getCategoryDisplayEntity()
                                                           .getCategoryImagePC(), realPath + pcFileName, true);
            model.getCategoryDto().getCategoryDisplayEntity().setCategoryImagePC(pcFileName);
        } else {
            model.getCategoryDto().getCategoryDisplayEntity().setCategoryImagePC(model.getFileNamePC());
        }
    }

    /**
     * カテゴリDtoクラスに変換
     *
     * @param categoryDto      カテゴリDtoクラス
     * @param categoryResponse カテゴリレスポンス
     */
    public void setCategoryInformation(CategoryDto categoryDto, CategoryResponse categoryResponse) {
        if (categoryResponse == null) {
            categoryResponse = new CategoryResponse();
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategorySeq(categoryResponse.getCategorySeq());
        categoryEntity.setCategoryId(categoryResponse.getCategoryId());
        categoryEntity.setCategoryName(categoryResponse.getCategoryName());
        categoryEntity.setCategoryType(
                        EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, categoryResponse.getCategoryType()));
        categoryEntity.setVersionNo(categoryResponse.getVersionNo());
        categoryEntity.setRegistTime(conversionUtility.toTimestamp(categoryResponse.getRegistTime()));
        categoryEntity.setUpdateTime(conversionUtility.toTimestamp(categoryResponse.getUpdateTime()));
        categoryEntity.setCategoryOpenStartTimePC(
                        conversionUtility.toTimestamp(categoryResponse.getCategoryOpenStartTime()));
        categoryEntity.setCategoryOpenEndTimePC(
                        conversionUtility.toTimestamp(categoryResponse.getCategoryOpenEndTime()));
        categoryEntity.setCategoryOpenStatusPC(
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, categoryResponse.getCategoryOpenStatus()));
        categoryDto.setCategoryEntity(categoryEntity);

        CategoryDisplayEntity categoryDisplayEntity = new CategoryDisplayEntity();
        categoryDisplayEntity.setCategorySeq(categoryResponse.getCategorySeq());
        categoryDisplayEntity.setCategoryNamePC(categoryResponse.getCategoryName());
        categoryDisplayEntity.setMetaDescription(categoryResponse.getMetaDescription());
        categoryDisplayEntity.setMetaKeyword(categoryResponse.getMetaDescription());
        categoryDisplayEntity.setFreeTextPC(categoryResponse.getFreeText());
        categoryDisplayEntity.setCategoryNotePC(categoryResponse.getCategoryNote());
        categoryDisplayEntity.setRegistTime(conversionUtility.toTimestamp(categoryResponse.getRegistTime()));
        categoryDisplayEntity.setUpdateTime(conversionUtility.toTimestamp(categoryResponse.getUpdateTime()));
        categoryDisplayEntity.setCategoryImagePC(categoryResponse.getCategoryImage());
        categoryDto.setCategoryDisplayEntity(categoryDisplayEntity);
        if (ObjectUtils.isNotEmpty(categoryResponse.getFrontDisplay())) {
            categoryDto.setFrontDisplay(EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class,
                                                                      categoryResponse.getFrontDisplay()
                                                                     ));
        }
    }

    /**
     * カテゴリ登録リクエストに変換
     *
     * @param categoryInputModel カテゴリ入力
     * @return categoryRegistRequest カテゴリ登録リクエスト
     */
    protected CategoryRegistRequest toCategoryRegistRequest(CategoryInputModel categoryInputModel) {
        CategoryRegistRequest categoryRegistRequest = new CategoryRegistRequest();

        categoryRegistRequest.setCategoryId(categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryId());
        categoryRegistRequest.setCategoryImage(categoryInputModel.getCategoryImagePC());

        if (categoryInputModel.getCategoryDto() != null) {
            CategoryRequest categoryRequest = new CategoryRequest();

            categoryRequest.setCategoryId(categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryId());
            categoryRequest.setCategorySeq(categoryInputModel.getCategoryDto().getCategoryEntity().getCategorySeq());
            categoryRequest.setCategoryName(categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryName());
            if (categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryOpenStatusPC() != null) {
                categoryRequest.setCategoryOpenStatus(categoryInputModel.getCategoryDto()
                                                                        .getCategoryEntity()
                                                                        .getCategoryOpenStatusPC()
                                                                        .getValue());
            }
            categoryRequest.setCategoryOpenStartTime(
                            categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryOpenStartTimePC());
            categoryRequest.setCategoryOpenEndTime(
                            categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryOpenEndTimePC());
            if (categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryType() != null) {
                categoryRequest.setCategoryType(
                                categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryType().getValue());
            }
            categoryRequest.setVersionNo(categoryInputModel.getCategoryDto().getCategoryEntity().getVersionNo());
            categoryRequest.setOpenGoodsCount(
                            categoryInputModel.getCategoryDto().getCategoryEntity().getOpenGoodsCountPC());

            categoryRegistRequest.setCategoryRequest(categoryRequest);
        }
        if (categoryInputModel.getCategoryDto().getCategoryDisplayEntity() != null) {
            CategoryDisplayRequest categoryDisplayRequest = new CategoryDisplayRequest();

            categoryDisplayRequest.setCategorySeq(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategorySeq());
            categoryDisplayRequest.setCategoryName(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategoryNamePC());
            categoryDisplayRequest.setCategoryNote(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategoryNotePC());
            categoryDisplayRequest.setFreeText(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getFreeTextPC());
            categoryDisplayRequest.setMetaDescription(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getMetaDescription());
            categoryDisplayRequest.setCategoryImage(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategoryImagePC());

            categoryRegistRequest.setCategoryDisplayRequest(categoryDisplayRequest);
        }

        if (HTypeCategoryType.AUTO.equals(categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryType())) {
            CategoryConditionRegistUpdateRequest categoryConditionRegistUpdateRequest =
                            new CategoryConditionRegistUpdateRequest();
            if (categoryInputModel.getCategoryConditionType() != null) {
                categoryConditionRegistUpdateRequest.setConditionType(categoryInputModel.getCategoryConditionType());
            }
            if (categoryInputModel.getCategoryConditionItems() != null) {
                categoryConditionRegistUpdateRequest.setConditionDetailList(
                                toCategoryConditionDetailRegistUpdateRequestList(
                                                categoryInputModel.getCategoryConditionItems()));
            }

            categoryRegistRequest.setCategoryCondition(categoryConditionRegistUpdateRequest);
        }

        categoryRegistRequest.getCategoryDisplayRequest()
                             .setCategoryImage(this.updateCategoryImagePath(categoryInputModel.getCategoryDto()
                                                                                              .getCategoryDisplayEntity()
                                                                                              .getCategoryImagePC()));

        return categoryRegistRequest;
    }

    /**
     * カテゴリ更新リクエストに変換
     *
     * @param categoryInputModel カテゴリ入力
     * @return CategoryUpdateRequest カテゴリ更新リクエスト
     */
    protected CategoryUpdateRequest toCategoryUpdateRequest(CategoryInputModel categoryInputModel) {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest();

        if (categoryInputModel.getCategoryDto().getCategoryEntity() != null) {
            CategoryEntity categoryEntity = categoryInputModel.getCategoryDto().getCategoryEntity();
            categoryUpdateRequest.setCategorySeq(categoryEntity.getCategorySeq());
            categoryUpdateRequest.setCategoryName(categoryEntity.getCategoryName());
            if (categoryEntity.getCategoryOpenStatusPC() != null) {
                categoryUpdateRequest.setCategoryOpenStatus(categoryEntity.getCategoryOpenStatusPC().getValue());
            }
            categoryUpdateRequest.setCategoryOpenStartTime(categoryEntity.getCategoryOpenStartTimePC());
            categoryUpdateRequest.setCategoryOpenEndTime(categoryEntity.getCategoryOpenEndTimePC());
            categoryUpdateRequest.setVersionNo(categoryEntity.getVersionNo());
            categoryUpdateRequest.setOpenGoodsCount(categoryEntity.getOpenGoodsCountPC());
        }

        if (categoryInputModel.getCategoryDto().getCategoryDisplayEntity() != null) {
            CategoryDisplayRequest categoryDisplayRequest = new CategoryDisplayRequest();

            categoryDisplayRequest.setCategorySeq(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategorySeq());
            categoryDisplayRequest.setCategoryName(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategoryNamePC());
            categoryDisplayRequest.setCategoryNote(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategoryNotePC());
            categoryDisplayRequest.setFreeText(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getFreeTextPC());
            categoryDisplayRequest.setMetaDescription(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getMetaDescription());
            categoryDisplayRequest.setCategoryImage(
                            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().getCategoryImagePC());

            categoryUpdateRequest.setCategoryDisplayRequest(categoryDisplayRequest);
        }

        if (HTypeCategoryType.AUTO.equals(categoryInputModel.getCategoryDto().getCategoryEntity().getCategoryType())) {
            CategoryConditionRegistUpdateRequest categoryConditionRegistUpdateRequest =
                            new CategoryConditionRegistUpdateRequest();
            if (categoryInputModel.getCategoryConditionType() != null) {
                categoryConditionRegistUpdateRequest.setConditionType(categoryInputModel.getCategoryConditionType());
            }
            if (categoryInputModel.getCategoryConditionItems() != null) {
                categoryConditionRegistUpdateRequest.setConditionDetailList(
                                toCategoryConditionDetailRegistUpdateRequestList(
                                                categoryInputModel.getCategoryConditionItems()));
            }

            categoryUpdateRequest.setCategoryCondition(categoryConditionRegistUpdateRequest);
        }

        if (StringUtils.isNotEmpty(categoryInputModel.getGoodsSortColumn())) {
            CategoryGoodsManagementRegistUpdateRequest categoryGoodsManagementRegistUpdateRequest =
                            new CategoryGoodsManagementRegistUpdateRequest();
            if (HTypeCategoryGoodsSort.NEW_ITEM.getValue().equals(categoryInputModel.getGoodsSortDisplay())
                || HTypeCategoryGoodsSort.OLD_ITEM.getValue().equals(categoryInputModel.getGoodsSortDisplay())) {
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortColumn(HTypeSortByType.NEW.getValue());
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortOrder(HTypeCategoryGoodsSort.OLD_ITEM.getValue()
                                                                                                            .equals(categoryInputModel.getGoodsSortDisplay()));
            } else if (HTypeCategoryGoodsSort.HIGH_PRICE.getValue().equals(categoryInputModel.getGoodsSortDisplay())
                       || HTypeCategoryGoodsSort.LOW_PRICE.getValue()
                                                          .equals(categoryInputModel.getGoodsSortDisplay())) {
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortColumn(HTypeSortByType.PRICE.getValue());
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortOrder(HTypeCategoryGoodsSort.LOW_PRICE.getValue()
                                                                                                             .equals(categoryInputModel.getGoodsSortDisplay()));
            } else if (HTypeCategoryGoodsSort.MANUAL.getValue().equals(categoryInputModel.getGoodsSortDisplay())) {
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortColumn(HTypeSortByType.RECOMMEND.getValue());
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortOrder(true);
            } else {
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortColumn(HTypeSortByType.POPULARITY.getValue());
                categoryGoodsManagementRegistUpdateRequest.setGoodsSortOrder(false);
            }

            List<CategoryGoodsRegistUpdateRequest> categoryGoodsRegistUpdateRequestList = new ArrayList<>();
            int index = 1;

            if (CollectionUtils.isNotEmpty(categoryInputModel.getGoodsGroupSeqSort())) {

                for (Integer goodsGroupSeq : categoryInputModel.getGoodsGroupSeqSort()) {
                    CategoryGoodsRegistUpdateRequest categoryGoodsRegistUpdateRequest =
                                    new CategoryGoodsRegistUpdateRequest();
                    categoryGoodsRegistUpdateRequest.setGoodsGroupSeq(goodsGroupSeq);
                    categoryGoodsRegistUpdateRequest.setManualOrderDisplay(index++);

                    categoryGoodsRegistUpdateRequestList.add(categoryGoodsRegistUpdateRequest);
                }
            } else {
                for (GoodsModelItem goodsModelItem : categoryInputModel.getGoodsModelItems()) {
                    CategoryGoodsRegistUpdateRequest categoryGoodsRegistUpdateRequest =
                                    new CategoryGoodsRegistUpdateRequest();
                    categoryGoodsRegistUpdateRequest.setGoodsGroupSeq(goodsModelItem.getGoodsGroupSeq());
                    categoryGoodsRegistUpdateRequest.setManualOrderDisplay(index++);

                    categoryGoodsRegistUpdateRequestList.add(categoryGoodsRegistUpdateRequest);
                }
            }
            categoryGoodsManagementRegistUpdateRequest.setManualyRegistGoodsList(categoryGoodsRegistUpdateRequestList);
            categoryUpdateRequest.setCategoryGoodsManagement(categoryGoodsManagementRegistUpdateRequest);
        }

        return categoryUpdateRequest;
    }

    /**
     * カテゴリ条件詳細クラスリストに変換
     *
     * @param categoryConditionItems カテゴリ条件管理
     * @return カテゴリ条件詳細クラスリスト
     */
    protected List<CategoryConditionDetailRegistUpdateRequest> toCategoryConditionDetailRegistUpdateRequestList(List<CategoryConditionItem> categoryConditionItems) {

        if (categoryConditionItems == null) {
            return null;
        }

        List<CategoryConditionDetailRegistUpdateRequest> categoryConditionDetailRegistUpdateRequestList =
                        new ArrayList<>();

        for (CategoryConditionItem categoryConditionItem : categoryConditionItems) {
            CategoryConditionDetailRegistUpdateRequest categoryConditionDetailRegistUpdateRequest =
                            new CategoryConditionDetailRegistUpdateRequest();
            categoryConditionDetailRegistUpdateRequest.setConditionNo(categoryConditionItem.getConditionNo());
            categoryConditionDetailRegistUpdateRequest.setConditionColumn(categoryConditionItem.getConditionColumn());
            categoryConditionDetailRegistUpdateRequest.setConditionOperator(
                            categoryConditionItem.getConditionOperator());
            if (HTypeConditionOperatorType.SETTING.getValue().equals(categoryConditionItem.getConditionOperator())
                || HTypeConditionOperatorType.NO_SETTING.getValue()
                                                        .equals(categoryConditionItem.getConditionOperator())) {
                categoryConditionDetailRegistUpdateRequest.setConditionValue("");
            } else {
                categoryConditionDetailRegistUpdateRequest.setConditionValue(categoryConditionItem.getConditionValue());
            }

            categoryConditionDetailRegistUpdateRequestList.add(categoryConditionDetailRegistUpdateRequest);
        }

        return categoryConditionDetailRegistUpdateRequestList;
    }

    /**
     * 画像パスを更新する
     *
     * @param path カテゴリ画像相対パス
     * @return 元の画像パス
     */
    protected String updateCategoryImagePath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        String imagePath = PropertiesUtil.getSystemPropertiesValue("images.path.category");
        return path.replaceAll(imagePath + SEPARATOR, "");
    }

    /**
     * 商品モデル項目リストに変換
     *
     * @param categoryItemListResponse カテゴリ内商品一覧レスポンス
     * @return 商品モデル項目リスト
     */
    public List<GoodsModelItem> toGoodsModelItemList(CategoryItemListResponse categoryItemListResponse) {

        if (CollectionUtils.isEmpty(categoryItemListResponse.getCategoryItemList())) {
            return new ArrayList<>();
        }

        return categoryItemListResponse.getCategoryItemList().stream().map(categoryItemResponse -> {
            GoodsModelItem goodsModelItem = new GoodsModelItem();

            goodsModelItem.setCategorySeq(categoryItemResponse.getCategorySeq());
            goodsModelItem.setGoodsGroupSeq(categoryItemResponse.getGoodsGroupSeq());
            goodsModelItem.setManualOrderDisplay(categoryItemResponse.getManualOrderDisplay());
            goodsModelItem.setGoodsGroupCode(categoryItemResponse.getGoodsGroupCode());
            goodsModelItem.setGoodsGroupName(categoryItemResponse.getGoodsGroupName());
            goodsModelItem.setCategoryId(categoryItemResponse.getCategoryId());
            goodsModelItem.setFrontDisplay(categoryItemResponse.getFrontDisplay());
            goodsModelItem.setGoodsPrice(categoryItemResponse.getGoodsPrice());
            goodsModelItem.setWhatsnewDate(categoryItemResponse.getWhatsnewDate());
            goodsModelItem.setPopularityCount(categoryItemResponse.getPopularityCount());

            return goodsModelItem;
        }).collect(Collectors.toList());
    }

    /**
     * カテゴリ条件項目りすとに変換
     *
     * @param categoryConditionResponse カテゴリ条件クラス
     * @return カテゴリ条件項目リスト
     */
    public List<CategoryConditionItem> toCategoryConditionItems(CategoryConditionResponse categoryConditionResponse) {

        if (CollectionUtils.isEmpty(categoryConditionResponse.getConditionDetailList())) {
            return new ArrayList<>();
        }

        return categoryConditionResponse.getConditionDetailList().stream().map(categoryConditionDetail -> {
            CategoryConditionItem categoryConditionItem = new CategoryConditionItem();

            categoryConditionItem.setConditionNo(categoryConditionDetail.getConditionNo());
            categoryConditionItem.setConditionColumn(categoryConditionDetail.getConditionColumn());
            categoryConditionItem.setConditionOperator(categoryConditionDetail.getConditionOperator());
            categoryConditionItem.setConditionValue(categoryConditionDetail.getConditionValue());

            return categoryConditionItem;
        }).collect(Collectors.toList());
    }

    /**
     * カテゴリ商品な並べ替えを設定する
     *
     * @param categoryInputModel カテゴリ入力
     * @param sortColumn         商品並び順項目
     * @param sortOrder          商品並び順
     */
    public void setCategoryGoodSort(CategoryInputModel categoryInputModel, String sortColumn, Boolean sortOrder) {
        categoryInputModel.setGoodsSortColumn(sortColumn);
        categoryInputModel.setGoodsSortOrder(sortOrder);
        categoryInputModel.setGoodsSortDisplay(getGoodsSortDisplay(sortColumn, sortOrder));
    }

    /**
     * 品並び順表示に変換
     *
     * @param sortColumn 商品並び順項目
     * @param sortOrder  商品並び順
     * @return 品並び順表示
     */
    protected String getGoodsSortDisplay(String sortColumn, Boolean sortOrder) {
        if (HTypeSortByType.NEW.getValue().equals(sortColumn)) {
            return sortOrder ? HTypeCategoryGoodsSort.OLD_ITEM.getValue() : HTypeCategoryGoodsSort.NEW_ITEM.getValue();
        } else if (HTypeSortByType.PRICE.getValue().equals(sortColumn)) {
            return sortOrder ?
                            HTypeCategoryGoodsSort.LOW_PRICE.getValue() :
                            HTypeCategoryGoodsSort.HIGH_PRICE.getValue();
        } else if (HTypeSortByType.RECOMMEND.getValue().equals(sortColumn)) {
            return HTypeCategoryGoodsSort.MANUAL.getValue();
        } else {
            return HTypeCategoryGoodsSort.POPULARITY.getValue();
        }
    }

    /**
     * 商品のソート
     *
     * @param optionSort         オプションソート
     * @param goodsGroupChecked  商品グループチェック済み
     * @param position           位置
     * @param optionSortManual   オプション 手動ソート
     * @param categoryInputModel カテゴリ入力
     */
    public void sortGoods(String optionSort,
                          List<Integer> goodsGroupChecked,
                          Integer position,
                          String optionSortManual,
                          CategoryInputModel categoryInputModel) {

        List<GoodsModelItem> goodsModelItems = categoryInputModel.getGoodsModelItems();

        if (CollectionUtils.isNotEmpty(goodsModelItems)) {
            HTypeCategoryGoodsSort goodsSort = EnumTypeUtil.getEnumFromValue(HTypeCategoryGoodsSort.class, optionSort);

            if (HTypeCategoryGoodsSort.POPULARITY.equals(goodsSort)) {
                goodsModelItems.sort(Comparator.comparing(GoodsModelItem::getPopularityCount).reversed());
            } else if (HTypeCategoryGoodsSort.NEW_ITEM.equals(goodsSort)) {
                goodsModelItems.sort(Comparator.comparing(GoodsModelItem::getWhatsnewDate).reversed());
            } else if (HTypeCategoryGoodsSort.OLD_ITEM.equals(goodsSort)) {
                goodsModelItems.sort(Comparator.comparing(GoodsModelItem::getWhatsnewDate));
            } else if (HTypeCategoryGoodsSort.HIGH_PRICE.equals(goodsSort)) {
                goodsModelItems.sort(Comparator.comparing(GoodsModelItem::getGoodsPrice).reversed());
            } else if (HTypeCategoryGoodsSort.LOW_PRICE.equals(goodsSort)) {
                goodsModelItems.sort(Comparator.comparing(GoodsModelItem::getGoodsPrice));
            } else if (HTypeCategoryGoodsSort.MANUAL.equals(goodsSort)) {
                HTypeCategoryGoodsManualSort goodsManualSort =
                                EnumTypeUtil.getEnumFromValue(HTypeCategoryGoodsManualSort.class, optionSortManual);
                if (HTypeCategoryGoodsManualSort.TOP.equals(goodsManualSort)) {
                    position = 0;
                } else if (HTypeCategoryGoodsManualSort.BOTTOM.equals(goodsManualSort)) {
                    position = goodsModelItems.size() - goodsGroupChecked.size();
                } else {
                    position--;
                }

                List<GoodsModelItem> goodsModelItemNews = new ArrayList<>();
                Map<Integer, GoodsModelItem> goodsModelItemMap = new HashMap<>();

                goodsModelItems.forEach(goods -> {
                    if (goodsGroupChecked.contains(goods.getGoodsGroupSeq())) {
                        goodsModelItemMap.put(goods.getGoodsGroupSeq(), goods);
                    } else {
                        goodsModelItemNews.add(goods);
                    }
                });

                if (position > goodsModelItemNews.size()) {
                    position = goodsModelItemNews.size();
                }

                for (int i = 0; i <= goodsModelItemNews.size(); i++) {
                    if (i == position) {
                        for (Integer checked : goodsGroupChecked) {
                            goodsModelItemNews.add(i++, goodsModelItemMap.get(checked));
                        }
                        break;
                    }
                }

                categoryInputModel.setGoodsModelItems(goodsModelItemNews);
            }
        }
    }

    /**
     * 新しく追加された商品を並べ替える
     *
     * @param optionSort        オプションソート
     * @param goodsModelItems   商品グループチェック済み
     */
    public List<GoodsModelItem> sortGoodsForNewlyAdded(String optionSort, List<GoodsModelItem> goodsModelItems) {
        if (CollectionUtils.isNotEmpty(goodsModelItems)) {
            HTypeCategoryGoodsSort goodsSort = EnumTypeUtil.getEnumFromValue(HTypeCategoryGoodsSort.class, optionSort);

            if (HTypeCategoryGoodsSort.POPULARITY.equals(goodsSort)) {
                return goodsModelItems.stream()
                                      .sorted(Comparator.comparing(GoodsModelItem::getPopularityCount).reversed())
                                      .collect(Collectors.toList());
            } else if (HTypeCategoryGoodsSort.NEW_ITEM.equals(goodsSort)) {
                return goodsModelItems.stream()
                                      .sorted(Comparator.comparing(GoodsModelItem::getWhatsnewDate).reversed())
                                      .collect(Collectors.toList());
            } else if (HTypeCategoryGoodsSort.OLD_ITEM.equals(goodsSort)) {
                return goodsModelItems.stream()
                                      .sorted(Comparator.comparing(GoodsModelItem::getWhatsnewDate))
                                      .collect(Collectors.toList());
            } else if (HTypeCategoryGoodsSort.HIGH_PRICE.equals(goodsSort)) {
                return goodsModelItems.stream()
                                      .sorted(Comparator.comparing(GoodsModelItem::getGoodsPrice).reversed())
                                      .collect(Collectors.toList());
            } else if (HTypeCategoryGoodsSort.LOW_PRICE.equals(goodsSort)) {
                return goodsModelItems.stream()
                                      .sorted(Comparator.comparing(GoodsModelItem::getGoodsPrice))
                                      .collect(Collectors.toList());
            }
        }

        return goodsModelItems;
    }

    /**
     * 商品削除
     *
     * @param goodsGroupSeq      商品グループSEQ
     * @param goodsModelItemList 商品モデルアイテムリスト
     */
    public void removeGoods(Integer goodsGroupSeq, List<GoodsModelItem> goodsModelItemList) {
        for (GoodsModelItem goodsModelItem : goodsModelItemList) {
            if (goodsModelItem.getGoodsGroupSeq().equals(goodsGroupSeq)) {
                goodsModelItemList.remove(goodsModelItem);
                break;
            }
        }
    }

    /**
     * カテゴリ条件を追加
     *
     * @param categoryInputModel 　カテゴリ入力
     */
    public void addCategoryCondition(CategoryInputModel categoryInputModel) {
        if (categoryInputModel.getCategoryConditionItems() == null) {
            categoryInputModel.setCategoryConditionItems(new ArrayList<>());
        }
        categoryInputModel.getCategoryConditionItems().add(new CategoryConditionItem());
    }

    /**
     * カテゴリ条件を削除
     *
     * @param categoryInputModel 　カテゴリ入力
     * @param index              　インデックス
     */
    public void removeCategoryCondition(CategoryInputModel categoryInputModel, Integer index) {
        for (int i = 0; i < categoryInputModel.getCategoryConditionItems().size(); i++) {
            if (i == index) {
                categoryInputModel.getCategoryConditionItems().remove(i);
                break;
            }
        }
    }

    /**
     * 画面初期描画時に任意必須項目のデフォルト値を設定<br/>
     * 新規登録/更新に関わらず、モデルに設定されていない場合はデフォルト値を設定
     *
     * @param categoryInputModel
     */
    public void setDefaultValueForLoad(CategoryInputModel categoryInputModel) {
        // 公開状態プルダウンのデフォルト値を設定
        if (StringUtils.isEmpty(categoryInputModel.getCategoryOpenStatusPC())) {
            categoryInputModel.setCategoryOpenStatusPC(HTypeOpenStatus.NO_OPEN.getValue());
        }

        CategoryConditionItem categoryConditionItem = new CategoryConditionItem();
        List<CategoryConditionItem> categoryConditionItems = new ArrayList<>();

        categoryConditionItem.setConditionColumn(HTypeConditionColumnType.GOOD_TAG.getValue());
        categoryConditionItem.setConditionOperator(HTypeConditionOperatorType.MATCH.getValue());

        categoryConditionItems.add(categoryConditionItem);

        categoryInputModel.setCategoryConditionItems(categoryConditionItems);
    }

    /**
     * プレビュー日時が未設定の場合、現在日時を画面にセット
     *
     * @param categoryInputModel
     */
    protected void setPreviewForm(CategoryInputModel categoryInputModel) {

        if (org.apache.commons.lang3.StringUtils.isBlank(categoryInputModel.getPreviewDate())) {
            categoryInputModel.setPreviewDate(this.dateUtility.getCurrentYmdWithSlash());
            categoryInputModel.setPreviewTime(this.dateUtility.getCurrentHMS());
        }
        // 日付は設定されているが、時刻未設定の場合
        else if (org.apache.commons.lang3.StringUtils.isNotBlank(categoryInputModel.getPreviewDate())
                 && org.apache.commons.lang3.StringUtils.isBlank(categoryInputModel.getPreviewTime())) {
            categoryInputModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }
    }

    /**
     * 商品グループ検索条件リクエストに変換
     *
     * @param goodsSearchModel 自画面Model
     * @return 商品グループ検索条件リクエスト
     */
    public ProductListGetRequest toGoodsSearchForBackDaoConditionDtoForProductsSearchAjax(GoodsSearchModel goodsSearchModel) {

        ProductListGetRequest productListGetRequest = new ProductListGetRequest();

        // キーワード
        if (goodsSearchModel.getSearchKeyword() != null) {

            // 全角変換
            String keywordString = (String) zenHanConversionUtility.toZenkaku(goodsSearchModel.getSearchKeyword());

            // 大文字変換
            keywordString = org.apache.commons.lang3.StringUtils.upperCase(keywordString);

            String[] searchKeywordArray = keywordString.split("[\\s|　]+");

            for (int i = 0; i < searchKeywordArray.length; i++) {
                switch (i) {
                    case 0:
                        productListGetRequest.setKeywordLikeCondition1(searchKeywordArray[i].trim());
                        break;
                    case 1:
                        productListGetRequest.setKeywordLikeCondition2(searchKeywordArray[i].trim());
                        break;
                    case 2:
                        productListGetRequest.setKeywordLikeCondition3(searchKeywordArray[i].trim());
                        break;
                    case 3:
                        productListGetRequest.setKeywordLikeCondition4(searchKeywordArray[i].trim());
                        break;
                    case 4:
                        productListGetRequest.setKeywordLikeCondition5(searchKeywordArray[i].trim());
                        break;
                    case 5:
                        productListGetRequest.setKeywordLikeCondition6(searchKeywordArray[i].trim());
                        break;
                    case 6:
                        productListGetRequest.setKeywordLikeCondition7(searchKeywordArray[i].trim());
                        break;
                    case 7:
                        productListGetRequest.setKeywordLikeCondition8(searchKeywordArray[i].trim());
                        break;
                    case 8:
                        productListGetRequest.setKeywordLikeCondition9(searchKeywordArray[i].trim());
                        break;
                    case 9:
                        productListGetRequest.setKeywordLikeCondition10(searchKeywordArray[i].trim());
                        break;
                    default:
                        // この処理は到達しない
                }
            }
        }

        return productListGetRequest;
    }

}