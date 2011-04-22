

public class Client {

		
		/**
		 */
		public void sendCommand(){
		}

			
			/**
			 */
			public void createCommand(){
			}


			/**
			 * @uml.property  name="visual"
			 * @uml.associationEnd  aggregation="composite" inverse="client:Visual"
			 * @uml.association  name="show by"
			 */
			private Visual visual;


			/**
			 * Getter of the property <tt>visual</tt>
			 * @return  Returns the visual.
			 * @uml.property  name="visual"
			 */
			public Visual getVisual() {
				return visual;
			}


			/**
			 * Setter of the property <tt>visual</tt>
			 * @param visual  The visual to set.
			 * @uml.property  name="visual"
			 */
			public void setVisual(Visual visual) {
				this.visual = visual;
			}


				
				/**
				 */
				public Client(){
				}

}
