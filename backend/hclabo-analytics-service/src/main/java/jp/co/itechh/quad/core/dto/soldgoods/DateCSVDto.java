package jp.co.itechh.quad.core.dto.soldgoods;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 日付データCSVDto
 */
@Data
@Component
@Scope("prototype")
public class DateCSVDto {

    /**
     * 日付データ
     */
    private String date;

    /**
     * 販売個数
     */
    private Integer sales;

    /**
     * キャンセル数
     */
    private Integer cancel;
}
