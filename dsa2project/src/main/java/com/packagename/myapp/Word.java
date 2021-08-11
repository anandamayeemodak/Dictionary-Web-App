package com.packagename.myapp;

public class Word {
   String type="";
   String wordx="";
   String meaning="";
   String synonym="";
   int height=0;
   Word leftNode=null;
   Word rightNode=null; 
 Word(){}
 Word(String w,String m,String s,String t)
 {
	 type = t;
	 wordx = w;
	 meaning = m;
	 synonym = s;
	 int height=0;
	 Word leftNode=null;
	 Word rightNode=null;	
 }
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getWordx() {
	return wordx;
}

public void setWordx(String wordx) {
	this.wordx = wordx;
}

public String getMeaning() {
	return meaning;
}

public void setMeaning(String meaning) {
	this.meaning = meaning;
}

public String getSynonym() {
	return synonym;
}

public void setSynonym(String synonym) {
	this.synonym = synonym;
}

}
