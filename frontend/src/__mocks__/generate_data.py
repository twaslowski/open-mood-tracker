import json
import random
from datetime import datetime, timedelta

# Load the initial data
initial_data = [
  {
    "timestamp": "2025-04-01T00:00:00.000",
    "datapoints": [
      {
        "metricId": 1,
        "metricName": "Mood",
        "metricDescription": "Daily mood score",
        "minValue": -3,
        "maxValue": 3,
        "baselineValue": 0,
        "datapointValue": 1,
        "labels": []
      },
      {
        "metricId": 2,
        "metricName": "Stress",
        "metricDescription": "Daily stress level",
        "minValue": 0,
        "maxValue": 5,
        "baselineValue": 1,
        "datapointValue": 1,
        "labels": []
      },
      {
        "metricId": 3,
        "metricName": "Sleep",
        "metricDescription": "Hours of sleep",
        "minValue": 4,
        "maxValue": 12,
        "baselineValue": 8,
        "datapointValue": 8,
        "labels": []
      }
    ]
  }
]

# Function to generate random datapoints
def generate_data(initial_data, days):
  generated_data = []
  start_date = datetime.fromisoformat(initial_data[0]["timestamp"])

  for day in range(days):
    current_date = start_date + timedelta(days=day)
    new_entry = {
      "timestamp": current_date.isoformat(),
      "datapoints": []
    }
    for datapoint in initial_data[0]["datapoints"]:
      new_datapoint = datapoint.copy()
      new_datapoint["datapointValue"] = random.randint(datapoint["minValue"], datapoint["maxValue"])
      new_entry["datapoints"].append(new_datapoint)
    generated_data.append(new_entry)

  return generated_data

# Generate 30 days of data
new_data = generate_data(initial_data, days=90)

# Save to a JSON file
with open("generated_graph_data.json", "w") as f:
  json.dump(new_data, f, indent=2)

print("Data generation complete. Saved to 'generated_graph_data.json'.")