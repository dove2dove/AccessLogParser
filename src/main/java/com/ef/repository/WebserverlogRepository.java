/*
* @(#)WebserverlogRepository.java 1.0 10/25/2017 
* Copyright (c) 2017-2018
*/

package com.ef.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ef.domain.Parserresult;
import com.ef.domain.Webserverlog;

/**
 * WebserverlogRepository: This interface extends the Spring data interface
 * (CrudRepository) to provide CRUD functions for managing the Webserverlog
 * table.
 * 
 * @author <a href="mailto:victor.woodrow@gmail.com">Victor Woodrow</a>
 * @version 1.0
 */

@Repository
public interface WebserverlogRepository extends CrudRepository<Webserverlog, Long> {

	@Query("SELECT new com.ef.domain.Parserresult(v.ip, count(v.ip)) FROM Webserverlog v where date between :startdate and :enddate group by v.ip")
	public List<Parserresult> findIPCountbyDate(@Param("startdate") Date startdate, @Param("enddate") Date enddate);
}
