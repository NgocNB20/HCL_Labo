package jp.co.itechh.quad.novelty.presentation.api;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAutomaticGrantRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * ノベルティエンドポイント Controller
 *
 * @author Doan Thang (VJP)
 */
@RestController
public class NoveltyController extends AbstractController implements OrderApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(NoveltyController.class);

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** コンストラクタ */
    @Autowired
    public NoveltyController(ConversionUtility conversionUtility,
                             HeaderParamsUtility headerParamsUtil,
                             MessagePublisherService messagePublisherService) {
        this.conversionUtility = conversionUtility;
        this.headerParamsUtil = headerParamsUtil;
        this.messagePublisherService = messagePublisherService;
    }

    /**
     * POST /order/novelty-product-automatic-grant : ノベルティ商品自動付与
     *
     * @param noveltyProductAutomaticGrantRequest ノベルティ商品自動付与リクエスト
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> execute(@Valid @RequestBody
                                                        NoveltyProductAutomaticGrantRequest noveltyProductAutomaticGrantRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.novelty.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setOrderCode(noveltyProductAutomaticGrantRequest.getOrderCode());
        String messageResponse;
        try {
            // キューにパブリッシュー
            this.messagePublisherService.publish(routing, message);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者SEQを取得する
     *
     * @return AdminSeq 管理者SEQ
     */
    private Integer getAdminSeq() {
        return this.conversionUtility.toInteger(this.headerParamsUtil.getAdministratorSeq());
    }
}