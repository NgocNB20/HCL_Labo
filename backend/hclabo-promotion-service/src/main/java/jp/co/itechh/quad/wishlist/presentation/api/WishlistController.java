package jp.co.itechh.quad.wishlist.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.memberinfo.wishlist.WishlistDao;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistGoodsStockStatusGetLogic;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistListDeleteService;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistListGetService;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistRegistService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistRegistResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * お気に入りエンドポイント Controller
 *
 * @author Doan THANG (VJP)
 *
 */
@RestController
public class WishlistController extends AbstractController implements PromotionsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(WishlistController.class);

    /** お気に入り情報リスト削除サービス */
    public final WishlistListDeleteService wishlistListDeleteService;

    /** お気に入り情報登録サービス */
    public final WishlistRegistService wishlistRegistService;

    /** お気に入り情報リスト取得（ショップ指定） */
    private final WishlistListGetService wishlistListGetService;

    /** お気に入り在庫状況表示取得 */
    private final WishlistGoodsStockStatusGetLogic wishlistGoodsStockStatusGetLogic;

    /** お気に入りDAO */
    private final WishlistDao wishlistDao;

    /** お気に入りエンドポイントHelper */
    private final WishlistHelper wishlistHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "通知の失敗： ";

    /**
     * コンストラクタ
     * @param wishlistListDeleteService お気に入り情報リスト削除サービス
     * @param wishlistRegistService お気に入り情報登録サービス
     * @param wishlistListGetService お気に入り情報リスト取得
     * @param wishlistGoodsStockStatusGetLogic お気に入り在庫状況表示取得
     * @param wishlistDao お気に入りDAO
     * @param wishlistHelper お気に入りエンドポイントHelper
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    public WishlistController(WishlistListDeleteService wishlistListDeleteService,
                              WishlistRegistService wishlistRegistService,
                              WishlistListGetService wishlistListGetService,
                              WishlistGoodsStockStatusGetLogic wishlistGoodsStockStatusGetLogic,
                              WishlistDao wishlistDao,
                              WishlistHelper wishlistHelper,
                              HeaderParamsUtility headerParamsUtil,
                              MessagePublisherService messagePublisherService) {
        this.wishlistListDeleteService = wishlistListDeleteService;
        this.wishlistRegistService = wishlistRegistService;
        this.wishlistListGetService = wishlistListGetService;
        this.wishlistGoodsStockStatusGetLogic = wishlistGoodsStockStatusGetLogic;
        this.wishlistDao = wishlistDao;
        this.wishlistHelper = wishlistHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.messagePublisherService = messagePublisherService;
    }

    /**
     * DELETE /promotions/wishlist/{goodsCode} : お気に入り情報削除
     * お気に入り情報削除
     *
     * @param goodsCode 商品コード (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> delete(String goodsCode) {
        // レスポンス
        wishlistListDeleteService.execute(getCustomerId(), goodsCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /promotions/wishlist : お気に入り情報一覧取得
     * お気に入り情報一覧取得
     *
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return お気に入り情報一覧レスポンス (status code 200)
     *         or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<WishlistListResponse> get(@Valid PageInfoRequest pageInfoRequest) {

        // お気に入りリスト検索
        WishlistSearchForDaoConditionDto conditionDto =
                        wishlistHelper.toWishlistConditionDtoForSearchWishlistList(getCustomerId());

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        // 検索
        List<WishlistDto> wishlistList = wishlistListGetService.execute(HTypeSiteType.FRONT_PC, conditionDto);
        List<WishlistDto> wishlistDtoList = wishlistGoodsStockStatusGetLogic.execute(wishlistList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }

        // レスポンス
        WishlistListResponse wishlistListResponse = wishlistHelper.toWishlistListResponse(wishlistDtoList);
        wishlistListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(wishlistListResponse, HttpStatus.OK);
    }

    /**
     * POST /promotions/wishlist/{goodsCode} : お気に入り情報登録
     * お気に入り情報登録
     *
     * @param goodsCode 商品コード (required)
     * @return 登録件数 (status code 200)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<WishlistRegistResponse> regist(String goodsCode) {

        // 登録処理
        WishlistEntity registEntity = wishlistRegistService.execute(getCustomerId(), HTypeSiteType.FRONT_PC, goodsCode);

        WishlistEntity wishlistEntity = wishlistDao.getEntity(getCustomerId(), registEntity.getGoodsSeq());

        // レスポンス
        WishlistRegistResponse wishlistRegistResponse = wishlistHelper.toWishlistRegistResponse(wishlistEntity);
        return new ResponseEntity<>(wishlistRegistResponse, HttpStatus.OK);
    }

    /**
     * DELETE /promotions/wishlist/delete-by-member-info-seq : お気に入り商品の削除お気に入り商品の削除
     *
     * @param memberInfoSeq 会員SEQ (required)
     * @return 成功 (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> deleteByMemberInfoSeq(
                    @ApiParam(value = "会員SEQ", required = true) @PathVariable("memberInfoSeq") Integer memberInfoSeq) {

        try {
            // ルーティングキー取得
            String routing = PropertiesUtil.getSystemPropertiesValue("queue.wishlistdelete.routing");

            // キューにパブリッシュー
            messagePublisherService.publish(routing, memberInfoSeq);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("オーソリ期限切れ間近注文通知" + messageLogParam + e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 顧客IDを取得する
     *
     * @return customerId 顧客ID
     */
    private Integer getCustomerId() {
        if (this.headerParamsUtil.getMemberSeq() == null) {
            return null;
        } else {
            return Integer.valueOf(this.headerParamsUtil.getMemberSeq());
        }
    }
}