package nl.raymondloman.resume.services;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.raymondloman.resume.domain.enums.Language;

public class DateService {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(DateService.class);
   
   public String getMonthDisplayName(Month month, Language language) {
      switch (language) {
         case ENGLISH:
            return month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

         case NEDERLANDS:
            int monthNumber = month.getValue();
            switch (monthNumber) {
               case 1:
                  return "Jan";
               case 2:
                  return "Feb";
               case 3:
                  return "Mrt";
               case 4:
                  return "Apr";
               case 5:
                  return "Mei";
               case 6:
                  return "Jun";
               case 7:
                  return "Jul";
               case 8:
                  return "Aug";
               case 9:
                  return "Sep";
               case 10:
                  return "Okt";
               case 11:
                  return "Nov";
               case 12:
                  return "Dec";

               default:
                  LOGGER.error("Found invalid month [{}]", monthNumber);
            }
         default:
            LOGGER.error("Found invalid language, using default English. Please add language to getMonthDisplayName");

            return month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
      }
      
   }

   public boolean isOverlappingPeriodAndWorkItem(LocalDate workItemStart, LocalDate workItemEnd, LocalDate periodStart, LocalDate periodEnd) {
      
      Objects.requireNonNull(workItemStart);
      Objects.requireNonNull(workItemEnd);
      Objects.requireNonNull(periodStart);
      Objects.requireNonNull(periodEnd);
      
      
      boolean workItemStartInPeriod = this.isInPeriod(periodStart, periodEnd, workItemStart);
      boolean workItemEndInPeriod = this.isInPeriod(periodStart, periodEnd, workItemEnd);
      boolean periodStartIsInWorkitem = this.isInPeriod(workItemStart, workItemEnd, periodStart);
      boolean periodEndIsInWorkItem = this.isInPeriod(workItemStart, workItemEnd, periodEnd);
      
      return  workItemStartInPeriod || workItemEndInPeriod || periodStartIsInWorkitem || periodEndIsInWorkItem ;
      
   }
   
    public boolean isInPeriod(LocalDate periodStart, LocalDate periodEnd, LocalDate someDate) {
      
      return someDate.compareTo(periodStart) >= 0 && periodEnd.compareTo(someDate) >= 0;
   }      
}
