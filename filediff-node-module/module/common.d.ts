export type Message = Array<string | Uint8Array | Uint16Array>

export interface Channel {
  sendMessage(message: Message): void;
  onMessage?: (message: Message) => void;
}

export interface ChannelTestApi {
  foo(): void;
}

export function newRemoteChannelTest(channel: Channel): Promise<ChannelTestApi>
