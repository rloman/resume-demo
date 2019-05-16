package nl.raymondloman.resume.filter;

public class EducationFilter {

   private boolean on = true;
   private boolean schools = true;
   private boolean courses = true;
   private boolean other = true;

   public boolean isOn() {
      return this.on;
   }

   public void setOn(boolean on) {
      this.on = on;
   }

   public boolean isSchools() {
      return this.schools;
   }

   public void setSchools(boolean schools) {
      this.schools = schools;
   }

   public boolean isCourses() {
      return this.courses;
   }

   public void setCourses(boolean courses) {
      this.courses = courses;
   }

   public boolean isOther() {
      return this.other;
   }

   public void setOther(boolean other) {
      this.other = other;
   }
}