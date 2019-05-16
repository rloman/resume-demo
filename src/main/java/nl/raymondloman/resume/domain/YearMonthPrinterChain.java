package nl.raymondloman.resume.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.raymondloman.resume.domain.assignment.Assignment;
import nl.raymondloman.resume.domain.employer.Employer;
import nl.raymondloman.resume.domain.other.Node;
import nl.raymondloman.resume.domain.training.Training;
import nl.raymondloman.resume.services.DateService;
import nl.raymondloman.resume.visitor.Visitor;

public class YearMonthPrinterChain extends Node  {
   
   private LocalDate periodStart;
   private LocalDate periodEnd;
   
   private List<Assignment> assignments = new ArrayList<>();
   private List<Training> trainingsItems = new ArrayList<>();
   private List<Employer> employers = new ArrayList<>();
   
   private YearMonthPrinterChain chain;
   
   private DateService dateService = new DateService();
   
   public YearMonthPrinterChain(LocalDate periodStart, LocalDate periodMaxOfCompleteChain) {
      this.periodStart = periodStart;
      this.periodEnd = periodStart.plusMonths(1).minusDays(1);
      
      if(periodStart.compareTo(periodMaxOfCompleteChain) < 0) {
         this.chain = new YearMonthPrinterChain(periodStart.plusMonths(1), periodMaxOfCompleteChain);
      }
   }
   
   
   public void add(AbstractWorkItem workItem) {
      
      
      if(dateService.isOverlappingPeriodAndWorkItem(workItem.getStartDate(), workItem.getEndDate(), periodStart, periodEnd)) {
         if(workItem instanceof Assignment) {
            this.assignments.add((Assignment) workItem);
         }
         else {
            if(workItem instanceof Training) {
               this.trainingsItems.add((Training)workItem);
            }
            else {
               if(workItem instanceof Employer) {
                  this.employers.add((Employer) workItem);
               }
            }
         }
      }
      if(this.chain != null) {
         this.chain.add(workItem);
      }
   }
   
   public boolean isEmpty() {
      return this.assignments.isEmpty() && this.employers.isEmpty() && this.trainingsItems.isEmpty();
   }

   @Override
   public String toString() {
      return "YearMonthPrinterChain [periodStart=" + periodStart + ", periodEnd=" + periodEnd + ", workItems=" + assignments + ", chain=" + chain + "]";
   }

   @Override
   public void accept(Visitor visitor) {
      visitor.visit(this);
      
   }

   
   public LocalDate getPeriodStart() {
      return periodStart;
   }

   
   public YearMonthPrinterChain getChain() {
      return chain;
   }

   
   public List<Training> getTrainingsItems() {
      Collections.sort(this.trainingsItems, (a,b) -> a.getStartDate().compareTo(b.getStartDate()));
      return trainingsItems;
   }

   
   public List<Employer> getEmployers() {
      Collections.sort(this.employers, (a,b) -> a.getStartDate().compareTo(b.getStartDate()));
      return employers;
   }

   
   public List<Assignment> getAssignments() {
      Collections.sort(this.assignments, (a,b) -> a.getStartDate().compareTo(b.getStartDate()));
      return assignments;
   }

}
