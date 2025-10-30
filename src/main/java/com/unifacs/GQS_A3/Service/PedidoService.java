package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.model.Cliente;
import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.PedidoProduto;
import com.unifacs.GQS_A3.model.Produto;
import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.Repository.PedidoRepository;
import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import com.unifacs.GQS_A3.dto.PedidoRequestDTO;
import com.unifacs.GQS_A3.dto.ProdutoPedidoDTO;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;


    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    public Pedido criarPedido(PedidoRequestDTO pedidoDTO){
        Cliente cliente = clienteRepository.findById(pedidoDTO.getIdCliente()).orElseThrow(()
                -> new RuntimeException("Cliente não encontrado"));

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente);

        double valorTotal = 0;
        List<PedidoProduto> produtos = new ArrayList<>();

        for(ProdutoPedidoDTO produtoDTO: pedidoDTO.getProdutos()){
            Produto produto = produtoRepository.findById(produtoDTO.getIdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto " + produtoDTO.getIdProduto() + " não encontrado"));

            if(produto.getEstoque() < produtoDTO.getQuantidade()){
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }
            produto.setEstoque(produto.getEstoque() - produtoDTO.getQuantidade());
            produtoRepository.save(produto);

            PedidoProduto pedidoProduto = new PedidoProduto();
            pedidoProduto.setPedido(novoPedido);
            pedidoProduto.setProduto(produto);
            pedidoProduto.setQtdeProduto(produtoDTO.getQuantidade());
            produtos.add(pedidoProduto);

            double subtotal = produto.getValor() * produtoDTO.getQuantidade();
            valorTotal += subtotal;
        }
        novoPedido.setValorTotal(valorTotal);
        novoPedido.setPedidoProduto(produtos);

        return pedidoRepository.save(novoPedido);
    }

    public List<Pedido> listarPedidos(){
        return pedidoRepository.findAll();
    }

    public void deletarPedido(Long id){
        pedidoRepository.deleteById(id);
    }

    public Optional<Pedido> buscarPedido(Long id) {
        return pedidoRepository.findById(id);
    }
}
