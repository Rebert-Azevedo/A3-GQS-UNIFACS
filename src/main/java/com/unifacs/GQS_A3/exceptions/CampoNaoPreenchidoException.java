package com.unifacs.GQS_A3.exceptions;

public class CampoNaoPreenchidoException extends RuntimeException{

    public CampoNaoPreenchidoException(String mensagem){
        super(mensagem);
    }
}
