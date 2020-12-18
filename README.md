# AQS data linkage

### Data linkage work with EPA's AQS in Apache NiFi
#### Aviva Weinbaum, Civic Digital Fellow
##### Fall 2020

### Introduction
All of Us is interested in data linkages with EPA datasets, because there are many environmental determinants of health that researchers would like to study. I began with creating a linkage with the [Air Quality Service’s](https://aqs.epa.gov/aqsweb/documents/data_api.html) data because of their well-documented API. I chose to first implement linkages with the six criteria air pollutants (ozone, particulate matter, carbon monoxide, lead, sulfur dioxide, and nitrogen dioxide) because these have the most research showing negative health impacts, but any more of the service’s many measured compounds could easily be added.

### Linkage
AoU participants will be linked with all data from sites within 25 miles of their home addresses.

### Models
This linkage involves 2 databases:
- EPA: all AQS data, one table for each pollutant. See `epa.py` for schemas.
- AQS: Participant-specific linked data. See `aqs.py` for schemas.

### NiFi Pipeline
##### Overview:
The get AQS data process group collects all EPA data from a given date range and populates epa.db. The get participant-specific data process group gets the participant’s address and queries epa.db to get all data within 25 miles, and populates `aqs.db`. Failures during API calls and database queries are handled by writing the flowfile out to a local error folder.  
Open template `AQS.xml` in NiFi to see specifics.

Specifics on process groups:  
1. get AQS data  
One flowfile is generated for each state, and a script splits the given time frame by day so that the AQS API’s request limit is never reached. The API is invoked 6 times (once for each of the criteria air pollutants) and the data is split up by site. Execute SQL statements check if the entry is already in the database. If it is, the flowfile is routed to failure, and if not, the entry is added to the EPA database. The record has to be converted to Avro Record format in order to be written to the database. A DBCPConnectionPool controller also must be set up with a JDBC SQLite driver to connect to the database (the same controller can be used for the check if distinct entry process group as well).   
NOTE: Check if distinct entry and writing to the database cannot both be running at the same time, or else you will get errors that the database file is locked.
2. get participant-specific data  
This process group converts a participant’s address to URL format and then uses the [OpenCage Geocoding API](https://opencagedata.com/api) to get the latitude and longitude of that address. A script then selects all entries in the EPA database within a 25 mile radius of those coordinates. That data is then added to the AQS database, linked with the participant. Same as above, a DBCPConnectionPool controller must be set up with the AQS database.

### Next steps
- AoU will need to get their own key to access the AQS API.
- Participant IDs and addresses will need to be read into the participant-specific data process group.
- Finally, it would be good to set up better error handling.

### Files:

NiFi template:
- `AQS.xml`

Models:
- `epa.py` - model with table for each pollutant
- `aqs.py` - model linking participants with nearby sites

Scripts for NiFi pipeline:
- `epq_date_script.groovy`
- `lonlat.groovy`
- `parseaddr.groovy`
