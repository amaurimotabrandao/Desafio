package com.outforce.desafio.dto;

import com.outforce.desafio.domain.enums.CuponStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CuponDTO {

    private String id;

    @NotBlank(message = "Campo obrigatório")
    private String code;

    @NotBlank(message = "Campo obrigatório")
    private String description;

    @DecimalMin(value = "0.5", message = "Desconto mínimo é de 0.5")
    @NotNull(message = "Campo obrigatório")
    private Double discountValue;

    @Future(message = "Deve ser uma data futura")
    @NotNull(message = "Campo obrigatório")
    private LocalDateTime expirationDate;

    private Boolean published;

    private CuponStatus status = CuponStatus.ATIVO;
    private boolean redeemed;
}
