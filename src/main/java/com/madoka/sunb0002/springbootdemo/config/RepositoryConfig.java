/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Sun Bo
 *
 */
@Configuration
@EnableJpaRepositories(transactionManagerRef = "sbshop-txnmgr", basePackages = {
		"com.madoka.sunb0002.springbootdemo.repositories" }, repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableJpaAuditing
@EnableTransactionManagement
public class RepositoryConfig {

	// Because I want to use "sbshop" name for EntityMgr and TxnMgr, I autowired
	// the EntityManager which is created by SpringBootStarter (not manually
	// created as bean as in old Spring)
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Bean("sbshop-entitymgr")
	public EntityManager entityManager() {
		return entityManagerFactory.createEntityManager();
	}

	@Bean("sbshop-txnmgr")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

}
