package org.itstep.config;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
@EnableTransactionManagement // <tx:annotation-driven transaction-manager="transactionManager" />
@Configuration
@PropertySource("classpath:db.properties")
public class JpaConfig {
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.url}")
    private String url;
    // Database
    /*
       <jdbc:embedded-database type="H2" id="h2DataSource"/>
       <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource"
        id="mariaDbDataSource">
            <property name="username" value="todoapp"/>
            <property name="password" value="qwerty"/>
            <property name="url" value="jdbc:mariadb://localhost/todolist"/>
            <property name="driverClassName" value="org.mariadb.jdbc.Driver" />
        </bean>
     */
    @Bean
    public DataSource getDataSource() {
          // MariaDb
//        var dataSource = new DriverManagerDataSource(url, username, password);
//        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
//        return dataSource;
          // h2 - <jdbc:embedded-database type="H2" id="h2DataSource"/>
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    // entityManagerFactory
        /*
        <bean id="entityManagerFactory"
          class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
            <property name="dataSource" ref="mariaDbDataSource" />
            <property name="packagesToScan" value="org.itstep" />
            <property name="jpaPropertyMap">
                <map>
                    <entry key="hibernate.format_sql" value="true" />
                    <entry key="hibernate.dialect" value="org.hibernate.dialect.MariaDBDialect" />
 <!--   <entry key="hibernate.connection.driver_class" value="org.mariadb.jdbc.Driver"/>-->
                </map>
            </property>
            <property name="jpaVendorAdapter">
                <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                    <property name="generateDdl" value="true" />
                    <property name="showSql" value="true" />
                </bean>
            </property>
        </bean>
         */

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        var entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(getDataSource());
        entityManagerFactoryBean.setPackagesToScan("org.itstep");
        entityManagerFactoryBean.setJpaPropertyMap(Map.of(
                "hibernate.format_sql", "true"
        ));
        var jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        return entityManagerFactoryBean;
    }
    // Transaction
    // <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
    //        <property name="entityManagerFactory" ref="entityManagerFactory" />
    //    </bean>
    @Bean
    public JpaTransactionManager getTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}




