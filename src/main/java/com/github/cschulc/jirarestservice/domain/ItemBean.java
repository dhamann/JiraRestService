package com.github.cschulc.jirarestservice.domain;

import com.google.gson.annotations.Expose;

public class ItemBean {

	@Expose
	private String	field;
	@Expose
	private String	fieldtype;
	@Expose
	private String	from;
	@Expose
	private String	fromString;
	@Expose
	private String	to;
	@Expose
	private String	toString;

	public void setField(String field) {

		this.field = field;
	}

	public void setFieldtype(String fieldtype) {

		this.fieldtype = fieldtype;
	}

	public void setFrom(String from) {

		this.from = from;
	}

	public void setFromString(String fromString) {

		this.fromString = fromString;
	}

	public void setTo(String to) {

		this.to = to;
	}

	public void setToString(String toString) {

		this.toString = toString;
	}

	public String getField() {

		return field;
	}

	public String getFieldtype() {

		return fieldtype;
	}

	public String getFrom() {

		return from;
	}

	public String getFromString() {

		return fromString;
	}

	public String getTo() {

		return to;
	}

	public String getToString() {

		return toString;
	}
}
