package com.boiz.streaming.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final String message;
  private final HttpStatus status;

  public static CustomException of(final String message, final HttpStatus status) {
      return new CustomException(message, status);
  }

  private CustomException(final String message, final HttpStatus status) {
      this.message = message;
      this.status = status;
  }

  @Override
  public String getMessage() {
      return message;
  }

}
