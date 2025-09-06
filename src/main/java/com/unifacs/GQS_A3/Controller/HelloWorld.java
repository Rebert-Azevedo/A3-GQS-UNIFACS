package com.unifacs.GQS_A3.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorld {
    @GetMapping
    public String helloWorld(){
        return  "Aplicação A3 Gestão e Qualidade de Software";
    }
}
