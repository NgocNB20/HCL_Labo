/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest;

import com.gmo_pg.g_pay.client.output.ErrHolder;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dao.linkpay.SettlementMethodLinkDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPayBillDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPayShopDao;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayShopEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.IGetLinkplusUrlPaymentAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.BillingSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.OrderPaymentDao;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * LinkPay決済バリューエントリードメインサービス<br/>
 */
@Service
public class LinkPaymentService {

    /** GMO制約：注文者氏名桁数 */
    public static final int GMO_CHECK_ORDER_NAME_LIMIT = 20;

    /** GMO制約：注文者氏名カナ桁数 */
    public static final int GMO_CHECK_ORDER_KANA_LIMIT = 20;

    /** ショップSEQ */
    public static final int SHOP_SEQ = 1001;

    /** リンク決済結果Json Key */
    public static final String LINK_PAY_RESULT_JSON_KEY = "result=";

    /** メルペイカテゴリID */
    private static final String MERPAY_CATEGORYID = "1714";

    /** 住所アダプター */
    private final IAddressBookAdapter addressBookAdapter;

    /** マルチペイメントショップDao */
    private final MulPayShopDao mulPayShopDao;

    /** 後日払い識別子 */
    public static List<String> LATERDATEPAYMENTLIST = Arrays.asList("cvs", "payeasy", "ganb");

    /** 即時払い結果 */
    private static final String PAYSUCCESS = "PAYSUCCESS";

    /** 後日払い結果 */
    private static final String REQSUCCESS = "REQSUCCESS";

    /** マルチペイメント請求Dao */
    private final MulPayBillDao mulPayBillDao;

    /** 請求伝票Daoクラス */
    private final BillingSlipDao billingSlipDao;

    /** 注文決済Daoクラス */
    private final OrderPaymentDao orderPaymentDao;

    /** リンク決済個別決済手段 マスタDao */
    private final SettlementMethodLinkDao settlementMethodLinkDao;

    /** GMOリンク決済URL取得アダプター */
    private final IGetLinkplusUrlPaymentAdapter gmoGetLinkPaymentUrlAdapter;

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 変換Helper取得 */
    private final ConversionUtility conversionUtility;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** 受注アダプター */
    private final IOrderReceivedAdapter orderReceivedAdapter;

