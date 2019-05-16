package nl.raymondloman.resume.domain.education;

import java.util.List;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class EducationContainer extends Node {

   private EducationList schools;
   private EducationList other;
   private CourseList courses;

   public EducationList getSchools() {
      return this.schools;
   }

   public void setSchools(List<Education> schools) {
      this.schools = new EducationList(schools);
   }

   public CourseList getCourses() {
      return this.courses;
   }

   public void setCourses(List<Course> courses) {
      this.courses = new CourseList(courses);
   }

   public EducationList getOther() {
      return this.other;
   }

   public void setOther(List<Education> other) {
      this.other = new EducationList(other);
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}