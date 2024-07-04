package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.CategoriaEntity;
import com.example.demo.repository.CategoriaRepository;


@Configuration
public class DataInitializer {
	

		@Autowired
	    private CategoriaRepository categoriaRepository;

		@Bean
	    public CommandLineRunner loadData() {
	    	return args -> {
		        if (categoriaRepository.count() == 0) {
		        	categoriaRepository.save(new CategoriaEntity(1L, "Lacteos"));
		        	categoriaRepository.save(new CategoriaEntity(2L, "Pastas"));
		        	categoriaRepository.save(new CategoriaEntity(3L, "Enlatados"));
		        }
	    	};
	    }  
	    	
}
