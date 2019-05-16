package nl.raymondloman.resume.domain.consultancy;

import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.visitor.Visitor;

public class Consultancy extends Assignment {

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}