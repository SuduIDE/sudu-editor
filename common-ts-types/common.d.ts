export interface IDisposable {
  dispose(): void
}

export const enum LogLevel {
  OFF = 0,
  FATAL = 1,
  ERROR = 2,
  WARN = 3,
  INFO = 4,
  DEBUG = 5,
  TRACE = 6
}

export function setLogLevel(logLevel: LogLevel): void;

export function setLogOutput(logHandler: (logLevel: LogLevel, text: string)=>void): void;

