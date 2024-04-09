package jp.co.itechh.quad.admin.pc.web.admin.common.download;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ダウンロード用日付フォーマット
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class DateFormatForDownload extends DateFormat {
    private static final long serialVersionUID = 1L;
    private static final TimeZone TIMEZONE_Z = TimeZone.getTimeZone("UTC");

    private final StdDateFormat fmt = new StdDateFormat().withTimeZone(TIMEZONE_Z).withColonInTimeZone(true);

    /**
     * コンストラクタ
     */
    public DateFormatForDownload() {
        this.calendar = new GregorianCalendar();
    }

    @Override
    public Date parse(String source) {
        return parse(source, new ParsePosition(0));
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return fmt.parse(source, pos);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return fmt.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Object clone() {
        return this;
    }
}