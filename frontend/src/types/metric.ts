export interface MetricType {
  id: number;
  name: string;
  description: string;
  labels: Label[];
  minValue: number;
  maxValue: number;
  defaultValue: number;
  defaultMetric: boolean;
  ownerId: number;
  sortOrder: string; // ASC | DESC

  getDefaultValueAsString(): string;
}

interface Label {
  label: string;
  value: number;
}

export class Metric implements MetricType {
  id: number;
  name: string;
  description: string;
  labels: Label[];
  minValue: number;
  maxValue: number;
  defaultValue: number;
  defaultMetric: boolean;
  ownerId: number;
  sortOrder: 'ASC' | 'DESC';

  constructor(
    id: number,
    name: string,
    description: string,
    labels: Label[],
    minValue: number,
    maxValue: number,
    defaultValue: number,
    defaultMetric: boolean,
    ownerId: number,
    sortOrder: 'ASC' | 'DESC'
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.labels = labels;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.defaultValue = defaultValue;
    this.defaultMetric = defaultMetric;
    this.ownerId = ownerId;
    this.sortOrder = sortOrder;
  }

  getDefaultValueAsString = (): string => {
    return (
      this.labels.find((label) => label.value === this.defaultValue)?.label ||
      this.defaultValue.toString()
    );
  };
}
