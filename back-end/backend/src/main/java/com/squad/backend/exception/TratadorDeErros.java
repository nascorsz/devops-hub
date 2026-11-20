package com.squad.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// Classe que "captura" erros de todos os controllers e transforma em respostas amigáveis
@RestControllerAdvice
public class TratadorDeErros {

    // Executado quando o @Valid encontra dados inválidos.
    // Junta as mensagens definidas nas entidades (ex.: "A área é obrigatória.")
    // e devolve como texto simples com status 400 (Bad Request).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> tratarValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.joining(" "));
        return ResponseEntity.badRequest().body(mensagem);
    }
}