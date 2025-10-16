package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.PedidoRepository;
import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import com.unifacs.GQS_A3.model.Pedido;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class PedidoServiceTest {

    @Test
    void criarPedido_deveRetornarPedidoSalvo() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);

        Pedido pedidoSalvo = new Pedido();
        pedidoSalvo.setCliente("João");

        Mockito.when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        PedidoService pedidoService = new PedidoService(pedidoRepository, produtoRepository);

        Pedido resultado = pedidoService.criarPedido("João");

        assertNotNull(resultado);
        assertEquals("João", resultado.getCliente());
    }
}
