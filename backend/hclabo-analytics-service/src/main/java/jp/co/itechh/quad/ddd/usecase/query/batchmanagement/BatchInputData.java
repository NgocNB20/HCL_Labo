package jp.co.itechh.quad.ddd.usecase.query.batchmanagement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * バッチインプットデータ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Getter
@Setter
@NoArgsConstructor
public class BatchInputData {

    /**
     * 管理者ID
     */
    private String adminId;

    /**
     * ファイルパス
     */
    private String filePath;

    /**
     * 起動種別（0:手動/1:スケジューラ）
     */
    private String startType;
}
