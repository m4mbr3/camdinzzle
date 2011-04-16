

import java.util.Collection;

public class Species {

	/**
	 * @uml.property  name="name"
	 */
	private String name;

	/**
	 * Getter of the property <tt>name</tt>
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * @param name  The name to set.
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @uml.property  name="type"
	 */
	private String type;

	/**
	 * Getter of the property <tt>type</tt>
	 * @return  Returns the type.
	 * @uml.property  name="type"
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter of the property <tt>type</tt>
	 * @param type  The type to set.
	 * @uml.property  name="type"
	 */
	public void setType(String type) {
		this.type = type;
	}

	/** 
	 * @uml.property name="timeoflive"
	 */
	private int timeoflive;

	/** 
	 * Getter of the property <tt>timeoflive</tt>
	 * @return  Returns the timeoflive.
	 * @uml.property  name="timeoflive"
	 */
	public int getTimeoflive() {
		return timeoflive;
	}

	/** 
	 * Setter of the property <tt>timeoflive</tt>
	 * @param timeoflive  The timeoflive to set.
	 * @uml.property  name="timeoflive"
	 */
	public void setTimeoflive(int timeoflive) {
		this.timeoflive = timeoflive;
	}

		
		/**
		 */
		public void addDinosaur(){
		}

			
			/**
			 */
			public Species(){
			}

			/**
			 * @uml.property  name="dinosaur"
			 */
			private Dinosaur dinosaur;

			/**
			 * Getter of the property <tt>dinosaur</tt>
			 * @return  Returns the dinosaur.
			 * @uml.property  name="dinosaur"
			 */
			public Dinosaur getDinosaur() {
				return dinosaur;
			}

			/**
			 * Setter of the property <tt>dinosaur</tt>
			 * @param dinosaur  The dinosaur to set.
			 * @uml.property  name="dinosaur"
			 */
			public void setDinosaur(Dinosaur dinosaur) {
				this.dinosaur = dinosaur;
			}

			/** 
			 * @uml.property name="dinosaur1"
			 * @uml.associationEnd multiplicity="(1 -1)" inverse="species:Dinosaur"
			 * @uml.association name="make of"
			 */
			@SuppressWarnings("rawtypes")
			private Collection<?> dinosaur1 = new java.util.ArrayList();

			/** 
			 * Getter of the property <tt>dinosaur1</tt>
			 * @return  Returns the dinosaur1.
			 * @uml.property  name="dinosaur1"
			 */
			public Collection<?> getDinosaur1() {
				return dinosaur1;
			}

			/** 
			 * Setter of the property <tt>dinosaur1</tt>
			 * @param dinosaur1  The dinosaur1 to set.
			 * @uml.property  name="dinosaur1"
			 */
			public void setDinosaur1(Collection<?> dinosaur1) {
				this.dinosaur1 = dinosaur1;
			}

}
