package deques.palindrome;

import deques.Deque;
import org.junit.Test;

import static org.junit.Assert.*;

public class PalindromeTest {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertFalse(palindrome.isPalindrome("persiflage"));
        assertFalse(palindrome.isPalindrome("me"));

        assertTrue(palindrome.isPalindrome("lol"));
        assertTrue(palindrome.isPalindrome("redivider"));

        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));

        assertTrue(palindrome.isPalindrome("!#!"));
        assertFalse(palindrome.isPalindrome("!#8098!"));

        CharacterComparator offByOne = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertTrue(palindrome.isPalindrome("", offByOne));
        assertTrue(palindrome.isPalindrome("a", offByOne));

        assertFalse(palindrome.isPalindrome("goof", offByOne));
        assertFalse(palindrome.isPalindrome("redivider", offByOne));
        assertFalse(palindrome.isPalindrome("!#!", offByOne));
    }
}
