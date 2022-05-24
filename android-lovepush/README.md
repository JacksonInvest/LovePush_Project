
# LovePush

A brief description of what this project does and who it's for


[![test](https://github.com/blocoio/android-template/workflows/test/badge.svg?branch=master)](https://github.com/blocoio/android-template/actions?query=workflow%3Atest+branch%3Amaster)
[![lint](https://github.com/blocoio/android-template/workflows/lint/badge.svg?branch=master)](https://github.com/blocoio/android-template/actions?query=workflow%3Alint+branch%3Amaster)

## MVP - Model View Presenter architecture with 3 layers
- Model: The model will continue to contain the data in simple classes, so nothing really changes here.
- View: The view will continue to be implemented using Activity or Fragment classes, but we will change the scope of what the view controls.
- Presenter: The last part is the presenter, which handles UI updates based on changes to the data model, and also processes users inputs. The presenter will contain much of the business code and replaces the controller from MVC.

![ArchiTecture](https://i.stack.imgur.com/yDSba.png)


    
## Other useful features
- Dependency injection (with [Dagger](https://github.com/google/dagger))
- Google [Material Design](https://material.io/blog/android-material-theme-color) library
- Http Client [Retrofit](https://square.github.io/retrofit/) library
- View Binding [Butter Knife](https://github.com/JakeWharton/butterknife) library

- Edge To Edge Configuration
- Resource defaults
    - styles.xml - app themes
    - colors.xml - colors for the entire project
    - styles.xml - widget styles 
    - styles.xml - text appearances

# Getting started

### Manual
1. Download this repository extract and open the folder
2. Import Project in android studio and sync with gradle 
3. On `app/build.gradle`, update the dependencies Android Studio suggests
4. On `styles.xml` & `colors.xml` set your application primary and secondary colors 


And you're ready to start working on love push app.

### Using Import From Version Control In Android Studio 
1. Copy the repository url.
2. Open android studio goto file -> new -> project from version control.
3. Click open on this window and wait for the gradle sync.
4. On `app/build.gradle`, update the dependencies Android Studio suggests
5. On `styles.xml` & `colors.xml` set your application primary and secondary colors 

And you're ready to start working on love push app.

# Notes
- Jetifier is enabled inside `gradle.properties`. Even though it's not necessary for the 
  current dependencies, it was left enabled so anyone can start using the template without 
  worrying when to turn it on. We do recommend you to check [Can I drop Jetifier?](https://github.com/plnice/can-i-drop-jetifier) 
  when expanding the template.
- Android Template contains `.github/workflows` for lint check and unit testing. You can easily take this project worflow and repurpose it with a few path changes, you can also find a commented example in test.yml for Instrumentation Testing and CodeCoverage that we advice to keep a clean project, you will however need to replace the secret keys with your own.


