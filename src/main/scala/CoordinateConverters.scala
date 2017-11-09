package com.github.dmarcous.s2utils.converters

import com.github.dmarcous.s2utils.s2.S2Utilities
import com.google.common.geometry.{S2Cell, S2CellId, S2LatLng}
import com.vividsolutions.jts.geom.{Coordinate, Point}

/** Enclosing objects for [[com.github.dmarcous.s2utils.converters.CoordinateConverters]]
  * methods for converting coordinate objects. */
object CoordinateConverters
{

  /** Convert radian coordinates to degree coordinates
    * inclination = theta
    * azimuth = phi
    *
    *  @param radius radius length
    *  @param inclination inclination angle (theta)
    *  @param azimuth azimuth angle (phi)
    *  @return Cartesian coordinates tuple (x, y, z)
    */
  def radianCoordinatesToCartesianCoordinates(radius: Double, inclination: Double, azimuth: Double) :
    (Double, Double, Double) =
  {
    val x = radius * math.sin(inclination) * math.cos(azimuth)
    val y = radius * math.sin(inclination) * math.sin(azimuth)
    val z = radius * math.cos(inclination)

    (x, y ,z)
  }

  def radianCoordinatesToCartesianCoordinates(radianCoordinates: (Double, Double, Double)) :
    (Double, Double, Double) =
      this.radianCoordinatesToCartesianCoordinates(radianCoordinates._1, radianCoordinates._2, radianCoordinates._3)

  def polarCoordinatesToCartesianCoordinates(radius: Double, inclination: Double, azimuth: Double) :
    (Double, Double, Double) = this.radianCoordinatesToCartesianCoordinates(radius, inclination, azimuth)

  def polarCoordinatesToCartesianCoordinates(polarCoordinates: (Double, Double, Double)) :
    (Double, Double, Double) =
      this.radianCoordinatesToCartesianCoordinates(polarCoordinates)

  // Convert degree coordinates to radian coordinates
  // inclination = theta
  // azimuth = phi
  def cartesianCoordinatesToRadianCoordinates(x: Double, y: Double, z: Double) : (Double, Double, Double) =
  {
    val radius = Math.sqrt((x * x) + (y * y) + (z * z))
    val inclination = math.acos(z / radius)
    val azimuth = math.atan2(y, x)

    (radius, inclination, azimuth)
  }

  def cartesianCoordinatesToRadianCoordinates(cartesianCoordinates: (Double, Double, Double)) :
  (Double, Double, Double) =
    this.cartesianCoordinatesToRadianCoordinates(cartesianCoordinates._1, cartesianCoordinates._2,
                                                 cartesianCoordinates._3)

  def cartesianCoordinatesToPolarCoordinates(x: Double, y: Double, z: Double) : (Double, Double, Double) =
    this.cartesianCoordinatesToRadianCoordinates(x, y, z)

  def cartesianCoordinatesToPolarCoordinates(cartesianCoordinates: (Double, Double, Double)) :
    (Double, Double, Double) =
      this.cartesianCoordinatesToRadianCoordinates(cartesianCoordinates)

  // Converts 2d cartesian coordiantes to 3d adjusted coordiantes to be used with geo functions
  def cartesianCoordinates2dTo3d(x: Double, y: Double) : (Double, Double, Double) =
  {
    val radianX = UnitConverters.degreesToRadians(x)
    val radianY = UnitConverters.degreesToRadians(y)

    val adjustedX = math.cos(radianY)* math.cos(radianX)
    val adjustedY = math.cos(radianY)* math.sin(radianX)
    val adjustedZ = math.sin(radianY)

    (adjustedX, adjustedY, adjustedZ)
  }

  def cartesianCoordinates2dTo3d(cartesianCoordinates: (Double, Double)) :
  (Double, Double, Double) =
    this.cartesianCoordinates2dTo3d(cartesianCoordinates._1, cartesianCoordinates._2)

  // Convert adjusted 3d Cartesian coordinates to 2d radian coordianted
  def adjusted3dCartesianCoordiantesTo2dRadianCoordiantes(x: Double, y: Double, z: Double) : (Double, Double) =
  {
    val lon = math.atan2(y, x)
    val hyp = math.sqrt((x * x) + (y * y))
    val lat = math.atan2(y, hyp)

    (lon, lat)
  }

  def adjusted3dCartesianCoordiantesTo2dRadianCoordiantes(cartesianCoordinates: (Double, Double, Double)) :
    (Double, Double) = this.adjusted3dCartesianCoordiantesTo2dRadianCoordiantes(
      cartesianCoordinates._1, cartesianCoordinates._2, cartesianCoordinates._3)

  // Converts a longitude and latitude to S2CellId object
  def lonLatToS2CellID(lon: Double, lat: Double, lvl: Int): S2CellId =
  {
    S2CellId.fromLatLng(S2LatLng.fromDegrees(lat, lon)).parent(lvl)
  }
  // x = lon, y=lat
  def coordinateToS2CellID(coordinate: Coordinate, lvl: Int): S2CellId =
  {
    this.lonLatToS2CellID(coordinate.x, coordinate.y, lvl)
  }
  def pointToS2CellID(point: Point, lvl: Int): S2CellId =
  {
    this.lonLatToS2CellID(point.getX, point.getX, lvl)
  }

  // Converts a longitude and latitude to S2Cell object
  def lonLatToS2Cell(lon: Double, lat: Double, lvl: Int): S2Cell =
  {
    new S2Cell(this.lonLatToS2CellID(lon, lat, lvl))
  }
  // x = lon, y=lat
  def coordinateToS2Cell(coordinate: Coordinate, lvl: Int): S2Cell =
  {
    this.lonLatToS2Cell(coordinate.x, coordinate.y, lvl)
  }
  def pointToS2Cell(point: Point, lvl: Int): S2Cell =
  {
    this.lonLatToS2Cell(point.getX, point.getX, lvl)
  }

  def lonLatToS2CellFullToken(lon: Double, lat: Double, lvl: Int): String =
  {
    S2Utilities.getS2CellFullToken(this.lonLatToS2CellID(lon, lat, lvl))
  }
  // x = lon, y=lat
  def coordinateToS2CellFullToken(coordinate: Coordinate, lvl: Int): String =
  {
    this.lonLatToS2CellFullToken(coordinate.x, coordinate.y, lvl)
  }
  def pointToS2CellFullToken(point: Point, lvl: Int): String =
  {
    this.lonLatToS2CellFullToken(point.getX, point.getX, lvl)
  }

}