import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, first } from 'rxjs/operators';

import { SimpleBPEEncoder } from './simple-bpe-encoder';

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BpeEncoderProviderService {
	
	private _currentBpeEncoderValue:SimpleBPEEncoder;
	
	private _currentBpeEncoderSubject: BehaviorSubject<SimpleBPEEncoder>;
	private currentBpeEncoderSubject: Observable<SimpleBPEEncoder>;

	constructor(
		private httpClient : HttpClient	
	) { 
		this._currentBpeEncoderSubject = new BehaviorSubject<SimpleBPEEncoder>(undefined);
		this.currentBpeEncoderSubject = this._currentBpeEncoderSubject.asObservable();
		
		this.initialize();
	}
	
	public hasBPEEncoder():boolean {
		if(this._currentBpeEncoderValue == undefined || this._currentBpeEncoderValue == null) {
			return false;
		}
		return true;
	}
	
	public getBPEEncoder():SimpleBPEEncoder {
		if(this.hasBPEEncoder()) { 
			return this._currentBpeEncoderValue;
		}
	}
	
	public initialize() : void {
		this.loadTokenMap().subscribe( 
			data => {
				this.onBPETokenMapProvided(data);
			},
			error => {}
		)
	}
	
	public subscribeBPEEncoder():Observable<SimpleBPEEncoder> {
		return this.currentBpeEncoderSubject;
	}
	
	private onBPETokenMapProvided(tokenMap: BPETokenMap) : void {
		let encoderTokenMap = new Map<string,number>(Object.entries(tokenMap.tokens));
		
		this._currentBpeEncoderValue = new SimpleBPEEncoder(encoderTokenMap);
		this._currentBpeEncoderSubject.next(this._currentBpeEncoderValue); 
	}
	
	private loadTokenMap() : Observable<BPETokenMap> {
		return this.httpClient
				.get<BPETokenMap>("/assets/bpe16k/tokens.json")
				.pipe(first());
	}
}

export class  BPETokenMap {
	public tokens: Map<string,number> = new Map<string,number>();
} 

