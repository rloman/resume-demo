package nl.raymondloman.resume.domain.personal;

import java.util.List;

import nl.raymondloman.resume.domain.i18n.ListI18N;
import nl.raymondloman.resume.domain.i18n.StringI18N;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class Recommendation extends Node {

   private StringI18N whoI18N;
   private String who;

   private ListI18N linesI18N;
   private List<String> lines;

   public String getWho() {
      return this.who;
   }

   public void setWho(String who) {
      this.who = who;
   }

   public List<String> getLines() {
      return this.lines;
   }

   public void setLines(List<String> lines) {
      this.lines = lines;
   }

   public ListI18N getLinesI18N() {
      return this.linesI18N;
   }

   public void setLinesI18N(ListI18N linesI18N) {
      this.linesI18N = linesI18N;
   }

   public StringI18N getWhoI18N() {
      return this.whoI18N;
   }

   public void setWhoI18N(StringI18N whoI18N) {
      this.whoI18N = whoI18N;
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}