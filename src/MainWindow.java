/*
    Numeth is simple application to solve many mathematical problems numerically.

    Copyright (C) 2012 Rafael Rendón Pablo <smart.rendon@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import edu.inforscience.lang.*;

import edu.inforscience.graphics.*;

public class MainWindow extends JFrame {
  private JMenuBar menuBar;

  private JMenu fileMenu;
  private JMenuItem saveLogAction;
  private JMenuItem exportGraphAction;
  private JMenuItem quitAction;

  private JMenu editMenu;
  private JMenuItem preferencesAction;

  private JMenu helpMenu;
  private JMenuItem aboutAction;


  private JToolBar toolBar;
  private JTextPane logPane;
  private JButton solveButton;
  private JList solutionsList;
  private JTextField errorText;
  private JTextField expressionText;
  private JSplitPane splitPane;
  private JComboBox methodList;
  private JSplitPane leftPanel;

  private JPanel planeContainer;
  private JToolBar planeToolbar;
  private Plane plane;

  private JButton zoomInButton;
  private JButton zoomOutButton;
  private JButton zoomResetButton;
  private JButton showAxisButton;
  private JButton showGridButton;
  private JButton movePlaneButton;

  private Locale currentLocale;
  private ResourceBundle messages;


  private Function function;

  public MainWindow()
  {
    super("Numeth");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(1000, 600);
    setLocationRelativeTo(null);

    // Resources for internationalization
    currentLocale = new Locale("es", "MX");
    messages = ResourceBundle.getBundle("Messages", currentLocale);

    fileMenu = new JMenu(messages.getString("MainWindow.fileMenu"));
    editMenu = new JMenu(messages.getString("MainWindow.editMenu"));
    helpMenu = new JMenu(messages.getString("MainWindow.helpMenu"));

    menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);

    expressionText  = new JTextField(50);
    errorText       = new JTextField(10);
    solveButton = new JButton(messages.getString("MainWindow.solveButton"));
    solveButton.addActionListener(new ActionHandler());

    // Tool bar
    toolBar = new JToolBar();
    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    toolBar.add(new JLabel("<html><i>f(x)</i></html>:"));
    toolBar.add(expressionText);
    toolBar.add(new JLabel(messages.getString("MainWindow.errorLabel")));
    toolBar.add(errorText);


    // Method list
    String[] methods = new String[] {messages.getString("Method.bestSuited"),
                                    messages.getString("Method.linear"),
                                    messages.getString("Method.bisection"),
                                    messages.getString("Method.newton.raphson"),
                                    messages.getString("Method.secant")
                                  };

    methodList = new JComboBox(methods);


    toolBar.add(methodList);
    toolBar.add(solveButton);


    // Add tool bar to the main window
    add(toolBar, BorderLayout.PAGE_START);


    // Solution log container
    logPane = new JTextPane();
    logPane.setBackground(Color.BLACK);
    JScrollPane logScrollPane = new JScrollPane(logPane,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    // Solution container
    String[] sols = new String[] { "[3, 3.5]", "[5.5, 6]", "[10, 10.5]"};
    solutionsList = new JList(sols);

    JPanel solutionsPanel = new JPanel();
    solutionsPanel.setLayout(new BoxLayout(solutionsPanel, BoxLayout.Y_AXIS));
    solutionsPanel.add(
        new JLabel(
          messages.getString("MainWindow.solutionsLabel")
        )
      );

    JScrollPane solutionsScrollPane = new JScrollPane(solutionsList,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    solutionsPanel.add(solutionsScrollPane);


    // Left panel
    leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    leftPanel.setTopComponent(logScrollPane);
    leftPanel.setBottomComponent(solutionsPanel);
    leftPanel.setDividerLocation(350);

    // Plane container
    zoomInButton = new JButton(new ImageIcon("pictures/zoom-in.png"));
    zoomOutButton = new JButton(new ImageIcon("pictures/zoom-out.png"));
    zoomResetButton = new JButton(new ImageIcon("pictures/zoom-reset.png"));
    showAxisButton = new JButton(new ImageIcon("pictures/show-axis.png"));
    showGridButton = new JButton(new ImageIcon("pictures/show-grid.png"));
    movePlaneButton = new JButton(new ImageIcon("pictures/move-plane.png"));

    planeToolbar = new JToolBar();
    planeToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
    planeToolbar.add(showAxisButton);
    planeToolbar.add(showGridButton);
    planeToolbar.add(movePlaneButton);
    planeToolbar.add(zoomOutButton);
    planeToolbar.add(zoomResetButton);
    planeToolbar.add(zoomInButton);



    plane = new Plane();
    planeContainer = new JPanel(new BorderLayout());
    planeContainer.add(planeToolbar, BorderLayout.PAGE_START);
    planeContainer.add(plane, BorderLayout.CENTER);

    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setLeftComponent(leftPanel);
    splitPane.setRightComponent(planeContainer);

    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(200);

    add(splitPane, BorderLayout.CENTER);

  }


  class ActionHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent event) {
      if (event.getSource() == solveButton) {
        String expression = expressionText.getText();
        Parser parser = new Parser();
        if (parser.validate(expression)) {
          function = new Function(expression);
          plane.addFunction(function);
          plane.plot();
        }

      }
    }
  }
}

