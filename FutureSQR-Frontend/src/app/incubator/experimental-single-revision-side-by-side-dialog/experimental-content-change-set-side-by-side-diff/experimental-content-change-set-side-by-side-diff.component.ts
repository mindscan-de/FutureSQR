import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { first, skipWhile } from 'rxjs/operators';

import { SimpleBPEEncoder } from '../../bpe/simple-bpe-encoder';
import { BpeEncoderProviderService } from '../../bpe/bpe-encoder-provider.service';
import { BPEDiffUtils } from '../../bpe/bpe-diff-utils';

@Component({
  selector: 'app-experimental-content-change-set-side-by-side-diff',
  templateUrl: './experimental-content-change-set-side-by-side-diff.component.html',
  styleUrls: ['./experimental-content-change-set-side-by-side-diff.component.css']
})
export class ExperimentalContentChangeSetSideBySideDiffComponent implements OnInit {
	
	public leftContent : ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel("",1); 
	public rightContent : ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel("",1);
	
	// make the editor readonly
	public readOnly:boolean = true;
	public viewPortMargin:number = 1;
	
	private bpeEncoder : SimpleBPEEncoder;
	private bpeDiffUtils = new BPEDiffUtils();
	
	// TODO: create a ui model from it
	// actually this will an intermediate external model
	@Input() contentChangeSet:string[] =[];

	constructor(
		// Actually i need an encoder..., maybe using a service...
		provider:BpeEncoderProviderService
	) {
		if(provider.hasBPEEncoder()) {
			this.bpeEncoder = provider.getBPEEncoder();
		}
		else {
			provider.subscribeBPEEncoder().pipe(skipWhile( v => !v )).pipe(first()).subscribe(
				data => {
					this.onBPEEncoderAvailable(data);
					},
				error => {
					
				}
			);
		}
	}

	ngOnInit(): void {
	}
	
	onBPEEncoderAvailable(encoder:SimpleBPEEncoder):void {
		console.log ("we got informed....")
		this.bpeEncoder = encoder;
		
		this.updateDiffRendering();
	}
	
 	ngOnChanges(changes: SimpleChanges): void {
		if(changes.contentChangeSet != undefined) {
			let contentChangeSetCurrent:string[] = changes.contentChangeSet.currentValue;
			// This needs to be reworked such that the line numbers are correctly transferred.
			this.leftContent = this.filterLeftDiff(contentChangeSetCurrent, 12);
			this.rightContent = this.filterRightDiff(contentChangeSetCurrent, 15);
			
			this.updateDiffRendering();
		}
	}
	
	private filterLeftDiff(linediff: string[], left_line_count_start: number) : ExperimentalUiDiffContentModel {
		let leftdiff = linediff.filter(line => !line.startsWith("+")).join("\n");
		
		let result:ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel(leftdiff, left_line_count_start);
		
		// we are interested in encoding these to ints.
		let __test = linediff.filter(line => line.startsWith("-"));

		if(__test && __test.length>0) {
			console.log(__test[0]);
			console.log(typeof __test[0]);
			
			if(this.bpeEncoder) {
				console.log(this.bpeEncoder.encode([__test[0]]));
			}
			else
			{
				console.log("BPEEncoder not ready.");
			}
		}
		
			
		return result; 
	}
	
	private filterRightDiff(linediff: string[], right_line_count_start: number) : ExperimentalUiDiffContentModel {
		let rightdiff = linediff.filter(line => !line.startsWith("-")).join("\n");
		
		let result:ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel(rightdiff,right_line_count_start);
		
		return result; 
	}

	private updateDiffRendering():void {
		
	}
	/**
	
	Algorithmic considerations:
	
	* No minus on the left side - only additions done on the right side (may still come from some other 
	  ContentChangeSet or File Changeset) -> mark each line as added
    * No plus on the right side - only deletions done on the left side (may still be to be found in other
      ContentChangesets or File changesets) -> mark each line as removed

	* each line should have a resolve state (Moved, Added, Deleted, Modified, WSOnly)
	* each line should have a source info (line, file) / multiple sources possible? / line range
	* each line should have a destination info (line, file) / multiple destinations possible? / line range
	
	* also try to resolve in blocks to reduce complexity
    
    * try to do a line by line match left/right using cooccurence matrix.
    * try to resolve order - maybe just some lines were moved around
	* first solve locally
	* then solve in file. report which parts could be matched
	* then resolve in commit. (e.g. extract method / move method to other class)
	* be whitespace resistant
	* calculate minimal diff between two matched lines - create left side / create right side highlights 
	* Try to explain the diff. e.g where which fragment comes from.
	* linesplits, line merges are interesting e.g. method arguments, method parameters,
	* inserts and split/merges
	* try solving misaligned "close brackets" - git-algorithm problem. - always the "wrong" brackets are aligned
      (git likely tried to solve the merge conflict errors)
	* try to figure out if only whitespace mismatch / e.g. code was indented / unindented
	* try to resolve one line and mark one line char by char as added/deleted/modified/unmodified/yet undecided
	* use own changes as source for test cases collect revisions for testcases 


	* The real question is, can this be trained in an ML model?
	* maybe something BPE like? BPE is slow, but is it slow when Byte pairs are already available? and if byte pairs
	* are calculated over time for the given source code?
	* this maybe will create useful tokens, such that the context can be preserved and the tokens can be grouped.
	 */

}

export class ExperimentalUiDiffContentModel {
	public diffContent: string = "";
	public diffLineNumberStart: number = 1;
	
	constructor( content:string, start:number) {
		this.diffContent = content;
		this.diffLineNumberStart = start;
	}
}
