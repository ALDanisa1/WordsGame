import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WordMover extends Thread {
	private FallingWord myWord;
	private HungryWordMover hungryWordMover;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	CountDownLatch startLatch; //so all can start at once

	WordMover(FallingWord word, HungryWordMover w2) {
		myWord = word;
		hungryWordMover = w2;
	}
	WordMover( HungryWordMover word) {
		hungryWordMover = word;
	}

	WordMover(FallingWord word, HungryWordMover w2 , WordDictionary dict, Score score,
			  CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
		this(word,w2);
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
	WordMover(HungryWordMover word, WordDictionary dict, Score score,
			  CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
		this(word);
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
	
	
	
	public void run() {



		if(myWord != null ) {
			//System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
			try {
				System.out.println(myWord.getWord() + " waiting to start " );
				System.out.println(hungryWordMover.getHungryWord() + " waiting to start " );
				startLatch.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} //wait for other threads to start
			System.out.println(myWord.getWord() + " started");
			System.out.println(hungryWordMover.getHungryWord() + " started");
			while (!done.get()) {
				//animate the word
				while ((!hungryWordMover.pushed() && !myWord.dropped()) && !done.get()) {
					myWord.drop(10);
					hungryWordMover.push(10);
					try {
						sleep(myWord.getSpeed());
						sleep(hungryWordMover.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (myWord.getY() == hungryWordMover.getyPosition() && (myWord.getX() > hungryWordMover.getxPosition()-100 && myWord.getX()< hungryWordMover.getxPosition()+100) ) {
						score.missedWord();
						myWord.resetWord();
					}
					;
					while (pause.get() && !done.get()) {
					}
					;
				}
				if (!done.get() && myWord.dropped()) {
					score.missedWord();
					myWord.resetWord();
				}
				else if(myWord.dropped()) {
					myWord.resetWord();
				}


				if(!done.get() && hungryWordMover.pushed()){
					score.missedWord();
					hungryWordMover.resetWord();
				}
				else if (hungryWordMover.pushed()) {
					hungryWordMover.resetWord();
				}


			}
		}

	}
}
