package nl.raymondloman.resume.visitor;

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

public interface Visitor {

   void visit(Resume resume);
   void visit(Header header);
   void visit(Personalia personalia);
   void visit(Employer employer);
   void visit(KnowledgeMatrix knowledgeMatrix);
   void visit(Trainings trainings);
   void visit(Training training);
   void visit(Assignment assignment);
   void visit(PersonalProject personalProject);
   void visit(EducationContainer educationContainer);
   void visit(Education education);
   void visit(Course course);
   void visit(AdditionalInformation additionalInformation);
   void visit(Recommendation recommendation);
   void visit(EmployerList employerList);
   void visit(AssignmentList assignmentList);
   void visit(RecommendationList recommendationList);
   void visit(PersonalProjectList personalProjectList);
   void visit(ConsultancyList consultancyList);
   void visit(Consultancy consultancy);
   void visit(Introduction introduction);
   void visit(PersonalIntroduction personalIntroduction);
   void visit(EducationList educationList);
   void visit(CourseList courses);
   void visit(YearMonthPrinterChain yearMonthPrinterChain);
}