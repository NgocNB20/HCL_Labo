/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.auth;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 権限グループ一覧画面 Action クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/auth")
@Controller
@SessionAttributes(value = "administratorAuthModel")
@PreAuthorize("hasAnyAuthority('ADMIN:4')")
public class AdministratorAuthController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorAuthController.class);

    /**
     * 権限グループAPI
     **/
    private final AuthorizationApi authorizationApi;

    /**
     * Helper
     */
    public AdministratorAuthHelper administratorAuthHelper;

    /**
     * メタ権限情報。DICON で設定された値が自動バインドされる。必ず設定されている必要あり
     */
    private final List<MetaAuthType> metaAuthList;

    /**
     * コンストラクタ
     *
     * @param administratorAuthHelper 権限グループ一覧画面 Helper クラス
     * @param authorizationApi        権限グループAPI
     */
    @Autowired
    public AdministratorAuthController(AdministratorAuthHelper administratorAuthHelper,
                                       AuthorizationApi authorizationApi) {
        this.administratorAuthHelper = administratorAuthHelper;
        this.authorizationApi = authorizationApi;
        this.metaAuthList = (List<MetaAuthType>) ApplicationContextUtility.getApplicationContext()
                                                                          .getBean("metaAuthTypeList");
    }

    /**
     * 初期表示
     *
     * @param administratorAuthModel 権限グループ一覧画面
     * @param error                  the error
     * @param redirectAttrs          the redirect attrs
     * @param model                  the model
     * @return 遷移先ページ
     */
    @GetMapping(value = "")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/auth/index")
    protected String doLoadIndex(AdministratorAuthModel administratorAuthModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttrs,
                                 Model model) {

        //        super.doLoad();

        clearModel(AdministratorAuthModel.class, administratorAuthModel, model);

        // 権限グループ情報とメタ権限情報をページへ設定する
        Integer shopSeq = getCommonInfo().getCommonInfoBase().getShopSeq();

        try {
            AuthorizationListResponse authorizationListResponse = authorizationApi.get(null);
            List<AdminAuthGroupEntity> authList = administratorAuthHelper.toAdminAuthGroupEntityList(
                            authorizationListResponse.getAuthorizationResponseList(), shopSeq);
            administratorAuthHelper.toPageForLoad(authList, metaAuthList, administratorAuthModel);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        return "administrator/auth/index";
    }
}