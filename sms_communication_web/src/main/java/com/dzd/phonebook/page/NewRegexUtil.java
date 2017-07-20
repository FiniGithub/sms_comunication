package com.dzd.phonebook.page;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewRegexUtil
{
  public static boolean elevenNumber(String mobile)
  {
    boolean flag = false;
    try {
      String regex = "^((13[0-9])|(17[0,6,7,8])|(15[^4,\\D])|(18[0-9])|147)\\d{8}$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(mobile);
      flag = matcher.matches();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }

  public static void main(String[] args) {
    boolean flag = elevenNumber("13714285381");
    System.out.println(flag);
  }
}