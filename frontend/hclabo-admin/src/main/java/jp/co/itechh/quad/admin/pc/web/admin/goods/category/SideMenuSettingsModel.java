package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.validator.HDateValidator;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * サイドメニュー設定モデル
 *
 * @author Doan Thang (VJP)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SideMenuSettingsModel extends AbstractModel {

    /** カテゴリーコンテンツリスト */
    private List<SideMenuSettingsCategoryItem> sideMenuSettingsCategoryItemList;

    /** カテゴリーツリーDTOリスト */
    private List<CategoryTreeDto> categoryTreeDtoList;

    /** カテゴリーツリーエラーアイテムリスト */
    private List<CategoryTreeErrorItem> categoryTreeErrorItemList;

    /** サイドメニュー設定リスト */
    private String sideMenuSettingsList;

    /** 登録種別 */
    private String registType;

    /** カテゴリーID ※doLoad呼び出し時に設定（変更させないこと） */
    private String cid;

    /** プレビュー日付 */
    @NotEmpty(message = "{HSeparateDateTimeValidator.NOT_DATE_detail}", groups = {PreviewGroup.class})
    @HCDate
    @HVDate(groups = {PreviewGroup.class})
    private String previewDate;

    /** プレビュー時間 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", message = HDateValidator.NOT_DATE_TIME_MESSAGE_ID, groups = {PreviewGroup.class})
    private String previewTime;

    /** プレビューアクセスキー */
    private String preKey;

    /** プレビュー日時 */
    private String preTime;

}
