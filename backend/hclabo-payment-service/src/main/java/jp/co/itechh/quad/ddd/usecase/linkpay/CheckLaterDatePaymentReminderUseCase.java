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
 * リンク決済(後日払い)の支払期限切れ間近を判定する
 *
 * @author Pham Quang Dieu (VJP)
 */
@Service
public class CheckLaterDatePaymentReminderUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLaterDatePaymentReminderUseCase.class);

    /** LinkPay決済バリューエントリードメインサービス */
    LinkPaymentService linkPaymentService;

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** コンストラクタ */
    @Autowired
    public CheckLaterDatePaymentReminderUseCase(LinkPaymentService linkPaymentService, DateUtility dateUtility) {
        this.linkPaymentService = linkPaymentService;
        this.dateUtility = dateUtility;
    }

    /**
     * 支払期限切れ間近を判定する
     *
     * @param transactionId 取引ID
     * @return True：支払期限間近
     * False：支払期限間近ではない
     */
    public boolean laterDatePaymentReminderCheck(String transactionId) {
        try {
            int leftDaysToRemind = PropertiesUtil.getSystemPropertiesValueToInt("linkpay.leftdays.remind");
            // 督促対象を判定する
            Date thresholdDate = getDayAfter(leftDaysToRemind);
            Date today = getDateofToday();
            return linkPaymentService.getReminderPayment(transactionId, thresholdDate, today);
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

    /**
     * 現在日付を取得する。
     *
     * @return yyyyMMdd の日付
     * @throws ParseException 変換エラー
     */
    protected Date getDateofToday() throws ParseException {

        final String date = new SimpleDateFormat(dateUtility.YMD_SLASH).format(new Date());

        final SimpleDateFormat formatter = new SimpleDateFormat(dateUtility.YMD_SLASH);

        return formatter.parse(date);
    }
}
