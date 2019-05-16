package nl.raymondloman.resume.domain.education;

import java.time.LocalDate;

import nl.raymondloman.resume.Constants;
import nl.raymondloman.resume.domain.i18n.StringI18N;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.visitor.Visitor;

public class Education extends Node implements Comparable<Education> {

   private String education;

   private StringI18N descriptionI18N;
   private String description = Constants.EMPTYSTRING;

   private String organization = Constants.EMPTYSTRING;
   private LocalDate startDate;
   private LocalDate endDate;
   private Boolean diploma;

   public String getEducation() {
      return this.education;
   }

   public void setEducation(String education) {
      this.education = education;
   }

   public String getOrganization() {
      return this.organization;
   }

   public void setOrganization(String organization) {
      this.organization = organization;
   }

   public LocalDate getStartDate() {
      return this.startDate;
   }

   public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
   }

   public LocalDate getEndDate() {
      return this.endDate;
   }

   public void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
   }

   public Boolean isDiploma() {
      return this.diploma;
   }

   public void setDiploma(boolean diploma) {
      this.diploma = diploma;
   }

   @Override
   public int compareTo(Education other) {
      // sort descending on startdate
      return other.getStartDate().compareTo(this.getStartDate());
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public StringI18N getDescriptionI18N() {
      return this.descriptionI18N;
   }

   public void setDescriptionI18N(StringI18N descriptionI18N) {
      this.descriptionI18N = descriptionI18N;
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
      if (!(obj instanceof Education)) {
         return false;
      }
      Education other = (Education) obj;
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
   public void accept(Visitor visitor) {
      visitor.visit(this);

   }
}