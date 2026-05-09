package com.outforce.desafio.domain;

import com.outforce.desafio.domain.enums.CuponStatus;
import com.outforce.desafio.exception.CuponException;
import lombok.Getter;
import lombok.Setter;
import java.text.Normalizer;
import java.time.LocalDateTime;

@Setter
@Getter
public class CuponDomain {

    private String id;
    private String code;
    private String description;
    private Double discountValue;
    private LocalDateTime expirationDate;
    private Boolean published;
    private CuponStatus status = CuponStatus.ATIVO;
    private boolean redeemed;

    public void validate() {
       this.checkCode();
       this.checkExpirationDate();
       this.checkDiscountValue();
       this.checkPublished();
    }

    private void checkCode() {
        this.code = (Normalizer
                .normalize(this.code, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9]", "") // Retiro caracteres especiais
        );

        if (this.code.length() != 6) {
            throw new CuponException("Campo code deve ter 6 caracteres, desconsiderando os especiais");
        }
    }

    private void checkExpirationDate() {
        boolean after = LocalDateTime.now().isAfter(this.expirationDate);
        if (after) {
            throw new CuponException("campo expirationDate precisa ser uma data futura");
        }
    }

    private void checkDiscountValue() {
       if (this.discountValue < 0.5) {
           throw new CuponException("campo discountValue menor que 0.5");
       }
    }

    private void checkPublished() {
        if (this.published == null) {
            this.published = false;
        }
    }

    public void checkDelete() {
        this.status = CuponStatus.INATIVO;
        this.published = false;
    }
}
