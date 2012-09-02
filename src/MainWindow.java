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

import edu.inforscience.graphics.*;

public class MainWindow extends JFrame {
  private Plane plane;

  public MainWindow()
  {
    super("Numeth");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(600, 400);
    setLocationRelativeTo(null);

    plane = new Plane();
    add(plane, BorderLayout.CENTER);
  }
}

