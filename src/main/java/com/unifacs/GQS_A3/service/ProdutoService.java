package com.unifacs.GQS_A3.service;

import com.unifacs.GQS_A3.repository.ProdutoRepository;
import com.unifacs.GQS_A3.exceptions.CampoNaoPreenchidoException;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
import com.unifacs.GQS_A3.model.Produto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public Produto adicionarProduto(Produto produto){
        if(produto.getNome() == null || produto.getValor() == 0 || produto.getEstoque() == 0){
            throw new CampoNaoPreenchidoException("nome, valor e estoque precisam ser preenchidos");
        }
        return produtoRepository.save(produto);
    }
    public Produto editarProduto(Long id, Produto alterProduto){
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com Id "+ id +" não encontrado"));
        produto.setNome(alterProduto.getNome() != null ? alterProduto.getNome() : produto.getNome());
        produto.setDescricao(alterProduto.getDescricao() != null ? alterProduto.getDescricao() : produto.getDescricao());
        produto.setValor(alterProduto.getValor() != 0 ? alterProduto.getValor() : produto.getValor());
        produto.setEstoque(alterProduto.getEstoque() != 0 ? alterProduto.getEstoque() : produto.getEstoque());
        return produtoRepository.save(produto);
    }
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }
    public Produto buscarProdutoPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Produto com Id " + id + " não encontrado"));
    }
}
