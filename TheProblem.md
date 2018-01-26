# A Weather Casting Demo

The Foobarnian Ministry of Meterology wants to develop a JSON service to
disseminate weather alerts for all of Foobarnia from their supercomputers. But,
the Parliment denied their funding request because they have never seen a web
application before and had no idea how it could be useful.

So, the Foobarnians have asked you to build a sample web app using an existing
service, to prove to the Parliment how useful a web app can be.

The app should consist of two simple views.

  * Main Screen / Index:
    1. A table showing a list of 10 predefined cities of your choosing and the current temperature in each.
    2. The table can be sorted on the current temperature column by clicking the header of the column.
    3. Clicking on one of the rows in the table will bring up the detail view for that city.
  * Detail view:
    1. Displays the 5 day forecast for the city.
    2. Each day shows weather description and the current temperature.

### Guidelines

The app can be entirely in-browser, using any JS framework you prefer. App data
does not need to be persisted.

There is no particular design specified, but any effort to make the app look
amazingly good would be appreciated.

### Hints

You can use the OpenWeatherMap API to get the current weather for any city:
Current Weather: http://openweathermap.org/current
Five Day Forecast: http://openweathermap.org/forecast5

Here is a API key: `f333c36f17612d7b693745b00991425a`
