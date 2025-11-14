[![Build core](https://github.com/twaslowski/open-mood-tracker/actions/workflows/build.yml/badge.svg)](https://github.com/twaslowski/open-mood-tracker/actions/workflows/build.yml)
[![Image](https://img.shields.io/docker/v/twaslowski/mood-tracker?sort=semver)](https://hub.docker.com/r/twaslowski/mood-tracker)
![License](https://img.shields.io/github/license/twaslowski/open-mood-tracker)
[![Vercel Deployment](https://deploy-badge.vercel.app/vercel/mood-tracker)](https://vercel.com/tobias-waslowskis-projects/open-mood-tracker)

> [!WARNING]
> This repository is now archived. There is a follow-up project that you can check out if you're interested.
> Essentially, it turned out that Telegram was simply unsuited for more complex user interactions. Therefore, I implemented a frontend-first
> iteration of this project built on NextJS. It maintains the same features as well as a lightweight Telegram integration.
> Check it out [here](https://github.com/twaslowski/mood-tracker).

This is a Telegram-based Mood Tracking bot. 

It allows users to record their mood states and other health-related metrics. 
Mood Trackers are generally "useful for people with mental health conditions —
such as depression and anxiety — to help identify and regulate moods".

It can be difficult to get into the habit of tracking your mood.
This project aims at making the process of tracking your mood on a daily basis as frictionless
as possible, by integrating into a popular messaging app that you may be using on a daily basis
anyway.

## Usage

My instance of the bot, dubbed _moody_, is running [here](https://t.me/open_mood_tracker_bot).

You're absolutely free to use it if you like, but if you're more technically inclined,
you can host your own instance. Public Docker images and a Helm chart are freely available,
as well as some instructions for running [down below](#Running).

### Commands

The available commands are:

- `/start`: Start the bot and register yourself.
- `/record`: Record your current mood
- `/baseline`: Create a [baseline record](#baselines)
- `/autobaseline`: Toggle creation of automatic baseline records.
- `/configure`: Configure the bot.
- `/help`: Show the available commands.

### Baselines

_Baselines_ are an essential concept to the bot. They represent your personal feeling of "normal".

For example, this could mean eight hours of sleep and a neutral mood
(as opposed to depressed or manic). You can record an average day by sending `/baseline` to the bot. 
Further, for extended periods of normalcy, you can configure an automatic baseline records to be
created at a certain point throughout the day by triggering `/autobaseline`.

### Configuring the bot

There is a frontend to configure the bot to your needs. This is helpful for more complex
configuration, such as which metrics to track, which baselines to set, and to even create
custom metrics! You can access the configuration frontend by sending
`/configure` to the bot. That will generate a temporary session, in which you can configure
the bot to your liking.

## Running

In order to run the application, you will need to create a Telegram Bot with [BotFather](https://t.me/botfather).
[Task](https://taskfile.dev/) is used to perform common lifecycle tasks and will be used in this README,
but you can also run the commands manually if you prefer; simply refer to the `Taskfile.yml` for the commands.

### Running locally

You can run the Spring Boot application locally. This requires:

- Java 21 or higher
- Maven 3.9 or higher
- A Docker runtime and the compose plugin

```
TELEGRAM_TOKEN=<your-telegram-token> task run
```

### docker-compose

A docker-compose file is provided to run the bot, frontend and database together. Simply run:

```bash
docker compose -f scripts/docker-compose.yaml up -d
```

### Running in Kubernetes

For Kubernetes, a Helm chart is provided. You can install it with the following command:

```bash
```
