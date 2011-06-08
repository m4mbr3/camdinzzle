package Client;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class TimeOutOptionPane extends JOptionPane
{

	private static final long serialVersionUID = 1L;

	public TimeOutOptionPane() {
        super();
    }

    final static int PRESET_TIME = 28;

    public static int showTimeoutDialog(Component parentComponent, Object message, final String title, int optionType,
            int messageType, Object[] options, final Object initialValue) {
        JOptionPane pane = new JOptionPane(message, messageType, optionType, null, options, initialValue);
         
        pane.setInitialValue(initialValue);

        final JDialog dialog = pane.createDialog(parentComponent, title);
        
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        pane.selectInitialValue();
        new Thread() {
            public void run() {
                try {
                    for (int i=PRESET_TIME; i>=0; i--) {
                        Thread.sleep(980);
                        if (dialog.isVisible() && i<300) {
                            dialog.setTitle("Hai " + i + " secondi per confermare");
                        }
                    }
                    if (dialog.isVisible()) {
                        dialog.setVisible(false);
                    }
                } catch (Throwable t) {
                }
            }
        }.start();
        dialog.setVisible(true);

        Object selectedValue = pane.getValue();
        if (selectedValue.equals("uninitializedValue")) {
            selectedValue = initialValue;
        }
        if (selectedValue == null)
            return CLOSED_OPTION;
        if (options == null) {
            if (selectedValue instanceof Integer)
                return ((Integer) selectedValue).intValue();
            return CLOSED_OPTION;
        }
        for (int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
            if (options[counter].equals(selectedValue))
                return counter;
        }
        return CLOSED_OPTION;
    }

}
