package br.com.leticiacouto.ProjetoBanco.exceptions;

//erro de não encontrado
public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}