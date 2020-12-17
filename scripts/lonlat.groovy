/*
 * Querying EPA database for all sites within 40km radius
 * of participant's address.
 *
 * Used the formula from:
 * https://www.movable-type.co.uk/scripts/latlong-db.html
 */

import groovy.sql.Sql
import java.util.*
import java.sql.*
import java.lang.Math
import java.nio.charset.*

def flowFile = session.get()

if (flowFile == null) {
    return
}

def longitude = Double.parseDouble(flowFile.getAttribute("lon"))
def latitude = Double.parseDouble(flowFile.getAttribute("lat"))

// constants that can be changed as needed:
def tablenames = ['ozone', 'pm', 'no2', 'so2', 'carbonmonoxide', 'lead']
def radius = 40
def R = 6371

def maxLat = latitude + Math.toDegrees(radius/R)
def minLat = latitude - Math.toDegrees(radius/R)
def maxLon = longitude + Math.toDegrees(Math.asin(radius/R) / Math.cos(Math.toRadians(latitude)))
def minLon = longitude - Math.toDegrees(Math.asin(radius/R) / Math.cos(Math.toRadians(latitude)))

def db = [url:'jdbc:sqlite:/Users/aviva/nifi/fitbit/epa.db', user:'aviva', password:'', driver:'org.sqlite.JDBC']
def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

for(int i = 0; i<tablenames.size(); i++) {
    def newFF = session.create(flowFile)
    def params = ["tablename":tablenames[i], "lat":Math.toRadians(latitude), "lon": Math.toRadians(longitude), "minLat":minLat, "minLon":minLon, "maxLat":maxLat, "maxLon":maxLon, "radius":radius, "R":R]
    def rows = sql.rows("Select id, site_number, latitude, longitude, sample_measurement, acos(sin($params.lat)*sin(radians(latitude)) + cos($params.lat)*cos(radians(latitude))*cos(radians(longitude)-$params.lon)) * $params.R As D\n" +
            "From\n" +
            "(select id, site_number, latitude, longitude, sample_measurement\n" +
            "from '$params.tablename'\n" +
            "Where latitude Between $params.minLat And $params.maxLat\n" +
            "And longitude Between $params.minLon And $params.maxLon) as selected\n" +
            "Where acos(sin($params.lat)*sin(radians(latitude)) + cos($params.lat)*cos(radians(latitude))*cos(radians(longitude)-$params.lon)) * $params.R < $params.radius")

    newFF = session.putAttribute(newFF, "sql", groovy.json.JsonOutput.toJson(rows))
    newFF = session.putAttribute(newFF, "tablename", tablenames[i])
    session.transfer(newFF, REL_SUCCESS)
}

// transfer original flowfile
session.transfer(flowFile, REL_FAILURE)

sql.close()