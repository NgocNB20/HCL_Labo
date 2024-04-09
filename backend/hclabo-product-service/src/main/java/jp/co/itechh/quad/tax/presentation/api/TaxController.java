package jp.co.itechh.quad.tax.presentation.api;

import jp.co.itechh.quad.core.service.division.DivisionMapGetService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.tax.presentation.api.param.TaxesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 税 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class TaxController extends AbstractController implements ProductsApi {

    /*** 分類リスト取得サービス */
    private final DivisionMapGetService divisionMapGetService;

    /**
     * コンストラクタ
     *
     * @param divisionMapGetService 分類リスト取得サービス
     */
    public TaxController(DivisionMapGetService divisionMapGetService) {
        this.divisionMapGetService = divisionMapGetService;
    }

    /**
     * GET /products/taxes : 商品税率MAP取得
     * 商品税率MAP取得
     *
     * @return 税率一覧レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<TaxesResponse> get() {

        Map<BigDecimal, String> taxRateMap = divisionMapGetService.getTaxRateMapList();
        Map<String, String> mapRes = new HashMap<>();
        taxRateMap.forEach((k, v) -> mapRes.put(k.toString(), v));

        TaxesResponse taxesResponse = new TaxesResponse();
        taxesResponse.setTaxRateMapList(mapRes);
        return new ResponseEntity<>(taxesResponse, HttpStatus.OK);
    }
}