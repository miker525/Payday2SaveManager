package PD2SM;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame {
	private static final long serialVersionUID = -2650279427914996809L;
	private JPanel contentPane, tabBpanel, tabRpanel, tabDpanel;
	private static JTextField textField;
	private static JTextArea textArea;
	private static JButton btnNewButton, btnNewButton_1,btnNewButton_2;
	private static JList<String> list, listsid;
	private static DefaultListModel<String> dlm = new DefaultListModel<String>(), dlm2 = new DefaultListModel<String>();
	private static String paydayloc = null;
	private static String paydaySaveloc = null;
	private static File backups, createbak;

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() {
				try 
				{
					Frame frame = new Frame();
					frame.setVisible(true);
					textArea.setText("Welcome To The Payday 2 Save Manager");
					initializesftw();
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	public Frame() 
	{
		//Frame Info
		setTitle("Payday 2 Save Manager");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 315);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//Creating Content Panes
		JPanel panel = new JPanel();
		panel.setBounds(10, 0, 414, 105);
		contentPane.add(panel);
		tabRpanel = new JPanel();
		tabDpanel = new JPanel();
		//Loading Payday 2 image and icon
		BufferedImage image = null, icon = null;
	    try
	    {
	        image = ImageIO.read(getClass().getResource("/PD2SM/payday.PNG"));
	        icon = ImageIO.read(getClass().getResource("/PD2SM/icon.png"));
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    Image scaledImage = image.getScaledInstance(panel.getWidth(),panel.getHeight(),Image.SCALE_SMOOTH);
	    JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
	    panel.add(picLabel);
	    this.setIconImage(icon);
	     //Tab information
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    tabbedPane.setBounds(10, 116, 414, 149);
	    
	    tabBpanel = new JPanel();
	    tabbedPane.addTab("Back Up", tabBpanel);
	    tabBpanel.setLayout(null);
	    
	    textField = new JTextField();
	    textField.setBounds(93, 11, 306, 20);
	    tabBpanel.add(textField);
	    textField.setColumns(10);
	    
	    JLabel lblBackupName = new JLabel("Backup Name:");
	    lblBackupName.setBounds(10, 14, 94, 14);
	    tabBpanel.add(lblBackupName);
	    
	    btnNewButton = new JButton("Create Backup");
	    btnNewButton.addActionListener(new ActionListener() 
	    {
	    	public void actionPerformed(ActionEvent arg0) 
	    	{
	    		createBackup();
	    	}
	    });
	    btnNewButton.setBounds(10, 90, 389, 20);
	    tabBpanel.add(btnNewButton);
	    JScrollPane scrollPane_2 = new JScrollPane();
	    scrollPane_2.setBounds(10, 35, 389, 54);
	    tabBpanel.add(scrollPane_2);
	    listsid = new JList<String>(dlm2);
	    scrollPane_2.setViewportView(listsid);
	    tabbedPane.addTab("Restore", tabRpanel);
	    tabRpanel.setLayout(null);
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(10, 11, 389, 82);
	    tabRpanel.add(scrollPane);
	    list = new JList<String>(dlm);
	    list.addKeyListener(new KeyListener() 
	    {           
            public void keyPressed(KeyEvent e) 
	    	{
                if (e.getKeyCode() == KeyEvent.VK_DELETE) 
                {
                	deleteDialog();
                }
            }
            public void keyReleased(KeyEvent e) { }
            public void keyTyped(KeyEvent e) { }
        });
	    scrollPane.setViewportView(list);
	    btnNewButton_1 = new JButton("Restore Save");
	    btnNewButton_1.addActionListener(new ActionListener() 
	    {
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		restoreSave();
	    	}
	    });
	    btnNewButton_1.setBounds(10, 95, 194, 23);
	    tabRpanel.add(btnNewButton_1);
	    
	    btnNewButton_2 = new JButton("Delete Save");
	    btnNewButton_2.addActionListener(new ActionListener() 
	    {
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		deleteDialog();
	    	}
	    });
	    btnNewButton_2.setBounds(205, 95, 194, 23);
	    tabRpanel.add(btnNewButton_2);
	    
	    
	    tabbedPane.addTab("Debug", tabDpanel);
	    tabDpanel.setLayout(null);
	    JScrollPane scrollPane_1 = new JScrollPane();
	    scrollPane_1.setBounds(10, 11, 389, 99);
	    tabDpanel.add(scrollPane_1);
	    textArea = new JTextArea();
	    textArea.setWrapStyleWord(true);
	    textArea.setEditable(false);
	    scrollPane_1.setViewportView(textArea);
	    contentPane.add(tabbedPane);
	}
	private static void deleteDialog() {
    	Object[] options = {"Yes, please", "No way!"};
		int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete the selected backup?", "WOAH!?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (n == JOptionPane.YES_OPTION) 
		{
			try 
			{
				File f = new File(backups+"\\"+list.getSelectedValue());
				delete(f);
				textArea.setText(textArea.getText()+"\nDeleting Backup "+ f.getName());
				getBackups();
			}
			catch (IOException ioe)
			{
				System.out.println(ioe);
				textArea.setText(textArea.getText()+"\nDeleting Backup Failed. Error Below\n"+ ioe);
			}
		}
		else
		{
			textArea.setText(textArea.getText()+"\nDeletion Cancelled");
		}
	}
	
	public static boolean initializesWithPath(String path) {
		if(path!=null) {
			paydayloc=path+"\\steamapps\\Common\\Payday 2";
			File pdf = new File(paydayloc);
			if (pdf.exists())
			{
				System.out.println("Payday 2 Folder found in "+path);
				textArea.setText(textArea.getText()+"\nPayday 2 Folder Has Been Located");
				pdf = new File(path + "\\backups");
				textArea.setText(textArea.getText()+"\nChecking For Backups Directory");
				if(!pdf.exists())
				{
					pdf.mkdir();
					textArea.setText(textArea.getText()+"\nNo Backup found. Creating New Backup Directory");
					backups = pdf;
				}
				else
				{
					textArea.setText(textArea.getText()+"\nBackup Directory found!");
					backups = pdf;
					getBackups();
				}
				paydaySaveloc=path + "\\userdata\\";
				getSteamID();
				return true;
			}
		}
		return false;
	}
	public static void initializesftw()
	{
		boolean paydayFound=false;

		String steamPath=getPath();
		textField.setEnabled(false);
		btnNewButton.setEnabled(false);
		btnNewButton_1.setEnabled(false);
		btnNewButton_2.setEnabled(false);
		list.setEnabled(false);
		//Try cache
		paydayFound=initializesWithPath(steamPath);
		if(!paydayFound) {
			//Try x86
			steamPath=System.getenv("ProgramFiles(X86)")+"\\Steam";
			paydayFound=initializesWithPath(steamPath);
			if(!paydayFound) {
				//Try x64
				textArea.setText(textArea.getText()+"\nPayday 2 Folder Not Found in (X86)");
				steamPath=System.getenv("ProgramFiles")+"\\Steam";
				paydayFound=initializesWithPath(steamPath);
				if(!paydayFound) {
					//Try userInput
					textArea.setText(textArea.getText()+"\nPayday 2 Folder Not Found in (Program Files)");
					steamPath = (String)JOptionPane.showInputDialog(
					                    null,
					                    "Steam Location:",
					                    "Where in the world is Dallas?",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    null,
					                    System.getenv("ProgramFiles")+"\\Steam");
					paydayFound=initializesWithPath(steamPath);
					//Bah, not even the user knows where it is.  Give up.
					if(!paydayFound) {
						textArea.setText(textArea.getText()+"\nPayday 2 Is Either Not Installed On Your Machine Or Is Installed To An Alternate Location. Please Fix Before Continuing");
						JOptionPane.showMessageDialog(null, "Could not find payday at "+steamPath+"\\steamapps\\Common\\Payday 2","Unable to start", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
		if(paydayFound) {
			textField.setEnabled(true);
			btnNewButton.setEnabled(true);
			btnNewButton_1.setEnabled(true);
			btnNewButton_2.setEnabled(true);
			list.setEnabled(true);
			savePath(steamPath);
		}
	}
	private static String getPath() {
		BufferedReader reader=null;
		File f=new File("payday2SaveManagerPath.txt");
		String path=null;
		try {
			if(f.exists()) {
				reader=new BufferedReader(new FileReader(f));
				path=reader.readLine();
			}
		} catch(IOException ioe) {
			System.out.println(ioe);
			textArea.setText(textArea.getText()+ "\nError Encountered. Error Message Below\n"+ioe);
		} finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch(IOException e) {}
			}
		}
		return path;
	}
	private static void savePath(String path) {
		BufferedWriter writer=null;
		File f=new File("payday2SaveManagerPath.txt");
		try {
			if(f.exists())
				f.delete();
			writer=new BufferedWriter(new FileWriter(f));
			writer.write(path);
		} catch(IOException ioe) {
			System.out.println(ioe);
			textArea.setText(textArea.getText()+ "\nError Encountered. Error Message Below\n"+ioe);
		} finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch(IOException e) {}
			}
		}
	}
	
	public static void getBackups()
	{
		dlm.clear();
		File[] bups = backups.listFiles(new FileFilter() 
		{  
		    public boolean accept(File pathname) 
		    {  
		        return pathname.isDirectory();  
		    }  
		});  
		for (File bup : bups) 
		{  
			dlm.addElement(bup.getName().toString());
		} 
		textArea.setText(textArea.getText()+"\nAdding Backups To Backup List");
	}

	public static void createBackup()
	{
		String selectedProfile=listsid.getSelectedValue();
		if(selectedProfile==null && dlm2.size()==1)
			selectedProfile=dlm2.firstElement();
		if(selectedProfile!=null) {
			createbak = new File(backups + "\\" + selectedProfile + "-" + textField.getText());
			createbak.mkdir();
			String x = null;
			x = paydaySaveloc + selectedProfile + "\\218620\\remote";
			
			File fx = new File(x);
			File[] lx = fx.listFiles();
			for (File f: lx)
			{
				try
				{
					File bak = new File(createbak.getPath() + "\\" + f.getName());
					copyFile(f, bak);
					textArea.setText(textArea.getText()+"\n"+f.getName()+" Has Been Copied to Backup "+createbak.getPath());
				}
				catch (IOException ioe)
				{
					System.out.println(ioe);
					textArea.setText(textArea.getText()+ "\nError Encountered. Error Message Below\n"+ioe);
				}
			}
			getBackups();
			JOptionPane.showMessageDialog(null, "Backup Completed Successfully! YAY!", "Backup Complete!", JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, "Select a profile", "No backup", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!sourceFile.exists()) {
			return;
		}
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileInputStream sourceStream=null;
		FileOutputStream destinationStream=null;
		FileChannel source = null;
		FileChannel destination = null;
		try {
			sourceStream=new FileInputStream(sourceFile);
			destinationStream=new FileOutputStream(destFile);
			source=sourceStream.getChannel();
			destination = destinationStream.getChannel();
			if (destination != null && source != null) {
				destination.transferFrom(source, 0, source.size());
			}
		} finally {
			if(sourceStream!=null) {
				if (source != null) {
					source.close();
				}
				sourceStream.close();
			}
			if(destinationStream!=null) {
				if (destination != null) {
					destination.close();
				}
				destinationStream.close();
			}
		}
	}
	
	private static void getSteamID()
	{
		String x = paydaySaveloc;
		
		File fx = new File(x);
		File[] lx = fx.listFiles();
		for (File f: lx)
		{
			dlm2.add(0, f.getName());
			textArea.setText(textArea.getText()+"\n"+f.getName()+" Account Found and added to Steam IDs");
		}
	}
	
	private static void restoreSave()
	{
		String paysaveloc = null;
		String sid = ((String) list.getSelectedValue()).split("\\-")[0];
		paysaveloc = paydaySaveloc + sid + "\\218620\\remote";
		
		File paysavedir = new File(paysaveloc); //
		File[] filecount = paysavedir.listFiles();
		if (filecount.length>0)
		{
			Object[] options = {"Yes, please", "No way!"};
			int n = JOptionPane.showOptionDialog(null, "Would You like to Overwrite your current save", "WOAH!?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (n == JOptionPane.YES_OPTION) 
			{
				File seldir = new File(backups.getPath() +"\\" + list.getSelectedValue());
				File[] ffound = seldir.listFiles();
				for (File l: ffound)
				{
					try 
					{
						File fr = new File(paysaveloc+"\\"+l.getName());
						copyFile(l, fr);
						textArea.setText(textArea.getText()+"\n"+l.getName() +" Restored");
					} 
					catch (IOException e) 
					{
						System.out.println(e);
						textArea.setText(textArea.getText()+ "\nError Encountered. Error Message Below\n"+e);
					}
				}
				JOptionPane.showMessageDialog(null, "Restore Completed Successfully! YAY!", "Restore Complete!", JOptionPane.INFORMATION_MESSAGE);
				
			}
			else if (n == JOptionPane.NO_OPTION) 
			{
				textArea.setText(textArea.getText()+"\nRestore Cancelled");
				System.out.println("Restore Cancelled");
			}

		}
		else
		{
			File seldir = new File(backups.getPath() +"\\" + list.getSelectedValue());
			File[] ffound = seldir.listFiles();
			for (File l: ffound)
			{
				try 
				{
					File fr = new File(paysaveloc+"\\"+l.getName());
					copyFile(l, fr);
					textArea.setText(textArea.getText()+"\n"+l.getName() +" Restored");
				} 
				catch (IOException e) 
				{
					System.out.println(e);
					textArea.setText(textArea.getText()+ "\nError Encountered. Error Message Below\n"+e);
				}
			}
			JOptionPane.showMessageDialog(null, "Restore Completed Successfully! YAY!", "Restore Complete!", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory())
	    	{
	    		if(file.list().length==0)
	    		{
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " + file.getAbsolutePath());
	    		}
	    		else
	    		{
	        	   String files[] = file.list();
	        	   for (String temp : files) 
	        	   {
	        	      File fileDelete = new File(file, temp);
	        	     delete(fileDelete);
	        	   }
	        	   if(file.list().length==0)
	        	   {
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " + file.getAbsolutePath());
	        	   }
	    		}
	    	}
	    	else
	    	{
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
	
}
