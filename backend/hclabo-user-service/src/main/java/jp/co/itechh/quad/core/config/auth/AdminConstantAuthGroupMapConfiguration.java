/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.config.auth;

import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 規定権限グループ設定のコンフィグレーションクラス<br/>
 *
 * @author doanthang
 */
@Configuration
@PropertySource(value = {"classpath:config/hitmall/adminConstantAuthGroupMap.properties"},
                ignoreResourceNotFound = true, encoding = "UTF-8")
public class AdminConstantAuthGroupMapConfiguration {

    @Bean("adminConstantAuthGroupMap")
    public Map<Integer, List<AdminAuthGroupDetailEntity>> adminConstantAuthGroupMap() throws IOException {
        return getProperties();
    }

    private Map<Integer, List<AdminAuthGroupDetailEntity>> getProperties() throws IOException {
        String KEY_START = "adminConstantAuthGroup[";
        String KEY_1 = "].authAdminGroupSeq";

        String KEY_2_1 = "].authTypeCode[";
        String KEY_2_2 = "].authLevel[";

        String KEY_END = "]";

        Map<Integer, List<AdminAuthGroupDetailEntity>> map = new HashMap<>();

        Properties properties = new Properties();
        InputStream propertiesInputStream = AdminConstantAuthGroupMapConfiguration.class.getResourceAsStream(
                        "/config/hitmall/adminConstantAuthGroupMap.properties");
        InputStreamReader inputStreamReader = new InputStreamReader(propertiesInputStream, StandardCharsets.UTF_8);
        properties.load(inputStreamReader);
        int i = 0;
        while (properties.containsKey(KEY_START + i + KEY_1)) {
            List<AdminAuthGroupDetailEntity> authGroupDetailEntityList = new ArrayList<>();

            Integer authAdminGroupSeq = Integer.valueOf(properties.getProperty(KEY_START + i + KEY_1));

            int j = 0;
            while (properties.containsKey(KEY_START + i + KEY_2_1 + j + KEY_END)) {
                AdminAuthGroupDetailEntity entity = new AdminAuthGroupDetailEntity();

                String authTypeCode = properties.getProperty(KEY_START + i + KEY_2_1 + j + KEY_END);
                String authLevel = properties.getProperty(KEY_START + i + KEY_2_2 + j + KEY_END);

                entity.setAuthTypeCode(authTypeCode);
                entity.setAuthLevel(Integer.valueOf(authLevel));

                authGroupDetailEntityList.add(entity);

                j++;
            }
            map.put(authAdminGroupSeq, authGroupDetailEntityList);
            i++;
        }

        return map;
    }

}