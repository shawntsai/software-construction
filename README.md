# software-construction

These are the problem sets from 
## expressivo

> x * x * x
x*x*x

> !simplify x=2
8

> !d/dx
(x*x)*1+(x*1+1*x)*x

> !simplify x=0.5000
.75

> x * y
x*y

> !d/dy
0*y+x*1

## minesweepter
Multiplayer Minesweeper server
We will refer to the board as a grid of squares. Each square is either flagged, dug, or untouched. Each square also either contains a bomb, or does not contain a bomb.

Our variant works very similarly to standard Minesweeper, but with multiple players simultaneously playing on a single board. In both versions, players lose when they try to dig an untouched square that happens to contain a bomb. Whereas a standard Minesweeper game would end at this point, in our version, the game keeps going for the other players. In our version, when one player blows up a bomb, they still lose, and the game ends for them (the server ends their connection), but the other players may continue playing. The square where the bomb was blown up is now a dug square with no bomb. The player who lost may reconnect to the same game again via telnet to start playing again.

Note that there are some tricky cases of user-level concurrency. For example, say user A has just modified the game state (i.e. by digging in one or more squares) such that square i,j obviously has a bomb. Meanwhile, user B has not observed the board state since this update has taken place, so user B goes ahead and digs in square i,j. Your program should allow the user to dig in that square — a user of Multiplayer Minesweeper must accept this kind of risk.

We are not specifically defining, or asking you to implement, any kind of “win condition” for the game.


## Tweet
The theme of this problem set is to build a toolbox of methods that can extract information from a set of tweets downloaded from Twitter.

Extracting data from tweets
Filtering lists of tweets
implement one additional kind of evidence in guessFollowsGraph().


## Library
implement a book library where people can check out, buy, borrow books

BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.

