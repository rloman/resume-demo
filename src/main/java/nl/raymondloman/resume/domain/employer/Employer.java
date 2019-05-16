package nl.raymondloman.resume.domain.employer;

import nl.raymondloman.resume.domain.AbstractWorkItem;
import nl.raymondloman.resume.visitor.Visitor;

public class Employer extends AbstractWorkItem {

   private String name;

   @Override
   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}