package nl.raymondloman.resume.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.domain.assignment.AssignmentList;
import nl.raymondloman.resume.domain.consultancy.Consultancy;
import nl.raymondloman.resume.domain.consultancy.ConsultancyList;
import nl.raymondloman.resume.domain.education.EducationContainer;
import nl.raymondloman.resume.domain.employer.Employer;
import nl.raymondloman.resume.domain.employer.EmployerList;
import nl.raymondloman.resume.domain.other.AdditionalInformation;
import nl.raymondloman.resume.domain.other.Header;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.domain.personal.Personalia;
import nl.raymondloman.resume.domain.personal.Recommendation;
import nl.raymondloman.resume.domain.personal.RecommendationList;
import nl.raymondloman.resume.domain.training.Training;
import nl.raymondloman.resume.visitor.Visitor;

public class Resume extends Node {

   private Header header = new Header();

   private Personalia personalia;

   private PersonalIntroduction personalIntroduction;

   private EmployerList employers;

   private Introduction businessIntroduction;

   private Trainings trainings = new Trainings();

   private AssignmentList assignments;

   private ConsultancyList consultancies;

   private PersonalProjectList personalProjects;

   private EducationContainer educationContainer;

   private AdditionalInformation additionalInformation;

   private RecommendationList recommendations;

   private List<ManuallyOverriddenKnowledgeBean> manuallyOverriddenKnowledge;

   public Header getHeader() {
      return this.header;
   }

   public Personalia getPersonalia() {
      return this.personalia;
   }

   public EmployerList getEmployers() {
      return this.employers;
   }

   public Trainings getTrainings() {
     return this.trainings;
   }

   public void setTrainings(List<Training> trainings) {
      for (Training element : trainings) {
         if (!this.trainings.get(element.getType()).isPresent()) {
            this.trainings.put(element.getType(), new ArrayList<>());
         }
         Optional<List<Training>> optionalList = this.trainings.get(element.getType()); 
         if(optionalList.isPresent()) {
            optionalList.get().add(element);
         }
         
      }
   }

   public AssignmentList getAssignments() {
      return this.assignments;
   }

   public EducationContainer getEducationContainer() {
      return this.educationContainer;
   }

   public void setEducationContainer(EducationContainer educationContainer) {
      this.educationContainer = educationContainer;
   }

   public AdditionalInformation getAdditionalInformation() {
      return this.additionalInformation;
   }

   public void setAdditionalInformation(AdditionalInformation additionalInformation) {
      this.additionalInformation = additionalInformation;
   }

   public RecommendationList getRecommendations() {
      return this.recommendations;
   }

   public void setRecommendations(List<Recommendation> recommendations) {
      this.recommendations = new RecommendationList(recommendations);
   }

   public void setEmployers(List<Employer> employers) {
      this.employers = new EmployerList(employers);
   }

   public void setAssignments(List<Assignment> assignments) {
      this.assignments = new AssignmentList(assignments);
   }

   public PersonalProjectList getPersonalProjects() {
      return this.personalProjects;
   }

   public void setPersonalProjects(List<PersonalProject> personalProjects) {
      this.personalProjects = new PersonalProjectList(personalProjects);
   }

   public ConsultancyList getConsultancies() {
      return this.consultancies;
   }

   public void setConsultancies(List<Consultancy> consultancies) {
      this.consultancies = new ConsultancyList(consultancies);
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }

   @Override
   public String toString() {
      return "- Resume [personalia=" + this.personalia + ", assignments=" + this.assignments + "]";
   }

   public Introduction getBusinessIntroduction() {
      return this.businessIntroduction;
   }

   public void setBusinessIntroduction(Introduction introduction) {
      this.businessIntroduction = introduction;
   }

   public List<ManuallyOverriddenKnowledgeBean> getManuallyOverriddenKnowledge() {
      return this.manuallyOverriddenKnowledge;
   }

   public void setManuallyOverriddenKnowledge(List<ManuallyOverriddenKnowledgeBean> manuallyOverriddenKnowledge) {
      this.manuallyOverriddenKnowledge = manuallyOverriddenKnowledge;
   }

   public PersonalIntroduction getPersonalIntroduction() {
      return this.personalIntroduction;
   }

   public void setPersonalIntroduction(PersonalIntroduction personalIntroduction) {
      this.personalIntroduction = personalIntroduction;
   }
}