    /** 販売伝票アダプター */
    private final ISalesAdapter salesAdapter;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkPaymentService.class);

    /**
     * コンストラクタ
     */
    @Autowired
    public LinkPaymentService(IAddressBookAdapter addressBookAdapter,
                              IGetLinkplusUrlPaymentAdapter gmoGetLinkPaymentUrlAdapter,
                              MulPayShopDao mulPayShopDao,
                              MulPayBillDao mulPayBillDao,
                              BillingSlipDao billingSlipDao,
                              OrderPaymentDao orderPaymentDao,
                              SettlementMethodLinkDao settlementMethodLinkDao,
                              IBillingSlipRepository billingSlipRepository,
                              ConversionUtility conversionUtility,
                              MulPayProxyService mulPayProxyService,
                              DateUtility dateUtility,
                              IOrderReceivedAdapter orderReceivedAdapter,
                              ISalesAdapter salesAdapter) {
        this.addressBookAdapter = addressBookAdapter;
        this.gmoGetLinkPaymentUrlAdapter = gmoGetLinkPaymentUrlAdapter;
        this.mulPayShopDao = mulPayShopDao;
        this.mulPayBillDao = mulPayBillDao;
        this.billingSlipDao = billingSlipDao;
        this.orderPaymentDao = orderPaymentDao;
        this.settlementMethodLinkDao = settlementMethodLinkDao;
        this.billingSlipRepository = billingSlipRepository;
        this.conversionUtility = conversionUtility;
        this.mulPayProxyService = mulPayProxyService;
        this.dateUtility = dateUtility;
        this.orderReceivedAdapter = orderReceivedAdapter;
        this.salesAdapter = salesAdapter;
    }

    /**
     * GMO決済URL発行
     *
     * @param orderCode
     * @param billingAddressId
     * @param paymentPrice
     * @param returnUrl
     */
    public CreateGmoPaymentUrlDto createGmoPaymentUrl(String orderCode,
                                                      String billingAddressId,
                                                      int paymentPrice,
                                                      String returnUrl) {

        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);
        AssertChecker.assertNotEmpty("billingAddressId is empty", billingAddressId);
        AssertChecker.assertIntegerPositive("paymentPrice is zero or negative", paymentPrice);

        // 住所情報取得
        AddressBookAddressResponse billingAddressResponse = addressBookAdapter.getAddressById(billingAddressId);
        AssertChecker.assertNotNull("billingAddressResponse is null", billingAddressResponse);

        // 請求先氏名 取得
        String billingTargetName = billingAddressResponse.getLastName() + "　" + billingAddressResponse.getFirstName();

        // 請求先氏名カナ 取得
        String billingTargetKana = billingAddressResponse.getLastKana() + "　" + billingAddressResponse.getFirstKana();

        //設定ID 取得
        String settingId = PropertiesUtil.getSystemPropertiesValue("linkpay.setting.id");

        //決済可能期限 取得
        String expireMinutes = PropertiesUtil.getSystemPropertiesValue("linkpay.expire.time");
        Timestamp expireDateTmp = dateUtility.getDateBySubtractionMinutes(-Integer.parseInt(expireMinutes));
        String expireDate = dateUtility.format(expireDateTmp, "yyyyMMddHHmm");

        //マルチペイメントショップ 取得
        MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(SHOP_SEQ);

        /* URL発行パラメータ設定 */
        // geturlparam URL発行情報
        Map<String, String> geturlparam = new HashMap<>();
        geturlparam.put("ShopID", mulPayShopEntity.getShopId());
        geturlparam.put("ShopPass", mulPayShopEntity.getShopPass());

        // transaction 取引共通項目
        Map<String, String> transaction = new HashMap<>();
        transaction.put("OrderID", orderCode);
        transaction.put("Amount", String.valueOf(paymentPrice));
        transaction.put("RetUrl", returnUrl);
        transaction.put("PaymentExpireDate", expireDate);
        transaction.put("ResultSkipFlag", "1");

        // customer お客様情報
        Map<String, String> customer = new HashMap<>();
        customer.put("CustomerName", billingTargetName);
        customer.put("CustomerKana", billingTargetKana);
        customer.put("TelNo", billingAddressResponse.getTel());

        // メルペイ
        Map<String, Object> merpay = new HashMap<>();
        List<Map<String, String>> items = new ArrayList<>();
        Map<String, String> item = new HashMap<>();
        item.put("CategoryId", MERPAY_CATEGORYID);
        items.add(item);
        merpay.put("Items", items);

        CreateGmoPaymentUrlDto linkPaymentUrlResponse =
                        gmoGetLinkPaymentUrlAdapter.createGetLinkPaymentURL(geturlparam, transaction, customer,
                                                                            settingId, merpay
                                                                           );
        checkLinkPayCreateUrlOutput(linkPaymentUrlResponse);

        return linkPaymentUrlResponse;
    }

    /**
     * LinkPay決済URLエラーチェック
     *
     * @param linkPayResponse LinkPay決済URLパラメータ
     */
    public void checkLinkPayCreateUrlOutput(CreateGmoPaymentUrlDto linkPayResponse) {

        if (linkPayResponse.getErrList() == null && linkPayResponse.getWarnList() == null) {
            return;
        }

        // エラーがある場合
        if (!CollectionUtils.isEmpty(linkPayResponse.getErrList())) {

            ApplicationException appException = new ApplicationException();

            boolean isNoDefinedCode = false;
            List<String> errList = new ArrayList<>();
            for (ErrHolder errHolder : linkPayResponse.getErrList()) {

                errList.add(errHolder.getErrInfo());

                String message = PropertiesUtil.getSystemPropertiesValue(errHolder.getErrInfo());
                if (StringUtils.isBlank(message)) {
                    isNoDefinedCode = true;
                }
            }

            if (isNoDefinedCode) {
                appException.addMessage("PAYMENT_GMO_LINK_TYPE_PLUS_UNABLE_PROCEED");
            } else {
                for (ErrHolder errHolder : linkPayResponse.getErrList()) {
                    appException.addMessage(errHolder.getErrInfo());
                }
            }

            LOGGER.error(AppLevelFacesMessageUtil.getAllMessage("LMC000000", new String[] {errList.toString()})
                                                 .getMessage());

            throw appException;
        }

        // ワーニングがある場合はログ出力
        if (!CollectionUtils.isEmpty(linkPayResponse.getWarnList())) {
            LOGGER.warn("createLinkPaymentURL:" + AppLevelFacesMessageUtil.getAllMessage(
                            "LMC000000", new String[] {linkPayResponse.getWarnList().toString()}).getMessage());
            return;
        }
    }

    /**
     * GMOリンク決済結果受取
     *
     * @param linkPayJsonText リンク決済jsonテキスト
     * @return ReceiveLinkPaymentResultDto
     */
    public ReceiveLinkPaymentResultDto receiveLinkPaymentResult(String linkPayJsonText) {

        LOGGER.info("receiveLinkPaymentResult:" + linkPayJsonText);

        // リンク決済結果キー項目より後を切り出し
        int startIndex = linkPayJsonText.indexOf(LINK_PAY_RESULT_JSON_KEY);
        String[] resultData = (linkPayJsonText.substring(startIndex + LINK_PAY_RESULT_JSON_KEY.length())).split(
                        Pattern.quote("."));

        // デコード処理
        byte[] bytes = Base64.getUrlDecoder()
                             .decode(URLDecoder.decode(resultData[0], StandardCharsets.UTF_8).getBytes());
        String decodedJsonText = new String(bytes, StandardCharsets.UTF_8);
        LOGGER.info("receiveLinkPaymentResult decoded:" + decodedJsonText);

        // jsonオブジェクトへ変換
        JSONObject jsonObj = new JSONObject(decodedJsonText);

        // 戻り値用インスタンス
        ReceiveLinkPaymentResultDto receiveLinkPaymentResultDto = new ReceiveLinkPaymentResultDto();

        // HM取引ID 取得
        String transactionId = orderReceivedAdapter.getTransactionIdByOrderCodeLatest(
                        jsonObj.getJSONObject("transactionresult").optString("OrderID"));
        if (transactionId == null) {
            throw new DomainException("PAYMENT_GMO_LINK_TYPE_PLUS_UNABLE_PROCEED");
        } else {
            receiveLinkPaymentResultDto.setTransactionId(transactionId);
        }

        // HM請求伝票 取得
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        if (billingSlipEntity == null) {
            throw new DomainException("PAYMENT_GMO_LINK_TYPE_PLUS_UNABLE_PROCEED");
        }

        // リンク決済でない場合
        if (HTypeSettlementMethodType.LINK_PAYMENT != billingSlipEntity.getOrderPaymentEntity()
                                                                       .getSettlementMethodType()) {
            throw new DomainException("PAYMENT_LINK0002-E");
        }

        // 「GMOから決済せずに戻る」の判定処理
        //取引共通項目.取引ID = nullの場合は、レスポンスボディなしで400番で処理終了
        if (jsonObj.getJSONObject("transactionresult").optString("AccessID", null) == null) {
            receiveLinkPaymentResultDto.setNoProcessBack(true);
            return receiveLinkPaymentResultDto;
        }

        String paymethod = jsonObj.getJSONObject("transactionresult").optString("Paymethod", null);
        String result = jsonObj.getJSONObject("transactionresult").optString("Result", null);

        // 決済が完了していない場合
        if ((HTypePaymentLinkType.LATERDATEPAYMENT == getHTypePaymentLinkType(paymethod) && !REQSUCCESS.equals(result))
            || (HTypePaymentLinkType.IMMEDIATEPAYMENT == getHTypePaymentLinkType(paymethod) && !PAYSUCCESS.equals(
                        result))) {

            ApplicationException appException = new ApplicationException();
            // 取引ID受け渡し用メッセージ設定
            appException.addMessage("PAYMENT_LINK0001-I", new String[] {transactionId});

            // エラーメッセージ設定
            String errInfo = jsonObj.getJSONObject("transactionresult").optString("ErrInfo", null);
            if (errInfo != null) {
                String[] errInfos = errInfo.split("\\|");

                boolean isNoDefinedCode = false;
                for (String curErrInfo : errInfos) {
                    String message = PropertiesUtil.getSystemPropertiesValue(curErrInfo);
                    if (StringUtils.isBlank(message)) {
                        isNoDefinedCode = true;
                        break;
                    }
                }

                if (isNoDefinedCode) {
                    appException.addMessage("PAYMENT_GMO_LINK_TYPE_PLUS_UNABLE_PROCEED");
                } else {
                    for (String curErrInfo : errInfos) {
                        appException.addMessage(curErrInfo);
                    }
                }
            } else {
                appException.addMessage("PAYMENT_GMO_LINK_TYPE_PLUS_UNABLE_PROCEED");
            }

            throw appException;
        }

        // マルペイ請求エンティティデータ生成
        MulPayBillEntity mulPayBillEntity = createMulPayBillEntity(jsonObj, billingSlipEntity);

        // 取得データをマルチペイメント請求 テーブルへ登録する
        insertMullPayBillDao(mulPayBillEntity);

        // 即時払いの場合、マルチペイメント決済結果 テーブルへ登録
        if (HTypePaymentLinkType.IMMEDIATEPAYMENT == getHTypePaymentLinkType(mulPayBillEntity.getPayMethod())) {
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(SHOP_SEQ);
            mulPayProxyService.registMulpayResult(
                            createMulPayResultRequest(mulPayBillEntity, mulPayShopEntity.getShopId(),
                                                      mulPayShopEntity.getShopPass(), transactionId
                                                     ), "DIRECT");
        }

        receiveLinkPaymentResultDto.setTransactionId(transactionId);
        receiveLinkPaymentResultDto.setMulPayBillEntity(mulPayBillEntity);

        return receiveLinkPaymentResultDto;
    }

    /**
     * 後日払い支払期限切れを判定する
     *
     * @param transactionId 取引ID
     * @param today         yyyyMMdd の日付
     * @return True：期限切れ間近
     * False：期限切れ間近ではない
     */
    public Boolean getReminderExpired(String transactionId, Date today) {
        return (orderPaymentDao.getReminderExpiredTransactionId(transactionId, today) != null);
    }

    /**
     * 支払期限切れ間近を判定する
     *
     * @param transactionId 取引ID
     * @param thresholdDate yyyyMMdd の日付
     * @param today         yyyyMMdd の日付
     * @return True.. 支払期限切れ
     * False.. 支払期限切れではない
     */
    public Boolean getReminderPayment(String transactionId, Date thresholdDate, Date today) {
        return (orderPaymentDao.getReminderTargetOrderByTransactionId(transactionId, thresholdDate, today) != null);
    }

    /**
     * マルチペイメント請求に変換<br/>
     *
     * @param jsonObj           リンク決済jsonオブジェクト
     * @param billingSlipEntity 請求伝票エンティティ
     * @return マルチペイメント請求
     */
    private MulPayBillEntity createMulPayBillEntity(JSONObject jsonObj, BillingSlipEntity billingSlipEntity) {

        // リンク決済結果 取引共通項目の取得
        JSONObject transactionResultJsonObj = jsonObj.getJSONObject("transactionresult");
        // リンク決済結果 決済固有取引情報の取得
        JSONObject payMethodDetailJson = jsonObj.getJSONObject(transactionResultJsonObj.optString("Paymethod", null));
        // GMO決済方法取得
        SettlementMethodLinkEntity settlementMethodLinkEntity =
                        settlementMethodLinkDao.getSettlementMethodLinkByPayMethod(
                                        transactionResultJsonObj.optString("Paymethod"));
        // 販売伝票を取得
        SalesSlip salesSlip = salesAdapter.getSalesSlip(billingSlipEntity.getTransactionId());
        // 販売伝票が取得できない場合はエラー
        if (salesSlip == null) {
            throw new DomainException("PAYMENT_EPAU0001-E", new String[] {billingSlipEntity.getTransactionId()});
        }

        // マルチペイメント請求エンティティ 生成
        MulPayBillEntity mulPayBillEntity = new MulPayBillEntity();

        // マルチペイメント請求エンティティへパラメータ設定
        mulPayBillEntity.setPayType(settlementMethodLinkEntity.getPayType());
        // 決済手段識別子
        mulPayBillEntity.setPayMethod(transactionResultJsonObj.optString("Paymethod", null));
        // HM注文決済ID
        mulPayBillEntity.setOrderPaymentId(billingSlipEntity.getOrderPaymentEntity().getOrderPaymentId().getValue());
        // オーダーID
        mulPayBillEntity.setOrderId(transactionResultJsonObj.optString("OrderID", null));
        // (GMO)取引ID
        mulPayBillEntity.setAccessId(transactionResultJsonObj.optString("AccessID", null));
        // 取引パスワード
        mulPayBillEntity.setAccessPass(transactionResultJsonObj.optString("AccessPass", null));
        // リンクタイプPlus処理結果
        mulPayBillEntity.setResult(transactionResultJsonObj.optString("Result", null));
        // 利用金額
        mulPayBillEntity.setAmount(BigDecimal.valueOf(salesSlip.getBillingAmount()));
        // 決済日付（yyyy/mm/dd hh:MM:ss→yyyyMMddHHmmss）
        String processDate = transactionResultJsonObj.optString("Processdate", null);
        if (!StringUtils.isBlank(processDate)) {
            mulPayBillEntity.setTranDate(processDate.replaceAll("/|:| ", ""));
        }
        // エラーコード
        mulPayBillEntity.setErrCode(transactionResultJsonObj.optString("ErrCode", null));
        // エラー詳細コード
        mulPayBillEntity.setErrInfo(transactionResultJsonObj.optString("ErrInfo", null));

        if (payMethodDetailJson != null) {
            // ----コンビニ、ペイジー----
            // トランザクション ID
            mulPayBillEntity.setTranId(payMethodDetailJson.optString("TranID", null));
            // 確認番号
            if ("cvs".equals(mulPayBillEntity.getPayMethod())) {
                mulPayBillEntity.setConfNo(payMethodDetailJson.optString("CvsConfNo", null));
            } else if ("payeasy".equals(mulPayBillEntity.getPayMethod())) {
                mulPayBillEntity.setConfNo(payMethodDetailJson.optString("ConfNo", null));
            }
            // 支払期限日時
            mulPayBillEntity.setPaymentTerm(payMethodDetailJson.optString("PaymentTerm", null));

            // ----コンビニ----
            // 支払先コンビニコード
            mulPayBillEntity.setConvenience(payMethodDetailJson.optString("CvsCode", null));
            // 受付番号
            mulPayBillEntity.setReceiptNo(payMethodDetailJson.optString("CvsReceiptNo", null));

            // ----ペイジー----
            // お客様番号
            mulPayBillEntity.setCustId(payMethodDetailJson.optString("CustID", null));
            // 収納機関番号
            mulPayBillEntity.setBkCode(payMethodDetailJson.optString("BkCode", null));
            // 暗号化決済番号
            mulPayBillEntity.setEncryptReceiptNo(payMethodDetailJson.optString("EncryptReceiptNo", null));
            // ペイメントURL
            mulPayBillEntity.setPaymentURL(payMethodDetailJson.optString("PayeasyPaymentURL", null));

            // ----銀行振込（バーチャル口座あおぞら）----
            // 取引有効期限
            mulPayBillEntity.setExpireDate(payMethodDetailJson.optString("ExpireDate", null));
            // 振込事由
            mulPayBillEntity.setTradeReason(payMethodDetailJson.optString("TradeReason", null));
            // 振込依頼者氏名
            mulPayBillEntity.setTradeClientName(payMethodDetailJson.optString("TradeClientName", null));
            // 振込依頼者メールアドレス
            mulPayBillEntity.setTradeClientMailAddress(payMethodDetailJson.optString("TradeClientMailaddress", null));
            // 銀行コード
            mulPayBillEntity.setBankCode(payMethodDetailJson.optString("BankCode", null));
            // 銀行名
            mulPayBillEntity.setBankName(payMethodDetailJson.optString("BankName", null));
            // 支店コード
            mulPayBillEntity.setBranchCode(payMethodDetailJson.optString("BranchCode", null));
            // 支店名
            mulPayBillEntity.setBranchName(payMethodDetailJson.optString("BranchName", null));
            // 振込先口座種別
            mulPayBillEntity.setAccountType(payMethodDetailJson.optString("AccountType", null));
            // 振込先口座番号
            mulPayBillEntity.setAccountNumber(payMethodDetailJson.optString("AccountNumber", null));
        }

        // 登録日時
        mulPayBillEntity.setRegistTime(dateUtility.getCurrentTime());
        // 更新日時
        mulPayBillEntity.setUpdateTime(dateUtility.getCurrentTime());

        return mulPayBillEntity;
    }

    /**
     * マルチペイメント決済結果通知受付リクエスト モデルを生成
     *
     * @param *             @param jsonObj
     * @param shopId
     * @param shopPass
     * @param transactionId
     * @return マルチペイメント決済結果通知受付リクエストモデル
     */
    private MulPayResultRequest createMulPayResultRequest(MulPayBillEntity mulPayBillEntity,
                                                          String shopId,
                                                          String shopPass,
                                                          String transactionId) {

        MulPayResultRequest mulPayResultRequest = new MulPayResultRequest();

        // オーダーID
        mulPayResultRequest.setOrderId(mulPayBillEntity.getOrderId());
        // 現状態
        mulPayResultRequest.setStatus("PAYSUCCESS");
        // 利用金額
        mulPayResultRequest.setAmount(mulPayBillEntity.getAmount());
        // 処理日付
        mulPayResultRequest.setTranDate(mulPayBillEntity.getTranDate());
        // エラーコード
        mulPayResultRequest.setErrCode(mulPayBillEntity.getErrCode());
        // エラー詳細コード
        mulPayResultRequest.setErrInfo(mulPayBillEntity.getErrInfo());
        // 決済方法
        mulPayResultRequest.setPayType(mulPayBillEntity.getPayType());
        // 決済手段識別子
        mulPayResultRequest.setPaymethod(mulPayBillEntity.getPayMethod());

        return mulPayResultRequest;
    }

    /**
     * リンク決済タイプ判定<br/>
     *
     * @param payMethod 決済手段識別子
     * @return リンク決済タイプ
     */
    public static HTypePaymentLinkType getHTypePaymentLinkType(String payMethod) {

        if (LATERDATEPAYMENTLIST.contains(payMethod)) {
            return HTypePaymentLinkType.LATERDATEPAYMENT;
        } else {
            return HTypePaymentLinkType.IMMEDIATEPAYMENT;
        }
    }

    /**
     * マルチペイメント請求をインサート
     *
     * @param mulPayBillEntity マルチペイメント請求
     */
    private void insertMullPayBillDao(MulPayBillEntity mulPayBillEntity) {
        mulPayBillDao.insert(mulPayBillEntity);
    }

    /**
     * 指定された分を加算または減算したTimestamp型を返します。
     *
     * @param laterDateLimit 加算(減算)する量
     * @return 指定された分を加算(減算)したTimestamp
     */
    private Timestamp getLaterDateLimit(String laterDateLimit) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date parsedDate = formatter.parse(laterDateLimit);
            return conversionUtility.toTimestamp(parsedDate);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return null;
        }
    }

}