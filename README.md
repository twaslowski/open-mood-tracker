# mood-tracker

This is a mood tracker application written with Spring Boot.
For now, you can interact with it as a Telegram Bot, although web interfaces or even an App
could be implemented in the future.

## Usage

You can find my personal instance of the bot running at:

https://t.me/open_mood_tracker_bot

The available commands are:

- `/start`: Start the bot and register yourself.
- `/record`: Record your current mood
- `/baseline`: Create a [baseline record](#baselines)
- `/auto-baseline`: Toggle creation of automatic baseline records.
- `/settings`: Configure the bot [in progress].

### Baselines

You can define baselines, which corresponds to your personal feeling of "normal". For me personally,
this is eight hours of sleep and a neutral mood (as opposed to depressed or manic). You can record
an average day by sending `/baseline` to the bot. Further, for extended periods of normalcy, you can
configure a automatic baseline records to be created at a certain point throughout the day by sending
`/auto-baseline`.

## Roadmap

**Features**

- [ ] Allow users to fully configure the bot to their needs
- [ ] Statistics and visualization

**Tech Debt**

- [ ] Ensure scheduled tasks can be canceled

## Configuring the bot

As of now, the bot is primarily configurable via a Configmap that is injected into the application.
You can find the Configmap with the configuration for the demo bot [here](https://github.com/twaslowski/open-mood-tracker/blob/main/charts/values/application-values.yaml).

I'm working on making Notifications and tracked Metrics as well as Baselines configurable right now.

## Running

All you need to run the bot is a Telegram Bot Token, as well as a Kubernetes cluster (you could use
microk8s or k3s) as well as Helm installed. You can then run the following commands:

```
git clone git@github.com:twaslowski/open-mood-tracker.git
cd open-mood-tracker
./lifecycle/deploy.sh
```

For setting an initial postgres password (recommended), export a DATASOURCE_PASSWORD variable.
