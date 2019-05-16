package nl.raymondloman.resume.domain;

import nl.raymondloman.resume.domain.enums.Topic;
import nl.raymondloman.resume.domain.enums.Used;

public class Experience {
   
   // mainly used for testing purposes
   public static Experience create(Topic topic) {
      Experience result = new Experience();
      result.setTopic(topic);
      
      return result;
   }

   private Topic topic;
   private Used used;

   public Topic getTopic() {
      return topic;
   }

   public void setTopic(Topic topic) {
      this.topic = topic;
   }

   public Used getUsed() {
      return used;
   }

   public void setUsed(Used used) {
      this.used = used;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((topic == null) ? 0 : topic.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof Experience)) {
         return false;
      }
      Experience other = (Experience) obj;
      if (topic != other.topic) {
         return false;
      }
      return true;
   }

}
