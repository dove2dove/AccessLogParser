/*
* @(#)ParserresultRepository.java 1.0 10/25/2017 
* Copyright (c) 2017-2018
*/

package com.ef.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ef.domain.Parserresult;

/**
 * ParserresultRepository: This interface extends the Spring data interface
 * (CrudRepository) to provide CRUD functions for managing the Parserresult
 * table.
 * 
 * @author <a href="mailto:victor.woodrow@gmail.com">Victor Woodrow</a>
 * @version 1.0
 */

@Repository
public interface ParserresultRepository extends CrudRepository<Parserresult, Integer> {
	// This is an interface that extends an implemented class
}
