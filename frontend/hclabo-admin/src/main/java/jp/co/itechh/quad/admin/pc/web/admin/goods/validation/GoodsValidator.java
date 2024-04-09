package jp.co.itechh.quad.admin.pc.web.admin.goods.validation;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.GoodsModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;

@Data
@Component
public class GoodsValidator implements SmartValidator {

    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED = "HRequiredValidator.REQUIRED_detail";
    public static final String MSGCD_MUILT_CODE_SEARH = "HRequiredValidator.MUILT_SEARCH_CODE_detail";
    public static final String MSGCD_KEYWORD_LENGTH = "HRequiredValidator.REQUIRED_detail";
    public static final String MSGCD_NOT_EMPTY = "javax.validation.constraints.NotEmpty.message";
    public static final String MSGCD_NOT_EMPTY_BLANK = "REQUIRED-EMPTY-MSG";
    public static final String MSGCD_NUMBER_OF_DIGITS = "SEARCH-GOODS-TAG-001";

    /** 登録日／更新日選択フラグ */
    public static final String FILED_NAME_PERIOD_TYPE = "selectRegistOrUpdate";
    public static final String FILED_NAME_DATE_FROM = "searchRegOrUpTimeFrom";
    public static final String FILED_NAME_DATE_TO = "searchRegOrUpTimeTo";
    public static final String FILED_NAME_GOODS_TAG = "searchGoodsTag";

    public static final String FILED_NAME_SEARCH_CODE = "multiCodeRadio";

    public static final String FILED_NAME_SEARCH_AREA = "multiCodeList";

    public static final String FILED_SETTING_KEYWORD = "settingKeywords";

    /** 商品グループコード */
    private final static String GOODS_GROUP_CODE_RADIO = "0";

    /** 商品番号 */
    private final static String GOODS_CODE_RADIO = "1";

    /** JANコード */
    private final static String JAN_CODE_RADIO = "2";

    @Override
    public boolean supports(Class<?> clazz) {
        return GoodsModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!SearchGroup.class.equals(validationHints[0]) && !AllDownloadGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、SearchGroupとAllDownloadGroup以外の場合、チェックしない
            return;
        }

        GoodsModel model = (GoodsModel) target;

        // 更新／登録日バリデータ
        if (!StringUtils.isEmpty(model.getSearchRegOrUpTimeFrom()) || !StringUtils.isEmpty(
                        model.getSearchRegOrUpTimeTo())) {
            if (StringUtils.isEmpty(model.getSelectRegistOrUpdate())) {
                errors.rejectValue(FILED_NAME_PERIOD_TYPE, MSGCD_REQUIRED);
            }
        }
        if ("0".equals(model.getSelectRegistOrUpdate()) || "1".equals(model.getSelectRegistOrUpdate())) {
            if (StringUtils.isEmpty(model.getSearchRegOrUpTimeFrom()) && StringUtils.isEmpty(
                            model.getSearchRegOrUpTimeTo())) {
                // 登録日or更新日のラジオボタンが選択されているが、期間が設定されていない場合はラジオボタンをクリアする ※チェックはしない
                model.setSelectRegistOrUpdate(null);
            }
        }

        if (!StringUtils.isEmpty(model.getSearchGoodsTag())) {
            String[] goodsTagArray = model.getSearchGoodsTag().split("[\\s|　]+");

            for (String goodsTag : goodsTagArray) {
                // １００桁を超えている場合はエラー
                if (goodsTag.length() > 100) {
                    errors.rejectValue(FILED_NAME_GOODS_TAG, MSGCD_NUMBER_OF_DIGITS);
                    break;
                }
            }
        }

        // 複数番号検索バリデータ
        if (!StringUtils.isEmpty(model.getMultiCodeList())) {
            if (StringUtils.isEmpty(model.getMultiCodeRadio())) {
                errors.rejectValue(FILED_NAME_SEARCH_CODE, MSGCD_REQUIRED);
            }
        }

        if (GOODS_GROUP_CODE_RADIO.equals(model.getMultiCodeRadio()) || GOODS_CODE_RADIO.equals(
                        model.getMultiCodeRadio()) || JAN_CODE_RADIO.equals(model.getMultiCodeRadio())) {
            if (StringUtils.isEmpty(model.getMultiCodeList())) {
                model.setMultiCodeRadio(null);
            }
        }

        checkDataBeforeSearch(target, errors, validationHints);
    }

    /**
     * 検索前　入力情報チェック<br/>
     * Validatorで対応できないもの
     *
     * @param target
     * @param errors
     */
    public void checkDataBeforeSearch(Object target, Errors errors, Object... validationHints) {

        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!SearchGroup.class.equals(validationHints[0]) && !AllDownloadGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、SearchGroupとAllDownloadGroup以外の場合、チェックしない
            return;
        }

        GoodsModel model = (GoodsModel) target;

        // 複数番号（複数番号検索用）件数制限チェック
        if (model.getMultiCodeList() != null && StringUtil.isNotEmpty(
                        model.getMultiCodeList().replaceAll("[\\s|　]", ""))) {
            // 検索に有効な文字列が存在する場合
            // 複数番号（複数番号検索用）最大件数を設定ファイルから取得
            int searchMultiCodeMaxCount = Integer.parseInt(
                            PropertiesUtil.getSystemPropertiesValue("goods.search.multi.code.maxcount"));
            // 選択肢区切り文字を設定ファイルから取得
            String searchMultiCodeDivchar = PropertiesUtil.getSystemPropertiesValue("goods.search.multi.code.divchar");
            String searchMultiCode = model.getMultiCodeList();
            // 空白を削除する
            searchMultiCode = searchMultiCode.replaceAll("[ 　\t\\x0B\f]", "");
            // 2つ以上連続した改行コードを1つにまとめる
            searchMultiCode = searchMultiCode.replaceAll("(" + searchMultiCodeDivchar + "){2,}", "\n");
            // 先頭または最後尾の改行コードを削除する
            searchMultiCode = searchMultiCode.replaceAll(
                            "^[" + searchMultiCodeDivchar + "]+|[" + searchMultiCodeDivchar + "]$", "");
            // 検索用複数番号を配列化する
            String[] searchMultiCodeArray = searchMultiCode.split(searchMultiCodeDivchar);
            if (searchMultiCodeArray.length > searchMultiCodeMaxCount) {
                errors.rejectValue(FILED_NAME_SEARCH_AREA, MSGCD_MUILT_CODE_SEARH);
            }
        }
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}