package nl.raymondloman.resume.domain.other;

import nl.raymondloman.resume.visitor.Visitor;

public class Header extends Node {

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}