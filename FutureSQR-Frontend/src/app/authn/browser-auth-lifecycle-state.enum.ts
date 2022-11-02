export enum BrowserAuthLifecycleState {
	// The browser auth lifecylcle state only lives in Javascript life time in some kind of singleton 
	
	// default state after "F5" or page reload
	None,
	
	// has preauthentication csfr token available
	PreAuthenicated,
	
	// after a successful reAuthentication
	ReAuthenticated,
	
	// TODO later, ask for a password in case of dangerous operations, in case we are only ReAuthenticated
	// We are fully authenticated in case we provided full proof of username / password / preauth token 
	// and/or some kind of challenge / response  - SAML / OAuth?
	FullyAuthenticated
}
