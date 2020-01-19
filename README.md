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
1. void initDictionary()
2. int score(String word)
3. boolean isValid(String word)
4. List<String> generateWords(String letters)
   List<String> generateWords(String letters, sortMode sort)
5. List<Move> generateMoves(Game game)
   List<Move> generateMoves(Game game, sortMode sort)
   List<Move> generateMoves(Game game, String rack)
   List<Move> generateMoves(Game game, String rack, sortMode sort)
```

### Game(String lang, int players) | Game(Game game)
> Default language: "EN", player count: 2

---
```java
1. void displayBoard()
2. void displayPlayersInfo()
3. char[][] getBoard()
4. String getPlayersRack()
5. boolean isEmpty()
6. boolean isCrossing(Move move)
7. boolean isValid(Move move)
   boolean isValid(Move move, String rack)
8. boolean tryMakeMove(Move move)
   boolean tryMakeMove(Move move, String rack, String add)
9. void makeMove(Move move)
   void makeMove(Move move, String rack, String add)
10. void passTurn()
```

## ToDo
- score calculation for given move
- blank support