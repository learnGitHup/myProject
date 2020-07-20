package com.example.common.mongoconf;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deng Xianghai
 * @create 2019-08-20 14:56
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.example.mapper.mongo",mongoTemplateRef = "rateMongo")
public class MongoTemplateConf {


    @Autowired
    @Qualifier("rateProperties")
    private MongoProperties preProperties;


    @Bean(name = "rateMongo")
    public MongoTemplate commonMongoTemplate()throws Exception{
        MongoDbFactory firstFactory= preFactory( this.preProperties);
        MappingMongoConverter converter=new MappingMongoConverter(firstFactory,new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(firstFactory,converter);
    }


    @Bean
    @Primary
    public MongoDbFactory preFactory(MongoProperties mongoProperties) throws Exception {

        ServerAddress serverAdress = new ServerAddress(mongoProperties.getHost(),mongoProperties.getPort());
        List<MongoCredential> mongoCredentials=new ArrayList<>();
        if (mongoProperties.getUsername() == null || mongoProperties.getAuthenticationDatabase() == null || mongoProperties.getPassword() == null){
            return new SimpleMongoDbFactory(new MongoClient(serverAdress,mongoCredentials), mongoProperties.getDatabase());
        }else {
            mongoCredentials.add(MongoCredential.createCredential(mongoProperties.getUsername(),mongoProperties.getAuthenticationDatabase(),mongoProperties.getPassword()));

            return new SimpleMongoDbFactory(new MongoClient(serverAdress,mongoCredentials), mongoProperties.getDatabase());
        }

    }






}
