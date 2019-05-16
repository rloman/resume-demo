package nl.raymondloman.resume.domain.education;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class CourseList extends Node implements Iterable<Course> {
   
   private List<Course> shorttermEducation;

   public CourseList(List<Course> shorttermEducation) {
      this.shorttermEducation = shorttermEducation;
      Collections.sort(this.shorttermEducation);
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }

   @Override
   public Iterator<Course> iterator() {
     return this.shorttermEducation.iterator();
   }

   public boolean isEmpty() {
      return shorttermEducation.isEmpty();
   }
   
   public Stream<Course> stream() {
      return this.shorttermEducation.stream();
   }
}
