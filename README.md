# AQS data linkage

### Data linkage work with EPA's AQS in Apache NiFi

Files:

NiFi template:
- AQS.xml

Models:
- epa.py - model with table for each pollutant
- aqs.py - model linking participants with nearby sites

Scripts for NiFi flow:
- epq_date_script.groovy
- lonlat.groovy
- parseaddr.groovy
