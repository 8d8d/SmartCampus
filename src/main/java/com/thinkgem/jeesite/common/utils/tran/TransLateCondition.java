package com.thinkgem.jeesite.common.utils.tran;



public class TransLateCondition {
	
	
	public TransLateCondition(String key, String table, String colName,
			String tableKey,String...strings) {
		super();
		this.key = key;
		this.table = table;
		this.colName = colName;
		this.tableKey = tableKey;
		this.otherParam = strings;
	}
	/**
	 * 根据key从List中查询出需要翻译的字段
	 */
	private String key;
	/**
	 * 从哪张表翻译
	 */
	private String table;
	/**
	 * 需要取哪几列字段
	 */
	private String colName;
	/**
	 * 翻译表的哪一列去匹配
	 */
	private String tableKey;
	
	private String[] otherParam;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getTableKey() {
		return tableKey;
	}

	public void setTableKey(String tableKey) {
		this.tableKey = tableKey;
	}

	public String[] getOtherParam() {
		return otherParam;
	}

	public void setOtherParam(String[] otherParam) {
		this.otherParam = otherParam;
	}
	
}
