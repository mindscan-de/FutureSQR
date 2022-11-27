import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map, first } from 'rxjs/operators';

import { SimpleBPEEncoder } from './simple-bpe-encoder';

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BpeEncoderProviderService {
	
	private bpeEncoder:SimpleBPEEncoder;

	constructor(
		private httpClient : HttpClient	
	) { }
	
	public hasBPEEncoder():boolean {
		if(this.bpeEncoder == undefined || this.bpeEncoder == null) {
			return false;
		}
		return true;
	}
	
	public getBPEEncoder():SimpleBPEEncoder {
		if(this.hasBPEEncoder()) { 
			return this.bpeEncoder;
		}
	}
	
	public initialize() : void {
		this.loadTokenMap().subscribe( 
			data => {},
			error => {}
		)
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

