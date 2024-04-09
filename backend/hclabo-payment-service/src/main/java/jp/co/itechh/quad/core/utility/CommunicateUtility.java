/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import com.gmo_pg.g_pay.client.output.BaseOutput;
import com.gmo_pg.g_pay.client.output.ErrHolder;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.common.CheckMessageDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通信ヘルパークラス
 *
 * @author yt13605
 * @author tomo (itec) 2011/08/29 #2717 GMO側に取引データが存在しない場合の対応
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更
 * @author Kaneko(itec) 2012/09/26 UTF-8のプロパティファイルを読み込めるように対応
 */
@Component
public class CommunicateUtility {

    /** 標準メッセージID */
    public static final String MULTIPAY_DEFAULT_MSG_ID = "LMC000001";

    /** 未エラーメッセージID */
    protected static final String MULTIPAY_NOTERROR_MSG_ID = "LMC01";

    /** 警告メッセージキー */
    protected static final String WARNING_MSG_KEY = "warnInfo";

    /** 該当する取引情報がGMOサーバ上に存在しない GMOエラーコードセット */
    protected static final Set<String> GMO_ERROR_CODE_TRAN_NOT_FOUND;

    /** 取引情報は期限の180日を経過している GMOエラーコードセット */
    protected static final Set<String> GMO_ERROR_CODE_TRAN_EXPIRED;

    /** 仮売上後90日を経過している　GMOエラーコードセット */
    protected static final Set<String> GMO_ERROR_CODE_AUTH_EXPIRED;

    /** 定数：重複防止用Prefix(messageIdMapからメッセージID取得用) */
    protected static final String MSG_ID_MAP_PREFIX = "mmap.";

    static {

        // 「指定されたIDとパスワードの取引が存在しません。」のエラーコードセット
        Set<String> gmoErrorCodeTranNotFound = new HashSet<>();
        gmoErrorCodeTranNotFound.add("E01110002");
        GMO_ERROR_CODE_TRAN_NOT_FOUND = Collections.unmodifiableSet(gmoErrorCodeTranNotFound);

        // 「180日超えの取引のため、処理を行う事が出来ません。」のエラーコードセット
        Set<String> gmoErrorCodeTranExpired = new HashSet<>();
        gmoErrorCodeTranExpired.add("E11010010");
        gmoErrorCodeTranExpired.add("E11010011");
        gmoErrorCodeTranExpired.add("E11010012");
        gmoErrorCodeTranExpired.add("E11010013");
        gmoErrorCodeTranExpired.add("E11010014");
        GMO_ERROR_CODE_TRAN_EXPIRED = Collections.unmodifiableSet(gmoErrorCodeTranExpired);

        // 「仮売上有効期間を超えています。」のエラーコードセット
        Set<String> gmoErrorCodeAuthExpired = new HashSet<>();
        gmoErrorCodeAuthExpired.add("E01420010");
        gmoErrorCodeAuthExpired.add("M01060010");
        GMO_ERROR_CODE_AUTH_EXPIRED = Collections.unmodifiableSet(gmoErrorCodeAuthExpired);
    }

    /**
     * 通信処理エラーチェック
     *
     * @param output 出力パラメータ
     * @return エラーメッセージリスト (null時はエラーなし)
     */
    public List<CheckMessageDto> checkOutput(BaseOutput output) {

        if (!output.isErrorOccurred()) {
            return null;
        }

        List<CheckMessageDto> res = new ArrayList<>();
        List<?> list = output.getErrList();
        for (Object obj : list) {
            if (obj instanceof ErrHolder) {
                CheckMessageDto e = ApplicationContextUtility.getBean(CheckMessageDto.class);
                String messageId = (String) PropertiesUtil.getSystemPropertiesValue(
                                MSG_ID_MAP_PREFIX + ((ErrHolder) obj).getErrInfo());
                if (messageId == null) {
                    messageId = MULTIPAY_DEFAULT_MSG_ID;
                    e.setArgs(new Object[] {((ErrHolder) obj).getErrInfo()});
                }

                e.setMessageId(messageId);
                e.setMessage(getMessage(messageId, null));
                if (messageId.startsWith(MULTIPAY_NOTERROR_MSG_ID)) {
                    e.setError(false);
                } else {
                    e.setError(true);
                }
                res.add(e);
            }
        }

        return res;
    }

    /**
     * チェックメッセージDto取得
     *
     * @param err エラーホルダ
     * @return チェックメッセージDto
     */
    public CheckMessageDto getCheckMessageDto(ErrHolder err) {
        CheckMessageDto e = ApplicationContextUtility.getBean(CheckMessageDto.class);

        String messageId = (String) PropertiesUtil.getSystemPropertiesValue(err.getErrInfo());
        if (messageId == null) {
            messageId = MULTIPAY_DEFAULT_MSG_ID;
            e.setArgs(new Object[] {err.getErrInfo()});
        }
        e.setMessageId(messageId);
        e.setMessage(getMessage(messageId, null));
        e.setError(true);
        return e;
    }

    /**
     * 警告メッセージ取得
     *
     * @param warnMsg 警告メッセージ
     * @return チェックメッセージDto
     */
    public CheckMessageDto getWarnMessage(Map<String, String> warnMsg) {
        CheckMessageDto e = ApplicationContextUtility.getBean(CheckMessageDto.class);

        String messageId = (String) PropertiesUtil.getSystemPropertiesValue(warnMsg.get(WARNING_MSG_KEY));
        e.setMessageId(messageId);
        e.setMessage(getMessage(messageId, null));
        e.setError(false);
        return e;
    }

