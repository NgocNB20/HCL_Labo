package jp.co.itechh.quad.front.pc.web.front.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GMOカード照会結果
 *
 * @author Pham Quang Dieu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCardOutput {

    /** カード情報リスト */
    private List cardList;
    /** エラーリスト */
    private List errList;
    /** Csvレスポンス */
    private String csvResponse;
}
