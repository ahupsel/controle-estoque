package br.com.hupsel.controleestoque.infraestrutura.memory;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryLeakSimulator {

    // ERRO INTENCIONAL: lista estática nunca liberada
    private static final List<byte[]> CACHE = new ArrayList<>();

    @PostConstruct
    public void simulateLeak() {
        // Simula crescimento infinito de memória
        for (int i = 0; i < 50; i++) {
            CACHE.add(new byte[1024 * 1024]); // 1MB
        }
        System.out.println(">>> Memory leak SIMULADO: " + CACHE.size() + " MB alocados");
    }
}
