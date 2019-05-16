package nl.raymondloman.resume.domain;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.raymondloman.resume.domain.enums.Language;
import nl.raymondloman.resume.visitor.AsciidocGeneratingVisitor;
import nl.raymondloman.resume.visitor.AsciidocGeneratingVisitor.AsciidocGeneratingVisitorBuilder;
import nl.raymondloman.resume.visitor.GeneratingVisitorTestHelper;

public class IntroductionTest extends GeneratingVisitorTestHelper {

   private Introduction intro;

   private AsciidocGeneratingVisitor visitor;

   @Before
   public void setUp() {
      this.intro = new Introduction();
      this.intro.setLines(new ArrayList<>());
      AsciidocGeneratingVisitorBuilder b = new AsciidocGeneratingVisitorBuilder(Language.NEDERLANDS);
      this.visitor = b.build();
   }

   @Test
   public void testSimple() {
      this.intro.setTitle("Carpago Software");
      this.intro.getLines().add("Carpago Software first line");
      this.intro.accept(this.visitor);

      this.check(".Carpago Software", 1);
      this.check("* Carpago Software first line", 2);
   }

   @Test
   public void testMoreDepth() {
      this.intro.setTitle("Carpago Software");
      this.intro.getLines().add("Carpago Software sub 0 line 1");
      this.intro.getLines().add("Carpago Software sub 0 line 2");

      Introduction subIntro = new Introduction();
      subIntro.setLines(new ArrayList<>());
      subIntro.getLines().add("Carpago Software sub 1 line 1");
      subIntro.getLines().add("Carpago Software sub 1 line 2");
      List<Introduction> introList = new ArrayList<Introduction>();
      introList.add(subIntro);
      this.intro.setSub(introList);

      Introduction subSubIntro = new Introduction();
      subSubIntro.setLines(new ArrayList<>());
      subSubIntro.getLines().add("Carpago Software sub 2 line 1");
      subSubIntro.getLines().add("Carpago Software sub 2 line 2");
      introList = new ArrayList<>();
      introList.add(subSubIntro);
      subIntro.setSub(introList);

      this.intro.accept(this.visitor);
      int counter = 1;
      this.check(".Carpago Software", counter++);
      this.check("* Carpago Software sub 0 line 1", counter++);
      this.check("* Carpago Software sub 0 line 2", counter++);
      this.check("** Carpago Software sub 1 line 1", counter++);
      this.check("** Carpago Software sub 1 line 2", counter++);
      this.check("*** Carpago Software sub 2 line 1", counter++);
      this.check("*** Carpago Software sub 2 line 2", counter++);
   }

   @Test
   public void testObscureWithMoreTitles() {
      this.intro.setTitle("Carpago Software");
      this.intro.getLines().add("Carpago Software sub 0 line 1");
      this.intro.getLines().add("Carpago Software sub 0 line 2");

      Introduction subIntro = new Introduction();
      subIntro.setLines(new ArrayList<>());
      subIntro.getLines().add("Carpago Software sub 1 line 1");
      subIntro.getLines().add("Carpago Software sub 1 line 2");
      List<Introduction> introList = new ArrayList<>();
      introList.add(subIntro);
      this.intro.setSub(introList);

      Introduction subSubIntro = new Introduction();
      subSubIntro.setTitle("My title");
      subSubIntro.setLines(new ArrayList<>());
      subSubIntro.getLines().add("Carpago Software sub 2 line 1");
      subSubIntro.getLines().add("Carpago Software sub 2 line 2");
      introList = new ArrayList<>();
      introList.add(subSubIntro);
      subIntro.setSub(introList);

      this.intro.accept(this.visitor);
      int counter = 1;
      this.check(".Carpago Software", counter++);
      this.check("* Carpago Software sub 0 line 1", counter++);
      this.check("* Carpago Software sub 0 line 2", counter++);
      this.check("** Carpago Software sub 1 line 1", counter++);
      this.check("** Carpago Software sub 1 line 2", counter++);
      this.check("*** My title", counter++);
      this.check("**** Carpago Software sub 2 line 1", counter++);
      this.check("**** Carpago Software sub 2 line 2", counter++);
   }
}
