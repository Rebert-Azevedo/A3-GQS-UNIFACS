package com.unifacs.GQS_A3.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unifacs.GQS_A3.Service.ClienteService;
import com.unifacs.GQS_A3.Service.PedidoService;
import com.unifacs.GQS_A3.Service.ProdutoService;
import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.PedidoProduto;
import com.unifacs.GQS_A3.dto.PedidoRequestDTO;
import com.unifacs.GQS_A3.dto.PedidoResponseDTO;
import com.unifacs.GQS_A3.dto.ProdutoPedidoDTO;
import com.unifacs.GQS_A3.Service.PedidoProdutoService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    public final PedidoService pedidoService;
    public final ClienteService clienteService;
    public final ProdutoService produtoService;
    public final PedidoProdutoService pedidoProdutoService;

    public PedidoController(PedidoService pedidoService,
                            ClienteService clienteService,
                            ProdutoService produtoService,
                            PedidoProdutoService pedidoProdutoService) {

        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
        this.pedidoProdutoService = pedidoProdutoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos(){
        return new ResponseEntity<>(pedidoService.listarPedidos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(@PathVariable Long id){

        Function<List<PedidoProduto>, List<ProdutoPedidoDTO>> listaProdutos = pedidoProdutos -> {
            List<ProdutoPedidoDTO> lista = new ArrayList<>();
            for(PedidoProduto pd: pedidoProdutos){
                ProdutoPedidoDTO produtos = new ProdutoPedidoDTO();
                produtos.setIdProduto(pd.getProduto().getId());
                produtos.setQuantidade(pd.getQtdeProduto());
                lista.add(produtos);
            }

            return lista;
        };
        Pedido pedido = pedidoService.buscarPedido(id);
        PedidoResponseDTO pedidoResponse = new PedidoResponseDTO();
        pedidoResponse.setId(pedido.getId());
        pedidoResponse.setNomeCliente(pedido.getCliente().getNome());
        pedidoResponse.setVlrTotal(pedido.getValorTotal());
        pedidoResponse.setProdutos(listaProdutos.apply(pedido.getPedidoProduto()));

        return new ResponseEntity<>(pedidoResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping()
    public ResponseEntity<Object> criarPedido(@RequestBody PedidoRequestDTO pedido){

        PedidoResponseDTO novoPedido = pedidoService.criarPedido(pedido);
        return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
    }
}