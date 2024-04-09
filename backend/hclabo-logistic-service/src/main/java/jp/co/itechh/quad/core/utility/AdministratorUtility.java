/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 管理者業務ヘルパークラス
 */
@Component
public class AdministratorUtility {

    private final AdministratorApi administratorApi;

    @Autowired
    public AdministratorUtility(AdministratorApi administratorApi) {
        this.administratorApi = administratorApi;
    }

    /**
     * 管理者氏名を返却
     *
     * @param administratorSeq 管理者SEQ
     * @return 管理者氏名
     */
    public String getAdministratorName(Integer administratorSeq) {
        if (administratorSeq == null) {
            return null;
        }

        AdministratorResponse administratorResponse = administratorApi.getByAdministratorSeq(administratorSeq);
        if (administratorResponse == null) {
            return null;
        }
        String lastName = administratorResponse.getAdministratorLastName();
        String firstName = administratorResponse.getAdministratorFirstName();
        if (StringUtils.isNotEmpty(lastName) && StringUtils.isNotEmpty(firstName)) {
            return lastName + "　" + firstName;
        }
        // 以下はありえないはず
        if (StringUtils.isNotEmpty(lastName)) {
            return lastName;
        }
        if (StringUtils.isNotEmpty(firstName)) {
            return firstName;
        }
        return null;
    }

}