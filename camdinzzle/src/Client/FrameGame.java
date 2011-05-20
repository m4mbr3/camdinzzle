/**
 * 
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @author Andrea
 *
 */
public class FrameGame extends JFrame implements WindowListener, ActionListener, MouseListener,Visual{

	private Dimension screenSize;
	private JPanel panel;
	private JPanel panelControl;
	final private int row = 40;
	final private int col = 40;
	private JButton[][] buttons;
	private ImageIcon iconVegetarian;
	private ImageIcon iconWater;
	private ImageIcon iconCarrion;
	private ImageIcon iconDinoCarn;
	private ImageIcon iconDinoVege;
	private ImageIcon iconDark;
	private ImageIcon iconLand;
	
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameGame(String title) throws HeadlessException{
		super(title);
		// TODO Auto-generated constructor stub
		buttons = new JButton[row][col];
		iconVegetarian = new ImageIcon("Images/vege.jpg");
		iconLand = new ImageIcon("Images/terra.jpg");
		this.setVisible(true);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(0,0);
		
		this.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
		//this.setLayout(new BorderLayout());
		this.addWindowListener(this);
		panel = new JPanel();
		panelControl = new JPanel();
		panel.setVisible(true);
		panelControl.setVisible(true);
		panel.setSize((int)screenSize.getWidth()-100, (int)screenSize.getHeight());
		panelControl.setSize(100, (int)screenSize.getHeight());
		panel.setLayout(new GridLayout(40,40));
		panelControl.setLayout(null);
		for (int i=0; i < row; i++)
		{
			for(int j=0; j < col; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].setVisible(true);
				//buttons[i][j].setBackground(Color.blue);
				buttons[i][j].setSize(((int)screenSize.getWidth()-100)/col, ((int)screenSize.getHeight()/row));
				buttons[i][j].setName(i+"X"+j);
				buttons[i][j].addActionListener(this);
				buttons[i][j].addMouseListener(this);
				buttons[i][j].setBorder(null);
				//buttons[i][j].setToolTipText();
				buttons[i][j].setIcon(iconLand);				
				panel.add(buttons[i][j]);
				System.out.println("Creata la cella "+i+"X"+ j);
			}
		}
	
		this.add(panel);
		this.add(panelControl);
		//this.add(panelControl, BorderLayout.EAST);
		this.repaint();
		
		this.validate();
		}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameGame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args)
	{
		FrameGame m = new FrameGame("fc");
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		this.repaint();
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.repaint();
		for(int i = 0; i < row; i++)
			for(int j =0; j< col;j++)
				{
					/*if()
					{
						System.out.println("Fuck "+i+"X"+j);
					}*/
				}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() instanceof JButton)
			((JButton) arg0.getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() instanceof JButton)
			((JButton) arg0.getComponent()).setBorder(null);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawMap(String msg) {
		// TODO Auto-generated method stub
		ArrayList<String> mapList = ClientMessageBroker.manageGeneralMap(msg);
		int j=0;
		int z=0;
		for(int i = 2; i< mapList.size(); i++)
		{
			if(j==col)
			{
				j=0;
				z++;
			}
			if(mapList.get(i).compareTo("b")==0)
				buttons[j][z].setIcon(iconDark);
			else if(mapList.get(i).compareTo("v")==0)
				buttons[j][z].setIcon(iconVegetarian);
			else if(mapList.get(i).compareTo("t")==0)
				buttons[j][z].setIcon(iconLand);
			else if(mapList.get(i).compareTo("a")==0)
				buttons[i][z].setIcon(iconWater);
			j++;
		}
	}

	@Override
	public void drawDinoZoom(String dinoId, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawDinoList(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawTime(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawConnectionState(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawRanking(String msg) {
		// TODO Auto-generated method stub
		
	}
}
