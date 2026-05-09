package com.outforce.desafio;

import com.outforce.desafio.controller.CuponController;
import com.outforce.desafio.dto.CuponDTO;
import com.outforce.desafio.service.CuponService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CuponController.class)
class CuponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CuponService cuponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cuponGetOkTest() throws Exception {

        CuponDTO cuponDTO = new CuponDTO();
        String id = UUID.randomUUID().toString();
        cuponDTO.setId(id);
        cuponDTO.setCode("Teste01");

        Mockito.when(cuponService.get(id)).thenReturn(Optional.of(cuponDTO));

        mockMvc.perform(get("/cupon/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("Teste01"));
    }

    @Test
    void cuponGetIsNotFountTest() throws Exception {

        CuponDTO cuponDTO = new CuponDTO();
        String id = UUID.randomUUID().toString();
        cuponDTO.setId(id);
        cuponDTO.setCode("Teste01");

        Mockito.when(cuponService.get(id)).thenReturn(Optional.of(cuponDTO));

        mockMvc.perform(get("/cupon/019e093b-b39c-7680-9c12-96ed261b8973" )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuponPostCreatedTest() throws Exception {

        CuponDTO request = new CuponDTO();
        String id = UUID.randomUUID().toString();
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2).withNano(0);

        request.setId(id);
        request.setCode("Test01");
        request.setDiscountValue(10.0);
        request.setDescription("Descrição teste Cupon");
        request.setExpirationDate(localDateTime);

        CuponDTO response = new CuponDTO();

        response.setId(id);
        response.setCode("Test01");
        response.setDiscountValue(10.0);
        response.setDescription("Descrição teste Cupon");
        response.setExpirationDate(localDateTime);

        Mockito.when(cuponService.create(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/cupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("Test01"))
                .andExpect(jsonPath("$.description").value("Descrição teste Cupon"))
                .andExpect(jsonPath("$.expirationDate").value(localDateTime.toString())
                );
    }

    @Test
    void cuponPostBadRequestTest() throws Exception {

        CuponDTO request = new CuponDTO();

        mockMvc.perform(post("/cupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("Campo obrigatório"))
                .andExpect(jsonPath("$.description").value("Campo obrigatório"))
                .andExpect(jsonPath("$.discountValue").value("Campo obrigatório"))
                .andExpect(jsonPath("$.expirationDate").value("Campo obrigatório"));
    }

    @Test
    void cuponPostBadRequestWithFieldsTest() throws Exception {

        CuponDTO request = new CuponDTO();
        request.setCode("Xsta23");
        request.setDiscountValue(0.1);
        request.setExpirationDate(LocalDateTime.now().minusDays(3));

        mockMvc.perform(post("/cupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.discountValue").value("Desconto mínimo é de 0.5"))
                .andExpect(jsonPath("$.expirationDate").value("Deve ser uma data futura"));
    }

    @Test
    void cuponDeleteNoContentTest() throws Exception {
        CuponDTO dto = new CuponDTO();
        String id = UUID.randomUUID().toString();
        dto.setId(id);
        dto.setCode("Teste01");

        Mockito.when(cuponService.delete(id)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete("/cupon/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuponDeleteNotFoundTest() throws Exception {
        String id = UUID.randomUUID().toString();

        Mockito.when(cuponService.delete(id))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/cupon/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}