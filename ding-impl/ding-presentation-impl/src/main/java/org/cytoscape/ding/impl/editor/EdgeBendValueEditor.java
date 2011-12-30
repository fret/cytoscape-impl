package org.cytoscape.ding.impl.editor;

import static org.cytoscape.view.presentation.property.MinimalVisualLexicon.NODE_X_LOCATION;
import static org.cytoscape.view.presentation.property.MinimalVisualLexicon.NODE_Y_LOCATION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.cytoscape.ding.Bend;
import org.cytoscape.ding.impl.BendImpl;
import org.cytoscape.ding.impl.DEdgeView;
import org.cytoscape.ding.impl.DVisualLexicon;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.presentation.RenderingEngineFactory;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.presentation.property.RichVisualLexicon;
import org.cytoscape.view.vizmap.gui.editor.ValueEditor;

public class EdgeBendValueEditor extends JDialog implements ValueEditor<Bend> {

	private static final long serialVersionUID = 9145223127932839836L;

	private static final Dimension DEF_PANEL_SIZE = new Dimension(600, 300);
	
	private static final Color NODE_COLOR = Color.gray;
	private static final Color EDGE_COLOR = Color.BLACK;
	private static final Color BACKGROUND_COLOR = Color.white;
	
	private DEdgeView edgeView;

	private final CyNetworkFactory cyNetworkFactory;
	private final CyNetworkViewFactory cyNetworkViewFactory;
	private final RenderingEngineFactory<CyNetwork> presentationFactory;

	public EdgeBendValueEditor(final CyNetworkFactory cyNetworkFactory,
			final CyNetworkViewFactory cyNetworkViewFactory, final RenderingEngineFactory<CyNetwork> presentationFactory) {
		super();
		
		// Null check
		if (cyNetworkFactory == null)
			throw new NullPointerException("CyNetworkFactory is null.");
		if (cyNetworkViewFactory == null)
			throw new NullPointerException("CyNetworkViewFactory is null.");
		if (presentationFactory == null)
			throw new NullPointerException("RenderingEngineFactory is null.");
		
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.presentationFactory = presentationFactory;
		
		
		this.setModal(true);
	}

	private void initUI(final CyNetworkFactory cyNetworkFactory,
			final CyNetworkViewFactory cyNetworkViewFactory, final RenderingEngineFactory<CyNetwork> presentationFactory) {
		
		this.getContentPane().removeAll();
		
		setTitle("Edge Bend Editor");

		// Create Dummy View for this editor
		JPanel innerPanel = new JPanel();
		innerPanel.setBorder(new TitledBorder("CTRL-Click to add new Edge Handle / Drag Handles to adjust Bend"));
		setPreferredSize(DEF_PANEL_SIZE);
		setLayout(new BorderLayout());
		add(innerPanel, BorderLayout.CENTER);

		// Create very simple dummy view.
		final CyNetwork dummyNet = cyNetworkFactory.createNetworkWithPrivateTables();
		final CyNode source = dummyNet.addNode();
		final CyNode target = dummyNet.addNode();
		final CyEdge edge = dummyNet.addEdge(source, target, true);

		// Create View
		final CyNetworkView dummyview = cyNetworkViewFactory.createNetworkView(dummyNet);

		// Set appearances of the view
		final View<CyNode> sourceView = dummyview.getNodeView(source);
		final View<CyNode> targetView = dummyview.getNodeView(target);
		edgeView = (DEdgeView) dummyview.getEdgeView(edge);
		
		sourceView.setVisualProperty(RichVisualLexicon.NODE_FILL_COLOR, NODE_COLOR);
		targetView.setVisualProperty(RichVisualLexicon.NODE_FILL_COLOR, NODE_COLOR);
		
		sourceView.setVisualProperty(RichVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.ELLIPSE);
		targetView.setVisualProperty(RichVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.ELLIPSE);
		
		sourceView.setVisualProperty(RichVisualLexicon.NODE_WIDTH, 40d);
		sourceView.setVisualProperty(RichVisualLexicon.NODE_HEIGHT, 40d);
		targetView.setVisualProperty(RichVisualLexicon.NODE_WIDTH, 40d);
		targetView.setVisualProperty(RichVisualLexicon.NODE_HEIGHT, 40d);
		
		edgeView.setVisualProperty(RichVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT, EDGE_COLOR);
		edgeView.setVisualProperty(RichVisualLexicon.EDGE_WIDTH, 4d);
		edgeView.setVisualProperty(RichVisualLexicon.EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.ARROW);
		edgeView.setVisualProperty(DVisualLexicon.EDGE_TARGET_ARROW_UNSELECTED_PAINT, EDGE_COLOR);
		
		final Bend newBend = new BendImpl();
		edgeView.setVisualProperty(DVisualLexicon.EDGE_BEND, newBend);
		
		
		dummyview.getNodeView(source).setVisualProperty(NODE_X_LOCATION, 0d);
		dummyview.getNodeView(source).setVisualProperty(NODE_Y_LOCATION, 20d);
		dummyview.getNodeView(target).setVisualProperty(NODE_X_LOCATION, 200d);
		dummyview.getNodeView(target).setVisualProperty(NODE_Y_LOCATION, 0d);

		innerPanel.setBackground(BACKGROUND_COLOR);
		// Render it in this panel
		final RenderingEngine<CyNetwork> renderingEngine = presentationFactory.createRenderingEngine(innerPanel, dummyview);
		dummyview.fitContent();
		
		final JPanel buttonPanel = new JPanel();
		final BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonLayout);
		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		this.add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
	}

	@Override
	public <S extends Bend> Bend showEditor(Component parent, S initialValue) {
		initUI(cyNetworkFactory, cyNetworkViewFactory, presentationFactory);
		this.setLocationRelativeTo(parent);
		this.setVisible(true);		
		return edgeView.getBend();
	}

	@Override
	public Class<Bend> getType() {
		return Bend.class;
	}
}