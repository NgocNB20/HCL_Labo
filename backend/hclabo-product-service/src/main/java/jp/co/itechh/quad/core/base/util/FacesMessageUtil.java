package jp.co.itechh.quad.core.base.util;

import jp.co.itechh.quad.core.base.application.FacesMessage;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 * メッセージユーティリティ
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 * @version $Revision: 1.0 $
 */
public class FacesMessageUtil {

    /**
     * メッセージ取得
     *
     * @param messageId メッセージID
     * @param args メッセージ引数
     * @return FacesMessage
     */
    public static FacesMessage getMessage(String messageId, Object[] args) {

        // プロパティファイルからメッセージを取得
        MessageSource messageSource = ApplicationContextUtility.getBean(MessageSource.class);

        String message = null;
        try {

            // メッセージを取得
            message = messageSource.getMessage(messageId, args, Locale.getDefault());

        } catch (NoSuchMessageException nme) {
            // メッセージ取得できない場合はnullを返却
            return null;
        }

        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setDetail(message);
        facesMessage.setSummary(message);

        return facesMessage;
    }
}