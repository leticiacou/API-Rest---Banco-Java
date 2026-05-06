package br.com.leticiacouto.ProjetoBanco.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.leticiacouto.ProjetoBanco.exceptions.ResourceNotFoundException;
import br.com.leticiacouto.ProjetoBanco.exceptions.BusinessException;
import br.com.leticiacouto.ProjetoBanco.exceptions.ErrorResponse;

//classe que vai pegar todos os erros pra tratar
@RestControllerAdvice
public class GlobalExceptionHandler {

//  tratamento do erro notFound da nossa classe ResourceNotFoundException
//    O @ExceptionHandler serve pra vincular um método a um tipo de exceção,
//    quando aquela exceção for lançada, o Spring automaticamente chama aquele método.
    @ExceptionHandler(ResourceNotFoundException.class)
//  responseEntity do tipo error, que a gente criou
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        ErrorResponse error = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Captura qualquer outro erro não tratado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}