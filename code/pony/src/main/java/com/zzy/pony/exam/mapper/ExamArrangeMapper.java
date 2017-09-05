package com.zzy.pony.exam.mapper;

import java.util.List;
import java.util.Map;

public interface ExamArrangeMapper {
	List<Map<String, String>> timeList(Integer examId);
}
