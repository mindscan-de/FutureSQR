/*
* This is a very poor implementation of the future bpe diff tools, but it has to start somewhere... 
* This will be rewritten a few times, whenever i tried another or better idea. I haven't seen this
* kind of approach anywhere - so the idea needs to be developed as well.
*
* Just in case you are offended by this code, I am certainly are, but pease excuse this poor code...
*
*/


export class BPEDiffUtils {
	
	public static readonly SYNDROME_UNCHANGED = "_";
	public static readonly SYNDROME_DELETED   = "D";
	public static readonly SYNDROME_INSERTED  = "I";
	public static readonly SYNDROME_REPLACED  = "R";
	
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
		return [];
	}
	
	private v1_bpeFindRelative( bpw_short_vector:number[], bpe_longer_vector:number[]): number[] {
		return [];
	}
}
