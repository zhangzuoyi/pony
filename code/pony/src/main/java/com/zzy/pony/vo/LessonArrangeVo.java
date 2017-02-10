package com.zzy.pony.vo;

import java.util.List;

import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;

public class LessonArrangeVo {
	private Integer classId;
	private String className;
	private SchoolYear schoolYear;
	private Term term;
	private List<WeekDayVo> weekDays;
	

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public List<WeekDayVo> getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(List<WeekDayVo> weekDays) {
		this.weekDays = weekDays;
	}

	public static class WeekDayVo{
		private String weekDay;
		private String weekDayName;
		private List<PeriodVo> periods;
		public String getWeekDay() {
			return weekDay;
		}
		public void setWeekDay(String weekDay) {
			this.weekDay = weekDay;
		}
		public List<PeriodVo> getPeriods() {
			return periods;
		}
		public void setPeriods(List<PeriodVo> periods) {
			this.periods = periods;
		}
		public String getWeekDayName() {
			return weekDayName;
		}
		public void setWeekDayName(String weekDayName) {
			this.weekDayName = weekDayName;
		}
		
	}
	
	public static class PeriodVo{
		private Integer arrangeId;
		private LessonPeriod lessonPeriod;
		private Subject subject;
		private String otherLesson;
		public Integer getArrangeId() {
			return arrangeId;
		}
		public void setArrangeId(Integer arrangeId) {
			this.arrangeId = arrangeId;
		}
		public LessonPeriod getLessonPeriod() {
			return lessonPeriod;
		}
		public void setLessonPeriod(LessonPeriod lessonPeriod) {
			this.lessonPeriod = lessonPeriod;
		}
		public Subject getSubject() {
			return subject;
		}
		public void setSubject(Subject subject) {
			this.subject = subject;
		}
		public String getOtherLesson() {
			return otherLesson;
		}
		public void setOtherLesson(String otherLesson) {
			this.otherLesson = otherLesson;
		}
		
	}
}
