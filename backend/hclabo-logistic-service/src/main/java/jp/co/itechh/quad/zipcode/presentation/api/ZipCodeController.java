package jp.co.itechh.quad.zipcode.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.core.logic.shop.zipcode.ZipCodeAddressGetLogic;
import jp.co.itechh.quad.core.service.zipcode.ZipCodeMatchingCheckService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressGetRequest;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeCheckRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 郵便番号 Controller
 *
 * @author Doan Thang (VJP)
 */
@RestController
public class ZipCodeController extends AbstractController implements ShippingsApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZipCodeController.class);

    /** 郵便番号住所取得ロジック */
    private final ZipCodeAddressGetLogic addressGetLogic;

    /** 郵便番号整合性チェックサービス<br/> */
    private final ZipCodeMatchingCheckService zipCodeMatchingCheckService;

    /** 郵便番号自動更新　Helper */
    private final ZipCodeHelper zipCodeHelper;

    /** エラーコード。郵便番号と都道府県とが一致しない */
    public static final String MSGCD_PREFECTURE_CONSISTENCY = "AOX001011";

    /**
     * コンストラクタ
     * @param addressGetLogic 郵便番号住所取得ロジック
     * @param zipCodeMatchingCheckService 郵便番号整合性チェックサービス
     * @param zipCodeHelper 郵便番号自動更新　Helper
     */
    @Autowired
    public ZipCodeController(ZipCodeAddressGetLogic addressGetLogic,
                             ZipCodeMatchingCheckService zipCodeMatchingCheckService,
                             ZipCodeHelper zipCodeHelper) {
        this.addressGetLogic = addressGetLogic;
        this.zipCodeMatchingCheckService = zipCodeMatchingCheckService;
        this.zipCodeHelper = zipCodeHelper;
    }

    /**
     * GET /shippings/zipcode/addresses : 郵便番号住所情報取得
     * 郵便番号住所情報取得
     *
     * @param zipCodeAddressGetRequest 郵便番号住所情報取得リクエスト (optional)
     * @return 郵便番号住所情報レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<ZipCodeAddressResponse> get(
                    @ApiParam("郵便番号住所情報取得リクエスト") @Valid ZipCodeAddressGetRequest zipCodeAddressGetRequest) {
        ZipCodeAddressResponse zipCodeAddressResponse = new ZipCodeAddressResponse();

        List<ZipCodeAddressDto> list = addressGetLogic.getAddressList(zipCodeAddressGetRequest.getZipCode());
        zipCodeAddressResponse = zipCodeHelper.toZipCodeAddressResponse(list);

        return new ResponseEntity<>(zipCodeAddressResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/zipcode/check : 郵便番号整合性チェック
     * 郵便番号整合性チェック
     *
     * @param zipCodeCheckRequest 郵便番号整合性チェックリクエスト (required)
     * @return 成功 (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "郵便番号整合性チェックリクエスト", required = true) @Valid @RequestBody
                                                      ZipCodeCheckRequest zipCodeCheckRequest) {

        // 配送方法登録サービス実行
        boolean check = zipCodeMatchingCheckService.execute(zipCodeCheckRequest.getZipCode(),
                                                            zipCodeCheckRequest.getPrefecture()
                                                           );
        if (!check) {
            throwMessage(MSGCD_PREFECTURE_CONSISTENCY, new Object[] {"情報"});
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}