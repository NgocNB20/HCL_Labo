package jp.co.itechh.quad.impossiblearea.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaGetLogic;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryImpossibleAreaListDeleteService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryImpossibleAreaRegistService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryImpossibleAreaSearchService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaDeleteRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListGetRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListResponse;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaRegistRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaResponse;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.PageInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 配送不可能エリ Controller.
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class ImpossibleAreaController extends AbstractController implements ShippingsApi {

    /**
     * 配送不可能エリア検索Serviceインタフェース
     */
    private final DeliveryImpossibleAreaSearchService deliveryImpossibleAreaSearchService;

    /**
     * 配送不可能エリア登録Serviceインターフェース
     */
    private final DeliveryImpossibleAreaRegistService deliveryImpossibleAreaRegistService;

    /**
     * 配送不可能エリア削除Serviceインターフェース
     */
    private final DeliveryImpossibleAreaListDeleteService deliveryImpossibleAreaListDeleteService;

    /**
     * 配送不可能エリア Helper
     */
    private final ImpossibleAreaHelper impossibleAreaHelper;

    /**
     * 配送不可能エリアエンティティ取得Logicインターフェース
     */
    private final DeliveryImpossibleAreaGetLogic deliveryImpossibleAreaGetLogic;

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImpossibleAreaController.class);

    /** メッセージコード：チェックボックスがチェックされていない */
    protected static final String MSGCD_NO_CHECK = "AYD000602";

    /**
     * コンストラクター
     *
     * @param deliveryImpossibleAreaSearchService     配送不可能エリア検索Serviceインタフェース
     * @param deliveryImpossibleAreaRegistService     配送不可能エリア登録Serviceインターフェース
     * @param deliveryImpossibleAreaListDeleteService 配送不可能エリア削除Serviceインターフェース
     * @param impossibleAreaHelper                    配送不可能エリア Helper
     * @param deliveryImpossibleAreaGetLogic          配送不可能エリアエンティティ取得Logicインターフェース
     */
    public ImpossibleAreaController(DeliveryImpossibleAreaSearchService deliveryImpossibleAreaSearchService,
                                    DeliveryImpossibleAreaRegistService deliveryImpossibleAreaRegistService,
                                    DeliveryImpossibleAreaListDeleteService deliveryImpossibleAreaListDeleteService,
                                    ImpossibleAreaHelper impossibleAreaHelper,
                                    DeliveryImpossibleAreaGetLogic deliveryImpossibleAreaGetLogic) {
        this.deliveryImpossibleAreaSearchService = deliveryImpossibleAreaSearchService;
        this.deliveryImpossibleAreaRegistService = deliveryImpossibleAreaRegistService;
        this.deliveryImpossibleAreaListDeleteService = deliveryImpossibleAreaListDeleteService;
        this.impossibleAreaHelper = impossibleAreaHelper;
        this.deliveryImpossibleAreaGetLogic = deliveryImpossibleAreaGetLogic;
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq}/impossible-areas : 配送不可能エリア一覧取得
     * 配送不可能エリア一覧取得
     *
     * @param deliveryMethodSeq            配送方法SEQ (required)
     * @param impossibleAreaListGetRequest 配送不可能エリア一覧リクエスト (required)
     * @param pageInfoRequest              ページ情報リクエスト（ページネーションのため） (optional)
     * @return 配送不可能エリア一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ImpossibleAreaListResponse> getByDeliveryMethodSeq(Integer deliveryMethodSeq,
                                                                             @NotNull @Valid
                                                                                             ImpossibleAreaListGetRequest impossibleAreaListGetRequest,
                                                                             @Valid PageInfoRequest pageInfoRequest) {

        DeliveryImpossibleAreaConditionDto conditionDto =
                        impossibleAreaHelper.toDeliveryImpossibleAreaConditionDto(deliveryMethodSeq,
                                                                                  impossibleAreaListGetRequest
                                                                                 );

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<DeliveryImpossibleAreaResultDto> resultList = deliveryImpossibleAreaSearchService.execute(conditionDto);

        ImpossibleAreaListResponse impossibleAreaListResponse =
                        impossibleAreaHelper.toImpossibleAreaListResponse(resultList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        impossibleAreaListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(impossibleAreaListResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods/{deliveryMethodSeq}/impossible-areas : 配送不可能エリア登録
     * 配送不可能エリア登録
     *
     * @param deliveryMethodSeq           配送方法SEQ (required)
     * @param impossibleAreaRegistRequest 配送不可能エリア登録リクエスト (required)
     * @return 配送不可能エリアレスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<ImpossibleAreaResponse> regist(Integer deliveryMethodSeq,
                                                         @Valid
                                                                         ImpossibleAreaRegistRequest impossibleAreaRegistRequest) {

        DeliveryImpossibleAreaEntity entity =
                        impossibleAreaHelper.toDeliveryImpossibleAreaEntity(impossibleAreaRegistRequest,
                                                                            deliveryMethodSeq
                                                                           );

        deliveryImpossibleAreaRegistService.execute(entity);

        DeliveryImpossibleAreaEntity entityRes =
                        deliveryImpossibleAreaGetLogic.execute(entity.getDeliveryMethodSeq(), entity.getZipCode());

        ImpossibleAreaResponse impossibleAreaResponse = impossibleAreaHelper.toImpossibleAreaResponse(entityRes);

        return new ResponseEntity<>(impossibleAreaResponse, HttpStatus.OK);

    }

    /**
     * DELETE /shippings/methods/{deliveryMethodSeq}/impossible-areas : 配送不可能エリア削除
     * 配送不可能エリア削除
     *
     * @param deliveryMethodSeq           配送方法SEQ (required)
     * @param impossibleAreaDeleteRequest 配送不可能エリア削除リクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> delete(Integer deliveryMethodSeq,
                                       @NotNull @Valid ImpossibleAreaDeleteRequest impossibleAreaDeleteRequest) {
        List<DeliveryImpossibleAreaEntity> deleteList =
                        impossibleAreaHelper.toDeliveryImpossibleAreaEntity(impossibleAreaDeleteRequest,
                                                                            deliveryMethodSeq
                                                                           );

        if ((deleteList == null) || deleteList.isEmpty()) {
            throwMessage(MSGCD_NO_CHECK);
        }

        deliveryImpossibleAreaListDeleteService.execute(deleteList);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}