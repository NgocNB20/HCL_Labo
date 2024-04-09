/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.aop.gmo;

import com.gmo_pg.g_pay.client.input.SecureTran2Input;
import com.gmo_pg.g_pay.client.output.AlterTranOutput;
import com.gmo_pg.g_pay.client.output.ChangeTranOutput;
import com.gmo_pg.g_pay.client.output.DeleteCardOutput;
import com.gmo_pg.g_pay.client.output.DeleteMemberOutput;
import com.gmo_pg.g_pay.client.output.EntryTranOutput;
import com.gmo_pg.g_pay.client.output.ErrHolder;
import com.gmo_pg.g_pay.client.output.ExecTranOutput;
import com.gmo_pg.g_pay.client.output.SaveCardOutput;
import com.gmo_pg.g_pay.client.output.SaveMemberOutput;
import com.gmo_pg.g_pay.client.output.SearchCardOutput;
import com.gmo_pg.g_pay.client.output.SearchMemberOutput;
import com.gmo_pg.g_pay.client.output.SearchTradeOutput;
import com.gmo_pg.g_pay.client.output.SecureTran2Output;
import com.gmo_pg.g_pay.client.output.TradedCardOutput;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeJobCode;
import jp.co.itechh.quad.core.dao.multipayment.MulPayBillDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPayShopDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPaySiteDao;
import jp.co.itechh.quad.core.dto.multipayment.CommunicateResult;
import jp.co.itechh.quad.core.dto.multipayment.HmAlterTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmChangeTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmDeleteCardInput;
import jp.co.itechh.quad.core.dto.multipayment.HmDeleteMemberInput;
import jp.co.itechh.quad.core.dto.multipayment.HmEntryTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmExecTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmSaveCardInput;
import jp.co.itechh.quad.core.dto.multipayment.HmSaveMemberInput;
import jp.co.itechh.quad.core.dto.multipayment.HmSearchCardInput;
import jp.co.itechh.quad.core.dto.multipayment.HmSearchMemberInput;
import jp.co.itechh.quad.core.dto.multipayment.HmTradedCardInput;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayShopEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPaySiteEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 受注インターセプター
 * PaymentClientをコンテナ管理します
 *
 * @author yt23807
 */
@Order(2)
@Aspect
@Component
@Scope("prototype")
public class PaymentClientInterceptor {

