package br.senai.sp.cfp138.clinicguia.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.senai.sp.cfp138.clinicguia.interceptor.AppInterceptor;

@Configuration
public class AppConfig implements WebMvcConfigurer{

	@Autowired
	private AppInterceptor interceptor;
	
	
	 @Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		 registry.addInterceptor(interceptor);
	}
	// metodo de conexao

		@Bean
		public DataSource dataSource() {

			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			//URL do banco de dados
			dataSource.setUrl("jdbc:mysql://localhost:3307/clinicguia");
			dataSource.setUsername("root");
			dataSource.setPassword("root");
			return dataSource;
		}

		// configura o hibernate (ORM - Mapeamento Objeto Relacional)

		@Bean
		public JpaVendorAdapter jpaVendorAdapter() {
			HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
			adapter.setDatabase(Database.MYSQL);
			//dialeto usado
			adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
			adapter.setShowSql(true);
			//permite criar a conexão sozinho
			adapter.setPrepareConnection(true);
			// Linguagem de definição de dados permite criar as tabelas sozinho
			adapter.setGenerateDdl(true);
			return adapter;
		}

}
