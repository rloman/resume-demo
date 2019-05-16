package nl.raymondloman.resume.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import nl.raymondloman.resume.domain.i18n.ListI18N;
import nl.raymondloman.resume.domain.i18n.StringI18N;
import nl.raymondloman.resume.domain.other.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWorkItem extends Node implements Comparable<AbstractWorkItem> {

   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWorkItem.class);

   private String city;

   private StringI18N functionI18N;
   private String function;

   private ListI18N responsibilitiesI18N;
   private List<String> responsibilities;

   private ListI18N resultsI18N;
   private List<String> results;

   private List<Experience> experience;

   private LocalDate startDate;
   private LocalDate endDate = LocalDate.now();
   private List<LocalDate> dates;

   private List<String> references;

   private int days = -1;

   public abstract String getName();

   public LocalDate getStartDate() {
      if(this.startDate != null) {
         return this.startDate;
      }
      else {
         return this.dates.get(0);
      }
      
   }

   public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
   }

   public LocalDate getEndDate() {
      if(this.endDate != null) {
         return this.endDate;
      }
      else {
         LocalDate now = LocalDate.now();
         //
         Optional<LocalDate> optionalLastDateSmallerThanNow = this.dates.stream()
                 .sorted(Comparator.reverseOrder())
                 .filter(d -> d.compareTo(now) < 0)
                 .findFirst();
         if(optionalLastDateSmallerThanNow.isPresent()) {
            return optionalLastDateSmallerThanNow.get();
         }
         else {
            LOGGER.error("Found Workitem for which all entered dates are larger than now - is not started yet. Hence no endDate can be set, falling back to now for now :-)");

            return now;
         }
      }
   }

   public void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
   }

   public String getCity() {
      return this.city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public List<Experience> getExperience() {
      return this.experience;
   }

   public void setExperience(List<Experience> experience) {
      this.experience = experience;
   }

   public String getFunction() {
      return this.function;
   }

   public void setFunction(String function) {
      this.function = function;
   }

   public List<String> getResponsibilities() {
      return this.responsibilities;
   }

   public List<String> getResults() {
      return this.results;
   }

   public void setResponsibilities(List<String> responsibilities) {
      this.responsibilities = responsibilities;
   }

   public void setResults(List<String> results) {
      this.results = results;
   }

   public StringI18N getFunctionI18N() {
      return this.functionI18N;
   }

   public void setFunctionI18N(StringI18N functionI18N) {
      this.functionI18N = functionI18N;
   }

   public ListI18N getResponsibilitiesI18N() {
      return this.responsibilitiesI18N;
   }

   public void setResponsibilitiesI18N(ListI18N responsibilitiesI18N) {
      this.responsibilitiesI18N = responsibilitiesI18N;
   }

   public ListI18N getResultsI18N() {
      return this.resultsI18N;
   }

   public void setResultsI18N(ListI18N resultsI18N) {
      this.resultsI18N = resultsI18N;
   }
   public List<String> getReferences() {
      return this.references;
   }

   public void setReferences(List<String> references) {
      this.references = references;
   }

   public List<LocalDate> getDates() {
      if(this.dates != null) {
         return this.dates.stream()
                 .filter(d -> d.compareTo(LocalDate.now()) < 0)
                 .collect(Collectors.toList());
      }
      else {
         return null; // bad but it seems to test on it, rloman will refactor later
      }

   }

   public void setDates(List<LocalDate> dates) {
      this.dates = dates;
      Collections.sort(this.dates);
      this.endDate = null; // to set to null to know that dates are set
   }

   public int calculateDays() {
      if (this.days != -1) {
         return this.days;
      }
      else {
         if (this.getDates() != null) {
            return this.getDates().size();
         }
         else {
            return Long.valueOf(ChronoUnit.DAYS.between(this.getStartDate(), this.getEndDate())).intValue();
         }
      }
   }

   public void setDays(int days) {
      this.days = days;
   }

   public int getDays() {
      return this.days;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
      result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof AbstractWorkItem)) {
         return false;
      }
      AbstractWorkItem other = (AbstractWorkItem) obj;
      if (this.endDate == null) {
         if (other.endDate != null) {
            return false;
         }
      }
      else if (!this.endDate.equals(other.endDate)) {
         return false;
      }
      if (this.startDate == null) {
         if (other.startDate != null) {
            return false;
         }
      }
      else if (!this.startDate.equals(other.startDate)) {
         return false;
      }
      return true;
   }

   @Override
   public int compareTo(AbstractWorkItem o) {
         if (this.getDates() != null && o.getDates() != null) {
            return o.getDates().get(0).compareTo(this.getDates().get(0));
         }
         else {
            if(this.getDates() != null) {
               return o.getStartDate().compareTo(this.getDates().get(0));
            }
            else {
               if(o.getDates() != null) {
                  return o.getDates().get(0).compareTo(this.getStartDate());
               }
               else {
                  return o.getStartDate().compareTo(this.getStartDate());
               }
            }
         }
   }
}
