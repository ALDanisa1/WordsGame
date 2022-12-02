//Anele Danisa
//DNSANE001
//22/08/2022
//Game Hungry word

public class HungryWordMover {
	private  int maxX;
	private String hungryWord;
	private int xPosition;
	private int yPosition;
	private boolean pushed;
	
	private int wordSpeed; //how fast this word is
	private static int maxWait=1000;
	private static int minWait=100;

	public static WordDictionary dict;
	
	HungryWordMover() { //constructor with defaults
		hungryWord ="computer";
		xPosition =0;
		yPosition =0;
		pushed =false;
		wordSpeed=(int)(Math.random() * (maxWait-minWait)+minWait);
	}

	HungryWordMover(String text) {
		this();
		this.hungryWord =text;
	}

	HungryWordMover(String text, int maxX, int y) {
		this(text);
		this.maxX=maxX;
		this.yPosition =y;
	}

	//getters and setters

	public synchronized  int getSpeed() {
		return wordSpeed;
	}

	public synchronized  int getxPosition() {
		return xPosition;
	}

	public synchronized  void setxPosition(int xPosition) {
		if (xPosition >maxX) { //check if the word has moved
			xPosition =maxX;//update the position of the hungry word
			pushed =true; //user did not manage to catch this word
		}
		this.xPosition = xPosition;
	}
	public synchronized  int getyPosition() {
		return yPosition;
	}

	public synchronized  String getHungryWord() {
		return hungryWord;
	}

	public synchronized void resetPos() {
		setxPosition(0);
	}

	public synchronized void resetWord() {
		resetPos();//set the xpostion to 0
		hungryWord =dict.getNewWord(); //get the word from the dictionary
		pushed =false; //the word is no longer at the end of a screen
		wordSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); //choose any speed in random
	}
	
	public synchronized boolean matchWord(String typedText) {
		if (typedText.equals(this.hungryWord)) { //checking if the typed word correspond with the hungry word
			return true;
		}
		else
			return false;
	}

	public synchronized  void push(int inc) {
		setxPosition(xPosition +inc);
	}
	
	public synchronized  boolean pushed() {
		return pushed;
	}

	//reset the speed of the word and giving it the minimum and maximum waiting time
	public static void resetSpeed( ) {

		maxWait=1000;
		minWait=100;
	}
}
