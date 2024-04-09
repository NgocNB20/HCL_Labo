package jp.co.itechh.quad.ddd.usecase.linkpay;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * リンク決済(後日払い)の支払期限切れ判定ユースケース
 *
 * @author Pham Quang Dieu (VJP)
 */
@Service
public class CheckLaterDatePaymentExpiredUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLaterDatePaymentExpiredUseCase.class);

    /** LinkPay決済バリューエントリードメインサービス */
    private final LinkPaymentService linkPaymentService;

    private final DateUtility dateUtility;

    /** コンストラクタ */
    @Autowired
    public CheckLaterDatePaymentExpiredUseCase(LinkPaymentService linkPaymentService, DateUtility dateUtility) {
        this.linkPaymentService = linkPaymentService;
        this.dateUtility = dateUtility;
    }

    /**
     * 後日払い支払期限切れを判定する
     *
     * @param transactionId 取引ID
     * @return True：支払期限切れ
     * False：支払期限切れではない
     */
    public boolean laterDatePaymentExpiredCheck(String transactionId) {

        try {
            int leftDaysToRemind = PropertiesUtil.getSystemPropertiesValueToInt("linkpay.leftdays.expire");
            // 期限切れ対象を判定する
            Date thresholdDate = getDayAfter(-leftDaysToRemind);
            return (linkPaymentService.getReminderExpired(transactionId, thresholdDate));
        } catch (ParseException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new RuntimeException("エラーメッセージはありません");
        }
    }

    /**
     * 指定された日数後の日付を取得する。
     *
     * @param days 何日後か
     * @return yyyyMMdd の日付
     * @throws ParseException 変換エラー
     */
    protected Date getDayAfter(final int days) throws ParseException {

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        final Date targetDate = cal.getTime();
        final String date = new SimpleDateFormat(dateUtility.YMD_SLASH).format(targetDate);
        final SimpleDateFormat formatter = new SimpleDateFormat(dateUtility.YMD_SLASH);

        return formatter.parse(date);
    }

}
