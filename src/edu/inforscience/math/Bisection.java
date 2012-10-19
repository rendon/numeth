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

public class Bisection {

  public static final int MAX_ITERATIONS = 200;
  private Function function;
  private Parser parser;

  public Bisection(Function f)
  {
    function = f;
    parser = new Parser();
  }

  public double f(double x)
  {
    return parser.evaluate(function, "x", x);
  }


  public Solution find(double min, double max, double epsilon, int iterations)
  {
    if (iterations == MAX_ITERATIONS)
      return new Solution(min, max, (min + max)/2);

    double x = (min + max)/2;
    double fx = f(x);

    if (Math.abs(fx) < epsilon) {
      return new Solution(min, max, x);
    } else {
      double fa = f(min);

      if (Math.sign(fa) != Math.sign(fx))
        return find(min, x, epsilon, iterations + 1);
      else
        return find(x, max, epsilon, iterations + 1);
    }
  }

  public ArrayList<Solution> solve(double a, double b, double epsilon)
  {
    BruteForce bruteForce = new BruteForce(function);
    ArrayList<Solution> possibleIntervals = bruteForce.solve(a, b);

    ArrayList<Solution> roots = new ArrayList<Solution>();

    for (int i = 0; i < possibleIntervals.size(); i++) {
      Solution sol = possibleIntervals.get(i);
      roots.add(find(sol.getA(), sol.getB(), epsilon, 0));
    }

    return roots;
  }
}

