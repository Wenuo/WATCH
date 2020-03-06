package com.smartwatch.ywatch;

public class Colors
{

	    //private String colorNumber;
		private int colorIcon;
		//private boolean isSelected = false;
		public Colors(){};

		public Colors(int colorIcon){
			this.colorIcon = colorIcon;
			//this.colorNumber=colorNumber;
		
		}


		public int getColorIcon(){
			return colorIcon;
		}
		/*public String getColorNumber(){
			return colorNumber;
		}*/
		public void setColorIcon(int colorIcon){
			this.colorIcon=colorIcon;
		}
		/*public void setColorNumber(String colorNumber){
			this.colorNumber=colorNumber;
		}*/
	
		/*public boolean isSelected() {
			return isSelected;
		}
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}*/

}
