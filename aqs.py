from sqlalchemy import (
    Column,
    DateTime,
    Integer,
    String,
    Float,
    ForeignKey
)
from sqlalchemy.ext.declarative import declarative_base
from dictalchemy import DictableModel

Base = declarative_base(cls=DictableModel)

''' 
aqs entries for participants from sites within 25 miles of their address
'''

class AQSParticipant(Base):

    __tablename__ = "aqs_participant"

    id = Column("id", Integer, primary_key=True, autoincrement=False)
    participantId = Column("participant_id", Integer, ForeignKey("participant.participant_id"), nullable=False)
    pollutantName = Column("pollutant_name", String, nullable=False)
    site_number = Column(Integer, nullable=False)
    county = Column(String, nullable=False)
    state = Column(String, nullable=False)
    date_local = Column(DateTime, nullable=False)
    sample_measurement = Column(Float, nullable=False)
