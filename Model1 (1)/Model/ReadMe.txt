ReadMe

Cashier:
username:kister   password:kister123
username:aleks    password:aleks123

Manager:
username:aster     password:aster1234
username:bister    password:bister1234

Administrator:
username:dister    password:dister12345
username:bister    password:bister12345


(Manager supports multi-sector functions or its available due to the fact that in the database in the manager table ,manager id is unique so it is possible to add another manager with the same credentials but different sector and it will be counted as separate but the name will still connect them making them accessible by querries;and thats not limited to only manager as the same logic can be applied to cashier or administrator)
(In the project only the integer parsing or double parsing is fully handeled since for strings the database operations will just fail and display an alert while numbers parsed from strings in the case of parsing characters into numbers they disrupt the flow and the the run doesn't reach the database operations)
(Alerts in the form of informatiove or error alerts are meant as a display of visualization if soething has gone wrong or not and they have been recycled or reused in the project so they dont neccesarily mean to display the correct text according to the situation but their prime purpose is to be informative or error types)
(in the manager terminal when viewing notifications the textfield that appears before entering the new scene is meant for the sector as one manager can be part of many sectors and thus he can choose to see the notifications for the desired sector if its part of them)
(for the hyperlink conversion in the control/menu2 replace the absolute path in the varaiable DIRECTORY eith the new path of the folder up to Model)
