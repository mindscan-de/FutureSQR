export class CurrentBackendUserCapabilities {
	roles: string[] = [];
	featureflags: Map<string, boolean> = new Map<string, boolean>();
}
