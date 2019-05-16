package nl.raymondloman.resume.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.raymondloman.resume.Constants;
import nl.raymondloman.resume.domain.AbstractWorkItem;
import nl.raymondloman.resume.domain.Experience;
import nl.raymondloman.resume.domain.Introduction;
import nl.raymondloman.resume.domain.KnowledgeMatrix;
import nl.raymondloman.resume.domain.ManuallyOverriddenKnowledgeBean;
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
import nl.raymondloman.resume.domain.enums.Topic;
import nl.raymondloman.resume.domain.enums.TrainingType;
import nl.raymondloman.resume.domain.other.AdditionalInformation;
import nl.raymondloman.resume.domain.other.Header;
import nl.raymondloman.resume.domain.other.KnowledgeBean;
import nl.raymondloman.resume.domain.personal.Personalia;
import nl.raymondloman.resume.domain.personal.Recommendation;
import nl.raymondloman.resume.domain.personal.RecommendationList;
import nl.raymondloman.resume.domain.training.Training;
import nl.raymondloman.resume.filter.ChronologischAppendixFilter;
import nl.raymondloman.resume.filter.EducationFilter;
import nl.raymondloman.resume.filter.SectionFilter;
import nl.raymondloman.resume.filter.WorkItemSectionFilter;
import nl.raymondloman.resume.i18n.DutchLabels;
import nl.raymondloman.resume.i18n.EnglishLabels;
import nl.raymondloman.resume.i18n.Labels;
import nl.raymondloman.resume.services.DateService;

public class AsciidocGeneratingVisitor implements Visitor {

   private static final Logger LOGGER = LoggerFactory.getLogger(AsciidocGeneratingVisitor.class);

   private static final byte MINIMAL_STARS_TO_BE_IN_KNOWLEDGEMATRIX = 3;

   private static final String EMPTY_ASCIIDOC = "{empty}";
   private static final String CARPAGO_SOFTWARE_TITLE = ".Acme Software";
   private static final String CARPAGO_CONSULTANCY_TITLE = ".Acme Consultancy";
   private static final String TABLE_BEGIN_END_ASCIIDOC = "|===";
   private static final String CARPAGO_TRAINING_TITLE = ".Acme Training";
   private static final String TABLE_HEADER_DEFAULT = "[cols=\"4,4,3\" options=\"header\", caption=\"\", frame=\"all\" ]";

   private static final String TRAININGS_TABLE_HEADER = "[cols=\"8,3\" options=\"header\", \"footer\", caption=\"\", frame=\"all\" ]";
   private static final String TRAININGS_TABLE_WITH_CUSTOMERNAME_HEADER = "[cols=\"4,4,3\" options=\"header\", \"footer\", caption=\"\", frame=\"all\" ]";

   private static final String BREAKTABLE_ASCIIDOC = "//^";
   private static final String TOPLEVEL_ASCIIDOC = "= ";
   private static final String HARDPAGEBREAK_ASCIIDOC = "<<<";
   private static final String HORIZONTAL_RULER_ASCIIDOC = "'''";
   private static final String TAB_ASCIIDOCTOR = "--";
   private static final String OPLEIDINGTABLEHEADER = "[cols=\"3,6,4,6,3\" caption=\"\" options=\"header\"]";
   private static final String OPLEIDINGTABLEHEADERCOURSES = "[cols=\"9,4,6,3\" caption=\"\" options=\"header\"]";

   private DateService dateService = new DateService();

   // for tracking / verification
   private Map<LocalDate, Training> trainingsPerDate = new HashMap<>();
   private List<String> warnings = new ArrayList<>();

   // for printing the missing topics in the end
   private List<String> warningsRegardingMissingTopics = new ArrayList<>();

   // instance variables set by the builder
   private Language language;
   private Labels labels;
   private int history;
   private int minimalTrainingOccurrencesToRender;
   private List<TrainingType> trainingtypeWhitelist;
   private List<TrainingType> trainingtypeBlacklist;
   private String outputFilename;
   private String resumeOutputFileNamePdf;

   private PrintStream currentOutputStream = System.out;

   private KnowledgeMatrix knowledgeMatrix = new KnowledgeMatrix();

   // settings for filtering sections
   private SectionFilter sectionFilter;

   // to hold if the introduction is already rendered or not
   private boolean renderedIntroductionAcme;

   private Map<Topic, Integer> manuallyOverriddenStars = new HashMap<>();
   private Map<Topic, LocalDate> manuallyOverriddenLastUsed = new HashMap<>();

   private ChronologischAppendixFilter chronologischAppendixFilter;

   private boolean enableCustomerNamesForTrainings;

   private AsciidocGeneratingVisitor(AsciidocGeneratingVisitorBuilder builder) {

      this.language = builder.language;
      this.history = builder.history;
      this.labels = builder.labels;
      this.sectionFilter = builder.sectionFilter;
      this.minimalTrainingOccurrencesToRender = builder.minimalTrainingOccurrences;
      this.trainingtypeWhitelist = builder.trainingtypeWhitelist;
      this.trainingtypeBlacklist = builder.trainingtypeBlacklist;
      this.outputFilename = builder.outputFilename;
      this.resumeOutputFileNamePdf = builder.resumeOutputFileNamePdf;
      this.chronologischAppendixFilter = builder.chronologischAppendixFilter;
      this.enableCustomerNamesForTrainings = builder.enableCustomerNamesForTrainings;
   }

