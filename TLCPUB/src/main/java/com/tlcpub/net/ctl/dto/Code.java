package com.tlcpub.net.ctl.dto;


import com.sun.istack.internal.NotNull;

import com.tlcpub.net.core.dto.AbstractModel;
import org.springframework.beans.annotation.AnnotationBeanUtils;
import com.tlcpub.net.core.type.YesNo;


public class Code extends AbstractModel{



   @NotNull
//   @Length(min=3, max=3)
  // @Expression(value = "groupId IS UPPERCASE")
   private String groupId; 
   
   @NotNull
   //@Length(min=3, max=3)
   //@Expression(value = "codeId IS UPPERCASE")
   private String codeId;
   
   @NotNull
   //@Length(min=2, max=30)
   private String codeName;

   private YesNo isUse;
   
   //@Expression(value = "seqNum IS GREATER THAN 0")
   private int seqNum = 1;
   
   private String descr;


   public String getGroupId() {
      return groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getCodeId() {
      return codeId;
   }

   public void setCodeId(String codeId) {
      this.codeId = codeId;
   }

   public String getCodeName() {
      return codeName;
   }

   public void setCodeName(String codeName) {
      this.codeName = codeName;
   }
   
   public YesNo getIsUse() {
      return isUse;
   }

   public void setIsUse(YesNo isUse) {
      this.isUse = isUse;
   }

   public int getSeqNum() {
      return seqNum;
   }

   public void setSeqNum(int seqNum) {
      this.seqNum = seqNum;
   }

   public String getDescr() {
      return descr;
   }

   public void setDescr(String descr) {
      this.descr = descr;
   }

   @Override
   public String toString() {
      return "Code [groupId=" + groupId + ", codeId=" + codeId
            + ", codeName=" + codeName + ", isUse=" + isUse + ", seqNum="
            + seqNum + ", descr=" + descr + "]";
   }
}