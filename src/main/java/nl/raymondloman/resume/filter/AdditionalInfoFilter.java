package nl.raymondloman.resume.filter;

public class AdditionalInfoFilter {

   private boolean on = true;
   private boolean hobbies = true;
   private boolean otherCharacteristics = true;

   public boolean isOn() {
      return this.on;
   }

   public void setOn(boolean on) {
      this.on = on;
   }

   public boolean isHobbies() {
      return this.hobbies;
   }

   public void setHobbies(boolean hobbies) {
      this.hobbies = hobbies;
   }

   public boolean isOtherCharacteristics() {
      return this.otherCharacteristics;
   }

   public void setOtherCharacteristics(boolean otherCharacteristics) {
      this.otherCharacteristics = otherCharacteristics;
   }
}