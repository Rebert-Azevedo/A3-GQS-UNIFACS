package com.unifacs.GQS_A3.service;

import com.unifacs.GQS_A3.dto.PedidoResponseDTO;
import com.unifacs.GQS_A3.exceptions.EstoqueInsuficienteException;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.PedidoProduto;
import com.unifacs.GQS_A3.model.Produto;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
import com.unifacs.GQS_A3.repository.PedidoRepository;
import com.unifacs.GQS_A3.repository.ProdutoRepository;
import com.unifacs.GQS_A3.dto.PedidoRequestDTO;
import com.unifacs.GQS_A3.dto.ProdutoPedidoDTO;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;


    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public PedidoResponseDTO criarPedido(PedidoRequestDTO pedidoDTO) {
        Usuario usuario = usuarioRepository.findById(pedidoDTO.getIdUsuario()).orElseThrow(()
                -> new RecursoNaoEncontradoException("Usuario não encontrado"));

        Pedido novoPedido = new Pedido();
        novoPedido.setUsuario(usuario);
        novoPedido.setValorTotal(calcTotalPedido(pedidoDTO, novoPedido));

        pedidoRepository.save(novoPedido);

        PedidoResponseDTO pedidoResponse = new PedidoResponseDTO();
        pedidoResponse.setId(novoPedido.getId());
        pedidoResponse.setNomeUsuario(novoPedido.getUsuario().getNome());
        pedidoResponse.setVlrTotal(novoPedido.getValorTotal());
        pedidoResponse.setProdutos(pedidoDTO.getProdutos());

        return pedidoResponse;
    }

    public double calcTotalPedido(PedidoRequestDTO itensPedido, Pedido pedido) {
        List<PedidoProduto> produtos = new ArrayList<>();
        double valorTotal = 0;

        for (ProdutoPedidoDTO produtoDTO : itensPedido.getProdutos()) {
            Produto produto = produtoRepository
                    .findById(produtoDTO.getIdProduto())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto " + produtoDTO.getIdProduto() + " não encontrado"));

            if (produto.getEstoque() < produtoDTO.getQuantidade()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + produto.getNome());
            }
            produto.setEstoque(produto.getEstoque() - produtoDTO.getQuantidade());
            produtoRepository.save(produto);

            PedidoProduto pedidoProduto = new PedidoProduto();
            pedidoProduto.setPedido(pedido);
            pedidoProduto.setProduto(produto);
            pedidoProduto.setQtdeProduto(produtoDTO.getQuantidade());
            produtos.add(pedidoProduto);

            double subtotal = produto.getValor() * produtoDTO.getQuantidade();
            valorTotal += subtotal;
        }

        pedido.setPedidoProduto(produtos);
        return valorTotal;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public void deletarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    public Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id).
                orElseThrow(() -> new RecursoNaoEncontradoException("Pedido: " + id + " não encontrado"));
    }
}
