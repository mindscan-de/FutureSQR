/*
* This is a very poor implementation of the future bpe diff tools, but it has to start somewhere... 
* This will be rewritten a few times, whenever i tried another or better idea. I haven't seen this
* kind of approach anywhere - so the idea needs to be developed as well.
*
* Just in case you are offended by this code, I am certainly are, but pease excuse this poor code...
*
*/


export class BPEDiffUtils {
	
	public static readonly SYNDROME_UNCHANGED:string = "_";
	public static readonly SYNDROME_DELETED:string   = "D";
	public static readonly SYNDROME_INSERTED:string  = "I";
	public static readonly SYNDROME_REPLACED:string  = "R";
	
	public static readonly RELATIVE_POSITION_UNKNOWN:number = undefined; 
	
	public bpeCalculateDiffSyndrome(bpe_left_diff : number[], bpe_right_diff : number[]):string[] {
		if (bpe_left_diff.length != bpe_right_diff.length) {
			throw new Error("can not calculate syndromes for different array lengths");
		}
		
		let syndrome = [];

		for(let i:number=0;i<bpe_left_diff.length;i++) {
			if(bpe_left_diff[i] == bpe_right_diff[i]) {
				syndrome.push(BPEDiffUtils.SYNDROME_UNCHANGED);
			}
			else if( bpe_left_diff[i] == 0 ) {
				syndrome.push(BPEDiffUtils.SYNDROME_INSERTED);
			}
			else if( bpe_right_diff[i] == 0) {
				syndrome.push(BPEDiffUtils.SYNDROME_DELETED);
			}
			else {
				syndrome.push(BPEDiffUtils.SYNDROME_REPLACED);
			}
		}

		return syndrome;
	}
	
	/*
	* Actually it is not enough to stretchout one array, but to stretch out both arrays.
	* This algorithm fails for some cases, where parts of the left and right line are 
	* moved around.
	*/
	public bpeStretchoutDiff(bpe_left_diff:number[], bpe_right_diff:number[]):number[][] {
		if(bpe_left_diff.length == bpe_right_diff.length) {
			return [ bpe_left_diff, bpe_right_diff ];
		}
		else if(bpe_left_diff.length < bpe_right_diff.length) {
			return [ this.v1_bpeStretchout(bpe_left_diff, this.v1_bpeFindRelative(bpe_left_diff, bpe_right_diff)) , bpe_right_diff ]
		}
		else {
			return [ bpe_left_diff, this.v1_bpeStretchout( bpe_right_diff, this.v1_bpeFindRelative(bpe_right_diff, bpe_left_diff)) ]
		}
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
				for(let j=0;j<(bpe_relative_findings[i]-delta_offset);i++) {
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
		}		 
		
		return findings;
	}
}
