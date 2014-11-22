package com.ibm.sendgrid;

public class SendGridException extends Exception {
  
	private static final long serialVersionUID = 1L;

public SendGridException(Exception e) {
    super(e);
  }
}