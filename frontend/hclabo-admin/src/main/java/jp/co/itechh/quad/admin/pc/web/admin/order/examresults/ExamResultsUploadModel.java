package jp.co.itechh.quad.admin.pc.web.admin.order.examresults;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.List;

/**
 * 検査結果アップロード画面モデル
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamResultsUploadModel extends AbstractModel {


    /** ファイル削除失敗メッセージ */
    public static final String MSGCD_FAIL_DELETE = "AOX000803";

    /** アップロードファイル */
    private MultipartFile registUploadFile;

    /** アップロード対象 */
    @NotEmpty(groups = UploadGroup.class)
    private String uploadTarget;

    /** インフォメーションメッセージ */
    private String infoMsg;

    /** 一時領域へ保存されたアップロードファイル */
    private File savedTempraryFile;

    /** 検査結果登録データ件数 */
    private Integer registCount;

    /** エラーリスト */
    private List<ExamResultsUploadModelItem> resultItems;

    /** バリデーション限度数 */
    private Integer validLimit;

    /** 列名称 */
    private String columnLabel;

    /**
     * 処理が成功したかどうか
     *
     * @return true 成功
     */
    public boolean isSuccess() {

        if (resultItems == null) {
            return true;
        }

        return resultItems.isEmpty();
    }
}
