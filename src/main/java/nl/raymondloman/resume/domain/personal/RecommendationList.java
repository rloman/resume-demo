package nl.raymondloman.resume.domain.personal;

import java.util.Iterator;
import java.util.List;

import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class RecommendationList extends Node implements Iterable<Recommendation> {

   private List<Recommendation> recommendations;

   public RecommendationList(List<Recommendation> recommendations) {
      this.recommendations = recommendations;
   }

   public boolean isEmpty() {
      return this.recommendations.isEmpty();
   }

   @Override
   public Iterator<Recommendation> iterator() {
      return this.recommendations.iterator();
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }
}