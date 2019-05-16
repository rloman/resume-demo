package nl.raymondloman.resume.domain.other;

import java.time.LocalDate;

public class KnowledgeBean {

   private int days;
   private int hits;
   private LocalDate lastUsed;

   public KnowledgeBean(int days, int hits, LocalDate lastUsed) {
      this.days = days;
      this.hits = hits;
      this.lastUsed = lastUsed;
   }

   public void hit() {
      this.hits++;
   }

   public void addDays(int days) {
      this.days += days;
   }

   public int getDays() {
      return this.days;
   }

   public void setDays(int days) {
      this.days = days;
   }

   public LocalDate getLastUsed() {
      return this.lastUsed;
   }

   public void setLastUsed(LocalDate lastUsed) {
      this.lastUsed = lastUsed;
   }

   public int getHits() {
      return this.hits;
   }

   public void setHits(int hits) {
      this.hits = hits;
   }

   @Override
   public String toString() {
      return "KnowledgeBean [days=" + days + ", hits=" + hits + ", lastUsed=" + lastUsed + "]";
   }
}