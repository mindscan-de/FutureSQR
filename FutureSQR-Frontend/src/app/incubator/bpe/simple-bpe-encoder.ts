/*
 * For the diff I would like to test, whether building the diff via 
 * BytePairEncoding is able to provide some readability.  
 *
 * I calculated some BPE tables for a general java corpus, some time back.
 * i think i will use them for testing - since i wrote that in python, i
 * should go for a jupyter notebook first and then and only then for a
 * javascript implementation, which is also temporary?
 * you can find the 10k token BPE dataset in the FluentGenesis-Classifier
 * project and also an encoder for this. ( which is actually quite slow... )
 * but i would say, it is god enough right now.
 * 
 * time for some experiments offline....
 *
 * -----
 * The more i test this idea ob using byte pair encoding for the diff analysis and diff presentation, the more i
 * become convinced this is the right way to do it.
 * -----
 * 
 */

export class SimpleBPEEncoder {

	private __bpeEncoderTable:Map<string,number> = new Map<string,number>();
	
	constructor() {
		
	}

	public encode( tokens : string[] ):number[] {
		let encoded_tokens:number[] = [];
		
		for( let token of tokens ) {
			let more_tokens: number[] = this.__buildBpeTokens(token)
			encoded_tokens.push(...more_tokens);
		}
		
		return encoded_tokens;
	}
	
	
	private __buildBpeTokens( textToken:string ): number[] {
		// we don't' need to transform a token, when it already has an unique encoding in the bpeEncoderTable
		// then looking up this token is absolutely sufficient
		if( this.__bpeEncoderTable.has(textToken) ) {
			return [ this.__bpeEncoderTable.get(textToken) ];
		}
		
		// TODO do the real fun part here.
		
		return []
	}
}
