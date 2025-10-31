package com.unifacs.GQS_A3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoPedidoDTO {
    private Long idProduto;
    private int quantidade;
}
