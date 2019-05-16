package nl.raymondloman.resume.domain.education;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class EducationList extends Node implements Iterable<Education> {
   
   private List<Education> schools;

   public EducationList(List<Education> schools) {
      this.schools = schools;
      Collections.sort(this.schools);
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }

   @Override
   public Iterator<Education> iterator() {
     return this.schools.iterator();
   }
   
   public Stream<Education> stream() {
      return this.schools.stream();
   }

   public boolean isEmpty() {
      return schools.isEmpty();
   }
}