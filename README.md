# Tic-Tac-Toe-Variant

This program plays the following variant of Tic-Tac Toe.

Two players place pieces on a 3x3 board. Each of them place 3 pieces each, and after that each of them moves one of their pieces on each step. If at any point one of the players get three in a row, they win and the game ends.

For example, if "0" denotes an empty space, "1" denotes Player 1's pieces, and "2" denotes Player 2's pieces, the following is a sample game:

0 0 0
0 0 0
0 0 0

1 0 0
0 0 0
0 0 0


1 0 0
0 2 0
0 0 0


1 0 0
0 2 0
0 1 0

1 0 0
0 2 2
0 1 0

1 0 0
1 2 2
0 1 0

1 0 0
1 2 2
2 1 0

1 1 0
0 2 2
2 1 0

1 1 2
0 2 0
2 1 0

The program provides options for user input (methods starting with userPlayer...), a bot playing (methods starting with myPlayer...), and a random player playing (methods starting with randomPlayer...).
