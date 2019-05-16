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
import nl.raymondloman.resume.domain.enums.Language;
import nl.raymondloman.resume.domain.other.AdditionalInformation;
import nl.raymondloman.resume.domain.other.Header;
import nl.raymondloman.resume.domain.personal.Personalia;
import nl.raymondloman.resume.domain.personal.Recommendation;
import nl.raymondloman.resume.domain.personal.RecommendationList;
import nl.raymondloman.resume.domain.training.Training;
import nl.raymondloman.resume.services.DateService;

public class YearMonthAsciidocGeneratingVisitor implements Visitor {
   
   private DateService dateService = new DateService();

   private LocalDate periodStart;
   private LocalDate periodMaxOfCompleteChain;

   private int currentYear = -1;

   public YearMonthAsciidocGeneratingVisitor(LocalDate periodStart, LocalDate periodMaxOfCompleteChain) {

      this.periodStart = periodStart;
      this.periodMaxOfCompleteChain = periodMaxOfCompleteChain;
   }

   @Override
   public void visit(Resume resume) {
      System.out.println("=== Chronologisch overzicht");
      YearMonthPrinterChainGeneratingVisitor generating = new YearMonthPrinterChainGeneratingVisitor(periodStart,
                        periodMaxOfCompleteChain);
      resume.accept(generating);
      YearMonthPrinterChain chain = generating.getResult();

      chain.accept(this);

   }

   @Override
   public void visit(YearMonthPrinterChain currentChain) {

      YearMonthPrinterChain subChain = currentChain.getChain();
      if (subChain != null) {
         System.out.println();
         subChain.accept(this);
      }
      if (!currentChain.isEmpty()) {

         System.out.println();
         if (this.currentYear != currentChain.getPeriodStart().getYear()) {
            System.out.printf("==== %d%n", currentChain.getPeriodStart().getYear());
            this.currentYear = currentChain.getPeriodStart().getYear();
            System.out.println();
         }
         System.out.printf("===== %s %d%n", dateService.getMonthDisplayName(currentChain.getPeriodStart().getMonth(), Language.ENGLISH), currentChain.getPeriodStart().getYear());
         System.out.println("{empty}");
         System.out.println();
         if (!currentChain.getAssignments().isEmpty()) {
            System.out.println("[none]");
            for (Assignment a : currentChain.getAssignments()) {
               a.accept(this);
            }
            System.out.println();
         }

         if (!currentChain.getTrainingsItems().isEmpty()) {
            System.out.println("[none]");
            System.out.println("* Trainings");
            System.out.println("[none]");
            for (Training t : currentChain.getTrainingsItems()) {
               t.accept(this);
            }
            System.out.println();
         }

         if (!currentChain.getEmployers().isEmpty()) {
            System.out.println("[none]");
            System.out.println("* Employers");
            System.out.println("[none]");
            for (Employer e : currentChain.getEmployers()) {
               e.accept(this);
            }
            System.out.println();
         }
      }
   }

   @Override
   public void visit(Header header) {

   }

   @Override
   public void visit(Personalia personalia) {

   }

   @Override
   public void visit(Employer employer) {
      System.out.printf("** %s %s%n", employer.getName(), employer.getFunction());
   }

   @Override
   public void visit(KnowledgeMatrix knowledgeMatrix) {
   }

   @Override
   public void visit(Trainings trainings) {

   }

   @Override
   public void visit(Training training) {
      System.out.printf("** %s (%02d-%02d t/m %02d-%02d) for %s%n", 
                        training.getName(), 
                        training.getStartDate().getDayOfMonth(), 
                        training.getStartDate().getMonthValue(),
                        training.getEndDate().getDayOfMonth(),
                        training.getEndDate().getMonthValue(),
                        training.getCustomers().get(0));
   }

   @Override
   public void visit(Assignment assignment) {
      System.out.printf("* %s%n", assignment.getName());

   }

   @Override
   public void visit(PersonalProject personalProject) {
      System.out.printf("* %s%n", personalProject.getDescription()+" (personal project)");
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

   }

   @Override
   public void visit(AssignmentList assignmentList) {

   }

   @Override
   public void visit(RecommendationList recommendationList) {

   }

   @Override
   public void visit(PersonalProjectList personalProjectList) {

   }

   @Override
   public void visit(ConsultancyList consultancyList) {

   }

   @Override
   public void visit(Consultancy consultancy) {
      System.out.printf("* %s%n", consultancy.getName() +" consultancy(" +consultancy.getDescription()+")" );
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
}