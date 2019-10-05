package deques.palindrome;

import deques.Deque;
import deques.LinkedDeque;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> letters = new LinkedDeque<>();
        for (int i = 0; i < word.length(); i++) {
            letters.addLast(word.charAt(i));
        }
        return letters;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> toCheck = wordToDeque(word);
        while (!(toCheck.isEmpty() || toCheck.size() == 1)) {
            if (toCheck.removeFirst() != toCheck.removeLast()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> toCheck = wordToDeque(word);
        while (!(toCheck.isEmpty() || toCheck.size() == 1)) {
            if (!cc.equalChars(toCheck.removeFirst(), toCheck.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
