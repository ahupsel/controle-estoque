package br.com.hupsel.controleestoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.hupsel.controleestoque")
@EnableCaching
public class ControleEstoqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleEstoqueApplication.class, args);
    }
}
