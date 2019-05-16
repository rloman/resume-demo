package nl.raymondloman.resume.domain.education;

import nl.raymondloman.resume.visitor.Visitor;

public class Course extends Education {

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}