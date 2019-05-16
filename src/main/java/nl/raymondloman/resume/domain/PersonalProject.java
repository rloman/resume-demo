package nl.raymondloman.resume.domain;

import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.visitor.Visitor;

public class PersonalProject extends Assignment {

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}