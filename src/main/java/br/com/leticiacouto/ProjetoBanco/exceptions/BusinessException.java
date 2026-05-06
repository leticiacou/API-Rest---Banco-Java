package br.com.leticiacouto.ProjetoBanco.exceptions;

//a classe BusinessException serve pra mostra que é um erro causado por regra de negócio
//e não pelo sistema
public class BusinessException extends AppException {
    public BusinessException(String message) {
        super(message);
    }
}