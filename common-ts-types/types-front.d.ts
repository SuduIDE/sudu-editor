import { IDisposable } from "./types";

export interface Uri {
  scheme?: string,
  authority?: string,
  path?: string
}

export interface IEvent<T> {
  /**
   * Registers disposable event callback
   *
   * When event is fired, a new value V will be delivered to all listeners that were registered at the moment when the value V processing started.
   * It means that if listener A disposes listener B during processing of value V, listener B will still receive value V.
   * @param listener callback to be called when event is fired
   */
  (listener: (e: T) => any): IDisposable;
}
