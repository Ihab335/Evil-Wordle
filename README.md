## Evil Wordle

Welcome to the **Evil Wordle**, a program designed to make your Wordle experience as challenging as possible by choosing the hardest words for you to guess. This tool takes a list of potential secret words and your initial guess, and uses the feedback from your guess to calculate the difficulty of remaining words.

### Features

- **Difficulty Scoring:** The program evaluates the difficulty of each word based on feedback from the player's guess, taking into account correct letters (green) and letters present but misplaced (orange).
- **Evil Word Selection:** The primary goal is to make Wordle as difficult as possible by selecting the most challenging words for the player to guess. It does so by identifying words with the lowest scores, using the user's first guess and its letters as a reference.
- **Letter Frequency Analysis:** To further increase the challenge, the program analyzes the frequency of each letter across the secret words. Less frequent letters are considered more obscure and prioritized in the word selection.

### Usage

To use the **EvilW Wordle**, follow these steps:

1. Provide a list of secret words (a sample list is included in the repository).
2. Input the user's first guess as a reference for word selection.
3. Run the `ranked_evil` function with the secret word list and the user's first guess:

```python
ranked_evil(secrets, first_guess)
