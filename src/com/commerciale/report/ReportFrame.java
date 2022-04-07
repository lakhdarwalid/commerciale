package com.commerciale.report;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

public class ReportFrame extends JDialog{
	public ReportFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setMinimumSize(new Dimension(1000, 1000));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//setBounds(0, 0, screenSize.width, screenSize.height);
		setBounds(0, 0, screenSize.width, 850);
	}
	

}
