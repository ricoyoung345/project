package trans;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import trans.digest.DigestUtils;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.*;

public class MainFrame extends JFrame {
	static boolean FILE_SERVICE = false;
	static String beforeAppend;
	static String response;
	static Socket socket;
	static BufferedReader in;
	static PrintWriter out;
	static ResourceDownloadPanel rdp;
	String serverIP;
	int serverPort;
	String shareFolder;
	
	JPanel contentPane;
	JLabel jShareFolderL = new JLabel();
	JLabel jServerIPL= new JLabel();
	JLabel jServerPortL= new JLabel();
	JLabel jlblIP = new JLabel();
	
	JTextField jServerIP = new JTextField();
    JTextField jServerPort = new JTextField();
    JTextField jShareFolder = new JTextField();
    
    DefaultTableModel table_data_model = new DefaultTableModel();
	JTable resource_table = new JTable(table_data_model);
	JScrollPane JSP= new JScrollPane(resource_table);
   
    JButton jbtnSend = new JButton();
    JButton jbtnSetting = new JButton();
    JButton jbtnConnServer = new JButton();
    JButton jbtnRefreshSrc = new JButton();
    JButton jbtnRequestSrc = new JButton();
    JButton jbtnQuit = new JButton();
    JButton jbtnBrowserFolder = new JButton();
    
    // 显示信息？？？
    JTabbedPane jtpTransFile = new JTabbedPane();
    TransFileManager tfm = new TransFileManager(jtpTransFile);
	private Socket comSocket;
    
    public MainFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
	private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        this.getContentPane().setBackground(new Color(206, 227, 249));
        setSize(new Dimension(800, 500));
        setTitle("P2P文件共享传输软件");
        this.addWindowListener(new MainFrame_this_windowAdapter(this));
        jbtnSend.setBackground(new Color(236, 247, 255));
        jbtnSend.setBounds(new Rectangle(14, 14, 85, 25));
        jbtnSend.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnSend.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnSend.setText("发送本机文件");
        jbtnSend.addActionListener(new MainFrame_jbtnSend_actionAdapter(this));
        
        jlblIP.setText("本机IP：");
        jlblIP.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jlblIP.setBounds(new Rectangle(197, 18, 180, 16));
        
        jShareFolderL.setText("共享目录：");
        jShareFolderL.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jShareFolderL.setBounds(new Rectangle(14, 54, 80, 16));
        
        jShareFolder.setBackground(Color.white);
        jShareFolder.setBorder(BorderFactory.createEtchedBorder());
        jShareFolder.setText("");
        jShareFolder.setBounds(new Rectangle(100, 54, 180, 21));
        
        jbtnBrowserFolder.setBackground(new Color(236, 247, 255));
        jbtnBrowserFolder.setBounds(new Rectangle(300, 54, 85, 25));
        jbtnBrowserFolder.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnBrowserFolder.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnBrowserFolder.setText("浏览");
        jbtnBrowserFolder.addActionListener(new MainFrame_jbtnBrowserFolder_actionAdapter(this));
        
        jbtnSetting.setBackground(new Color(236, 247, 255));
        jbtnSetting.setBounds(new Rectangle(106, 14, 73, 25));
        jbtnSetting.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnSetting.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnSetting.setText("设置");
        jbtnSetting.addActionListener(new MainFrame_jbtnSetting_actionAdapter(this));
        
        jServerIPL.setText("服务器IP：");
        jServerIPL.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jServerIPL.setBounds(new Rectangle(400, 18, 80, 16));
        jServerPortL.setText("服务器端口：");
        jServerPortL.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jServerPortL.setBounds(new Rectangle(600, 18, 80, 16));
        
        jServerIP.setBackground(Color.white);
        jServerIP.setBorder(BorderFactory.createEtchedBorder());
        jServerIP.setText("");
        jServerIP.setBounds(new Rectangle(480, 14, 110, 21));
        
