package nl.raymondloman.resume;

public final class Constants {

   public static final String INPUT_FILENAME = "src/main/resources/resume.json";
   public static final String BUSINESS_NAME = "Acme Solutions";
   public static final String EMPTYSTRING = "";
   
   public static final class AsciiDocConstants {
      
      public static final String OUTPUTDIRECTORY = "src/main/asciidoc/";
      public static final String DUTCH_CV_OUTPUT_FILENAME = "cv.adoc";
      public static final String ENGLISH_RESUME_OUTPUT_FILENAME = "resume.adoc";

      private AsciiDocConstants() {
      }
   }

   private Constants() {
   }
}