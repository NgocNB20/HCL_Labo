package jp.co.itechh.quad.shop.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import jp.co.itechh.quad.core.service.shop.ShopGetService;
import jp.co.itechh.quad.core.service.shop.ShopUpdateService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.shop.presentation.api.param.ShopResponse;
import jp.co.itechh.quad.shop.presentation.api.param.ShopUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * ショップ　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class ShopController extends AbstractController implements ConfigurationsApi {

    /**
     * ショップ情報取得サービス
     */
    private final ShopGetService shopInfoGetService;

    /**
     * ショップ情報更新サービス
     */
    private final ShopUpdateService shopUpdateService;

    /**
     * ショップ helper
     */
    private final ShopHelper shopHelper;

    /**
     * コンストラクター
     * @param shopInfoGetService ショップ情報取得サービス
     * @param shopUpdateService  ショップ情報更新サービス
     * @param shopHelper         ショップ helper
     */
    public ShopController(ShopGetService shopInfoGetService,
                          ShopUpdateService shopUpdateService,
                          ShopHelper shopHelper) {
        this.shopInfoGetService = shopInfoGetService;
        this.shopUpdateService = shopUpdateService;
        this.shopHelper = shopHelper;
    }

    /**
     * GET /configurations/shop : ショップ情報取得
     * ショップ情報取得
     *
     * @return ショップ情報レスポンス (status code 200)
     *         or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ShopResponse> get() {

        ShopEntity shopEntity = shopInfoGetService.execute();

        ShopResponse shopResponse = shopHelper.toShopResponse(shopEntity);

        return new ResponseEntity<>(shopResponse, HttpStatus.OK);
    }

    /**
     * PUT /configurations/shop : ショップ情報更新
     * ショップ情報更新
     *
     * @param shopUpdateRequest ショップ情報リクエスト (required)
     * @return ショップ情報レスポンス (status code 200)
     *         or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ShopResponse> update(@ApiParam(value = "ショップ情報リクエスト", required = true) @Valid @RequestBody
                                                               ShopUpdateRequest shopUpdateRequest) {

        Integer shopSeq = 1001;

        ShopEntity shopEntity = shopHelper.toShopEntity(shopUpdateRequest, shopSeq);

        shopUpdateService.execute(shopEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}