   @Override
   public void visit(Resume resume) {

      this.deInternationaliseResumeVisit(resume);

      try {
         this.redirectOutputStream();
      }
      catch (FileNotFoundException fnfe) {
         LOGGER.error("Unable to redirect outputstream with message [{}]", fnfe.getMessage());
         return;
      }

      resume.getHeader().accept(this);
      System.out.println();

      System.out.print(TOPLEVEL_ASCIIDOC);
      System.out.println(this.labels.resume());
      System.out.printf("%s <%s>%n", resume.getPersonalia().getFirstNameLastName(), resume.getPersonalia().getEmail());
      System.out.printf("{revnumber}, %s %s%n", this.labels.lastUpdated(), this.formatDateShort(LocalDate.now()));

      System.out.println();

      System.out.println("[.text-right]");
      if (this.language.equals(Language.ENGLISH)) {
         System.out.printf("image:nl.png[\"NL\", title=\"Nederlands\", width=40, link=\"cv.html\"]");
      }
      else {
         System.out.printf("image:uk.png[\"EN\", title=\"English\", width=40, link=\"resume.html\"]");
      }

      System.out.println();

      System.out.println("[.text-right]");
      System.out.printf("[small]#link:%s[PDF %s, window=\"_blank\"]#%n", this.resumeOutputFileNamePdf, this.labels.version().toLowerCase());

      System.out.println();
      resume.getPersonalia().accept(this);
      System.out.println();

      if (resume.getPersonalIntroduction() != null) {
         System.out.printf("== %s%n", this.labels.personalIntroduction());
         resume.getPersonalIntroduction().accept(this);
         System.out.println();
      }

      if (this.sectionFilter.isRecommendations()) {
         resume.getRecommendations().accept(this);
      }

      this.generateKnowledgeMatrix(resume);
      this.knowledgeMatrix.accept(this);

      if (this.sectionFilter.getOverview().isOn() || this.log(this.sectionFilter.getOverview())) {
         System.out.printf("== %s%n", this.labels.summarizedOverview());
         WorkItemSectionFilter overviewFilter = this.sectionFilter.getOverview();

         if (overviewFilter.isAssignments()) {
            this.renderSummarizedOverviewAssignments(resume.getAssignments());
            this.renderHorizontalRuler();
         }
         if (overviewFilter.isTrainings()) {
            this.renderSummarizedOverviewTrainings(resume.getTrainings());
            this.renderHorizontalRuler();
         }

         if (overviewFilter.isConsultancy()) {
            this.renderSummarizedOverviewConsultancies(resume.getConsultancies());
            this.renderHorizontalRuler();
         }
         if (overviewFilter.isEmployers()) {
            this.renderSummarizedOverviewEmployers(resume.getEmployers());
         }
      }

      if (this.sectionFilter.getExtensive().isOn() || this.log(this.sectionFilter.getExtensive())) {
         WorkItemSectionFilter extensiveFilter = this.sectionFilter.getExtensive();
         System.out.printf("== %s%n", this.labels.extensiveList());
         if (this.isHistoryFilterOn()) {
            System.out.printf("WARNING: " + this.labels.filterMessageFormatString(), this.history);
            System.out.println();
         }

         if (extensiveFilter.isSelfEmployedOn() || this.log(extensiveFilter)) {
            System.out.printf("=== %s Acme%n", this.labels.selfEmployed());
            System.out.println();

            if (extensiveFilter.isAssignments() || this.log(extensiveFilter)) {
               System.out.println("[[software]]");
               resume.getAssignments().accept(this);
            }
            if (extensiveFilter.isTrainings() || this.log(extensiveFilter)) {
               resume.getTrainings().accept(this);
            }

            if (extensiveFilter.isConsultancy() || this.log(extensiveFilter)) {
               this.renderPageBreakHard();
               System.out.println("[[consultancy]]");
               resume.getConsultancies().accept(this);
            }
            if (extensiveFilter.isPersonalProjects() || this.log(extensiveFilter)) {
               this.renderPageBreakHard();
               resume.getPersonalProjects().accept(this);
            }
         }

         if (extensiveFilter.isEmployers() || this.log(extensiveFilter)) {
            resume.getEmployers().accept(this);
         }
      }

      if (this.sectionFilter.getEducation().isOn() || this.log(this.sectionFilter)) {
         resume.getEducationContainer().accept(this);
      }

      if (this.sectionFilter.getAdditionalInfo().isOn() || this.log(this.sectionFilter)) {
         resume.getAdditionalInformation().accept(this);
      }

      System.out.println();

      if (this.chronologischAppendixFilter != null) {
         System.out.println("== Appendix");

         // print warnings evt.
         if (!this.warnings.isEmpty() || !this.warningsRegardingMissingTopics.isEmpty()) {
            System.out.println();
            System.out.println("=== Warnings");
            if (!this.warningsRegardingMissingTopics.isEmpty()) {
               System.out.println("==== Topics with less stars");
               warningsRegardingMissingTopics.stream().forEach(warningString -> {
                  System.out.println("* " + warningString);
               });

            }
            System.out.println();
            if (!this.warnings.isEmpty()) {
               System.out.println("=== General warnings");
               warnings.stream().forEach(warningString -> {
                  System.out.println("* " + warningString);
               });
            }
         }

         LocalDate startDate = LocalDate.of(this.chronologischAppendixFilter.getStartYear(), this.chronologischAppendixFilter.getStartMonth(), 1);
         LocalDate endDate = LocalDate.of(this.chronologischAppendixFilter.getEndYear(), this.chronologischAppendixFilter.getEndMonth(), 1);
         YearMonthAsciidocGeneratingVisitor visitorForRenderingChronView = new YearMonthAsciidocGeneratingVisitor(startDate, endDate);

         resume.accept(visitorForRenderingChronView);
      }
      // the end
      this.resetOutputStream();

      LOGGER.info("[{}] Resume generated", this.language);
   }

   @Override
   public void visit(Introduction introduction) {
      this.renderIntroductionRecursive(introduction, 0);
   }

   @Override
   public void visit(PersonalIntroduction personalIntroduction) {
      this.renderIntroductionRecursive(personalIntroduction, 0);
   }

   private void renderIntroductionRecursive(PersonalIntroduction introduction, int depth) {
      if (introduction.getTitle() != null) {
         if (depth == 0) {
            System.out.println("." + introduction.getTitle());
         }
         else {
            depth++;
            System.out.println();
            System.out.println("* " + introduction.getTitle());
            depth++;
         }

      }

      if (introduction.getLines() != null) {
         for (String line : introduction.getLines()) {
            System.out.println(line + " + ");
         }
      }
      else {
         LOGGER.warn("Introduction [{}] does not contain any lines!", introduction);
      }

      if (introduction.getSub() != null) {
         for (Introduction intro : introduction.getSub()) {
            this.renderIntroductionRecursive(intro, depth + 1);
         }
      }
   }

   private void renderIntroductionRecursive(Introduction introduction, int depth) {
      if (introduction.getTitle() != null) {
         if (depth == 0) {
            System.out.println("." + introduction.getTitle());
         }
         else {
            depth++;
            for (int i = 0; i < depth; i++) {
               System.out.print("*");
            }
            System.out.println(" " + introduction.getTitle());
            // depth++;
         }

      }

      if (introduction.getLines() != null) {
         for (String line : introduction.getLines()) {
            for (int i = 0; i < depth; i++) {
               System.out.print("*");
            }
            System.out.println("* " + line);
         }
      }
      else {
         LOGGER.warn("Introduction [{}] does not contain any lines!", introduction);
      }

      if (introduction.getSub() != null) {
         for (Introduction intro : introduction.getSub()) {
            this.renderIntroductionRecursive(intro, depth + 1);
         }

      }
   }

   @Override
   public void visit(Trainings trainings) {
      this.renderPageBreakHard();
      System.out.println("[[trainingen]]");
      System.out.printf("==== %s%n", this.labels.Trainings());
      this.renderHistoryFilterIfApplicable();
      System.out.println();

      List<TrainingType> trainingsTypeSorted = Arrays.asList(TrainingType.values());
      Collections.sort(trainingsTypeSorted, (a, b) -> a.toString().compareTo(b.toString()));
      for (TrainingType trainingType : trainingsTypeSorted) {
         System.out.println();
         Optional<List<Training>> trainingenOptional = trainings.get(trainingType);
         if (trainingenOptional.isPresent()) {
            List<Training> trainingen = trainingenOptional.get().stream()
                              .filter(t -> this.filterWorkitemIn(t))
                              .collect(Collectors.toList());
            if (this.isTrainingtypeToBeRendered(trainingType, trainingen.size())) {
               Collections.sort(trainingen);
               System.out.println("[horizontal options=\"no-wrap\" ]");
               System.out.printf("%s{nbsp}(%d):: %n", trainingType.toString().replaceAll("_", " "), trainingen.size());
               System.out.println("+");
               this.renderTab();
               System.out.println();

               for (Training training : trainingen) {
                  training.accept(this);
               }
               System.out.printf("%s:: ", this.labels.topicsCovered());
               System.out.printf("%s%n", String.join(", ", trainingType.getTopics().stream()
                                 .map(e -> e.toString().replace(" ", "{nbsp}"))
                                 .distinct().collect(Collectors.toList())));
               System.out.println();
               this.renderTab();
               System.out.println();
               System.out.println(HORIZONTAL_RULER_ASCIIDOC);
               System.out.println();
            }
         }
         else {
            if(!trainingType.isEmbeddedOnly()) {
               LOGGER.warn("No trainings found for trainingtype [{}]", trainingType);
            }
         }
      }
   }

   @Override
   public void visit(Training training) {
      // first validate some
      {

         Training otherTraining = this.trainingsPerDate.get(training.getStartDate());
         if (otherTraining == null) {
            this.trainingsPerDate.put(training.getStartDate(), training);
         }
         else {
            if (this.language == Language.NEDERLANDS && training.getStartDate().equals(otherTraining.getStartDate())) {
               String warningString = String.format("Training %s (%s) has same startDate as Training %s (%s)",
                                 training.getName(), training.getStartDate(), otherTraining.getName(), otherTraining.getStartDate());
               this.warnings.add(warningString);
               LOGGER.warn(warningString);
            }
         }
      }

      List<String> customers = training.getCustomers();
      String customer = null;
      String via = Constants.EMPTYSTRING;

      if (customers.size() == 1) { // only final customer so simple
         customer = customers.get(0);
      }
      else {
         if (customers.size() == 2) { // through intermediate, so less soimple :-)
            customer = customers.get(1);
            via += this.labels.through() + " " + customers.get(0);
         }
         else {
            customer = String.join(", ", training.getCustomers()); // no clue, just print
         }
      }

      if (this.enableCustomerNamesForTrainings) {
         System.out.printf("%s: %s %s %s %s %s %s %s%n", this.getDateStringFromTraining(training),
                           training.getTrainees(), this.labels.people(),
                           this.labels.of(), customer, this.labels.at(),
                           training.getCity(), via);
      }
      else {
         System.out.printf("%s: %s %s %s %s%n",
                           this.getDateStringFromTraining(training),
                           training.getTrainees(),
                           this.labels.people(),
                           this.labels.at(),
                           training.getCity());

      }

      System.out.println();
      if (training.getResults() != null) {
         System.out.printf("{nbsp}{nbsp}{nbsp}%s:: %n", this.labels.results());
         for (String result : training.getResults()) {
            System.out.println("* " + result);
         }
      }
      System.out.println();
      this.renderReferences(training);
      System.out.println();
      System.out.println(BREAKTABLE_ASCIIDOC);
      System.out.println();

      System.out.println();

   }

