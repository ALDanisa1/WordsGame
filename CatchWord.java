import java.util.concurrent.atomic.AtomicBoolean;

//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	int same = 0;
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE

	private static  FallingWord[] words; //list of words
	private static  HungryWordMover[] hungryWordMover; //list of words
	private static FallingWord[] new_list ;
	private static int noWords; //how many
	private static Score score; //user score

	
	CatchWord(String typedWord) {
		target=typedWord;
	}

	public static void setWords(FallingWord[] wordList, HungryWordMover[] w) {
		new_list = new FallingWord[wordList.length+w.length];
		words = wordList;
		hungryWordMover = w;
		noWords = words.length;
	}
	
	public static void setScore(Score sharedScore) {
		score=sharedScore;
	}
	
	public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}
	public FallingWord[] merge(FallingWord[] words, HungryWordMover[] word, FallingWord[] new_list){
		String Hword = word[0].getHungryWord();
		FallingWord fallingWord = new FallingWord(Hword);

		for (int i = 0; i < new_list.length; i++) {
			if(i != words.length ) {
				new_list[i] = words[i];
			}
			else if(i == new_list.length-1){
				new_list[i] = fallingWord;
			}
		}
		return new_list;
	}
	
	public void run() {
		new_list = merge(words, hungryWordMover,new_list);
		System.out.println("newlisy " + new_list[new_list.length-1].getWord());
		System.out.println("hungry " + hungryWordMover[0].getHungryWord());
		int i= 0;
		int height = 0;
		FallingWord targetWord = null;



		while (i < noWords+1) {
			while(pause.get()) {};
			if (new_list[i].matchWord(target) ) {
				System.out.println("inside");
				if(new_list[i].getY() > height){
					height = new_list[i].getY();
					targetWord = new_list[i];
				}
			} else if (hungryWordMover[0].matchWord(target) && i == words.length-1 && targetWord == null) {
				hungryWordMover[0].resetWord();
				System.out.println( " score! '" + target); //for checking
				score.caughtWord(target.length());
			}

			i++;
		}
		if(targetWord != null) {
			if(targetWord.getWord().equals(hungryWordMover[0].getHungryWord()) && targetWord.getY() < hungryWordMover[0].getyPosition()) {
				hungryWordMover[0].resetWord();
				System.out.println( " score! " + target); //for checking
				score.caughtWord(target.length());
			}
			else{
				targetWord.resetWord();
				System.out.println( " score! " + target); //for checking
				score.caughtWord(target.length());
			}

		}

	}

}