    /** ログ出力 */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentClientInterceptor.class);

    /** 決済方法　クレジット */
    protected static final String PAYTYPE_CREDIT = "0";

    /** マルチペイメント用ショップ設定Dao */
    protected MulPayShopDao mulPayShopDao;
    /** マルチペイメント用サイト設定Dao */
    protected MulPaySiteDao mulPaySiteDao;
    /** マルチペイメント請求Dao */
    protected MulPayBillDao mulPayBillDao;

    /** コンストラクタ */
    @Autowired
    public PaymentClientInterceptor(MulPayShopDao mulPayShopDao,
                                    MulPaySiteDao mulPaySiteDao,
                                    MulPayBillDao mulPayBillDao) {
        this.mulPayShopDao = mulPayShopDao;
        this.mulPaySiteDao = mulPaySiteDao;
        this.mulPayBillDao = mulPayBillDao;
    }

    /**
     * 決済処理インターセプター
     *
     * @param invocation 対象となるTranメソッド
     * @return 出力パラメータ
     * @throws Throwable 例外
     */
    @Around("execution(* com.gmo_pg.g_pay.client.PaymentClient.*(..))")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {

        Object obj = null;

        // 共通情報取得
        Integer shopSeq = 1001;

        // 対象となるメソッドを取得
        String methodName = invocation.getSignature().getName();

        // メソッドに応じて、インターセプター内の処理を振り分ける。
        if (methodName.equals("doEntryTran")) {
            obj = interceptDoEntryTran(invocation, shopSeq);
        } else if (methodName.equals("doExecTran")) {
            obj = interceptDoExecTran(invocation, shopSeq);
        } else if (methodName.equals("doSecureTran2")) {
            obj = interceptDoSecureTran2(invocation);
        } else if (methodName.equals("doAlterTran")) {
            obj = interceptDoAlterTran(invocation, shopSeq);
        } else if (methodName.equals("doChangeTran")) {
            obj = interceptDoChangeTran(invocation, shopSeq);
        } else if (methodName.equals("doSearchTrade")) {
            obj = interceptDoSearchTrade(invocation, shopSeq);
        } else if (methodName.equals("doSearchMember")) {
            obj = interceptDoSearchMember(invocation, shopSeq);
        } else if (methodName.equals("doSaveMember")) {
            obj = interceptDoSaveMember(invocation, shopSeq);
        } else if (methodName.equals("doDeleteMember")) {
            obj = interceptDoDeleteMember(invocation, shopSeq);
        } else if (methodName.equals("doTradedCard")) {
            obj = interceptDoTradedCard(invocation, shopSeq);
        } else if (methodName.equals("doSearchCard")) {
            obj = interceptDoSearchCard(invocation, shopSeq);
        } else if (methodName.equals("doSaveCard")) {
            obj = interceptDoSaveCard(invocation, shopSeq);
        } else if (methodName.equals("doDeleteCard")) {
            obj = interceptDoDeleteCard(invocation, shopSeq);
        } else {
            obj = interceptDoOtherPaymentMethod(invocation, shopSeq);
        }

        return obj;
    }

    /**
     * クレジット決済-取引登録インターセプター
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoEntryTran(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoEntryTran:START");

            /*
             * 前処理
             */
            // 引数を取得します。（EntryTranInput)
            Object[] obj = inv.getArgs();
            HmEntryTranInput input = (HmEntryTranInput) obj[0];
            // マルチペイショップ設定情報を取得します。
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            if (isNull(input.getShopId())) {
                input.setShopId(mulPayShopEntity.getShopId());
            }
            if (input.getShopPass() == null) {
                input.setShopPass(mulPayShopEntity.getShopPass());
            }

            input.setOrderId(input.getOrderId());
            input.setTdTenantName(mulPayShopEntity.getTdTenantName());

            /*
             * 本処理
             */
            EntryTranOutput outputParam = (EntryTranOutput) inv.proceed();

            /*
             * 後処理
             */
            MulPayBillEntity mulPayBillEntity = copyOutputProp(input, outputParam);
            mulPayBillEntity.setPayType(PAYTYPE_CREDIT);
            mulPayBillEntity.setTranType("EntryTran");
            // エラー内容を取得
            if (outputParam.isErrorOccurred()) {
                setErrorOutput(mulPayBillEntity, outputParam.getErrList());
            }
            // マルチペイメント請求TBL登録
            int result = mulPayBillDao.insert(mulPayBillEntity);
            if (result == 0) {
                LOGGER.warn("マルチペイメント請求TBLへの登録件数0件");
            }

            return outputParam;

        } finally {
            LOGGER.info("interceptDoEntryTran:END");
        }
    }

    /**
     * クレジット-決済実行インターセプター
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoExecTran(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoExecTran:START");

            /*
             * 前処理
             */
            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmExecTranInput inputParam = (HmExecTranInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            // 現在のショップに対応するMulPayShopを取得
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, inputParam, null);

            // MulPayBillエンティティの取得
            String bufOrderId = null;
            MulPayBillEntity mulPayBillEntity = mulPayBillDao.getLatestEntityByAccessInfo(inputParam.getAccessId(),
                                                                                          inputParam.getAccessPass()
                                                                                         );
            bufOrderId = mulPayBillEntity.getOrderId();
            inputParam.setOrderId(bufOrderId);

            /*
             * メソッド実行
             */
            ExecTranOutput outputParam = (ExecTranOutput) inv.proceed();

            /*
             * 後処理
             */
            MulPayBillEntity mulPayBill = copyOutputProp(inputParam, outputParam);
            mulPayBill.setPayType(PAYTYPE_CREDIT);
            mulPayBill.setTranType("ExecTran");
            // TODO 暫定対応　copyOutputPropでoutputObjectのコピー時にOrderIdがnullで上書きされるため、こちらで再セット
            //  ※現行(3.6.1)とBeanUtilsが異なるためnullで上書きされる。また、現行でもChangeTranやAlterTranで同様にsetterを呼び出しているのは謎
            //  ※ClickUP：https://app.clickup.com/t/31nadcw
            mulPayBill.setOrderId(bufOrderId);
            // エラー内容を取得
            if (outputParam.isErrorOccurred()) {
                setErrorOutput(mulPayBill, outputParam.getErrList());
            }
            // マルチペイメント請求TBL登録
            int result = mulPayBillDao.insert(mulPayBill);
            if (result == 0) {
                LOGGER.warn("マルチペイメント請求TBLへの登録件数0件");
            }

            return outputParam;

        } finally {
            LOGGER.info("interceptDoExecTran:END");
        }
    }

    /**
     * クレジット決済認証後決済実行インターセプター
     *
     * @param inv 実行メソッド
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoSecureTran2(ProceedingJoinPoint inv) throws Throwable {
        try {
            LOGGER.info("interceptDoSecureTran2:START");

            /*
             * メソッド実行
             */
            SecureTran2Output outputParam = (SecureTran2Output) inv.proceed();

            /*
             * 後処理
             */
            // orderIdの取得チェック
            if (outputParam.getOrderId() == null) {
                // nullの場合は、マルチペイメント請求TBLには登録しない。
                LOGGER.warn("出力パラメータのオーダーＩＤがnullの為、マルチペイメント請求TBLへの登録無し");
                return outputParam;
            }

            // MulPayBillエンティティの取得
            MulPayBillEntity oldMulPayBillEntity =
                            mulPayBillDao.getLatestNoErrorEntityByOrderId(outputParam.getOrderId());

            // 引数を取得します。（SecureTran2Input)
            Object[] obj = inv.getArgs();
            SecureTran2Input inputParam = (SecureTran2Input) obj[0];

            MulPayBillEntity mulPayBillEntity = copyOutputProp(inputParam, outputParam);
            mulPayBillEntity.setPayType(PAYTYPE_CREDIT);
            mulPayBillEntity.setTranType("SecureTran2");

            CopyUtil.copyPropertiesIfDestIsNull(oldMulPayBillEntity, mulPayBillEntity, null);

            mulPayBillEntity.setMulPayBillSeq(null);

            // エラー内容を取得
            if (outputParam.isErrorOccurred()) {
                setErrorOutput(mulPayBillEntity, outputParam.getErrList());
            }
            // マルチペイメント請求TBL登録
            int result = mulPayBillDao.insert(mulPayBillEntity);
            if (result == 0) {
                LOGGER.warn("マルチペイメント請求TBLへの登録件数0件");
            }

            return outputParam;

        } finally {
            LOGGER.info("interceptDoSecureTran2:END");
        }
    }

    /**
     * クレジット決済-決済変更インターセプター
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoAlterTran(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoAlterTran:START");

            /*
             * 前処理
             */
            // 引数を取得します。（AlterTranInput)
            Object[] obj = inv.getArgs();
            HmAlterTranInput inputParam = (HmAlterTranInput) obj[0];

            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            if (inputParam.getShopId() == null) {
                inputParam.setShopId(mulPayShopEntity.getShopId());
            }
            if (inputParam.getShopPass() == null) {
                inputParam.setShopPass(mulPayShopEntity.getShopPass());
            }

            MulPayBillEntity mulPayBillEntity = mulPayBillDao.getLatestEntityByAccessInfo(inputParam.getAccessId(),
                                                                                          inputParam.getAccessPass()
                                                                                         );
            String bufOrderId = null;
            bufOrderId = mulPayBillEntity.getOrderId();

            /*
             * メソッド実行
             */
            AlterTranOutput outputParam = (AlterTranOutput) inv.proceed();

            // 処理済みの通信結果を保持
            CommunicateResult communicateResult = ApplicationContextUtility.getBean(CommunicateResult.class);
            boolean successFlag = !outputParam.isErrorOccurred();
            HTypeJobCode jobcode = EnumTypeUtil.getEnumFromValue(HTypeJobCode.class, inputParam.getJobCd());
            if (HTypeJobCode.SALES == jobcode) {
                communicateResult.addSaleFixTran(bufOrderId, successFlag);
            } else {
                communicateResult.addCancelTran(bufOrderId, successFlag);
            }

            /*
             * 後処理
             */
            MulPayBillEntity mulPayBill = copyOutputProp(inputParam, outputParam);
            mulPayBill.setPayType(PAYTYPE_CREDIT);
            mulPayBill.setTranType("AlterTran");
            mulPayBill.setOrderId(bufOrderId);
            // エラー内容を取得
            if (outputParam.isErrorOccurred()) {
                setErrorOutput(mulPayBill, outputParam.getErrList());
            }

            // マルチペイメント請求TBL登録
            int result = mulPayBillDao.insert(mulPayBill);
            if (result == 0) {
                LOGGER.warn("マルチペイメント請求TBLへの登録件数0件");
            }

            return outputParam;
        } finally {
            LOGGER.info("interceptDoAlterTran:END");
        }
    }

    /**
     * クレジット決済-金額変更インターセプター
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoChangeTran(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoChangeTran:START");
            /*
             * 前処理
             */
            // 引数を取得します。（ChangeTranInput)
            Object[] obj = inv.getArgs();
            HmChangeTranInput inputParam = (HmChangeTranInput) obj[0];

            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            if (inputParam.getShopId() == null) {
                inputParam.setShopId(mulPayShopEntity.getShopId());
            }
            if (inputParam.getShopPass() == null) {
                inputParam.setShopPass(mulPayShopEntity.getShopPass());
            }
            MulPayBillEntity mulPayBillEntity = mulPayBillDao.getLatestEntityByAccessInfo(inputParam.getAccessId(),
                                                                                          inputParam.getAccessPass()
                                                                                         );
            String bufOrderId = null;
            bufOrderId = mulPayBillEntity.getOrderId();

            /*
             * メソッド実行
             */
            ChangeTranOutput outputParam = (ChangeTranOutput) inv.proceed();

            // 処理済みの通信結果を保持
            CommunicateResult communicateResult = ApplicationContextUtility.getBean(CommunicateResult.class);
            boolean successFlag = !outputParam.isErrorOccurred();
            communicateResult.addChangeTran(bufOrderId, successFlag);

            /*
             * 後処理
             */
            MulPayBillEntity mulPayBill = copyOutputProp(inputParam, outputParam);
            mulPayBill.setPayType(PAYTYPE_CREDIT);
            mulPayBill.setTranType("ChangeTran");
            mulPayBill.setOrderId(bufOrderId);
            // エラー内容を取得
            if (outputParam.isErrorOccurred()) {
                setErrorOutput(mulPayBill, outputParam.getErrList());
            }

            // マルチペイメント請求TBL登録
            int result = mulPayBillDao.insert(mulPayBill);
            if (result == 0) {
                LOGGER.warn("マルチペイメント請求TBLへの登録件数0件");
            }

            return outputParam;
        } finally {
            LOGGER.info("interceptDoChangeTran:END");
        }
    }

    /**
     * クレジットの状況確認用 SearchTrade 用インタセプト処理。
     * MulPayBillへの保存要求がある場合に保存処理を行う
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoSearchTrade(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {

        // メソッドを実行します。
        SearchTradeOutput outputDto = (SearchTradeOutput) this.interceptDoOtherPaymentMethod(inv, shopSeq);

        // エラーが含まれる場合はそのまま返す
        if (outputDto == null || outputDto.isErrorOccurred()) {
            return outputDto;
        }

        return outputDto;
    }

    /**
     * 会員照会用 SearchMember 用インタセプト処理。
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoSearchMember(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {

        try {
            LOGGER.info("interceptDoSearchMember:START");

            /*
             * 前処理
             */
            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmSearchMemberInput inputParam = (HmSearchMemberInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            obj[0] = inputParam;

            /*
             * メソッド実行
             */
            SearchMemberOutput outputParam = (SearchMemberOutput) inv.proceed();

            return outputParam;

        } finally {
            LOGGER.info("interceptDoSearchMember:END");
        }
    }

    /**
     * 会員登録用 SaveMember 用インタセプト処理。
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoSaveMember(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {

        try {
            LOGGER.info("interceptDoSaveMember:START");

            /*
             * 前処理
             */
            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmSaveMemberInput inputParam = (HmSaveMemberInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            obj[0] = inputParam;

            /*
             * メソッド実行
             */
            SaveMemberOutput outputParam = (SaveMemberOutput) inv.proceed();

            return outputParam;

        } finally {
            LOGGER.info("interceptDoSaveMember:END");
        }
    }

    /**
     * 会員削除用 doDeleteMember 用インタセプト処理
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoDeleteMember(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoDeleteMember:START");

            // --- 前処理

            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmDeleteMemberInput inputParam = (HmDeleteMemberInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            // MulPayShopエンティティの取得→inputParamへセット
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            // ショップ設定情報を入力パラメータへコピー
            CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, inputParam, null);

            obj[0] = inputParam;

            // メソッド実行
            DeleteMemberOutput outputParam = (DeleteMemberOutput) inv.proceed();

            return outputParam;

        } finally {
            LOGGER.info("interceptDoDeleteMember:END");
        }
    }

    /**
     * 決済後カード登録用 doTradedCard 用インタセプト処理
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoTradedCard(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoTradedCard:START");

            // --- 前処理

            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmTradedCardInput inputParam = (HmTradedCardInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            // MulPayShopエンティティの取得→inputParamへセット
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            // ショップ設定情報を入力パラメータへコピー
            CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, inputParam, null);

            obj[0] = inputParam;

            // メソッド実行
            TradedCardOutput outputParam = (TradedCardOutput) inv.proceed();

            return outputParam;
        } finally {
            LOGGER.info("interceptDoTradedCard:END");
        }
    }

    /**
     * カード照会用 doSearchCard 用インタセプト処理
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoSearchCard(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoSearchCard:START");

            // --- 前処理

            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmSearchCardInput inputParam = (HmSearchCardInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            // MulPayShopエンティティの取得→inputParamへセット
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            // ショップ設定情報を入力パラメータへコピー
            CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, inputParam, null);

            obj[0] = inputParam;

            // メソッド実行
            SearchCardOutput outputParam = (SearchCardOutput) inv.proceed();

            return outputParam;
        } finally {
            LOGGER.info("interceptDoSearchCard:END");
        }
    }

    /**
     * カード登録用 doSaveCard 用インタセプト処理
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoSaveCard(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoSaveCard:START");

            // --- 前処理

            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmSaveCardInput inputParam = (HmSaveCardInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            // MulPayShopエンティティの取得→inputParamへセット
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            // ショップ設定情報を入力パラメータへコピー
            CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, inputParam, null);

            obj[0] = inputParam;

            // メソッド実行
            SaveCardOutput outputParam = (SaveCardOutput) inv.proceed();

            return outputParam;
        } finally {
            LOGGER.info("interceptDoSaveCard:END");
        }
    }

    /**
     * カード削除用 doDeleteCard 用インタセプト処理
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoDeleteCard(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {
        try {
            LOGGER.info("interceptDoDeleteCard:START");

            // --- 前処理

            // 引数を取得します。（ExecTranInput)
            Object[] obj = inv.getArgs();
            HmDeleteCardInput inputParam = (HmDeleteCardInput) obj[0];
            // MulPaySiteを取得
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();
            CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, inputParam, null);

            inputParam.setSitePass(mulPaySiteEntity.getSitePassword());

            // MulPayShopエンティティの取得→inputParamへセット
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            // ショップ設定情報を入力パラメータへコピー
            CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, inputParam, null);

            obj[0] = inputParam;

            // メソッド実行
            DeleteCardOutput outputParam = (DeleteCardOutput) inv.proceed();

            return outputParam;
        } finally {
            LOGGER.info("interceptDoDeleteCard:END");
        }
    }

    /**
     * マルチペイメントAPI用インターセプター（特定API以外）
     *
     * @param inv     実行メソッド
     * @param shopSeq ショップSEQ
     * @return メソッド実行戻り値
     * @throws Throwable 例外
     */
    protected Object interceptDoOtherPaymentMethod(ProceedingJoinPoint inv, Integer shopSeq) throws Throwable {

        try {
            LOGGER.info("interceptDoOtherPaymentMethod:START");

            // メソッドパラメータから、引数取得
            Object obj = inv.getArgs()[0];

            // サイト設定、ショップ設定情報をinputParamへコピーする
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
            MulPaySiteEntity mulPaySiteEntity = mulPaySiteDao.getEntity();

            if (isInputParmName(obj.getClass().getName())) {

                CopyUtil.copyPropertiesIfDestIsNull(mulPayShopEntity, obj, null);
                CopyUtil.copyPropertiesIfDestIsNull(mulPaySiteEntity, obj, null);
                Class<?> clazz = obj.getClass();

                Method getMethod = null;
                Method setMethod = null;

                // orderIdメソッドを取り出す。
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals("getOrderId")) {
                        getMethod = method;
                        setMethod = clazz.getMethod("setOrderId", method.getReturnType());
                        Object objOrderId = getMethod.invoke(obj);

                        if (objOrderId != null) {
                            setMethod.invoke(obj, objOrderId);
                        }

                        break;
                    }
                }
            }

            // メソッドを実行します。
            Object retObj = inv.proceed();

            return retObj;
        } finally {
            LOGGER.info("interceptDoOtherPaymentMethod:END");
        }

    }

    /**
     * 引数オブジェクトが対象のものであるか判定
     * オブジェクト名が○○○Inputである場合は処理対象とする
     *
     * @param inputName 入力パラメータオブジェクト名
     * @return true:処理対象、false：非処理対象
     */
    protected boolean isInputParmName(String inputName) {

        return inputName.endsWith("Input");
    }

    /**
     * パラメータ移送と時刻セット（クレジット決済用）
     * 入力パラメータ、出力パラメータを登録用エンティティにデータ移送する。
     *
     * @param inputObject  入力パラメータ
     * @param outputObject 出力パラメータ
     * @return マルチペイメント請求エンティティ
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected MulPayBillEntity copyOutputProp(Object inputObject, Object outputObject)
                    throws IllegalAccessException, InvocationTargetException {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        MulPayBillEntity mulPayBillEntity = ApplicationContextUtility.getBean(MulPayBillEntity.class);
        // プロパティ移送
        BeanUtils.copyProperties(mulPayBillEntity, inputObject);
        BeanUtils.copyProperties(mulPayBillEntity, outputObject);
        // 現在時刻のセット
        mulPayBillEntity.setRegistTime(dateUtility.getCurrentTime());
        mulPayBillEntity.setUpdateTime(dateUtility.getCurrentTime());

        return mulPayBillEntity;

    }

    /**
     * エラー情報設定メソッド
     * レスポンスからエラー情報を取得し、エンティティに設定します。
     *
     * @param mulPayBillEntity マルチペイメント請求エンティティ
     * @param errorList        エラーリスト
     * @return マルチペイメント請求エンティティ
     */
    protected MulPayBillEntity setErrorOutput(MulPayBillEntity mulPayBillEntity, List<?> errorList) {

        StringBuilder strBuffErrInfo = new StringBuilder();
        StringBuilder strBuffErrCode = new StringBuilder();

        int index = 0;
        @SuppressWarnings("unchecked")
        List<ErrHolder> errorList2 = (List<ErrHolder>) errorList;
        for (ErrHolder holder : errorList2) {
            if (index > 0) {
                strBuffErrInfo.append("|");
                strBuffErrCode.append("|");
            }
            strBuffErrInfo.append(holder.getErrInfo());
            strBuffErrCode.append(holder.getErrCode());
            index++;
        }

        mulPayBillEntity.setErrInfo(strBuffErrInfo.toString());
        mulPayBillEntity.setErrCode(strBuffErrCode.toString());

        return mulPayBillEntity;

    }

    /**
     * 文字列型のNULLチェック
     *
     * @param str チェック対象文字列
     * @return true:NULL false:NOT NULL
     */
    protected boolean isNull(String str) {
        return str == null;
    }
}