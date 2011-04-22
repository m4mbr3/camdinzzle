

public interface Visual {

		
		/**
		 */
		public abstract void drawMap();

			
			/**
			 */
			public abstract void drawDinoZoom();


				
				/**
				 */
				public abstract void drawDinoList();


					
					/**
					 */
					public abstract void drawTime();


						
						/**
						 */
						public abstract void drawConnectionState();


							
							/**
							 */
							public abstract void showRanking();


							/**
							 * @return  Returns the client.
							 * @uml.property  name="client"
							 * @uml.associationEnd  inverse="visual:Client"
							 * @uml.association  name="show by"
							 */
							public Client getClient();


							/**
							 * Setter of the property <tt>client</tt>
							 * @param client  The client to set.
							 * @uml.property  name="client"
							 */
							public void setClient(Client client);
							
						
					
				
			
		

}
