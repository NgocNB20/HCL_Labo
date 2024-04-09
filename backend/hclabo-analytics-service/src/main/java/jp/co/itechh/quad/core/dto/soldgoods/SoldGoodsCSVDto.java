package jp.co.itechh.quad.core.dto.soldgoods;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品販売CSVDto
 */
@Data
@Component
@Scope("prototype")
public class SoldGoodsCSVDto {

    /**
     * 商品管理番号
     */
    @CsvColumn(order = 10, columnLabel = "商品管理番号")
    private String goodsGroupCode;

    /**
     * 商品番号
     */
    @CsvColumn(order = 20, columnLabel = "商品番号")
    private String goodsCode;

    /**
     * 商品名
     */
    @CsvColumn(order = 30, columnLabel = "商品名")
    private String goodsName;

    /**
     * 規格1
     */
    @CsvColumn(order = 40, columnLabel = "規格1")
    private String unitValue1;

    /**
     * 規格2
     */
    @CsvColumn(order = 50, columnLabel = "規格2")
    private String unitValue2;

    /**
     * JANコード
     */
    @CsvColumn(order = 60, columnLabel = "JANコード")
    private String janCode;

    /**
     * 単価
     */
    @CsvColumn(order = 70, columnLabel = "単価")
    private Integer unitPrice;

    /**
     * 販売個数
     */
    @CsvColumn(order = 80, columnLabel = "販売個数")
    private Integer salesTotal;

    /**
     * キャンセル数
     */
    @CsvColumn(order = 90, columnLabel = "キャンセル数")
    private Integer cancelTotal;

    /**
     * 日付データ
     */
    private List<DateCSVDto> dateCSVDtoList;
}

