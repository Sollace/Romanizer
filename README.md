# Romanizer

Provides a number converter for translating between regular integers and their equivalent extended roman numerals representation.

# How To Use

Converting to roman numerals:
```
String result = Romanizer.romanize(5); // --> "IV"
```

Converting from roman numerals:
```
int number = Romanizer.deromanize("IV"); // --> 5
```

# Adding to your project

You can download the jar and sources from the [releases tab](https://github.com/Sollace/Romanizer/releases)

Add it to a folder in your gradle project named "lib" and then specify it as a dependency like so:

```
repositories {
    flatDir { dirs 'lib' }
}
...
dependencies {
    compileOnly "com.sollace:Romanizer:Romanizer:1.0.2"
}
```
