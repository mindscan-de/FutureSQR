/*
** This is a very poor implementation of the future bpe diff tools, but it has to start somewhere... 
** This will be rewritten a few times, whenever i tried another or better idea. I haven't seen this
** kind of approach anywhere - so the idea needs to be developed as well.
**
** Just in case you are offended by this code, I am certainly are, but pease excuse this poor code...
**
** Next Challenge is to calculate this diff better.
** http://localhost:4200/futuresqr/revision/72e55437b8d4ab40c1663ff4ee98b3ddf4937517
*/

import { BpeDiffSyndrome } from './bpe-diff-syndrome.enum';


export class BPEDiffUtils {
	
	public static readonly RELATIVE_POSITION_UNKNOWN:number = undefined;
	
	/*
	** This calculates the syndrome between two bpe encoded vectors. These two bpe vectors must have the same length
	** and both vectors must be aligned optimally to each other.
	**
	*/
	public bpeCalculateDiffSyndrome(bpe_left_diff : number[], bpe_right_diff : number[]):BpeDiffSyndrome[] {
		if (bpe_left_diff.length != bpe_right_diff.length) {
			console.log("left");
			console.log(bpe_left_diff);
			console.log("right");
			console.log(bpe_right_diff);
			throw new Error("can not calculate syndromes for different array lengths");
		}
		
		let syndrome = [];

		for(let i:number=0;i<bpe_left_diff.length;i++) {
			syndrome.push(this.calculateElementDiffSyndrome(bpe_left_diff[i],bpe_right_diff[i]));
		}

		return syndrome;
	}
	
	private calculateElementDiffSyndrome(leftelement:number, rightelement:number):BpeDiffSyndrome {
		if(leftelement == rightelement) {
			return BpeDiffSyndrome.Unchanged;
		}
		
		if(leftelement == 0) {
			return BpeDiffSyndrome.Inserted;
		}
		
		if(rightelement == 0) {
			return BpeDiffSyndrome.Deleted;
		}
		
		return BpeDiffSyndrome.Replaced;
	} 
	
	/*
	** This calculates a "dot product" between two bpe_encoded vectors. The idea behind this is to provide
	** a possibility to detect which lines of the left diff match up with the lines in the right diff to
	** properly detect extract mathod operations and to calculate the difference for an extracted method.
	** (But this matching (Across ContentChangesets and across FileChangesets) will be calculated later).
	*/
	public bpeSimilarity(bpe_left_diff:number[], bpe_right_diff:number[]):number {
		let similarity:number = 0;
		
		let left_map:Map<[number, number], number> = this.bpeCooccurenceMatrix(bpe_left_diff);
		let right_map:Map<[number, number], number> = this.bpeCooccurenceMatrix(bpe_right_diff);
		
		// actually we want to look whether every pair of the short vector is contained in the longer vector
		// and actually the reverse way too, how much of the longer vector is part of the shorter vector
		// tokens with a higher index number should have a higher overall weight, because they are 
		// increasingly rare.
		
		return  similarity
	}
	
	/*
	** This Co-Occurrence matrix calculation stores basically two calculations, self occurrence 
	** is stored by creating a tuple with Zero for the second element, because the bpe_encoder
	** doesn't produce the Zero token.
	*/
	private bpeCooccurenceMatrix (bpe_encoded:number[]) : Map<[number, number], number> {
		let resultMap:Map<[number, number], number> = new Map<[number,number],number>();
		
		if(bpe_encoded.length==0) {
			return resultMap;
		}
		
		for(let i:number = 0; i<bpe_encoded.length-1;i++) {
			// neigbours
			this.bpeAccumulate([bpe_encoded[i] , bpe_encoded[i+1]],resultMap);
			// self
			this.bpeAccumulate([bpe_encoded[i] , 0],resultMap);
		}
		
		// also process last self element here.
		this.bpeAccumulate([bpe_encoded[bpe_encoded.length-1],0], resultMap);
		
		return resultMap
	}
	
	private bpeAccumulate(element:[number,number], accumulatorMap: Map<[number, number], number>) : void {
		if(accumulatorMap.has(element)) {
			accumulatorMap.set(element, 1 + accumulatorMap.get(element));
		}
		else {
			accumulatorMap.set(element, 1);
		}
	}
	
	/*
	** Actually it is not enough to stretchout one array, but to stretch out both arrays.
	** This algorithm fails for some cases, where parts of the left and right line are 
	** moved around.
	*/
	public bpeStretchoutDiff(bpe_left_diff:number[], bpe_right_diff:number[]):[number[],number[]] {
		if(bpe_left_diff.length == bpe_right_diff.length) {
			return [ bpe_left_diff, bpe_right_diff ];
		}
		else if(bpe_left_diff.length < bpe_right_diff.length) {
			let stretched_left:number[] = this.v1_bpeStretchout(bpe_left_diff, this.v1_bpeFindRelative(bpe_left_diff, bpe_right_diff))
			// after stretching these may still not be same length: so we must adapt either of these
			return this.bpeExtendToMax(stretched_left , bpe_right_diff)
		}
		else {
			let stretched_right = this.v1_bpeStretchout( bpe_right_diff, this.v1_bpeFindRelative(bpe_right_diff, bpe_left_diff))
			// after stretching these may still not be same length: so we must adapt either of these
			return this.bpeExtendToMax( bpe_left_diff , stretched_right )
		}
	}
	
