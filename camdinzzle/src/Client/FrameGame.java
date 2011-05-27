/**
 * 
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.ScrollPane;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


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
	private ImageIcon iconVegetation;
	private ImageIcon iconWater;
	private ImageIcon iconCarrion;
	private ImageIcon iconDinoCarn;
	private ImageIcon iconDinoVege;
	private ImageIcon iconDark;
	private ImageIcon iconLand;
	private ImageIcon iconVegetationDisable;
	private ImageIcon iconWaterDisable;
	private ImageIcon iconCarrionDisable;
	private ImageIcon iconDinoCarnDisable;
	private ImageIcon iconDinoVegeDisable;
	private ImageIcon iconLandDisable;
	private JList dinoList;
	private JTextArea dinoState;
	private JList playerList;
	private JPanel commandButtons;
	private JButton[] commandDinoButton;
	private JButton[] commandGameButton;
	private JPanel timer;
	private JFrame ranking;
	private JTable ranking1;
	private Client client;
	
	private final int widthControlPanel=300;
	private final int visibleRowCountDinoList=6;
	private final int visibleRowCountPlayerList=8;
	private final Font fontDinoList = new Font("Serif", Font.PLAIN, 24); 
	private final Font fontDinoState = new Font("Serif", Font.PLAIN, 18);
	private final Font fontPlayerState = new Font("Serif", Font.PLAIN, 24);
	
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameGame(String title,Client client) throws HeadlessException{
		super(title);
		this.client=client;
		buttons = new JButton[row][col];
		iconVegetation = new ImageIcon("Images/vege.jpg");
		iconLand = new ImageIcon("Images/terra.jpg");
		iconWater = new ImageIcon("Images/acqua.jpg");
		iconDark = new ImageIcon("Images/red.jpg");
		iconCarrion = new ImageIcon("Images/carrion.jpg");
		iconCarrionDisable = new ImageIcon("Images/CarrionDisable.jpg");
		iconLandDisable  = new ImageIcon("Images/terraDisable.jpg");
		iconWaterDisable  = new ImageIcon("Images/acquaDisable.jpg");
		iconVegetationDisable  = new ImageIcon("Images/vegeDisable.jpg");
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
		panel.setBorder(null);
//		panelControl.setBorder(null);
		panel.setPreferredSize(new Dimension((int)screenSize.getWidth()-widthControlPanel, (int)screenSize.getHeight()));
		panelControl.setPreferredSize(new Dimension(widthControlPanel, (int)screenSize.getHeight()));
		panel.setLayout(new GridLayout(row,col));
		panelControl.setLayout(new FlowLayout());
		commandButtons = new JPanel();
		commandButtons.setVisible(true);
		commandButtons.setBorder(null);
		commandButtons.setPreferredSize(new Dimension(widthControlPanel-10, ((int)screenSize.getHeight()/14*4)));
		timer = new JPanel();
		timer.setVisible(true);
		timer.setBorder(null);
		timer.setPreferredSize(new Dimension(widthControlPanel-10, (int)screenSize.getHeight()/14));
		for (int i=0; i < row; i++)
		{
			for(int j=0; j < col; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].setVisible(true);
				//buttons[i][j].setBackground(Color.blue);
				buttons[i][j].setSize(((int)screenSize.getWidth()-widthControlPanel)/col, ((int)screenSize.getHeight()/row));
				buttons[i][j].setName(i+"X"+j);
				buttons[i][j].addActionListener(this);
				buttons[i][j].addMouseListener(this);
				buttons[i][j].setBorder(null);
				//buttons[i][j].setToolTipText();
				buttons[i][j].setIcon(iconLand);
				buttons[i][j].setEnabled(false);
//				buttons[i][j].setDisabledIcon(iconLandDisable);
				panel.add(buttons[i][j]);
//				System.out.println("Creata la cella "+i+"X"+ j);
			}
		}	
		
		this.add(panel,BorderLayout.WEST);
		//this.add(panelControl);
		this.add(panelControl, BorderLayout.EAST);
//		this.repaint();
		dinoList = new JList();
		panelControl.add(new JScrollPane(dinoList));
		dinoState = new JTextArea();
		panelControl.add(dinoState);
		panelControl.add(commandButtons);
		panelControl.add(new JScrollPane(playerList));
		panelControl.add(timer);
		
		
		this.validate();
		}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameGame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO ???
	}
	public static void main(String[] args)
	{
		Client c = new Client("ciao");
		FrameGame m = new FrameGame("fc",c);
		String msg = "@mappaGenerale,{40,40},[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][t][t][t][t][t][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][t][t][t][t][t][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][v][v][d][t][v][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][v][t][t][v][t][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][t][t][v][t][a][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b]";
		//String msg = "@mappaGenerale,{40,40},[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][v][t][t][t][v][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][t][a][t][t][t][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][t][a][d][v][v][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][t][t][t][t][v][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][v][t][t][t][t][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b];[b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b][b]";
		ArrayList<String> mapList = ClientMessageBroker.manageGeneralMap(msg);
		m.drawMap(mapList);
		msg = "@vistaLocale,{23,15},{5,5},[a][a][v,10][t][a];[v,10][t][t][v,10][t];[v,10][v,10][d,a - 1][t][v,10];[t][c,22][t][t][c,22];[t][t][t][t][t]";
		mapList = ClientMessageBroker.manageDinoZoom(msg);
		m.drawDinoZoom("a - 1", mapList);
		msg = "@listaDinosauri,a - 1,a - 2,a - 3,a - 4,a - 5,a - 6,a - 7,a - 8,a - 9";
		String[] msgDinoList = ClientMessageBroker.manageDinoList(msg);
		m.drawDinoList(msgDinoList);
		msg = "@statoDinosauro,a,a,Carnivorous,{23,3},1,1000,25";
		String[] msgDinoState = ClientMessageBroker.manageDinoState(msg);
		m.drawDinoState("a - 1", msgDinoState);
		m.drawCommandButtons();
		msg = "@listaGiocatori,a,s,d,f,g,h,j,k";
		String[] msgPlayerList = ClientMessageBroker.managePlayerList(msg);
		m.drawPlayerList(msgPlayerList);
		m.drawTime();
		m.repaint();
	}
	
	public void startFrameGame(Client client)
	{
		 FrameGame game = new FrameGame("Isola dei Dinosauri", client);
		 game.drawMap(client.getConnManager().mappaGenerale());
		 String[] msgDinoList = client.getConnManager().listaDinosauri();
		 game.drawDinoList(msgDinoList);
		 for(int i=0; i<msgDinoList.length; i++)
		 {
			 game.drawDinoZoom(msgDinoList[i], client.getConnManager().vistaLocale(msgDinoList[i]));
		 }
		 game.drawDinoState(msgDinoList[0], client.getConnManager().statoDinosauro(msgDinoList[0]));
		 game.drawCommandButtons();
		 game.drawPlayerList(client.getConnManager().listaGiocatori());
		 game.drawTime();
		 game.repaint();
		 
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

				}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if(arg0.getComponent().equals(commandDinoButton[0]))
		{
			//TODO chiamare movimento
		}
		if(arg0.getComponent().equals(commandDinoButton[1]))
		{
			//TODO chiamare cresci dinosauro
		}
		if(arg0.getComponent().equals(commandDinoButton[2]))
		{
			//TODO ciamare deponi uovo
			client.getConnManager().deponiUovo(null);
		}
		if(arg0.getComponent().equals(commandGameButton[0]))
		{
			//TODO chiamare classifica creare popup
			
			ArrayList<String> classifica = client.getConnManager().classifica();
			drawRanking(classifica);
		}
		if(arg0.getComponent().equals(commandGameButton[1]))
		{
			//TODO chiamare passa turno
		}
		if(arg0.getComponent().equals(commandGameButton[2]))
		{
			//TODO chiamre esci partita
		}
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
	/**
	 * inizializza la mappa generale con le carateristiche inviategli nel msg
	 * @param msg
	 */
	public boolean drawMap(ArrayList<String> mapList) {
//		ArrayList<String> mapList = ClientMessageBroker.manageGeneralMap(msg);
		if(mapList!=null)
		{
			int j=0;
			int z=0;
			for(int i = 2; i< mapList.size(); i++)
			{
				if(z==col)
				{
					z=0;
					j++;
				}
				buttons[j][z].setEnabled(false);
				if(mapList.get(i).compareTo("b")==0)
					{
						buttons[j][z].setIcon(iconDark);
						buttons[j][z].setDisabledIcon(iconDark);
					}
				else if(mapList.get(i).compareTo("v")==0)
					buttons[j][z].setDisabledIcon(iconVegetationDisable);
				else if(mapList.get(i).compareTo("t")==0)
					buttons[j][z].setDisabledIcon(iconLandDisable);
				else if(mapList.get(i).compareTo("a")==0)
					buttons[j][z].setDisabledIcon(iconWaterDisable);
				else if(mapList.get(i).compareTo("d")==0)
					buttons[j][z].setDisabledIcon(iconDark);
				z++;
			}
			return true;
		}
		else
			return false;
	}

	@Override
	/**
	 * Sovrascrive la mappa generale con la vista locale di un dinosauro
	 * @param dinoId non serve
	 * @param msg : messaggio contenente vista locale
	 */
	public void drawDinoZoom(String dinoId, ArrayList<String> mapList) 
	{
//		ArrayList<String> mapList = ClientMessageBroker.manageDinoZoom(msg);
		int startRow = Integer.parseInt(mapList.get(0));
		int startCol = Integer.parseInt(mapList.get(1));
		int maxRow = startRow - Integer.parseInt(mapList.get(2));
		int maxCol = Integer.parseInt(mapList.get(3)) + startCol;
		
		int row=startRow;
		int col=startCol;
		String[] energySplit = new String [2];
//		System.out.println(mapList.size());
		
		for(int i=4; i<mapList.size(); i++)
		{
			if((row>=0)&&(row<this.row)&&(col>=0)&&(col<this.col))
			{
				if(col==maxCol)
				{
					col=startCol;
					row--;
				}
				if(mapList.get(i).compareTo("b")==0)
				{
					buttons[row][col].setIcon(iconDark);
				}
				else if(mapList.get(i).compareTo("t")==0)
				{
					buttons[row][col].setIcon(iconLand);
				}
				else if(mapList.get(i).compareTo("a")==0)
				{
					buttons[row][col].setIcon(iconWater);
				}
					
				else if(mapList.get(i).indexOf(",") != -1)
				{
					energySplit = ((String)mapList.get(i)).split(",");
					if(energySplit[0].compareTo("d")==0)
					{
						buttons[row][col].setIcon(iconDark);
					}
					else if(energySplit[0].compareTo("v")==0)
					{
						buttons[row][col].setIcon(iconVegetation);
					}
					else if(energySplit[0].compareTo("c")==0)
					{
						buttons[row][col].setIcon(iconCarrion);
					}
				}
				col++;
			}
			for(int rowEnable=startRow+1; rowEnable>maxRow-1; rowEnable--)
			{
				for(int colEnable=startCol-1; colEnable<maxCol+1; colEnable++)
				{
					if((rowEnable>=0)&&(rowEnable<this.row)&&(colEnable>=0)&&(colEnable<this.col))
					{
						buttons[rowEnable][colEnable].setEnabled(true);
					}
				}
			}

			
			
		}
		
		
		
	}

	@Override
	/**
	 * prende la lista dinosauri e la stampa in alto a destra
	 */
	public void drawDinoList(String[] msgDinoList) {
		
//		String[] msgDinoList = ClientMessageBroker.manageDinoList(msg);
		dinoList = new JList(msgDinoList);
		dinoList.setVisibleRowCount(visibleRowCountDinoList);
		dinoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dinoList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			/**
			 * classe di ascolto inner anonima
			 */
			public void valueChanged(ListSelectionEvent e) 
			{
			//TODO inserire chiamata dello stato dino
				drawDinoState(dinoList.getSelectedValue().toString(), client.getConnManager().statoDinosauro(dinoList.getSelectedValue().toString()));
				dinoState.repaint();
				
			}
		});
		dinoList.setVisible(true);
		dinoList.setPreferredSize(new Dimension(widthControlPanel-25,(int)screenSize.getHeight()/14*4));
