package com.sys.sys.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sys.sys.model.Motorcycle;
import com.sys.sys.model.MotorcycleStatus;
import com.sys.sys.model.Yard;
import com.sys.sys.repository.MotorcycleRepository;
import com.sys.sys.repository.YardRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    @Autowired
    private YardRepository yardRepository;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @PostConstruct
    public void init() {
        // Criação dos pátios
        var yards = List.of(
            Yard.builder().name("Pátio Central").capacidadeTotal(50).build(),
            Yard.builder().name("Pátio Norte").capacidadeTotal(50).build(),
            Yard.builder().name("Pátio Sul").capacidadeTotal(50).build()
        );

        var savedYards = yardRepository.saveAll(yards);

        var brands = List.of("Honda", "Yamaha", "Suzuki", "Kawasaki", "BMW");
        var models = List.of("CG 160", "XJ6", "GSR 750", "Ninja 300", "GS 1200");
        var statusList = List.of(MotorcycleStatus.Funcional, MotorcycleStatus.Manutenção);

        var motos = new ArrayList<Motorcycle>();
        var random = new Random();

        for (Yard yard : savedYards) {
            for (int i = 0; i < 30; i++) {
                motos.add(Motorcycle.builder()
                    .placa(generateRandomPlate(random))
                    .marca(brands.get(random.nextInt(brands.size())))
                    .modelo(models.get(random.nextInt(models.size())))
                    .ano(2010 + random.nextInt(15))
                    .status(statusList.get(random.nextInt(statusList.size())))
                    .km(random.nextDouble() * 50000)
                    .patio(yard)
                    .build());
            }
        }

        motorcycleRepository.saveAll(motos);
    }

    private String generateRandomPlate(Random random) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        return String.format("%c%c%c%d%c%d%d",
            letters.charAt(random.nextInt(26)),
            letters.charAt(random.nextInt(26)),
            letters.charAt(random.nextInt(26)),
            random.nextInt(10),
            letters.charAt(random.nextInt(26)),
            random.nextInt(10),
            random.nextInt(10)
        );
    }
}