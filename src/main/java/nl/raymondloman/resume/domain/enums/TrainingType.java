package nl.raymondloman.resume.domain.enums;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public enum TrainingType {

   Android_Awareness(Topic.Android, Topic.Java, Topic. JUnit, Topic.Gradle),

   Oracle_Certified_Associate_Applied(Topic.Java, Topic.JUnit, Topic.Maven, Topic.Git, Topic.Jenkins),
   Oracle_Certified_Associate(Topic.Java),
   Oracle_Certified_Associate_Examtraining(Topic.Java),
   Oracle_Certified_Programmer(Topic.Java),
   Oracle_Certified_Programmer_Examtraining(Topic.Java),

   REST_Security_with_JWT(Topic.Java, Topic.Angular, Topic.REST, Topic.Security, Topic.Spring_Security),

   OCP_Topics_and_Security(new TrainingType[]{REST_Security_with_JWT, Oracle_Certified_Programmer}, Topic.OWASP, Topic.WebGoat),
   
   SE_Track_Databases( Topic.MySQL,
                     Topic.JPA),
   
   SE_Track_TestingAndSecurity(  Topic.JUnit,
                     Topic.Mockito, Topic.Security, Topic.Spring_Security, Topic.OWASP, Topic.WebGoat),
   
   SE_Track_Frontend(Topic.Spring_Boot,
                     Topic.REST,
                     Topic.HTML5,
                     Topic.JavaScript,
                     Topic.NodeJS,
                     Topic.CSS3,
                     Topic.jQuery,
                     Topic.Bootstrap,
                     Topic.Angular
                     ),
   SE_Track(new TrainingType[]{TrainingType.SE_Track_Frontend, TrainingType.SE_Track_Databases, TrainingType.SE_Track_TestingAndSecurity},
                     Topic.Java,
                     Topic.Spring,
                     Topic.Spring_Boot,
                     Topic.Git,
                     Topic.Maven,
                     Topic.Jenkins
                    ),
   Java_for_DevOps(Topic.Java),
   Developer_Devops_Training(
                     Topic.UML,
                     Topic.Java,
                     Topic.Spring_Boot,
                     Topic.Maven,
                     Topic.Git,
                     Topic.Git_Flow,
                     Topic.Java_EE,
                     Topic.REST,
                     Topic.JAXRS,
                     Topic.Jersey,
                     Topic.JPA,
                     Topic.HTML5,
                     Topic.Angular,
                     Topic.Continuous_Delivery,
                     Topic.MicroServices,
                     Topic.DevOps,
                     Topic.Jenkins,
                     Topic.Docker,
                     Topic.Scrum),
   Angular(Topic.JavaScript, Topic.TypeScript, Topic.Angular),
   Tomcat_Administration(Topic.Tomcat, Topic.Java, Topic.REST, Topic.Java_EE),
   JavaScript_Fundamentals(Topic.JavaScript),
   Apache_HTTP_Server_Administration(
                     Topic.Apache,
                     Topic.Linux,
                     Topic.SSL,
                     Topic.Docker,
                     Topic.MicroServices),
   Continuous_Delivery_and_Tooling(
                     Topic.Java,
                     Topic.Spring_Boot,
                     Topic.JUnit,
                     Topic.Git,
                     Topic.Git_Flow,
                     Topic.Maven,
                     Topic.Jenkins,
                     Topic.Nexus,
                     Topic.Artifactory,
                     Topic.Sonar,
                     Topic.Continuous_Delivery),
   CPP_Advanced_Programming(Topic.CPP, Topic.CPP_Standard_Library, Topic.CPP_Standard_Template_Library),
   Data_Persistence_with_Hibernate(Topic.Java, Topic.Hibernate, Topic.JPA, Topic.Java_EE, Topic.MySQL),
   CPP_Programming(
                     Topic.CPP,
                     Topic.CPP_Standard_Library),
   Advanced_JavaScript(Topic.JavaScript, Topic.jQuery),
   Git_for_developers(
                     Topic.Git,
                     Topic.Git_Flow),
   Grails_Programming(Topic.Grails, Topic.Groovy, Topic.MySQL),
   Groovy_Programming(Topic.Groovy),
   Weblogic_Essentials(
                     Topic.Java,
                     Topic.Java_EE,
                     Topic.Weblogic,
                     Topic.JMS),
   Weblogic_Basic_Administration(Topic.Java, Topic.Java_EE, Topic.Weblogic, Topic.JMS),
   Weblogic_Advanced_Administration(
                     Topic.Java,
                     Topic.Java_EE,
                     Topic.Weblogic,
                     Topic.JMS),
   Ruby_Programming(Topic.Ruby),
   UML_OO_and_RUP(
                     Topic.Java,
                     Topic.RUP,
                     Topic.UML),
   Ruby_on_Rails_Programming(
                     Topic.Ruby,
                     Topic.Rails,
                     Topic.REST),
   Java_SE_8_Upgradetraining(Topic.Java),
   Java_EE_7_Programming(
                     Topic.Java,
                     Topic.Java_EE,
                     Topic.JMS),
   Java_Development_with_Spring_Boot(
                     Topic.Java,
                     Topic.Spring_Boot,
                     Topic.REST,
                     Topic.Jersey,
                     Topic.JAXRS,
                     Topic.JPA,
                     Topic.Spring_MVC,
                     Topic.Spring_Security),
   Java_EE_7_Upgradetraining(
                     Topic.Java,
                     Topic.Java_EE,
                     Topic.JSON,
                     Topic.Jackson,
                     Topic.REST,
                     Topic.JAXRS,
                     Topic.JAXWS,
                     Topic.JMS),
   Java_SE_Fundamentals(
                     Topic.Java);

private final Set<Topic> topics = new LinkedHashSet<>(); // to prevent duplicates I moved from List to Set

private TrainingType(TrainingType[] trainingTypes, Topic ... myOwnTopics) {
   this(trainingTypes);
   
   this.topics.addAll(Arrays.asList(myOwnTopics));
}

private TrainingType(TrainingType...trainingTypes) {
   
   for(TrainingType trainingType : trainingTypes) {
      this.topics.addAll(trainingType.getTopics());
   }
}

private TrainingType(Topic... topics) {
   this.topics.addAll(Arrays.asList(topics));
}


private void validateTrainingType(Collection<Topic> topicList) {
   for (Topic topic : topicList) {
      if (Collections.frequency(topicList, topic) > 1) {
         System.err.printf("WARNING: Duplicate Topic '%s' found in TrainingType '%s' %n", topic, this.name());
      }
   }
}

public Set<Topic> getTopics() {
   this.validateTrainingType(this.topics);

   return this.topics;
}

// this method returns true if this trainingType is embedded in an other trainingType and not perse directly trained to students
public boolean isEmbeddedOnly() {
   switch (this) {
      case SE_Track_Databases:
      case SE_Track_Frontend:
      case SE_Track_TestingAndSecurity:
      case REST_Security_with_JWT:
         return true;

      default:
          return false;
   }
}

@Override
public String toString() {

   switch (this) {
      case CPP_Advanced_Programming:
         return "C++ Advanced Programming";

      case UML_OO_and_RUP:
         return "UML, OO and RUP";

      case CPP_Programming:
         return "C++ Programming";

      case SE_Track:
         return "Full Stack Software Engineer Intro";
         
      case SE_Track_Databases:
         return "Persistence from database to JPA";
         
      case SE_Track_Frontend:
         return "Frontend Development";
      case SE_Track_TestingAndSecurity:
         return "Testing and Security";
      default:
         return this.name().replaceAll("_", " ");
   }
}
}
