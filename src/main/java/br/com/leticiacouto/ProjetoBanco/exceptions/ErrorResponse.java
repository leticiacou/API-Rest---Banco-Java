package br.com.leticiacouto.ProjetoBanco.exceptions;

import java.time.LocalDateTime;

//classe que vai tratar nossos erros
public class ErrorResponse {
//    status do erro
    private int status;
//    mensagem do erro
    private String message;
//    quando aconteceu
    private LocalDateTime timestamp;

//  construtor
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

//  getters
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
