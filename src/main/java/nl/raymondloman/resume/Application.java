package nl.raymondloman.resume;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import nl.raymondloman.resume.domain.Resume;
import nl.raymondloman.resume.domain.enums.Language;
import nl.raymondloman.resume.domain.enums.TrainingType;
import nl.raymondloman.resume.visitor.AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder;
import nl.raymondloman.resume.visitor.Visitor;

@SuppressWarnings("unused")
public class Application {

   private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

   private static AsciidocGeneratingVisitorBuilder createBuilder(Language language) {
      AsciidocGeneratingVisitorBuilder builder = new AsciidocGeneratingVisitorBuilder(language)
                        
                        .minimalTrainingOccurrences(2)
                        
                        .disableTrainingType(TrainingType.Data_Persistence_with_Hibernate)
                        .disableTrainingType(TrainingType.Ruby_on_Rails_Programming)
                        .disableTrainingType(TrainingType.Ruby_Programming)
                        .disableTrainingType(TrainingType.Groovy_Programming)
                        .disableTrainingType(TrainingType.Grails_Programming)

                        .enableTrainingType(TrainingType.OCP_Topics_and_Security)
                        .enableTrainingType(TrainingType.SE_Track)
                        .enableTrainingType(TrainingType.SE_Track_Databases)
                        .enableTrainingType(TrainingType.SE_Track_Frontend)
                        .enableTrainingType(TrainingType.SE_Track_TestingAndSecurity)
                        .enableTrainingType(TrainingType.Java_Development_with_Spring_Boot)
                        .enableTrainingType(TrainingType.Angular)
                        .enableTrainingType(TrainingType.Android_Awareness)

                        //   .history(5) 
                        //    .enableAppendix(1, 2008, LocalDate.now().getMonthValue(), LocalDate.now().getYear())
                        
//                        .enableCustomerNamesForTrainings()
                        ;

      return builder;
   }

   public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

      Resume resume = readResumeFromFile();

      // Create dutch builder
      AsciidocGeneratingVisitorBuilder builder = createBuilder(Language.NEDERLANDS);
      Visitor visitor = null;

      {
         visitor = builder.build();
         resume.accept(visitor);
      }

      // Amend to English builder
      {

         visitor = builder.language(Language.ENGLISH).build();
         resume.accept(visitor);
      }

      LOGGER.info("Resume generated");
   }

   private static Resume readResumeFromFile() throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
      // JSON from file to Object
      Resume resume = mapper.readValue(new File(Constants.INPUT_FILENAME), Resume.class);

      return resume;
   }
}
