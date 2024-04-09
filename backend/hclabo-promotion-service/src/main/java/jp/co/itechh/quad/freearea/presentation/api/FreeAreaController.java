package jp.co.itechh.quad.freearea.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.freearea.FreeAreaSearchDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaDeleteLogic;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaGetLogic;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaGetService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaListGetService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaRegistService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaUpdateService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaRegistRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaUpdateRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.PageInfoResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * フリーエリア Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class FreeAreaController extends AbstractController implements ContentManagementApi {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeAreaController.class);

    /**
     * フリーエリア取得ロジック
     */
    private final FreeAreaGetLogic freeAreaGetLogic;

    /**
     * フリーエリア削除ロジック
     */
    private final FreeAreaDeleteLogic freeAreaDeleteLogic;

    /**
     * フリーエリア更新サービス
     */
    private final FreeAreaUpdateService freeAreaUpdateService;

    /**
     * フリーエリア登録サービス
     */
    private final FreeAreaRegistService freeAreaRegistService;

    /**
     * フリーエリア Helper
     */
    private final FreeAreaHelper freeAreaHelper;
    /**
     * フリーエリア情報取得サービス
     */
    private final FreeAreaGetService freeAreaGetService;

    /**
     * フリーエリアリスト取得サービス
     */
    private final FreeAreaListGetService freeAreaListGetService;

    /**
     * フリーエリア削除失敗メッセージコード<br/>
     * <code>MSGCD_FREEAREA_DELETE_FAIL</code>
     */
    public static final String MSGCD_FREEAREA_DELETE_FAIL = "ASF000103W";

    /**
     * コンストラクタ
     *
     * @param freeAreaGetLogic       フリーエリア取得ロジック
     * @param freeAreaDeleteLogic    フリーエリア削除ロジック
     * @param freeAreaUpdateService  フリーエリア更新サービス
     * @param freeAreaRegistService  フリーエリア登録サービス
     * @param freeAreaHelper         フリーエリア Helper
     * @param freeAreaListGetService フリーエリアリスト取得
     */
    @Autowired
    public FreeAreaController(FreeAreaGetLogic freeAreaGetLogic,
                              FreeAreaDeleteLogic freeAreaDeleteLogic,
                              FreeAreaUpdateService freeAreaUpdateService,
                              FreeAreaRegistService freeAreaRegistService,
                              FreeAreaHelper freeAreaHelper,
                              FreeAreaGetService freeAreaGetService,
                              FreeAreaListGetService freeAreaListGetService) {
        this.freeAreaGetLogic = freeAreaGetLogic;
        this.freeAreaDeleteLogic = freeAreaDeleteLogic;
        this.freeAreaUpdateService = freeAreaUpdateService;
        this.freeAreaRegistService = freeAreaRegistService;
        this.freeAreaHelper = freeAreaHelper;
        this.freeAreaListGetService = freeAreaListGetService;
        this.freeAreaGetService = freeAreaGetService;
    }

    /**
     * PUT /content-management/freeareas/{freeAreaSeq} : フリーエリア更新 フリーエリア更新
     *
     * @param freeAreaSeq           フリーエリアSEQ (required)
     * @param freeAreaUpdateRequest フリーエリア更新 (required)
     * @return フリーエリアレスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<FreeAreaResponse> update(
                    @ApiParam(value = "フリーエリアSEQ", required = true) @PathVariable("freeAreaSeq") Integer freeAreaSeq,
                    @ApiParam(value = "フリーエリア更新", required = true) @Valid @RequestBody
                                    FreeAreaUpdateRequest freeAreaUpdateRequest) {
        // ショップSEQを設定
        Integer shopSeq = 1001;

        FreeAreaEntity freeAreaEntity = freeAreaHelper.toFreeAreaEntityForUpdate(freeAreaUpdateRequest, freeAreaSeq);
        freeAreaUpdateService.execute(freeAreaEntity);

        FreeAreaEntity freeAreaEntityRes = freeAreaGetLogic.execute(shopSeq, freeAreaSeq);
        FreeAreaResponse freeAreaResponse = freeAreaHelper.toFreeAreaResponse(freeAreaEntityRes);

        return new ResponseEntity<>(freeAreaResponse, HttpStatus.OK);
    }

    /**
     * POST /content-management/freeareas : フリーエリア登録 フリーエリア登録
     *
     * @param freeAreaRegistRequest フリーエリア登録リクエスト (required)
     * @return フリーエリアレスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<FreeAreaResponse> regist(
                    @ApiParam(value = "フリーエリア登録リクエスト", required = true) @Valid @RequestBody
                                    FreeAreaRegistRequest freeAreaRegistRequest) {
        // ショップSEQを設定
        Integer shopSeq = 1001;

        FreeAreaEntity freeAreaEntity = freeAreaHelper.toFreeAreaEntityForRegist(freeAreaRegistRequest);

        freeAreaRegistService.execute(freeAreaEntity);
        FreeAreaEntity freeAreaEntityRes = freeAreaGetLogic.execute(shopSeq, freeAreaEntity.getFreeAreaSeq());
        FreeAreaResponse freeAreaResponse = freeAreaHelper.toFreeAreaResponse(freeAreaEntityRes);

        return new ResponseEntity<>(freeAreaResponse, HttpStatus.OK);
    }

    /**
     * DELETE /content-management/freeareas/{freeAreaSeq} : フリーエリア削除 フリーエリア削除
     *
     * @param freeAreaSeq フリーエリアSEQ (required)
     * @return 削除成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(
                    @ApiParam(value = "フリーエリアSEQ", required = true) @PathVariable("freeAreaSeq") Integer freeAreaSeq) {

        // ショップSEQを設定
        Integer shopSeq = 1001;
        int result = 0;
        // 削除対象フリーエリア情報取得
        FreeAreaEntity entity = freeAreaGetLogic.execute(shopSeq, freeAreaSeq);
        if (ObjectUtils.isNotEmpty(entity)) {
            result = freeAreaDeleteLogic.execute(entity);
        }
        if (result == 0) {
            throwMessage(MSGCD_FREEAREA_DELETE_FAIL);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /content-management/freeareas : フリーエリア一覧取得 フリーエリア一覧取得
     *
     * @param freeAreaSeq     フリーエリア一覧レスポンス (optional)
     * @param pageInfoRequest ページ情報リクエスト (optional)
     * @return フリーエリア一覧レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<FreeAreaListResponse> get(@Valid FreeAreaListGetRequest freeAreaSeq,
                                                    @Valid PageInfoRequest pageInfoRequest) {

        // 条件取得
        FreeAreaSearchDaoConditionDto conditionDto =
                        freeAreaHelper.toFreeAreaSearchDaoConditionDtoForSearch(freeAreaSeq);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        // 検索
        List<FreeAreaEntity> freeAreaEntityList = freeAreaListGetService.execute(conditionDto);
        FreeAreaListResponse freeAreaListResponse = freeAreaHelper.toFreeAreaListResponse(freeAreaEntityList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        freeAreaListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(freeAreaListResponse, HttpStatus.OK);
    }

    /**
     * GET /content-management/freeareas/{freeAreaSeq} : フリーエリア取得 フリーエリア取得
     *
     * @param freeAreaSeq フリーエリアSEQ (required)
     * @return フリーエリアレスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<FreeAreaResponse> getByFreeAreaSeq(Integer freeAreaSeq) {
        // ショップSEQを設定
        Integer shopSeq = 1001;

        // 編集中フリーエリア取得
        FreeAreaEntity freeAreaEntity = freeAreaGetLogic.execute(shopSeq, freeAreaSeq);
        FreeAreaResponse freeAreaResponse = freeAreaHelper.toFreeAreaResponse(freeAreaEntity);

        return new ResponseEntity<>(freeAreaResponse, HttpStatus.OK);
    }

    /**
     * GET /content-management/freeareas/open : 公開中フリーエリア取得
     * 公開中フリーエリア取得
     *
     * @param freeAreaKey 公開中フリーエリア取得リクエスト (required)
     * @return フリーエリアレスポンス (status code 200)
     * or システムエラー (status code 200)
     */

    @Override
    public ResponseEntity<FreeAreaResponse> getByFreeAreaKey(@NotNull @Valid FreeAreaGetRequest freeAreaKey) {

        FreeAreaEntity freeAreaEntity = freeAreaGetService.execute(freeAreaKey.getFreeAreaKey());
        FreeAreaResponse freeAreaResponse = freeAreaHelper.toFreeAreaResponse(freeAreaEntity);

        return new ResponseEntity<>(freeAreaResponse, HttpStatus.OK);
    }
}