        jServerPort.setBackground(Color.white);
        jServerPort.setBorder(BorderFactory.createEtchedBorder());
        jServerPort.setText("7777");
        jServerPort.setBounds(new Rectangle(680, 14, 100, 21));
        
        jbtnConnServer.setBackground(new Color(236, 247, 255));
        jbtnConnServer.setBounds(new Rectangle(400, 54, 85, 25));
        jbtnConnServer.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnConnServer.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnConnServer.setText("连接服务器");
        jbtnConnServer.addActionListener(new MainFrame_jbtnConnServer_actionAdapter(this));
        
        jbtnRefreshSrc.setBackground(new Color(236, 247, 255));
        jbtnRefreshSrc.setBounds(new Rectangle(500, 54, 85, 25));
        jbtnRefreshSrc.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnRefreshSrc.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnRefreshSrc.setText("刷新资源");
        jbtnRefreshSrc.addActionListener(new MainFrame_jbtnRefreshSrc_actionAdapter(this));
        jbtnRefreshSrc.setVisible(false);
        
        jbtnRequestSrc.setBackground(new Color(236, 247, 255));
        jbtnRequestSrc.setBounds(new Rectangle(600, 54, 85, 25));
        jbtnRequestSrc.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnRequestSrc.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnRequestSrc.setText("下载资源");
        jbtnRequestSrc.addActionListener(new MainFrame_jbtnRequestSrc_actionAdapter(this));
        jbtnRequestSrc.setVisible(false);
        
        jbtnQuit.setBackground(new Color(236, 247, 255));
        jbtnQuit.setBounds(new Rectangle(700, 54, 85, 25));
        jbtnQuit.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
        jbtnQuit.setBorder(BorderFactory.createRaisedBevelBorder());
        jbtnQuit.setText("关闭连接");
        jbtnQuit.addActionListener(new MainFrame_jbtnQuit_actionAdapter(this));
        jbtnQuit.setVisible(false);
        
        jtpTransFile.setBackground(new Color(206, 227, 249));
        jtpTransFile.setBounds(new Rectangle( 0, 84, 386, 406));
        
