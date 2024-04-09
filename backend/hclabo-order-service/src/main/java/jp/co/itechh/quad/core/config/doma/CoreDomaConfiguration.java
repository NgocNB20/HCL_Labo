/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.config.doma;

import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaConfig;
import org.seasar.doma.boot.autoconfigure.DomaConfigBuilder;
import org.seasar.doma.boot.autoconfigure.DomaProperties;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Doma2 設定クラス
 *
 * @author yt23807
 * @version $Revision: 1.0 $
 *
 */
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(DomaProperties.class)
@AutoConfigureAfter(DomaAutoConfiguration.class)
public class CoreDomaConfiguration {

    /**
     * DomaConfig上書き
     * @param dataSource
     * @param dialect
     * @param sqlFileRepository
     * @param naming
     * @param jdbcLogger
     * @param entityListenerProvider
     * @param domaConfigBuilder
     * @return
     */
    @Bean
    @Primary
    public DomaConfig config(DataSource dataSource,
                             Dialect dialect,
                             SqlFileRepository sqlFileRepository,
                             Naming naming,
                             JdbcLogger jdbcLogger,
                             EntityListenerProvider entityListenerProvider,
                             DomaConfigBuilder domaConfigBuilder,
                             DomaProperties domaProperties) {
        // @see org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration
        if (domaConfigBuilder.dataSource() == null) {
            domaConfigBuilder.dataSource(dataSource);
        }
        if (domaConfigBuilder.dialect() == null) {
            domaConfigBuilder.dialect(dialect);
        }
        if (domaConfigBuilder.sqlFileRepository() == null) {
            domaConfigBuilder.sqlFileRepository(sqlFileRepository);
        }
        if (domaConfigBuilder.naming() == null) {
            domaConfigBuilder.naming(naming);
        }
        if (domaConfigBuilder.jdbcLogger() == null) {
            domaConfigBuilder.jdbcLogger(jdbcLogger);
        }
        if (domaConfigBuilder.entityListenerProvider() == null) {
            domaConfigBuilder.entityListenerProvider(entityListenerProvider);
        }
        // UnknownColumnHandler上書き
        // UnknownColumnの発生を許容する
        domaConfigBuilder.unknownColumnHandler(new UnknownColumnHandler() {
            @Override
            public void handle(Query query, EntityType<?> entityType, String unknownColumnName) {
            }
        });

        return domaConfigBuilder.build();
    }
}