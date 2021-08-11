package com.packagename.myapp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import com.helger.commons.error.list.ErrorList;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Select;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import ch.qos.logback.core.layout.EchoLayout;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed
     *            bean.
     */
	//@Autowired GreetService service
	ArrayList<Word> wordsList = new ArrayList<Word>();
	int searchCount = 0;
	Grid <Word> wordGrid = new Grid<>(Word.class);
	{
	wordGrid.setColumns("type","wordx", "meaning", "synonym");
	wordGrid.getColumnByKey("type").setHeader("Type");
	wordGrid.getColumnByKey("wordx").setHeader("Word");
	wordGrid.getColumnByKey("meaning").setHeader("Meaning");
	wordGrid.getColumnByKey("synonym").setHeader("Synonym");
	}
	 
    public MainView() {

    	
    	WordsAVLTree dictionary = new WordsAVLTree();
    	dictionary.create();
    	
    	add(
    			buildAppLayout(dictionary,wordGrid));
    }

	private Component buildAppLayout(WordsAVLTree dictionary, Grid<Word> wordGrid) {
		AppLayout tabLayout = new AppLayout(); 
		VerticalLayout pageLayout = new VerticalLayout(); 
		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.add(new Icon(VaadinIcon.NOTEBOOK),
				new H2("POCKET DICTIONARY"));
		titleLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		Tab Home = new Tab("Home");
		Div page1 = new Div();
		page1.add(titleLayout,
    			buildForm(dictionary),
    			addSynonym(dictionary),
    			searchWord(dictionary),
    			updateDisplay(dictionary),
    			wordGrid);
		
		VerticalLayout statsPageLayout = new VerticalLayout();
		VerticalLayout QuizLayout = new VerticalLayout();
		QuizLayout.setClassName("Quiz");
		QuizLayout.setWidth("70%");

		
		RadioButtonGroup<String> radioGroup1 = new RadioButtonGroup<>();
		RadioButtonGroup<String> radioGroup2 = new RadioButtonGroup<>();
		RadioButtonGroup<String> radioGroup3 = new RadioButtonGroup<>();
		RadioButtonGroup<String> radioGroup4 = new RadioButtonGroup<>();
		RadioButtonGroup<String> radioGroup5 = new RadioButtonGroup<>();
		
		radioGroup1.setItems("queue","cue");
		radioGroup2.setItems("fazed","phased");
		radioGroup3.setItems("compliments","complements");
		radioGroup4.setItems("symbols","cymbals");
		radioGroup5.setItems("board","bored");
		
		Button Submit = new Button("SUBMIT");
		Submit.setThemeName("Primary");
		Submit.addClickListener(click-> {
			int score=0;
			Map<String,String> Answers = new HashMap<>(); //creating hashmap for answers
			Answers.put("ans1", "queue");
			Answers.put("ans2", "fazed");
			Answers.put("ans3", "compliments");
			Answers.put("ans4", "symbols");
			Answers.put("ans5", "bored");
			
			if(radioGroup1.getValue() == Answers.get("ans1")) {score++;}
			else {radioGroup1.setHelperText("Right answer is: " + Answers.get("ans1"));}
			if(radioGroup2.getValue() == Answers.get("ans2")) {score++;}
			else {radioGroup2.setHelperText("Right answer is: " + Answers.get("ans2"));}
			if(radioGroup3.getValue() == Answers.get("ans3")) {score++;}
			else {radioGroup3.setHelperText("Right answer is: " + Answers.get("ans3"));}
			if(radioGroup4.getValue() == Answers.get("ans4")) {score++;}
			else {radioGroup4.setHelperText("Right answer is: " + Answers.get("ans4"));}
			if(radioGroup5.getValue() == Answers.get("ans5")) {score++;}
			else {radioGroup5.setHelperText("Right answer is: " + Answers.get("ans5"));}
			
			Button close = new Button("CLOSE");
			close.setThemeName("Tertiary");
			VerticalLayout scoreNotifLayout = new VerticalLayout();
			if(score<4)
			{
			scoreNotifLayout.setClassName("badScore");
			scoreNotifLayout.add(new Text("Err! Looks like you need more practice!"),new H3(" SCORE: " + score + "/5"),close);
			}
			else
			{
			 scoreNotifLayout.setClassName("goodScore");
			 scoreNotifLayout.add(new Text("Well Done!"),new H3(" SCORE: " + score + "/5"),close);
			}
			Notification ScoreNotif = new Notification(scoreNotifLayout);
			ScoreNotif.setPosition(Position.MIDDLE);
			ScoreNotif.open();
			close.addClickListener(event -> ScoreNotif.close());	 
			
			
		});
		H2 QuizTitle = new H2("QUIZ TIME!");
		QuizTitle.setId("quizTitle");
		QuizTitle.setWidth("100%");
		QuizTitle.setHeight("20%");
		
		
		QuizLayout.add(QuizTitle,
				new Text("1. The _______  at the Bank was very long."),
				radioGroup1,
				new Text("2. Rebecca was not ________ by the loud noise next door."),
				radioGroup2,
				new Text("3. Your Hat ______ your outfit very nicely."),
				radioGroup3,
				new Text("4. Before taking your driving test, make sure you know all of the ______  that appear on Road signs."),
				radioGroup4,
				new Text("5. I was so _______  during the film that I fell asleep."),
				radioGroup5,
				Submit
				);
		HorizontalLayout tipTitle = new  HorizontalLayout();
		tipTitle.add(new Icon(VaadinIcon.CLUSTER),new H2(" TIP OF THE DAY!"));
		tipTitle.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		
		VerticalLayout tipCard = new VerticalLayout(tipTitle, new H3("Develop a reading habit:"),new Text(" Vocabulary building is easiest when you encounter words in context. "));
		tipCard.setClassName("tipOfTheDay");
		tipCard.setWidth("60%");
		tipCard.setHeight("50%");	
		
		HorizontalLayout wordTitle = new  HorizontalLayout();
		wordTitle.add(new Icon(VaadinIcon.ACADEMY_CAP),new H2(" WORD OF THE DAY!"));
		wordTitle.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		VerticalLayout wordCard = new VerticalLayout(wordTitle,new H3("WORD: ("+dictionary.root.type+") "+ dictionary.root.wordx),
				new H4("MEANING: "+dictionary.root.meaning),
				new H4("SYNONYM: "+dictionary.root.synonym));
		wordCard.setClassName("tipOfTheDay");
		wordCard.setWidth("60%");
		VerticalLayout rightCards = new VerticalLayout(tipCard,wordCard);
		
		HorizontalLayout Cards = new  HorizontalLayout();
		Cards.add(QuizLayout,rightCards);
		
		//Text TitleText = new Text("QUIZ TIME!");
		
		statsPageLayout.add(
			    new H3("YOUR PROGRESS : "),
			    new Text("WORDS ADDED: "),
				AddProgress(dictionary),
				new Text("WORDS SEARCHED TODAY: "),
				SearchProgress(dictionary),
				Cards);
		
		Tab Stats = new Tab("Stats");
		Div page2 = new Div();
		page2.add(statsPageLayout);
		page2.setVisible(false);
		
    	Map<Tab, Component> tabsToPages = new HashMap<>();
    	tabsToPages.put(Home, page1);
    	tabsToPages.put(Stats, page2);
		
    	Tabs tabs = new Tabs(Home, Stats);
    	Div pages = new Div(page1, page2);
    	
    	
    	tabs.addSelectedChangeListener(event -> {
    	    tabsToPages.values().forEach(page -> page.setVisible(false));
    	    Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
    	    selectedPage.setVisible(true);
    	});
    	
    	
    	
    	tabLayout.addToNavbar(tabs);
    	pageLayout.add(pages);
		
		Div AppWrapperLayout = new Div(tabLayout,pages);
		AppWrapperLayout.setWidth("100%");
		return AppWrapperLayout;
	}

	private Component addSynonym(WordsAVLTree obj) {
		HorizontalLayout addSyn = new HorizontalLayout();
		TextField existingWord = new TextField("Exisitng Word");
		TextField newS = new TextField("New Synonym");
		Button addSButton = new Button("ADD SYNONYM");
		addSButton.setThemeName("Primary");
		Binder<Word> SearchWordBinder = new Binder<>(Word.class);
		SearchWordBinder.forField(existingWord).bind("wordx");
		SearchWordBinder.forField(newS).bind("synonym");
		
		SearchWordBinder.readBean(new Word());
		
		
		addSButton.addClickListener(click -> {
			
            try {
				
				Word SearchedWord = new Word();
				SearchWordBinder.writeBean(SearchedWord);
				
				if(obj.searchWord(SearchedWord.wordx)!=null)
				{
					obj.addSynonym(SearchedWord.wordx, SearchedWord.synonym);
					 Button close = new Button("Close");
					 VerticalLayout searchNotifLayout = new VerticalLayout();
					 searchNotifLayout.add(new H6("Synonym Added!"),close);
					 Notification SearchNotif = new Notification(searchNotifLayout);
					 SearchNotif.setPosition(Position.MIDDLE);
					 SearchNotif.open();
					 close.addClickListener(event -> SearchNotif.close());
				}
				else
				{
					 Button close = new Button("Close");
					 VerticalLayout searchNotifLayout = new VerticalLayout();
					 searchNotifLayout.add(new H6(" Word Not Found!"),close);
					 Notification SearchNotif = new Notification(searchNotifLayout);
					 SearchNotif.setPosition(Position.MIDDLE);
					 SearchNotif.open();
					 close.addClickListener(event -> SearchNotif.close());
				}
            }
            catch (ValidationException e) {
				// TODO Auto-generated catch block
				add(new Html(e.getValidationErrors().stream().map(err->
				"<p>"+ err.getErrorMessage() + "</p>").collect(Collectors.joining("\n"))));
//				e.printStackTrace();
			}
		});
		
		addSyn.add(existingWord,newS,addSButton);
		addSyn.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		Div AddSynWrapperLayout = new Div(addSyn);
		AddSynWrapperLayout.setWidth("100%");
		return AddSynWrapperLayout;
	}

	private Component SearchProgress(WordsAVLTree obj) 
	{
		
		ProgressBar SearchProgressBar = new ProgressBar(0, 10, searchCount);
		SearchProgressBar.setWidth("60%");
		SearchProgressBar.setThemeName("Primary");
		Text fraction = new Text(searchCount+"/10");
		Button SProgressButton = new Button("UPDATE");
		SProgressButton.setThemeName("Primary");
		SProgressButton.addClickListener(click-> {
			SearchProgressBar.setValue(searchCount);
			fraction.setText(SearchProgressBar.getValue()+"/10");
		});
		
		HorizontalLayout SProgressReport = new HorizontalLayout(SearchProgressBar,fraction,SProgressButton);
		SProgressReport.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		
		Div SPWrapperLayout = new Div(SProgressReport);
		SPWrapperLayout.setWidth("100%");
		return SPWrapperLayout;
		
	}

	private Component updateDisplay(WordsAVLTree obj) {
		// TODO Auto-generated method stub
		H3 updation = new H3("UPDATE DISPLAY");
		
		Icon refreshIcon = new Icon(VaadinIcon.REFRESH);
		refreshIcon.addClickListener(click -> {
			
			ArrayList<String> retList = obj.display(obj.root);
			Iterator<String> it = retList.iterator();
			ArrayList<Word> prevList = new ArrayList<Word>();
			prevList = wordsList;
			wordGrid.scrollToIndex(prevList.size()+1);
			wordsList.clear();
			while(it.hasNext())
			{
			wordsList.add(new Word(it.next().toString(),it.next().toString(),it.next().toString(),it.next().toString()));
			}
			
			wordGrid.setItems(wordsList);
			
		   
		});
		
		HorizontalLayout gridDispLayout = new HorizontalLayout(updation,refreshIcon);
		gridDispLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		Div DWrapperLayout = new Div(gridDispLayout);
		DWrapperLayout.setWidth("100%");
		return DWrapperLayout;
	}

	private Component AddProgress(WordsAVLTree obj) {
		
		int wordCount=obj.returnCount();
		ProgressBar AddProgressBar = new ProgressBar(0, 20, wordCount);
		AddProgressBar.setWidth("80%");
		AddProgressBar.setThemeName("Primary");
		Text fraction = new Text(wordCount+"/20");
		Button progressButton = new Button("UPDATE");
		progressButton.setThemeName("Primary");
		progressButton.addClickListener(click-> {
			AddProgressBar.setValue(obj.returnCount());
			fraction.setText(AddProgressBar.getValue()+"/20");
		});
		
		HorizontalLayout progressReport = new HorizontalLayout(AddProgressBar,fraction,progressButton);
		progressReport.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		
		Div PWrapperLayout = new Div(progressReport);
		PWrapperLayout.setWidth("100%");
		return PWrapperLayout;
		
	}

	private Component searchWord(WordsAVLTree obj) {
		// TODO Auto-generated method stub
		TextField searchWordField = new TextField("Search word");
		Button SearchButton = new Button("SEARCH WORD");
		
		SearchButton.setThemeName("primary");
		
		Binder<Word> SearchWordBinder = new Binder<>(Word.class);
		SearchWordBinder.forField(searchWordField).bind("wordx");
		
		SearchWordBinder.readBean(new Word());
		
		SearchButton.addClickListener(click -> {
			searchCount++;
			try {
				
				Word SearchedWord = new Word();
				SearchWordBinder.writeBean(SearchedWord);
				
				if(obj.searchWord(SearchedWord.wordx)!=null)
				{
				 

				 H6 SWord = new H6("WORD:"+SearchedWord.wordx+"  ");
				 H6 retms = new H6(obj.searchWord(SearchedWord.wordx));
				 Button close = new Button("Close");
				 VerticalLayout searchNotifLayout = new VerticalLayout();
				 searchNotifLayout.add(SWord,retms,close);
				 Notification SearchNotif = new Notification(searchNotifLayout);
				 SearchNotif.setPosition(Position.MIDDLE);
				 SearchNotif.open();
				 close.addClickListener(event -> SearchNotif.close());
				 
				}
				else {
					 H6 content = new H6("Word Not Found!");
					 Text google = new Text("Try Searching on Google instead?");
					 Icon googleIcon = new Icon(VaadinIcon.SEARCH);
					 Anchor anchor = new Anchor("www.google.com",googleIcon);
					 Button close = new Button("Close");
					 VerticalLayout searchNotifLayout = new VerticalLayout();
					 HorizontalLayout searchButtonLayout = new HorizontalLayout();
					 searchButtonLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
					 searchButtonLayout.add(new Text("Google"),anchor,close);
					 searchNotifLayout.add(content,google,searchButtonLayout);
					 Notification SearchNotif = new Notification(searchNotifLayout);
					 SearchNotif.setPosition(Position.MIDDLE);
					 
					 SearchNotif.open();
					 close.addClickListener(event -> SearchNotif.close());
					 
				}
				SearchWordBinder.readBean(new Word()); //clearing the binder once word is accepted
				//typeSelect.setValue(" ");
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				add(new Html(e.getValidationErrors().stream().map(err->
				"<p>"+ err.getErrorMessage() + "</p>").collect(Collectors.joining("\n"))));
//				e.printStackTrace();
			}
		});
		
		HorizontalLayout SearchWordLayout = new HorizontalLayout(searchWordField,SearchButton);
		SearchWordLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		Div SWrapperLayout = new Div(SearchWordLayout);
		SWrapperLayout.setWidth("100%");
		return SWrapperLayout;
	}

	private Component buildForm(WordsAVLTree obj) {
		// Creating UI for adding a word
		TextField newType = new TextField("type(noun/adj/adv/verb)");
		TextField newWord = new TextField("word");
		TextField newMeaning = new TextField("meaning");
		TextField newSynonym = new TextField("synonym");
		
		Button AddWord = new Button("ADD WORD");
		AddWord.setThemeName("primary");
		AddWord.setEnabled(false); //Disabling addword button till user inputs all fields
		
		
		Binder<Word> AddWordBinder = new Binder<>(Word.class);
		AddWordBinder.forField(newType).asRequired("Type is Required!").bind("type");
		AddWordBinder.forField(newWord).asRequired("Word is Required!").bind("wordx");
		AddWordBinder.forField(newMeaning).asRequired("Meaning is Required!").bind("meaning");
		AddWordBinder.forField(newSynonym).asRequired("Synonym is Required!").bind("synonym");
		
		
		
		AddWordBinder.addStatusChangeListener(status -> {
			AddWord.setEnabled(!status.hasValidationErrors());
		});
		
		AddWordBinder.readBean(new Word());
		
		AddWord.addClickListener(click -> {
			try {
				
				Word AddedWord = new Word();
				AddWordBinder.writeBean(AddedWord);
				boolean res =  obj.insertWord(AddedWord);
				if(res)
				{
				Notification.show(AddedWord.wordx + " Added!").setPosition(Position.MIDDLE);
				}
				else if(!res)
				{
					Notification.show("Word already exists!").setPosition(Position.MIDDLE);	
				}
				AddWordBinder.readBean(new Word()); //clearing the binder once word is accepted
				//typeSelect.setValue(" ");
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				add(new Html(e.getValidationErrors().stream().map(err->
				"<p>"+ err.getErrorMessage() + "</p>").collect(Collectors.joining("\n"))));
			}
		});
		
		
		//creating layout for the above fields
		HorizontalLayout AddWordLayout = new HorizontalLayout(newType,newWord,newMeaning,newSynonym,AddWord);
		AddWordLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		Div wrapperLayout = new Div(AddWordLayout);
		wrapperLayout.setWidth("100%");
		return wrapperLayout;
	}

}


