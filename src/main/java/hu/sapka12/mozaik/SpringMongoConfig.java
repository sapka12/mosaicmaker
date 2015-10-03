package hu.sapka12.mozaik;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan(basePackages =
{
    "hu.sapka12.mozaik"
})
@PropertySource("classpath:config.properties")
@EnableMongoRepositories
public class SpringMongoConfig extends AbstractMongoConfiguration
{

    @Value("${mongodb.host}")
    private String mongoHost;

    @Value("${mongodb.port}")
    private int mongoPort;

    @Value("${mongodb.db}")
    private String mongoDb;

    @Value("${mongodb.user}")
    private String mongoUser;

    @Value("${mongodb.password}")
    private String mongoPassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    protected String getDatabaseName()
    {
        return mongoDb;
    }

    @Override
    public Mongo mongo() throws Exception
    {
        if (StringUtils.isEmpty(mongoPassword))
        {
            return new MongoClient(mongoHost, mongoPort);
        } else
        {
            MongoCredential credential = MongoCredential.createCredential(mongoUser, mongoDb, mongoPassword.toCharArray());
            return new MongoClient(new ServerAddress(mongoHost, mongoPort), Arrays.asList(credential));
        }
    }
}
