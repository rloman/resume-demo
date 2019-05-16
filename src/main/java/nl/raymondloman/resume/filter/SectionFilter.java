package nl.raymondloman.resume.filter;

public class SectionFilter {

   private boolean recommendations = true;
   private WorkItemSectionFilter overview = new WorkItemSectionFilter();
   private WorkItemSectionFilter extensive = new WorkItemSectionFilter();
   private EducationFilter education = new EducationFilter();
   private AdditionalInfoFilter additionalInfo = new AdditionalInfoFilter();

   public boolean isRecommendations() {
      return this.recommendations;
   }

   public void setRecommendations(boolean recommendations) {
      this.recommendations = recommendations;
   }

   public WorkItemSectionFilter getOverview() {
      return this.overview;
   }

   public void setOverview(WorkItemSectionFilter overview) {
      this.overview = overview;
   }

   public WorkItemSectionFilter getExtensive() {
      return this.extensive;
   }

   public void setExtensive(WorkItemSectionFilter extensive) {
      this.extensive = extensive;
   }

   public EducationFilter getEducation() {
      return this.education;
   }

   public void setEducation(EducationFilter education) {
      this.education = education;
   }

   public AdditionalInfoFilter getAdditionalInfo() {
      return this.additionalInfo;
   }

   public void setAdditionalInfo(AdditionalInfoFilter additionalInfo) {
      this.additionalInfo = additionalInfo;
   }

   public boolean isSchools() {
      return this.education.isSchools();
   }

   public void setSchools(boolean schools) {
      this.education.setSchools(schools);
   }

   public boolean isCourses() {
      return this.education.isCourses();
   }

   public void setCourses(boolean courses) {
      this.education.setCourses(courses);
   }

   public boolean isOther() {
      return this.education.isOther();
   }

   public void setOther(boolean other) {
      this.education.setOther(other);
   }

   public boolean isHobbies() {
      return this.additionalInfo.isHobbies();
   }

   public void setHobbies(boolean hobbies) {
      this.additionalInfo.setHobbies(hobbies);
   }

   public boolean isOtherCharacteristics() {
      return this.additionalInfo.isOtherCharacteristics();
   }

   public void setOtherCharacteristics(boolean otherCharacteristics) {
      this.additionalInfo.setOtherCharacteristics(otherCharacteristics);
   }
}