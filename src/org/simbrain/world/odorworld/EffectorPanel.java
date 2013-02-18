/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.world.odorworld;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.simbrain.world.odorworld.effectors.Effector;
import org.simbrain.world.odorworld.entities.OdorWorldEntity;

/**
 * Panel showing an agent's effectors.
 */
public class EffectorPanel extends JPanel {

	/** Table representing Effector. */
	private JTable table;

	/** Table model. */
	private EffectorModel model;

	public EffectorPanel(final OdorWorldEntity entity) {

		// Set up table
		model = new EffectorModel();
		table = new JTable(model);
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
				.setHorizontalAlignment(SwingConstants.CENTER);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setFocusable(false);
		/**
		 * Right Click Interface
		 */
		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(final MouseEvent e) {
				if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
					int row = table.rowAtPoint(e.getPoint());
					int column = table.columnAtPoint(e.getPoint());
					table.setRowSelectionInterval(row, row);
					if (column == 0) {
					    JPopupMenu sensorPop = new JPopupMenu();
					    JMenuItem menuItem = new JMenuItem("Sensor Popup Menu");
					    sensorPop.add(menuItem);
					    sensorPop.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		for (Effector effector : entity.getEffectors()) {
			model.addRow(effector);
		}

		JScrollPane scrollPane = new JScrollPane(table);
		JPanel buttonBar = new JPanel();
		ImageIcon plus = new ImageIcon("src/org/simbrain/resource/plus.png");
		ImageIcon minus = new ImageIcon("src/org/simbrain/resource/minus.png");
		// TODO: addEffector
		JButton addEffector;
		JButton deleteEffector;
		buttonBar.add(addEffector = new JButton(plus));
		buttonBar.add(deleteEffector = new JButton(minus));
		deleteEffector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent del) {
				int [] selectedRows = table.getSelectedRows();
				for (int i = 0; i < selectedRows.length; i++) {
			        model.removeRow(selectedRows[i]);
			        model.fireTableDataChanged();
				}
			}
		});
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, scrollPane);
		add(BorderLayout.SOUTH, buttonBar);
		}
	/**
	 * Table model which represents Effectors
	 */
	class EffectorModel extends AbstractTableModel {

		/** Column names. */
		String[] columnNames = { "Id", "Label", "Type" };

		/** Internal list of components. */
		private List<Effector> data = new ArrayList<Effector>();

		/**
		 * Add a row
		 *
		 * @param Effector
		 */
		public void addRow(Effector effector) {
			data.add(effector);
		}

		/**
		 * Remove a row
		 *
		 * @param row
		 */
		public void removeRow(int row) {
			data.remove(row);
		}

		/**
		 * {@inheritDoc}
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		 * {@inheritDoc}
		 */
		public int getRowCount() {
			return data.size();
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getValueAt(int row, int col) {
			switch (col) {
			case 0:
				return data.get(row).getId();
			case 1:
				return data.get(row).getLabel();
			case 2:
				return data.get(row).getClass().getSimpleName();
			default:
				return null;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void setValueAt(Object value, int row, int col) {
			switch (col) {
			case 0:
				return;
			case 1:
				data.get(row).setLabel((String) value);
				return;
			case 2:
				return;
			}
			this.fireTableDataChanged();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isCellEditable(int row, int col) {
			switch (col) {
			case 0:
				return true;
			case 1:
				return true;
			case 2:
				return false;
			default:
				return false;
			}
		}
		/**
		 * {@inheritDoc}
		 */
		public Class getColumnClass(int col) {
			switch (col) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			default:
				return null;
			}
		}

	}
}