        resource_table.setBackground(new Color(236, 247, 255));
        resource_table.setBounds(new Rectangle(400, 84, 396, 412));
        JSP.setBackground(new Color(236, 247, 255));
        JSP.setBounds(new Rectangle(400, 84, 396, 412));
        JSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        contentPane.setBackground(new Color(206, 227, 249));
        contentPane.setToolTipText("");
        contentPane.add(jServerIPL);
        contentPane.add(jServerPortL);
        contentPane.add(jServerIP);
        contentPane.add(jServerPort);
        contentPane.add(jtpTransFile);
        contentPane.add(jbtnSend);
        contentPane.add(jbtnSetting);
        contentPane.add(jbtnConnServer);
        contentPane.add(jbtnRefreshSrc);
        contentPane.add(jbtnRequestSrc);
        contentPane.add(jbtnQuit);
        contentPane.add(jlblIP);
        //contentPane.add(resource_table);
        contentPane.add(jShareFolderL);
        contentPane.add(jShareFolder);
        contentPane.add(jbtnBrowserFolder);
        contentPane.add(JSP);
        table_data_model.addColumn("序号");
        table_data_model.addColumn("资源名称");
        table_data_model.addColumn("资源大小");
        // 文件传输管理线程在这里只运行一次
        // 要运行多次的是SocketThread线程
        tfm.start();
        byte [] ip = get_IPAdress();
        jlblIP.setText("本机IP："+(ip[0]&0xff)+"."+(ip[1]&0xff)+"."+(ip[2]&0xff)+"."+(ip[3]&0xff));
    }

    public byte [] get_IPAdress() throws SocketException{
    	
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                	addresses.nextElement().getHostAddress().getBytes();
                    return addresses.nextElement().getAddress();
                }
            }
        } catch (SocketException e) {
        	System.out.println( e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("resource")
	public void jbtnConnServer_actionPerformed(ActionEvent e) {
        if(jServerIP.getText().trim().equals("")||jServerPort.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this,"服务器IP或端口号有误！");
            return;
        }
        if(jShareFolder.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this,"请设置文件共享目录！");
            return;
        }
    	
        serverIP=jServerIP.getText();
        serverPort=Integer.parseInt(jServerPort.getText());
        shareFolder = jShareFolder.getText();
        Global.path = shareFolder;
        
        rdp = new ResourceDownloadPanel(jtpTransFile);
        jtpTransFile.addTab("资源下载...", rdp);
        rdp.jResourceDownloadStatus.setText("正在与对方建立连接...");
        try{
        	//String beforeAppend;
			//String response;
			//Socket socket;
	    	//BufferedReader in;
	    	//PrintWriter out;
			
			socket = new Socket(serverIP, serverPort);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//服务器返回的消息
			out = new PrintWriter(socket.getOutputStream(), false);//发送给服务器的消息

			// 打印服务器返回的消息
			beforeAppend = rdp.jResourceDownloadStatus.getText();
			rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+in.readLine());

			// 发送握手消息"CONNECT"，开始握手
			out.print("CONNECT");
			out.flush();
			response = in.readLine();

			// 接收确认信息
			if(!response.equals("ACCEPT"))
			{
				JOptionPane.showMessageDialog(this,"[错误] >> "+"\n向服务端发送的握手信息未能接收到正确的确认包");
				jtpTransFile.remove(jtpTransFile.getSelectedIndex());
				return;
			}
			else
			{
				beforeAppend = rdp.jResourceDownloadStatus.getText();
				rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"\033[1;32m[成功] >>\033[0m 成功连接到Napd服务器 " 
				+ serverIP + ":" + serverPort);
			}

			File folder = new File(shareFolder);
			File[] files = folder.listFiles();
			FileInputStream f_stream;
			String filename;
			String filehash;
			String filesize;
			beforeAppend = rdp.jResourceDownloadStatus.getText();
			rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[信息] 正在为工作目录 " + shareFolder + " 建立文件索引...\n");
			int index_total = 0;
			
			for(int i = 0; i < files.length; i++)
			{
				if(files[i].isFile())
				{
					filename = files[i].getName();
					f_stream = new FileInputStream(files[i]);
					filehash = DigestUtils.md5Hex(f_stream);
					f_stream.close();
					filesize = String.valueOf(files[i].length());
					
					out.print("ADD " + filename + " " + filehash + " " + filesize);
					out.flush();
					response = in.readLine();
					
					if(!response.equals("OK")){
						JOptionPane.showMessageDialog(this,"[错误] >> " + response.substring(6));
						jtpTransFile.remove(jtpTransFile.getSelectedIndex());
						return;
					}
					else
					{
						beforeAppend = rdp.jResourceDownloadStatus.getText();
						rdp.jResourceDownloadStatus.setText(beforeAppend+". ");
						index_total++;
					}
				}
			}
			beforeAppend = rdp.jResourceDownloadStatus.getText();
			rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"\n\033[1;32m[成功] >>\033[0m 成功添加 " 
			+ index_total + " 个文件信息到服务器");
			
			jbtnRefreshSrc.setVisible(true);
			jbtnQuit.setVisible(true);
			
			// 开启文件服务器线程
			if(!FILE_SERVICE){
				Runnable run = new peer_server(this);
				Thread thread = new Thread(run);
				thread.start();
				FILE_SERVICE = true;
			}
        }
        catch (Exception err){
        	JOptionPane.showMessageDialog(this,"[错误] >> "+err);
        	jtpTransFile.remove(jtpTransFile.getSelectedIndex());
			return;
		}
    }
    
    
    public void jbtnSetting_actionPerformed(ActionEvent e) {
        SettingDialog sd=new SettingDialog(this);
        sd.jtfPort.setText(String.valueOf(tfm.port));
        sd.jspnThreadNum.setValue(new Integer(tfm.maxThreadNum));
        // show用来显示新窗口
        sd.show();
        // 如果参数设置成功
        if(sd.flag){
            if(tfm.port!=sd.port){
                tfm.setPort(sd.port);
            }
            tfm.setMaxThreadNum(sd.threadNum);
        }
    }

    public void jbtnSend_actionPerformed(ActionEvent e) {
        TransFileDialog tfd=new TransFileDialog(this);
        tfd.show();
        // 文件传输开始的话
        if(tfd.flag){
            for(int i=0;i<tfd.files.length;i++){
                tfm.sendFile(tfd.serverName,tfd.port,tfd.files[i].getAbsolutePath(),tfd.message);
            }
        }
    }
    
    public void jbtnBrowserFolder_actionPerformed(ActionEvent e) {
    	JFileChooser jfc=new JFileChooser();
        jfc.setDialogTitle("请选择共享文件夹");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = jfc.showOpenDialog(this);
        if(JFileChooser.APPROVE_OPTION == result){
        	File file = jfc.getSelectedFile();
        	if(!file.isDirectory()){
        		JOptionPane.showMessageDialog(this,"你选择的目录不存在！");
                return;
        	}
        	else{
        		shareFolder = file.getPath();
                jShareFolder.setText(shareFolder);
        	}
        }
        else{
        	return;
        }
    }
    
	
    public void jbtnRefreshSrc_actionPerformed(ActionEvent e) {
		try{
			int table_row_count = table_data_model.getRowCount();
			while(table_row_count > 0){
				table_data_model.removeRow(0);
				--table_row_count;
			}
			beforeAppend = rdp.jResourceDownloadStatus.getText();
			rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[信息] 正在向服务器请求文件列表...");
			
	    	String[] respArray;
			// 发送LIST命令
			out.print("LIST");
			out.flush();

			int list_total = 0;
			response = in.readLine();
			respArray = response.split(" ");
			while((!respArray[0].equals("OK")) && (!respArray[0].equals("ERROR")))
			{
				list_total++;
				String[] resource = new String[]{(((Integer)list_total).toString()),respArray[0],respArray[1]};
				table_data_model.addRow(resource);
				response = in.readLine();
				respArray = response.split(" ");
			}
			beforeAppend = rdp.jResourceDownloadStatus.getText();
			rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[信息] 一共获取到 " + list_total + " 个文件");
			
			jbtnRequestSrc.setVisible(true);
			
			if(!response.equals("OK")){
				JOptionPane.showMessageDialog(this,"[错误] >> " + response.substring(6));
				jtpTransFile.remove(jtpTransFile.getSelectedIndex());
				return;
			}
		}
		catch(Exception err){
			JOptionPane.showMessageDialog(this,"[错误] >> "+err);
        	jtpTransFile.remove(jtpTransFile.getSelectedIndex());
			return;
		}
    }

	
	public void jbtnQuit_actionPerformed(ActionEvent e) {
		try{
			out.print("QUIT");
			out.flush();

			response = in.readLine();
			if(!response.equals("GOODBYE"))
			{
				JOptionPane.showMessageDialog(this,"[错误] >> 程序未正常退出： " + response);
	        	jtpTransFile.remove(jtpTransFile.getSelectedIndex());
				return;
			}
			else
			{
				JOptionPane.showMessageDialog(this,"[成功] >> 成功关闭连接");
				jtpTransFile.remove(jtpTransFile.getSelectedIndex());
			}

			in.close();
			out.close();
			socket.close();
			jbtnRefreshSrc.setVisible(false);
			jbtnRequestSrc.setVisible(false);
			jbtnQuit.setVisible(false);
		}
		catch(Exception err){
			JOptionPane.showMessageDialog(this,"[错误] >> "+err);
        	jtpTransFile.remove(jtpTransFile.getSelectedIndex());
			return;
		}
    }
	
	
	public void jbtnRequestSrc_actionPerformed(ActionEvent e) {
		try{
			String selected_file_name = "";
			String[] respArray;
			if( resource_table.getSelectedRow() < 0){
				return;
			}
			selected_file_name = (String)resource_table.getValueAt(resource_table.getSelectedRow(), 1); 
			System.out.println(selected_file_name);
			
			if(!selected_file_name.isEmpty())
			{
				//发送REQUEST
				out.print("REQUEST " + selected_file_name);
				out.flush();
				
				response = in.readLine();
				respArray = response.split(" ");
				if(respArray[0].equals("OK")){
					beforeAppend = rdp.jResourceDownloadStatus.getText();
					rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[错误] >> 在服务器上并未找到文件'" + selected_file_name);
					return;
				}

				while((!respArray[0].equals("OK")) && (!respArray[0].equals("ERROR")))
				{
					comSocket = new Socket(respArray[0], 7701);
					
					String comResponse;
					BufferedReader comIn = new BufferedReader(new InputStreamReader(comSocket.getInputStream()));
					PrintWriter comOut = new PrintWriter(comSocket.getOutputStream(), false);
					
					//验证身份
					comOut.println("HELLO");
					comOut.flush();
					comResponse = comIn.readLine();
					
					//确认
					if(!comResponse.equals("ACCEPT"))
					{
						beforeAppend = rdp.jResourceDownloadStatus.getText();
						rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[错误] >> 客户端握手消息验证失败");
						return;
					}

					Socket fileSocket = new Socket(respArray[0], 7702);
					comOut.println("GET " + selected_file_name);
					comOut.flush();
					InputStream fileIn = fileSocket.getInputStream();
					
					File f = new File(shareFolder+File.separator+"recv");
					 if (!f.exists()) 
					 {
						 f.mkdirs();
					 }
					BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(shareFolder+File.separator+"recv"+File.separator + selected_file_name));
					int bytesRead,current = 0;
					
					byte[] buffer = new byte[Integer.parseInt(respArray[1])];								
					bytesRead = fileIn.read(buffer, 0, buffer.length);
					current = bytesRead;

					beforeAppend = rdp.jResourceDownloadStatus.getText();
					rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[信息] 开始传输文件...");
					do
					{
						System.out.print(". ");

						bytesRead = fileIn.read(buffer, current, (buffer.length - current));
						if(bytesRead >= 0)
							current += bytesRead;
					} while(bytesRead > -1 && buffer.length != current);

					fileOut.write(buffer, 0, current);
					fileOut.flush();

					beforeAppend = rdp.jResourceDownloadStatus.getText();
					rdp.jResourceDownloadStatus.setText(beforeAppend+"\n"+"[成功] 文件传输成功");
					
					fileIn.close();
					fileOut.close();
					fileSocket.close();
					
					respArray[0] = "OK";
                    
                    response = in.readLine();
                    respArray = response.split(" ");							
				}
                
				if(!respArray[0].equals("OK")){
					JOptionPane.showMessageDialog(this,"[错误] >> " + response.substring(6));
					jtpTransFile.remove(jtpTransFile.getSelectedIndex());
					return;
				}
			}
		}
		catch(Exception err){
			JOptionPane.showMessageDialog(this,"[错误] >> "+err);
        	jtpTransFile.remove(jtpTransFile.getSelectedIndex());
			return;
		}
    }
	
    public void this_windowClosed(WindowEvent e) {
    	try{
			out.print("QUIT");
			out.flush();

			response = in.readLine();
			if(!response.equals("GOODBYE")){
				JOptionPane.showMessageDialog(this,"[错误] >> 程序未正常退出： " + response);
			}

			in.close();
			out.close();
			socket.close();
		}
		catch(Exception err){
			JOptionPane.showMessageDialog(this,"[错误] >> "+err);
		}
        tfm.close();
    }
}


