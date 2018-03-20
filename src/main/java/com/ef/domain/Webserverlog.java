/*
* @(#)Webserverlog.java 1.0 10/25/2017 
* Copyright (c) 2017-2018
*/

package com.ef.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Webserverlog: An hibernate entity to persist the data from the log file.
 * 
 * @author <a href="mailto:victor.woodrow@gmail.com">Victor Woodrow</a>
 * @version 1.0
 */

@Entity
@Table(name = "webserverlog")
@NamedQueries({ @NamedQuery(name = "Webserverlog.findAll", query = "SELECT w FROM Webserverlog w") })
public class Webserverlog implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	@Column(name = "ip")
	private String ip;
	@Column(name = "request")
	private String request;
	@Column(name = "status")
	private String status;
	@Column(name = "user")
	private String user;

	public Webserverlog() {
		// To create a plain record with no data
	}

	public Webserverlog(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Webserverlog)) {
			return false;
		}
		Webserverlog other = (Webserverlog) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WHubParser.Webserverlog[ id=" + id + " ]";
	}

}
