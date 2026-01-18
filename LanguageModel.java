import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String str = "";
        char charRe;
        In in = new In(fileName); 
        for (int i = 0; i < windowLength; i++) {
            if (in.isEmpty()) return;
            str += in.readChar();
        }

        while (!in.isEmpty()) {
            charRe = in.readChar();
            List probs = CharDataMap.get(str);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(str, probs);
            }

            probs.update(charRe);
            str = str.substring(1) + charRe;
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
    public void calculateProbabilities(List probs) {				
    int countAllChars = 0;
    ListIterator itr = probs.listIterator(0);
    while (itr.hasNext()) {
        CharData current = itr.next();
        countAllChars += current.count;
    }
    if (countAllChars == 0) return;

    double allCP = 0.0;
    itr = probs.listIterator(0);
    CharData last = null;
    while (itr.hasNext()) {
        CharData current = itr.next();
        last = current;
        current.p = (double) current.count / countAllChars;
        allCP += current.p;
        current.cp = allCP;
    }
    if (last != null) last.cp = 1.0; 
}

    // Returns a random character from the given probabilities list.
    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        ListIterator itr = probs.listIterator(0);
        CharData last = null;
        
        while (itr.hasNext()) {
            CharData current = itr.next();
            last = current;
            if (r < current.cp) {
                return current.chr;
            }
        }

        return last.chr;
    }
    
	//   Generates a random text, based on the probabilities that were learned during training.
	public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }
        String generatedText = initialText;
        String window = generatedText.substring(generatedText.length() - windowLength);

        while (generatedText.length() < textLength) {
            List probs = CharDataMap.get(window);

            if (probs == null) {
                break;
            }
            char nextChar = getRandomChar(probs);
            generatedText += nextChar;
            window = generatedText.substring(generatedText.length() - windowLength);
        }
        return generatedText;
    }

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);

        lm.train(fileName);
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}


