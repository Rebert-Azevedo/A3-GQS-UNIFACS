package com.unifacs.GQS_A3.service;

import com.unifacs.GQS_A3.repository.PedidoProdutoRepository;
import com.unifacs.GQS_A3.model.PedidoProduto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoProdutoService {
    private final PedidoProdutoRepository pedidoProdutoRepository;

    public PedidoProdutoService(PedidoProdutoRepository pedidoProdutoRepository){
        this.pedidoProdutoRepository = pedidoProdutoRepository;
    }

    public PedidoProduto criarPedidoProduto(PedidoProduto pedidoProduto){
        return pedidoProdutoRepository.save(pedidoProduto);
    }
    public List<PedidoProduto> listarPedidoProduto(){
        return pedidoProdutoRepository.findAll();
    }
    public Optional<PedidoProduto> buscarPedidoProduto(Long id){
        return pedidoProdutoRepository.findById(id);
    }
    public void deletarPedidoProduto(Long id){
        pedidoProdutoRepository.deleteById(id);
    }
}