	private bpeExtendToMax(left:number[], right:number[]):[number[], number[]] {
		if(left.length == right.length) {
			return [left, right]
		}
		
		let maxSize = Math.max(left.length, right.length);
		
		if(left.length!=maxSize) {
			return [this.extendArray(left, maxSize),right];
		}
		else {
			return [left, this.extendArray(right, maxSize)]
		}
	}
	
	private extendArray(input:number[], newLength:number):number[] {
		let extended = new Array<number>(...input);
		
		for(let i:number=newLength-extended.length; i>0;i--) {
			extended.push(0);
		}
		
		return extended;
	}
	
	private v1_bpeStretchout( bpe_short_vector:number[], bpe_relative_findings:number[]): number[] {
		let delta_offset = 0;
		let stretched: number[] = [];

		for(let i:number = 0; i<bpe_short_vector.length;i++) {
			// actually we always push, so this can be extracted...
			if(bpe_relative_findings[i]==undefined) {
				stretched.push(bpe_short_vector[i]);
			}
			else if (bpe_relative_findings[i]<delta_offset) {
				stretched.push(bpe_short_vector[i]);
			}
			else {
				for(let j=0;j<(bpe_relative_findings[i]-delta_offset);j++) {
					stretched.push(0);
				}
				stretched.push(bpe_short_vector[i]);
	            // this is actually bad in case things were moved around...
				delta_offset = bpe_relative_findings[i]; 
			}
		}
		
		return stretched;
	}
	
	private v1_bpeFindRelative( bpe_short_vector:number[], bpe_longer_vector:number[]): number[] {
		let findings:number[] = new Array<number>(bpe_short_vector.length);

		for(let i:number=0; i<bpe_short_vector.length;i++) {
			let element = bpe_short_vector[i];
			if(bpe_longer_vector.includes(element)) {
				// actually we should consider multiple findings....
				// But maybe we should consider tracking unique elements first and the build around them...
				let index_inLong = bpe_longer_vector.indexOf(element,i)-i;
		        findings[i]=index_inLong;
			}
			else {
				findings[i]=undefined;
			}
		}
		
		console.log(findings);
		
		return findings;
	}
	
	
	public bpe_prepare_syndrome_unified(syndrome:BpeDiffSyndrome[], left_line_tokens:number[], right_line_tokens:number[]):BPEVisualizationEntry[] {
		let unified_visulalization_entry:BPEVisualizationEntry[] = []

		let latest_syndrome:BpeDiffSyndrome = undefined;
		let stashed_left_tokens:number[] = [];
		let stashed_right_tokens:number[] = [];
		
		for(let i:number = 0;i<syndrome.length;i++) {
			if(latest_syndrome != syndrome[i]) {
				switch(latest_syndrome) {
					case BpeDiffSyndrome.Unchanged: {
						unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Unchanged, stashed_left_tokens ));
						break;
					}
					case BpeDiffSyndrome.Deleted: {
						unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Deleted, stashed_left_tokens ));
						break;
					}
					case BpeDiffSyndrome.Inserted: {
						unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Inserted, stashed_right_tokens ));
						break;
					}
					case BpeDiffSyndrome.Replaced: {
						// actually this is not cool in case the next syndrome is adjacent to a deletion or insertion
						// for readability. - anyways it is like it is now.
						unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Deleted, stashed_left_tokens ));
						unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Inserted, stashed_right_tokens ));
						break;
					}
				}
				
				// update syndrome
				latest_syndrome = syndrome[i];
				// cleanup token stashes after emitting
				stashed_left_tokens = [];
				stashed_right_tokens = [];
			}
			
			// depending on current syndrome, we want to handle the different stashes...
			switch(syndrome[i]) {
				case  BpeDiffSyndrome.Unchanged: {
					stashed_left_tokens.push(left_line_tokens[i]);
					break;
				}
				case BpeDiffSyndrome.Deleted: {
					stashed_left_tokens.push(left_line_tokens[i]);
					break;
				}
				case BpeDiffSyndrome.Inserted: {
					stashed_right_tokens.push(right_line_tokens[i]);
					break;
				}
				case BpeDiffSyndrome.Replaced: {
					stashed_left_tokens.push(left_line_tokens[i]);
					stashed_right_tokens.push(right_line_tokens[i]);
					break;
				}
			}
		}

		switch(latest_syndrome) {
			case BpeDiffSyndrome.Unchanged: {
				unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Unchanged, stashed_left_tokens ));
				break;
			}
			case BpeDiffSyndrome.Deleted: {
				unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Deleted, stashed_left_tokens ));
				break;
			}
			case BpeDiffSyndrome.Inserted: {
				unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Inserted, stashed_right_tokens ));
				break;
			}
			case BpeDiffSyndrome.Replaced: {
				unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Deleted, stashed_left_tokens ));
				unified_visulalization_entry.push(new BPEVisualizationEntry (BpeDiffSyndrome.Inserted, stashed_right_tokens ));
				break;
			}
		}
				
		return unified_visulalization_entry;
	}
}

export class BPEVisualizationEntry {
	public syndrome:string;
	public tokens:number[];
	
	constructor(syndrome:BpeDiffSyndrome, tokens:number[]) {
		this.syndrome = syndrome.toString();
		this.tokens = tokens;
	}
}