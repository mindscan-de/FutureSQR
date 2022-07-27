import { BackendModelThreadsMessage } from './backend-model-threads-message';

export class BackendModelThreadsFullThread {
	public threadId: string = "";
	public authorId: string = "";
	public messagesId: string[] = [];
	public messages: Map<string,BackendModelThreadsMessage> = new Map<string,BackendModelThreadsMessage>();
}
