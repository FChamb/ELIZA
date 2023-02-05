CS1006 Practical 1 -- Eliza

Description:
The Eliza program is a series of classes which work together to read a specified script and then
generate responses to a user asking questions. This program package contain several classes which
act as object variables for the main Engine to work with. The Engine and it's Run counterpart
contain the core functionality of Eliza. The program package also contains three provided scripts
for a user to interact with.

Usage:
To begin using Eliza, the user should enter the terminal and enter the Eliza directory. When in
the directory, begin by running > javac *.java < to compile the various java classes. Then decide
which script to communicate with. There are three, a psychotherapist, eight-year-old, and a drunk.
When the script has been chosen run the command > java RunEngine {name_of_chosen_script}.txt <
This will begin Eliza and enable communication. Eliza will begin by welcoming the user. From there
the user can communicate however they like. When a user is done, they can enter an appropriate
quit command to exit. Those consist of many varieties, but a few examples are: exit, quit, bye, etc.

Steps outlined below:
1. Open terminal
2. Navigate into Eliza directory
3. Run > javac *.java <
4. Choose script
5. Run > java RunEngine {name_of_chosen_script}.txt <
6. Communicate freely with Eliza