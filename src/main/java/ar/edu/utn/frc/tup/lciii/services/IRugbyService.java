package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.PoolResponseDTO;

import java.util.List;

public interface IRugbyService {
    List<PoolResponseDTO> GetAllPools();

    PoolResponseDTO GetPoolById(char poolId);
}
