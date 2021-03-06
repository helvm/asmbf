package rave.pass;

import java.util.List;

import rave.common.ConstructBuilder;
import rave.construct.primitive.Plus;
import rave.nodes.INode;

public class PlusMergePass implements IPass {

	@Override
	public int match(List<INode> input) {
		int matches = 0;
		for(INode instr : input)
			if(instr instanceof Plus)
				matches++;
			else
				break;
		
		return matches;
	}

	@Override
	public INode build(List<INode> input) {
		int matches = 0;
		for(INode instr : input)
			if(instr instanceof Plus)
				matches++;
			else
				break;
		
		return new ConstructBuilder().type(Plus.class).coefficient(matches).build();
	}

}
