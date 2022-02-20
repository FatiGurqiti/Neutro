# Neutro

**Neutro** is an Android application built with Kotlin that serves as person to person debt tracker.

## Technologies

- Kotlin
- XML
- Firebase
- Android Studio

![Image of Thumbnail](https://github.com/FatiGurqiti/Neutro/blob/master/images/Neutro.png)

## Documentation

The app uses [Firebase](https://firebase.google.com/), in order to be able to do the tasks simply, replace existing _google-services.json_.


### Usage the app

When opening the app, simply create your account or log in if you already have one. Account tasks are the same as [Lay](https://github.com/FatiGurqiti/Lay). You may check it out for more detail.

As mentioned in the title, Neutro helps you to keep track of debt. To do that you have to have contacts. Click on _Add Contact_ and enter e-mail adress of a _Neutro_ user. The app will not allow you enter any other e-mail than one that is registered to _Neutro_. 
You may view or delete your contacts on _Profile_ activity, which you can access by clicking _profile_ Image Button on the top right corner of Main Activity.

<br>

![Image of Debt](https://github.com/FatiGurqiti/Neutro/blob/master/images/Add%20Debt.png)  

<br>

It is mandatory to add debt only to user's existing contacts so that the user cannot add debt to non-existing object. To do so, click on _Add Debt_ button on Main Activity and select a _Contact_. Enter _Amount_ and _Label_ and finish the task by clicking "ADD" button.
Once the task is completed, the app will refresh the page to be able to show the new debt
<br>

These datas are beign kept on Firestore as *ArrayLists*
![Image of Firebase](https://github.com/FatiGurqiti/Neutro/blob/master/images/Firebase.png)  

### Explanation of hard tasks

![Image of Explanation](https://github.com/FatiGurqiti/Neutro/blob/master/images/Explenation.png)

<br>

To add new data to the cloud, program creates an Arraylist for the data and checks if the user has previous data by runing a query. If the user has previous data; get them all to new Array Lists and add current data on top of the Array List and update the Array List on Firebase with new Array List. 

```
                    IDArray = document.get("id") as ArrayList<String?> //Get array from FireBase

                    IDArray.add(id)  //Add current data to it

                    amountArray.add(amount)

                    //Update the data
                    db.collection(collection)
                        .document(document)
                        .update("id",IDArray)
```

<br>

If the user doesn't have previous data simpy, add current data to an Array List and create a collection. 
```
                    IDArray.add(id) //Add data to ArrayList
                    //And do the same for all data

                    var debthash = hashMapOf( //Create hashmap
                        "id" to IDArray,
                        "to" to toArray,
                        "name" to nameArray,
                        "amount" to amountArray,
                        "label" to labelArray,
                        "time" to timeArray
                    )

                    //Create document
                    db.collection(collection)
                        .document(document)
                        .set(debthash)
              
```

To have a better understanding, you can check codes on **DebtController** under util folder