class MainFrame_this_windowAdapter extends WindowAdapter {
    private MainFrame adaptee;
    MainFrame_this_windowAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosed(WindowEvent e) {
        adaptee.this_windowClosed(e);
    }
}


class MainFrame_jbtnSend_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_jbtnSend_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnSend_actionPerformed(e);
    }
}


class MainFrame_jbtnSetting_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_jbtnSetting_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnSetting_actionPerformed(e);
    }
}

abstract class MainFrame_jresourceListClick_actionAdapter implements MouseListener {
    private MainFrame adaptee;
    MainFrame_jresourceListClick_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }
}

class MainFrame_jbtnConnServer_actionAdapter implements ActionListener {
	private MainFrame adaptee;
    MainFrame_jbtnConnServer_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnConnServer_actionPerformed(e);
    }
}

class MainFrame_jbtnBrowserFolder_actionAdapter implements ActionListener {
	private MainFrame adaptee;
	MainFrame_jbtnBrowserFolder_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnBrowserFolder_actionPerformed(e);
    }
}

class MainFrame_jbtnRefreshSrc_actionAdapter implements ActionListener {
	private MainFrame adaptee;
	MainFrame_jbtnRefreshSrc_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnRefreshSrc_actionPerformed(e);
    }
}

class MainFrame_jbtnQuit_actionAdapter implements ActionListener {
	private MainFrame adaptee;
	MainFrame_jbtnQuit_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnQuit_actionPerformed(e);
    }
}

