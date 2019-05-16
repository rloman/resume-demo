package nl.raymondloman.resume.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class DeInternationalisingFromDutchVisitor extends AbstractDeInternationalisingVisitor {

   private static final Logger LOGGER = LoggerFactory.getLogger(DeInternationalisingFromDutchVisitor.class);

   @Override
   public void visit(Employer employer) {
      employer.setFunction(employer.getFunctionI18N().getNl());
      if (employer.getResponsibilitiesI18N() != null) {
         employer.setResponsibilities(employer.getResponsibilitiesI18N().getNl());
      }
      if (employer.getResultsI18N() != null) {
         employer.setResults(employer.getResultsI18N().getNl());
      }
   }

   @Override
   public void visit(Assignment assignment) {
      assignment.setFunction(assignment.getFunctionI18N().getNl());
      assignment.setDescription(assignment.getDescriptionI18N().getNl());
      assignment.setObjectives(assignment.getObjectivesI18N().getNl());
      assignment.setResults(assignment.getResultsI18N().getNl());
   }

   @Override
   public void visit(PersonalProject personalProject) {
      this.visit((Assignment) personalProject);
   }

   @Override
   public void visit(Education education) {
      if (education.getDescriptionI18N() != null) {
         education.setDescription(education.getDescriptionI18N().getNl());
      }
   }

   @Override
   public void visit(Course course) {
      this.visit((Education) course);
   }

   @Override
   public void visit(AdditionalInformation additionalInformation) {
      additionalInformation.setPointsOfAttention(additionalInformation.getPointsOfAttentionI18N().getNl());
      additionalInformation.setHobbies(additionalInformation.getHobbiesI18N().getNl());
      additionalInformation.setRemainder(additionalInformation.getRemainderI18N().getNl());

   }

   @Override
   public void visit(Recommendation recommendation) {
      recommendation.setLines(recommendation.getLinesI18N().getNl());
      recommendation.setWho(recommendation.getWhoI18N().getNl());

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
      consultancy.setDescription(consultancy.getDescriptionI18N().getNl());
      consultancy.setObjectives(consultancy.getObjectivesI18N().getNl());
      consultancy.setResults(consultancy.getResultsI18N().getNl());

   }

   @Override
   public void visit(Introduction introduction) {
      if (introduction.getTitleI18N() != null) {
         introduction.setTitle(introduction.getTitleI18N().getNl());
      }
      if (introduction.getLinesI18N() != null) {
         introduction.setLines(introduction.getLinesI18N().getNl());
      }
      if (introduction.getActivitiesTitleI18N() != null) {
         introduction.setActivitiesTitle(introduction.getActivitiesTitleI18N().getNl());
      }

      super.visit(introduction);
   }

   @Override
   public void visit(Training training) {
      LOGGER.debug("Training result before deInternationalisation [{}]", training.getResultsI18N());
      if (training.getResultsI18N() != null) {
         training.setResults(training.getResultsI18N().getNl());
      }
      LOGGER.debug("Training result after deInternationalisation [{}]", training.getResults());
   }

   @Override
   public void visit(YearMonthPrinterChain yearMonthPrinterChain) {
      // TODO Auto-generated method stub
      
   }
}