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

import edu.inforscience.lang.Parser;

import java.io.*;

public class Numeth {

  public static void main(String[] args) throws IOException
  {
    //MainWindow window = new MainWindow();
    //window.setVisible(true);

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.print(">> ");
    String expression = reader.readLine();
    Parser parser = new Parser(expression);

    System.out.println("x\t\tf(x)");
    for (double x = -5; x <= 5; x += 0.5) {
      parser.setVariable("x", x);
      System.out.println(x + "  " + parser.evaluate());
    }

    //System.out.println(parser.evaluate("2^0/0! - 2^2/2! + 2^4/4! - 2^6/6! + 2^8/8! - 2^10/10!"));

  }
}

