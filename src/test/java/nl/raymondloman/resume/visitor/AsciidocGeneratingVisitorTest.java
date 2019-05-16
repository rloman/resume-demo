package nl.raymondloman.resume.visitor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.raymondloman.resume.domain.AbstractWorkItem;
import nl.raymondloman.resume.domain.Experience;
import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.domain.assignment.AssignmentList;
import nl.raymondloman.resume.domain.consultancy.Consultancy;
import nl.raymondloman.resume.domain.education.Course;
import nl.raymondloman.resume.domain.education.Education;
import nl.raymondloman.resume.domain.education.EducationContainer;
import nl.raymondloman.resume.domain.employer.Employer;
import nl.raymondloman.resume.domain.employer.EmployerList;
import nl.raymondloman.resume.domain.enums.Language;
import nl.raymondloman.resume.domain.enums.Topic;
import nl.raymondloman.resume.domain.enums.TrainingType;
import nl.raymondloman.resume.domain.other.KnowledgeBean;
import nl.raymondloman.resume.domain.training.Training;
import nl.raymondloman.resume.visitor.AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder;

public class AsciidocGeneratingVisitorTest extends GeneratingVisitorTestHelper {

   private static final String JAN31EN = "2014-01-31";
   private static final String JAN28ENJAN30 = "28-01-2014 en 30-01-2014";
   private static final String JAN28 = "28-01-2014";
   private static final String FAILMESSAGE = "Should fail since we are making a data mistake";
   private static final String JAN28ENJAN29 = "28-01-2014 en 29-01-2014";
   private static final String JAN28TMJAN30 = "28-01-2014 t/m 30-01-2014";
   private static final String JAN30EN = "2014-01-30";
   private static final String JAN28EN = "2014-01-28";

   private AsciidocGeneratingVisitor visitor;

