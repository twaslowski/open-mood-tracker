# mood-tracker

This is a mood tracker application written with Spring Boot.
For now, you can interact with it as a Telegram Bot, although web interfaces or even an App
could be implemented in the future.

## Usage

You can find my personal instance of the bot running at:

https://t.me/open_mood_tracker_bot

This section is a rough user guide to show you how to use the bot.

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
custom metrics! You can access the (admittedly very rough) configuration frontend by sending
`/configure` to the bot. That will generate a temporary session, in which you can configure
the bot to your liking.

## Running

All you need to run the bot is a Telegram Bot Token, as well as a Kubernetes cluster (you could use
microk8s or k3s) as well as Helm installed. You can then run the following commands:

```
git clone git@github.com:twaslowski/open-mood-tracker.git
cd open-mood-tracker
./lifecycle/deploy.sh
```

For setting an initial postgres password (recommended), export a DATASOURCE_PASSWORD variable.
