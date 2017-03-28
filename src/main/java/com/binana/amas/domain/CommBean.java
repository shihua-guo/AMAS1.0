package com.binana.amas.domain;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

import com.binana.amas.domain.enumeration.College;

/**
 * 同于存放报表的功用类
 * @author shihua
 *
 */
/*@SqlResultSetMapping(
    name="collegePieMapping",
    classes={
        @ConstructorResult(
            targetClass=CommBean.class,
            columns={
                @ColumnResult(name="name",type=String.class),
                @ColumnResult(name="value",type=Long.class)
            }
        )
    }
)*/
public class CommBean {
	private String name;
	private long value;
	public CommBean(String name, long value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CommBean [name=" + name + ", value=" + value + "]";
	}
	
}
