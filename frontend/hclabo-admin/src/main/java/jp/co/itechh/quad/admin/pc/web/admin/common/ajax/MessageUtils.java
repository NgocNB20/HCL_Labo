package jp.co.itechh.quad.admin.pc.web.admin.common.ajax;

import jp.co.itechh.quad.admin.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * エラーメッセージ（Ajax用）
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public class MessageUtils {

    /**
     * メッセージ取得
     *
     * @param messageId メッセージID
     * @param args メッセージ引数
     */
    private static String getMessageString(String messageId, Object[] args) {
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
        return message;
    }

    /**
     * メッセージ取得
     *
     * @param bindingResult bindingResult
     * @return List<ValidatorMessage>
     */
    public static List<ValidatorMessage> getMessageErrorFromBindingResult(BindingResult bindingResult) {
        List<ValidatorMessage> validateErrors = new ArrayList<>();
        // プロパティファイルからメッセージを取得
        MessageSource messageSource = ApplicationContextUtility.getBean(MessageSource.class);
        bindingResult.getAllErrors().forEach(validateError -> {
            ValidatorMessage mess = new ValidatorMessage();
            String fieldName = "";
            try {
                fieldName = ((FieldError) validateError).getField();
            } catch (Exception e) {
            }
            String message = messageSource.getMessage(validateError, Locale.getDefault());
            mess.setMessage(message);
            mess.setField(fieldName);
            validateErrors.add(mess);
        });
        return validateErrors;
    }

    /**
     * メッセージ取得
     *
     * @param list
     * @param messageCode
     * @param args
     * @return List<ValidatorMessage>
     */
    public static void getAllMessage(List<ValidatorMessage> list, String messageCode, Object[] args) {
        // そのままのコードで取得
        String message = getMessageString(messageCode, copyArgs(args));
        ValidatorMessage mess = new ValidatorMessage();
        mess.setField("globalMessage");
        if (message != null) {
            mess.setMessage(message);
            list.add(mess);
            return;
        }
        // Fail
        message = getMessageString(getCode(messageCode, AppLevelFacesMessage.TYPE_FATAL), copyArgs(args));
        if (message != null) {
            mess.setMessage(message);
            list.add(mess);
            return;
        }
        // Error
        message = getMessageString(getCode(messageCode, AppLevelFacesMessage.TYPE_ERROR), copyArgs(args));
        if (message != null) {
            mess.setMessage(message);
            list.add(mess);
            return;
        }
        // Warn
        message = getMessageString(getCode(messageCode, AppLevelFacesMessage.TYPE_WARN), copyArgs(args));
        if (message != null) {
            mess.setMessage(message);
            list.add(mess);
            return;
        }
        // Info
        message = getMessageString(getCode(messageCode, AppLevelFacesMessage.TYPE_INFO), copyArgs(args));
        if (message != null) {
            mess.setMessage(message);
            list.add(mess);
            return;
        }
    }

    /**
     * メッセージ引数のコピーを渡す<br/>
     * FacesMessageUtil#getMessageの処理でメッセージが取得できない場合も<br/>
     * 引数の文字列は、HTMLエスケープされてしまう為<br/>
     *
     * @param args メッセージ引数
     * @return コピーしたメッセージ引数
     */
    private static Object[] copyArgs(Object[] args) {
        if (args == null) {
            return null;
        }
        return Arrays.copyOf(args, args.length);
    }

    /**
     * エラーメッセージコードに整形
     *
     * @param messageCode メッセージコード
     * @param messageType メッセージタイプ
     * @return メッセージコード + メッセージタイプ
     */
    public static String getCode(String messageCode, String messageType) {
        return messageCode + messageType;
    }
}