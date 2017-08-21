package com.zzy.pony.util.activiti;

import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

public class Variable {

    private String key;
    private String value;
    private String type;

    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> addVariableMap(Map<String, Object> vars) {
        ConvertUtils.register(new DateConverter(), java.util.Date.class);

        if (StringUtils.isBlank(key)) {
            return vars;
        }

        Class<?> targetType = Enum.valueOf(PropertyType.class, type).getValue();
        Object objectValue = ConvertUtils.convert(value, targetType);
        vars.put(key, objectValue);
        return vars;
    }

}
