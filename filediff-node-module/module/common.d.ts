type Message = Array<string | Uint8Array | Uint16Array>

export interface IDisposable {
    dispose(): void
}

export interface Channel {
    sendMessage(message: Message): void
    onMessage?: (message: Message) => void
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

export interface ChannelTestApi {
    foo() : void;
}

export function newRemoteChannelTest(channel: Channel): Promise<ChannelTestApi>
