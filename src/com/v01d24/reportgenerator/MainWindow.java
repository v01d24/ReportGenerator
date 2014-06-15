package com.v01d24.reportgenerator;

import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.v01d24.reportgenerator.constants.Style;
import com.v01d24.reportgenerator.vars.Variable;
import com.v01d24.reportgenerator.vars.VariablesGroup;

import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.jdom.JDOMException;
import javax.swing.JEditorPane;

public class MainWindow implements ActionListener {

	private JFrame frame;
	//Panels
	private JTabbedPane groupsTab;
	private JPanel previewTab;
	private JScrollPane previewScrollPane;
	private JEditorPane previewPane;
	//Controls
	private JButton btnOpenTemplate;
	private JButton btnSaveDocument;
	private JButton btnRefreshPreview;
	
	//Processor
	private DocumentProcessor docProcessor;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Генератор отчётов");
		frame.setBounds(100, 100, 650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		frame.getContentPane().add(toolBar, gbc_toolBar);
		
		btnOpenTemplate = new JButton("Открыть шаблон");
		btnOpenTemplate.addActionListener(this);
		toolBar.add(btnOpenTemplate);
		
		btnSaveDocument = new JButton("Сохранить отчёт");
		btnSaveDocument.addActionListener(this);
		toolBar.add(btnSaveDocument);
		
		btnRefreshPreview = new JButton("Обновить");
		btnRefreshPreview.addActionListener(this);
		toolBar.add(btnRefreshPreview);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.6);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		frame.getContentPane().add(splitPane, gbc_splitPane);
		
		groupsTab = new JTabbedPane(JTabbedPane.TOP);
		groupsTab.setMinimumSize(new Dimension(Style.WIDGET_PANEL_WIDTH,100));
		splitPane.setLeftComponent(groupsTab);
		
		previewTab = new JPanel();
		splitPane.setRightComponent(previewTab);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[] {0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0};
		previewTab.setLayout(gbl_panel);
		
		JLabel lblPreview = new JLabel("Просмотр");
		GridBagConstraints gbc_lblPreview = new GridBagConstraints();
		gbc_lblPreview.insets = new Insets(0, 0, 5, 0);
		gbc_lblPreview.gridx = 0;
		gbc_lblPreview.gridy = 0;
		previewTab.add(lblPreview, gbc_lblPreview);
		
		previewScrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.NORTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		previewTab.add(previewScrollPane, gbc_scrollPane);
		
		previewPane = new JEditorPane();
		previewPane.setContentType("text/html");
		previewScrollPane.setViewportView(previewPane);
		
		docProcessor = new DocumentProcessor();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnOpenTemplate) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Open document file", "odt");
			fc.setFileFilter(filter);
			int result = fc.showOpenDialog(frame.getContentPane());
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					docProcessor.setFile(fc.getSelectedFile());
					//String content = this.docProcessor.getText();
					String content = docProcessor.getHtml();
					previewPane.setText(content);
					previewScrollPane.scrollRectToVisible(new Rectangle());
					initTabs();
				} catch (JDOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if (source == btnSaveDocument) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Open document file", "odt");
			fc.setFileFilter(filter);
			int result = fc.showSaveDialog(frame.getContentPane());
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					docProcessor.saveToFile(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if (source == btnRefreshPreview) {
			docProcessor.refreshValues();
			String content = docProcessor.getHtml();
			Rectangle visibleRect = previewScrollPane.getVisibleRect();
			previewPane.setText(content);
			previewScrollPane.scrollRectToVisible(visibleRect);
		}
	}

    protected JComponent makeInputsPanel(VariablesGroup variablesGroup) {

    	JPanel groupPanel = new JPanel();
    	groupPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    	
        List<String> variablesNames = variablesGroup.getNamesList();

        for (String variableName: variablesNames) {
        	Variable variable = variablesGroup.getVariableByName(variableName);
        	groupPanel.add((JComponent)variable.getWidget());
        }
        return groupPanel;
    	/*JScrollPane groupScrollPane = new JScrollPane(groupPanel);
    	groupScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return groupScrollPane;*/
    }

	private void initTabs() {
		groupsTab.removeAll();
		Set<String> groupsNames = docProcessor.getGroupsNames();
		for (String groupName: groupsNames) {
			VariablesGroup variablesGroup = docProcessor.getGroupByName(groupName);
			JComponent panel = makeInputsPanel(variablesGroup);
			groupsTab.addTab(groupName, null, panel);
		}
	}

}
