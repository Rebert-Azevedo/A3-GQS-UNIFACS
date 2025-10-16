package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.Produto;
import com.unifacs.GQS_A3.Repository.PedidoRepository;
import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }
}
