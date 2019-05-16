package nl.raymondloman.resume.visitor;

import nl.raymondloman.resume.domain.Introduction;
import nl.raymondloman.resume.domain.PersonalProject;
import nl.raymondloman.resume.domain.PersonalProjectList;
import nl.raymondloman.resume.domain.YearMonthPrinterChain;
import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.domain.assignment.AssignmentList;
import nl.raymondloman.resume.domain.consultancy.Consultancy;
import nl.raymondloman.resume.domain.consultancy.ConsultancyList;
import nl.raymondloman.resume.domain.education.Course;
import nl.raymondloman.resume.domain.education.Education;
import nl.raymondloman.resume.domain.employer.Employer;
import nl.raymondloman.resume.domain.employer.EmployerList;
import nl.raymondloman.resume.domain.other.AdditionalInformation;
import nl.raymondloman.resume.domain.personal.Recommendation;
import nl.raymondloman.resume.domain.personal.RecommendationList;
import nl.raymondloman.resume.domain.training.Training;

public class DeInternationalisingFromEnglishVisitor extends AbstractDeInternationalisingVisitor {

   @Override
   public void visit(Employer employer) {
      employer.setFunction(employer.getFunctionI18N().getEn());
      if (employer.getResponsibilitiesI18N() != null) {
         employer.setResponsibilities(employer.getResponsibilitiesI18N().getEn());
      }
      if (employer.getResultsI18N() != null) {
         employer.setResults(employer.getResultsI18N().getEn());
      }
   }

   @Override
   public void visit(Assignment assignment) {
      assignment.setFunction(assignment.getFunctionI18N().getEn());
      assignment.setDescription(assignment.getDescriptionI18N().getEn());
      assignment.setObjectives(assignment.getObjectivesI18N().getEn());
      assignment.setResults(assignment.getResultsI18N().getEn());
   }

   @Override
   public void visit(PersonalProject personalProject) {
      this.visit((Assignment) personalProject);
   }

   @Override
   public void visit(Education education) {
      if (education.getDescriptionI18N() != null) {
         education.setDescription(education.getDescriptionI18N().getEn());
      }
   }

   @Override
   public void visit(Course course) {
      this.visit((Education) course);
   }

   @Override
   public void visit(AdditionalInformation additionalInformation) {
      additionalInformation.setPointsOfAttention(additionalInformation.getPointsOfAttentionI18N().getEn());
      additionalInformation.setHobbies(additionalInformation.getHobbiesI18N().getEn());
      additionalInformation.setRemainder(additionalInformation.getRemainderI18N().getEn());

   }

   @Override
   public void visit(Recommendation recommendation) {
      recommendation.setLines(recommendation.getLinesI18N().getEn());
      recommendation.setWho(recommendation.getWhoI18N().getEn());

   }

   @Override
   public void visit(EmployerList employerList) {
      for (Employer employer : employerList) {
         employer.accept(this);
      }

   }

   @Override
   public void visit(AssignmentList assignmentList) {
      for (Assignment assignment : assignmentList) {
         assignment.accept(this);
      }
   }

   @Override
   public void visit(RecommendationList recommendationList) {
      for (Recommendation recommendation : recommendationList) {
         recommendation.accept(this);
      }
   }

   @Override
   public void visit(PersonalProjectList personalProjectList) {
      for (PersonalProject personalProject : personalProjectList) {
         personalProject.accept(this);
      }
   }

   @Override
   public void visit(ConsultancyList consultancyList) {
      for (Consultancy consultancy : consultancyList) {
         consultancy.accept(this);
      }
   }

   @Override
   public void visit(Consultancy consultancy) {
      consultancy.setDescription(consultancy.getDescriptionI18N().getEn());
      consultancy.setObjectives(consultancy.getObjectivesI18N().getEn());
      consultancy.setResults(consultancy.getResultsI18N().getEn());

   }

   @Override
   public void visit(Introduction introduction) {
      if (introduction.getTitleI18N() != null) {
         introduction.setTitle(introduction.getTitleI18N().getEn());
      }
      if (introduction.getLinesI18N() != null) {
         introduction.setLines(introduction.getLinesI18N().getEn());
      }
      if (introduction.getActivitiesTitleI18N() != null) {
         introduction.setActivitiesTitle(introduction.getActivitiesTitleI18N().getEn());
      }

      super.visit(introduction);
   }

   @Override
   public void visit(Training training) {
      if (training.getResultsI18N() != null) {
         training.setResults(training.getResultsI18N().getEn());
      }
   }

   @Override
   public void visit(YearMonthPrinterChain yearMonthPrinterChain) {
      // TODO Auto-generated method stub
      
   }
}