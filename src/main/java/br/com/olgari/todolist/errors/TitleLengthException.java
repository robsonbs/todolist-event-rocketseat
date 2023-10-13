package br.com.olgari.todolist.errors;

public class TitleLengthException extends Exception {
  public TitleLengthException() {
    super("O titulo não pode conter mais que 50 caracteres");
  }

}
