package nl.raymondloman.resume.filter;


public class ChronologischAppendixFilter {
   
   private int startYear;
   private int startMonth;
   private int endYear;
   private int endMonth;
   
   public ChronologischAppendixFilter(int startMonth, int startYear, int endMonth, int endYear) {
      this.startMonth = startMonth;
      this.startYear = startYear;
      this.endYear = endYear;
      this.endMonth = endMonth;
   }
   
   public int getStartYear() {
      return startYear;
   }
   
   public int getStartMonth() {
      return startMonth;
   }
   
   public int getEndYear() {
      return endYear;
   }
   
   public int getEndMonth() {
      return endMonth;
   }
}
