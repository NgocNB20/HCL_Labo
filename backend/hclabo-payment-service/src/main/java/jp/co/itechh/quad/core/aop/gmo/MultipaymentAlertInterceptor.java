/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.aop.gmo;

import com.gmo_pg.g_pay.client.output.ErrHolder;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dao.multipayment.MulPayBillDao;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.utility.CommunicateUtility;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.core.web.RequestParamsUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MultiPaymentAlertRequest;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * マルチペイメント通信エラー時にメールを送信するインタセプタ
 * @author yt23807
 */
@Order(1)
@Aspect
@Component
@Scope("prototype")
public class MultipaymentAlertInterceptor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipaymentAlertInterceptor.class);

    /** 定数：OrderId 取得メソッド名称 */
    protected static final String GET_ORDER_ID_METHOD = "getOrderId";

    /** 定数：AccessId 取得メソッド名称 */
    protected static final String GET_ACCESS_ID_METHOD = "getAccessId";

    /** 定数：AccessPass 取得メソッド名称 */
    protected static final String GET_ACCESS_PASS_METHOD = "getAccessPass";

    /** 定数：アラート検証対象のPREFIX名 */
    protected static final String TARGET_PREFIX = "mulpay_";

    /** 定数：受注ID不明 */
    protected static final String UNKNOWN_ORDER_ID = "受注ID不明";

    /** GMO通信ユーティリティ **/
    protected CommunicateUtility communicateUtility;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /**
     * コンストラクタ
     *
     * @param notificationSubApi
     * @param asyncService
     */
    @Autowired
    public MultipaymentAlertInterceptor(NotificationSubApi notificationSubApi, AsyncService asyncService) {
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
    }

    /**
     * インタセプト対象のメソッドをインボークする。
     * @param invocation ポイントカットした実行
     * @return インタセプトしたオブジェクトが返した戻り値
     * @throws Throwable インタセプトしたオブジェクトが投げた例外
     */
    @Around("execution(* com.gmo_pg.g_pay.client.PaymentClient.*(..))")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        communicateUtility = ApplicationContextUtility.getBean(CommunicateUtility.class);

        String methodName = invocation.getSignature().getName();

        Object returnValue = null;
        String orderId = UNKNOWN_ORDER_ID;

        try {

            // PaymentClient のメソッドを実行する
            returnValue = invocation.proceed();

            // このインタセプタ処理内で発生した例外は、呼び出し元へ伝播させない
            try {

                List<ErrHolder> errHolderList = this.getErrHolderList(returnValue);
                orderId = this.getOrderId(invocation.getArgs(), returnValue);

                // エラーが１件でもある場合はエラーメール送信を行う
                if (!errHolderList.isEmpty()) {
                    this.alert(methodName, errHolderList, null, orderId);
                }

            } catch (Exception e) {
                LOGGER.warn("PaymentClient API の戻り値の取得に失敗しました。", e);
            }

        } catch (Throwable th) {

            // PaymentClient で例外が発生した場合はエラーメール送信を行う
            orderId = this.getOrderId(invocation.getArgs(), returnValue);
            this.alert(methodName, null, th, orderId);

            throw th;
        }

        return returnValue;
    }

    /**
     * ポイントカットしたメソッドから OrderId を取得する
     *
     * @param args  ポイントカットメソッドの引数
     * @param returnValue ポイントカットメソッドの戻り値
     * @return 取得した受注ID
     */
    protected String getOrderId(Object[] args, Object returnValue) {

        String orderId = null;

        // ポイントカットメソッドの引数から orderId 取得を試みる
        orderId = this.getOrderIdFromArgument(args);
        if (orderId != null) {
            return orderId;
        }

        // ポイントカットメソッドの戻り値から orderId 取得を試みる
        orderId = this.getOrderIdFromReturnValue(returnValue);
        if (orderId != null) {
            return orderId;
        }

        // MulPayBill エンティティから orderId 取得を試みる
        orderId = this.getOrderIdFromMulPayBill(args, returnValue);
        if (orderId != null) {
            return orderId;
        }

        // orderId を取得できなかった場合
        return UNKNOWN_ORDER_ID;
    }

    /**
     * ポイントカットしたメソッドの引数より orderId 取得を試みる処理。
     *
     * @param args ポイントカットメソッドの引数
     * @return orderId。取得できなかった場合は null
     */
    protected String getOrderIdFromArgument(Object[] args) {

        if (args.length != 1) {
            return null;
        }

        try {
            return (String) args[0].getClass().getMethod(GET_ORDER_ID_METHOD).invoke(args[0]);
        } catch (Exception e) {
            // ここでは例外発生時のハンドリング処理は行わない。
            // 例外が発生する場合は、そもそも getOrderId メソッドが対象の引数に存在していないケースであるため null
            // を返すことが妥当
            return null;
        }

    }

    /**
     * ポイントカットメソッドの戻り値より orderId 取得を試みる処理。
     *
     * @param returnValue ポイントカットメソッドの戻り値
     * @return orderId。取得できなかった場合は null
     */
    protected String getOrderIdFromReturnValue(Object returnValue) {

        if (returnValue == null) {
            return null;
        }

        try {
            return (String) returnValue.getClass().getMethod(GET_ORDER_ID_METHOD).invoke(returnValue);
        } catch (Exception e) {
            // ここでは例外発生時のハンドリング処理は行わない。
            // 例外が発生する場合は、そもそも getOrderId メソッドが対象の引数に存在していないケースであるため null
            // を返すことが妥当
            return null;
        }

    }

    /**
     * MulPayBill より orderId 取得を試みる処理。
     * <pre>
     * ポイントカットメソッドの引数及び戻り値に含まれる accessId および accessPass を使用して MulPayBill エンティティを取得し、その orderId を返す処理。
     * </pre>
     *
     * @param args ポイントカットメソッドの引数
     * @param returnValue ポイントカットしたメソッドの戻り値
     * @return orderId。 取得できなかった場合は null
     */
    protected String getOrderIdFromMulPayBill(Object[] args, Object returnValue) {

        // 当クラスはインタセプタのため、 DI での DAO オブジェクト注入は期待できない。
        // そのため例外的に能動的なコンポーネント取得を行う。

        MulPayBillDao dao = ApplicationContextUtility.getBean(MulPayBillDao.class);
        if (dao == null) {
            LOGGER.warn(MulPayBillDao.class.getSimpleName() + " コンポーネントを取得できませんでした。OrderId 取得は出来ません。");
            return null;
        }

        //
        // MulPayBill からエンティティを取得するためのキーを取得する
        //

        String accessId = null;
        String accessPass = null;

        if (args.length == 1) {

            //
            // 引数より AccessId 及び AccessPass を取得
            //

            try {
                accessId = (String) args[0].getClass().getMethod(GET_ACCESS_ID_METHOD).invoke(args[0]);
            } catch (Exception e) {
                // ここでは例外発生時のハンドリング処理は行わない。
                // getAccessId メソッドが引数にない/アクセスできない場合は、別の方法で acccessId 取得を試みる。
            }

            try {
                accessPass = (String) args[0].getClass().getMethod(GET_ACCESS_PASS_METHOD).invoke(args[0]);
            } catch (Exception e) {
                // ここでは例外発生時のハンドリング処理は行わない。
                // getAccessPass メソッドが引数にない/アクセスできない場合は、別の方法で acccessPass
                // 取得を試みる。
            }

        }

        if (returnValue != null) {

            //
            // 戻り値より AccessId 及び AccessPass を取得する
            //

            // accessId/accessPass 取得のどちらかで例外が発生したら必要な情報が取得できないことが確定するため
            // try-catch ブロックは一つにまとめる
            try {

                if (accessId == null) {
                    accessId = (String) returnValue.getClass().getMethod(GET_ACCESS_ID_METHOD).invoke(returnValue);
                }

                if (accessPass == null) {
                    accessPass = (String) returnValue.getClass().getMethod(GET_ACCESS_PASS_METHOD).invoke(returnValue);
                }

            } catch (Exception e) {
                // ここでは例外発生時のハンドリング処理は行わない。
                // 値を取得できない場合の処理は後続処理で行う。
            }

        }

        // accessId もしくは accessPass を取得できなかった場合
        if (accessId == null || accessPass == null) {
            LOGGER.debug("戻り値には　MulPayBill 取得キー情報が含まれていません。");
            return null;
        }

        //
        // MulPayBill エンティティを取得する
        //

        LOGGER.debug("MulPayBill エンティティ取得キー accessId = " + accessId + ", accessPass = " + accessPass);

        MulPayBillEntity entity = dao.getLatestEntityByAccessInfo(accessId, accessPass);

        if (entity != null) {
            LOGGER.debug("取得エンティティ mulpaybillseq = " + entity.getMulPayBillSeq() + ", orderId = "
                         + entity.getOrderId());
            return entity.getOrderId();
        }

        // GMO との通信で使用した accessId & accessPass に対応する MulPayBill エンティティを
        // 取得できない場合は orderId = null として返す。

        LOGGER.debug("該当エンティティなし");

        return null;
    }

    /**
     * 戻り値に含まれるエラーリストを取得する
     * @param returnValue 戻り値
     * @return エラーリスト
     * @throws java.lang.reflect.InvocationTargetException 発生していた例外
     * @throws IllegalAccessException 発生していた例外
     */
    public List<ErrHolder> getErrHolderList(Object returnValue)
                    throws java.lang.reflect.InvocationTargetException, IllegalAccessException {

        List<ErrHolder> holderList = new ArrayList<>();

        if (returnValue == null) {
            return holderList;
        }

        java.lang.reflect.Method getErrList = null;
        java.lang.reflect.Method getEntryErrList = null;
        java.lang.reflect.Method getExecErrList = null;

        for (java.lang.reflect.Method method : returnValue.getClass().getMethods()) {

            // public メソッドでないのなら無視
            // 引数をとるメソッドであるのなら無視
            // 戻り値がリストでないのなら無視
            if (!java.lang.reflect.Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length != 0
                && !List.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }

            if ("getErrList".equals(method.getName())) {
                LOGGER.debug("getErrList メソッドが見つかりました");
                getErrList = method;
                continue;
            }

            if ("getEntryErrList".equals(method.getName())) {
                LOGGER.debug("getEntryErrList メソッドが見つかりました");
                getEntryErrList = method;
                continue;
            }

            if ("getExecErrList".equals(method.getName())) {
                LOGGER.debug("getExecErrList メソッドが見つかりました");
                getExecErrList = method;
                continue;
            }
        }

        // getErrList() メソッドの内容を取得する
        if (getErrList != null) {
            List<?> list = (List<?>) getErrList.invoke(returnValue);

            // 内容が List<ErrHolder> のオブジェクトの場合のみ、返却リストへ追加する
            if (list != null && !list.isEmpty() && list.get(0).getClass().equals(ErrHolder.class)) {
                @SuppressWarnings("unchecked")
                List<ErrHolder> tmp = (List<ErrHolder>) list;
                holderList.addAll(tmp);
            }
        }

        // getEntryErrList() メソッドの内容を取得する
        if (getEntryErrList != null) {
            List<?> list = (List<?>) getEntryErrList.invoke(returnValue);

            // 内容が List<ErrHolder> のオブジェクトの場合のみ、返却リストへ追加する
            if (list != null && !list.isEmpty() && list.get(0).getClass().equals(ErrHolder.class)) {
                @SuppressWarnings("unchecked")
                List<ErrHolder> tmp = (List<ErrHolder>) list;
                holderList.addAll(tmp);
            }
        }

        // getExecErrList() メソッドの内容を取得する
        if (getExecErrList != null) {
            List<?> list = (List<?>) getExecErrList.invoke(returnValue);

            // 内容が List<ErrHolder> のオブジェクトの場合のみ、返却リストへ追加する
            if (list != null && !list.isEmpty() && list.get(0).getClass().equals(ErrHolder.class)) {
                @SuppressWarnings("unchecked")
                List<ErrHolder> tmp = (List<ErrHolder>) list;
                holderList.addAll(tmp);
            }
        }

        LOGGER.debug("返されたエラー一覧：" + holderList);

        return holderList;
    }

    /**
     * 管理者へのメール送信処理
     * このメソッドは、いかなる例外も返してはいけない。
     * @param methodName メソッド名
     * @param errHolderList エラーリスト
     * @param th ポイントカットしたオブジェクトがスローした例外
     * @param orderId オーダ番号
     */
    public void alert(String methodName, List<ErrHolder> errHolderList, Throwable th, String orderId) {

        try {

            // 何も問題が起きていない場合
            if ((errHolderList == null || errHolderList.isEmpty()) && th == null) {
                return;
            }

            // 対象外の API の場合はアラート処理を行わない
            if (!this.isTargetApi(methodName)) {
                LOGGER.debug("設定ファイルが存在していないか、処理対象外のAPIのため管理者への通知は行われません。");
                return;
            }

            // エラーホルダまたは例外からメッセージを作成する
            String errorText =
                            communicateUtility.holder2Message(errHolderList) + communicateUtility.exception2Message(th);

            // 送信処理
            this.sendMail(errorText, methodName, orderId);

        } catch (Throwable secondThrowable) {
            // このメソッドの呼び出し元に例外/エラー等は一切伝播させてはいけないため、
            // 全てのスローされたオブジェクトをここで握りつぶす。
            LOGGER.warn("Multipayment操作でエラーが発生しましたが、管理者へはメール送信を行えませんでした。", secondThrowable);
        }
    }

    /**
     * 管理者宛のメールを送信する
     * @param text  エラーメッセージ
     * @param api   エラーの発生したAPI
     * @param orderId   エラーの発生した OrderId
     * @throws Exception    発生した例外
     */
    protected void sendMail(String text, String api, String orderId) {
        //
        // メールを送信する
        //
        HeaderParamsUtility headerParamsUtil = ApplicationContextUtility.getBean(HeaderParamsUtility.class);
        RequestParamsUtility requestParamsUtil = ApplicationContextUtility.getBean(RequestParamsUtility.class);
        String aui = requestParamsUtil.getAccessUid();
        String sessionId = requestParamsUtil.getSessionId();
        String memberSeq = headerParamsUtil.getMemberSeq();

        MultiPaymentAlertRequest multiPaymentAlertRequest = new MultiPaymentAlertRequest();

        multiPaymentAlertRequest.setText(text);
        multiPaymentAlertRequest.setApi(api);
        multiPaymentAlertRequest.setOrderId(orderId);

        if (StringUtils.isNotEmpty(aui)) {
            multiPaymentAlertRequest.setAccessUid(aui);
        } else {
            multiPaymentAlertRequest.setAccessUid("");
        }

        if (StringUtils.isNotEmpty(sessionId)) {
            multiPaymentAlertRequest.setSessionId(sessionId);
        } else {
            multiPaymentAlertRequest.setSessionId("");
        }

        if (StringUtils.isNotEmpty(memberSeq)) {
            multiPaymentAlertRequest.setMemberInfoSeq(memberSeq);
        } else {
            multiPaymentAlertRequest.setMemberInfoSeq("");
        }

        Object[] args = new Object[] {multiPaymentAlertRequest};
        Class<?>[] argsClass = new Class<?>[] {MultiPaymentAlertRequest.class};

        asyncService.asyncService(notificationSubApi, "multiPaymentAlert", args, argsClass);

        LOGGER.info("アラートメールを送信しました。");
    }

    /**
     * アラートメールを送信する対象の API かどうかを確認する
     * @param methodName    検証対象のAPI
     * @return true - 検証するよ
     */
    protected boolean isTargetApi(String methodName) {

        // "検証対象API"をpropertiesから取得
        String val = PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + methodName);
        if ("true".equalsIgnoreCase(val)) {
            LOGGER.debug(methodName + " はメール送信ターゲットである");
            return true;
        }

        LOGGER.debug(methodName + " はメール送信ターゲットでない");

        return false;

    }
}