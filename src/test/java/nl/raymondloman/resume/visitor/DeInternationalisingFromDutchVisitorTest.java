package nl.raymondloman.resume.visitor;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.domain.employer.Employer;
import nl.raymondloman.resume.domain.i18n.ListI18N;
import nl.raymondloman.resume.domain.i18n.StringI18N;
import nl.raymondloman.resume.visitor.DeInternationalisingFromDutchVisitor;

public class DeInternationalisingFromDutchVisitorTest  {
   
   private DeInternationalisingFromDutchVisitor visitor;
   
   @Before
   public void setUp() {
      this.visitor = new DeInternationalisingFromDutchVisitor();
   }
   
   @Test
   public void testVisitEmployer() {
      
      Employer employer = new Employer();
      employer.setFunctionI18N(new StringI18N());
      employer.setResponsibilitiesI18N(new ListI18N());
      employer.setResultsI18N(new ListI18N());
      employer.getFunctionI18N().setNl("Pragmatisch Programmeur");
      employer.getResponsibilitiesI18N().setNl(Arrays.asList("resp1", "resp2", "resp3"));
      employer.getResultsI18N().setNl(Arrays.asList("result1", "result2", "result3"));
      
      employer.accept(this.visitor);
      
      assertEquals("Pragmatisch Programmeur", employer.getFunction());
      assertEquals(Arrays.asList("resp1", "resp2", "resp3"), employer.getResponsibilities());
      assertEquals(Arrays.asList("result1", "result2", "result3"), employer.getResults());
   }
   
   @Test
   public void testVisitAssignment() {
      
      Assignment assignment = new Assignment();
      assignment.setFunctionI18N(new StringI18N());
      assignment.setDescriptionI18N(new StringI18N());
      assignment.setObjectivesI18N(new ListI18N());
      assignment.setResultsI18N(new ListI18N());
      
      assignment.getFunctionI18N().setNl("Pragmatisch Programmeur");
      assignment.getDescriptionI18N().setNl("description");
      assignment.getObjectivesI18N().setNl(Arrays.asList("objective1", "objective2", "objective3"));
      assignment.getResultsI18N().setNl(Arrays.asList("result1", "result2", "result3"));
      
      assignment.accept(this.visitor);
      
      assertEquals("Pragmatisch Programmeur", assignment.getFunction());
      assertEquals("description", assignment.getDescription());
      assertEquals(Arrays.asList("objective1", "objective2", "objective3"), assignment.getObjectives());
      assertEquals(Arrays.asList("result1", "result2", "result3"), assignment.getResults());
   }
}