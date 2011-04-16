

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

								
								/**
								 */
								public Dinosaur(){
								}


								/** 
								 * @uml.property name="species"
								 * @uml.associationEnd multiplicity="(1 1)" inverse="dinosaur1:Species"
								 * @uml.association name="make of"
								 */
								private Species species = new Species();

								/** 
								 * Getter of the property <tt>species</tt>
								 * @return  Returns the species.
								 * @uml.property  name="species"
								 */
								public Species getSpecies() {
									return species;
								}

								/** 
								 * Setter of the property <tt>species</tt>
								 * @param species  The species to set.
								 * @uml.property  name="species"
								 */
								public void setSpecies(Species species) {
									this.species = species;
								}
							
						
					

}
