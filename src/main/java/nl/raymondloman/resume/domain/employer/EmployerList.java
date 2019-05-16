package nl.raymondloman.resume.domain.employer;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class EmployerList extends Node implements Iterable<Employer> {

   private List<Employer> employers;

   public EmployerList(List<Employer> employers) {
      this.employers = employers;
      Collections.sort(this.employers);
   }

   @Override
   public Iterator<Employer> iterator() {
      return this.employers.iterator();
   }

   public Stream<Employer> stream() {
      return this.employers.stream();
   }

   public boolean isEmpty() {
      return this.employers.isEmpty();
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}
