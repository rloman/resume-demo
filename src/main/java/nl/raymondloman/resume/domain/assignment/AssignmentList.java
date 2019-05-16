package nl.raymondloman.resume.domain.assignment;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class AssignmentList extends Node implements Iterable<Assignment> {

   private List<Assignment> assignments;

   public AssignmentList(List<Assignment> assignments) {
      this.assignments = assignments;
      Collections.sort(this.assignments);
   }

   @Override
   public Iterator<Assignment> iterator() {
      return this.assignments.iterator();
   }

   public Stream<Assignment> stream() {
      return this.assignments.stream();
   }

   public boolean isEmpty() {
      return this.assignments.isEmpty();
   }

   @Override
   public String toString() {
      return "- AssignmentList: [assignments=" + this.assignments + "]";
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}
