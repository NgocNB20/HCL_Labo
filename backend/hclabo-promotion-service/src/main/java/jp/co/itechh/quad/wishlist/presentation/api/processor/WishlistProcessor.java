package jp.co.itechh.quad.wishlist.presentation.api.processor;

import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistListDeleteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * お気に入り Processor
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class WishlistProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(WishlistProcessor.class);

    /** お気に入り情報リスト削除サービス */
    public final WishlistListDeleteService wishlistListDeleteService;

    /**
     * コンストラクタ.
     *
     * @param wishlistListDeleteService お気に入り情報リスト削除サービス
     */
    @Autowired
    public WishlistProcessor(WishlistListDeleteService wishlistListDeleteService) {
        this.wishlistListDeleteService = wishlistListDeleteService;
    }

    /**
     * Processorメソッド
     *
     * @param memberInfoSeq 会員SEQ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(Integer memberInfoSeq) {

        LOGGER.info("お気に入り削除処理開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        try {
            // レスポンス
            int deleteCount = wishlistListDeleteService.execute(memberInfoSeq);

            reportString.append("Number of deletes [").append(deleteCount).append("]で処理が終了しました。");
            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());
        } catch (Exception e) {
            reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

            throw e;
        } finally {
            LOGGER.info(reportString.toString());
            LOGGER.info("お気に入り削除処理終了");
        }
    }
}
