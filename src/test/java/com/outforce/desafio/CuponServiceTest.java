package com.outforce.desafio;

import com.outforce.desafio.domain.CuponDomain;
import com.outforce.desafio.domain.enums.CuponStatus;
import com.outforce.desafio.dto.CuponDTO;
import com.outforce.desafio.entity.CuponEntity;
import com.outforce.desafio.repository.CuponRepository;
import com.outforce.desafio.service.CuponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CuponServiceTest {

    @Mock
    private CuponRepository cuponRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CuponService cuponService;

    @Test
    void cuponServiceCreateTest() {

        String id = UUID.randomUUID().toString();

        CuponDTO cuponDTO = new CuponDTO();
        cuponDTO.setCode("ABC123");
        cuponDTO.setDescription("Cupom Teste");
        cuponDTO.setDiscountValue(10.0);
        cuponDTO.setExpirationDate(LocalDateTime.now().plusDays(2));

        CuponDomain domain = new CuponDomain();
        domain.setCode("ABC123");
        domain.setDescription("Cupom Teste");
        domain.setDiscountValue(10.0);
        domain.setExpirationDate(cuponDTO.getExpirationDate());

        CuponEntity entity = new CuponEntity();
        entity.setId(id);
        entity.setCode("ABC123");
        entity.setDescription("Cupom Teste");
        entity.setDiscountValue(10.0);
        entity.setExpirationDate(cuponDTO.getExpirationDate());

        Mockito.when(modelMapper.map(cuponDTO, CuponDomain.class)).thenReturn(domain);
        Mockito.when(modelMapper.map(domain, CuponEntity.class)).thenReturn(entity);
        Mockito.when(cuponRepository.save(entity)).thenReturn(entity);
        Mockito.when(modelMapper.map(entity, CuponDTO.class)).thenReturn(cuponDTO);

        CuponDTO response = cuponService.create(cuponDTO);

        assertEquals(id, response.getId());
        assertEquals(6, response.getCode().length());
        assertTrue(response.getExpirationDate().isAfter(LocalDateTime.now()));

        assertFalse(response.getDescription().isEmpty());
        assertFalse(response.getId().isEmpty());

        Mockito.verify(cuponRepository).save(entity);
        Mockito.verify(modelMapper).map(domain, CuponEntity.class);
        Mockito.verify(modelMapper).map(entity, CuponDTO.class);
    }

    @Test
    void cuponServiceGetTest() {
        String id = UUID.randomUUID().toString();

        CuponDTO cuponDTO = new CuponDTO();
        cuponDTO.setId(id);
        cuponDTO.setCode("ABC123");
        cuponDTO.setDescription("Cupom Teste");
        cuponDTO.setDiscountValue(10.0);
        cuponDTO.setExpirationDate(LocalDateTime.now().plusDays(2));

        CuponEntity entity = new CuponEntity();
        entity.setId(id);
        entity.setCode("ABC123");
        entity.setDescription("Cupom Teste");
        entity.setDiscountValue(10.0);
        entity.setExpirationDate(cuponDTO.getExpirationDate());

        Mockito.when(modelMapper.map(entity, CuponDTO.class)).thenReturn(cuponDTO);
        Mockito.when(cuponRepository.findByIdAndStatus(cuponDTO.getId(), CuponStatus.ATIVO)).thenReturn(Optional.of(entity));

        Optional<CuponDTO> optionalCuponDTO = cuponService.get(cuponDTO.getId());

        Mockito.verify(cuponRepository).findByIdAndStatus(cuponDTO.getId(), CuponStatus.ATIVO);
        Mockito.verify(modelMapper).map(entity, CuponDTO.class);

        assertTrue(optionalCuponDTO.isPresent());
        assertEquals("ABC123", optionalCuponDTO.get().getCode());
        assertEquals(10.0, optionalCuponDTO.get().getDiscountValue());
        assertEquals(cuponDTO.getId(), optionalCuponDTO.get().getId());
    }


    @Test
    void cuponServiceNotFoundTest() {
        String id = UUID.randomUUID().toString();

        Mockito.when(cuponRepository.findByIdAndStatus(id, CuponStatus.ATIVO)).thenReturn(Optional.empty());
        Optional<CuponDTO> response = cuponService.get(id);
        assertTrue(response.isEmpty());

        Mockito.verify(cuponRepository).findByIdAndStatus(id, CuponStatus.ATIVO);
    }


    @Test
    void cuponServiceDeleteTest() {

        String id = UUID.randomUUID().toString();

        CuponDTO cuponDTO = new CuponDTO();
        cuponDTO.setId(id);
        cuponDTO.setCode("ABC123");
        cuponDTO.setDescription("Cupom Teste");
        cuponDTO.setDiscountValue(10.0);
        cuponDTO.setExpirationDate(LocalDateTime.now().plusDays(2));

        CuponDomain domain = new CuponDomain();
        domain.setId(id);
        domain.setCode("ABC123");
        domain.setDescription("Cupom Teste");
        domain.setDiscountValue(10.0);
        domain.setExpirationDate(cuponDTO.getExpirationDate());

        CuponEntity entity = new CuponEntity();
        entity.setId(id);
        entity.setCode("ABC123");
        entity.setDescription("Cupom Teste");
        entity.setDiscountValue(10.0);
        entity.setExpirationDate(cuponDTO.getExpirationDate());

        Mockito.when(modelMapper.map(entity, CuponDomain.class)).thenReturn(domain);
        Mockito.when(modelMapper.map(domain, CuponEntity.class)).thenReturn(entity);
        Mockito.when(modelMapper.map(entity, CuponDTO.class)).thenReturn(cuponDTO);

        Mockito.when(cuponRepository.findByIdAndStatus(cuponDTO.getId(), CuponStatus.ATIVO)).thenReturn(Optional.of(entity));
        Mockito.when(cuponRepository.save(entity)).thenReturn(entity);

        Optional<CuponDTO> optionalCuponDTO = cuponService.delete(cuponDTO.getId());
        assertTrue(optionalCuponDTO.isPresent());
        assertEquals(cuponDTO.getId(), optionalCuponDTO.get().getId());
        assertEquals(CuponStatus.INATIVO, domain.getStatus());

        Mockito.verify(cuponRepository).findByIdAndStatus(cuponDTO.getId(), CuponStatus.ATIVO);
        Mockito.verify(cuponRepository).save(entity);

        Mockito.verify(modelMapper).map(entity, CuponDomain.class);
        Mockito.verify(modelMapper).map(domain, CuponEntity.class);
        Mockito.verify(modelMapper).map(entity, CuponDTO.class);

    }

    @Test
    void cuponServiceDeleteReturnEmptyTest() {
        String id = UUID.randomUUID().toString();

        // Cupom já está INATIVO — findByIdAndStatus(ATIVO) não o encontra
        Mockito.when(cuponRepository.findByIdAndStatus(id, CuponStatus.ATIVO))
                .thenReturn(Optional.empty());

        Optional<CuponDTO> resultado = cuponService.delete(id);

        assertTrue(resultado.isEmpty());

        Mockito.verify(cuponRepository, Mockito.never()).save(Mockito.any());
    }

}

