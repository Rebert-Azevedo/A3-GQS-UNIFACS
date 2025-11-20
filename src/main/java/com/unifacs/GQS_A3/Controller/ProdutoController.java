package com.unifacs.GQS_A3.Controller;

import com.unifacs.GQS_A3.Service.ProdutoService;
import com.unifacs.GQS_A3.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Produto> adicionarProduto(@RequestBody Produto produto){
        Produto novoProduto = produtoService.adicionarProduto(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Produto> editarProduto(@PathVariable Long id, @RequestBody Produto produto){
        Produto updatedProduto = produtoService.editarProduto(id, produto);
        return new ResponseEntity<>(updatedProduto, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos(){
        return new ResponseEntity<>(produtoService.listarProdutos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable Long id){
        Produto produto = produtoService.buscarProdutoPorId(id);
        return new ResponseEntity<>(produto, HttpStatus.OK);
    }
}
