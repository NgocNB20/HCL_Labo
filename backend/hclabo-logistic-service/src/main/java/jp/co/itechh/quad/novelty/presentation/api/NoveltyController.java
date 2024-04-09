package jp.co.itechh.quad.novelty.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentMemberDao;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentJudgmentDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentValidateDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentMemberEntity;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentConditionGetLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentConditionListGetLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentEnclosureGoodsCodeGetListLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyProductAddLogic;
import jp.co.itechh.quad.core.service.shop.novelty.NoveltyPresentJudgmentCheckService;
import jp.co.itechh.quad.core.service.shop.novelty.NoveltyPresentRegistService;
import jp.co.itechh.quad.core.service.shop.novelty.NoveltyPresentSearchService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionDeleteRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionExclusionsListGetRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionJudgmentRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListGetRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionRegistRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionUpdateRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentGoodsCodeGetListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentMemberRegist;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentMemberRegistRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAdd;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAddJudgmentListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ノベルティプレゼント条件 Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@RestController
public class NoveltyController extends AbstractController implements ShippingsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoveltyController.class);

    /** ノベルティプレゼント条件Helper */
    private final NoveltyHelper noveltyHelper;

    /** ノベルティプレゼント条件Logic */
    private final NoveltyPresentConditionGetLogic noveltyPresentConditionGetLogic;

    /** ノベルティプレゼント条件検索Logic */
    private final NoveltyPresentConditionListGetLogic noveltyPresentConditionListGetLogic;

    /** ノベルティプレゼント登録更新サービス */
    private final NoveltyPresentRegistService noveltyPresentRegistService;

    /** ノベルティプレゼント同梱商品エンティティ取得 */
    private final NoveltyPresentEnclosureGoodsCodeGetListLogic noveltyPresentEnclosureGoodsCodeGetListLogic;

    /** 商品API */
    private final ProductApi productApi;

    /** ノベルティプレゼント条件検索サービス */
    private final NoveltyPresentSearchService noveltyPresentSearchService;

    /** ノベルティプレゼント判定用チェックサービス */
    private final NoveltyPresentJudgmentCheckService noveltyPresentJudgmentCheckService;

    /** ノベルティプレゼント対象会員Daoクラス */
    private final NoveltyPresentMemberDao noveltyPresentMemberDao;

    /** NoveltyProductAddLogic */
    private final NoveltyProductAddLogic noveltyProductAddLogic;

    /** 日付ユーティリティ */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     */
    @Autowired
    public NoveltyController(NoveltyHelper noveltyHelper,
                             NoveltyPresentConditionGetLogic noveltyPresentConditionGetLogic,
                             NoveltyPresentConditionListGetLogic noveltyPresentConditionListGetLogic,
                             NoveltyPresentRegistService noveltyPresentRegistService,
                             NoveltyPresentEnclosureGoodsCodeGetListLogic noveltyPresentEnclosureGoodsCodeGetListLogic,
                             NoveltyPresentSearchService noveltyPresentSearchService,
                             ProductApi productApi,
                             NoveltyPresentJudgmentCheckService noveltyPresentJudgmentCheckService,
                             NoveltyPresentMemberDao noveltyPresentMemberDao,
                             DateUtility dateUtility,
                             NoveltyProductAddLogic noveltyProductAddLogic) {
        this.noveltyHelper = noveltyHelper;
        this.noveltyPresentConditionGetLogic = noveltyPresentConditionGetLogic;
        this.noveltyPresentConditionListGetLogic = noveltyPresentConditionListGetLogic;
        this.noveltyPresentRegistService = noveltyPresentRegistService;
        this.noveltyPresentEnclosureGoodsCodeGetListLogic = noveltyPresentEnclosureGoodsCodeGetListLogic;
        this.noveltyPresentSearchService = noveltyPresentSearchService;
        this.productApi = productApi;
        this.noveltyPresentJudgmentCheckService = noveltyPresentJudgmentCheckService;
        this.noveltyPresentMemberDao = noveltyPresentMemberDao;
        this.dateUtility = dateUtility;
        this.noveltyProductAddLogic = noveltyProductAddLogic;
    }

    /**
     * GET /shippings/novelty/present-conditions : ノベルティプレゼント条件一覧取得
     * ノベルティプレゼント条件一覧取得
     *
     * @param noveltyPresentConditionListGetRequest ノベルティプレゼント条件リクエスト (required)
     * @return ノベルティプレゼント条件リストレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NoveltyPresentConditionListResponse> get(
                    @NotNull @ApiParam(value = "ノベルティプレゼント条件リクエスト", required = true) @Valid
                                    NoveltyPresentConditionListGetRequest noveltyPresentConditionListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト（ページネーションのため）") @Valid
                                    jp.co.itechh.quad.novelty.presentation.api.param.PageInfoRequest pageInfoRequest) {
        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();

        NoveltyPresentSearchForDaoConditionDto conditionDto =
                        noveltyHelper.toNoveltyPresentSearchForDaoConditionDto(noveltyPresentConditionListGetRequest);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        // 検索
        List<NoveltyPresentSearchResultDto> noveltyPresentConditionDtoList =
                        noveltyPresentSearchService.execute(conditionDto);

        NoveltyPresentConditionListResponse noveltyPresentConditionListResponse =
                        noveltyHelper.toNoveltyPresentConditionListResponse(noveltyPresentConditionDtoList);

        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        noveltyPresentConditionListResponse.setPageInfo(pageInfoResponse);
        return new ResponseEntity<>(noveltyPresentConditionListResponse, HttpStatus.OK);
    }

    /**
     * GET /shippings/novelty/present-conditions/{noveltyPresentConditionSeq} : ノベルティプレゼント条件取得
     * ノベルティプレゼント条件取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ (required)
     * @return ノベルティプレゼント条件リストレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NoveltyPresentConditionResponse> getByNoveltyPresentConditionSeq(Integer noveltyPresentConditionSeq) {

        try {
            NoveltyPresentConditionEntity noveltyPresentConditionEntity =
                            noveltyPresentConditionGetLogic.execute(noveltyPresentConditionSeq);

            NoveltyPresentConditionResponse noveltyPresentConditionResponse =
                            noveltyHelper.toNoveltyPresentConditionResponse(noveltyPresentConditionEntity);

            return new ResponseEntity<>(noveltyPresentConditionResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * GET /shippings/novelty/present-conditions/exclusions-for-regist : 登録/更新時の選択可能除外条件一覧取得
     * 登録/更新時の選択可能除外条件一覧取得
     *
     * @param noveltyPresentConditionExclusionsListGetRequest 登録/更新時の選択可能除外条件一覧取得リクエスト (required)
     * @return ノベルティプレゼント条件リストレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NoveltyPresentConditionListResponse> getExclusions(@NotNull @Valid
                                                                                             NoveltyPresentConditionExclusionsListGetRequest noveltyPresentConditionExclusionsListGetRequest) {
        try {

            List<NoveltyPresentConditionEntity> exclusionList = noveltyPresentConditionListGetLogic.getExclusionList(
                            noveltyPresentConditionExclusionsListGetRequest.getNoveltyPresentConditionSeq());

            NoveltyPresentConditionListResponse noveltyPresentConditionExclusionsListResponse =
                            noveltyHelper.toNoveltyPresentSearchResultDtoList(exclusionList);

            return new ResponseEntity<>(noveltyPresentConditionExclusionsListResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST /shippings/novelty/present-conditions : ノベルティプレゼント条件登録
     * ノベルティプレゼント条件登録
     *
     * @param noveltyPresentConditionRegistRequest ノベルティプレゼント条件登録リクエスト (required)
     * @return ノベルティプレゼント条件レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> regist(
                    @Valid NoveltyPresentConditionRegistRequest noveltyPresentConditionRegistRequest) {
        NoveltyPresentConditionEntity noveltyPresentConditionEntity =
                        noveltyHelper.toNoveltyPresentConditionEntity(noveltyPresentConditionRegistRequest);
        NoveltyPresentValidateDto noveltyPresentValidateDto = noveltyHelper.toNoveltyPresentValidateDto(
                        noveltyPresentConditionRegistRequest.getNoveltyPresentValidateListRequest());

        noveltyPresentRegistService.regist(noveltyPresentConditionEntity, noveltyPresentValidateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /shippings/novelty/present-conditions : ノベルティプレゼント条件更新
     * ノベルティプレゼント条件更新
     *
     * @param noveltyPresentConditionUpdateRequest ノベルティプレゼント条件更新リクエスト (required)
     * @return ノベルティプレゼント条件レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> update(@ApiParam(value = "ノベルティプレゼント条件更新リクエスト", required = true) @Valid @RequestBody
                                                       NoveltyPresentConditionUpdateRequest noveltyPresentConditionUpdateRequest) {
        NoveltyPresentConditionEntity noveltyPresentConditionEntity =
                        noveltyHelper.toNoveltyPresentConditionEntity(noveltyPresentConditionUpdateRequest);
        NoveltyPresentValidateDto noveltyPresentValidateDto = noveltyHelper.toNoveltyPresentValidateDto(
                        noveltyPresentConditionUpdateRequest.getNoveltyPresentValidateListRequest());

        noveltyPresentRegistService.update(noveltyPresentConditionEntity, noveltyPresentValidateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE /shippings/novelty/present-conditions : 会員ノベルティプレゼント済み情報取得
     * 会員ノベルティプレゼント済み情報取得
     *
     * @param noveltyPresentConditionDeleteRequest ノベルティプレゼント条件削除リクエスト (required)
     * @return 成功 (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(
                    @Valid NoveltyPresentConditionDeleteRequest noveltyPresentConditionDeleteRequest) {

        noveltyPresentRegistService.delete(noveltyPresentConditionDeleteRequest.getNoveltyPresentConditionSeq());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /shippings/novelty/present-conditions/getGoodsSeqList/{noveltyPresentConditionSeq} : ノベルティ商品番号取得
     * ノベルティ商品番号取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ (required)
     * @return ノベルティ商品番号取得レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NoveltyPresentGoodsCodeGetListResponse> getGoodsSeqListByConditionSeq(Integer noveltyPresentConditionSeq) {

        NoveltyPresentGoodsCodeGetListResponse noveltyPresentGoodsCodeGetListResponse =
                        new NoveltyPresentGoodsCodeGetListResponse();

        List<String> goodsSeqList = noveltyPresentEnclosureGoodsCodeGetListLogic.execute(noveltyPresentConditionSeq);

        ProductDetailListResponse productDetailListResponse = new ProductDetailListResponse();
        if (CollectionUtils.isNotEmpty(goodsSeqList)) {
            ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
            List<Integer> goodsSeqIntegerList = new ArrayList<>();
            for (String str : goodsSeqList) {
                goodsSeqIntegerList.add(Integer.parseInt(str));
            }

            productDetailListGetRequest.setGoodsSeqList(goodsSeqIntegerList);
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            productDetailListResponse = productApi.getDetails(productDetailListGetRequest, pageInfoRequest);
        }

        if (productDetailListResponse.getGoodsDetailsList() == null) {
            noveltyPresentGoodsCodeGetListResponse.setGoodsCodeList(null);
        }

        List<String> goodsCodeList = new ArrayList<>();
        for (String str : goodsSeqList) {
            for (GoodsDetailsResponse goodsDetailsResponse : productDetailListResponse.getGoodsDetailsList()) {
                if (str.equals(goodsDetailsResponse.getGoodsSeq().toString())) {
                    if ("0".equals(goodsDetailsResponse.getGoodsOpenStatus()) && "0".equals(
                                    goodsDetailsResponse.getSaleStatus()) && (
                                        goodsDetailsResponse.getRealStock() != null
                                        && goodsDetailsResponse.getRealStock().intValue() > 0)) {
                        goodsCodeList.add(goodsDetailsResponse.getGoodsCode());
                    }
                }
            }
        }

        noveltyPresentGoodsCodeGetListResponse.setGoodsCodeList(goodsCodeList);

        return new ResponseEntity<>(noveltyPresentGoodsCodeGetListResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/novelty/present-condition-judgment : ノベルティプレゼント条件判定
     * ノベルティプレゼント条件判定
     *
     * @param noveltyPresentConditionJudgmentRequest ノベルティプレゼント条件判定リクエスト (required)
     * @return ノベルティ商品追加一覧レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NoveltyProductAddJudgmentListResponse> noveltyPresentConditionJudgment(
                    @ApiParam(value = "ノベルティプレゼント条件判定リクエスト", required = true) @Valid @RequestBody
                                    NoveltyPresentConditionJudgmentRequest noveltyPresentConditionJudgmentRequest) {

        if (ObjectUtils.isNotEmpty(noveltyPresentConditionJudgmentRequest)) {

            // 条件リスト作成
            NoveltyPresentSearchForDaoConditionDto conditionDto =
                            noveltyHelper.createNoveltyConditionList(noveltyPresentConditionJudgmentRequest);
            List<NoveltyPresentConditionEntity> conditionList =
                            noveltyPresentConditionListGetLogic.getJudgmentListByCondition(conditionDto);

            List<NoveltyPresentConditionEntity> judgmentList = new ArrayList<>();

            NoveltyPresentJudgmentDto judgmentDto = new NoveltyPresentJudgmentDto();
            judgmentDto.setNoveltyPresentConditionEntityList(conditionList);
            judgmentDto.setConditionJudgmentDto(
                            noveltyHelper.toConditionJudgmentDto(noveltyPresentConditionJudgmentRequest));
            judgmentDto.setHmTargetOrderGoodsList(new ArrayList<>());
            judgmentDto.setConditionMatchOrderGoodsMap(new HashMap<>());

            if (ObjectUtils.isNotEmpty(conditionList)) {
                // 判定処理
                judgmentList = noveltyPresentJudgmentCheckService.execute(judgmentDto);
            }
            NoveltyProductAddJudgmentListResponse response = new NoveltyProductAddJudgmentListResponse();

            Map<String, Integer> noveltyGoodsRestOfStockMap = new HashMap<>();
            if (ObjectUtils.isNotEmpty(judgmentList)) {
                List<NoveltyProductAdd> noveltyProductAddList = new ArrayList<>();
                for (NoveltyPresentConditionEntity noveltyPresentConditionEntity : judgmentList) {
                    if (HTypeEnclosureUnitType.ORDER.equals(noveltyPresentConditionEntity.getEnclosureUnitType())) {
                        noveltyProductAddList.addAll(
                                        noveltyProductAddLogic.addOrderNovelty(noveltyPresentConditionEntity,
                                                                               noveltyGoodsRestOfStockMap
                                                                              ));
                    } else {
                        noveltyProductAddList.addAll(
                                        noveltyProductAddLogic.addOrderGoodsNovelty(noveltyPresentConditionEntity,
                                                                                    judgmentDto,
                                                                                    noveltyGoodsRestOfStockMap
                                                                                   ));
                    }
                }
                response.setNoveltyProductAddList(noveltyProductAddList);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * POST /shippings/novelty/present-member-regist : ノベルティプレゼント対象会員登録
     * ノベルティプレゼント対象会員登録
     *
     * @param noveltyPresentMemberRegistRequest ノベルティプレゼント対象会員登録リクエスト (required)
     * @return 成功 (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> noveltyPresentMemberRegist(
                    @ApiParam(value = "ノベルティプレゼント対象会員登録リクエスト", required = true) @Valid @RequestBody
                                    NoveltyPresentMemberRegistRequest noveltyPresentMemberRegistRequest) {

        // 現在日時取得
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 対象会員登録
        for (NoveltyPresentMemberRegist regist : noveltyPresentMemberRegistRequest.getNoveltyPresentMemberRegistList()) {
            NoveltyPresentMemberEntity noveltyPresentMemberEntity =
                            noveltyPresentMemberDao.getEntity(regist.getNoveltyPresentConditionId(),
                                                              regist.getMemberInfoSeq(), regist.getOrderreceivedId(),
                                                              Integer.valueOf(regist.getItemId())
                                                             );

            if (noveltyPresentMemberEntity == null) {
                NoveltyPresentMemberEntity member = new NoveltyPresentMemberEntity();
                member.setNoveltyPresentConditionSeq(regist.getNoveltyPresentConditionId());
                member.setMemberInfoSeq(regist.getMemberInfoSeq());
                member.setGoodsSeq(Integer.valueOf(regist.getItemId()));
                member.setOrderreceivedId(regist.getOrderreceivedId());
                member.setRegistTime(currentTime);
                member.setUpdateTime(currentTime);

                noveltyPresentMemberDao.insert(member);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}