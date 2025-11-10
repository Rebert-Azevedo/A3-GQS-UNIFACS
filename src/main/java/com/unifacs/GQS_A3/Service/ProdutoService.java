package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import com.unifacs.GQS_A3.model.Produto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public Produto adicionarProduto(Produto produto){
        return produtoRepository.save(produto);
    }
    public Produto editarProduto(Long id, Produto alterProduto){
        if(produtoRepository.existsById(id)){
            Produto produto = produtoRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            produto.setNome(alterProduto.getNome() != null ? alterProduto.getNome() : produto.getNome());
            produto.setDescricao(alterProduto.getDescricao() != null ? alterProduto.getDescricao() : produto.getDescricao());
            produto.setValor(alterProduto.getValor() != 0 ? alterProduto.getValor() : produto.getValor());
            produto.setEstoque(alterProduto.getEstoque() != 0 ? alterProduto.getEstoque() : produto.getEstoque());
            return produtoRepository.save(produto);
        }
        return null;
    }
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }
    public Optional<Produto> buscarProdutoPorId(Long id){
        return produtoRepository.findById(id);
    }
}