   @Override
   public void visit(Header header) {
      System.out.println(":pagenums: true");
      System.out.printf(":toc-title: %s%n", this.labels.toc());
      System.out.println(":toclevels: 3");
      System.out.println(":status:");
      System.out.println(":revnumber: {docVersion}");
      System.out.printf(":version-label: %s%n", this.labels.version());
   }

   @Override
   public void visit(Personalia personalia) {
      
      long age = ChronoUnit.YEARS.between(personalia.getDateOfBirth(), LocalDate.now());

      System.out.printf("== %s%n", this.labels.personalia());
      System.out.printf(":age: %d%n", age);
      System.out.println();
      System.out.println("[cols=\"2,5\"]");
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.printf("|%s|%s%n", this.labels.name(), personalia.getFirstNameLastName());
      System.out.printf("|%s|%d%n", this.labels.age(), age);
      System.out.printf("|%s|%s%n", this.labels.dateOfBirth(), this.formatDateShort(personalia.getDateOfBirth()));
      System.out.printf("|%s|%s%n", this.labels.address(), personalia.getAddressHouseNumber());
      System.out.printf("|%s|%s%n", this.labels.zipCode(), personalia.getZipCode());
      System.out.printf("|%s|%s%n", this.labels.city(), personalia.getCity());
      System.out.printf("|%s|%s%n", this.labels.country(), personalia.getCountry().getDisplayCountry());
      System.out.printf("|%s|%s%n", this.labels.telephone(), personalia.getTelephone());
      System.out.printf("|%s|%s%n", this.labels.mobile(), personalia.getMobile());
      System.out.printf("|%s|%s%n", this.labels.email(), personalia.getEmail());
      System.out.printf("|%s|%s%n", this.labels.url(), personalia.getUrl());
      String maritalStatusString = null;
      switch (personalia.getMaritalStatus()) {
         case married:
            maritalStatusString = this.labels.married();
            break;
         case single:
            maritalStatusString = this.labels.single();
            break;
         case relationship:
            maritalStatusString = this.labels.relationship();
            break;
         default:
            break;
      }
      System.out.printf("|%s|%s%n", this.labels.maritalStatus(), maritalStatusString);
      System.out.printf("|%s|%s%n", this.labels.children(), personalia.getChildren());

      // same for Dutch and English
      System.out.printf("|%s|%s%n", this.labels.drivingLicense(), personalia.getDrivingLicense().name());
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
   }

