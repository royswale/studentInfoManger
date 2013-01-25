package dao;

import java.util.List;

import pojo.profession;
import pojo.student;
import exception.myException;

public interface professionDao  extends dao{
	profession save(profession profession)throws myException;
	int updateProfession(int profession_id,profession newProfession)throws myException;
	List<profession> findProfessions(String conidtion,List<Object> values)throws myException;
	int deleteProfession(String conidtion,List<Object> values)throws myException;

}
