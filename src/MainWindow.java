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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import edu.inforscience.lang.*;

import java.io.*;

import edu.inforscience.graphics.*;
import edu.inforscience.math.*;
import edu.inforscience.math.Math;

public class MainWindow extends JFrame {
  public static final int BEST_SUITED         = 0;
  public static final int BRUTE_FORCE         = 1;
  public static final int BISECTION           = 2;
  public static final int NEWTON_RAPHSON      = 3;
  public static final int SECANT              = 4;
  public static final int FIXED_POINT         = 5;
  public static final int AITKEN_ACCELERATION = 6;

  public static final int MAX_COLORS          = 25;

  private JMenuBar menuBar;
  private JMenu fileMenu;
  private JMenuItem saveLogAction;
  private JMenuItem exportGraphAction;
  private JMenuItem quitAction;

  private JMenu editMenu;
  private JMenuItem preferencesAction;

  private JMenu helpMenu;
  private JMenuItem aboutAction;

  private String lastFunctionName;

  private JToolBar toolBar;
  private JTextPane logPane;
  private JButton solveButton;
  private JList solutionsList;
  private JTextField errorText;
  private JTextField gxFunction;
  private JLabel gxFunctionLabel;
  private JTextField expressionText;
  private JSplitPane mainVerticalSplit;
  private JComboBox methodList;
  private JSplitPane leftPanel;
  private JSplitPane mainHorizontalSplit;
  private Vector<Function> functionList;

  private CheckBoxList functionCheckList;
  private JPanel planeContainer;
  private JToolBar planeToolbar;
  private Plane plane;

  private JButton zoomInButton;
  private JButton zoomOutButton;
  private JButton zoomResetButton;
  private JButton showAxisButton;
  private JButton showGridButton;

  private ButtonGroup functionButtonGroup;

  private JTextField scaleText;

  private Locale currentLocale;
  private ResourceBundle messages;

  private Function function;
  private PrintWriter writer;
  private String[] methodNames;
  private Color[] graphColors;
  private int colorIndex;

  BackgroundClickHandler clickHandler;

  public MainWindow()
  {
    super("Numeth");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(1024, 768);
    setLocationRelativeTo(null);


    // Action handler object
    ActionHandler actionHandler = new ActionHandler();

    functionButtonGroup = new ButtonGroup();

    // Resources for internationalization
    currentLocale = new Locale("en", "US");
    messages = ResourceBundle.getBundle("Messages", currentLocale);

    fileMenu = new JMenu(messages.getString("MainWindow.fileMenu"));
    editMenu = new JMenu(messages.getString("MainWindow.editMenu"));
    helpMenu = new JMenu(messages.getString("MainWindow.helpMenu"));

    menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);

