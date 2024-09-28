package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.PoolResponseDTO;
import ar.edu.utn.frc.tup.lciii.services.IRugbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * /rwc/2023/pools: To list all available pools with standings.
 * /rwc/2023/pools/{pool_id}: To retrieve standings for a specific pool.
 */

@RestController
@RequestMapping("/rwc/2023")
public class RugbyController {

    @Autowired
    private IRugbyService rugbyService;

    @GetMapping("/pools")
    public ResponseEntity<List<PoolResponseDTO>> GetAllPools(){
        List<PoolResponseDTO> response = rugbyService.GetAllPools();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pools/{pool_id}")
    public ResponseEntity<PoolResponseDTO> GetPoolById(@PathVariable char pool_id){
        PoolResponseDTO response = rugbyService.GetPoolById(pool_id);
        return ResponseEntity.ok(response);
    }
}
