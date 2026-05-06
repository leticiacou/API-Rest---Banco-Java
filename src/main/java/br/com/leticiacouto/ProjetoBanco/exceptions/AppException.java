package br.com.leticiacouto.ProjetoBanco.exceptions;

//criamos uma classe que herda o runTime pra outras não precisarem
public class AppException  extends RuntimeException{
    public AppException(String message) {
//      todas as outras vão ter só a mensagem ou algo mais específico junto com a mensagem
        super(message);
    }
}
