package nl.raymondloman.resume.domain.training;

import java.util.List;

import nl.raymondloman.resume.domain.AbstractWorkItem;
import nl.raymondloman.resume.domain.enums.TrainingType;
import nl.raymondloman.resume.visitor.Visitor;

public class Training extends AbstractWorkItem {

   private TrainingType type;
   private List<String> customers;
   private int trainees;
   
   private boolean eveningTraining;

   public List<String> getCustomers() {
      return this.customers;
   }

   public void setCustomers(List<String> customers) {
      this.customers = customers;
   }

   public int getTrainees() {
      return this.trainees;
   }

   public void setTrainees(int trainees) {
      this.trainees = trainees;
   }

   public TrainingType getType() {
      return this.type;
   }

   public void setType(TrainingType type) {
      this.type = type;
   }

   @Override
   public String getName() {
      return this.getType().toString();
   }
   
   public boolean isEveningTraining() {
      return eveningTraining;
   }

   public void setEveningTraining(boolean eveningTraining) {
      this.eveningTraining = eveningTraining;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((this.customers == null) ? 0 : this.customers.hashCode());
      result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!super.equals(obj)) {
         return false;
      }
      if (!(obj instanceof Training)) {
         return false;
      }
      Training other = (Training) obj;
      if (this.customers == null) {
         if (other.customers != null) {
            return false;
         }
      }
      else if (!this.customers.equals(other.customers)) {
         return false;
      }
      if (this.type != other.type) {
         return false;
      }
      return true;
   }

   @Override
   public int compareTo(AbstractWorkItem o) {
      Training other = (Training) o;
      int result = this.getType().toString().compareTo(other.getType().toString());

      if (result != 0) {
         return result;
      }
      else {
         return super.compareTo(other);
      }
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}