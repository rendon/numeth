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
package edu.inforscience.graphics;

public class Point2D {
  private double  px,
    py;

  public Point2D(double x, double y)
  {
    px = x;
    py = y;
  }

  public Point2D(Point2D point)
  {
    px = point.x();
    py = point.y();
  }

  public void setX(double x) { px = x; }
  public void setY(double y) { py = y; }

  public double x() { return px; }
  public double y() { return py; }
  public double distanceTo(Point2D point)
  {
    return Math.sqrt((px - point.x()) * (px - point.x()) +
                     (py - point.y()) * (py - point.y()));
  }

  // Returns the distance from origin to this point
  public double distance()
  {
    return Math.sqrt(x() * x() + y() * y());
  }
}
