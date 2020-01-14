# ScrabbLib
**ScrabbLib** - the ultimate library written in Java programming language for Scrabble
(the word game in which players score points by placing tiles, each bearing a single letter,
onto a game board divided into a 15x15 grid of squares. The tiles must form words that, in
crossword fashion, read left to right in rows or downward in columns, and be included in a
standard dictionary or lexicon).

## Features
- **lets you play the real game using simple methods**
**(graphical interface recommended)**
- **based on original rules of traditional Scrabble game**
**(notation system, scoring, acceptable words)**
- **custom AI returning best possible words/moves in a given scenario**
- **supports english and polish language (dictionaries included)**

## Methods
### ScrabbLib(String language)
> Default language: "EN"

---
```java
1. void initDictionary() //initializies the dictionary for specific language

2. int score(String word) //returns score (sum of values) for given word

3. boolean isValid(String word) //returns true if word exists in dictionary, otherwise false

4. List<String> generateWords(String letters)
   List<String> generateWords(String letters, sortMode sort) //Generates and returns valid
   //words for given letters sorted by score or length (default sorting mode: score)

5. List<Move> generateMoves(Game game)
   List<Move> generateMoves(Game game, sortMode sort)
   List<Move> generateMoves(Game game, String rack)
   List<Move> generateMoves(Game game, String rack, sortMode sort) //generates and
   //returns best possible moves for given game state sorted by score or word length
   //(default sorting mode: score, letters: rack of player whose turn is currently
```

### Game(String lang, int players) | Game(Game game)
> Default language: "EN", player count: 2

---
```java
1. void displayBoard() //displays a Scrabble board for current game state on console

2. void displayPlayersInfo() //displays statistics of players (score and rack)

3. char[][] getBoard() //returns a Scrabble board for current game state

4. String getPlayersRack() //returns rack of player whose turn is currently

5. boolean isEmpty() //returns boolean if the Scrabble board is currently empty (no tiles placed)

6. boolean isCrossing(Move move) //returns boolean if given move meets the placed tiles on board

7. boolean isValid(Move move)
   boolean isValid(Move move, String rack) //returns boolean if given scenario is valid to be
   //played for current game state

8. boolean tryMakeMove(Move move)
   boolean tryMakeMove(Move move, String rack, String add) //checks if given scenario is valid
   //and makes the move if it's true

9. void makeMove(Move move)
   void makeMove(Move move, String rack, String add) //makes the move without validity check

10. void passTurn() //changes the turn for next player
```

## ToDo
- score calculation for given move
- blank support