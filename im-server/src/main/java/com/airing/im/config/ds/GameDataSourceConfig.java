package com.airing.im.config.ds;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = { "com.airing.im.dao.game" }, sqlSessionFactoryRef = "gameSqlSessionFactory")
public class GameDataSourceConfig {

    @Bean(name = "gameDataSource")
    @ConfigurationProperties(prefix = "mysql.datasource.game")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "gameSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("gameDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/game/**/*.xml"));

        return bean.getObject();
    }

    @Bean(name = "gameTransactionManager")
    public DataSourceTransactionManager getTransactionManager(@Qualifier("gameDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "gameSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(
            @Qualifier("gameSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
