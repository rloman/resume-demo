package nl.raymondloman.resume.domain.consultancy;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class ConsultancyList extends Node implements Iterable<Consultancy> {

   private List<Consultancy> consultancy;

   public ConsultancyList(List<Consultancy> consultancy) {
      this.consultancy = consultancy;
      Collections.sort(this.consultancy);
   }

   @Override
   public Iterator<Consultancy> iterator() {
      return this.consultancy.iterator();
   }

   public Stream<Consultancy> stream() {
      return this.consultancy.stream();
   }

   public boolean isEmpty() {
      return this.consultancy.isEmpty();
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}