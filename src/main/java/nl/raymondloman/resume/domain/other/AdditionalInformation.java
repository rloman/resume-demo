package nl.raymondloman.resume.domain.other;

import java.util.List;

import nl.raymondloman.resume.domain.i18n.ListI18N;
import nl.raymondloman.resume.visitor.Visitor;

public class AdditionalInformation extends Node {

   private ListI18N pointsOfAttentionI18N;
   private List<String> pointsOfAttention;

   private ListI18N remainderI18N;
   private List<String> remainder;

   private ListI18N hobbiesI18N;
   private List<String> hobbies;

   public List<String> getPointsOfAttention() {
      return this.pointsOfAttention;
   }

   public void setPointsOfAttention(List<String> pointsOfAttention) {
      this.pointsOfAttention = pointsOfAttention;
   }

   public List<String> getRemainder() {
      return this.remainder;
   }

   public void setRemainder(List<String> remainder) {
      this.remainder = remainder;
   }

   public List<String> getHobbies() {
      return this.hobbies;
   }

   public void setHobbies(List<String> hobbies) {
      this.hobbies = hobbies;
   }

   public ListI18N getPointsOfAttentionI18N() {
      return this.pointsOfAttentionI18N;
   }

   public void setPointsOfAttentionI18N(ListI18N pointsOfAttentionI18N) {
      this.pointsOfAttentionI18N = pointsOfAttentionI18N;
   }

   public ListI18N getRemainderI18N() {
      return this.remainderI18N;
   }

   public void setRemainderI18N(ListI18N remainderI18N) {
      this.remainderI18N = remainderI18N;
   }

   public ListI18N getHobbiesI18N() {
      return this.hobbiesI18N;
   }

   public void setHobbiesI18N(ListI18N hobbiesI18N) {
      this.hobbiesI18N = hobbiesI18N;
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}