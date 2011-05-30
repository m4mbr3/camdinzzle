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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.Option;


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
	private JScrollPane ranking2;
	private Client client;
	private String dinoId;
	private int flag=0;
	
	private final int widthControlPanel=300;
	private final int visibleRowCountDinoList=6;
	private final int visibleRowCountPlayerList=8;
	private final int maxAttempt = 5;
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
				buttons[i][j].setName(i+","+j +";");
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
//		this.add(panelControl);
		this.add(panelControl, BorderLayout.EAST);
//		this.repaint();
//		dinoList = new JList();
//		panelControl.add(new JScrollPane(dinoList));
//		dinoState = new JTextArea();
//		panelControl.add(dinoState);
//		panelControl.add(commandButtons);
//		panelControl.add(new JScrollPane(playerList));
//		panelControl.add(timer);
		
		
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
/*	public static void main(String[] args)
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
//		startFrameGame(c);
	}
*/	
	public static void startFrameGame(Client client)
	{
		 FrameGame game = new FrameGame("Isola dei Dinosauri", client);
		 game.drawMap(client.getConnManager().mappaGenerale());
		 //TODO gestire lista vuota
		 String[] msgDinoList = client.getConnManager().listaDinosauri();
		 game.drawDinoList(msgDinoList);
		 game.drawDinoState(msgDinoList[0], client.getConnManager().statoDinosauro(msgDinoList[0]));
		 game.drawCommandButtons();
		 game.drawPlayerList(client.getConnManager().listaGiocatori());
		 game.drawTime();
		 game.repaint();
		 
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		this.repaint();
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//this.repaint();
		for(int i = 0; i < row; i++)
			for(int j =0; j< col;j++)
				{

				}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		//TODO controllare messaggio!!!
		for(int i=0; i<3; i++)
		{
			commandDinoButton[i].setEnabled(false);
		}
		/*MOVIMENTO
		 * dopo aver cliccato una cella con un dinosauro si attiva il tasto movimento,
		 * dopo aver selezionato il tasto di destinazione si fa la chiamata a server
		 * se il messaggio non è null ridisegna la mappa
		 * se @no viene creato un popup
		 */
		if(arg0.getComponent().equals(commandDinoButton[0]))
		{
//TODO eliminare bottone movimento e mettere aggiorna lista giocatori in fondo
			
		}
		/*CRESCI DINOSAURO
		 * dopo aver cliccato una cella con un dinosauro si attiva il tasto
		 * se il messaggio non è null viene aggiornato la descrizione dello stato dino appena cresciuto
		 */
		if(arg0.getComponent().equals(commandDinoButton[1]))
		{
			String[] groUpDino = client.getConnManager().cresciDinosauro(dinoId);
			if(groUpDino==null)
			{
				errorMessage();
			}
			else
			{
				drawDinoState(dinoId,client.getConnManager().statoDinosauro(dinoId));
				
			}	
		}
		/*DEPONI UOVO
		 *  dopo aver cliccato una cella con un dinosauro si attiva il tasto
		 *  dopo aver inviato la richiesta
		 *  se la risposta è diversa da null controlla il messaggio
		 *  se non è no ridisegna la mappa altrimenti crea un popup
		 */
		if(arg0.getComponent().equals(commandDinoButton[2]))
		{	
			String[] newEgg = client.getConnManager().deponiUovo(dinoId);
			if(newEgg==null)
			{
				errorMessage();
			}
			else
			{
				if(newEgg[0].equals("no"))
				{
					errorMessageServer(newEgg);
				}
				else
				{
					drawMap(client.getConnManager().mappaGenerale());
				}
			}

		}
		/*CLASSIFICA
		 * tasto sempre attivo
		 * se la risposta non è null e non no, crea un popup con la classifica
		 * altrimenti stampa l'errore
		 */
		if(arg0.getComponent().equals(commandGameButton[0]))
		{
			ArrayList<String> classifica = client.getConnManager().classifica();
			if(classifica!=null)
			{
				if(!classifica.contains("no"))
				{
					drawRanking(classifica);
				}
				else
				{
					errorMessageServer(classifica);
				}
			}
			else
			{
				errorMessage();
			}
		}
		/*PASSA TURNO
		 * tasto sempre attivo
		 * fa la chiamata a server
		 * se la risposta è null o no crea popup con errore
		 * altrimenti nulla
		 */
		if(arg0.getComponent().equals(commandGameButton[1]))
		{
			String[] check = client.getConnManager().passaTurno();
			if(check==null)
			{
				errorMessage();
			}
			else
			{
				if(check[0].equals("no"))
				{
					errorMessageServer(check);
				}
			}
		}
		/*ESCI DALLA PARTITA
		 * fa la chiamata a server
		 * se la risposta è null o no crea popup con errore
		 */
		if(arg0.getComponent().equals(commandGameButton[2]))
		{
			String check = client.getConnManager().uscitaPartita();
			if(check==null)
			{
				errorMessage();
			}
			else
			{
				if(check.equals("no"))
				{
					errorMessageServer(check);
				}
				else
				{
					this.setVisible(false);
					FrameGameManager gameIntro = new FrameGameManager("ManagerPanel", client);
				}
			}
		}
		/*BOTTONI MAPPA
		 * se viene cliccato un bottone contenente un dinosauro del giocatore allora
		 * attiva i pulsanti MOVIMENTO, CRESCI DINOSAURO, DEPONI UOVO
		 */
		if(arg0.getComponent() instanceof JButton)
		{
			if(flag==1)
			{
				String nameCell = arg0.getComponent().getName();
				if(nameCell.contains(";"))
				{
					String[] option = {"yes","no"};
					int opt = JOptionPane.showOptionDialog(panel, "Vuoi muovere il dinosauro", "Muovi Dinosauro", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, "yes");
					if(opt==0)
					{
						String[] nameDest = nameCell.split(";");
						String[] newNameDest = nameDest[0].split(",");
						String[] check = client.getConnManager().muoviDinosauro(dinoId, newNameDest[0], newNameDest[1]);
						if(check==null)
						{
							errorMessage();
						}
						else
						{
							if(!check[0].equals("no"))
							{
								drawMap(client.getConnManager().mappaGenerale());
								drawDinoState(dinoId, client.getConnManager().statoDinosauro(dinoId));
							}
							else
							{
								errorMessageServer(check);
							}
						}
					}
				}
				flag=0;
			}
			else if(((String)arg0.getComponent().getName()).contains("-"))
			{
				String[] idDino = arg0.getComponent().getName().split(";");
				dinoId = idDino[1];
				for(int i=0; i<3; i++)
				{
					commandDinoButton[i].setEnabled(true);
				}
				flag=1;
			}
		}
	}

	@Override
	/*
	 * se il cursore entra in un bottone della mappa si crea un bordo nero
	 */
	public void mouseEntered(MouseEvent arg0) 
	{
		if ((arg0.getComponent() instanceof JButton)&&(!(arg0.getComponent().equals(commandDinoButton[0])))&&(!(arg0.getComponent().equals(commandDinoButton[1])))&&(!(arg0.getComponent().equals(commandDinoButton[2])))&&(!(arg0.getComponent().equals(commandGameButton[0])))&&(!(arg0.getComponent().equals(commandGameButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[2]))))
			((JButton) arg0.getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));
	}

	@Override
	/*
	 * quando il cursore esce dal bottone della mappa il bordo si cancella
	 */
	public void mouseExited(MouseEvent arg0) 
	{
		if ((arg0.getComponent() instanceof JButton)&&(!(arg0.getComponent().equals(commandDinoButton[0])))&&(!(arg0.getComponent().equals(commandDinoButton[1])))&&(!(arg0.getComponent().equals(commandDinoButton[2])))&&(!(arg0.getComponent().equals(commandGameButton[0])))&&(!(arg0.getComponent().equals(commandGameButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[2]))))
			((JButton) arg0.getComponent()).setBorder(null);
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
				
	}
	
	@Override
	/**
	 * inizializza la mappa generale con le carateristiche inviategli nella mapList
	 * richiede le mappe locali di tutti i dinosauri del giocatore e le stampa su quella generale
	 * se il server risponde con errato messaggio mapList è null e viene rifatta la richiesta per un numero massimo uguale a maxAttempt
	 * @param mapList
	 */
	public void drawMap(ArrayList<String> mapList) 
	{
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
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "dark");
						buttons[j][z].setIcon(iconDark);
						buttons[j][z].setDisabledIcon(iconDark);
					}
				else if(mapList.get(i).compareTo("v")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "vegetation");
						buttons[j][z].setDisabledIcon(iconVegetationDisable);
					}
				else if(mapList.get(i).compareTo("t")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
						buttons[j][z].setDisabledIcon(iconLandDisable);
					}
				else if(mapList.get(i).compareTo("a")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "water");
						buttons[j][z].setDisabledIcon(iconWaterDisable);
					}
				else if(mapList.get(i).compareTo("d")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "dinosaur");
						buttons[j][z].setDisabledIcon(iconDark);
					}
				z++;
			}
			String[] msgDinoList = client.getConnManager().listaDinosauri();		
			for(int i=0; i<msgDinoList.length; i++)
			{
				drawDinoZoom(msgDinoList[i], client.getConnManager().vistaLocale(msgDinoList[i]));
			}
			
		}
		else
		{
			int count = 0;
			ArrayList<String> msg;
			do
			{
				msg = client.getConnManager().mappaGenerale();
				count++;
			}
			while((msg==null)&&(count<maxAttempt));
			if(msg==null)
			{
				errorMessageServer();
			}
		}
	}

	@Override
	/**
	 * Sovrascrive la mappa generale con la vista locale di un dinosauro
	 * nel caso in cui il messaggio è null viene rifatta la richiesta per maxAttempt volte
	 * se mapList contiente no viene creato un popup di errore
	 * @param dinoId 
	 * @param mapList : messaggio contenente vista locale
	 */
	public void drawDinoZoom(String dinoId, ArrayList<String> mapList) 
	{
//		ArrayList<String> mapList = ClientMessageBroker.manageDinoZoom(msg);
		if(mapList!=null)
		{
			if(!mapList.get(0).equals("no"))
			{
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
							buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "dark");
							buttons[row][col].setIcon(iconDark);
						}
						else if(mapList.get(i).compareTo("t")==0)
						{
							buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "land");
							buttons[row][col].setIcon(iconLand);
						}
						else if(mapList.get(i).compareTo("a")==0)
						{
							buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "water");
							buttons[row][col].setIcon(iconWater);
						}
							
						else if(mapList.get(i).indexOf(",") != -1)
						{
							energySplit = ((String)mapList.get(i)).split(",");
							if(energySplit[0].compareTo("d")==0)
							{
								buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + energySplit[1]);
								buttons[row][col].setToolTipText("id dinosauro: " + energySplit[1]);
								buttons[row][col].setIcon(iconDark);
							}
							else if(energySplit[0].compareTo("v")==0)
							{
								buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "vegetation");
								buttons[row][col].setToolTipText("energia vegetazione: " + energySplit[1]);
								buttons[row][col].setIcon(iconVegetation);
							}
							else if(energySplit[0].compareTo("c")==0)
							{
								buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "carrion");
								buttons[row][col].setToolTipText("energia carogna: " + energySplit[1]);
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
			else
			{
				errorMessageServer(mapList);
			}
		}
		else
		{
			int count = 0;
			ArrayList<String> msg;
			do
			{
				msg = client.getConnManager().vistaLocale(dinoId);
				count++;
			}
			while((msg==null)&&(count<maxAttempt));
			if(msg==null)
			{
				errorMessageServer();
			}
		}
	}

	@Override
	/**
	 * prende la lista dinosauri e la stampa in alto a destra
	 * se msgDinoList è null fa la richiesta per un numero pari a maxAttempt dopo di che crea un popup di errore
	 * se il messaggio contiene no crea un popup di errore
	 * se il messaggio è valido stampa la lista dei dinosauri
	 * quando viene cliccato un dinosauro viene aggiornato il campo stato dinosauro
	 */
	public void drawDinoList(String[] msgDinoList) 
	{
		
//		String[] msgDinoList = ClientMessageBroker.manageDinoList(msg);
		if(msgDinoList!=null)
		{
			if(!msgDinoList[0].equals("no"))
			{
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
						drawDinoState(dinoList.getSelectedValue().toString(), client.getConnManager().statoDinosauro(dinoList.getSelectedValue().toString()));
						panelControl.repaint();
						
					}
				});
				dinoList.setVisible(true);
				dinoList.setPreferredSize(new Dimension(widthControlPanel-25,(int)screenSize.getHeight()/14*4));
				dinoList.setFont(fontDinoList);	
				panelControl.add(new JScrollPane(dinoList));
			}
			else
			{
				errorMessageServer(msgDinoList);
			}
		}
		else
		{
			int count = 0;
			String[] msg;
			do
			{
				msg = client.getConnManager().listaDinosauri();
				count++;
			}
			while((msg==null)&&(count<maxAttempt));
			if(msg==null)
			{
				errorMessageServer();
			}
		}
	}

	@Override
	public void drawTime() 
	{
		// TODO countdown 2m
		timer.setBackground(Color.GREEN);
		panelControl.add(timer);
		
		
	}

	@Override
	public void drawConnectionState(String msg) 
	{

		
	}

	@Override
	/**STAMPA CLASSIFICA
	 * crea un popup con la classifica
	 */
	public void drawRanking(ArrayList<String> classifica) {
		// TODO sistemare intestazione colonne
			ranking = new JFrame("Classifica");
			ranking.setVisible(true);
			ranking.setSize(new Dimension(350, 500));
			ranking2 = new JScrollPane();
			String[] columnNames = {"USERNAME","NOME SPECIE","PUNTEGGIO","IN PARTITA"};
			String[][] rowData = new String [classifica.size()/4][4];
			int j=0,z=0;
			for(int i=1;i<classifica.size();i++)
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
			ranking2.getViewport().add(ranking1);
			ranking.add(ranking2);
		
	}

	@Override
	/**STAMPA LO STATO DINOSAURO
	 * 
	 */
	public void drawDinoState(String dinoId, String[] msgDinoState) 
	{
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
		panelControl.add(dinoState);
		
		
	}
	@Override
	/**stampa la lista giocatori
	 * 
	 */
	public void drawPlayerList(String[] msgPlayerList)
	{
		String[] newMsgPlayerList = new String[8];
		for(int i=0;i<msgPlayerList.length-1; i++)
			newMsgPlayerList[i]=msgPlayerList[i+1];
		playerList = new JList(newMsgPlayerList);
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
	/**
	 * istanzia i bottoni per comandare i dinosauri e il gioco
	 */
	 public void drawCommandButtons()
	 {
		 commandDinoButton = new JButton[3];
		 commandDinoButton[0] = new JButton("Muovi Dinosauro");
		 commandDinoButton[0].setName("Muovi Dinosauro");
		 commandDinoButton[1] = new JButton("Cresci Dinosauro");
		 commandDinoButton[1].setName("Cresci Dinosauro");
		 commandDinoButton[2] = new JButton("Deponi Uovo");
		 commandDinoButton[2].setName("Deponi Uovo");
		 commandGameButton = new JButton[3];
		 commandGameButton[0] = new JButton("Classifica");
		 commandGameButton[0].setName("Classifica");
		 commandGameButton[1] = new JButton("Passa Turno");
		 commandGameButton[1].setName("Passa Turno");
		 commandGameButton[2] = new JButton("Esci dalla Partita");
		 commandGameButton[2].setName("Esci dalla Partita");
		 
		 for(int i=0;i<3;i++)
		 {
			 commandDinoButton[i].setPreferredSize(new Dimension(widthControlPanel-10,(int)screenSize.getHeight()/14/5*3));
			 commandDinoButton[i].setVisible(true);
			 commandDinoButton[i].setEnabled(false);
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
		 panelControl.add(commandButtons);
		 

	 }
	 /**
	  * messaggio di errore in caso di messaggio di risposta illeggibile
	  */
	 public void errorMessage()
	 {
		 JOptionPane.showMessageDialog(panel, "Azione non compiuta", "Error", JOptionPane.ERROR_MESSAGE);
	 }
	 
	 /**
	  * messaggio di errore con l'Arrey di stringhe ricevuta dal server
	  * @param msg
	  */
	 public void errorMessageServer(String[] msg)
	 {
		 JOptionPane.showMessageDialog(panel, msg);
	 }
	 
	 /**
	  * messaggio di errore in caso di comunicazione non corretta con il server
	  */
	 public void errorMessageServer()
	 {
		 JOptionPane.showMessageDialog(panel, "Errore nella comunicazione col server");
	 }
	 
	 /**
	  * messaggio di errore con l'ArrayList ricevuto dal server
	  * @param msg
	  */
	 public void errorMessageServer(ArrayList<String> msg)
	 {
		 JOptionPane.showMessageDialog(panel, msg);
	 }
	 
	 /**
	  * messaggio di errore con la striga ricevuto dal server
	  * @param msg
	  */
	 public void errorMessageServer(String msg)
	 {
		 JOptionPane.showMessageDialog(panel, msg);
	 }
	 
	 

}