   @Override
   public void visit(Employer employer) {
      if (this.filterWorkitemIn(employer)) {
         System.out.println();
         System.out.println("[horizontal]");
         System.out.printf("%s{nbsp}%s{nbsp}%s{nbsp}%s{nbsp}%s:: %n",
                           this.getMonthDisplayName(employer.getStartDate().getMonth()),
                           employer.getStartDate().getYear(),
                           this.labels.upToAndIncluding(),
                           LocalDate.now().equals(employer.getEndDate()) ? this.labels.currently()
                                             : this.getMonthDisplayName(
                                                               employer.getEndDate().getMonth()),
                           LocalDate.now().equals(employer.getEndDate()) ? Constants.EMPTYSTRING
                                             : employer.getEndDate().getYear());
         System.out.println("+");
         this.renderTab();
         System.out.println();
         System.out.printf("_%s, %s_%n", employer.getName(), employer.getCity());
         System.out.println();
         System.out.println("." + employer.getFunction().trim());
         System.out.println(EMPTY_ASCIIDOC);
         System.out.println();
         System.out.printf("%s:: %n", this.labels.responsibilities());
         for (String verantwoordelijkheid : employer.getResponsibilities()) {
            System.out.println("* " + verantwoordelijkheid);
         }
         if (employer.getResults() != null && !employer.getResults().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.results());
            for (String result : employer.getResults()) {
               System.out.println("* " + result);
            }
         }
         this.renderReferences(employer);

         System.out.println();
         System.out.println(BREAKTABLE_ASCIIDOC);
         System.out.println();
         System.out.printf("%s:: %n", this.labels.usedTechniquesSystemsMethodologies());

         String experienceAsString = String.join(", ",
                           employer.getExperience().stream().map(e -> e.getTopic().toString().replaceAll(" ", "{nbsp}"))
                                             .distinct()
                                             .sorted().collect(Collectors.toList()));
         System.out.println(experienceAsString);
         System.out.println();
         System.out.println();
         this.renderTab();
         System.out.println();
         this.renderHorizontalRuler();
      }
   }

   @Override
   public void visit(Assignment assignment) {
      if (this.filterWorkitemIn(assignment)) {

         System.out.println();
         System.out.println("[horizontal]");
         System.out.printf("%s{nbsp}%s{nbsp}%s{nbsp}%s{nbsp}%s:: %n",
                           this.getMonthDisplayName(assignment.getStartDate().getMonth()),
                           assignment.getStartDate().getYear(),
                           this.labels.upToAndIncluding(),
                           LocalDate.now().equals(assignment.getEndDate()) ? this.labels.currently()
                                             : this.getMonthDisplayName(
                                                               assignment.getEndDate().getMonth()),
                           LocalDate.now().equals(assignment.getEndDate()) ? Constants.EMPTYSTRING
                                             : assignment.getEndDate().getYear());
         System.out.println("+");
         this.renderTab();
         System.out.println();
         System.out.printf("_%s, %s_%n", assignment.getName(), assignment.getCity());
         System.out.println();
         System.out.println(BREAKTABLE_ASCIIDOC);
         System.out.println();
         System.out.println("." + assignment.getFunction().trim());
         System.out.printf("%s:: %n", this.labels.assignmentDescription());
         System.out.println(assignment.getDescription());
         if (assignment.getObjectives() != null && !assignment.getObjectives().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.objective());
            for (String objective : assignment.getObjectives()) {
               System.out.println("* " + objective);
            }
         }

         if (assignment.getResults() != null && !assignment.getResults().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.results());
            for (String resultaat : assignment.getResults()) {
               System.out.println("* " + resultaat);
            }
         }
         this.renderReferences(assignment);
         System.out.println();
         System.out.println(BREAKTABLE_ASCIIDOC);

         System.out.printf("%s:: %n", this.labels.usedTechniquesSystemsMethodologies());

         String experienceAsString = String.join(", ",
                           assignment.getExperience().stream().map(e -> e.getTopic().toString().replaceAll(" ", "{nbsp}"))
                                             .distinct()
                                             .sorted().collect(Collectors.toList()));
         System.out.println(experienceAsString);
         System.out.println();
         System.out.println();
         this.renderTab();
         System.out.println();
         System.out.println(HORIZONTAL_RULER_ASCIIDOC);
         System.out.println();
      }

   }

   @Override
   public void visit(KnowledgeMatrix knowledgeMatrix) {

      int numberOfStars;
      LocalDate lastUsed = null;

      System.out.printf("== %s%n", this.labels.knowledgeMatrix());
      System.out.printf(".%s + %n", this.labels.legend());
      System.out.printf("{empty}*{empty} = %s, {empty}*{empty}*{empty} = %s,  {empty}*{empty}*{empty}*{empty} = %s%n", this.labels.good(), this.labels.veryGood(), this.labels.excellent());

      System.out.println("[cols=\"4,1,1\" options=\"header\", frame=\"all\"]");
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.printf("^|%s \\| Framework \\| Tool |%s ^|%s%n", this.labels.language(), this.labels.knowledge(), this.labels.lastUsed());
      for (Entry<Topic, KnowledgeBean> knowledgeBean : knowledgeMatrix) {

         if (this.manuallyOverriddenStars.containsKey(knowledgeBean.getKey())) {
            numberOfStars = this.manuallyOverriddenStars.get(knowledgeBean.getKey());
            LOGGER.info("Manually overriding stars [{}] for topic [{}]", numberOfStars, knowledgeBean.getKey());
         }
         else {
            numberOfStars = this.calculateNumberOfStarsForKnowledge(knowledgeBean.getValue());
         }

         if (this.manuallyOverriddenLastUsed.containsKey(knowledgeBean.getKey())) {
            lastUsed = this.manuallyOverriddenLastUsed.get(knowledgeBean.getKey());
            LOGGER.info("Manually overriding lastUsed [{}] for topic [{}]", lastUsed, knowledgeBean.getKey());
         }
         else {
            lastUsed = knowledgeBean.getValue().getLastUsed();
         }

         LOGGER.debug("Got [{}] numbers of starts for topic [{}]", numberOfStars, knowledgeBean.getKey());

         // validate the stars with the expected stars
         if(numberOfStars != knowledgeBean.getKey().getExpectedStars()) {
            LOGGER.error("Getting different numbers of stars for topic: [{}], expected:{}, actual:{}", knowledgeBean.getKey(), knowledgeBean.getKey().getExpectedStars(), numberOfStars);
         }

         if (numberOfStars >= MINIMAL_STARS_TO_BE_IN_KNOWLEDGEMATRIX) {
            // amend numberOfStarts to one, two and three stars since it seems clearer. Might improve this in another develop feature. this is a hotfix for now.
            numberOfStars = convertNumberOfStartsFromThreeFourFiveToOneTwoThree(numberOfStars);
            StringBuilder starBuilder = new StringBuilder();

            for (int i = 0; i < numberOfStars; i++) {
               starBuilder.append("{empty}*");
            }
            System.out.printf("|%s |%s ^|%d - %02d%n", knowledgeBean.getKey().toString().replaceAll("_", " "),
                              starBuilder.toString(),
                              lastUsed.getYear(), lastUsed.getMonth().getValue());
         }
         else {
            if (this.language == Language.NEDERLANDS) { // skip the other since that is so low that it is ok after three month of testing it
               String warning = String.format("%-35.35s %5.5s", knowledgeBean.getKey().toString().replaceAll("\\{plus\\}", "+"), Integer.valueOf(numberOfStars).toString());
               this.warningsRegardingMissingTopics.add(warning);

            }
         }
      }
      if (!this.warningsRegardingMissingTopics.isEmpty()) {

         StringBuilder warningHeaderBuilder = new StringBuilder();

         warningHeaderBuilder.append(String.format("Missing %d topics which are mentioned below and their stars", this.warningsRegardingMissingTopics.size()));

         warningHeaderBuilder.append(String.format("\n\t%-35.35s %-5.5s", "", ""));
         warningHeaderBuilder.append(String.format("\n\t%-35.35s %-5.5s", "Topic", "Stars"));
         warningHeaderBuilder.append(String.format("\n\t%-35.35s %-5.5s", "=====", "====="));

         warningHeaderBuilder.append("\n\t");

         warningHeaderBuilder.append(String.join("\n\t", this.warningsRegardingMissingTopics));

         warningHeaderBuilder.append("\n\n");

         LOGGER.warn(warningHeaderBuilder.toString());
      }
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
   }

   private int convertNumberOfStartsFromThreeFourFiveToOneTwoThree(int numberOfStars) {

      switch (numberOfStars) {
         case 3:
            return 1;
         case 4:
            return 2;
         case 5:
            return 3;
         default:
            throw new IllegalArgumentException();
      }
   }

   @Override
   public void visit(EducationContainer educationList) {
      System.out.println();
      System.out.println();
      System.out.printf("== %s%n", this.labels.educations());
      EducationFilter educationFilter = this.sectionFilter.getEducation();

      if (educationFilter.isSchools()) {
         System.out.printf(".%s%n", this.labels.schoolEducation());
         System.out.println(OPLEIDINGTABLEHEADER);
         System.out.println(TABLE_BEGIN_END_ASCIIDOC);
         System.out.printf("|%s ^|%s |%s ^|%s ^|%s%n", this.labels.name(), this.labels.description(), this.labels.organisation(), this.labels.period(), this.labels.diploma());
         educationList.getSchools().accept(this);
         System.out.println(TABLE_BEGIN_END_ASCIIDOC);
         System.out.println();
         System.out.println(EMPTY_ASCIIDOC);
         System.out.println();
      }

      if (educationFilter.isOther()) {
         EducationList otherEducationList = new EducationList(educationList.getOther().stream()
                           .filter(o -> this.filterEducation(o))
                           .collect(Collectors.toList()));
         if (!otherEducationList.isEmpty()) {
            System.out.printf(".%s ", this.labels.otherEducation());
            if (this.isHistoryFilterOn()) {
               System.out.print("(");
               this.renderHistoryFilterIfApplicable();
               System.out.print(")");
            }
            System.out.println();
            System.out.println(OPLEIDINGTABLEHEADER);
            System.out.println(TABLE_BEGIN_END_ASCIIDOC);
            System.out.printf("|%s ^|%s |%s ^|%s ^|%s%n", this.labels.name(), this.labels.description(), this.labels.organisation(), this.labels.period(), this.labels.diploma());
            otherEducationList.accept(this);
            System.out.println(TABLE_BEGIN_END_ASCIIDOC);
            System.out.println();
            this.renderPageBreakHard();
         }
      }

      if (educationFilter.isCourses()) {
         List<Course> courses = educationList.getCourses().stream().filter(c -> this.filterEducation(c)).collect(Collectors.toList());
         if (!courses.isEmpty()) {
            System.out.printf(".%s ", this.labels.courses());
            if (this.isHistoryFilterOn()) {
               System.out.print("(");
               this.renderHistoryFilterIfApplicable();
               System.out.print(")");
            }
            System.out.println();
            System.out.println(OPLEIDINGTABLEHEADERCOURSES);
            System.out.println(TABLE_BEGIN_END_ASCIIDOC);
            System.out.printf("^|%s |%s ^|%s ^|%s%n", this.labels.description(), this.labels.organisation(), this.labels.period(), this.labels.certificate());
            courses.forEach(course -> {
               course.accept(this);
            });
            System.out.println(TABLE_BEGIN_END_ASCIIDOC);
            System.out.println();
            System.out.println(EMPTY_ASCIIDOC);
            System.out.println();
         }
      }

   }

   @Override
   public void visit(Education education) {
      System.out.printf("|%s |%s |%s ^|%s{nbsp}%d{nbsp}%s{nbsp}%s{nbsp}%d ^|%s%n", education.getEducation(), education.getDescription(), education.getOrganization(),
                        this.getMonthDisplayName(education.getStartDate().getMonth()), education.getStartDate().getYear(), this.labels.upToAndIncluding(),
                        this.getMonthDisplayName(education.getEndDate().getMonth()), education.getEndDate().getYear(),
                        education.isDiploma() == null ? this.labels.notApplicable() : education.isDiploma() ? this.labels.yes() : this.labels.no());
   }

   @Override
   public void visit(Course course) {
      System.out.printf("|%s |%s ^|%s{nbsp}%d ^|%s%n", course.getDescription(), course.getOrganization(),
                        this.getMonthDisplayName(course.getStartDate().getMonth()), course.getStartDate().getYear(),
                        course.isDiploma() == null ? this.labels.notApplicable() : course.isDiploma() ? this.labels.yes() : this.labels.no());
   }

   @Override
   public void visit(AdditionalInformation additionalInformation) {
      System.out.printf("== %s%n", this.labels.additionalInformation());
      if (this.sectionFilter.getAdditionalInfo().isHobbies()) {
         System.out.printf("%s:: %n", this.labels.hobbies());
         
         // first create a String with all hobbies separated by comma and space
         String input = String.join(", ", additionalInformation.getHobbies());
         
         // then replace all (phrases which might contain a space) before end of sentence (hence the $) with comma and space to ' and' item
         input = input.replaceAll(", ((\\w|\\s)+)$", " "+this.labels.and()+" $1");
         System.out.println(input);
      }
      if (this.sectionFilter.getAdditionalInfo().isOtherCharacteristics()) {
         System.out.printf("%s:: %n", this.labels.otherCharacteristics());
         String input = String.join(", ", additionalInformation.getRemainder());
         
         // for explaination see just a few lines above
         // rloman repeatable code. -> later ... 
         input = input.replaceAll(", ((\\w|\\s)+)$", " "+this.labels.and()+" $1");
         System.out.println(input);
      }
   }

   @Override
   public void visit(Recommendation recommendation) {
      System.out.printf("[quote, %s]%n", recommendation.getWho());
      System.out.println("____");
      for (String line : recommendation.getLines()) {
         System.out.println(line + " +");
      }
      System.out.println("____");
      System.out.println();
      this.renderHorizontalRuler();
   }

   @Override
   public void visit(EmployerList employerList) {
      this.renderPageBreakHard();
      if (!employerList.isEmpty()) {
         System.out.printf("=== %s%n", this.labels.Employers());
         this.renderHistoryFilterIfApplicable();
         for (Employer employer : employerList) {
            employer.accept(this);
         }
      }
   }

   @Override
   public void visit(AssignmentList assignmentList) {
      if (!assignmentList.isEmpty()) {
         System.out.printf("==== %s%n", this.labels.Assignments());
         this.renderHistoryFilterIfApplicable();
         System.out.println();
         for (Assignment assignment : assignmentList) {
            assignment.accept(this);
         }
      }
   }

   @Override
   public void visit(RecommendationList recommendationList) {
      if (!recommendationList.isEmpty()) {
         System.out.printf("== %s%n", this.labels.recommendations());
         for (Recommendation recommendation : recommendationList) {
            recommendation.accept(this);
         }
      }
   }

   @Override
   public void visit(PersonalProject personalProject) {
      if (this.filterWorkitemIn(personalProject)) {
         System.out.println();
         System.out.println("[horizontal]");
         System.out.printf("%s{nbsp}%s{nbsp}%s{nbsp}%s{nbsp}%s:: %n",
                           this.getMonthDisplayName(personalProject.getStartDate().getMonth()),
                           personalProject.getStartDate().getYear(),
                           this.labels.upToAndIncluding(),
                           LocalDate.now().equals(personalProject.getEndDate()) ? this.labels.currently()
                                             : this.getMonthDisplayName(
                                                               personalProject.getEndDate().getMonth()),
                           LocalDate.now().equals(personalProject.getEndDate()) ? Constants.EMPTYSTRING
                                             : personalProject.getEndDate().getYear());
         System.out.println("+");
         this.renderTab();
         System.out.println();
         System.out.printf("_%s_%n", personalProject.getDescription());
         System.out.println();
         System.out.println(BREAKTABLE_ASCIIDOC);
         System.out.println();
         System.out.printf("%s:: %n", this.labels.function());
         System.out.println(personalProject.getFunction().trim());
         if (personalProject.getObjectives() != null && !personalProject.getObjectives().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.objective());
            System.out.println(String.join(", ", personalProject.getObjectives()));
         }

         if (personalProject.getResults() != null && !personalProject.getResults().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.results());
            System.out.println(String.join(", ", personalProject.getResults()));
         }
         this.renderReferences(personalProject);
         System.out.println(BREAKTABLE_ASCIIDOC);

         System.out.printf("%s:: %n", this.labels.usedTechniquesSystemsMethodologies());

         String experienceAsString = String.join(", ",
                           personalProject.getExperience().stream().map(e -> e.getTopic().toString().replaceAll(" ", "{nbsp}"))
                                             .distinct()
                                             .sorted().collect(Collectors.toList()));
         System.out.println(experienceAsString);
         System.out.println();
         System.out.println();
         this.renderTab();
         System.out.println();
         System.out.println(HORIZONTAL_RULER_ASCIIDOC);
         System.out.println();
      }

   }

   @Override
   public void visit(PersonalProjectList personalProjectList) {
      if (!personalProjectList.isEmpty()) {
         System.out.printf("=== %s%n", this.labels.productDevelopment());
         this.renderHistoryFilterIfApplicable();
         System.out.println();
         for (Assignment assignment : personalProjectList) {
            assignment.accept(this);
         }
      }
   }

   @Override
   public void visit(ConsultancyList consultancies) {
      if (!consultancies.isEmpty()) {
         System.out.println("==== Consultancy");
         this.renderHistoryFilterIfApplicable();
         System.out.println();
         for (Consultancy consultancy : consultancies) {
            consultancy.accept(this);
         }
      }
   }

   @Override
   public void visit(Consultancy consultancy) {
      if (this.filterWorkitemIn(consultancy)) {
         System.out.println();
         System.out.println("[horizontal]");
         if (consultancy.getDates() != null) {
            int size = consultancy.getDates().size();
            System.out.printf("%s{nbsp}%s{nbsp}(%d{nbsp}%s):: %n",
                              LocalDate.now().equals(consultancy.getDates().get(size - 1)) ? this.labels.currently()
                                                : this.getMonthDisplayName(
                                                                  consultancy.getDates().get(size - 1).getMonth()),
                              LocalDate.now().equals(consultancy.getDates().get(size - 1)) ? Constants.EMPTYSTRING
                                                : consultancy.getDates().get(size - 1).getYear(),
                              consultancy.calculateDays(), consultancy.calculateDays() > 1 ? this.labels.days() : this.labels.day());
         }
         else {
            System.out.printf("%s{nbsp}%s{nbsp}(%d{nbsp}%s):: %n",
                              LocalDate.now().equals(consultancy.getEndDate()) ? this.labels.currently()
                                                : this.getMonthDisplayName(
                                                                  consultancy.getEndDate().getMonth()),
                              LocalDate.now().equals(consultancy.getEndDate()) ? Constants.EMPTYSTRING
                                                : consultancy.getEndDate().getYear(),
                              consultancy.calculateDays(), consultancy.calculateDays() > 1 ? this.labels.days() : this.labels.day());

         }
         System.out.println("+");
         this.renderTab();
         System.out.println();
         System.out.printf("._%s te %s_%n", consultancy.getName(), consultancy.getCity());
         System.out.printf("%s:: %n", this.labels.description());
         System.out.println(consultancy.getDescription().trim());
         if (consultancy.getObjectives() != null && !consultancy.getObjectives().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.objective());
            System.out.println(String.join(", ", consultancy.getObjectives()));
         }

         if (consultancy.getResults() != null && !consultancy.getResults().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.results());
            System.out.println(String.join(", ", consultancy.getResults()));
         }
         this.renderReferences(consultancy);
         System.out.println();
         System.out.println(BREAKTABLE_ASCIIDOC);

         if (consultancy.getExperience() != null && !consultancy.getExperience().isEmpty()) {
            System.out.printf("%s:: %n", this.labels.usedTechniquesSystemsMethodologies());
            System.out.println(String.join(", ",
                              consultancy.getExperience().stream().map(e -> e.getTopic().toString().replaceAll(" ", "{nbsp}"))
                                                .distinct()
                                                .sorted().collect(Collectors.toList())));
         }
         System.out.println();
         System.out.println();
         this.renderTab();
         System.out.println();
         System.out.println(HORIZONTAL_RULER_ASCIIDOC);
         System.out.println();
      }
   }

   String getDateStringFromTraining(Training training) {

      if (training.getDates() != null) {
         List<String> dates = training.getDates().stream().map(t -> this.formatDateShort(t)).collect(Collectors.toList());
         if (dates.size() == 1) {
            return dates.get(0);
         }
         if (dates.size() > 3) {
            String datesString = dates.get(0) + " "
                              + this.labels.upToAndIncluding() + " "
                              + dates.get(dates.size() - 1)
                              + " (" + dates.size() + " " + (training.isEveningTraining() ? this.labels.evenings() : this.labels.days()) + ")";

            return datesString;
         }
         else {
            String datesString = String.join(", ", dates);
            StringBuilder b = new StringBuilder(datesString);
            int index = b.lastIndexOf(",");
            b.replace(index, index + 1, " " + this.labels.and());

            return b.toString();
         }

      }
      else {
         LocalDate startDate = training.getStartDate();
         LocalDate endDate = training.getEndDate();

         if (startDate.plusDays(1).equals(endDate)) {
            return this.formatDateShort(startDate) + " " + this.labels.and() + " " + this.formatDateShort(endDate);
         }
         else {
            if (training.getDays() != -1) {
               return this.formatDateShort(startDate) + " " + this.labels.upToAndIncluding() + " " + this.formatDateShort(endDate) + " (" + training.getDays() + " " + (training.isEveningTraining() ? this.labels.evenings() : this.labels.days()) + ")";
            }
            else {
               return this.formatDateShort(startDate) + " " + this.labels.upToAndIncluding() + " " + this.formatDateShort(endDate);
            }
         }

      }
   }

   private void redirectOutputStream() throws FileNotFoundException {

      this.currentOutputStream = System.out;
      final String outputDirectory = Constants.AsciiDocConstants.OUTPUTDIRECTORY;
      try {
         FileOutputStream outputStream = new FileOutputStream(outputDirectory + this.outputFilename);
         PrintStream ps = new PrintStream(outputStream);
         System.setOut(ps);
      }
      catch (FileNotFoundException e) {
         LOGGER.error("Unable to open output! Directory:[{}], fileName:[{}]", outputDirectory, this.outputFilename);

         throw e;
      }
   }

   // resets the outputstream to the previous outputstream
   private void resetOutputStream() {
      System.setOut(this.currentOutputStream);
   }

   // these three log methods are used for logging when rendering / skipping some sections of the resume
   private boolean log(WorkItemSectionFilter filter) {
      LOGGER.warn("Skipping workitem[{}]", filter);

      return false;
   }

   private boolean log(SectionFilter filter) {
      LOGGER.warn("Skipping sectionfilter [{}]", filter);

      return false;
   }

   private boolean log(TrainingType trainingType, int amount) {
      LOGGER.info("Skipping trainingType [{}] since [{}] is below [{}]", String.format("%-32s", trainingType), amount, this.minimalTrainingOccurrencesToRender);

      return false;
   }

   String formatDateShort(LocalDate localDate) {
      if (this.language == Language.NEDERLANDS) {
         return localDate.format(DateTimeFormatter.ofPattern("dd-MM-uuuu", new Locale("nl", "NL")));
      }
      else {
         if (this.language == Language.ENGLISH) {
            return localDate.format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
         }
         else {
            return localDate.format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
         }
      }
   }

   private boolean isHistoryFilterOn() {
      return this.history != -1;
   }

   boolean filterWorkitemIn(AbstractWorkItem work) {
      List<LocalDate> dates = work.getDates();
      if (dates == null) {
         return this.filterGeneral(work.getEndDate());
      }
      else {
         LocalDate lastDate = work.getDates().get(work.getDates().size() - 1);
         return this.filterGeneral(lastDate);
      }
   }

   boolean filterEducation(Education education) {
      LocalDate endDate = education.getEndDate();
      if (endDate == null) {
         return true; // apparantly a now running education which should be ok of course
      }
      else {
         return this.filterGeneral(education.getEndDate());
      }
   }

   boolean filterGeneral(LocalDate endDate) {
      return !this.isHistoryFilterOn() || LocalDate.now().minusYears(this.history).compareTo(endDate) < 0;
   }

   private void renderTab() {
      System.out.println(TAB_ASCIIDOCTOR);
   }

   private void renderHorizontalRuler() {
      System.out.println(HORIZONTAL_RULER_ASCIIDOC);
      System.out.println();
   }

   private void renderPageBreakHard() {
      System.out.println();
      System.out.println(HARDPAGEBREAK_ASCIIDOC);
      System.out.println();
   }

   private void deInternationaliseResumeVisit(Resume resume) {
      switch (this.language) {
         case ENGLISH:
            LOGGER.debug("Starting deinternationisation from English");
            resume.accept(new DeInternationalisingFromEnglishVisitor());
            LOGGER.debug("Ending deinternationisation from English");

            break;
         case NEDERLANDS:
            LOGGER.debug("Starting deinternationisation from Dutch");
            resume.accept(new DeInternationalisingFromDutchVisitor());
            LOGGER.debug("Ending deinternationisation from Dutch");

            break;

         default:
            LOGGER.error("No deinternation process started. Please add case in asciidocgenvis::deInternationaliseResumeVisit");

            break;
      }
   }

   boolean isTrainingtypeToBeRendered(TrainingType trainingType, int amount) {

      return (amount >= this.minimalTrainingOccurrencesToRender
                        || this.trainingtypeWhitelist.contains(trainingType))
                        && !this.trainingtypeBlacklist.contains(trainingType) || this.log(trainingType, amount);
   }

   private void renderSummarizedOverviewTrainings(Trainings trainings) {
      this.renderPageBreakHard();
      System.out.printf("=== %s %s%n", this.labels.summarizedOverview(), this.labels.trainings());
      System.out.println(CARPAGO_TRAINING_TITLE);
      System.out.println();
      if (this.enableCustomerNamesForTrainings) {
         System.out.println(TRAININGS_TABLE_WITH_CUSTOMERNAME_HEADER);
         System.out.println(TABLE_BEGIN_END_ASCIIDOC);
         System.out.printf("^|%s ^|%s ^|%s%n", this.labels.training(), this.labels.customers(), this.labels.period());
      }
      else {
         System.out.println(TRAININGS_TABLE_HEADER);
         System.out.println(TABLE_BEGIN_END_ASCIIDOC);
         System.out.printf("^|%s ^|%s%n", this.labels.training(), this.labels.period());
      }

      trainings.keySet().stream().sorted((t1, t2) -> t1.toString().compareTo(t2.toString())).forEach(trainingType -> {
         Optional<List<Training>> trainingsPerTrainingTypeOptional = trainings.get(trainingType);
         if (trainingsPerTrainingTypeOptional.isPresent()) {
            List<Training> trainingsPerTrainingType = trainingsPerTrainingTypeOptional.get();

            Set<String> customers = new TreeSet<>(); // to avoid duplicates
            for (Training training : trainingsPerTrainingType) {
               customers.addAll(training.getCustomers());
            }
            if (this.isTrainingtypeToBeRendered(trainingType, trainingsPerTrainingType.size())) {
               List<LocalDate> startDates = trainingsPerTrainingType.stream().map(t -> t.getDates() != null ? t.getDates().get(0) : t.getStartDate()).sorted().collect(Collectors.toList());
               List<LocalDate> endDates = trainingsPerTrainingType.stream().map(t -> t.getDates() != null ? t.getDates().get(t.getDates().size() - 1) : t.getEndDate()).sorted((e1, e2) -> e2.compareTo(e1))
                                 .collect(Collectors.toList());

               LocalDate first = startDates.get(0);
               LocalDate last = endDates.get(0);

               if (this.enableCustomerNamesForTrainings) {
                  System.out.printf("|%s ^|%s ^|%s %d - %s %d%n", trainingType.toString(), String.join(" {vbar} ", customers),
                                    this.getMonthDisplayName(
                                                      first.getMonth()),
                                    first.getYear(),
                                    this.getMonthDisplayName(last.getMonth()),
                                    last.getYear());
               }
               else {
                  System.out.printf("|%s ^|%s %d - %s %d%n",
                                    trainingType.toString(),
                                    this.getMonthDisplayName(first.getMonth()),
                                    first.getYear(),
                                    this.getMonthDisplayName(last.getMonth()),
                                    last.getYear());
               }
            }
         }
      });
      System.out.println();
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.println();
   }

   private void renderSummarizedOverviewConsultancies(ConsultancyList consultancies) {
      this.renderPageBreakHard();
      System.out.printf("=== %s consultancy%n", this.labels.summarizedOverview());
      System.out.println(CARPAGO_CONSULTANCY_TITLE);
      System.out.println();
      System.out.println(TABLE_HEADER_DEFAULT);
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.printf("^|%s ^|%s ^|%s%n", this.labels.customer(), this.labels.description(), this.labels.period());
      consultancies.stream()
                        .forEach(consultancy -> {
                           if (consultancy.getDates() != null) {
                              LocalDate endDate = consultancy.getDates().get(consultancy.getDates().size() - 1);
                              System.out.printf("|%s |%s ^|%s %s (%d %s)%n", consultancy.getName(), consultancy.getDescription(),
                                                LocalDate.now().equals(endDate) ? this.labels.currently()
                                                                  : this.getMonthDisplayName(
                                                                                    endDate.getMonth()),
                                                LocalDate.now().equals(endDate) ? Constants.EMPTYSTRING
                                                                  : endDate.getYear(),
                                                consultancy.calculateDays(), consultancy.calculateDays() > 1 ? this.labels.days() : this.labels.day());
                           }
                           else {
                              System.out.printf("|%s |%s ^|%s %s (%d %s)%n", consultancy.getName(), consultancy.getDescription(),
                                                LocalDate.now().equals(consultancy.getEndDate()) ? this.labels.currently()
                                                                  : this.getMonthDisplayName(
                                                                                    consultancy.getEndDate().getMonth()),
                                                LocalDate.now().equals(consultancy.getEndDate()) ? Constants.EMPTYSTRING
                                                                  : consultancy.getEndDate().getYear(),
                                                consultancy.calculateDays(), consultancy.calculateDays() > 1 ? this.labels.days() : this.labels.day());
                           }

                        });
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.println();

   }

   void renderSummarizedOverviewAssignments(AssignmentList assignments) {
      System.out.printf("=== %s %s%n", this.labels.summarizedOverview(), this.labels.assignments());
      System.out.println(CARPAGO_SOFTWARE_TITLE);
      System.out.println();
      System.out.println(TABLE_HEADER_DEFAULT);
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.printf("^|%s ^|%s ^|%s%n", this.labels.customer(), this.labels.function(), this.labels.period());
      assignments.stream().forEach(assignment -> {
         System.out.printf("|%s |%s ^|%s %d - %s %s%n", assignment.getName(), assignment.getFunction(),
                           this.getMonthDisplayName(assignment.getStartDate().getMonth()),
                           assignment.getStartDate().getYear(),
                           LocalDate.now().equals(assignment.getEndDate()) ? this.labels.currently()
                                             : this.getMonthDisplayName(
                                                               assignment.getEndDate().getMonth()),
                           LocalDate.now().equals(assignment.getEndDate()) ? Constants.EMPTYSTRING
                                             : assignment.getEndDate().getYear());
      });
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.println();

   }

   private void generateKnowledgeMatrix(Resume resume) {

      this.calculateExperience(resume.getEmployers());
      this.calculateExperience(resume.getTrainings());
      this.calculateExperience(resume.getAssignments());
      this.calculateExperience(resume.getPersonalProjects());
      this.manuallyOverriddenExperience(resume.getManuallyOverriddenKnowledge());
   }

   private void calculateExperience(PersonalProjectList personalProjectList) {
      for (Assignment assignment : personalProjectList) {
         for (Experience element : assignment.getExperience()) {
            this.validateDuplicates(assignment, assignment.getExperience(), element);
            int between = this.calculateDaysBetween(assignment.getStartDate(), assignment.getEndDate());
            double factor = 1;
            switch (element.getUsed()) {
               case NORMAL:
                  factor = 0.5;
                  break;
               case LOW:
                  factor = 0.1;
                  break;
               default:
                  break;
            }

            this.knowledgeMatrix.addExperienceGlobal(element.getTopic(), Double.valueOf(between * factor).intValue(),
                              assignment.getEndDate());
         }
      }
   }

   private void manuallyOverriddenExperience(List<ManuallyOverriddenKnowledgeBean> knowledge) {
      if (knowledge != null) {
         for (ManuallyOverriddenKnowledgeBean bean : knowledge) {
            if (bean.getStars() != null) {
               this.manuallyOverriddenStars.put(bean.getTopic(), bean.getStars());
            }
            if (bean.getLastUsed() != null) {
               this.manuallyOverriddenLastUsed.put(bean.getTopic(), bean.getLastUsed());
            }
         }
      }
   }

   private void validateDuplicates(AbstractWorkItem workitem, List<Experience> experiences, Experience experience) {
      if (Collections.frequency(experiences, experience) > 1) {
         LOGGER.error("Duplicate found for experience [{}] at assignment [{}]", experience.getTopic(), workitem.getName());
      }
   }

   private void calculateExperience(AssignmentList assignments) {
      for (Assignment assignment : assignments) {
         for (Experience element : assignment.getExperience()) {
            this.validateDuplicates(assignment, assignment.getExperience(), element);
            int between = this.calculateDaysBetween(assignment.getStartDate(), assignment.getEndDate());
            double factor = 1;
            switch (element.getUsed()) {
               case NORMAL:
                  factor = 0.5;
                  break;
               case LOW:
                  factor = 0.1;
                  break;
               default:
                  break;
            }

            this.knowledgeMatrix.addExperienceGlobal(element.getTopic(), Double.valueOf(between * factor).intValue(),
                              assignment.getEndDate());
         }
      }

   }

   private void calculateExperience(Trainings trainings) {
      for (Training training : trainings) {
         LocalDate endDate = null;
         int between = 0;
         if (training.getDates() != null) {
            int sizeOfDates = training.getDates().size();
            between = this.calculateDaysBetween(training.getDates().get(0), training.getDates().get(sizeOfDates - 1));
            endDate = training.getDates().get(sizeOfDates - 1);
         }
         else {
            between = this.calculateDaysBetween(training.getStartDate(), training.getEndDate());
            endDate = training.getEndDate();
         }

         for (Topic topic : training.getType().getTopics()) {
            this.knowledgeMatrix.addExperienceGlobal(topic, between,
                              endDate);
         }
      }
   }

   private void calculateExperience(EmployerList employers) {
      for (Employer employer : employers) {
         for (Experience element : employer.getExperience()) {
            this.validateDuplicates(employer, employer.getExperience(), element);
            int between = this.calculateDaysBetween(employer.getStartDate(), employer.getEndDate());
            double factor = 1;
            switch (element.getUsed()) {
               case NORMAL:

                  factor = 0.5;

                  break;

               case LOW:
                  factor = 0.1;

                  break;

               default:
                  break;
            }

            this.knowledgeMatrix.addExperienceGlobal(element.getTopic(), Double.valueOf(between * factor).intValue(),
                              employer.getEndDate());
         }
      }
   }

   private int calculateDaysBetween(LocalDate startDate, LocalDate endDate) {

      return Double.valueOf(ChronoUnit.DAYS.between(startDate, endDate)).intValue();
   }

   private String getMonthDisplayName(Month month) {

      return this.dateService.getMonthDisplayName(month, this.language);
   }

   // for test purposes only
   int calculateNumberOfStarsForKnowledge(KnowledgeBean knowledgeBean, LocalDate date) {
      LocalDate lastExperienceDate = knowledgeBean.getLastUsed();
      if (ChronoUnit.DAYS.between(lastExperienceDate, date) < 90) {
         return this.calculateForRecentExperience(knowledgeBean);
      }
      else {
         return this.calculateForNotRecentExperience(knowledgeBean, date);
      }
   }

   int calculateNumberOfStarsForKnowledge(KnowledgeBean knowledgeBean) {
      // this localdate.now here also makes a booboo // not true. it is the invoker when not testing
      return this.calculateNumberOfStarsForKnowledge(knowledgeBean, LocalDate.now());
   }

   int calculateForRecentExperience(KnowledgeBean knowledgeBean) {

      if (knowledgeBean.getDays() > 360) {
         return 5;
      }
      if (knowledgeBean.getDays() > 90) {
         return 4;
      }
      if (knowledgeBean.getDays() > 45) {
         return 3;
      }
      return 2;
   }

   int calculateForNotRecentExperience(KnowledgeBean knowledgeBean, LocalDate date) {

      // this LocalDate.now() here makes the booboo
      final long monthBetweenLastExperienceAndNow = ChronoUnit.MONTHS.between(knowledgeBean.getLastUsed(), date);

      // too long ago
      if (monthBetweenLastExperienceAndNow > 36) {
         return 0;
      }
      if (monthBetweenLastExperienceAndNow > 18) {
         if (knowledgeBean.getDays() > 200) {
            return 3;
         }
         else {
            return 2;
         }

      }
      if (monthBetweenLastExperienceAndNow > 9) {
         if (knowledgeBean.getDays() > 200) {
            return 4;
         }
         else {
            return 3;
         }
      }

      if (monthBetweenLastExperienceAndNow > 6) {
         if (knowledgeBean.getDays() > 200) {
            return 3;
         }
         else {
            return 2;
         }
      }

      if (monthBetweenLastExperienceAndNow > 4) {
         return 4;
      }
      else {
         return 5;
      }
   }

   void renderSummarizedOverviewEmployers(EmployerList employers) {
      this.renderPageBreakHard();
      System.out.printf("=== %s %s%n", this.labels.summarizedOverview(), this.labels.employers());
      System.out.println(TABLE_HEADER_DEFAULT);
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.printf("^|%s ^|%s ^|%s%n", this.labels.employer(), this.labels.function(), this.labels.period());
      employers.stream().forEach(employer -> {
         System.out.printf("|%s |%s ^|%s %d - %s %s%n", employer.getName(), employer.getFunction(),
                           this.getMonthDisplayName(employer.getStartDate().getMonth()),
                           employer.getStartDate().getYear(),
                           LocalDate.now().equals(employer.getEndDate()) ? this.labels.currently()
                                             : this.getMonthDisplayName(
                                                               employer.getEndDate().getMonth()),
                           LocalDate.now().equals(employer.getEndDate()) ? Constants.EMPTYSTRING
                                             : employer.getEndDate().getYear());
      });
      System.out.println(TABLE_BEGIN_END_ASCIIDOC);
      System.out.println();
   }

   void renderHistoryFilterIfApplicable() {
      if (this.isHistoryFilterOn()) {
         System.out.printf("filter < %d %s", this.history, this.history < 2 ? this.labels.year() : this.labels.years());
         LOGGER.info("Rendering with filter [{}] years", this.history);
      }
   }

   void renderReferences(AbstractWorkItem workItem) {
      System.out.println();
      if (workItem.getReferences() != null && !workItem.getReferences().isEmpty()) {
         if (workItem.getReferences().size() == 1) {
            System.out.printf("%s:: %n", this.labels.reference());
         }
         else {
            System.out.printf("%s:: %n", this.labels.references());
         }
         for (String reference : workItem.getReferences()) {
            System.out.printf("* " + reference + "[%s, window=\"_blank\"]%n", reference);
         }
      }
      System.out.println();
   }

   void setHistory(int history) {
      this.history = history;
   }

   public static class AsciidocGeneratingVisitorBuilder {

      private static final Logger LOGGER = LoggerFactory.getLogger(AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder.class);

      private Language language;
      private int history = -1;
      private Labels labels;
      private SectionFilter sectionFilter = new SectionFilter();
      private int minimalTrainingOccurrences;
      private List<TrainingType> trainingtypeWhitelist = new ArrayList<>();
      private List<TrainingType> trainingtypeBlacklist = new ArrayList<>();
      private String outputFilename;
      private String resumeOutputFileNamePdf;
      private ChronologischAppendixFilter chronologischAppendixFilter;
      private boolean enableCustomerNamesForTrainings;

      public AsciidocGeneratingVisitorBuilder(Language language) {
         this.language = language;
      }

      public AsciidocGeneratingVisitorBuilder language(Language language) {
         this.language = language;

         return this;
      }

      public AsciidocGeneratingVisitorBuilder history(int history) {
         if (history > 0) {
            this.history = history;
         }
         else {
            LOGGER.error("history should be greater than 0 but is [{}]. History NOT set", history);
         }
         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableOverview() {
         this.sectionFilter.getOverview().setOn(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableExtensive() {
         this.sectionFilter.getExtensive().setOn(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableOverviewTrainings() {
         this.sectionFilter.getOverview().setTrainings(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableOverviewConsultancy() {
         this.sectionFilter.getOverview().setConsultancy(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableOverviewEmployers() {
         this.sectionFilter.getOverview().setEmployers(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableExtensiveTrainings() {
         this.sectionFilter.getExtensive().setTrainings(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableExtensiveConsultancy() {
         this.sectionFilter.getExtensive().setConsultancy(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableExtensiveEmployers() {
         this.sectionFilter.getExtensive().setEmployers(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableEducation() {
         this.sectionFilter.getEducation().setOn(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableSchools() {
         this.sectionFilter.getEducation().setSchools(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableOtherEducation() {
         this.sectionFilter.getEducation().setOther(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableCourses() {
         this.sectionFilter.getEducation().setCourses(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableAdditionalInfo() {
         this.sectionFilter.getAdditionalInfo().setOn(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableHobbies() {
         this.sectionFilter.getAdditionalInfo().setHobbies(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableOtherCharacteristics() {
         this.sectionFilter.getAdditionalInfo().setOtherCharacteristics(false);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder minimalTrainingOccurrences(int minimalTrainingOccurrences) {
         this.minimalTrainingOccurrences = minimalTrainingOccurrences;

         return this;
      }

      public AsciidocGeneratingVisitorBuilder enableTrainingType(TrainingType trainingType) {
         this.trainingtypeWhitelist.add(trainingType);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder disableTrainingType(TrainingType trainingType) {
         this.trainingtypeBlacklist.add(trainingType);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder enableAppendix(int startMonth, int startYear, int endMonth, int endYear) {
         this.chronologischAppendixFilter = new ChronologischAppendixFilter(startMonth, startYear, endMonth, endYear);

         return this;
      }

      public AsciidocGeneratingVisitorBuilder enableCustomerNamesForTrainings() {

         this.enableCustomerNamesForTrainings = true;

         return this;
      }

      public AsciidocGeneratingVisitor build() {

         final Properties properties = new Properties();
         String version = null;
         try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
            version = properties.getProperty("version");
            LOGGER.debug("Using version [{}] from application.properties::version", version);
         } catch (IOException e) {
            LOGGER.error("Unable to read application.properties hence version missing. Assuming blank instead.");
            version = "";
         }

         switch (this.language) {
            case NEDERLANDS:
               this.labels = new DutchLabels();
               this.outputFilename = Constants.AsciiDocConstants.DUTCH_CV_OUTPUT_FILENAME;
               this.resumeOutputFileNamePdf = String.format("cv-raymondloman-v%s.pdf", version);

               break;
            case ENGLISH:
               this.labels = new EnglishLabels();
               this.outputFilename = Constants.AsciiDocConstants.ENGLISH_RESUME_OUTPUT_FILENAME;
               this.resumeOutputFileNamePdf = String.format("resume-raymondloman-v%s.pdf", version);
               break;

            default:
               LOGGER.error("Unknown language. No labels set. Please add to " + this.getClass().getName() + "::build");
               break;
         }
         return new AsciidocGeneratingVisitor(this);
      }

   }

   @Override
   public void visit(EducationList educationList) {
      for (Education education : educationList) {
         education.accept(this);
      }
   }

   @Override
   public void visit(CourseList courses) {
      for (Course course : courses) {
         course.accept(this);
      }
   }

   @Override
   public void visit(YearMonthPrinterChain yearMonthPrinterChain) {
      // TODO Auto-generated method stub

   }
}