package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.did;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.RegistUpdateGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * お届け不可日検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DeliveryDidModel extends AbstractModel {

    /** 年(引き継ぎ用） */
    public static final String FLASH_YEAR = "year";

    /** 配送方法SEQ(引き継ぎ用） */
    public static final String FLASH_DMCD = "dmcd";

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /** 検索結果一覧 */
    private List<DeliveryDidModelItem> resultItems;

    /** 年(引き継ぎ用） */
    private Integer redirectYear;

    /** 年 */
    @NotNull(message = "{HRequiredValidator.REQUIRED_detail}",
             groups = {SearchGroup.class, AllDownloadGroup.class, UploadGroup.class})
    private Integer year;

    /**
     * セレクトボックス オプション(年)
     */
    private Map<String, String> yearItems;

    /** 年月日 */
    @HCDate
    private String date;

    /** 理由 */
    private String reason;

    /** 年月日(入力) */
    @NotEmpty(groups = {RegistUpdateGroup.class})
    @HVDate(groups = {RegistUpdateGroup.class})
    @HCDate
    private String inputDate;

    /** 理由(入力) */
    @Length(min = 1, max = 20, groups = {RegistUpdateGroup.class})
    @NotEmpty(groups = {RegistUpdateGroup.class})
    private String inputReason;

    /** 削除対象休日 年月日 */
    private String deleteDate;

    /** 配送方法SEQ */
    private Integer dmcd;

    /** 配送方法SEQ(引き継ぎ用） */
    private Integer redirectDmcd;

    /** 配送方法名 */
    private String deliveryMethodName;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 配送方法種別 */
    private HTypeDeliveryMethodType deliveryMethodType;

    /**
     * @return the yearItems
     */
    public Map<String, String> getYearItems() {
        // ブラウザバックでセッション情報が削除された時用
        if (yearItems == null) {
            Map<String, String> yearMap = new LinkedHashMap<>();
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            for (int i = currentYear; i <= currentYear + 1; i++) {
                yearMap.put(Integer.toString(i), Integer.toString(i));
            }
            setYearItems(yearMap);
        }
        return yearItems;
    }

    public boolean isResult() {
        return resultItems != null && !resultItems.isEmpty();
    }

    /** 総件数 */
    private int totalCount;

}