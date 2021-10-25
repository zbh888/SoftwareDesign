Pixel 3 API 30

Android 11.0 (R) 

(Google API, x86)

openjdk version "15.0.2" 2021-01-19

OpenJDK Runtime Environment (build 15.0.2+7-27)

OpenJDK 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)

Macbook Air 2017 MacOS 11.2.1

Gradle dependency is 4.1.3. I don't know if your intellij fits this. You can run it on Android studio.

But if you have to run it on intellij, you could change it to like 4.0.0.

# Usage

## Home page:

Draw in the area below the button `Analyze`

And then click Analyze for response (Three image above would show something).

Certainly you will get nothing in response if you draw nothing, or library has nothing.

## Library page:

Click button to add gesture.

Once you add a gesture, you can delete it or modify it.

## Add gesture / Modify

Draw something below the TextArea for name.

And enter a name.

Above two are nessesary, you can't save without them.

Then Save or Cancel, it jumps back to library.

Note:

1. The library can't have the same name. You could try to save one with duplicate name, and it won't allow (Though name doesn't matter in this program, but duplicated name for gestures is not ideal).

But you can rename gesture as the same as before when you are modifying.

When modifying, save is just named as 'Modify', and it will be 'create' when you add a new gesture.

2. After you draw some gesture and not really satisfied with that, you could immediately start to draw another one by clicking any place on canvas.

Also, User should aware that we start keeping a gesture whenever you start to touch the screen. The system would assume you touch the canvas means that you want to draw a gesture. But that is a gesture at least, so you can't make a point a gesture. That means, if you finish drawing something, and you click the canvas. orginal drawing will be clear as a functionality. And there will not be analysis for empty drawing, of course.

3. The program support saving even if you kill the process. It saves the data to the storage since the data sturcture is seriazable.