    errorText = new JTextField(13);
    errorText.addActionListener(actionHandler);
    errorText.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        errorText.setBackground(Color.WHITE);
      }

      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        errorText.setBackground(Color.WHITE);
      }

      @Override
      public void changedUpdate(DocumentEvent documentEvent) {
        errorText.setBackground(Color.WHITE);
      }
    });

    gxFunctionLabel = new JLabel("g(x)=");
    gxFunctionLabel.setVisible(false);
    gxFunction = new JTextField(13);
    gxFunction.setVisible(false);

    expressionText  = new JTextField(30);
    expressionText.addActionListener(actionHandler);
    expressionText.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        expressionText.setBackground(Color.WHITE);
      }

      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        expressionText.setBackground(Color.WHITE);
      }

      @Override
      public void changedUpdate(DocumentEvent documentEvent) {
        expressionText.setBackground(Color.WHITE);
      }
    });

    solveButton = new JButton(messages.getString("MainWindow.solveButton"));
    solveButton.addActionListener(actionHandler);

    // Tool bar
    toolBar = new JToolBar();
    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    toolBar.add(new JLabel("<html><i>f(x)</i> = </html>:"));
    toolBar.add(expressionText);
    toolBar.add(new JLabel(messages.getString("MainWindow.errorLabel")));
    toolBar.add(errorText);
    toolBar.add(gxFunctionLabel);
    toolBar.add(gxFunction);


    // Method list
    methodNames = new String[] {messages.getString("Method.bestSuited"),
                                messages.getString("Method.linear"),
                                messages.getString("Method.bisection"),
                                messages.getString("Method.newton.raphson"),
                                messages.getString("Method.secant"),
                                messages.getString("Method.fixedPoint"),
                                messages.getString("Method.aitken")
                              };

    methodList = new JComboBox(methodNames);
    methodList.addActionListener(actionHandler);


    toolBar.add(methodList);
    toolBar.add(solveButton);


    // Add tool bar to the main window
    add(toolBar, BorderLayout.PAGE_START);


    lastFunctionName = "e";
    functionList = new Vector<Function>();


    // Solution container
    String[] sols = new String[] { };
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

    functionCheckList = new CheckBoxList();
    functionCheckList.addMouseListener(actionHandler);
    JScrollPane buttonGroupScrollPane = new JScrollPane(functionCheckList,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    JPanel functionPanel = new JPanel();
    functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
    functionPanel.add(
      new JLabel(
        messages.getString("MainWindow.functionList")
      )
    );
    functionPanel.add(buttonGroupScrollPane);


    // Left panel
    leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    leftPanel.setTopComponent(functionPanel);
    leftPanel.setBottomComponent(solutionsPanel);
    leftPanel.setDividerLocation(200);

    // Plane container
    zoomInButton    = new JButton(new ImageIcon("pictures/zoom-in.png"));
    zoomOutButton   = new JButton(new ImageIcon("pictures/zoom-out.png"));
    zoomResetButton = new JButton(new ImageIcon("pictures/zoom-reset.png"));
    showAxisButton  = new JButton(new ImageIcon("pictures/show-axis.png"));
    showGridButton  = new JButton(new ImageIcon("pictures/show-grid.png"));

    showAxisButton.addActionListener(actionHandler);
    showGridButton.addActionListener(actionHandler);
    zoomOutButton.addActionListener(actionHandler);
    zoomResetButton.addActionListener(actionHandler);
    zoomInButton.addActionListener(actionHandler);

    scaleText = new JTextField("1:1", 9);
    scaleText.addActionListener(actionHandler);
    scaleText.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        scaleText.setBackground(Color.WHITE);
      }

      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        scaleText.setBackground(Color.WHITE);
      }

      @Override
      public void changedUpdate(DocumentEvent documentEvent) {
        scaleText.setBackground(Color.WHITE);
      }
    });


    solutionsList.addMouseListener(actionHandler);



    planeToolbar = new JToolBar();
    planeToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
    planeToolbar.add(showAxisButton);
    planeToolbar.add(showGridButton);
    planeToolbar.add(zoomOutButton);
    planeToolbar.add(zoomResetButton);
    planeToolbar.add(zoomInButton);
    planeToolbar.add(new JLabel("A:B"));
    planeToolbar.add(scaleText);


    plane = new Plane();
    planeContainer = new JPanel(new BorderLayout());
    planeContainer.add(planeToolbar, BorderLayout.PAGE_START);
    planeContainer.add(plane, BorderLayout.CENTER);

    mainVerticalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    mainVerticalSplit.setLeftComponent(leftPanel);
    mainVerticalSplit.setRightComponent(planeContainer);

    mainVerticalSplit.setOneTouchExpandable(true);
    mainVerticalSplit.setDividerLocation(200);


    // Solution log container
    logPane = new JTextPane();
    logPane.setBackground(Color.BLACK);
    logPane.setForeground(Color.WHITE);
    logPane.setText("Solution log goes here!");
    JScrollPane logScrollPane = new JScrollPane(logPane,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    mainHorizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    mainHorizontalSplit.setTopComponent(mainVerticalSplit);
    mainHorizontalSplit.setBottomComponent(logScrollPane);
    mainHorizontalSplit.setDividerLocation(500);
    mainHorizontalSplit.setOneTouchExpandable(true);

    add(mainHorizontalSplit, BorderLayout.CENTER);

    graphColors = new Color[MAX_COLORS]; // Random color for the function graphs
    colorIndex = 0;
    generateRandomColors();

    writer = new PrintWriter(System.out, true);

    clickHandler = new BackgroundClickHandler();
  }

  class ActionHandler implements ActionListener, MouseListener {
    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent event) {
      if (event.getSource() == solveButton ||
          event.getSource() == expressionText ||
          event.getSource() == errorText) {

        String expression = expressionText.getText();
        Parser parser = new Parser();

        if (!expression.equals("") && parser.validate(expression)) {
          lastFunctionName = nextFunctionName();
          CheckBoxListEntry entry = null;

          function = new Function(expression, lastFunctionName);
          function.setColor(nextFunctionColor());
          functionList.add(function);
          entry = new CheckBoxListEntry(function.toString(), true);
          functionCheckList.addItem(entry);

          int i = 0;
          for (Function f : functionList) {
            entry = (CheckBoxListEntry)functionCheckList.getItem(i);
            f.setActive(entry.isSelected());
            i++;
          }

          plane.setFunctionList(functionList);
          plane.setShowMarkPoint(false);
          plane.plot();

          double epsilon = 1e-3;
          if (!errorText.getText().equals("")) {
            try {
              epsilon = Double.parseDouble(errorText.getText());
            } catch (NumberFormatException nfe) {
              errorText.setBackground(new Color(255, 170, 170));
            }
          }

          logPane.setText("");
          int method = methodList.getSelectedIndex();
          solve(function, method, epsilon);

          functionCheckList.updateUI();

        } else {
          expressionText.setBackground(new Color(255, 170, 170));
        }

      } else if (event.getSource() == showAxisButton) {
        plane.toggleShowAxis();

      } else if (event.getSource() == showGridButton) {
        plane.toggleShowGrid();

      } else if (event.getSource() == zoomOutButton) {
        plane.zoomOut(plane.getWidth()/2, plane.getHeight()/2);

      } else if (event.getSource() == zoomResetButton) {
        plane.resetZoom();

      } else if (event.getSource() == zoomInButton) {
        plane.zoomIn(plane.getWidth()/2, plane.getHeight()/2);

      } else if (event.getSource() == scaleText) {
        String input = scaleText.getText();
        if (input.matches("[ ]*[0-9]+[ ]*:[ ]*[0-9]+[ ]*")) {
          String[] tokens = input.split(":");
          int a = Integer.parseInt(tokens[0].trim());
          int b = Integer.parseInt(tokens[1].trim());

          plane.setScale(a, b);

        } else {
          scaleText.setBackground(new Color(255, 170, 170));
        }

      } else if (event.getSource() == methodList) {
        if (methodList.getSelectedIndex() == FIXED_POINT) {
          gxFunction.setVisible(true);
          gxFunctionLabel.setVisible(true);
        } else {
          gxFunction.setVisible(false);
          gxFunctionLabel.setVisible(false);
        }

        toolBar.updateUI();
      }
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
      if (event.getSource() == solutionsList) {
        JList list = (JList)event.getSource();
        if (event.getClickCount() == 2) {
          if (list.isSelectionEmpty()) return;
          double x = Double.parseDouble((String)list.getSelectedValue());
          plane.translate(x, 0);
          plane.mark(x, 0);
        }
      } else if (event.getSource() == functionCheckList) {
        if (functionList.size() == 0) return;

        solutionsList.setListData(new String[]{});
        plane.setShowMarkPoint(false);

        CheckBoxList list = (CheckBoxList)event.getSource();

        if (event.getClickCount() == 1) {
          clickHandler = new BackgroundClickHandler();

          try {
            clickHandler.execute();
          } catch(Exception e) {
          }
        }

        if (event.getClickCount() == 2) {
          clickHandler.cancel(true);

          int index = list.getSelectedIndex();
          functionList.get(index).setActive(true);
          ((CheckBoxListEntry)functionCheckList.getSelectedValue()).setSelected(true);
          plane.setFunctionList(functionList);
          plane.plot();

          expressionText.setText(functionList.get(index).getDefinition());

          int method = methodList.getSelectedIndex();
          double epsilon = 1e-3;
          try {
            epsilon = Double.parseDouble(errorText.getText());
          } catch(NumberFormatException nfe) {
            epsilon = 1e-3;
          }

          solve(functionList.get(index), method, epsilon);
        }

        list.updateUI();
      }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
  }

    class BackgroundClickHandler extends SwingWorker<Integer, Integer> {
      @Override
      protected Integer doInBackground() throws Exception {
        Thread.sleep(150);

        CheckBoxListEntry entry = null;
        int i = 0;
        for (Function f : functionList) {
          entry = (CheckBoxListEntry)functionCheckList.getItem(i);
          f.setActive(entry.isSelected());
          i++;
        }

        plane.setFunctionList(functionList);
        plane.plot();

        return 0;
      }
    }


  // Utility
  private void generateRandomColors()
  {
    Random random = new Random();
    for (int i = 0; i < MAX_COLORS; i++) {
      boolean elegible = false;
      int r = random.nextInt(255);
      int g = random.nextInt(255);
      int b = random.nextInt(255);

      while (i > 0) {
        r = random.nextInt(230);
        g = random.nextInt(230);
        b = random.nextInt(230);

        if ((r > 200 && g > 200) ||
            (r > 200 && b > 200) ||
            (g > 200 && b > 200))
          continue;
        int count = 0;
        for (int j = 0; j < i; j++) {
          if ((Math.abs(r - graphColors[j].getRed()) > 45) ||
              (Math.abs(g - graphColors[j].getGreen()) > 45) ||
              (Math.abs(b - graphColors[j].getBlue()) > 45))
            count++;
        }

        if (count == i)
          break;
      }

      graphColors[i] = new Color(r, g, b);
    }
  }
  private Color nextFunctionColor()
  {
    if (colorIndex < MAX_COLORS)
      return graphColors[colorIndex++];

    Random random = new Random();
    int r = random.nextInt(250);
    int g = random.nextInt(250);
    int b = random.nextInt(250);

    return new Color(r, g, b);
  }

  private String nextFunctionName()
  {
    String name = "";
    int length = lastFunctionName.length();
    char ch = lastFunctionName.charAt(length - 1);
    if (ch < 'w') {
      ch++;
      for (int i = 0; i + 1 < length; i++)
        name += lastFunctionName.charAt(i);
      name += ch;
    } else {
      for (int i = 0; i < length + 1; i++)
        name += "f";
    }

    return name;
  }

  private void solve(Function f, int method, double epsilon)
  {
    ArrayList<Solution> solutions;
    String[] S = new String[]{};

    switch (method) {
      case BEST_SUITED:
      case BRUTE_FORCE:
        BruteForce bruteForce = new BruteForce(f);
        solutions = bruteForce.solve(-100,  100);
        S = new String[solutions.size()];

        for (int i = 0; i < solutions.size(); i++) {
          Solution s = solutions.get(i);
          S[i] = Math.round(s.getX(), 6) + "";
        }
        break;

      case BISECTION:
        Bisection bisection = new Bisection(f);
        solutions = bisection.solve(-100, 100, epsilon);
        S = new String[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
          Solution sol = solutions.get(i);
          S[i] = Math.round(sol.getX(), 6) + "";
        }
        break;
      case NEWTON_RAPHSON:
        NewtonRaphson newtonRaphson = new NewtonRaphson(f);
        solutions = newtonRaphson.solve(-100, 100, epsilon);

        S = new String[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
          Solution sol = solutions.get(i);
          if (sol == null)
            S[i] = "NOT FOUND";
          else
            S[i] = Math.round(sol.getX(), 6) + "";
        }
        break;

      case SECANT:
        Secant secant = new Secant(f);
        solutions = secant.solve(-100, 100, epsilon);

        S = new String[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
          Solution sol = solutions.get(i);
          if (sol == null)
            S[i] = "NOT FOUND";
          else
            S[i] = Math.round(sol.getX(), 6) + "";
        }

        break;

      case FIXED_POINT:
        Function gx = null;
        String str = gxFunction.getText();
        Parser parser = new Parser();
        if (parser.validate(str)) {
          gx = new Function(str, "g(x)");
        } else {
          gxFunction.setBackground(new Color(255, 170, 170));
          break;
        }

        FixedPoint fixedPoint = new FixedPoint(f, gx);
        solutions = fixedPoint.solve(-100, 100, epsilon);

        S = new String[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
          Solution sol = solutions.get(i);
          if (sol == null)
            S[i] = "NOT FOUND";
          else
            S[i] = Math.round(sol.getX(), 6) + "";
        }
        break;

      case AITKEN_ACCELERATION:
        logPane.setText("NO IMPLEMENTED YET");
        break;
    };

    logPane.setText("");
    solutionsList.setListData(new String[]{});
    solutionsList.setListData(S);
  }
}



// Represents items in the list that can be selected

class CheckListItem
{
  private String  label;
  private boolean isSelected = false;

  public CheckListItem(String label)
  {
    this.label = label;
  }

  public boolean isSelected()
  {
    return isSelected;
  }

  public void setSelected(boolean isSelected)
  {
    this.isSelected = isSelected;
  }

  public String toString()
  {
    return label;
  }
}

// Handles rendering cells in the list using a check box

class CheckListRenderer extends JCheckBox
  implements ListCellRenderer
{
  public Component getListCellRendererComponent(
    JList list, Object value, int index,
    boolean isSelected, boolean hasFocus)
  {
    setEnabled(list.isEnabled());
    setSelected(((CheckListItem)value).isSelected());
    setFont(list.getFont());
    setBackground(list.getBackground());
    setForeground(list.getForeground());
    setText(value.toString());
    return this;
  }
}

