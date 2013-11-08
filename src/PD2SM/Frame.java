/*
	Payday 2 Save Manager - Backup and Recovery for your favorite games saves
    Copyright (C) 2013  Michael Rosenberg

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package PD2SM;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	private JPanel contentPane, tabBpanel, tabRpanel, tabDpanel;
	private static JTextField textField;
	private static JTextArea textArea;
	private static JButton btnNewButton, btnNewButton_1;
	private static JList list, listsid;
	private static DefaultListModel<String> dlm = new DefaultListModel<String>(), dlm2 = new DefaultListModel<String>();
	private static String paydayloc = null;
	private static boolean installed = false, xsixfour = true;
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
	    listsid = new JList(dlm2);
	    scrollPane_2.setViewportView(listsid);
	    tabbedPane.addTab("Restore", tabRpanel);
	    tabRpanel.setLayout(null);
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(10, 11, 389, 82);
	    tabRpanel.add(scrollPane);
	    list = new JList(dlm);
	    list.addKeyListener(new KeyListener() 
	    {            @Override
            public void keyPressed(KeyEvent e) 
	    	{
                if (e.getKeyCode() == KeyEvent.VK_DELETE) 
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
	    btnNewButton_1.setBounds(10, 95, 389, 23);
	    tabRpanel.add(btnNewButton_1);
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
	
	public static void initializesftw()
	{
		try 
		{
			paydayloc = System.getenv("ProgramFiles(X86)") + "\\Steam\\steamapps\\Common\\Payday 2";
			File pdf = new File(paydayloc);
			if (pdf.exists())
			{
				System.out.println("Payday 2 Folder found in (X86)");
				textArea.setText(textArea.getText()+"\nPayday 2 Folder Has Been Located");
				installed = true;
				pdf = new File(paydayloc + "\\backups");
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
				xsixfour = true;
				getSteamID();
			}
			else
			{
				textArea.setText(textArea.getText()+"\nPayday 2 Folder Not Found in (X86)");
				System.out.println("Payday 2 Folder Not Found in (X86)");
			}
		}
		catch (Exception e) 
		{
			System.out.println(e);
			paydayloc = System.getenv("ProgramFiles") + "\\Steam\\steamapps\\Common\\Payday 2";
			File pdf = new File(paydayloc);
			if (pdf.exists())
			{
				 System.out.println("Payday 2 found in (X86)");
				 textArea.setText(textArea.getText()+"\nPayday 2 Has Been Located");
				 installed = true;
				 textArea.setText(textArea.getText()+"\nChecking For Backups Directory");
				 pdf = new File(paydayloc + "\\backups");
				 if(!pdf.exists())
				 {
					pdf.mkdir();
					textArea.setText(textArea.getText()+"\nNo Backup found. Creating New Backup Directory");
					backups = pdf;
				 }
				 else
				 {
					textArea.setText(textArea.getText()+"\nBackup found!");
					backups = pdf;
					getBackups();
				 }
				 xsixfour = false;
				 getSteamID();
		    }
			else
			{
				 textArea.setText(textArea.getText()+"\nPayday 2 Folder Not Found in (X86)");
				 System.out.println("Payday 2 Folder Not Found in (X86)");
			}
		}
		if (!installed)
		{
			textArea.setText(textArea.getText()+"\nPayday 2 Is Either Not Installed On Your Machine Or Is Installed To An Alternate Location. Please Fix Before Continuing");
			textField.setEnabled(false);
			btnNewButton.setEnabled(false);
			btnNewButton_1.setEnabled(false);
			list.setEnabled(false);
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
		createbak = new File(backups + "\\" + listsid.getSelectedValue() + "-" + textField.getText());
		createbak.mkdir();
		String x = null;
		if (xsixfour)
		{
			x = System.getenv("ProgramFiles(X86)") + "\\Steam\\userdata\\" + listsid.getSelectedValue() + "\\218620\\remote";
		}
		else
		{
			x = System.getenv("ProgramFiles") + "\\Steam\\userdata\\" + listsid.getSelectedValue() + "\\218620\\remote";
		}
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
		JOptionPane.showMessageDialog(null, "Backup Completed Successfully! YAY!", "Backup Complete!", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	private static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!sourceFile.exists()) {
			return;
		}
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}

	}
	
	private static void getSteamID()
	{
		String x = null;
		if (xsixfour)
		{
			x = System.getenv("ProgramFiles(X86)") + "\\Steam\\userdata\\";
		}
		else
		{
			x = System.getenv("ProgramFiles") + "\\Steam\\userdata\\";
		}
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
		if (xsixfour)
		{
			paysaveloc = System.getenv("ProgramFiles(X86)") + "\\Steam\\userdata\\" + sid + "\\218620\\remote";
		}
		else
		{
			paysaveloc = System.getenv("ProgramFiles") + "\\Steam\\userdata\\" + sid + "\\218620\\remote";
		}
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