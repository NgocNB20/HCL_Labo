package jp.co.itechh.quad.admin.pc.web.admin.common.ajax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * バリデータメッセージクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatorMessage {

    /** アイテム名 */
    private String field;

    /** バリデータメッセージ */
    private String message;
}