   @Before
   public void setUp() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      this.visitor = b.build();
   }
   
   @Test
   public void testFilterEducationNull() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.history(3);
      this.visitor = b.build();
      
      Education education = new Education();
      boolean result = this.visitor.filterEducation(education);
      
      Assert.assertTrue(result);
   }
   
   @Test
   public void testFilterEducationTwoBack() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.history(3);
      this.visitor = b.build();
      
      Education education = new Education();
      education.setEndDate((LocalDate.now()).minusYears(1));
      boolean result = this.visitor.filterEducation(education);
      
      Assert.assertTrue(result);
   }
   
   @Test
   public void testFilterEducationThreeBack() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.history(3);
      this.visitor = b.build();
      
      Education education = new Education();
      education.setEndDate((LocalDate.parse("2014-01-01")));
      boolean result = this.visitor.filterEducation(education);
      
      Assert.assertFalse(result);
   }
   
   @Test
   public void testFilterGeneralFilterSetToThreeYears() {
      
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.history(3);
      this.visitor = b.build();
      
      boolean result = this.visitor.filterGeneral(LocalDate.parse("2014-01-01"));
      
      Assert.assertFalse(result);
      
   }
   
   // no filter set so should be true
   @Test
   public void testFilterGeneral() {
      
      boolean result = this.visitor.filterGeneral(LocalDate.now());
      
      Assert.assertTrue(result);
      
   }
   
   @Test
   public void testIsTrainingtypeToBeRenderedBlacklistedAndWhitelisted() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.minimalTrainingOccurrences(2);
      b.disableTrainingType(TrainingType.Advanced_JavaScript);
      b.enableTrainingType(TrainingType.Advanced_JavaScript);
      
      this.visitor= b.build();
      
      TrainingType trainingType = TrainingType.Advanced_JavaScript;
      int amount = 100;
      
      boolean result = this.visitor.isTrainingtypeToBeRendered(trainingType, amount);
      
      Assert.assertFalse(result);
   }
   
   @Test
   public void testIsTrainingtypeToBeRenderedBlacklisted() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.minimalTrainingOccurrences(2);
      b.disableTrainingType(TrainingType.Advanced_JavaScript);
      
      this.visitor= b.build();
      
      TrainingType trainingType = TrainingType.Advanced_JavaScript;
      int amount = 100;
      
      boolean result = this.visitor.isTrainingtypeToBeRendered(trainingType, amount);
      
      Assert.assertFalse(result);
   }
   
   @Test
   public void testIsTrainingtypeToBeRenderedWhitelisted() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.minimalTrainingOccurrences(2);
      b.enableTrainingType(TrainingType.Advanced_JavaScript);
      
      this.visitor= b.build();
      
      TrainingType trainingType = TrainingType.Advanced_JavaScript;
      int amount = 1;
      
      boolean result = this.visitor.isTrainingtypeToBeRendered(trainingType, amount);
      
      Assert.assertTrue(result);
   }
   
   @Test
   public void testIsTrainingtypeToBeRenderedFalse() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.minimalTrainingOccurrences(2);
      
      this.visitor= b.build();
      
      TrainingType trainingType = TrainingType.Advanced_JavaScript;
      int amount = 1;
      
      boolean result = this.visitor.isTrainingtypeToBeRendered(trainingType, amount);
      
      Assert.assertFalse(result);
   }
   
   @Test
   public void testIsTrainingtypeToBeRenderedTrue() {
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      b.minimalTrainingOccurrences(2);
      
      this.visitor= b.build();
      
      TrainingType trainingType = TrainingType.Advanced_JavaScript;
      int amount = 3;
      
      boolean result = this.visitor.isTrainingtypeToBeRendered(trainingType, amount);
      
      Assert.assertTrue(result);
   }
   
   @Test
   public void testRenderSummarizedOverviewAssignmentsTwo() {
      List<Assignment> assigments = new ArrayList<>();

      {
         Assignment crv = new Assignment();
         crv.setName("CRV");
         crv.setFunction("Software Engineer / Trainer");
         crv.setStartDate(LocalDate.parse("2014-01-21"));
         crv.setEndDate(LocalDate.parse("2014-09-01"));
         crv.setCity("Arnhem");
         assigments.add(crv);
      }
      
      {
         Assignment allego = new Assignment();
         allego.setName("Allego");
         allego.setFunction("Software Engineer");
         allego.setStartDate(LocalDate.parse("2015-07-01"));
         allego.setEndDate(LocalDate.parse("2016-10-01"));
         allego.setCity("Arnhem");
         assigments.add(allego);
      }

      AssignmentList assignmentList = new AssignmentList(assigments);
      this.visitor.renderSummarizedOverviewAssignments(assignmentList);

      int line = 1;
      check("=== Beknopt overzicht opdrachten", line++);
      check(".Carpago Software", line++);
      line++;
      
      check("[cols=\"4,4,3\" options=\"header\", caption=\"\", frame=\"all\" ]", line++);
      check("|===", line++);
      check("^|Klant ^|Functie ^|Periode", line++);
      check("|Allego |Software Engineer ^|Jul 2015 - Okt 2016", line++);
      check("|CRV |Software Engineer / Trainer ^|Jan 2014 - Sep 2014", line++);
      
   }

   
   @Test
   public void testRenderSummarizedOverviewAssignmentsOne() {
      List<Assignment> assigments = new ArrayList<>();

      {
         Assignment crv = new Assignment();
         crv.setName("CRV");
         crv.setFunction("Software Engineer / Trainer");
         crv.setStartDate(LocalDate.parse("2014-01-21"));
         crv.setEndDate(LocalDate.parse("2014-09-01"));
         crv.setCity("Arnhem");
         assigments.add(crv);
      }

      AssignmentList assignmentList = new AssignmentList(assigments);
      this.visitor.renderSummarizedOverviewAssignments(assignmentList);

      int line = 1;
      check("=== Beknopt overzicht opdrachten", line++);
      check(".Carpago Software", line++);
      line++;
      
      check("[cols=\"4,4,3\" options=\"header\", caption=\"\", frame=\"all\" ]", line++);
      check("|===", line++);
      check("^|Klant ^|Functie ^|Periode", line++);
      check("|CRV |Software Engineer / Trainer ^|Jan 2014 - Sep 2014", line++);
      
   }

   @Test
   public void testRenderSummarizedOverviewEmployersTwo() {

      List<Employer> employers = new ArrayList<>();

      {
         Employer infor = new Employer();
         infor.setName("Infor");
         infor.setFunction("Software Engineer");
         infor.setStartDate(LocalDate.parse("2012-09-01"));
         infor.setEndDate(LocalDate.parse("2014-01-01"));
         infor.setCity("Almelo");
         employers.add(infor);
      }
      
      {
         Employer belastingdienst = new Employer();
         belastingdienst.setName("Belastingdienst");
         belastingdienst.setFunction("Software Ontwikkelaar");
         belastingdienst.setStartDate(LocalDate.parse("2004-09-01"));
         belastingdienst.setEndDate(LocalDate.parse("2012-08-01"));
         belastingdienst.setCity("Apeldoorn");
         employers.add(belastingdienst);
      }

      EmployerList employerList = new EmployerList(employers);
      this.visitor.renderSummarizedOverviewEmployers(employerList);

      int line = 2;
      check("<<<", line++);
      line++;
      check("=== Beknopt overzicht werkgevers", line++);
      check("[cols=\"4,4,3\" options=\"header\", caption=\"\", frame=\"all\" ]", line++);
      check("|===", line++);
      check("^|Werkgever ^|Functie ^|Periode", line++);
      check("|Infor |Software Engineer ^|Sep 2012 - Jan 2014", line++);
      check("|Belastingdienst |Software Ontwikkelaar ^|Sep 2004 - Aug 2012", line++);

   }

   @Test
   public void testRenderSummarizedOverviewEmployersOne() {
      List<Employer> employers = new ArrayList<>();

      {
         Employer infor = new Employer();
         infor.setName("Infor");
         infor.setFunction("Software Engineer");
         infor.setStartDate(LocalDate.parse("2012-09-01"));
         infor.setEndDate(LocalDate.parse("2014-01-01"));
         infor.setCity("Almelo");
         employers.add(infor);
      }

      EmployerList employerList = new EmployerList(employers);
      this.visitor.renderSummarizedOverviewEmployers(employerList);

      int line = 2;
      check("<<<", line++);
      line++;
      check("=== Beknopt overzicht werkgevers", line++);
      check("[cols=\"4,4,3\" options=\"header\", caption=\"\", frame=\"all\" ]", line++);
      check("|===", line++);
      check("^|Werkgever ^|Functie ^|Periode", line++);
      check("|Infor |Software Engineer ^|Sep 2012 - Jan 2014", line++);

   }

   @Test
   public void testRenderHistoryFilterIfApplicableEmpty() {
      this.visitor.renderHistoryFilterIfApplicable();
      checkEmpty();
   }

   @Test
   public void testRenderHistoryFilterIfApplicableThree() {
      this.visitor.setHistory(3);
      this.visitor.renderHistoryFilterIfApplicable();
      check("filter < 3 jaren");
   }

   @Test
   public void testRenderReferencesWithNone() {
      AbstractWorkItem workItem = new Assignment();
      this.visitor.renderReferences(workItem);
      checkEmptyIgnoreWhitespace();
   }

   @Test
   public void testRenderReferencesWithOne() {
      final List<String> referencesFixture = Arrays.asList("http://www.carpago.nl");
      AbstractWorkItem workItem = new Assignment();
      workItem.setReferences(referencesFixture);
      this.visitor.renderReferences(workItem);
      checkContains("Referentie");
      checkDoesNotContain("Referenties");
      for (String reference : referencesFixture) {
         checkContains(reference);
      }
   }

   @Test
   public void testRenderReferencesWithThree() {
      final List<String> referencesFixture = Arrays.asList("http://www.carpago.nl", "http://www.infor.com", "http://www.belastingdienst.nl");
      AbstractWorkItem workItem = new Assignment();
      workItem.setReferences(referencesFixture);
      this.visitor.renderReferences(workItem);
      checkContains("Referenties");
      for (String reference : referencesFixture) {
         checkContains(reference);
      }
   }

   @Test
   public void testFilterWorkitemInShouldBeOut() {
      AbstractWorkItem assignment = new Assignment();
      assignment.setStartDate(LocalDate.parse("2016-01-01"));
      assignment.setEndDate(LocalDate.parse("2016-02-02"));
      this.visitor.setHistory(1);

      boolean result = this.visitor.filterWorkitemIn(assignment);
      Assert.assertFalse(result);
   }

   @Test
   public void testFilterWorkitemInShouldBeInWithoutFilter() {
      AbstractWorkItem assignment = new Assignment();
      assignment.setStartDate(LocalDate.parse("2014-01-01"));
      assignment.setEndDate(LocalDate.parse("2014-02-02"));
      this.visitor.setHistory(-1);

      boolean result = this.visitor.filterWorkitemIn(assignment);
      Assert.assertTrue(result);
   }

   @Test
   public void testFilterWorkitemInShouldBeIn1() {
      AbstractWorkItem assignment = new Assignment();
      assignment.setStartDate(LocalDate.parse("2016-01-01"));
      assignment.setEndDate(LocalDate.parse("2048-02-22"));
      this.visitor.setHistory(1);

      boolean result = this.visitor.filterWorkitemIn(assignment);
      Assert.assertTrue(result);
   }

   @Test
   public void testFilterWorkitemInShouldBeIn2() {
      AbstractWorkItem assignment = new Assignment();
      assignment.setStartDate(LocalDate.parse("2015-01-01"));
      assignment.setEndDate(LocalDate.parse("2048-02-22"));
      this.visitor.setHistory(2);

      boolean result = this.visitor.filterWorkitemIn(assignment);
      Assert.assertTrue(result);
   }

   @Test
   public void testGetDayStringFromTrainingWithStartAndEndateSet() {

      Training t = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      LocalDate endDate = LocalDate.parse(JAN30EN);
      t.setStartDate(startDate);
      t.setEndDate(endDate);

      String datesAsString = this.visitor.getDateStringFromTraining(t);

      Assert.assertEquals(JAN28TMJAN30, datesAsString);
   }

   @Test
   public void testSomeMoreDatesWithStartAndEndDateSet() {

      LocalDate now = LocalDate.now();
      Training t = new Training();
      t.setStartDate(now.minusDays(20));
      t.setEndDate(now.plusDays(10));

      String datesAsString = this.visitor.getDateStringFromTraining(t);

      final String expectedDateString = this.dateFixture(t.getStartDate())+" t/m "+this.dateFixture(t.getEndDate());

      Assert.assertEquals(expectedDateString, datesAsString);
   }

   @Test
   public void testSomeMoreDatesOne() {
      LocalDate now = LocalDate.now();
      Training t = new Training();
      LocalDate oneDayTraining = now.minusDays(3);
      t.setDates(Arrays.asList(oneDayTraining));

      final String expectedDateString = this.dateFixture(oneDayTraining);

      Assert.assertEquals(expectedDateString, this.visitor.getDateStringFromTraining(t));
   }



   @Test
   public void testSomeMoreDatesTwo() {
      LocalDate now = LocalDate.now();
      Training t = new Training();
      t.setDates(Arrays.asList(now.minusDays(20), now.minusDays(10), now.plusDays(3)));

      final String expectedDateString = this.dateFixture(now.minusDays(20))+" en "+
              this.dateFixture(now.minusDays(10));

      Assert.assertEquals(expectedDateString, this.visitor.getDateStringFromTraining(t));
   }

   @Test
   public void testSomeMoreDatesThree() {
      LocalDate now = LocalDate.now();
      Training t = new Training();
      t.setDates(Arrays.asList(now.minusDays(20), now.minusDays(10), now.minusDays(5), now.plusDays(3)));

      final String expectedDateString = this.dateFixture(now.minusDays(20))+", "+
              this.dateFixture(now.minusDays(10))+" en "+this.dateFixture(now.minusDays(5));

      Assert.assertEquals(expectedDateString, this.visitor.getDateStringFromTraining(t));
   }

   @Test
   public void testSomeMoreDatesFour() {
      LocalDate now = LocalDate.now();
      Training t = new Training();
      t.setDates(Arrays.asList(now.minusDays(20), now.minusDays(10), now.minusDays(5), now.minusDays(2), now.plusDays(3)));

      final String expectedDateString = this.dateFixture(now.minusDays(20))+" t/m "+
              this.dateFixture(now.minusDays(2))+" (4 dagen)";

      Assert.assertEquals(expectedDateString, this.visitor.getDateStringFromTraining(t));
   }

   @Test
   public void testRenderEveningTrainingWithDatesSet() {
      LocalDate now = LocalDate.now();
      Training t = new Training();
      t.setEveningTraining(true);
      t.setDates(Arrays.asList(now.minusDays(20), now.minusDays(10), now.minusDays(5), now.minusDays(2), now.plusDays(3)));

      final String expectedDateString = this.dateFixture(now.minusDays(20))+" t/m "+
              this.dateFixture(now.minusDays(2))+" (4 avonden)";

      Assert.assertEquals(expectedDateString, this.visitor.getDateStringFromTraining(t));
   }

   @Test
   public void testRenderEveningTrainingWithStartAndEnddateSet() {
      LocalDate now = LocalDate.now();
      Training t = new Training();
      t.setEveningTraining(true);

      t.setStartDate(now.minusDays(20));
      t.setEndDate(now.plusDays(3));
      t.setDays(4);


      final String expectedDateString = this.dateFixture(now.minusDays(20))+" t/m "+
              this.dateFixture(now.plusDays(3))+" (4 avonden)";

      Assert.assertEquals(expectedDateString, this.visitor.getDateStringFromTraining(t));
   }



   @Test
   public void testGetDayStringFromTrainingWithStartAndEndateSetOnlyForTwoConsecutiveDays() {

      Training t = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      LocalDate endDate = LocalDate.parse("2014-01-29");
      t.setStartDate(startDate);
      t.setEndDate(endDate);

      String datesAsString = this.visitor.getDateStringFromTraining(t);

      Assert.assertEquals(JAN28ENJAN29, datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithStartDateSetButNotEndDateSet() {

      Training t = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      t.setStartDate(startDate);

      String datesAsString = this.visitor.getDateStringFromTraining(t);

      Assert.assertEquals("28-01-2014 t/m " + this.visitor.formatDateShort(LocalDate.now()), datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithErrorDatesIsEmpty() {

      Training training = new Training();
      List<LocalDate> dates = new ArrayList<>();
      training.setDates(dates);

      try {
         // should throw exception
         this.visitor.getDateStringFromTraining(training);

         Assert.fail(FAILMESSAGE);
      }
      catch (IndexOutOfBoundsException e) {
         // ok
      }
   }

   @Test
   public void testGetDayStringFromTrainingWithErrorOnlyEnddateSet() {

      Training training = new Training();
      LocalDate endDate = LocalDate.parse(JAN28EN);
      training.setEndDate(endDate);

      try {
         // should throw exception
         this.visitor.getDateStringFromTraining(training);

         Assert.fail(FAILMESSAGE);
      }
      catch (NullPointerException e) {
         // ok
      }
   }

   @Test
   public void testGetDayStringFromTrainingWithOneDateSet() {

      Training training = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      List<LocalDate> dates = new ArrayList<>();
      dates.add(startDate);
      training.setDates(dates);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals(JAN28, datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithTwoDatesSet() {

      Training training = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      LocalDate endDate = LocalDate.parse(JAN30EN);
      List<LocalDate> dates = new ArrayList<>();
      dates.add(startDate);
      dates.add(endDate);
      training.setDates(dates);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals(JAN28ENJAN30, datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithThreeDatesSet() {

      Training training = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      LocalDate betweenDate = LocalDate.parse(JAN31EN);
      LocalDate endDate = LocalDate.parse(JAN30EN);
      List<LocalDate> dates = new ArrayList<>();
      dates.add(startDate);
      dates.add(betweenDate);
      dates.add(endDate);
      training.setDates(dates);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals("28-01-2014, 30-01-2014 en 31-01-2014", datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithFourDatesSet() {

      Training training = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      LocalDate betweenDate = LocalDate.parse(JAN31EN);
      LocalDate date = LocalDate.parse("2014-01-29");
      LocalDate endDate = LocalDate.parse(JAN30EN);
      List<LocalDate> dates = new ArrayList<>();
      dates.add(startDate);
      dates.add(date);
      dates.add(betweenDate);
      dates.add(endDate);
      training.setDates(dates);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals("28-01-2014 t/m 31-01-2014 (4 dagen)", datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithMoreThanFourDatesSet() {

      Training training = new Training();
      LocalDate startDate = LocalDate.parse(JAN28EN);
      LocalDate betweenDate = LocalDate.parse(JAN31EN);
      LocalDate date = LocalDate.parse("2014-01-29");
      LocalDate date2 = LocalDate.parse(JAN30EN);
      LocalDate endDate = LocalDate.parse("2014-08-09");
      List<LocalDate> dates = new ArrayList<>();
      dates.add(startDate);
      dates.add(date);
      dates.add(betweenDate);
      dates.add(date2);
      dates.add(endDate);
      training.setDates(dates);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals("28-01-2014 t/m 09-08-2014 (5 dagen)", datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithStartAndEnddateAndDaysSet() {

      Training training = new Training();
      LocalDate startDate = LocalDate.parse("2016-09-12");
      LocalDate endDate = LocalDate.parse("2016-10-19");
      training.setDays(16);
      training.setStartDate(startDate);
      training.setEndDate(endDate);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals("12-09-2016 t/m 19-10-2016 (16 dagen)", datesAsString);
   }

   @Test
   public void testGetDayStringFromTrainingWithTwoSeparateConsecutiveRange() {
      Training training = new Training();
      LocalDate startDate = LocalDate.parse("2016-07-06");
      LocalDate betweenDate = LocalDate.parse("2016-07-07");
      LocalDate date = LocalDate.parse("2016-07-08");
      LocalDate date2 = LocalDate.parse("2016-07-11");
      LocalDate endDate = LocalDate.parse("2016-07-12");
      List<LocalDate> dates = new ArrayList<>();
      dates.add(startDate);
      dates.add(date);
      dates.add(date2);
      dates.add(betweenDate);
      dates.add(endDate);
      training.setDates(dates);

      String datesAsString = this.visitor.getDateStringFromTraining(training);

      Assert.assertEquals("06-07-2016 t/m 12-07-2016 (5 dagen)", datesAsString);

   }

   @Test
   public void testVisitEducationList() {

      EducationContainer educationList = new EducationContainer();
      List<Education> schools = new ArrayList<>();

      {
         Education education = new Education();

         education.setDiploma(true);
         education.setStartDate(LocalDate.of(1981, 8, 12));
         education.setEndDate(LocalDate.of(1986, 9, 9));
         education.setDescription("Hoger Algemeen Voortgezet Onderwijs");
         education.setEducation("HAVO");
         education.setOrganization("Canisius");

         schools.add(education);

      }

      {
         Education education = new Education();

         education.setDiploma(true);
         education.setStartDate(LocalDate.of(1998, 9, 1));
         education.setEndDate(LocalDate.of(2003, 1, 28));
         education.setDescription("Hoger Informatica Onderwijs");
         education.setEducation("HIO");
         education.setOrganization("Saxion");

         schools.add(education);

      }

      educationList.setSchools(schools);
      educationList.setOther(new ArrayList<>());
      educationList.setCourses(new ArrayList<>());
      educationList.accept(this.visitor);

      checkContains("|HAVO |Hoger Algemeen Voortgezet Onderwijs |Canisius ^|Aug{nbsp}1981{nbsp}t/m{nbsp}Sep{nbsp}1986 ^|ja\n");
      checkLinesEmpty(1, 2, 11);
      check("== Opleiding", 3);
      check(".School opleidingen", 4);
      check("[cols=\"3,6,4,6,3\" caption=\"\" options=\"header\"]", 5);
      check("|===", 6);
      check("|Naam ^|Omschrijving |Organisatie ^|Periode ^|Diploma", 7);
      check("|HIO |Hoger Informatica Onderwijs |Saxion ^|Sep{nbsp}1998{nbsp}t/m{nbsp}Jan{nbsp}2003 ^|ja", 8);
      check("|HAVO |Hoger Algemeen Voortgezet Onderwijs |Canisius ^|Aug{nbsp}1981{nbsp}t/m{nbsp}Sep{nbsp}1986 ^|ja", 9);
      check("|===", 10);
      check("{empty}", 12);
   }
   
   @Test
   public void testVisitConsultancy() {

      Consultancy datavisual = new Consultancy();

      final String name = "DataVisual";
      datavisual.setName(name);
      final String city = "Enter";
      datavisual.setCity(city);
      datavisual.setStartDate(LocalDate.parse("2012-02-01"));
      datavisual.setEndDate(LocalDate.parse("2012-02-04"));
      final String description = "Het adviseren en begeleiden tijdens het opzetten van een ontwikkel-straat voor DataVisual haar development-team";
      datavisual.setDescription(description);
      final String experience = "Effectief werken met SCM, Continuous Delivery en -Deployment";
      datavisual.setObjectives(Arrays.asList(experience));
      final String results = "Git, Jenkins en Spring / Spring Boot applicaties worden automatisch gedeployed naar de desbetreffende omgeving";
      datavisual.setResults(Arrays.asList(results));
      final String reference = "http://www.datavisual.nl";
      datavisual.setReferences(Arrays.asList(reference));
      datavisual.setExperience(Arrays.asList(Experience.create(Topic.Continuous_Delivery), 
                        Experience.create(Topic.Git), 
                        Experience.create(Topic.Git_Flow),
                        Experience.create(Topic.Java),
                        Experience.create(Topic.Jenkins)));
      
      datavisual.accept(this.visitor);

      checkContains("[horizontal]");
      checkContains("Feb{nbsp}2012{nbsp}(3{nbsp}dagen):: ");
      checkContains("._DataVisual te Enter_");
      checkContains("Omschrijving:: ");
      checkContains(description);
      checkContains("Doelstelling:: ");
      checkContains(experience);
      checkContains("Resultaten:: ");   
      checkContains(results);   
      checkContains("Referentie:: ");
      checkContains("* http://www.datavisual.nl[http://www.datavisual.nl, window=\"_blank\"]");
      checkContains("Gebruikte technieken, systemen en methodieken:: ");
      checkContains("Continuous{nbsp}Delivery, Git, Git{nbsp}Flow, Java, Jenkins");
   }

   @Test
   public void testVisitEducation() {

      Education education = new Education();

      education.setDiploma(true);
      education.setStartDate(LocalDate.of(1985, 8, 12));
      education.setEndDate(LocalDate.of(1995, 9, 9));
      education.setDescription("Hoger Algemeen Voortgezet Onderwijs");
      education.setEducation("HAVO");
      education.setOrganization("Canisius");

      education.accept(this.visitor);

      check("|HAVO |Hoger Algemeen Voortgezet Onderwijs |Canisius ^|Aug{nbsp}1985{nbsp}t/m{nbsp}Sep{nbsp}1995 ^|ja\n");
   }

   @Test
   public void testVisitCourse() {

      Education education = new Course();

      education.setDiploma(true);
      education.setStartDate(LocalDate.of(1985, 8, 12));
      education.setDescription("Sun Certified Java Programmer");
      education.setEducation("SCJP");
      education.setOrganization("Sun");

      education.accept(this.visitor);

      check("|Sun Certified Java Programmer |Sun ^|Aug{nbsp}1985 ^|ja\n");
   }

   @Test
   public void testCalculateNumbersOfStarsForWeblogicLow() {
      LocalDate weblogicDate = LocalDate.now().minusYears(3).plusDays(10);
      KnowledgeBean knowledgeBean = new KnowledgeBean(3, 3, weblogicDate);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean);

      Assert.assertEquals(2, numbers);

   }

   @Test
   public void testCalculateNumbersOfStarsForWeblogicLowHits() {
      LocalDate weblogicDate = LocalDate.now().minusYears(3).plusDays(10); // almost 3 years ago
      KnowledgeBean knowledgeBean = new KnowledgeBean(3, 2, weblogicDate);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean);

      Assert.assertEquals(2, numbers);

   }

   @Test
   public void testCalculateNumbersOfStarsJavaHigh() {
      LocalDate weblogicDate = LocalDate.now();
      KnowledgeBean knowledgeBean = new KnowledgeBean(3000, 50, weblogicDate);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean);

      Assert.assertEquals(5, numbers);

   }

   @Test
   public void testCalculateNumbersOfStarsGroovyShortAgo() {
      LocalDate weblogicDate = LocalDate.now();
      KnowledgeBean knowledgeBean = new KnowledgeBean(600, 5, weblogicDate);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean);

      Assert.assertEquals(5, numbers);

   }

   @Test
   public void testCalculateNumbersOfStarsGroovyLongAgo() {
      LocalDate weblogicDate = LocalDate.now().minusYears(4);
      KnowledgeBean knowledgeBean = new KnowledgeBean(600, 5, weblogicDate);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean);

      Assert.assertEquals(0, numbers);

   }
   
   @Test
   public void testCalculateNumbersOfStarsAngular2Last201612OnAugustus2017ShouldBeThreeStarsWithMoreThan200Days() {
      LocalDate angular2Date = LocalDate.of(2016, 12, 12);
      KnowledgeBean knowledgeBean = new KnowledgeBean(250, 5, angular2Date);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean, LocalDate.of(2017, 8, 17));

      Assert.assertEquals(3, numbers);

   }
   
   @Test
   public void testCalculateNumbersOfStarsAngular2Last201612OnAugustus2017ShouldBeTwoStarsWithBelow200Days() {
      LocalDate angular2Date = LocalDate.of(2016, 12, 12);
      KnowledgeBean knowledgeBean = new KnowledgeBean(150, 5, angular2Date);
      int numbers = this.visitor.calculateNumberOfStarsForKnowledge(knowledgeBean, LocalDate.of(2017, 8, 17));

      Assert.assertEquals(2, numbers);

   }

   private String dateFixture(LocalDate date) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format("%02d", date.getDayOfMonth()));
      stringBuilder.append("-");
      stringBuilder.append(String.format("%02d", date.getMonthValue()));
      stringBuilder.append("-");
      stringBuilder.append(String.format("%04d", date.getYear()));

      return stringBuilder.toString();
   }
}