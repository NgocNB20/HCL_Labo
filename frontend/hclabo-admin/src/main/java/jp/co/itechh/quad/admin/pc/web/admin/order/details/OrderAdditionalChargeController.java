/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.MessageUtils;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.ValidatorMessage;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.AddAdjustmentAmountOfTransactionForRevisionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 追加料金ページアクション
 */
@RequestMapping("/order/details/additionalcharge")
@Controller
@SessionAttributes({"orderAdditionalChargeModel", "detailsUpdateCommonModel"})
@PreAuthorize("hasAnyAuthority('ORDER:8')")
public class OrderAdditionalChargeController extends AbstractOrderDetailsController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAdditionalChargeController.class);

    /** フロントチェック：非同期追加失敗*/
    private static final String MSGCD_AJAX_ADD_ADDITIONAL_CHARGE = "AOX002306";

    /** 受注詳細修正 */
    public static final String FLASH_UPDATE_MODEL = "redirectDetailsUpdateModel";

    /** 取引API */
    private final TransactionApi transactionApi;

    /**
     * コンストラクタ
     */
    @Autowired
    public OrderAdditionalChargeController(TransactionApi transactionApi) {
        this.transactionApi = transactionApi;
    }

    /**
     * 追加料金反映
     *
     * @param orderAdditionalChargeModel
     */
    @PostMapping(value = "/doUpdate")
    @ResponseBody
    public ResponseEntity<?> doUpdateAjax(@Validated OrderAdditionalChargeModel orderAdditionalChargeModel,
                                          BindingResult error,
                                          DetailsUpdateCommonModel detailsUpdateCommonModel,
                                          RedirectAttributes redirectAttrs,
                                          Model model) {

        List<ValidatorMessage> mapError = new ArrayList<>();

        // エラーチェック
        if (error.hasErrors()) {
            mapError = MessageUtils.getMessageErrorFromBindingResult(error);
            return ResponseEntity.badRequest().body(mapError);
        }

        // 改訂用取引に調整金額を追加する
        AddAdjustmentAmountOfTransactionForRevisionRequest request =
                        new AddAdjustmentAmountOfTransactionForRevisionRequest();
        request.setTransactionRevisionId(detailsUpdateCommonModel.getTransactionRevisionId());
        request.setAdjustName(orderAdditionalChargeModel.getInputAdditionalDetailsName());
        request.setAdjustPrice(Integer.valueOf(orderAdditionalChargeModel.getInputAdditionalDetailsPrice()));
        try {
            transactionApi.addAdjustmentAmountOfTransactionForRevision(request);
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            List<ValidatorMessage> messageList = new ArrayList<>();
            MessageUtils.getAllMessage(messageList, MSGCD_AJAX_ADD_ADDITIONAL_CHARGE, null);
            return ResponseEntity.badRequest().body(messageList);
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttrs, model);
            return ResponseEntity.badRequest().body("redirect:/error");
        }

        // Modelをセッションより破棄
        clearModel(OrderAdditionalChargeModel.class, orderAdditionalChargeModel, model);

        return ResponseEntity.ok(mapError);
    }
}