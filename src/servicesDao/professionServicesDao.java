package servicesDao;

import java.util.List;

import exception.myException;

import pojo.profession;

public interface professionServicesDao {
List<profession> select(String condition,List<Object> values)throws myException;
profession saveProfession(profession profession)throws myException;

}
