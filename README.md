### Status

[![Build Status](https://travis-ci.org/beatyt/TheMemencholyOfRiles.svg?branch=master)](https://travis-ci.org/beatyt/TheMemencholyOfRiles)

## Backstory

Riles once quizzed Tom on pop figure trivia.  Tom knew nothing.

So, Riles began posting news headlines into all chat during Dota 2 games.

Then, he started replacing celebrity's names with Tom's.

Seeing the opportunity for automation, Tom constructed an application for fetching and parsing headlines.

## Quickstart:

Edit the config.properties file to add your own url and data files.  Some data files are found in the target/classes/data directory, the ones read at startup are in src/main/java/resources/data.


## How to deploy:

1. Create a new project
2. `Git clone` this into that new project directory
3. Do the stuff for the pom.xml `mvn install` to get the dependencies.
4. /\\/\\e/\\/\\e

## Design patterns:

1. Singleton -- The PropertyHandler utilizes the Singleton pattern so that one instance of the configuration file will exist.  MySharedQueue uses the pattern as well.
2. Producer-Consumer -- Multithreaded queue for the scraping and parsing.

##Thanks beatyt
This project changed my life!