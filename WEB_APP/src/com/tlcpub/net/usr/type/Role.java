package com.tlcpub.net.usr.type;


import com.tlcpub.net.core.type.Symbolic;


public enum Role implements Symbolic {

   
   MEMBER("MEM"),     // 일반 회원 
   OPERATOR("OPR"),   // 서비스 운영자
   ADMIN("ADM"),      // 어드민
   SUPER("SUP");      // 시스템 운영자
   
   
   String symbol;
   
   
   Role(String symbol){
      this.symbol = symbol;
   }
   
   public String getSymbol(){
      return symbol;
   }
}