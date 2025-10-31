package com.unifacs.GQS_A3.Controller;

import com.unifacs.GQS_A3.Service.ProdutoService;
import com.unifacs.GQS_A3.model.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public Produto adicionarProduto(@RequestBody Produto produto){
        return produtoService.adicionarProduto(produto);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Produto> editarProduto(@PathVariable Long id, @RequestBody Produto produto){
        Produto updatedProduto = produtoService.editarProduto(id, produto);
        return ResponseEntity.ok(updatedProduto);

    }
}
