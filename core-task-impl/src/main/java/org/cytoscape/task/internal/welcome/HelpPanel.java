package org.cytoscape.task.internal.welcome;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.cytoscape.util.swing.OpenBrowser;

public class HelpPanel extends JPanel {
	
	private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 12);
	private static final Color LABEL_COLOR = new Color(0x40, 0x40, 0x40);

	private static final Color SELECTED_COLOR = new Color(0x63, 0xB8, 0xFF);
	private static final Font SELECTED_FONT = new Font("SansSerif", Font.BOLD, 12);

	
	private JLabel about;
	private JLabel manual;
	private JLabel tutorial;
	private JLabel bugReport;
	
	private final List<JLabel> labelSet;
	private final Map<JLabel, String> urlMap;
	
	private final OpenBrowser openBrowserServiceRef;
	
	HelpPanel(final OpenBrowser openBrowserServiceRef) {
		labelSet = new ArrayList<JLabel>();
		urlMap = new HashMap<JLabel, String>();
		this.openBrowserServiceRef = openBrowserServiceRef;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new GridLayout(4, 1));
		about = new JLabel("About Cytoscape >>");
		manual = new JLabel("User Manual >>");
		tutorial = new JLabel("Tutorials >>");
		bugReport = new JLabel("Report a bug >>");
		
		labelSet.add(about);
		labelSet.add(manual);
		labelSet.add(tutorial);
		labelSet.add(bugReport);
		urlMap.put(about, "http://www.cytoscape.org/what_is_cytoscape.html");
		urlMap.put(manual, "http://www.cytoscape.org/documentation_users.html");
		urlMap.put(tutorial, "http://opentutorials.cgl.ucsf.edu/index.php/Portal:Cytoscape");
		urlMap.put(bugReport, "http://www.cytoscape.org/");
		
		for(final JLabel label: labelSet) {
			label.setFont(LABEL_FONT);
			label.setForeground(LABEL_COLOR);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setHorizontalTextPosition(SwingConstants.CENTER);
			label.setOpaque(false);
			label.addMouseListener(new LabelMouseListener(label, urlMap.get(label), this));
			add(label);
		}
	}
	
	private final class LabelMouseListener implements MouseListener {
		
		private final JLabel label;
		private final String url;
		private final JPanel parent;
		
		LabelMouseListener(final JLabel label, final String url, final JPanel parent) {
			this.label = label;
			this.url = url;
			this.parent=parent;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			openBrowserServiceRef.openURL(url);			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
//			label.setFont(SELECTED_FONT);
//			label.setForeground(SELECTED_COLOR);
//			label.setBackground(WelcomeScreenDialog.PANEL_COLOR);
//			parent.setBackground(WelcomeScreenDialog.PANEL_COLOR);
//			label.setOpaque(false);
//			//parent.setOpaque(false);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
//			label.setFont(LABEL_FONT);
//			label.setForeground(LABEL_COLOR);
//			label.setBackground(WelcomeScreenDialog.PANEL_COLOR);
//			parent.setBackground(Color.red);
//			label.setOpaque(false);
//			//parent.setOpaque(false);
//			label.repaint();
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}

		
	}
	

}