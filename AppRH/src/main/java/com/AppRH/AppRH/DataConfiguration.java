package com.AppRH.AppRH;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DataConfiguration {
	
	@Bean
	public DataSource dataSource() {
		//Estancia o objeto
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		//Faz a chamada do driver de conexão com o banco.
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		//Faz a conexão com o banco (Necessario especificar o time zone para conexao).
		dataSource.setUrl("jdbc:mysql://localhost:3306/apprh?useTimezone=true&serverTimezone=UTC");
		
		//Adiciona o usuario e senha do banco de dados.
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		return dataSource; //Modificado para ler o data source.
	}
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabasePlatform("org.hibernate.dialect.MariaDBDialect");
		adapter.setPrepareConnection(true);
		return adapter;
	}
}
