package nl.raymondloman.resume.visitor;

import java.time.LocalDate;

import nl.raymondloman.resume.domain.Introduction;
import nl.raymondloman.resume.domain.KnowledgeMatrix;
import nl.raymondloman.resume.domain.PersonalIntroduction;
import nl.raymondloman.resume.domain.PersonalProject;
import nl.raymondloman.resume.domain.PersonalProjectList;
import nl.raymondloman.resume.domain.Resume;
import nl.raymondloman.resume.domain.Trainings;
import nl.raymondloman.resume.domain.YearMonthPrinterChain;
import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.domain.assignment.AssignmentList;
import nl.raymondloman.resume.domain.consultancy.Consultancy;
import nl.raymondloman.resume.domain.consultancy.ConsultancyList;
import nl.raymondloman.resume.domain.education.Course;
import nl.raymondloman.resume.domain.education.CourseList;
import nl.raymondloman.resume.domain.education.Education;
import nl.raymondloman.resume.domain.education.EducationContainer;
import nl.raymondloman.resume.domain.education.EducationList;
import nl.raymondloman.resume.domain.employer.Employer;
import nl.raymondloman.resume.domain.employer.EmployerList;
import nl.raymondloman.resume.domain.other.AdditionalInformation;
import nl.raymondloman.resume.domain.other.Header;
import nl.raymondloman.resume.domain.personal.Personalia;
import nl.raymondloman.resume.domain.personal.Recommendation;
import nl.raymondloman.resume.domain.personal.RecommendationList;
import nl.raymondloman.resume.domain.training.Training;

public class YearMonthPrinterChainGeneratingVisitor implements Visitor {
   
   private YearMonthPrinterChain chain;
   
   public YearMonthPrinterChainGeneratingVisitor(LocalDate periodStart, LocalDate periodMaxOfCompleteChain) {
      
      this.chain = new YearMonthPrinterChain(periodStart, periodMaxOfCompleteChain);
   }

   @Override
   public void visit(Resume resume) {
      resume.getAssignments().accept(this);
      resume.getTrainings().accept(this);
      resume.getConsultancies().accept(this);
      resume.getPersonalProjects().accept(this);
      resume.getEmployers().accept(this);
   }

   @Override
   public void visit(Header header) {
      
   }

   @Override
   public void visit(Personalia personalia) {
      
   }

   @Override
   public void visit(Employer employer) {
      this.chain.add(employer);
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
   public void visit(Training training) {
      this.chain.add(training);
   }

   @Override
   public void visit(Assignment assignment) {
      this.chain.add(assignment);
   }

   @Override
   public void visit(PersonalProject personalProject) {
      this.chain.add(personalProject);
      
   }

   @Override
   public void visit(EducationContainer educationContainer) {
      
   }

   @Override
   public void visit(Education education) {
      
   }

   @Override
   public void visit(Course course) {
      
   }

   @Override
   public void visit(AdditionalInformation additionalInformation) {
      
   }

   @Override
   public void visit(Recommendation recommendation) {
      
   }

   @Override
   public void visit(EmployerList employerList) {
      for(Employer e : employerList) {
         e.accept(this);
      }
      
   }

   @Override
   public void visit(AssignmentList assignmentList) {
      for(Assignment a : assignmentList) {
         a.accept(this);
      }
      
   }

   @Override
   public void visit(RecommendationList recommendationList) {
      
   }

   @Override
   public void visit(PersonalProjectList personalProjectList) {
      for(PersonalProject p : personalProjectList) {
         p.accept(this);
      }
      
   }

   @Override
   public void visit(ConsultancyList consultancyList) {
      for(Consultancy c : consultancyList) {
         c.accept(this);
      }
      
   }

   @Override
   public void visit(Consultancy consultancy) {
      this.chain.add(consultancy);
      
   }

   @Override
   public void visit(Introduction introduction) {
      
   }

   @Override
   public void visit(PersonalIntroduction personalIntroduction) {
      
   }

   @Override
   public void visit(EducationList educationList) {
      
   }

   @Override
   public void visit(CourseList courses) {
      
   }

   @Override
   public void visit(YearMonthPrinterChain yearMonthPrinterChain) {
      throw new RuntimeException(); // to prevent errors and will cleanup later
   }
   
   public YearMonthPrinterChain getResult() {
      return this.chain;
   }
}
