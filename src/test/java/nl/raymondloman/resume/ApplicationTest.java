package nl.raymondloman.resume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ApplicationTest {

   @Test
   public void testApplication() throws JsonParseException, JsonMappingException, IOException {

      Application.main(new String[]{});

      Path pathToOutputFile = Paths.get(Constants.AsciiDocConstants.OUTPUTDIRECTORY + Constants.AsciiDocConstants.DUTCH_CV_OUTPUT_FILENAME);
      Assert.assertTrue(Files.exists(pathToOutputFile, LinkOption.NOFOLLOW_LINKS));
   }
}