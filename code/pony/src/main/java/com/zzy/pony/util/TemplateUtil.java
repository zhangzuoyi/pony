package com.zzy.pony.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.util.IOUtils;

/**
 * 下载模板工具类
 * @author ZHANGZUOYI499
 *
 */
public class TemplateUtil {
	public static byte[] getContent(String fileName){
		String classpath = TemplateUtil.class.getClassLoader().getResource("templates").getPath();
		try {
			return IOUtils.toByteArray(new FileInputStream(new File(classpath,fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
