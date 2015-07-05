package trans;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ResourceDownloadPanel extends JPanel {
	private JTabbedPane jtp;
    JTextArea jResourceDownloadStatus = new JTextArea();
    JScrollPane scroll = new JScrollPane(jResourceDownloadStatus);
    public ResourceDownloadPanel(JTabbedPane jtp) {
    	this.jtp = jtp;
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setBackground(new Color(206, 227, 249));
        this.setBorder(BorderFactory.createEtchedBorder());
        jResourceDownloadStatus.setBackground(new Color(236, 247, 255));
        jResourceDownloadStatus.setDisabledTextColor(Color.orange);
        jResourceDownloadStatus.setText("");
        jResourceDownloadStatus.setWrapStyleWord(true);
        jResourceDownloadStatus.setBounds(new Rectangle(5, 5, 375, 360));
        jResourceDownloadStatus.setLineWrap(true);
        
        scroll.setBackground(new Color(236, 247, 255));
        scroll.setBounds(new Rectangle(5, 5, 375, 360));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroll);
    }
}