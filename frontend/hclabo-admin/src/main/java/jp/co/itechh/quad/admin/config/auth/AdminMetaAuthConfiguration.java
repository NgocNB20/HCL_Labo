/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.config.auth;

import jp.co.itechh.quad.admin.dto.administrator.MetaAuthLevel;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 運営者権限グループに設定できる権限の種別と権限レベルを定義するコンフィグレーションクラス<br/>
 *
 * @author doanthang
 */
@Configuration
@PropertySource(value = {"classpath:config/hitmall/adminMetaAuth.properties"}, ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class AdminMetaAuthConfiguration {

    @Bean("metaAuthTypeList")
    public List<MetaAuthType> metaAuthTypeList() throws IOException {
        return getProperties();
    }

    private List<MetaAuthType> getProperties() throws IOException {
        String KEY_1 = "administrator.MetaAuthType[";
        String KEY_1_1 = "].authTypeCode";
        String KEY_1_2 = "].typeDisplayName";

        String KEY_2 = "].MetaAuthLevel[";
        String KEY_2_1 = "].metaLevel";
        String KEY_2_2 = "].levelDisplayName";

        List<MetaAuthType> metaAuthTypeList = new ArrayList<>();

        Properties properties = new Properties();
        InputStream propertiesInputStream = AdminMetaAuthConfiguration.class.getResourceAsStream(
                        "/config/hitmall/adminMetaAuth.properties");
        InputStreamReader inputStreamReader = new InputStreamReader(propertiesInputStream, StandardCharsets.UTF_8);
        properties.load(inputStreamReader);
        int i = 0;
        while (properties.containsKey(KEY_1 + i + KEY_1_1)) {
            String authTypeCode = properties.getProperty(KEY_1 + i + KEY_1_1);
            String typeDisplayName = properties.getProperty(KEY_1 + i + KEY_1_2);
            MetaAuthType metaAuthType = new MetaAuthType(authTypeCode, typeDisplayName);

            int j = 0;
            List<MetaAuthLevel> metaAuthLevelList = new ArrayList<>();
            while (properties.containsKey(KEY_1 + i + KEY_2 + j + KEY_2_1)) {
                String metaLevel = properties.getProperty(KEY_1 + i + KEY_2 + j + KEY_2_1);
                String levelDisplayName = properties.getProperty(KEY_1 + i + KEY_2 + j + KEY_2_2);
                MetaAuthLevel metaAuthLevel = new MetaAuthLevel();
                metaAuthLevel.setMetaLevel(Integer.valueOf(metaLevel));
                metaAuthLevel.setLevelDisplayName(levelDisplayName);
                metaAuthLevelList.add(metaAuthLevel);
                j++;
            }

            metaAuthType.setMetaAuthLevelList(metaAuthLevelList);
            metaAuthTypeList.add(metaAuthType);
            i++;
        }

        return metaAuthTypeList;
    }

}