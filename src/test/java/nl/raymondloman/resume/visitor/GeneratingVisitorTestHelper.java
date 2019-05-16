package nl.raymondloman.resume.visitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.raymondloman.resume.Constants;

public abstract class GeneratingVisitorTestHelper {

   private static final Logger LOGGER = LoggerFactory.getLogger(GeneratingVisitorTestHelper.class);

	private ByteArrayOutputStream baos;

	protected GeneratingVisitorTestHelper() {

		this.baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(this.baos));

	}

	protected void check(String expected) {

		String actual = this.baos.toString();

		Assert.assertEquals(expected, actual);
	}
	
	protected void checkEmpty() {
	   check(Constants.EMPTYSTRING);
	}
	
	protected void checkDoesNotContain(String expected) {
	   Assert.assertFalse(this.baos.toString().contains(expected));
	}
	
	protected void checkEmptyIgnoreWhitespace() {
	   String actual = this.baos.toString();

      Assert.assertEquals(Constants.EMPTYSTRING, actual.trim());
	}
	
	protected void checkContains(String expected) {
	   Assert.assertTrue(this.baos.toString().contains(expected));
	}
	
	protected void checkLinesEmpty(int ... lines) {
	   for(int i : lines) {
	      check(Constants.EMPTYSTRING, i);
	   }
	}

   protected void check(String expected, int lineNumber) {
      String[] splittedLines = this.baos.toString().split(String.format("%n"));
      List<String> list = Arrays.asList(splittedLines);

      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("Start lines =============================");
         for (int i = 0; i < list.size(); i++) {
            LOGGER.debug("Line [{}] contains [{}]", i, list.get(i));
         }
         LOGGER.debug("End lines ===============================");
      }

      Assert.assertEquals(expected, list.get(lineNumber - 1));
   }

   @Override
   public String toString() {
      return baos.toString();
   }
}
