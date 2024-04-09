package jp.co.itechh.quad.admin.pc.web.admin.other.batchmanagement;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchName;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeManualBatch;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.other.batchmanagement.validator.group.ExecuteGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * バッチ管理画面モデル
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "accepttimeTo", comparison = "accepttimeFrom",
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
public class BatchManagementModel extends AbstractModel {

    /**
     * ページ番号
     */
    private String pageNumber;

    /**
     * 最大表示件数
     */
    private int limit;

    /**
     * タスクID
     */
    private Integer reportTaskId;

    /**
     * バッチ識別情報
     */
    @HVSpecialCharacter(allowCharacters = {'_'}, groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HVItems(target = HTypeBatchName.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String batchtypes;

    /**
     * バッチ識別情報 ラベル
     */
    private Map<String, String> batchtypesItems;

    /**
     * 受付け時刻-From
     */
    @HVDate(pattern = "yyyy/MM/dd", groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String accepttimeFrom;

    /**
     * 受付け時刻-To
     */
    @HVDate(pattern = "yyyy/MM/dd", groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String accepttimeTo;

    /**
     * 受付け時刻
     */
    private Timestamp accepttime;

    /**
     * タスク状態 検索条件
     */
    @HVItems(target = HTypeBatchStatus.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String taskstatuses;

    /**
     * タスク状態 ラベル
     */
    private Map<String, String> taskstatusesItems;

    /**
     * 検索結果リスト
     */
    private List<BatchManagementReportItem> resultItems;

    /**
     * 手動起動用バッチタイプ
     */
    @HVSpecialCharacter(allowCharacters = {'_'}, groups = {ExecuteGroup.class})
    @HVItems(target = HTypeManualBatch.class, groups = {ExecuteGroup.class})
    private String manualBatch;

    /**
     * 手動起動用バッチタイプ ラベル
     */
    private Map<String, String> manualBatchItems;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }
}