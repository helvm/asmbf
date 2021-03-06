package rave.pass;

import java.util.List;

import rave.common.ConstructBuilder;
import rave.construct.primitive.Minus;
import rave.nodes.INode;

public class MinusMergePass implements IPass {

	@Override
	public int match(List<INode> input) {
		int matches = 0;
		for(INode instr : input)
			if(instr instanceof Minus)
				matches++;
			else
				break;
		
		return matches;
	}

	@Override
	public INode build(List<INode> input) {
		int matches = 0;
		for(INode instr : input)
			if(instr instanceof Minus)
				matches++;
			else
				break;
		
		return new ConstructBuilder().type(Minus.class).coefficient(matches).build();
	}

}
