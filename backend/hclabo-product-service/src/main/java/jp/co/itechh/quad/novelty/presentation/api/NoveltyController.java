package jp.co.itechh.quad.novelty.presentation.api;

import jp.co.itechh.quad.core.service.noveltypresent.NoveltyPresentCheckService;
import jp.co.itechh.quad.core.service.noveltypresent.NoveltyPresentSearchService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.productnovelty.presentation.api.ProductNoveltyApi;
import jp.co.itechh.quad.productnovelty.presentation.api.param.CheckGoodsRequest;
import jp.co.itechh.quad.productnovelty.presentation.api.param.CheckGoodsResponse;
import jp.co.itechh.quad.productnovelty.presentation.api.param.NoveltyPresentConditionTargetGoodsCountGetRequest;
import jp.co.itechh.quad.productnovelty.presentation.api.param.NoveltyPresentConditionTargetGoodsCountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ノベルティプレゼント条件 Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@RestController
public class NoveltyController extends AbstractController implements ProductNoveltyApi {

    /** ノベルティプレゼント条件Helper */
    private final NoveltyHelper noveltyHelper;

    /** ノベルティプレゼント条件検索サービス */
    public NoveltyPresentSearchService noveltyPresentSearchService;

    /** ノベルティプレゼント登録更新用チェックサービス */
    public NoveltyPresentCheckService noveltyPresentCheckService;

    /**
     * 変換ユーティリティクラス
     */
    @Autowired
    public NoveltyController(NoveltyHelper noveltyHelper,
                             NoveltyPresentSearchService noveltyPresentSearchService,
                             NoveltyPresentCheckService noveltyPresentCheckService) {
        this.noveltyHelper = noveltyHelper;
        this.noveltyPresentSearchService = noveltyPresentSearchService;
        this.noveltyPresentCheckService = noveltyPresentCheckService;
    }

    /**
     * GET /products/novelty/present-conditions/target-goods/count : ノベルティプレゼント条件の商品数確認
     *
     * @param noveltyPresentConditionTargetGoodsCountGetRequest ノベルティプレゼント条件の商品数確認リクエスト (required)
     * @return ノベルティプレゼント条件の商品数確認レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NoveltyPresentConditionTargetGoodsCountResponse> getTargetGoodsCount(@NotNull @Valid
                                                                                                               NoveltyPresentConditionTargetGoodsCountGetRequest noveltyPresentConditionTargetGoodsCountGetRequest) {

        NoveltyPresentConditionTargetGoodsCountResponse noveltyPresentConditionResponse =
                        new NoveltyPresentConditionTargetGoodsCountResponse();
        noveltyPresentConditionResponse.setCount(noveltyPresentSearchService.getTargetGoodsCount(
                        noveltyHelper.toNoveltyPresentConditionTargetGoodsCountResponse(
                                        noveltyPresentConditionTargetGoodsCountGetRequest)));

        return new ResponseEntity<>(noveltyPresentConditionResponse, HttpStatus.OK);

    }

    /**
     * GET /products/novelty/present-conditions/status/check : ノベルティプレゼント条件の商品数確認
     *
     *
     * @param checkGoodsRequest ノベルティプレゼント条件の商品数確認リクエスト (required)
     * @return ノベルティプレゼント条件の商品数確認レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<CheckGoodsResponse> checkProduct(@Valid CheckGoodsRequest checkGoodsRequest) {
        CheckGoodsResponse checkGoodsResponse = new CheckGoodsResponse();

        // 共通情報の取得
        Integer shopSeq = 1001;

        // ノベルティ商品番号:商品マスタ存在チェック
        List<String> noveltyGoodsErrorCodes = noveltyPresentCheckService.checkNoveltyGoods(shopSeq,
                                                                                           checkGoodsRequest.getNoveltyGoodsCodeList()
                                                                                          );
        checkGoodsResponse.setNoveltyGoodsErrorCodes(noveltyGoodsErrorCodes);

        // 商品管理番号:商品マスタ存在チェック
        List<String> goodsGroupErrorCodes = noveltyPresentCheckService.checkGoodsGroupCode(shopSeq,
                                                                                           checkGoodsRequest.getGoodsGroupCodeList()
                                                                                          );
        checkGoodsResponse.setGoodsGroupErrorCodes(goodsGroupErrorCodes);

        // 商品番号:商品マスタ存在チェック
        List<String> goodsErrorCodes =
                        noveltyPresentCheckService.checkGoodsCode(shopSeq, checkGoodsRequest.getGoodsCodeList());
        checkGoodsResponse.setGoodsErrorCodes(goodsErrorCodes);

        // カテゴリＩＤ:カテゴリマスタ存在チェック
        List<String> categoryErrorId =
                        noveltyPresentCheckService.checkCategoryId(shopSeq, checkGoodsRequest.getCategoryIdList());
        checkGoodsResponse.setCategoryErrorId(categoryErrorId);

        checkGoodsResponse.setIconErrorSeqs(noveltyPresentCheckService.checkIconId(checkGoodsRequest.getIconSeqList()));

        // 商品名：商品マスタ存在チェック
        List<String> goodsErrorNames =
                        noveltyPresentCheckService.checkGoodsName(shopSeq, checkGoodsRequest.getGoodsNameList());
        checkGoodsResponse.setGoodsErrorNames(goodsErrorNames);

        // 検索キーワード：商品マスタ存在チェック
        List<String> searchKewordErrors =
                        noveltyPresentCheckService.checkKeyword(shopSeq, checkGoodsRequest.getSearchKeywordList());
        checkGoodsResponse.setSearchKewordErrors(searchKewordErrors);

        return new ResponseEntity<>(checkGoodsResponse, HttpStatus.OK);
    }
}