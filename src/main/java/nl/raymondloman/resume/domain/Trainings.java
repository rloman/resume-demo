package nl.raymondloman.resume.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import nl.raymondloman.resume.domain.enums.TrainingType;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.domain.training.Training;
import nl.raymondloman.resume.visitor.Visitor;

public class Trainings extends Node implements Iterable<Training> {
   
   private Map<TrainingType, List<Training>> trainings = new HashMap<TrainingType, List<Training>>();

   public Set<Entry<TrainingType, List<Training>>> entrySet() {
      return trainings.entrySet();
   }

   public Optional<List<Training>> get(Object key) {
      List<Training> result = trainings.get(key);
      if(result != null) {
         return Optional.of(result);
      }
      else {
         return Optional.empty();
      }
      
   }

   public List<Training> put(TrainingType key, List<Training> value) {
      return trainings.put(key, value);
   }

   public Set<TrainingType> keySet() {
      return trainings.keySet();
   }
   
   private List<Training> getAllTrainings() {
      List<Training> result = new ArrayList<>();
      for (Entry<TrainingType, List<Training>> element : this.trainings.entrySet()) {
         result.addAll(element.getValue());
      }

      return result;
   }
   
   @Override
   public Iterator<Training> iterator() {
     return this.getAllTrainings().iterator();
   }

   @Override
   public void accept(Visitor visitor) {
     visitor.visit(this);
   }
}