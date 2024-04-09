/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListGetRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Optional;

/**
 * 処理履歴一覧コントローラー
 *
 * @author kimura
 */
@RequestMapping("/order/details/processhistory")
@Controller
@SessionAttributes(value = "orderProcessHistoryModel")
@PreAuthorize("hasAnyAuthority('ORDER:4')")
public class OrderProcessHistoryController extends AbstractController {

    /** 不正操作 */
    public static final String MSGCD_REFERER_FAIL = "AOX000901";

    /** 取引API */
    private final TransactionApi transactionApi;

    /** helper */
    private final OrderProcessHistoryHelper orderProcesshistoryHelper;

    /** コンストラクタ */
    @Autowired
    public OrderProcessHistoryController(TransactionApi transactionApi,
                                         OrderProcessHistoryHelper orderProcesshistoryHelper) {
        this.transactionApi = transactionApi;
        this.orderProcesshistoryHelper = orderProcesshistoryHelper;
    }

    /**
     * 初期処理
     *
     * @param orderCode                受注番号
     * @param orderProcessHistoryModel 処理履歴一覧モデル
     * @param model                    モデル
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> orderCode,
                                 OrderProcessHistoryModel orderProcessHistoryModel,
                                 Model model) {

        String orderCodeData = null;
        if (orderCode.isPresent()) {
            orderCodeData = orderCode.get();
            orderProcessHistoryModel.setOrderCode(orderCodeData);
        }
        if (orderCodeData == null) {
            throwMessage(MSGCD_REFERER_FAIL);
        }

        // 一覧取得
        ProcessHistoryListGetRequest processHistoryListGetRequest = new ProcessHistoryListGetRequest();
        processHistoryListGetRequest.setOrderCode(orderCodeData);
        ProcessHistoryListResponse processHistoryListResponse =
                        this.transactionApi.getProcessHistoryList(processHistoryListGetRequest);

        if (ObjectUtils.isEmpty(processHistoryListResponse) || CollectionUtils.isEmpty(
                        processHistoryListResponse.getProcessHistoryList())) {
            throwMessage(MSGCD_REFERER_FAIL);
        }

        this.orderProcesshistoryHelper.toPageItems(
                        orderProcessHistoryModel, processHistoryListResponse.getProcessHistoryList());

        return "order/details/processhistory";
    }

}