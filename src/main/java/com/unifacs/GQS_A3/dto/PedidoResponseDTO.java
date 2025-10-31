package com.unifacs.GQS_A3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private String nomeCliente;
    private double vlrTotal;
    private List<ProdutoPedidoDTO> produtos = new ArrayList<>();
}
