package nl.raymondloman.resume.domain.assignment;

import java.util.List;

import nl.raymondloman.resume.domain.AbstractWorkItem;
import nl.raymondloman.resume.domain.i18n.ListI18N;
import nl.raymondloman.resume.domain.i18n.StringI18N;
import nl.raymondloman.resume.visitor.Visitor;

public class Assignment extends AbstractWorkItem {

   private String name;

   private StringI18N descriptionI18N;
   private String description;

   private ListI18N objectivesI18N;
   private List<String> objectives;

   public List<String> getObjectives() {
      return this.objectives;
   }

   public void setObjectives(List<String> objectives) {
      this.objectives = objectives;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public StringI18N getDescriptionI18N() {
      return this.descriptionI18N;
   }

   public void setDescriptionI18N(StringI18N descriptionI18N) {
      this.descriptionI18N = descriptionI18N;
   }

   public ListI18N getObjectivesI18N() {
      return this.objectivesI18N;
   }

   public void setObjectivesI18N(ListI18N objectivesI18N) {
      this.objectivesI18N = objectivesI18N;
   }

   @Override
   public String toString() {
      return String.format("%n- Assignment:  [name=" + this.name + ", description="
                        + this.description + ", getName()=" + this.getName() + "]");
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}
