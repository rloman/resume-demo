package nl.raymondloman.resume.visitor;

import nl.raymondloman.resume.domain.Introduction;
import nl.raymondloman.resume.domain.KnowledgeMatrix;
import nl.raymondloman.resume.domain.PersonalIntroduction;
import nl.raymondloman.resume.domain.Resume;
import nl.raymondloman.resume.domain.Trainings;
import nl.raymondloman.resume.domain.education.Course;
import nl.raymondloman.resume.domain.education.Education;
import nl.raymondloman.resume.domain.education.EducationContainer;
import nl.raymondloman.resume.domain.education.EducationList;
import nl.raymondloman.resume.domain.education.CourseList;
import nl.raymondloman.resume.domain.other.Header;
import nl.raymondloman.resume.domain.personal.Personalia;
import nl.raymondloman.resume.domain.training.Training;

public abstract class AbstractDeInternationalisingVisitor implements Visitor {

   @Override
   public void visit(Resume resume) {
      resume.getPersonalIntroduction().accept(this);
      resume.getRecommendations().accept(this);
      resume.getConsultancies().accept(this);
      resume.getPersonalProjects().accept(this);
      resume.getAssignments().accept(this);
      resume.getTrainings().accept(this);
      resume.getEmployers().accept(this);
      resume.getEducationContainer().accept(this);
      resume.getAdditionalInformation().accept(this);
   }

   @Override
   public void visit(EducationContainer educationList) {

      for (Education schoolEducation : educationList.getSchools()) {
         schoolEducation.accept(this);
      }
      for (Education shorttermEducation : educationList.getCourses()) {
         shorttermEducation.accept(this);
      }
      for (Education andereEducation : educationList.getOther()) {
         andereEducation.accept(this);
      }
   }

   @Override
   public void visit(Header header) {

   }

   @Override
   public void visit(Personalia personalia) {

   }

   @Override
   public void visit(KnowledgeMatrix knowledgeMatrix) {

   }

   @Override
   public void visit(Trainings trainings) {
      for(Training training : trainings) {
         training.accept(this);
      }
   }

   @Override
   public void visit(Introduction introduction) {
      if (introduction.getSub() != null) {
         for(Introduction intro: introduction.getSub()){
            intro.accept(this);
         }
      }
   }
   
   @Override
   public void visit(PersonalIntroduction introduction) {
      visit((Introduction) introduction);
   }

   @Override
   public void visit(EducationList schoolEducationList) {
      for(Education education : schoolEducationList) {
         education.accept(this);
      }
   }

   @Override
   public void visit(CourseList shorttermEducationList) {
      for(Course education : shorttermEducationList) {
         education.accept(this);
      }
   }
}