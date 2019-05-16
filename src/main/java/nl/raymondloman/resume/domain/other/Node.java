package nl.raymondloman.resume.domain.other;

import nl.raymondloman.resume.visitor.Visitor;

public abstract class Node {

   public abstract void accept(Visitor visitor);
}
