package nl.raymondloman.resume.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.raymondloman.resume.domain.enums.Topic;
import nl.raymondloman.resume.domain.other.KnowledgeBean;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class KnowledgeMatrix extends Node implements Iterable<Entry<Topic, KnowledgeBean>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeMatrix.class);

   private Map<Topic, KnowledgeBean> experience = new HashMap<>();

   public void addExperienceGlobal(Topic topic, int days, LocalDate lastUsed) {

      LOGGER.debug("Adding [{}] days of experience for Topic [{}] with lastUsedDate [{}]", days, topic, lastUsed);

      if (this.experience.get(topic) == null) {
         this.experience.put(topic, new KnowledgeBean(days, 1, lastUsed));
      }
      else {
         KnowledgeBean bean = this.experience.get(topic);
         if (lastUsed.compareTo(bean.getLastUsed()) > 0) {
            bean.setLastUsed(lastUsed);
         }
         bean.addDays(days);
         bean.hit();
      }
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
   }

   @Override
   public Iterator<Entry<Topic, KnowledgeBean>> iterator() {
      return this.experience.entrySet().stream().sorted(
                        (a, b) -> a.getKey().toString().toLowerCase().compareTo(b.getKey().toString().toLowerCase()))
                        .collect(Collectors.toList()).iterator();
   }
}