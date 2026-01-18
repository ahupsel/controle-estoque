package br.com.hupsel.controleestoque.infraestrutura.memory;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MemoryLeakFixed {

    // Cache limitado com política de remoção
    private final Map<Integer, byte[]> cacheLimitado =
            new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, byte[]> eldest) {
                    return size() > 10; // limite de 10MB
                }
            };

    @PostConstruct
    public void simulateSafeUsage() {
        for (int i = 0; i < 50; i++) {
            cacheLimitado.put(i, new byte[1024 * 1024]);
        }
        System.out.println(">>> Cache seguro, tamanho atual: " + cacheLimitado.size() + " MB");
    }
}
