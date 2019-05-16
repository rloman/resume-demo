package nl.raymondloman.resume.domain;

import java.util.List;

import nl.raymondloman.resume.domain.i18n.ListI18N;
import nl.raymondloman.resume.domain.i18n.StringI18N;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class Introduction extends Node {

   private StringI18N titleI18N;
   private String title;

   private StringI18N activitiesTitleI18N;
   private String activitiesTitle;

   private ListI18N linesI18N;
   private List<String> lines;

   private List<Introduction> sub;

   public List<String> getLines() {
      return this.lines;
   }


   public void setLines(List<String> lines) {
      this.lines = lines;
   }


   public List<Introduction> getSub() {
      return this.sub;
   }


   public void setSub(List<Introduction> sub) {
      this.sub = sub;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getActivitiesTitle() {
      return this.activitiesTitle;
   }

   public void setActivitiesTitle(String activitiesTitle) {
      this.activitiesTitle = activitiesTitle;
   }

   public StringI18N getTitleI18N() {
      return this.titleI18N;
   }

   public void setTitleI18N(StringI18N titleI18N) {
      this.titleI18N = titleI18N;
   }

   public StringI18N getActivitiesTitleI18N() {
      return this.activitiesTitleI18N;
   }

   public void setActivitiesTitleI18N(StringI18N activitiesTitleI18N) {
      this.activitiesTitleI18N = activitiesTitleI18N;
   }

   public ListI18N getLinesI18N() {
      return this.linesI18N;
   }

   public void setLinesI18N(ListI18N linesI18N) {
      this.linesI18N = linesI18N;
   }

   @Override
   public String toString() {
      return "Introduction [title=" + this.title + ", activitiesTitle=" + this.activitiesTitle + "]";
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}
