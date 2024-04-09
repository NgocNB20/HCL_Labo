package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.AdministratorDto;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.IAdministratorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 運営者アダプター.
 */
@Component
public class AdministratorAdapterImpl implements IAdministratorAdapter {

    /** 運営者Api */
    private final AdministratorApi administratorApi;

    /** 運営者アダプターHelperクラス */
    private final AdministratorAdapterHelper administratorAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param administratorApi           運営者Api
     * @param administratorAdapterHelper 運営者アダプターHelperクラス
     * @param headerParamsUtil           ヘッダパラメーターユーティル
     */
    @Autowired
    public AdministratorAdapterImpl(AdministratorApi administratorApi,
                                    AdministratorAdapterHelper administratorAdapterHelper,
                                    HeaderParamsUtility headerParamsUtil) {
        this.administratorApi = administratorApi;
        this.administratorAdapterHelper = administratorAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.administratorApi.getApiClient());
    }

    /**
     * 運営者取得
     *
     * @param administratorSeq 管理者SEQ
     * @return AdministratorDto
     */
    @Override
    public AdministratorDto get(Integer administratorSeq) {

        AdministratorResponse administratorResponse = administratorApi.getByAdministratorSeq(administratorSeq);

        return administratorAdapterHelper.toAdministratorDto(administratorResponse);
    }
}