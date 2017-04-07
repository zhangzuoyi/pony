package com.zzy.pony.AutoClassArrange;

import java.util.ArrayList;
import java.util.List;

import com.zzy.pony.util.GAUtil;

public class Test {

	public static void main(String[] args){
		DNA dna = DNA.getInstance();
		String[] teacherIdCandidate;  
		String[] subjectIdCandidate;
		String[] weekdayIdCandidate;
		String[] seqIdCandidate;
		String[] classIdCandidate;
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		List<Integer> list3 = new ArrayList<Integer>();
		List<Integer> list4 = new ArrayList<Integer>();
		List<Integer> list5 = new ArrayList<Integer>();

		for (int i = 0; i < 10; i++) {
			list.add(new Integer(i));
		}		
		teacherIdCandidate = GAUtil.getCandidateStrings(list, 4);
		for (int i = 0; i < 10; i++) {
			list2.add(new Integer(i));
		}		
		subjectIdCandidate = GAUtil.getCandidateStrings(list, 2);
		for (int i = 0; i < 10; i++) {
			list3.add(new Integer(i));
		}		
		weekdayIdCandidate = GAUtil.getCandidateStrings(list, 1);
		for (int i = 0; i < 10; i++) {
			list4.add(new Integer(i));
		}		
		seqIdCandidate = GAUtil.getCandidateStrings(list, 1);
		for (int i = 0; i < 10; i++) {
			list5.add(new Integer(i));
		}		
		classIdCandidate = GAUtil.getCandidateStrings(list, 3);
		
		dna.setClassIdCandidate(classIdCandidate);
		dna.setSeqIdCandidate(seqIdCandidate);
		dna.setSubjectIdCandidate(subjectIdCandidate);
		dna.setTeacherIdCandidate(teacherIdCandidate);
		dna.setWeekdayIdCandidate(weekdayIdCandidate);
		dna.getDnaString(2);
		dna.getDnaString(2);
		DNA dna2 = DNA.getInstance();
		//00030000813000900102290003002067500010030213000400402810005005076000020060659000800705260004008098400010090477
		
	}
	
}
