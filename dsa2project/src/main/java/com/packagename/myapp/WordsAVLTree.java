package com.packagename.myapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordsAVLTree
{
	int wordCount;
	int searchCount;
	boolean added;
	Word root;
	ArrayList<String> al=new ArrayList<String>();
	
	WordsAVLTree()
	{
	 wordCount = 0;	
	 searchCount = 0;
	 root = null;
	}
	int returnCount()
	{
		return wordCount;
	}
	int returnSearchCount()
	{
		return searchCount;
	}
	int height(Word root) {
		int leftHt, rightHt;
		if(root== null) {
			return 0;
		}
		else {
			if(root.leftNode== null) {
				leftHt=0;
			}
			else {
				leftHt= 1 + root.leftNode.height;
			}
			if(root.rightNode== null) {
				rightHt=0;
			}
			else {
				rightHt= 1 + root.rightNode.height;
			}			
		}
		
		if(leftHt > rightHt) {
			return leftHt;
		}
		else return rightHt;
	}
	
	
	
	public boolean insertWord(Word Node)  
    {  
        root = insertWord(root,Node); 
        if(added) {wordCount++;}
        return added;
    } 
	
	int balanceFactor(Word root){
		int leftHt, rightHt, balFac;
		if(root == null) {
			return 0;
		}
		else {
			if(root.leftNode== null) {
				leftHt=0;
			}
			else {
				leftHt= 1 + height(root.leftNode);
			}
			if(root.rightNode== null) {
				rightHt=0;
			}
			else {
				rightHt= 1 + height(root.rightNode);
			}
		}
		
		
		balFac = leftHt- rightHt; 
		return balFac;
		
	}
	
	public Word LL(Word root) { 
		Word temp;
		temp= root.leftNode;
		root.leftNode= temp.rightNode;
		temp.rightNode=root;
		temp.height= height(temp);
		root.height= height(root);
		return temp;
		
	}
	public Word RR(Word root) {
		Word temp;
		temp = root.rightNode;
		root.rightNode= temp.leftNode;		 
		temp.leftNode= root;
		temp.height= height(temp);
		root.height= height(root);
		return temp;
	}
	public Word LR(Word root) {
		root.leftNode= RR(root.leftNode);
		root = LL(root);
		return root;
	}
	public Word RL(Word root) {
		root.rightNode= LL(root.rightNode);
		root = RR(root);
		return root;
	
	}
	public Word insertWord(Word current, Word WordAdded) {
		added = true;
		if(current==null) {
			current= WordAdded;
			return current;
		} 
		if(WordAdded.wordx.compareToIgnoreCase(current.wordx) < 0) {
			current.leftNode= insertWord(current.leftNode, WordAdded);
			int bal = balanceFactor(current);
			if(bal==2) {
				if(WordAdded.wordx.compareToIgnoreCase(current.leftNode.wordx) < 0) {
					current = LL(current);
				}
				else current = LR(current);
				
			}
		}
		
		else if(WordAdded.wordx.compareToIgnoreCase(current.wordx) > 0){
			current.rightNode= insertWord(current.rightNode, WordAdded);
			int bal = balanceFactor(current);
			if(bal== -2) {
				if(WordAdded.wordx.compareToIgnoreCase(current.rightNode.wordx) > 0) {
					current = RR(current);
				}
				else current = RL(current);				
			}
		}
		else if(WordAdded.wordx.compareToIgnoreCase(current.wordx) == 0) {
			added = false;
		}
		current.height= height(current);
		return current;
	}

	public String searchWord(String searchW)  
    { 
		searchCount++;
		String returnedStr = searchWord(root, searchW);
        if(returnedStr!=null)
        {
        	searchCount--;
        }
        return returnedStr;
    }  
	
	private String searchWord(Word head, String searchW)  
    {  
        boolean check = false;  
        while ((head != null) && !check)  
        {  
            String headWord = head.wordx;  
            if (searchW.compareToIgnoreCase(headWord) < 0)   //element < headElement
                head = head.leftNode;  
            else if (searchW.compareToIgnoreCase(headWord) > 0)  
                head = head.rightNode;  
            else  
            {  
                check = true;  
                break;  
            }  
            searchWord(head, searchW);  
        }  
        if(check)
        {
        String retStr = "("+head.type+") "+"\nMeaning: " + head.meaning + "\nSynonym: " + head.synonym;
        return retStr;  
        }
        else {return null;}
    }  
	
	
	
	
	
	
	public Word wordExists(String searchW)  
    { 
		searchCount++;
		Word existing = wordExists(root, searchW);
    
        return existing;
    }  
	
	private Word wordExists(Word head, String searchW)  
    {  
        boolean check = false;  
        while ((head != null) && !check)  
        {  
            String headWord = head.wordx;  
            if (searchW.compareToIgnoreCase(headWord) < 0)   //element < headElement
                head = head.leftNode;  
            else if (searchW.compareToIgnoreCase(headWord) > 0)  
                head = head.rightNode;  
            else  
            {  
                check = true;  
                break;  
            }  
            wordExists(head, searchW);  
        }  
        if(check)
        {
        return head;  
        }
        else {return null;}
    }  
	
	
	
	public void addSynonym(String existingWord, String newSynonym)
	{
		 Word ret = wordExists(existingWord);
		 ret.synonym = ret.synonym + ", " + newSynonym;
	}
	
	      //arraylist of lists
    //declare this in class AVLWordTree
		   //list which will be added to al

	public void create() 
	{
		String w,m,t,s;
		File file=new File("wordsDatabase.txt");
		Word newNode=new Word();
		try
		{
			Scanner sc=new Scanner(file);
			sc.useDelimiter("-|\n");
			while(sc.hasNext())
			{
				w=sc.next();
				m=sc.next();
				t=sc.next();
				s=sc.next();
				newNode=new Word(w,m,s,t);
				root=insertWord(root,newNode);
			}
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}
	}
	
public ArrayList<String> display(Word currentNode)
	{
		if(currentNode!= null)
		 {
			al=display(currentNode.leftNode);
			al.add(currentNode.wordx);
			al.add(currentNode.meaning);
			al.add(currentNode.synonym);
			al.add(currentNode.type);
			al=display(currentNode.rightNode);
		 }
		return al;
	}

}
