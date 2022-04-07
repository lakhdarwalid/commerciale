package com.commerciale.service;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer{
	private List<List<Integer>> rowColHeight = new ArrayList<>();
	public MultiLineCellRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		 setText((String)value);
		 setFont(new Font("SansSerif", Font.PLAIN, 17));
		 		
		return this;
	}
}
 