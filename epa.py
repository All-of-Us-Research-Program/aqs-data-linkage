from sqlalchemy import (
    Column,
    DateTime,
    Integer,
    String,
    Float
)
from sqlalchemy.ext.declarative import declarative_base
from dictalchemy import DictableModel

Base = declarative_base(cls=DictableModel)

'''
This db has all of the EPA AQS data for the 6 criteria air pollutants.
All data is collected, and later linked with participants based on location.
'''

class Ozone(Base):
    __tablename__ = "ozone"

    id = Column(Integer, primary_key=True)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
    latitude = Column(Float, nullable=True)
    longitude = Column(Float, nullable=True)

class PM(Base):
    __tablename__ = "pm"

    id = Column(Integer, primary_key=True)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
    latitude = Column(Float, nullable=True)
    longitude = Column(Float, nullable=True)

class NO2(Base):
    __tablename__ = "no2"

    id = Column(Integer, primary_key=True)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
    latitude = Column(Float, nullable=True)
    longitude = Column(Float, nullable=True)

class CarbonMonoxide(Base):
    __tablename__ = "carbonmonoxide"

    id = Column(Integer, primary_key=True)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
    latitude = Column(Float, nullable=True)
    longitude = Column(Float, nullable=True)

class Lead(Base):
    __tablename__ = "lead"

    id = Column(Integer, primary_key=True)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
    latitude = Column(Float, nullable=True)
    longitude = Column(Float, nullable=True)

class SO2(Base):
    __tablename__ = "so2"

    id = Column(Integer, primary_key=True)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
    latitude = Column(Float, nullable=True)
    longitude = Column(Float, nullable=True)

