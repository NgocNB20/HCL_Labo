package jp.co.itechh.quad.ddd.presentation.reports.api.processor;

import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.usecase.reports.ReportRegistUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 集計用販売データ登録用Processor
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class ReportsRegistProcessor {

    /**
     * 集計用販売データ登録用 ユースケース
     */
    private final ReportRegistUseCase reportRegistUseCase;

    /**
     * コンストラクタ
     */
    @Autowired
    public ReportsRegistProcessor(ReportRegistUseCase reportRegistUseCase) {
        this.reportRegistUseCase = reportRegistUseCase;
    }

    /**
     * 登録 集計販売データ
     *
     * @param message メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(QueueMessage message) throws Exception {

        reportRegistUseCase.registData(message.getTransactionRevisionId(), message.getTransactionId());

    }

}
