package nl.raymondloman.resume.filter;


public class WorkItemSectionFilter {

   // to disable the rest below (so this entire stuff will not be printed if false)
   private boolean on = true;

   private boolean trainings = true;
   private boolean assignments = true;
   private boolean consultancy = true;
   private boolean employers = true;
   private boolean personalProjects = true;

   public boolean isOn() {
      return this.on;
   }

   public void setOn(boolean on) {
      this.on = on;
   }

   public boolean isTrainings() {
      return this.trainings;
   }

   public void setTrainings(boolean trainings) {
      this.trainings = trainings;
   }

   public boolean isAssignments() {
      return this.assignments;
   }

   public void setAssignments(boolean assignments) {
      this.assignments = assignments;
   }

   public boolean isConsultancy() {
      return this.consultancy;
   }

   public void setConsultancy(boolean consultancy) {
      this.consultancy = consultancy;
   }

   public boolean isEmployers() {
      return this.employers;
   }

   public void setEmployers(boolean employers) {
      this.employers = employers;
   }

   public boolean isSelfEmployedOn() {
      return this.isOn() && (this.isAssignments() || this.isConsultancy() || this.isTrainings());
   }

   public boolean isPersonalProjects() {
      return this.personalProjects;
   }

   public void setPersonalProjects(boolean personalProjects) {
      this.personalProjects = personalProjects;
   }

   @Override
   public String toString() {
      return "WorkItemSectionFilter [on=" + this.on + ", trainings=" + this.trainings + ", assignments=" + this.assignments + ", consultancy=" + this.consultancy + ", employers=" + this.employers + ", personalProjects="
                        + this.personalProjects + "]";
   }
}