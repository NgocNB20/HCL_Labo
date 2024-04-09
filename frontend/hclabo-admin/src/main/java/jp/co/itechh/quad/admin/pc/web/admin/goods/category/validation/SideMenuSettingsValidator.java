package jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.admin.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.CategoryTreeErrorItem;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.SideMenuSettingsModel;
import jp.co.itechh.quad.admin.validator.HSpecialCharacterValidator;
import lombok.Data;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import javax.validation.Validation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * サイドメニュー設定バリデータ
 *
 * @author Doan Thang (VJP)
 */
@Data
@Component
@Scope("prototype")
public class SideMenuSettingsValidator implements SmartValidator {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SideMenuSettingsValidator.class);

    /** 最小入力文字数 */
    private static final int MIN_LENGTH = 1;

    /** 最大入力文字数 */
    private static final int MAX_LENGTH = 120;

    /** エラーコード：必須 */
    private static final String NOT_EMPTY_MSG_ID = "javax.validation.constraints.NotEmpty.message";

    /** エラーコード：入力文字数 */
    private static final String LENGTH_MSG_ID = "org.hibernate.validator.constraints.Length.message";

    /** エラーコード：使用できない文字 */
    private static final String SPECIAL_CHARACTER_MSG_ID = "HSpecialCharacterValidator.INVALID_detail";

    /** インデックス */
    private static int index;

    /** エラーコード：必須 メッセージ */
    private String notEmptyMsg;

    /** エラーコード：入力文字数 メッセージ */
    private String lengthMsg;

    /** エラーコード：使用できない文字 メッセージ */
    private String specialCharacterMsg;

    /** カテゴリーツリーエラーアイテムリスト */
    private List<CategoryTreeErrorItem> categoryTreeErrorItemList;

    /** 特殊文字チェックバリデータ */
    private HSpecialCharacterValidator validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return SideMenuSettingsModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!ConfirmGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、ConfirmGroup以外の場合、チェックしない
            return;
        }

        // チェック対象取得
        SideMenuSettingsModel model = (SideMenuSettingsModel) target;
        if (ObjectUtils.isEmpty(model.getSideMenuSettingsList())) {
            return;
        }
        String jsonData = model.getSideMenuSettingsList();
        ObjectMapper objectMapper =
                        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ;
        CategoryTreeDto categoryTreeDto;
        try {
            categoryTreeDto = objectMapper.readValue(jsonData, CategoryTreeDto.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("例外処理が発生しました", e);
            return;
        }
        if (ObjectUtils.isEmpty(categoryTreeDto)) {
            return;
        }
        model.setCategoryTreeDtoList(categoryTreeDto.getCategoryTreeDtoList());
        categoryTreeErrorItemList = new ArrayList<>();

        // 特殊文字チェックバリデータ
        validator = Validation.buildDefaultValidatorFactory()
                              .getConstraintValidatorFactory()
                              .getInstance(HSpecialCharacterValidator.class);
        validator.setAllowPunctuation(true);

        // 必須チェック
        index = 0;
        notEmptyMsg = AppLevelFacesMessageUtil.getAllMessage(NOT_EMPTY_MSG_ID, null).getMessage();
        int emptyErrorCount = checkEmpty(categoryTreeDto.getCategoryTreeDtoList());
        if (emptyErrorCount > 0) {
            errors.reject(NOT_EMPTY_MSG_ID);
        }

        // 入力文字数チェック
        index = 0;
        // プロパティファイルからメッセージを取得
        String template = PropertiesUtil.getSystemPropertiesValue(LENGTH_MSG_ID);
        Map<String, Integer> args = new HashMap<>();
        args.put("min", MIN_LENGTH);
        args.put("max", MAX_LENGTH);
        lengthMsg = StrSubstitutor.replace(template, args, "{", "}");
        int lengthErrorCount = checkLength(categoryTreeDto.getCategoryTreeDtoList());
        if (lengthErrorCount > 0) {
            errors.reject(LENGTH_MSG_ID);
        }

        // 特殊文字チェック
        index = 0;
        specialCharacterMsg = AppLevelFacesMessageUtil.getAllMessage(SPECIAL_CHARACTER_MSG_ID, null).getMessage();
        int specialCharacterErrorCount = checkSpecialCharacter(categoryTreeDto.getCategoryTreeDtoList());
        if (specialCharacterErrorCount > 0) {
            errors.reject(SPECIAL_CHARACTER_MSG_ID);
        }

        // Indexでエラーリストソート
        categoryTreeErrorItemList.sort(Comparator.comparing(CategoryTreeErrorItem::getIndex));
        model.setCategoryTreeErrorItemList(categoryTreeErrorItemList);
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }

    /**
     * 必須チェック
     *
     * @param categoryTreeDtoList テゴリー木構造Dtoリスト
     * @return エラーカウンター
     */
    private int checkEmpty(List<CategoryTreeDto> categoryTreeDtoList) {
        int errorCount = 0;
        for (CategoryTreeDto treeDto : categoryTreeDtoList) {
            if (StringUtils.isEmpty(treeDto.getDisplayName())) {
                treeDto.setError("error");
                errorCount++;
                CategoryTreeErrorItem categoryTreeErrorItem = new CategoryTreeErrorItem();
                categoryTreeErrorItem.setIndex(index);
                categoryTreeErrorItem.setMsg(notEmptyMsg);
                categoryTreeErrorItemList.add(categoryTreeErrorItem);
            }
            ++index;
            if (treeDto.getCategoryTreeDtoList() != null) {
                errorCount += checkEmpty(treeDto.getCategoryTreeDtoList());
            }
        }
        return errorCount;
    }

    /**
     * 入力文字数チェック
     *
     * @param categoryTreeDtoList テゴリー木構造Dtoリスト
     * @return エラーカウンター
     */
    private int checkLength(List<CategoryTreeDto> categoryTreeDtoList) {
        int errorCount = 0;
        for (CategoryTreeDto treeDto : categoryTreeDtoList) {
            if (StringUtils.isNotEmpty(treeDto.getDisplayName()) && treeDto.getDisplayName().length() > MAX_LENGTH) {
                treeDto.setDisplayName(treeDto.getDisplayName().substring(0, MAX_LENGTH - 1));
                treeDto.setError("error");
                errorCount++;
                CategoryTreeErrorItem categoryTreeErrorItem = new CategoryTreeErrorItem();
                categoryTreeErrorItem.setIndex(index);
                categoryTreeErrorItem.setMsg(lengthMsg);
                categoryTreeErrorItemList.add(categoryTreeErrorItem);
            }
            ++index;
            if (treeDto.getCategoryTreeDtoList() != null) {
                errorCount += checkLength(treeDto.getCategoryTreeDtoList());
            }
        }
        return errorCount;
    }

    /**
     * 特殊文字チェック
     *
     * @param categoryTreeDtoList テゴリー木構造Dtoリスト
     * @return エラーカウンター
     */
    private int checkSpecialCharacter(List<CategoryTreeDto> categoryTreeDtoList) {
        int errorCount = 0;
        for (CategoryTreeDto treeDto : categoryTreeDtoList) {
            if (StringUtils.isNotEmpty(treeDto.getDisplayName()) && !validator.isValid(
                            treeDto.getDisplayName(), null)) {
                treeDto.setError("error");
                errorCount++;
                CategoryTreeErrorItem categoryTreeErrorItem = new CategoryTreeErrorItem();
                categoryTreeErrorItem.setIndex(index);
                categoryTreeErrorItem.setMsg(specialCharacterMsg);
                categoryTreeErrorItemList.add(categoryTreeErrorItem);
            }
            ++index;
            if (treeDto.getCategoryTreeDtoList() != null) {
                errorCount += checkSpecialCharacter(treeDto.getCategoryTreeDtoList());
            }
        }
        return errorCount;
    }
}