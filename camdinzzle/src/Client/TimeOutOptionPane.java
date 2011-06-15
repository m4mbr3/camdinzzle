package Client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
/**
 * Classe utilizzata per la visuzlizzazione del pannello di conferma del turno.
 */
public class TimeOutOptionPane extends JOptionPane
{
	private static final long serialVersionUID = 1L;
	final static int PRESET_TIME = 27;

	public TimeOutOptionPane() 
	{
        super();
	}

    public static int showTimeoutDialog(Component parentComponent, Object message, final String title, int optionType,
            int messageType, Object[] options, final Object initialValue) 
    {
    	try
    	{
	        JOptionPane pane = new JOptionPane(message, messageType, optionType, null, options, initialValue);
	         
	        pane.setInitialValue(initialValue);
	
	        final JDialog dialog = pane.createDialog(parentComponent, title);
	        
	        //Thread.sleep(400);
	        
	        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	        dialog.setTitle("Round confirm");
	        
	        pane.selectInitialValue();
	        
	        
	        Timer t = new Timer(27000, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					dialog.setVisible(false);
				}
			});
	        t.start();
	        
	        /*
	        new Thread() 
	        {
	            public void run() 
	            {
	                try 
	                {
	                    for (int i=PRESET_TIME; i>=0; i--)
	                    {
	                        Thread.sleep(980);
	                        if (dialog.isVisible() && i<300) 
	                        {
	                            dialog.setTitle("Hai " + i + " secondi per confermare");
	                        }
	                    }
	                    if (dialog.isVisible()) 
	                    {
	                        dialog.setVisible(false);
	                    }
	                }
	                catch (Throwable t) 
	                {
	                	
	                }
	            }
	        }.start();*/
	        dialog.setVisible(true);
	
	        Object selectedValue = pane.getValue();
	        if (selectedValue.equals("uninitializedValue")) 
	        {
	            return 2;
	        }
	        if (options == null) 
	        {
	            if (selectedValue instanceof Integer)
	                return ((Integer) selectedValue).intValue();
	            return CLOSED_OPTION;
	        }
	        for (int counter = 0, maxCounter = options.length; counter < maxCounter; counter++)
	        {
	            if (options[counter].equals(selectedValue))
	                return counter;
	        }
	        return CLOSED_OPTION;
    	}
    	catch(Exception t)
    	{
    		return CLOSED_OPTION;
    	}
    }
}
