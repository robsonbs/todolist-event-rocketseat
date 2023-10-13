package br.com.olgari.todolist.errors;

public class RequestErrorException extends Exception {
  public RequestErrorException(String message) {
    super(message);
  }
}