    /**
     * メッセージ取得
     *
     * @param messageId コード
     * @param args      引数
     * @return メッセージ
     */
    protected String getMessage(String messageId, Object[] args) {
        return AppLevelFacesMessageUtil.getAllMessage(messageId, args).getMessage();
    }

    /**
     * 取引情報が存在しない場合のエラーかを判定
     * @param output GMOとの通信アウトプット
     * @return true 取引情報が存在しない
     */
    public boolean isNotFound(BaseOutput output) {
        List<?> list = output.getErrList();
        // エラー情報がない場合
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }

        for (Object errObject : list) {
            ErrHolder holder = (ErrHolder) errObject;
            String errInfo = holder.getErrInfo();
            // 取引情報がGMOに存在しない場合
            if (GMO_ERROR_CODE_TRAN_NOT_FOUND.contains(errInfo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取引情報が時間経過によって無効になっているかどうかを返す
     * @param output GMOとの通信アウトプット
     * @return true 無効, false 時間経過によって無効になっていない
     */
    public boolean isOutdatedTran(BaseOutput output) {

        List<?> list = output.getErrList();
        // エラー情報がない場合
        if (list == null || list.isEmpty()) {
            return false;
        }

        for (Object errObject : list) {
            ErrHolder holder = (ErrHolder) errObject;
            String errInfo = holder.getErrInfo();
            // 取引情報がGMOに存在しない場合
            if (GMO_ERROR_CODE_TRAN_NOT_FOUND.contains(errInfo)) {
                return true;
            }
            // 取引情報が180日を経過していた場合
            if (GMO_ERROR_CODE_TRAN_EXPIRED.contains(errInfo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 仮売上の有効期限が切れているかどうかを返す
     * @param output GMOとの通信アウトプット
     * @return true 無効, false 有効期限切れでない
     */
    public boolean isAuthExpired(BaseOutput output) {
        List<?> list = output.getErrList();
        // エラー情報がない場合
        if (list == null || list.isEmpty()) {
            return false;
        }

        for (Object errObject : list) {
            ErrHolder holder = (ErrHolder) errObject;
            String errInfo = holder.getErrInfo();
            // 仮売上後、90日を経過していた場合(AmazonPayの場合は30日)
            if (GMO_ERROR_CODE_AUTH_EXPIRED.contains(errInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * エラーホルダからメッセージを作成する
     *
     * @param errHolderList エラーホルダ
     * @return メッセージ
     */
    public String holder2Message(List<ErrHolder> errHolderList) {

        String errorText = "";

        // エラーメッセージを取得する
        if (errHolderList != null && !errHolderList.isEmpty()) {
            List<String> errorMessages = getMessages(errHolderList);

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < errHolderList.size(); i++) {
                String errCode = errHolderList.get(i).getErrCode();
                String errInfo = errHolderList.get(i).getErrInfo();
                String errMsg = errorMessages.get(i);

                builder.append(errCode + ":" + errInfo + " - " + errMsg + "\n");
            }

            errorText = builder.toString();

        }

        return errorText;
    }

    /**
     * 例外からメッセージを作成する
     *
     * @param th 例外
     * @return メッセージ
     */
    public String exception2Message(Throwable th) {

        String errorText = "";

        // 例外をメッセージにする
        if (th != null) {
            String exception = th.getClass().getName();
            String mes = th.getMessage();
            errorText += "GMO マルチペイメントと通信中に " + exception + " が発生しました。\n" + mes + "\n";
        }

        return errorText;
    }

    /**
     * エラーメッセージに対応するメッセージリソースを取得する
     *
     * @param errHolders GMO xxxTranOutput の ErrHolder
     * @return ErrHolder に対応したメッセージリスト
     */
    public List<String> getMessages(List<ErrHolder> errHolders) {

        List<String> returnList = new ArrayList<>();
        if (errHolders == null) {
            return returnList;
        }

        for (ErrHolder holder : errHolders) {

            String errInfo = holder.getErrInfo();
            String message = "";

            if (errInfo != null && !errInfo.isEmpty()) {
                message = StringUtils.trimToEmpty(PropertiesUtil.getSystemPropertiesValue(errInfo));
            }

            returnList.add(message);
        }

        return returnList;
    }

    /**
     * エラー情報作成
     * <pre>
     * GMOレスポンスのエラーリストからエラー情報を取得して文字列化する。
     * エラーが複数ある場合は「｜」区切りで連結する。
     * // 要素0：エラーコード
     * // 要素1：エラー情報
     * </pre>
     *
     * @param output GMOレスポンス
     * @return エラー情報文字列
     */
    public String[] getError(BaseOutput output) {
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        List<String> strErrCodeList = new ArrayList<>();
        List<String> strErrInfoList = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<ErrHolder> errList = output.getErrList();
        for (ErrHolder holder : errList) {
            strErrCodeList.add(holder.getErrCode());
            strErrInfoList.add(holder.getErrInfo());
        }

        String[] retValue = {conversionUtility.toUnitStr(strErrCodeList, "|"),
                        conversionUtility.toUnitStr(strErrInfoList, "|")};
        return retValue;
    }

}