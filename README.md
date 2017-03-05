# Android-layout-to-pdf
Simple Android project that adds text views to a layout and applies it to a pdf.

## What is this project, anyway?
This is a simple example of dynamically creating a layout (one which does not ever exist on screen), then using that layout
to create an image that will be painted onto a page in a PDF.

This targets Android 5. It has not been tested below Android 5, but know that PDF rendering support was added in SDK Version 19. So these examples are guaranteed not work in any SDK version lower than 19.

This project is purposely made simple, I tried to strip out anything that would make this complicated. This code came from a project where I was generating a receipt on the fly and needed a good way to build my receipt, save it to a pdf, then send it to a printer. 

## A quick map of the source code and runtime.
The MainActivity exists only to execute an onClick event on the button. When the button is clicked, this calls the ExamplePdfMaker.java class file and builds a pdf. The pdf that will be generated is found in the app's own storage directory. 
You could change this. 

To find the PDF, open up the "Android Monitor" in Android Studio and filter by `ez_`. There should be a message that tells you the storage location for the file. I have observed that sometimes, this storage directory will be slightly different when accessing via adb. For example, you might need to go to /storage/emulated/legacy/0... when the app thinks it wrote to /storage/emulated/0... You can still reference within the app wherever it thinks it wrote it, even if logging in through ADB, the directory looks different.

In any case, if you are on an emulator, just browse to the app directory and view the example.pdf generated. If using adb, issue an adb shell command to download the pdf locally and view the file if you should so choose.

## Implementing in your own project.
* Build for SDK 19 and above
* Take the ExamplePdfMaker class and customize it for your own needs.
* Add the AndroidManifest.xml permissions for WRITE_EXTERNAL_STORAGE
