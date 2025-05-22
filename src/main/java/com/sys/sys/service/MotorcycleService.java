package com.sys.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.sys.model.Motorcycle;
import com.sys.sys.model.Yard;
import com.sys.sys.repository.MotorcycleRepository;
import com.sys.sys.repository.YardRepository;

@Service
public class MotorcycleService {

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @Autowired
    private YardRepository yardRepository;

    public Motorcycle saveMotorcycle(Motorcycle motorcycle) {
        if (motorcycleRepository.existsByPlaca(motorcycle.getPlaca())) {
            throw new IllegalArgumentException("Já existe uma moto cadastrada com essa placa.");
        }

        Yard yard = motorcycle.getPatio();

        if (yard != null) {
            Yard persistedYard = yardRepository.findById(yard.getId())
                .orElseThrow(() -> new IllegalArgumentException("Pátio não encontrado."));

            long totalMotosNoPatio = motorcycleRepository.countByPatio(persistedYard);

            if (totalMotosNoPatio >= persistedYard.getCapacidadeTotal()) {
                throw new IllegalStateException("O pátio está cheio. Capacidade máxima atingida.");
            }

            motorcycle.setPatio(persistedYard);
        }

        return motorcycleRepository.save(motorcycle);
    }
}