class MainFrame_jbtnRequestSrc_actionAdapter implements ActionListener {
	private MainFrame adaptee;
	MainFrame_jbtnRequestSrc_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbtnRequestSrc_actionPerformed(e);
    }
}

class Global
{
	public static String path = "";
}

class peer_server implements Runnable
{
	// 实现一个基本的文件服务器
	MainFrame handle;
	peer_server(MainFrame handle){
		this.handle = handle;
	}
	public void run()
	{
		try
		{
			// 预定义
			String path = Global.path;

			// 建立用于接收服务器消息的ServerSocket
			ServerSocket comServSock = new ServerSocket(7701);
			
			// 建立用于接收另一个peer消息、传输文件的ServerSocket
			ServerSocket fileServSock = new ServerSocket(7702);
			
			while(true)
			{
				Socket socket = comServSock.accept();
				
				//创建输入输出流
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
				
				String response = "";
				String[] respArray;

				// 循环监听连接请求，直到接收到"HELLO"或者"QUIT"握手消息
				while(!response.equals("HELLO") && !response.equals("QUIT"))
				{
					response = in.readLine();					
					// 如果接收到的握手消息是OPEN，回复确认消息HELLO
					if(response.equals("HELLO"))
					{
						out.println("ACCEPT");
						out.flush();
					}
				}

				// 循环监听连接请求，直到收到QUIT握手消息
				while(!response.equals("QUIT"))
				{
					response = in.readLine();
					
					//请求参数分段处理
					respArray = response.split(" ");					

					// syntax: GET [filename]
					if(respArray[0].equals("GET"))
					{
						try
						{
							// 请求的文件名不为空
							if(!respArray[1].isEmpty())
							{
								// 新建一个用于文件传输的socket
								Socket fileSocket = fileServSock.accept();

								File peerfile = new File(path + File.separator + respArray[1]);								
								byte[] buffer = new byte[(int)peerfile.length()];
								BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(peerfile));
								fileIn.read(buffer, 0, buffer.length);
								BufferedOutputStream fileOut = new BufferedOutputStream(fileSocket.getOutputStream());
								fileOut.write(buffer, 0, buffer.length);
								fileOut.flush();
								fileIn.close();
								fileOut.close();
								fileSocket.close();
								
								out.println("OK");
								out.flush();
							}
						}
						catch (Exception e)
						{
							out.print("ERROR "+e);
							out.flush();
						}
					}
					else if(response.equals("CLOSE"))
					{
						continue;
					}
				}
				out.print("GOODBYE");
				out.flush();
				socket.close();
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(handle," [错误] >> "+e);
			// System.out.println("\033[1;31m[错误] >>\033[0m "+e);			
			System.exit(-1);
		}
	}
}