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

import { Tokens16K } from './tokens16-k'; 

export class SimpleBPEEncoder {

	public static readonly OOV_MAX = 1000000000;
	private __bpeEncoderTable:Map<string,number> = new Map<string,number>();
	
	constructor() {
		this.__bpeEncoderTable = new Map<string, number>(Tokens16K.TOKEN_TO_INDEX);
	}

	public encode( tokens : string[] ):number[] {
		let encoded_tokens:number[] = [];
		
		for( let token of tokens ) {
			let more_tokens: number[] = this.__buildBpeTokens(token)
			encoded_tokens.push(...more_tokens);
		}
		
		return encoded_tokens;
	}
	
	// Not nice, Not optimized, but maybe good enough?
	private __buildBpeTokens( textToken:string ): number[] {
		// we don't' need to transform a token, when it already has an unique encoding in the bpeEncoderTable
		// then looking up this token is absolutely sufficient
		if( this.__bpeEncoderTable.has(textToken) ) {
			return [ this.__bpeEncoderTable.get(textToken) ];
		}
		
		// We spread the string out into strings consisting of one character each.
		let bpe_text_tokens: string[] = Array.from(textToken);

		// Here we implement Philip Gage's algorithm.
		while(true) {
			// actually there should be no reason to repeat the calculation over and over, because only
			// the values next to the replaced bpe_text tokens change, and require index recalculation.
			// But I will leave that for another day of runtime improvement.
			// also if we don't find a combination, we can skip this index lookup until one of the 
			// neighbour elements changed. - doing this calculation can still be improved. Also we should
			// have a list of most probable byte pairs, and if integrity of order is not violated by new 
			// indicies, we just continue with replacement until we run out of substitutions or until we
			// violate the order of replacements. (I already implemented this once in the FluentGenesis 
			// project when building the vocabulary.
			
			// a bit clumsy, but good enough for now, we search for the mostFrequentBytePair, by comparing the 
			// indicies of each combined Text token, they are already sorted in terms of priority speaking.
			let indexOfMostFrequentBytePair = SimpleBPEEncoder.OOV_MAX;
			let mostFrequentBytePair:string = "";
			for(let i:number = 0;i<bpe_text_tokens.length-1;i++){
				let combinedTextToken = bpe_text_tokens[i] + bpe_text_tokens[i+1];
				if(this.__bpeEncoderTable.has(combinedTextToken)) {
					let indexOfCombinedTextToken = this.__bpeEncoderTable.get(combinedTextToken);
					if (indexOfCombinedTextToken < indexOfMostFrequentBytePair) {
						indexOfMostFrequentBytePair = indexOfCombinedTextToken;
						mostFrequentBytePair = combinedTextToken;
					}
				}
			}
			
			// if we didn't found anything to combine, we stop this encoding step.
			if( indexOfMostFrequentBytePair == SimpleBPEEncoder.OOV_MAX) {
				break;
			}
			
			// we now replace the two adjacent bpe_text_tokens, with the most frequent byte pair, 
			// and build a new array.
			let replaced_bpe_text_tokens:string[] = [];
			for(let i:number = 0;i<bpe_text_tokens.length-1;i++){
				let combinedTextToken = bpe_text_tokens[i] + bpe_text_tokens[i+1];
				if( combinedTextToken == mostFrequentBytePair) {
					replaced_bpe_text_tokens.push(mostFrequentBytePair);
					// skip the next text token, because it is now part of the repaced token.
					i=i+1;
				}
				else {
					replaced_bpe_text_tokens.push(bpe_text_tokens[i]);
				}

				// if this is the last position, we must not forget the very last element in the list to copy over
				// because the for loop only runs until the element before last.
				if(i == bpe_text_tokens.length-2) {
					replaced_bpe_text_tokens.push(bpe_text_tokens[i+1]);
				}
			}
			
			// replace the tokenlist, with the one where we have the combined elements
			bpe_text_tokens = replaced_bpe_text_tokens;
		}
		
		
		// convert from from string tokens to bpe-tokens		
		let bpe_tokens: number[] = []
		for(let bpe_text_token of bpe_text_tokens) {
			bpe_tokens.push(this.__bpeEncoderTable.get(bpe_text_token))
		}
		
		return bpe_tokens 
	}
}