//		dinoList.setAlignmentX(LEFT_ALIGNMENT);
		dinoList.setFont(fontDinoList);	
		

		
	}

	@Override
	public void drawTime() {
		// TODO countdown 2m
		timer.setBackground(Color.GREEN);
		panelControl.add(timer);
		
		
	}

	@Override
	public void drawConnectionState(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawRanking(ArrayList<String> classifica) {
		// TODO creare popup con classifica
		ranking = new JFrame("Classifica");
		ranking.setVisible(true);
		ranking.setPreferredSize(new Dimension(300, 500));
		Object[] columnNames = new String[4];
		columnNames[0] = "USERNAME";
		columnNames[1] = "NOME SPECIE";
		columnNames[2] = "PUNTEGGIO";
		columnNames[3] = "IN PARTITA";
		Object[][] rowData = new String [classifica.size()/4][4];
		int j=0,z=0;
		for(int i=0;i<classifica.size();i++)
		{
			if(z>=4)
			{
				j++;
				z=0;
			}
			rowData[j][z] = classifica.get(i);
			z++;
		}
		ranking1 = new JTable(rowData, columnNames);
		ranking1.setVisible(true);
		ranking.add(ranking1);
		
	}

	@Override
	public void drawDinoState(String dinoId, String[] msgDinoState) {
		// TODO stampare stato dinosauro
//		String[] msgDinoState = ClientMessageBroker.manageDinoState(msg);
		String newMsgDinoState="";
		
		newMsgDinoState += "Dinosaur's state " + dinoId + " of player " + msgDinoState[0] + ":\n";
		newMsgDinoState += "	race: " + msgDinoState[1] + "\n";
		newMsgDinoState += "	type: " + msgDinoState[2] + "\n";
		newMsgDinoState += "	dinosaur's position: \n          	    row:" + msgDinoState[3] + "\n          	    col:" + msgDinoState[4] + "\n";
		newMsgDinoState += "	dimension: " + msgDinoState[5] + "\n";
		
		// Se il dinosauro appartiene al giocatore allora ci sono delle informazioni aggiuntive
		if(msgDinoState.length > 6)
		{
			newMsgDinoState += "	energy: " + msgDinoState[6] + "\n";
			newMsgDinoState += "	round lived: " + msgDinoState[7] + "\n";
		}
		
		dinoState = new JTextArea(newMsgDinoState);
		dinoState.setVisible(true);
		dinoState.setPreferredSize(new Dimension(widthControlPanel,(int)screenSize.getHeight()/14*2));
		dinoState.setFont(fontDinoState);
		dinoState.setEditable(false);
		
		
	}
	@Override
	public void drawPlayerList(String[] msgPlayerList)
	{
//		String[] msgPlayerList = ClientMessageBroker.managePlayerList(msg);
		playerList = new JList(msgPlayerList);
		playerList.setVisibleRowCount(visibleRowCountPlayerList);
		playerList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			/**
			 * classe di ascolto inner anonima
			 */
			public void valueChanged(ListSelectionEvent e) 
			{
			//TODO inserire chiamata aggiornamento lista giocatori
					
				
			}
		});
		playerList.setVisible(true);
		playerList.setPreferredSize(new Dimension(widthControlPanel-5,(int)screenSize.getHeight()/14*2));
		playerList.setFont(fontPlayerState);	
		panelControl.add(new JScrollPane(playerList));
	}
	 public void drawCommandButtons()
	 {
//TODO  
//		-aggiungere azioni ai bottoni

		 commandDinoButton = new JButton[3];
		 commandDinoButton[0] = new JButton("Muovi Dinosauro");
		 commandDinoButton[1] = new JButton("Cresci Dinosauro");
		 commandDinoButton[2] = new JButton("Deponi Uovo");
		 commandGameButton = new JButton[3];
		 commandGameButton[0] = new JButton("Classifica");
		 commandGameButton[1] = new JButton("Passa Turno");
		 commandGameButton[2] = new JButton("Esci dalla Partita");
		 
		 for(int i=0;i<3;i++)
		 {
			 commandDinoButton[i].setPreferredSize(new Dimension(widthControlPanel-10,(int)screenSize.getHeight()/14/5*3));
			 commandDinoButton[i].setVisible(true);
			 commandDinoButton[i].setEnabled(true);
			 commandDinoButton[i].addMouseListener(this);
			 commandButtons.add(commandDinoButton[i]);
		 }
		 for(int i=0;i<3;i++)
		 {
			 commandGameButton[i].setPreferredSize(new Dimension(widthControlPanel - 10, (int)screenSize.getHeight()/14/5*3));
			 commandGameButton[i].setVisible(true);
			 commandGameButton[i].setEnabled(true);
			 commandGameButton[i].addMouseListener(this);
			 commandButtons.add(commandGameButton[i]);
		 }
		 
		 

	 }
	 public void errorMessage()
	 {
		 
	 }
	 

}
