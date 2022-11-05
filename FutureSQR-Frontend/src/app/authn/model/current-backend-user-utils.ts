import { CurrentBackendUser }  from './current-backend-user';

export class CurrentBackendUserUtils {
	
	isValid(backendUser:CurrentBackendUser):boolean {
		// TODO combine with other metrics as well..
		return this.isUUIDValid(backendUser);
	}
	
	isUUIDValid(backendUser:CurrentBackendUser):boolean {
		return backendUser.uuid!=undefined && backendUser.uuid!=null && backendUser.uuid != "";
	}

}
