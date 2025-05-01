Capstone-1
This project is used as a primitive version of the user menu in an online banking app. It will write and read manually inputted payments and deposits into and from a "transactions.csv" file. Using that same file, the user will be able to search through transactions with the Ledger Screen and Reports Screen for better viewing needs. 

Getting Started 
The program will run most easily in a Java IDE, I used IntelliJ. You could also run from a command prompt window as it was designed as a CLI app. 

Home Screen
![Img Of Home Screen](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_34.png)


Ledger Screen
![Img of Ledger Screen](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_35.png)


Reports Screen
![Img of Reports Screen](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_36.png)

Reports Screen - Invalid Input (Same For All Screens)
![Img of Reports Screen with invalid input](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_37.png)



Reports Screen – Empty Input (Same for all Screens)
![Img of Reports Screen with empty input](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_38.png)


Interesting Code Section -----------------------------------------------------------------------------------
Result Helper Class – Allows me to check user input for returning to main menu or exiting the program.
![Img of Result Helper Class](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_39.png)



Uses allowUserToExitOrReturn();, returner();, and programQuitter(); to check each user input at any point in the program for an exit or return sequence call.
![Img of methods that use Result Helper Class](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_40.png)

Examples of this being used.
![Img of methods being used](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_41.png)

CurrentBalance();
Calculates the current balance from every added transaction. 
![Img of CurrentBalance Method](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_42.png)

activityLogger();
Tracks user activity at key points in the app, such as: launching the app, opening screens (home, ledger, reports), making payments & deposits, any function, and when the user searches a vendor.
![Img of ActivityLogger Method](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_44.png)

Examples of this being used.
![Img of key points that use ActivityLogger](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_47.png)
![Img of key points that use ActivityLogger](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_48.png) 
![Img of key points that use ActivityLogger](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_49.png) 
![Img of key points that use ActivityLogger](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_46.png) 
![Img of key points that use ActivityLogger](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_45.png)



Here is an example of how I used this.
![Img of example of CurrentBalance](https://github.com/Phoniix/capstone-1/blob/master/Screenshot_43.png)
