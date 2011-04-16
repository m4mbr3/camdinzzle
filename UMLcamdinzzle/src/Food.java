

public interface Food {

	/** 
	 * @return  Returns the power.
	 * @uml.property  name="power"
	 */
	public int getPower();

	/** 
	 * @return  Returns the maxpower.
	 * @uml.property  name="maxpower"
	 */
	public int getMaxpower();

	/** 
	 * Setter of the property <tt>power</tt>
	 * @param power  The power to set.
	 * @uml.property  name="power"
	 */
	public void setPower(int power);

	/** 
	 * Setter of the property <tt>maxpower</tt>
	 * @param maxpower  The maxpower to set.
	 * @uml.property  name="maxpower"
	 */
	public void setMaxpower(int maxpower);

		
		/**
		 */
		public abstract void rebirth();
		

}
