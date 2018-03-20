/*
* @(#)Parserresult.java 1.0 10/25/2017 
* Copyright (c) 2017-2018
*/

package com.ef.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Parserresult: An hibernate entity to persist the result of search.
 * 
 * @author <a href="mailto:victor.woodrow@gmail.com">Victor Woodrow</a>
 * @version 1.0
 */

@Entity
@Table(name = "parserresult")
@NamedQueries({ @NamedQuery(name = "Parserresult.findAll", query = "SELECT p FROM Parserresult p") })
public class Parserresult implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@Column(name = "IP")
	private String ip;
	@Basic(optional = false)
	@Column(name = "noofrequest")
	private int noofrequest;
	@Column(name = "reasonforblocking")
	private String reasonforblocking;

	public Parserresult() {
		// To create a plain record with no data
	}

	public Parserresult(Integer id) {
		this.id = id;
	}

	public Parserresult(String ip, long noofrequest) {
		this.ip = ip;
		this.noofrequest = (int) noofrequest;
	}

	public Parserresult(Integer id, String ip, int noofrequest) {
		this.id = id;
		this.ip = ip;
		this.noofrequest = noofrequest;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getNoofrequest() {
		return noofrequest;
	}

	public void setNoofrequest(int noofrequest) {
		this.noofrequest = noofrequest;
	}

	public String getReasonforblocking() {
		return reasonforblocking;
	}

	public void setReasonforblocking(String reasonforblocking) {
		this.reasonforblocking = reasonforblocking;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Parserresult)) {
			return false;
		}
		Parserresult other = (Parserresult) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WHubParser.Parserresult[ id=" + id + " ]";
	}

}
