package com.outforce.desafio;

import com.outforce.desafio.domain.CuponDomain;
import com.outforce.desafio.domain.enums.CuponStatus;
import com.outforce.desafio.exception.CuponException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CuponDomainTest {

    @Test
    void cuponCreateOKTest() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setCode("âbcdef");
        cuponDomain.setDescription("Teste");
        cuponDomain.setDiscountValue(1.2);

        LocalDateTime localDateTime = LocalDateTime.now().plusHours(2);
        cuponDomain.setExpirationDate(LocalDateTime.now().plusHours(2));
        cuponDomain.setPublished(true);

        cuponDomain.validate();

        assertEquals("abcdef", cuponDomain.getCode());
        assertEquals("Teste", cuponDomain.getDescription());
        assertEquals(1.2, cuponDomain.getDiscountValue());
        assertEquals(localDateTime, cuponDomain.getExpirationDate());
        assertEquals(true, cuponDomain.getPublished());
    }

    @Test
    void cuponCreateWithCodeFailTest() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setCode("ab-âõ/");
        cuponDomain.setDiscountValue(0.5);
        cuponDomain.setExpirationDate(LocalDateTime.now().plusDays(1));
        cuponDomain.setDescription("Testando Aplicação");

        LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
        cuponDomain.setExpirationDate(localDateTime);
        cuponDomain.setPublished(null);

        CuponException exception = assertThrows(CuponException.class, cuponDomain::validate);
        assertEquals(
                "Campo code deve ter 6 caracteres, desconsiderando os especiais",
                exception.getMessage()
        );
    }

    @Test
    void cuponCreateWithDiscontValueFail() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setDiscountValue(0.2);
        cuponDomain.setCode("TESTE1");
        cuponDomain.setExpirationDate(LocalDateTime.now().plusDays(1));
        cuponDomain.setDescription("Testando Aplicação");

        CuponException cuponException = assertThrows(CuponException.class, cuponDomain::validate);
        assertEquals("campo discountValue menor que 0.5", cuponException.getMessage());
    }

    @Test
    void cuponCreateWithExpirationDateFail() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setExpirationDate(LocalDateTime.now().minusDays(1));
        cuponDomain.setCode("TESTE1");
        cuponDomain.setDiscountValue(0.2);
        cuponDomain.setDescription("Testando Aplicação");
        CuponException cuponException = assertThrows(CuponException.class, cuponDomain::validate);
        assertEquals("campo expirationDate precisa ser uma data futura", cuponException.getMessage());
    }

    @Test
    void cuponCreateWithPublishFail() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setCode("TESTE1");
        cuponDomain.setDiscountValue(0.5);
        cuponDomain.setExpirationDate(LocalDateTime.now().plusDays(1));
        cuponDomain.setDescription("Testando Aplicação");
        cuponDomain.setPublished(null);

        cuponDomain.validate();
        assertEquals(false, cuponDomain.getPublished());
    }

    @Test
    void cuponDeleteOkTest() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setCode("TESTE1");
        cuponDomain.setDiscountValue(0.5);
        cuponDomain.setExpirationDate(LocalDateTime.now().plusDays(1));
        cuponDomain.setDescription("Testando Aplicação");

        cuponDomain.checkDelete();
        assertEquals(CuponStatus.INATIVO, cuponDomain.getStatus());
        assertEquals(false, cuponDomain.getPublished());

    }

    @Test
    void cuponDeleteTwoObject() {
        String id = UUID.randomUUID().toString();

        CuponDomain cuponDomain = new CuponDomain();
        cuponDomain.setId(id);
        cuponDomain.setCode("TESTE1");
        cuponDomain.setDiscountValue(0.5);
        cuponDomain.setExpirationDate(LocalDateTime.now().plusDays(1));
        cuponDomain.setDescription("Testando Aplicação");

        // Primeiro delete — legítimo
        cuponDomain.checkDelete();
        assertEquals(CuponStatus.INATIVO, cuponDomain.getStatus());
        assertEquals(false, cuponDomain.getPublished());

        // Simula uma segunda tentativa de deletar o mesmo objeto de domínio:
        // o status permanece INATIVO e published continua false — não há
        // reversão de estado nem exceção no domínio pois a proteção está
        // na Service (findByIdAndStatus filtra apenas ATIVOs).
        cuponDomain.checkDelete();
        assertEquals(CuponStatus.INATIVO, cuponDomain.getStatus());
        assertEquals(false, cuponDomain.getPublished());
    }

}
