# NextCompanion - Hacker's Nextbike Client
A **minimalistic** and **unofficial** client for Nextbike. Still under early development, use with care.

The app allows you to sing-in, see a list of bikes, rent bikes and return them.

The app is neither official, nor feature-rich. It also doesn't allow you to.
sign-up to nextbike or to delete your account, use GPS or read the news
feed. It was just tested for Norderstedt, so it's not guaranteed to work in
all areas. The app has no support for flexzones.

**I'm not a professional developer; especially not for Android. Until someone cool reviewed this source code, please consider this app bad by technical design.**

## Installation
* Build it
* [F-Droid](https://f-droid.org/packages/com.example.hochi.nextcompanion/)
* manual APK download (upcoming)

## Usage notes
* Sign up will never be supported so you have to sign up via the website or the official app
* Flexzones are not supported at the moment

## Privacy
This app does not track you or send any private data - including your geopositon and app usage metrics- to NextBike servers or third party servers. It does only send a couple of requests to the NextBike servers, just enough to rent you a bike. The original app does not offer an opt out for sending your position to its servers, because it depends on it during the return process for some reason. It even seems to track the exact rout I took in my case.

## Tested areas
NextCompanion should work in other cities as well.

* "Nextbike Norderstedt" Norderstedt, Germany
* "Nextbike Kassel" Kassel, Germany
* "Nextbike Poznań" Poznań, Poland

## Features
* [x] Sign in
* [x] List of rented bikes
* [x] Rent bikes
* [x] Return bikes
* [x] Sign out
* [ ] Good error handling
* [ ] Settings
* [ ] Flexzone support

## Possible features
* integration of other bike sharing systems

## This is not...
* feature rich
* the official client
* sign up on Nextbike
* read the news feed
* a map
* delete your account
* using GPS
* fancy swiping

## Motivation
tl;dr: The original app did not work for me. Too many crashes, completly overloaded with features, returning did not work and privacy issues.

The original client sent me random testing notifications like "IOS test" or "LOL" but idk, why does a bike sharing app needs notifications anyhow? It also features loading their news feed in the main activity and since a recent upgrade it became unusable for me because returning depends on personal GPS location.

## Disclaimer
This app is not affiliated to NextBike GmbH in any way. The above statements about the original app may be biased.
