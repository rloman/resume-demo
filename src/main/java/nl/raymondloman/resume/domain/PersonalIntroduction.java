package nl.raymondloman.resume.domain;

import nl.raymondloman.resume.visitor.Visitor;

public class PersonalIntroduction extends Introduction {

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}
