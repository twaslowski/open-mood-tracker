# mood-tracker

![Logo](./frontend/public/images/moody_logo.png)

This is a Telegram-based Mood Tracker bot. 
It allows users to record their mood states and other health-related metrics. 
Mood Trackers are generally "useful for people with mental health conditions —
such as depression and anxiety — to help identify and regulate moods".

It can be difficult to get into the habit of tracking your mood.
This project aims at making the process of tracking your mood on a daily basis as frictionless
as possible, by integrating into a popular messaging app that you may be using on a daily basis
anyway.

## Cool, so why should I use it?

While this is called a _mood tracker_, mood is far from the only thing you can track.
As a matter of fact, mood may not even be the most important thing to track for _you personally_.
Therefore, this application allows you to track **arbitrary mental-health related metrics**,
making it endlessly customizable to your specific needs.

Maybe you're more interested in your stress levels over time. Maybe you're interested in how
your sense of appetite correlates with your anxiety. All of that is doable. Simply select the
metrics you would like to track, `/record` them and `/graph` them as you like.

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

You can run this bot in several ways. The easiest way is simply using Docker; however, for
production-grade deployments, you may want to use Kubernetes.

In any case, the following things are required to run the bot:

- A `TELEGRAM_TOKEN` acquired from [@BotFather](https://t.me/botfather).
- A `DATASOURCE_URL` and `DATASOURCE_PASSWORD` for Postgres
- A `JWT_SECRET` for signing JWT tokens. This can be any random string, but should be kept secret.
You can generate one with e.g. `openssl rand -hex 32`.

### Running locally

For strictly local development, the above values are provided in the `application-local.yml`,
except the `TELEGRAM_TOKEN`.
Therefore, you can simply run the application via the following script:

```
export TELEGRAM_TOKEN=<your-telegram-token>

lifecycle/start-environment.sh  # start postgres
lifecycle/run.sh  # start application
```

### Running in Docker

> [!WARNING]
> This section is not correct right now.
> Frankly, this is not a workflow that I use; it is likely possible, but I would
> recommend simply launching a `minikube` instance and deploying it there via the provided
> Helm charts.

Here, you will have to actually supply environment variables yourself.
For instance, launch Postgres with the following command:

```docker
docker network create mood-tracker
```

```bash
docker run -d \
  --network mood-tracker \
  --name mood-tracker-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=mood-tracker \
  -p 5432:5432 \
  postgres:latest
```

Then, you can run the bot with the following command:

```bash
docker run -d --rm \
  --network mood-tracker \
  --name mood-tracker \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e TELEGRAM_TOKEN=<your-telegram-token> \
  -e DATASOURCE_URL=jdbc:postgresql://mood-tracker-postgres:5432/mood-tracker \
  -e DATASOURCE_PASSWORD=postgres \
  -e JWT_SECRET=<your-jwt-secret> \
  -p 8080:8080 \
  tobiaswaslowski/open-mood-tracker:latest
```

### Running in Kubernetes

For Kubernetes, a Helm chart is provided. You can install it with the following command:

```bash
helm upgrade --install \
  --values ./environments/dev/postgres.values.yaml \
  --namespace mood-tracker --create-namespace \
  postgres oci://registry-1.docker.io/bitnamicharts/postgresql

helm upgrade --install \
  --set telegramToken="$TELEGRAM_TOKEN" \
  --values <your values.yaml> \
  --namespace mood-tracker \
  mood-tracker ./charts/open-mood-tracker
```