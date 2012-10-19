/*
    Numeth is simple application to solve many mathematical problems numerically.

    Copyright (C) 2012 Rafael Rendon Pablo <smart.rendon@gmail.com>

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
package edu.inforscience.math;

import edu.inforscience.lang.Function;
import edu.inforscience.lang.Parser;

import java.util.ArrayList;

public class BruteForce {

  private Function function;
  private Parser parser;

  public BruteForce()
  {
    parser = new Parser();
  }

  public BruteForce(Function f)
  {
    function = f;
    parser = new Parser();
  }

  public double f(double x)
  {
    return parser.evaluate(function, "x", x);
  }


  /**
   * Returns the real solutions of f(x) in the closed interval [a, b].
   * @param a start of interval, inclusive
   * @param b end of interval, inclusive
   */
  public ArrayList<Solution> solve(double a, double b)
  {
    double x, dx = 0.5, prev, current;
    ArrayList<Solution> solutions = new ArrayList<Solution>();

    prev = f(a);
    for (x = a + dx; x <= b; x += dx) {
      current = f(x);

      if (Math.sign(prev) != Math.sign(current)) {
        solutions.add(new Solution(x - dx, x, x - 0.5 * dx));
      }
      prev = current;
    }

    return solutions;
  }

}

