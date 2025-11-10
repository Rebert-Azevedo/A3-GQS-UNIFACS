package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import com.unifacs.GQS_A3.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    public void testarCriacaoProduto(){
        Produto produto = new Produto();
        produto.setNome("produto01");
        produto.setDescricao("Teste do primeiro produto");
        produto.setValor(22.99);
        produto.setEstoque(25);

        Mockito.when(produtoRepository.save(produto)).thenReturn(produto);

        Produto novoProduto = produtoService.adicionarProduto(produto);

        Assertions.assertEquals(produto, novoProduto);
    }

    @Test
    public void testarListagemDeProdutos(){
        Produto produto1 = new Produto();
        produto1.setNome("produto01");
        produto1.setDescricao("Teste do primeiro produto");
        produto1.setValor(22.99);
        produto1.setEstoque(25);

        Produto produto2 = new Produto();
        produto2.setNome("produto02");
        produto2.setDescricao("Teste do segundo produto");
        produto2.setValor(14.99);
        produto2.setEstoque(30);

        List<Produto> minhaListaDeProdutos = Arrays.asList(produto1, produto2);

        Mockito.when(produtoRepository.findAll()).thenReturn(minhaListaDeProdutos);

        List<Produto> listaProdutos = produtoService.listarProdutos();

        Assertions.assertEquals(2, listaProdutos.size());
    }

    @Test
    public void testaBuscaDeProdutoPorId(){
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("produto");
        produto.setDescricao("Produto para testar a busca por Id");
        produto.setValor(29.99);
        produto.setEstoque(2);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> produtoEncontrado = produtoService.buscarProdutoPorId(1L);

        produtoEncontrado.ifPresent(meuProduto -> Assertions.assertEquals(produto, meuProduto));
    }

    @Test
    public void testarEdicaoDeProduto(){
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("produto");
        produto.setDescricao("Produto para testar a edição de um produto");
        produto.setValor(30.99);
        produto.setEstoque(1);

        Mockito.when(produtoRepository.save(produto)).thenReturn(produto);
        Mockito.when(produtoRepository.existsById(1L)).thenReturn(true);
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto produtoModificado = new Produto();
        produtoModificado.setEstoque(5);

        Produto produtoEditado = produtoService.editarProduto(1L, produtoModificado);

        Assertions.assertEquals(produtoModificado.getEstoque(), produtoEditado.getEstoque());
    }
}
