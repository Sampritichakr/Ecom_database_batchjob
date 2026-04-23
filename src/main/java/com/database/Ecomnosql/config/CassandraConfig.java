package com.database.Ecomnosql.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class CassandraConfig {

    // Grabbing the ZIP file from application.yaml
    @Value("${spring.cassandra.secure-connect-bundle}")
    private Resource bundle;

    // Grabbing your cloud username
    @Value("${spring.cassandra.username}")
    private String username;

    // Grabbing your cloud password
    @Value("${spring.cassandra.password}")
    private String password;

    // Grabbing the keyspace name
    @Value("${spring.cassandra.keyspace-name}")
    private String keyspace;

    // This completely hijacks the connection so it NEVER tries 127.0.0.1
    @Bean
    public CqlSession cqlSession() {
        try {
            return CqlSession.builder()
                    .withCloudSecureConnectBundle(bundle.getURL())
                    .withAuthCredentials(username, password)
                    .withKeyspace(keyspace)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to Astra Cloud. Check the ZIP file!", e);
        }
    }
}