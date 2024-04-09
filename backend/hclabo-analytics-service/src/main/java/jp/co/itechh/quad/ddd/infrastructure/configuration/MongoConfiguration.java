package jp.co.itechh.quad.ddd.infrastructure.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongo コンフィグレーション
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Configuration
public class MongoConfiguration {

    /**
     * mongoDbURL
     */
    @Value("${mongo.url}")
    private String mongoDbUrl;

    /**
     * mongoDb名
     */
    @Value("${mongo.name}")
    private String mongoDbName;

    /**
     * コントラクター
     *
     * @return MongoClient
     */
    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(mongoDbUrl);
        MongoClientSettings mongoClientSettings =
                        MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }

    /**
     * mongoTemplate
     *
     * @return MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), mongoDbName);
    }
}