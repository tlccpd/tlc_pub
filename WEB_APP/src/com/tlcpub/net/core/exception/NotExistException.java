package com.tlcpub.net.core.exception;


public class NotExistException extends ApplicationException {


   static final long serialVersionUID = 1;
   
   
   public NotExistException(Object source){
      super(source);
   }
}
