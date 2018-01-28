# weather

Generated using Luminus version "2.9.12.22"

Weather is a simple SPA written with ClojureScript to show the weather for a few cities.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed to run in dev.

[1]: https://github.com/technomancy/leiningen

## Running final version

To run the compiled version of the application (you still need lein), run:

    lein uberjar

to compile to a .jar file. Then execute, from the root directory:

    java -jar target/uberjar/weather.jar

And finally navigate to:

    localhost:3000

## Running in Dev

To start a web server for the application, in the root directory, run:

    lein run

Open a new terminal to the same root directory and run the following to compile the js:

    lein figwheel

Then go to `localhost:3000`

## Testing

I wanted to finish this before the end of the week so, out of a concern for everyone's time, I did not add any tests.

## License

Copyright © 2018 mczabaj
