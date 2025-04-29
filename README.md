Capstone-1
This project is used as a primitive version of the user menu in an online banking app. It will write and read manually inputted payments and deposits into and from a "transactions.csv" file. Using that same file, the user will be able to search through transactions with the Ledger Screen and Reports Screen for better viewing needs. 

Getting Started 
The program will run most easily in a Java IDE, I used IntelliJ. You could also run from a command prompt window as it was designed as a CLI app. 

Home Screen



Ledger Screen







Reports Screen


Reports Screen - Invalid Input (Same For All Screens)




Reports Screen – Empty Input (Same for all Screens)



Interesting Code Section
Result Helper Class – Allows me to check user input for returning to main menu or exiting the program.



Uses allowUserToExitOrReturn(), returner(), programQuitter() to check each user input at any point in the program for an exit or return sequence call.


Examples of this being used.


CurrentBalance();
Calculates the current balance from every added transaction. 



Here is an example of how I used this.
