package nl.raymondloman.resume.domain;

import java.io.Serializable;
import java.time.LocalDate;

import nl.raymondloman.resume.domain.enums.Topic;

public class ManuallyOverriddenKnowledgeBean implements Serializable {

   private static final long serialVersionUID = 7459679961151215027L;

   private Topic topic;
   private Integer stars;
   private LocalDate lastUsed = LocalDate.now();

   public Topic getTopic() {
      return this.topic;
   }

   public void setTopic(Topic topic) {
      this.topic = topic;
   }

   public Integer getStars() {
      return this.stars;
   }

   public void setStars(Integer stars) {
      this.stars = stars;
   }

   public LocalDate getLastUsed() {
      return this.lastUsed;
   }

   public void setLastUsed(LocalDate lastUsed) {
      this.lastUsed = lastUsed;
   }
}