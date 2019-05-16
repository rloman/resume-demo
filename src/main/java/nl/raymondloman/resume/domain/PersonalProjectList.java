package nl.raymondloman.resume.domain;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class PersonalProjectList extends Node implements Iterable<PersonalProject> {

   private List<PersonalProject> personalProjects;

   public PersonalProjectList(List<PersonalProject> personalProjects) {
      this.personalProjects = personalProjects;
      Collections.sort(this.personalProjects);
   }

   public boolean isEmpty() {
      return this.personalProjects.isEmpty();
   }

   @Override
   public Iterator<PersonalProject> iterator() {
      return this.personalProjects.iterator();
   }

   public Stream<PersonalProject> stream() {
      return this.personalProjects.stream();
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}