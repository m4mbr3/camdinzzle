

public abstract class Dinosaur {

	/**
	 * @uml.property  name="age"
	 */
	private int age = 0;

	/**
	 * Getter of the property <tt>age</tt>
	 * @return  Returns the age.
	 * @uml.property  name="age"
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Setter of the property <tt>age</tt>
	 * @param age  The age to set.
	 * @uml.property  name="age"
	 */
	public void setAge(int age) {
		this.age = age;
	}

		
		/**
		 */
		public int growUp(){
			return 0;
		}

			
			/**
			 */
			public void dead(){
			}

				
				/**
				 */
				public void newEgg(){
				}

					
					/**
					 */
					public abstract void move();

						
						/**
						 */
						public abstract void fight();

							
							/**
							 */
							public abstract void eat();
							
						
					

}
