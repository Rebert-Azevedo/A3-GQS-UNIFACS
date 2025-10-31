package com.unifacs.GQS_A3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO {
    private Long idCliente;
    private List<ProdutoPedidoDTO> produtos;

    public static double calcularValorTotal(){
        return 0;
    }
}