export enum UserAuthLifecycleState {
	// The current user auth lifecycle state belongs into the local storage, because we want to maintain it
	// over browser restarts / or lets say longer times.
	
	// starting state, without using the username
	None,
	
	// when the user was authenticated using login / in this state the user can use reauthenticate as a method.
	// in case of a new javascript lifecylce
	LoggedIn,
	
	// when the user was logged out, the user must use login to reach LoggedIn State
	// TODO: useful? we may remember that this particular user logged out?
	LoggedOut
}
