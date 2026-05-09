package com.outforce.desafio.service;

import com.outforce.desafio.domain.CuponDomain;
import com.outforce.desafio.domain.enums.CuponStatus;
import com.outforce.desafio.dto.CuponDTO;
import com.outforce.desafio.entity.CuponEntity;
import com.outforce.desafio.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuponService {


    private final CuponRepository cuponRepository;
    private final ModelMapper modelMapper;

    public CuponDTO create(CuponDTO cuponDTO) {

        CuponDomain cuponDomain = modelMapper.map(cuponDTO, CuponDomain.class);
        cuponDomain.validate();

        CuponEntity cuponEntity = modelMapper.map(cuponDomain, CuponEntity.class);

        CuponEntity save = cuponRepository.save(cuponEntity);
        cuponDTO.setId(save.getId());

        CuponDTO dtoRetorno = modelMapper.map(save, CuponDTO.class);

        return dtoRetorno;
    }


    public Optional<CuponDTO> get(String id) {
            Optional<CuponEntity> optionalCuponEntity = cuponRepository.findByIdAndStatus(id, CuponStatus.ATIVO);
            if (optionalCuponEntity.isEmpty()) {
                return Optional.empty();
            }

            CuponEntity cuponEntity = optionalCuponEntity.get();
            CuponDTO cuponDTO = modelMapper.map(cuponEntity, CuponDTO.class);

            return Optional.of(cuponDTO);
    }

    public Optional<CuponDTO> delete(String id) {
            Optional<CuponEntity> optionalCupon = cuponRepository.findByIdAndStatus(id, CuponStatus.ATIVO);
            if (optionalCupon.isEmpty()) {
                return Optional.empty();
            }

            CuponDomain cuponDomain = modelMapper.map(optionalCupon.get(), CuponDomain.class);
            cuponDomain.checkDelete();

            CuponEntity cuponEntity = modelMapper.map(cuponDomain, CuponEntity.class);
            CuponEntity save = cuponRepository.save(cuponEntity);
            CuponDTO cuponDTO = modelMapper.map(save, CuponDTO.class);

            return Optional.of(cuponDTO);
    }
}
