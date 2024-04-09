package jp.co.itechh.quad.product.presentation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.goods.menu.MenuCheckLogic;
import jp.co.itechh.quad.core.service.goods.category.CategoryTreeNodeGetService;
import jp.co.itechh.quad.core.service.goods.menu.impl.MenuGetServiceImpl;
import jp.co.itechh.quad.core.service.goods.menu.impl.MenuUpdateServiceImpl;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.menu.presentation.api.ProductsApi;
import jp.co.itechh.quad.menu.presentation.api.param.MenuResponse;
import jp.co.itechh.quad.menu.presentation.api.param.MenuUpdateRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * メニュー Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class MenuController extends AbstractController implements ProductsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    /** メニュー Helper */
    private final MenuHelper menuHelper;

    /** メニュー入力バリデータLogic */
    private final MenuCheckLogic menuCheckLogic;

    /** メニュー更新 */
    private final MenuUpdateServiceImpl menuUpdateServiceImpl;

    /** メニュー取得 */
    private final MenuGetServiceImpl menuGetServiceImpl;

    /** 公開商品グループ情報検索 */
    private final CategoryTreeNodeGetService categoryTreeNodeGetService;

    /**
     * コンストラクター
     * @param menuHelper メニュー Helper
     * @param menuCheckLogic メニュー入力バリデータLogic
     * @param menuUpdateServiceImpl メニュー更新サービス実装クラス
     * @param menuGetServiceImpl  メニュー取得
     * @param categoryTreeNodeGetService  公開商品グループ情報検索
     */
    public MenuController(MenuHelper menuHelper,
                          MenuCheckLogic menuCheckLogic,
                          MenuUpdateServiceImpl menuUpdateServiceImpl,
                          MenuGetServiceImpl menuGetServiceImpl,
                          CategoryTreeNodeGetService categoryTreeNodeGetService) {
        this.menuHelper = menuHelper;
        this.menuCheckLogic = menuCheckLogic;
        this.menuUpdateServiceImpl = menuUpdateServiceImpl;
        this.menuGetServiceImpl = menuGetServiceImpl;
        this.categoryTreeNodeGetService = categoryTreeNodeGetService;
    }

    /**
     * GET /products/menu : メニュー取得
     * メニュー取得
     *
     * @return メニューレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<MenuResponse> getMenu() {
        CategoryTreeDto categoryTreeDto = categoryTreeNodeGetService.execute(new CategorySearchForDaoConditionDto());

        ObjectWriter ow = new ObjectMapper().writer();
        try {
            String json = ow.writeValueAsString(categoryTreeDto);
            MenuResponse menuResponse = new MenuResponse();
            menuResponse.setCategoryTreeJson(json);
            return new ResponseEntity<>(menuResponse, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT /products/menu : メニュー更新
     * メニュー更新
     *
     * @param menuUpdateRequest メニュー更新リクエスト (required)
     * @return メニューレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @SneakyThrows
    @Override
    public ResponseEntity<MenuResponse> update(@ApiParam(value = "メニュー更新リクエスト", required = true) @Valid @RequestBody
                                                               MenuUpdateRequest menuUpdateRequest) {

        menuCheckLogic.checkForUpdate(menuUpdateRequest.getCategoryTreeJson());

        // アップデート
        menuUpdateServiceImpl.execute(menuHelper.toMenuEntity(menuUpdateRequest));

        // メニュー情報取得
        MenuEntity menuEntity = menuGetServiceImpl.execute(1001);

        // メニューレスポンスに変換
        MenuResponse menuResponse = menuHelper.toMenuResponse(menuEntity);

        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }
}